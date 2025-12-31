package com.example.coursemanagement.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursemanagement.databinding.ItemCourseBinding;
import com.example.coursemanagement.model.Course;

public class CourseAdapter extends ListAdapter<Course, CourseAdapter.CourseViewHolder> {

    private static final String TAG = "CourseAdapter";

    public interface OnEditClickListener {
        void onEditClick(@NonNull Course course);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(@NonNull Course course);
    }

    @NonNull
    private final OnEditClickListener onEditClick;

    @NonNull
    private final OnDeleteClickListener onDeleteClick;

    public CourseAdapter(@NonNull OnEditClickListener onEditClick,
                         @NonNull OnDeleteClickListener onDeleteClick) {
        super(new CourseDiffCallback());
        this.onEditClick = onEditClick;
        this.onDeleteClick = onDeleteClick;
        try {
            Log.d(TAG, "CourseAdapter initialized");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing CourseAdapter", e);
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            Log.d(TAG, "onCreateViewHolder: Creating new CourseViewHolder");
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ItemCourseBinding binding = ItemCourseBinding.inflate(inflater, parent, false);
            return new CourseViewHolder(binding);
        } catch (Exception e) {
            Log.e(TAG, "onCreateViewHolder: Error creating ViewHolder", e);
            throw new RuntimeException("Failed to create CourseViewHolder: " + e.getMessage(), e);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        try {
            Course course = getItem(position);
            Log.d(TAG, "onBindViewHolder: Binding course at position " + position + ": " + course.getCourseName());
            holder.bind(course);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: Error binding ViewHolder at position " + position, e);
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final ItemCourseBinding binding;

        CourseViewHolder(@NonNull ItemCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull final Course course) {
            try {
                Log.d(TAG, "CourseViewHolder.bind: Binding course data: " + course.getCourseName());

                try {
                    binding.tvCourseName.setText(course.getCourseName());
                    binding.tvCourseCode.setText(course.getCourseCode());
                    binding.tvCreditHours.setText("Credit Hours: " + course.getCreditHours());
                    binding.chipCourseType.setText(course.getCourseType());
                } catch (Exception e) {
                    Log.e(TAG, "CourseViewHolder.bind: Error setting text fields", e);
                }

                binding.btnEdit.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "CourseViewHolder.bind: Edit button clicked for course: " + course.getCourseName());
                        onEditClick.onEditClick(course);
                    } catch (Exception e) {
                        Log.e(TAG, "CourseViewHolder.bind: Error in edit click handler", e);
                    }
                });

                binding.btnDelete.setOnClickListener(v -> {
                    try {
                        Log.d(TAG, "CourseViewHolder.bind: Delete button clicked for course: " + course.getCourseName());
                        onDeleteClick.onDeleteClick(course);
                    } catch (Exception e) {
                        Log.e(TAG, "CourseViewHolder.bind: Error in delete click handler", e);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "CourseViewHolder.bind: Error binding course data", e);
            }
        }
    }

    private static class CourseDiffCallback extends DiffUtil.ItemCallback<Course> {

        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            try {
                boolean result = (oldItem.getId() != null ? oldItem.getId().equals(newItem.getId()) : newItem.getId() == null);
                Log.d(TAG, "CourseDiffCallback.areItemsTheSame: "
                        + oldItem.getCourseName() + " vs " + newItem.getCourseName() + " = " + result);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "CourseDiffCallback.areItemsTheSame: Error comparing items", e);
                return false;
            }
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            try {
                boolean result = oldItem.equals(newItem);
                Log.d(TAG, "CourseDiffCallback.areContentsTheSame: "
                        + oldItem.getCourseName() + " vs " + newItem.getCourseName() + " = " + result);
                return result;
            } catch (Exception e) {
                Log.e(TAG, "CourseDiffCallback.areContentsTheSame: Error comparing content", e);
                return false;
            }
        }
    }
}
