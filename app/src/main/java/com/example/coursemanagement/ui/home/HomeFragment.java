package com.example.coursemanagement.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coursemanagement.R;
import com.example.coursemanagement.adapter.CourseAdapter;
import com.example.coursemanagement.databinding.FragmentHomeBinding;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.repository.CourseRepository;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private final CourseRepository courseRepository = new CourseRepository();
    private CourseAdapter courseAdapter;
    private ValueEventListener coursesListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreateView: Inflating HomeFragment");
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Error inflating layout", e);
            Toast.makeText(getContext(), "Error loading home screen: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d(TAG, "onViewCreated: Setting up HomeFragment");

            setupRecyclerView();
            setupClickListeners();
            observeCourses();
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: Error setting up view", e);
            showErrorToUser("Error setting up home screen", e);
        }
    }

    private void setupRecyclerView() {
        try {
            Log.d(TAG, "setupRecyclerView: Setting up RecyclerView");
            courseAdapter = new CourseAdapter(
                    course -> {
                        try {
                            Log.d(TAG, "Edit clicked for course: " + course.getCourseName());
                            navigateToEditCourse(course);
                        } catch (Exception e) {
                            Log.e(TAG, "Error handling edit click", e);
                            showErrorToUser("Error opening edit screen", e);
                        }
                    },
                    course -> {
                        try {
                            Log.d(TAG, "Delete clicked for course: " + course.getCourseName());
                            showDeleteConfirmationDialog(course);
                        } catch (Exception e) {
                            Log.e(TAG, "Error handling delete click", e);
                            showErrorToUser("Error processing delete request", e);
                        }
                    }
            );

            binding.recyclerViewCourses.setAdapter(courseAdapter);
            binding.recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
            Log.d(TAG, "setupRecyclerView: RecyclerView setup complete");
        } catch (Exception e) {
            Log.e(TAG, "setupRecyclerView: Error setting up RecyclerView", e);
            showErrorToUser("Error setting up course list", e);
        }
    }

    private void setupClickListeners() {
        try {
            Log.d(TAG, "setupClickListeners: Setting up click listeners");
            binding.fabAddCourse.setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Add course FAB clicked");
                    navigateToAddCourse();
                } catch (Exception e) {
                    Log.e(TAG, "Error handling add course FAB click", e);
                    showErrorToUser("Error opening add course screen", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "setupClickListeners: Error setting up click listeners", e);
            showErrorToUser("Error setting up buttons", e);
        }
    }

    private void observeCourses() {
        try {
            Log.d(TAG, "observeCourses: Starting to observe courses");

            coursesListener = courseRepository.observeAllCourses(new CourseRepository.CoursesListener() {
                @Override
                public void onCoursesUpdated(@NonNull List<Course> courses) {
                    try {
                        Log.d(TAG, "observeCourses: Received " + courses.size() + " courses");

                        if (binding == null) {
                            Log.d(TAG, "observeCourses: Fragment binding is null, skipping UI update");
                            return;
                        }

                        courseAdapter.submitList(courses);

                        if (courses.isEmpty()) {
                            binding.recyclerViewCourses.setVisibility(View.GONE);
                            binding.tvEmptyState.setVisibility(View.VISIBLE);
                            Log.d(TAG, "observeCourses: Showing empty state");
                        } else {
                            binding.recyclerViewCourses.setVisibility(View.VISIBLE);
                            binding.tvEmptyState.setVisibility(View.GONE);
                            Log.d(TAG, "observeCourses: Showing course list");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "observeCourses: Error updating UI with courses", e);
                        if (binding != null) {
                            showErrorToUser("Error displaying courses", e);
                        }
                    }
                }

                @Override
                public void onError(@NonNull Exception e) {
                    Log.e(TAG, "observeCourses: Error loading courses from database", e);
                    showErrorToUser("Error loading courses from database", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "observeCourses: Error setting up course observation", e);
            showErrorToUser("Error initializing course loading", e);
        }
    }

    private void navigateToAddCourse() {
        try {
            Log.d(TAG, "navigateToAddCourse: Navigating to add course screen");
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.addCourseFragment);
        } catch (Exception e) {
            Log.e(TAG, "navigateToAddCourse: Navigation error", e);
            showErrorToUser("Error navigating to add course screen", e);
        }
    }

    private void navigateToEditCourse(@NonNull Course course) {
        try {
            Log.d(TAG, "navigateToEditCourse: Navigating to edit course screen for: " + course.getCourseName());
            Bundle bundle = new Bundle();
            bundle.putString("courseId", course.getId());
            bundle.putString("courseName", course.getCourseName());
            bundle.putString("courseCode", course.getCourseCode());
            bundle.putInt("creditHours", course.getCreditHours());
            bundle.putString("courseType", course.getCourseType());
            bundle.putLong("timestamp", course.getTimestamp());

            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.editCourseFragment, bundle);
        } catch (Exception e) {
            Log.e(TAG, "navigateToEditCourse: Navigation error", e);
            showErrorToUser("Error navigating to edit course screen", e);
        }
    }

    private void showDeleteConfirmationDialog(@NonNull Course course) {
        try {
            Log.d(TAG, "showDeleteConfirmationDialog: Showing delete confirmation for: " + course.getCourseName());
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Course")
                    .setMessage("Are you sure you want to delete \"" + course.getCourseName() + "\"?\n\nThis action cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        try {
                            Log.d(TAG, "showDeleteConfirmationDialog: User confirmed deletion");
                            deleteCourse(course);
                        } catch (Exception e) {
                            Log.e(TAG, "showDeleteConfirmationDialog: Error in delete confirmation", e);
                            showErrorToUser("Error processing delete confirmation", e);
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> Log.d(TAG, "showDeleteConfirmationDialog: User cancelled deletion"))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } catch (Exception e) {
            Log.e(TAG, "showDeleteConfirmationDialog: Error showing confirmation dialog", e);
            showErrorToUser("Error showing delete confirmation", e);
        }
    }

    private void deleteCourse(@NonNull Course course) {
        try {
            Log.d(TAG, "deleteCourse: Starting deletion process for: " + course.getCourseName());
            courseRepository.deleteCourse(course.getId(), new CourseRepository.OperationCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Log.i(TAG, "deleteCourse: Course deleted successfully");
                    Toast.makeText(getContext(), "Course deleted successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NonNull Exception e) {
                    Log.e(TAG, "deleteCourse: Failed to delete course", e);
                    showErrorToUser("Failed to delete course", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "deleteCourse: Critical error in delete function", e);
            showErrorToUser("Critical error in delete process", e);
        }
    }

    private void showErrorToUser(@NonNull String message, @Nullable Throwable exception) {
        try {
            String fullMessage = exception != null ? message + ": " + exception.getMessage() : message;
            Toast.makeText(getContext(), fullMessage, Toast.LENGTH_LONG).show();
            Log.e(TAG, "showErrorToUser: " + fullMessage, exception);
        } catch (Exception e) {
            Log.e(TAG, "showErrorToUser: Error showing error message", e);
        }
    }

    @Override
    public void onDestroyView() {
        try {
            Log.d(TAG, "onDestroyView: Cleaning up HomeFragment");
            super.onDestroyView();
            if (coursesListener != null) {
                courseRepository.removeCoursesListener(coursesListener);
                coursesListener = null;
            }
            binding = null;
        } catch (Exception e) {
            Log.e(TAG, "onDestroyView: Error during cleanup", e);
        }
    }
}
