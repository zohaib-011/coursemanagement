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

import com.example.coursemanagement.R;
import com.example.coursemanagement.databinding.FragmentAddCourseBinding;
import com.example.coursemanagement.model.Course;
import com.example.coursemanagement.repository.CourseRepository;

public class AddCourseFragment extends Fragment {

    private static final String TAG = "AddCourseFragment";

    private FragmentAddCourseBinding binding;
    private final CourseRepository courseRepository = new CourseRepository();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreateView: Inflating AddCourseFragment");
            binding = FragmentAddCourseBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Error inflating layout", e);
            Toast.makeText(getContext(), "Error loading add course screen: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Log.d(TAG, "onViewCreated: Setting up view components");

            binding.btnAddCourse.setOnClickListener(v -> {
                try {
                    Log.d(TAG, "Add course button clicked");
                    addCourse();
                } catch (Exception e) {
                    Log.e(TAG, "Error handling add course button click", e);
                    showErrorToUser("Error processing add course request", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: Error setting up view", e);
            showErrorToUser("Error setting up add course screen", e);
        }
    }

    private void addCourse() {
        try {
            Log.d(TAG, "addCourse: Starting course addition process");

            String courseName = binding.etCourseName.getText().toString().trim();
            String courseCode = binding.etCourseCode.getText().toString().trim();
            String creditHoursStr = binding.etCreditHours.getText().toString().trim();

            Log.d(TAG, "addCourse: Input validation - name: " + courseName + ", code: " + courseCode + ", credits: " + creditHoursStr);

            if (courseName.isEmpty()) {
                Log.w(TAG, "addCourse: Course name validation failed - empty");
                binding.etCourseName.setError("Course name is required");
                return;
            }

            if (courseCode.isEmpty()) {
                Log.w(TAG, "addCourse: Course code validation failed - empty");
                binding.etCourseCode.setError("Course code is required");
                return;
            }

            if (creditHoursStr.isEmpty()) {
                Log.w(TAG, "addCourse: Credit hours validation failed - empty");
                binding.etCreditHours.setError("Credit hours is required");
                return;
            }

            Integer creditHours = null;
            try {
                creditHours = Integer.valueOf(creditHoursStr);
            } catch (NumberFormatException e) {
                Log.e(TAG, "addCourse: Error parsing credit hours", e);
            }

            if (creditHours == null || creditHours <= 0) {
                Log.w(TAG, "addCourse: Credit hours validation failed - invalid value: " + creditHours);
                binding.etCreditHours.setError("Please enter valid credit hours");
                return;
            }

            String courseType = binding.rbTheory.isChecked() ? "Theory" : "Lab";
            Log.d(TAG, "addCourse: Course type selected: " + courseType);

            Course course = new Course(
                    "",
                    courseName,
                    courseCode,
                    creditHours,
                    courseType,
                    System.currentTimeMillis()
            );

            Log.d(TAG, "addCourse: Course object created: " + course);

            binding.btnAddCourse.setEnabled(false);
            binding.btnAddCourse.setText("Adding...");

            courseRepository.addCourse(course, new CourseRepository.OperationCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        binding.btnAddCourse.setEnabled(true);
                        binding.btnAddCourse.setText("Add Course");
                    } catch (Exception e) {
                        Log.e(TAG, "addCourse: Error re-enabling button", e);
                    }

                    Log.i(TAG, "addCourse: Course added successfully");
                    Toast.makeText(getContext(), "Course added successfully", Toast.LENGTH_SHORT).show();
                    clearForm();
                    try {
                        NavController navController = NavHostFragment.findNavController(AddCourseFragment.this);
                        navController.navigateUp();
                    } catch (Exception e) {
                        Log.e(TAG, "addCourse: Navigation error", e);
                        showErrorToUser("Course added but navigation failed", e);
                    }
                }

                @Override
                public void onError(@NonNull Exception e) {
                    try {
                        binding.btnAddCourse.setEnabled(true);
                        binding.btnAddCourse.setText("Add Course");
                    } catch (Exception btnError) {
                        Log.e(TAG, "addCourse: Error re-enabling button after failure", btnError);
                    }
                    Log.e(TAG, "addCourse: Failed to add course", e);
                    showErrorToUser("Failed to add course", e);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "addCourse: Critical error in addCourse function", e);
            showErrorToUser("Critical error in add course process", e);
            try {
                binding.btnAddCourse.setEnabled(true);
                binding.btnAddCourse.setText("Add Course");
            } catch (Exception btnError) {
                Log.e(TAG, "addCourse: Error re-enabling button after critical failure", btnError);
            }
        }
    }

    private void clearForm() {
        try {
            Log.d(TAG, "clearForm: Clearing form fields");
            binding.etCourseName.setText("");
            binding.etCourseCode.setText("");
            binding.etCreditHours.setText("");
            binding.rbTheory.setChecked(true);
            Log.d(TAG, "clearForm: Form cleared successfully");
        } catch (Exception e) {
            Log.e(TAG, "clearForm: Error clearing form", e);
            showErrorToUser("Error clearing form", e);
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
            Log.d(TAG, "onDestroyView: Cleaning up AddCourseFragment");
            super.onDestroyView();
            binding = null;
        } catch (Exception e) {
            Log.e(TAG, "onDestroyView: Error during cleanup", e);
        }
    }
}
