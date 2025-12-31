package com.example.coursemanagement.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coursemanagement.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CourseRepository {

    private static final String TAG = "CourseRepository";

    private final FirebaseDatabase database;
    private final DatabaseReference coursesRef;

    public interface CoursesListener {
        void onCoursesUpdated(@NonNull List<Course> courses);
        void onError(@NonNull Exception e);
    }

    public interface OperationCallback<T> {
        void onSuccess(T result);
        void onError(@NonNull Exception e);
    }

    public CourseRepository() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.database = db;
        this.coursesRef = db.getReference("courses");
        try {
            Log.d(TAG, "CourseRepository initialized");
            Log.d(TAG, "Firebase database instance: " + database.getApp().getName());
            Log.d(TAG, "Courses reference path: " + coursesRef.getPath());
        } catch (Exception e) {
            Log.e(TAG, "Error initializing CourseRepository", e);
        }
    }

    @NonNull
    public ValueEventListener observeAllCourses(@NonNull final CoursesListener listener) {
        Log.d(TAG, "observeAllCourses: Starting to observe all courses");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Log.d(TAG, "observeAllCourses: onDataChange called with " + snapshot.getChildrenCount() + " children");
                    List<Course> courses = new ArrayList<>();

                    for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                        try {
                            Course course = courseSnapshot.getValue(Course.class);
                            if (course != null) {
                                String key = courseSnapshot.getKey();
                                course.setId(key != null ? key : "");
                                courses.add(course);
                                Log.d(TAG, "observeAllCourses: Added course: " + course.getCourseName() + " (ID: " + course.getId() + ")");
                            } else {
                                Log.w(TAG, "observeAllCourses: Failed to parse course from snapshot: " + courseSnapshot.getKey());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "observeAllCourses: Error parsing individual course snapshot: " + courseSnapshot.getKey(), e);
                        }
                    }

                    Collections.sort(courses, new Comparator<Course>() {
                        @Override
                        public int compare(Course o1, Course o2) {
                            long t1 = o1.getTimestamp();
                            long t2 = o2.getTimestamp();
                            // Descending by timestamp
                            return Long.compare(t2, t1);
                        }
                    });

                    Log.d(TAG, "observeAllCourses: Sending " + courses.size() + " courses to UI");
                    listener.onCoursesUpdated(courses);
                } catch (Exception e) {
                    Log.e(TAG, "observeAllCourses: Error in onDataChange", e);
                    listener.onError(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "observeAllCourses: Database listener cancelled", error.toException());
                listener.onError(error.toException());
            }
        };

        coursesRef.addValueEventListener(valueEventListener);
        Log.d(TAG, "observeAllCourses: Value event listener added");

        return valueEventListener;
    }

    public void removeCoursesListener(@NonNull ValueEventListener listener) {
        try {
            Log.d(TAG, "removeCoursesListener: Removing value event listener");
            coursesRef.removeEventListener(listener);
        } catch (Exception e) {
            Log.e(TAG, "removeCoursesListener: Error removing listener", e);
        }
    }

    public void addCourse(@NonNull final Course course, @NonNull final OperationCallback<String> callback) {
        try {
            Log.d(TAG, "addCourse: Starting to add course: " + course.getCourseName());

            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                Log.w(TAG, "addCourse: Course name is blank");
                callback.onError(new Exception("Course name cannot be empty"));
                return;
            }

            if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
                Log.w(TAG, "addCourse: Course code is blank");
                callback.onError(new Exception("Course code cannot be empty"));
                return;
            }

            if (course.getCreditHours() <= 0) {
                Log.w(TAG, "addCourse: Invalid credit hours: " + course.getCreditHours());
                callback.onError(new Exception("Credit hours must be greater than 0"));
                return;
            }

            String key;
            try {
                key = coursesRef.push().getKey();
            } catch (Exception e) {
                Log.e(TAG, "addCourse: Error generating Firebase key", e);
                callback.onError(new Exception("Failed to generate course ID: " + e.getMessage(), e));
                return;
            }

            if (key == null) {
                Log.e(TAG, "addCourse: Generated key is null");
                callback.onError(new Exception("Failed to generate course ID - key is null"));
                return;
            }

            Log.d(TAG, "addCourse: Generated key: " + key);

            course.setId(key);
            course.setTimestamp(System.currentTimeMillis());

            Log.d(TAG, "addCourse: Setting course data in Firebase");
            coursesRef.child(key)
                    .setValue(course)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "addCourse: Course added successfully with ID: " + key);
                        callback.onSuccess(key);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "addCourse: Failed to add course: " + course.getCourseName(), e);
                        callback.onError(new Exception("Failed to add course: " + e.getMessage(), e));
                    });
        } catch (Exception e) {
            Log.e(TAG, "addCourse: Failed to add course: " + course.getCourseName(), e);
            callback.onError(new Exception("Failed to add course: " + e.getMessage(), e));
        }
    }

    public void updateCourse(@NonNull final Course course, @NonNull final OperationCallback<Void> callback) {
        try {
            Log.d(TAG, "updateCourse: Starting to update course: " + course.getCourseName() + " (ID: " + course.getId() + ")");

            if (course.getId() == null || course.getId().isEmpty()) {
                Log.w(TAG, "updateCourse: Course ID is empty");
                callback.onError(new Exception("Course ID cannot be empty"));
                return;
            }

            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                Log.w(TAG, "updateCourse: Course name is blank");
                callback.onError(new Exception("Course name cannot be empty"));
                return;
            }

            if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
                Log.w(TAG, "updateCourse: Course code is blank");
                callback.onError(new Exception("Course code cannot be empty"));
                return;
            }

            if (course.getCreditHours() <= 0) {
                Log.w(TAG, "updateCourse: Invalid credit hours: " + course.getCreditHours());
                callback.onError(new Exception("Credit hours must be greater than 0"));
                return;
            }

            coursesRef.child(course.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Log.w(TAG, "updateCourse: Course with ID " + course.getId() + " does not exist");
                        callback.onError(new Exception("Course not found with ID: " + course.getId()));
                        return;
                    }

                    Log.d(TAG, "updateCourse: Updating course data in Firebase");
                    coursesRef.child(course.getId()).setValue(course)
                            .addOnSuccessListener(aVoid -> {
                                Log.i(TAG, "updateCourse: Course updated successfully: " + course.getCourseName());
                                callback.onSuccess(null);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "updateCourse: Failed to update course: " + course.getCourseName(), e);
                                callback.onError(new Exception("Failed to update course: " + e.getMessage(), e));
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "updateCourse: Error checking if course exists", error.toException());
                    callback.onError(new Exception("Error verifying course existence: " + error.getMessage(), error.toException()));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "updateCourse: Failed to update course: " + course.getCourseName(), e);
            callback.onError(new Exception("Failed to update course: " + e.getMessage(), e));
        }
    }

    public void deleteCourse(@NonNull final String courseId, @NonNull final OperationCallback<Void> callback) {
        try {
            Log.d(TAG, "deleteCourse: Starting to delete course with ID: " + courseId);

            if (courseId.isEmpty()) {
                Log.w(TAG, "deleteCourse: Course ID is empty");
                callback.onError(new Exception("Course ID cannot be empty"));
                return;
            }

            coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Log.w(TAG, "deleteCourse: Course with ID " + courseId + " does not exist");
                        callback.onError(new Exception("Course not found with ID: " + courseId));
                        return;
                    }

                    Course course = snapshot.getValue(Course.class);
                    Log.d(TAG, "deleteCourse: Found course to delete: " + (course != null ? course.getCourseName() : "null"));

                    Log.d(TAG, "deleteCourse: Removing course from Firebase");
                    coursesRef.child(courseId).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Log.i(TAG, "deleteCourse: Course deleted successfully with ID: " + courseId);
                                callback.onSuccess(null);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "deleteCourse: Failed to delete course with ID: " + courseId, e);
                                callback.onError(new Exception("Failed to delete course: " + e.getMessage(), e));
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "deleteCourse: Error checking if course exists", error.toException());
                    callback.onError(new Exception("Error verifying course existence: " + error.getMessage(), error.toException()));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "deleteCourse: Failed to delete course with ID: " + courseId, e);
            callback.onError(new Exception("Failed to delete course: " + e.getMessage(), e));
        }
    }

    public void getCourseById(@NonNull final String courseId, @NonNull final OperationCallback<Course> callback) {
        try {
            Log.d(TAG, "getCourseById: Getting course with ID: " + courseId);

            if (courseId.isEmpty()) {
                Log.w(TAG, "getCourseById: Course ID is empty");
                callback.onError(new Exception("Course ID cannot be empty"));
                return;
            }

            coursesRef.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        Log.w(TAG, "getCourseById: Course with ID " + courseId + " does not exist");
                        callback.onSuccess(null);
                        return;
                    }

                    try {
                        Course course = snapshot.getValue(Course.class);
                        if (course != null) {
                            course.setId(courseId);
                            Log.d(TAG, "getCourseById: Successfully retrieved course: " + course.getCourseName());
                        } else {
                            Log.w(TAG, "getCourseById: Course data is null after parsing");
                        }
                        callback.onSuccess(course);
                    } catch (Exception e) {
                        Log.e(TAG, "getCourseById: Error parsing course data", e);
                        callback.onError(new Exception("Error parsing course data: " + e.getMessage(), e));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "getCourseById: Failed to get course with ID: " + courseId, error.toException());
                    callback.onError(new Exception("Failed to retrieve course: " + error.getMessage(), error.toException()));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "getCourseById: Failed to get course with ID: " + courseId, e);
            callback.onError(new Exception("Failed to retrieve course: " + e.getMessage(), e));
        }
    }
}
