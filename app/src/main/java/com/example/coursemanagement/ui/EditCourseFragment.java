package com.example.coursemanagement.ui;

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

import com.example.coursemanagement.databinding.FragmentEditCourseBinding;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.repository.CourseRepository;

public class EditCourseFragment extends Fragment {

    private static final String TAG = "EditCourseFragment";

    private FragmentEditCourseBinding binding;
    private final CourseRepository courseRepository = new CourseRepository();
    private Course currentCourse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreateView: Inflating EditCourseFragment");
            binding = FragmentEditCourseBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Error inflating layout", e);
            Toast.makeText(getContext(), "Error loading edit course screen: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d(TAG, "onViewCreated: Setting up EditCourseFragment");

            Bundle bundle = getArguments();
            if (bundle != null) {
                try {
                    String courseId = bundle.getString("courseId", "");
                    String courseName = bundle.getString("courseName", "");
                    String courseCode = bundle.getString("courseCode", "");
                    int creditHours = bundle.getInt("creditHours", 0);
                    String courseType = bundle.getString("courseType", "Theory");
                    long timestamp = bundle.getLong("timestamp", System.currentTimeMillis());

                    Log.d(TAG, "onViewCreated: Loading course data - ID: " + courseId + ", Name: " + courseName);

                    currentCourse = new Course(courseId, courseName, courseCode, creditHours, courseType, timestamp);
                    populateFields();
                } catch (Exception e) {
                    Log.e(TAG, "onViewCreated: Error processing arguments", e);
                    showErrorToUser("Error loading course data", e);
                }
            } else {
                Log.e(TAG, "onViewCreated: No arguments provided");
                showErrorToUser("No course data provided", null);
                try {
                    NavController navController = NavHostFragment.findNavController(EditCourseFragment.this);
                    navController.navigateUp();
                } catch (Exception navError) {
                    Log.e(TAG, "onViewCreated: Navigation error after missing arguments", navError);
                }
                return;
            }

            binding.btnUpdateCourse.setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Update course button clicked");
                    updateCourse();
                } catch (Exception e) {
                    Log.e(TAG, "Error handling update course button click", e);
                    showErrorToUser("Error processing update request", e);
                }
            });

            binding.btnCancel.setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Cancel button clicked");
                    NavController navController = NavHostFragment.findNavController(EditCourseFragment.this);
                    navController.navigateUp();
                } catch (Exception e) {
                    Log.e(TAG, "Error handling cancel button click", e);
                    showErrorToUser("Error canceling edit", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: Critical error setting up view", e);
            showErrorToUser("Error setting up edit course screen", e);
        }
    }

    private void populateFields() {
        try {
            Log.d(TAG, "populateFields: Populating form with course data");
            binding.etCourseNameEdit.setText(currentCourse.getCourseName());
            binding.etCourseCodeEdit.setText(currentCourse.getCourseCode());
            binding.etCreditHoursEdit.setText(String.valueOf(currentCourse.getCreditHours()));

            if ("Theory".equals(currentCourse.getCourseType())) {
                binding.rbTheoryEdit.setChecked(true);
            } else {
                binding.rbLabEdit.setChecked(true);
            }

            Log.d(TAG, "populateFields: Fields populated successfully");
        } catch (Exception e) {
            Log.e(TAG, "populateFields: Error populating fields", e);
            showErrorToUser("Error loading course details", e);
        }
    }

    private void updateCourse() {
        try {
            Log.d(TAG, "updateCourse: Starting course update process");

            String courseName = binding.etCourseNameEdit.getText().toString().trim();
            String courseCode = binding.etCourseCodeEdit.getText().toString().trim();
            String creditHoursStr = binding.etCreditHoursEdit.getText().toString().trim();

            Log.d(TAG, "updateCourse: Input validation - name: " + courseName + ", code: " + courseCode + ", credits: " + creditHoursStr);

            if (courseName.isEmpty()) {
                Log.w(TAG, "updateCourse: Course name validation failed - empty");
                binding.etCourseNameEdit.setError("Course name is required");
                return;
            }

            if (courseCode.isEmpty()) {
                Log.w(TAG, "updateCourse: Course code validation failed - empty");
                binding.etCourseCodeEdit.setError("Course code is required");
                return;
            }

            if (creditHoursStr.isEmpty()) {
                Log.w(TAG, "updateCourse: Credit hours validation failed - empty");
                binding.etCreditHoursEdit.setError("Credit hours is required");
                return;
            }

            Integer creditHours = null;
            try {
                creditHours = Integer.valueOf(creditHoursStr);
            } catch (NumberFormatException e) {
                Log.e(TAG, "updateCourse: Error parsing credit hours", e);
            }

            if (creditHours == null || creditHours <= 0) {
                Log.w(TAG, "updateCourse: Credit hours validation failed - invalid value: " + creditHours);
                binding.etCreditHoursEdit.setError("Please enter valid credit hours");
                return;
            }

            String courseType = binding.rbTheoryEdit.isChecked() ? "Theory" : "Lab";
            Log.d(TAG, "updateCourse: Course type selected: " + courseType);

            Course updatedCourse = new Course(
                    currentCourse.getId(),
                    courseName,
                    courseCode,
                    creditHours,
                    courseType,
                    currentCourse.getTimestamp()
            );

            Log.d(TAG, "updateCourse: Updated course object created: " + updatedCourse);

            binding.btnUpdateCourse.setEnabled(false);
            binding.btnUpdateCourse.setText("Updating...");

            courseRepository.updateCourse(updatedCourse, new CourseRepository.OperationCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    try {
                        binding.btnUpdateCourse.setEnabled(true);
                        binding.btnUpdateCourse.setText("Update");
                    } catch (Exception e) {
                        Log.e(TAG, "updateCourse: Error re-enabling button", e);
                    }

                    Log.i(TAG, "updateCourse: Course updated successfully");
                    Toast.makeText(getContext(), "Course updated successfully", Toast.LENGTH_SHORT).show();
                    try {
                        NavController navController = NavHostFragment.findNavController(EditCourseFragment.this);
                        navController.navigateUp();
                    } catch (Exception e) {
                        Log.e(TAG, "updateCourse: Navigation error", e);
                        showErrorToUser("Course updated but navigation failed", e);
                    }
                }

                @Override
                public void onError(@NonNull Exception e) {
                    try {
                        binding.btnUpdateCourse.setEnabled(true);
                        binding.btnUpdateCourse.setText("Update");
                    } catch (Exception btnError) {
                        Log.e(TAG, "updateCourse: Error re-enabling button after failure", btnError);
                    }

                    Log.e(TAG, "updateCourse: Failed to update course", e);
                    showErrorToUser("Failed to update course", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "updateCourse: Critical error in updateCourse function", e);
            showErrorToUser("Critical error in update course process", e);
            try {
                binding.btnUpdateCourse.setEnabled(true);
                binding.btnUpdateCourse.setText("Update");
            } catch (Exception btnError) {
                Log.e(TAG, "updateCourse: Error re-enabling button after critical failure", btnError);
            }
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
            Log.d(TAG, "onDestroyView: Cleaning up EditCourseFragment");
            super.onDestroyView();
            binding = null;
        } catch (Exception e) {
            Log.e(TAG, "onDestroyView: Error during cleanup", e);
        }
    }

    @NonNull
    public static EditCourseFragment newInstance(@NonNull Course course) {
        EditCourseFragment fragment = new EditCourseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("courseId", course.getId());
        bundle.putString("courseName", course.getCourseName());
        bundle.putString("courseCode", course.getCourseCode());
        bundle.putInt("creditHours", course.getCreditHours());
        bundle.putString("courseType", course.getCourseType());
        bundle.putLong("timestamp", course.getTimestamp());
        fragment.setArguments(bundle);
        return fragment;
    }
}
