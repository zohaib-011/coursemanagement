# 📚 Course Management Android Application - Complete Viva Guide

## Table of Contents
1. [Project Overview](#project-overview)
2. [Project Structure](#project-structure)
3. [Technology Stack & Dependencies](#technology-stack--dependencies)
4. [Color System & Theming](#color-system--theming)
5. [Layout Files & UI Components](#layout-files--ui-components)
6. [Data Model](#data-model)
7. [Firebase Integration](#firebase-integration)
8. [Navigation System](#navigation-system)
9. [CRUD Operations](#crud-operations)
10. [Delete Confirmation Dialog](#delete-confirmation-dialog)
11. [View Binding](#view-binding)
12. [RecyclerView & Adapter](#recyclerview--adapter)
13. [Text Display & Styling](#text-display--styling)
14. [Input Fields & Border Colors](#input-fields--border-colors)
15. [Buttons & Interactions](#buttons--interactions)
16. [Error Handling](#error-handling)
17. [App Flow Diagram](#app-flow-diagram)

---

## Project Overview

**Application Name:** CourseManagement  
**Package Name:** `com.example.coursemanagement`  
**Minimum SDK:** 27 (Android 8.1)  
**Target SDK:** 35  
**Compile SDK:** 36  
**Language:** Kotlin  
**Architecture:** Single Activity with Multiple Fragments  
**Database:** Firebase Realtime Database  

### What This App Does
This is a Course Management application that allows users to:
- **Add** new courses with name, code, credit hours, and type (Lab/Theory)
- **View** all courses in a list
- **Edit** existing course information
- **Delete** courses with confirmation dialog

---

## Project Structure

```
CourseManagement/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/coursemanagement/
│   │   │   ├── MainActivity.kt                    # Main entry point
│   │   │   ├── adapter/
│   │   │   │   └── CourseAdapter.kt               # RecyclerView adapter
│   │   │   ├── model/
│   │   │   │   └── Course.kt                      # Data class for Course
│   │   │   ├── repository/
│   │   │   │   └── CourseRepository.kt            # Firebase database operations
│   │   │   └── ui/
│   │   │       ├── AddCourseFragment.kt           # Add course screen
│   │   │       ├── EditCourseFragment.kt          # Edit course screen
│   │   │       ├── home/
│   │   │       │   ├── HomeFragment.kt            # Main screen with course list
│   │   │       │   └── HomeViewModel.kt           # ViewModel for home
│   │   │       ├── dashboard/
│   │   │       │   ├── DashboardFragment.kt       # Dashboard screen
│   │   │       │   └── DashboardViewModel.kt      # ViewModel for dashboard
│   │   │       └── notifications/
│   │   │           ├── NotificationsFragment.kt   # Notifications screen
│   │   │           └── NotificationsViewModel.kt  # ViewModel for notifications
│   │   ├── res/
│   │   │   ├── layout/                            # All XML layout files
│   │   │   ├── values/                            # Colors, strings, themes
│   │   │   ├── navigation/                        # Navigation graph
│   │   │   └── drawable/                          # Icons and drawables
│   │   └── AndroidManifest.xml                    # App manifest
│   ├── build.gradle.kts                           # App-level build config
│   └── google-services.json                       # Firebase configuration
├── build.gradle.kts                               # Project-level build config
└── settings.gradle.kts                            # Project settings
```

---

## Technology Stack & Dependencies

### build.gradle.kts (App Level)
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")  // Firebase plugin
}

dependencies {
    // Firebase BOM (Bill of Materials) - manages all Firebase versions
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))
    implementation("com.google.firebase:firebase-analytics")   // Analytics
    implementation("com.google.firebase:firebase-database")    // Realtime Database

    // AndroidX Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)                              // Material Design
    implementation(libs.androidx.constraintlayout)             // ConstraintLayout
    
    // Lifecycle Components
    implementation(libs.androidx.lifecycle.livedata.ktx)       // LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)      // ViewModel
    
    // Navigation Components
    implementation(libs.androidx.navigation.fragment.ktx)      // Navigation Fragment
    implementation(libs.androidx.navigation.ui.ktx)            // Navigation UI
}
```

### Key Features Enabled
```kotlin
buildFeatures {
    viewBinding = true  // Enables View Binding for type-safe view access
}
```

---

## Color System & Theming

### Colors Defined (res/values/colors.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>   <!-- Light Purple - Used in Dark Mode -->
    <color name="purple_500">#FF6200EE</color>   <!-- Primary Purple - Main brand color -->
    <color name="purple_700">#FF3700B3</color>   <!-- Dark Purple - Status bar, variants -->
    <color name="teal_200">#FF03DAC5</color>     <!-- Teal - Secondary/Accent color -->
    <color name="teal_700">#FF018786</color>     <!-- Dark Teal - Secondary variant -->
    <color name="black">#FF000000</color>        <!-- Black - Text on light backgrounds -->
    <color name="white">#FFFFFFFF</color>        <!-- White - Text on dark backgrounds -->
</resources>
```

### Theme Configuration (res/values/themes.xml) - Light Mode
```xml
<style name="Theme.CourseManagement" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <!-- Primary brand color - Used for buttons, FAB, highlights -->
    <item name="colorPrimary">@color/purple_500</item>           <!-- #FF6200EE -->
    
    <!-- Primary variant - Used for status bar -->
    <item name="colorPrimaryVariant">@color/purple_700</item>    <!-- #FF3700B3 -->
    
    <!-- Color on primary - Text/icons on primary colored surfaces -->
    <item name="colorOnPrimary">@color/white</item>              <!-- #FFFFFFFF -->
    
    <!-- Secondary color - Used for chips, accents -->
    <item name="colorSecondary">@color/teal_200</item>           <!-- #FF03DAC5 -->
    
    <!-- Secondary variant -->
    <item name="colorSecondaryVariant">@color/teal_700</item>    <!-- #FF018786 -->
    
    <!-- Color on secondary - Text/icons on secondary surfaces -->
    <item name="colorOnSecondary">@color/black</item>            <!-- #FF000000 -->
    
    <!-- Status bar color -->
    <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
</style>
```

### Night Mode Theme (res/values-night/themes.xml)
```xml
<style name="Theme.CourseManagement" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
    <item name="colorPrimary">@color/purple_200</item>           <!-- Lighter purple for dark mode -->
    <item name="colorPrimaryVariant">@color/purple_700</item>
    <item name="colorOnPrimary">@color/black</item>              <!-- Black text on light purple -->
    <item name="colorSecondary">@color/teal_200</item>
    <item name="colorSecondaryVariant">@color/teal_200</item>
    <item name="colorOnSecondary">@color/black</item>
</style>
```

### How Colors Are Applied in Code

#### 1. Title Text Color (Purple Primary)
```xml
<!-- In fragment_home.xml, fragment_add_course.xml, fragment_edit_course.xml -->
<TextView
    android:text="Course Management"
    android:textColor="?attr/colorPrimary" />  <!-- Uses purple_500 (#FF6200EE) -->
```

#### 2. FAB (Floating Action Button) Background
```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fabAddCourse"
    app:backgroundTint="?attr/colorPrimary"   <!-- Purple background -->
    android:tint="@android:color/white" />    <!-- White + icon -->
```

#### 3. Chip Background Color
```xml
<com.google.android.material.chip.Chip
    app:chipBackgroundColor="?attr/colorSecondary"   <!-- Teal background (#FF03DAC5) -->
    app:chipStrokeColor="?attr/colorPrimary"         <!-- Purple border -->
    app:chipStrokeWidth="1dp" />
```

#### 4. Delete Button Red Color
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnDelete"
    app:backgroundTint="@android:color/holo_red_light" />  <!-- Red background for danger -->
```

---

## Layout Files & UI Components

### 1. activity_main.xml - Main Container
**Purpose:** Hosts the Navigation Host Fragment for fragment navigation

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- NavHostFragment - Container for all fragments -->
    <fragment
        android:id="@+id/nav_host_fragment_activity_main"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/mobile_navigation" />
        
</androidx.constraintlayout.widget.ConstraintLayout>
```

**Key Points:**
- Uses `ConstraintLayout` as root container
- `NavHostFragment` manages fragment transactions
- `app:defaultNavHost="true"` - Handles back button navigation
- `app:navGraph` - Links to navigation graph XML

---

### 2. fragment_home.xml - Course List Screen
**Purpose:** Displays list of courses with RecyclerView and empty state

```xml
<LinearLayout
    android:orientation="vertical"
    android:padding="16dp">

    <!-- Header Section -->
    <LinearLayout
        android:orientation="horizontal"
        android:gravity="center_vertical">

        <!-- Title TextView -->
        <TextView
            android:layout_width="0dp"
            android:layout_weight="1"
            android:text="Course Management"
            android:textAppearance="@style/TextAppearance.MaterialComponents.Headline5"
            android:textColor="?attr/colorPrimary" />

        <!-- Add Button (FAB) -->
        <com.google.android.material.floatingactionbutton.FloatingActionButton
            android:id="@+id/fabAddCourse"
            android:src="@android:drawable/ic_input_add"
            android:tint="@android:color/white"
            app:backgroundTint="?attr/colorPrimary" />
    </LinearLayout>

    <!-- Content Container -->
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <!-- Course List -->
        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerViewCourses"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <!-- Empty State Message -->
        <TextView
            android:id="@+id/tvEmptyState"
            android:gravity="center"
            android:text="No courses added yet.\nTap + to add a course."
            android:textColor="?android:attr/textColorSecondary"
            android:visibility="gone" />
    </FrameLayout>
</LinearLayout>
```

**Layout Structure:**
```
LinearLayout (Vertical)
├── LinearLayout (Horizontal) - Header
│   ├── TextView - "Course Management" title
│   └── FloatingActionButton - Add course button
└── FrameLayout - Content container
    ├── RecyclerView - Course list
    └── TextView - Empty state (initially hidden)
```

---

### 3. fragment_add_course.xml - Add Course Form
**Purpose:** Form to add new course with input validation

```xml
<ScrollView
    android:fillViewport="true">

    <LinearLayout
        android:gravity="center"
        android:padding="24dp">

        <com.google.android.material.card.MaterialCardView
            app:cardCornerRadius="12dp"
            app:cardElevation="4dp">

            <LinearLayout android:padding="16dp">

                <!-- Title -->
                <TextView
                    android:text="Add New Course"
                    android:textAppearance="@style/TextAppearance.MaterialComponents.Headline5"
                    android:textColor="?attr/colorPrimary" />

                <!-- Course Name Input -->
                <com.google.android.material.textfield.TextInputLayout
                    android:hint="Course Name"
                    app:boxBackgroundMode="outline"
                    app:boxCornerRadiusBottomEnd="8dp"
                    app:boxCornerRadiusBottomStart="8dp"
                    app:boxCornerRadiusTopEnd="8dp"
                    app:boxCornerRadiusTopStart="8dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etCourseName"
                        android:inputType="textCapWords" />
                </com.google.android.material.textfield.TextInputLayout>

                <!-- Course Code Input -->
                <com.google.android.material.textfield.TextInputLayout
                    android:hint="Course Code"
                    app:boxBackgroundMode="outline">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etCourseCode"
                        android:inputType="textCapCharacters" />
                </com.google.android.material.textfield.TextInputLayout>

                <!-- Credit Hours Input -->
                <com.google.android.material.textfield.TextInputLayout
                    android:hint="Credit Hours"
                    app:boxBackgroundMode="outline">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etCreditHours"
                        android:inputType="number" />
                </com.google.android.material.textfield.TextInputLayout>

                <!-- Course Type Label -->
                <TextView
                    android:text="Course Type"
                    android:textAppearance="@style/TextAppearance.MaterialComponents.Body1" />

                <!-- Radio Buttons for Type Selection -->
                <RadioGroup
                    android:id="@+id/rgCourseType"
                    android:orientation="horizontal">

                    <com.google.android.material.radiobutton.MaterialRadioButton
                        android:id="@+id/rbTheory"
                        android:checked="true"
                        android:text="Theory" />

                    <com.google.android.material.radiobutton.MaterialRadioButton
                        android:id="@+id/rbLab"
                        android:text="Lab" />
                </RadioGroup>

                <!-- Submit Button -->
                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnAddCourse"
                    android:text="Add Course"
                    android:layout_height="56dp"
                    app:cornerRadius="8dp" />
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>
    </LinearLayout>
</ScrollView>
```

**Layout Structure:**
```
ScrollView
└── LinearLayout (centered)
    └── MaterialCardView (rounded corners: 12dp, elevation: 4dp)
        └── LinearLayout (vertical)
            ├── TextView - "Add New Course"
            ├── TextInputLayout - Course Name
            │   └── TextInputEditText
            ├── TextInputLayout - Course Code
            │   └── TextInputEditText
            ├── TextInputLayout - Credit Hours
            │   └── TextInputEditText
            ├── TextView - "Course Type" label
            ├── RadioGroup (horizontal)
            │   ├── MaterialRadioButton - "Theory" (default checked)
            │   └── MaterialRadioButton - "Lab"
            └── MaterialButton - "Add Course"
```

---

### 4. fragment_edit_course.xml - Edit Course Form
**Purpose:** Form to edit existing course with pre-filled data

```xml
<!-- Similar to Add Course with different IDs -->
<ScrollView>
    <LinearLayout>
        <com.google.android.material.card.MaterialCardView>
            <LinearLayout>
                <TextView android:text="Edit Course" />
                
                <!-- Input fields with "Edit" suffix in IDs -->
                <TextInputEditText android:id="@+id/etCourseNameEdit" />
                <TextInputEditText android:id="@+id/etCourseCodeEdit" />
                <TextInputEditText android:id="@+id/etCreditHoursEdit" />
                
                <RadioGroup android:id="@+id/rgCourseTypeEdit">
                    <MaterialRadioButton android:id="@+id/rbTheoryEdit" />
                    <MaterialRadioButton android:id="@+id/rbLabEdit" />
                </RadioGroup>

                <!-- Two buttons side by side -->
                <LinearLayout android:orientation="horizontal">
                    <MaterialButton
                        android:id="@+id/btnUpdateCourse"
                        android:text="Update"
                        android:layout_weight="1" />

                    <MaterialButton
                        android:id="@+id/btnCancel"
                        android:text="Cancel"
                        android:layout_weight="1"
                        style="@style/Widget.MaterialComponents.Button.OutlinedButton" />
                </LinearLayout>
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>
    </LinearLayout>
</ScrollView>
```

---

### 5. item_course.xml - Course List Item
**Purpose:** Individual course card in RecyclerView

```xml
<com.google.android.material.card.MaterialCardView
    android:layout_margin="8dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="4dp"
    app:strokeColor="@android:color/darker_gray"
    app:strokeWidth="1dp">

    <LinearLayout
        android:padding="16dp"
        android:orientation="vertical">

        <!-- Top Row: Course Info + Type Chip -->
        <LinearLayout android:orientation="horizontal">
            
            <!-- Course Name and Code -->
            <LinearLayout
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/tvCourseName"
                    android:textAppearance="@style/TextAppearance.MaterialComponents.Headline6"
                    android:textColor="?android:attr/textColorPrimary" />

                <TextView
                    android:id="@+id/tvCourseCode"
                    android:textAppearance="@style/TextAppearance.MaterialComponents.Body2"
                    android:textColor="?android:attr/textColorSecondary" />
            </LinearLayout>

            <!-- Course Type Chip -->
            <com.google.android.material.chip.Chip
                android:id="@+id/chipCourseType"
                app:chipBackgroundColor="?attr/colorSecondary"
                app:chipStrokeColor="?attr/colorPrimary"
                app:chipStrokeWidth="1dp" />
        </LinearLayout>

        <!-- Bottom Row: Credit Hours + Action Buttons -->
        <LinearLayout android:orientation="horizontal">
            
            <TextView
                android:id="@+id/tvCreditHours"
                android:layout_weight="1"
                android:textColor="?android:attr/textColorSecondary" />

            <!-- Edit Button (Outlined) -->
            <com.google.android.material.button.MaterialButton
                android:id="@+id/btnEdit"
                android:text="Edit"
                android:layout_height="36dp"
                app:cornerRadius="18dp"
                style="@style/Widget.MaterialComponents.Button.OutlinedButton" />

            <!-- Delete Button (Red) -->
            <com.google.android.material.button.MaterialButton
                android:id="@+id/btnDelete"
                android:text="Delete"
                android:layout_height="36dp"
                app:cornerRadius="18dp"
                app:backgroundTint="@android:color/holo_red_light"
                style="@style/Widget.MaterialComponents.Button.OutlinedButton" />
        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

**Layout Structure:**
```
MaterialCardView (margin: 8dp, corner: 12dp, stroke: gray 1dp)
└── LinearLayout (vertical, padding: 16dp)
    ├── LinearLayout (horizontal) - Top row
    │   ├── LinearLayout (vertical) - Course info
    │   │   ├── TextView - Course Name (Headline6, Primary text color)
    │   │   └── TextView - Course Code (Body2, Secondary text color)
    │   └── Chip - Course Type (Teal background, Purple border)
    └── LinearLayout (horizontal) - Bottom row
        ├── TextView - Credit Hours (Secondary text color)
        ├── MaterialButton - "Edit" (Outlined style)
        └── MaterialButton - "Delete" (Red background)
```

---

## Data Model

### Course.kt - Data Class
```kotlin
package com.example.coursemanagement.model

data class Course(
    var id: String = "",                           // Firebase auto-generated key
    var courseName: String = "",                   // e.g., "Data Structures"
    var courseCode: String = "",                   // e.g., "CS-201"
    var creditHours: Int = 0,                      // e.g., 3
    var courseType: String = "",                   // "Lab" or "Theory"
    var timestamp: Long = System.currentTimeMillis() // For sorting by date
) {
    // Firebase requires a no-argument constructor for deserialization
    constructor() : this("", "", "", 0, "", System.currentTimeMillis())
}
```

**Why No-Arg Constructor?**
Firebase Realtime Database uses reflection to create objects from data. It requires:
1. A no-argument constructor (empty constructor)
2. Properties must have default values or be `var` (mutable)

**Data Class Features:**
- `data class` auto-generates: `equals()`, `hashCode()`, `toString()`, `copy()`
- `copy()` is used in EditCourseFragment to create modified version

---

## Firebase Integration

### Firebase Configuration (google-services.json)
```json
{
  "project_info": {
    "project_number": "517485035226",
    "firebase_url": "https://coursemanagement-c9024-default-rtdb.firebaseio.com",
    "project_id": "coursemanagement-c9024",
    "storage_bucket": "coursemanagement-c9024.firebasestorage.app"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:517485035226:android:415b3f300cce08ae8c6fe3",
        "android_client_info": {
          "package_name": "com.example.coursemanagement"
        }
      },
      "api_key": [
        {
          "current_key": "AIzaSyBFhspsVYivkmX7A223X_SVt9qPK3grhTE"
        }
      ]
    }
  ]
}
```

### Firebase Initialization (MainActivity.kt)
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebase()
        setupUI()
    }
    
    private fun initializeFirebase() {
        // Step 1: Initialize Firebase App
        FirebaseApp.initializeApp(this)
        
        // Step 2: Enable offline persistence (data available offline)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
```

### CourseRepository.kt - All Firebase Operations
```kotlin
class CourseRepository {
    // Get Firebase Database instance
    private val database = FirebaseDatabase.getInstance()
    
    // Reference to "courses" node in database
    private val coursesRef = database.getReference("courses")
```

#### Firebase Database Structure
```
coursemanagement-c9024-default-rtdb/
└── courses/
    ├── -NxYz123ABC/           # Auto-generated key
    │   ├── id: "-NxYz123ABC"
    │   ├── courseName: "Data Structures"
    │   ├── courseCode: "CS-201"
    │   ├── creditHours: 3
    │   ├── courseType: "Theory"
    │   └── timestamp: 1704067200000
    ├── -NxYz456DEF/
    │   └── ...
    └── ...
```

---

## CRUD Operations

### 1. CREATE - Adding a Course
**File:** `CourseRepository.kt` → `addCourse()`

```kotlin
suspend fun addCourse(course: Course): Result<String> {
    return try {
        // Validation
        if (course.courseName.isBlank()) {
            return Result.failure(Exception("Course name cannot be empty"))
        }
        if (course.courseCode.isBlank()) {
            return Result.failure(Exception("Course code cannot be empty"))
        }
        if (course.creditHours <= 0) {
            return Result.failure(Exception("Credit hours must be greater than 0"))
        }
        
        // Generate unique Firebase key
        val key = coursesRef.push().key 
            ?: throw Exception("Failed to generate course ID")
        
        // Set the ID and timestamp
        course.id = key
        course.timestamp = System.currentTimeMillis()
        
        // Save to Firebase - await() suspends until complete
        coursesRef.child(key).setValue(course).await()
        
        Result.success(key)
    } catch (e: Exception) {
        Result.failure(Exception("Failed to add course: ${e.message}", e))
    }
}
```

**How it's called in AddCourseFragment:**
```kotlin
private fun addCourse() {
    // Get input values
    val courseName = binding.etCourseName.text.toString().trim()
    val courseCode = binding.etCourseCode.text.toString().trim()
    val creditHours = binding.etCreditHours.text.toString().toIntOrNull()
    val courseType = if (binding.rbTheory.isChecked) "Theory" else "Lab"
    
    // Create Course object
    val course = Course(
        courseName = courseName,
        courseCode = courseCode,
        creditHours = creditHours,
        courseType = courseType
    )
    
    // Launch coroutine to call repository
    lifecycleScope.launch {
        val result = courseRepository.addCourse(course)
        
        if (result.isSuccess) {
            Toast.makeText(context, "Course added successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()  // Go back to home
        } else {
            Toast.makeText(context, "Failed: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
        }
    }
}
```

---

### 2. READ - Fetching All Courses
**File:** `CourseRepository.kt` → `getAllCourses()`

```kotlin
fun getAllCourses(): Flow<List<Course>> = callbackFlow {
    // Create Firebase listener
    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val courses = mutableListOf<Course>()
            
            // Loop through all children (each course)
            for (courseSnapshot in snapshot.children) {
                // Convert snapshot to Course object
                val course = courseSnapshot.getValue(Course::class.java)
                course?.let {
                    it.id = courseSnapshot.key ?: ""  // Set the Firebase key as ID
                    courses.add(it)
                }
            }
            
            // Sort by timestamp (newest first)
            val sortedCourses = courses.sortedByDescending { it.timestamp }
            
            // Send to UI
            trySend(sortedCourses)
        }

        override fun onCancelled(error: DatabaseError) {
            close(error.toException())  // Close flow with error
        }
    }
    
    // Attach listener to courses reference
    coursesRef.addValueEventListener(listener)
    
    // Remove listener when flow is cancelled
    awaitClose { 
        coursesRef.removeEventListener(listener)
    }
}
```

**How it's observed in HomeFragment:**
```kotlin
private fun observeCourses() {
    lifecycleScope.launch {
        courseRepository.getAllCourses().collect { courses ->
            // Submit list to RecyclerView adapter
            courseAdapter.submitList(courses)
            
            // Show/hide empty state
            if (courses.isEmpty()) {
                binding.recyclerViewCourses.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.recyclerViewCourses.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
            }
        }
    }
}
```

**Key Concepts:**
- **Flow** - Kotlin's reactive stream, emits data over time
- **callbackFlow** - Converts callback-based API to Flow
- **collect** - Subscribes to Flow and receives emissions
- **ValueEventListener** - Firebase listener, called on every data change

---

### 3. UPDATE - Editing a Course
**File:** `CourseRepository.kt` → `updateCourse()`

```kotlin
suspend fun updateCourse(course: Course): Result<Unit> {
    return try {
        // Validation
        if (course.id.isEmpty()) {
            return Result.failure(Exception("Course ID cannot be empty"))
        }
        
        // Check if course exists
        val snapshot = coursesRef.child(course.id).get().await()
        if (!snapshot.exists()) {
            return Result.failure(Exception("Course not found"))
        }
        
        // Update in Firebase (overwrites all fields)
        coursesRef.child(course.id).setValue(course).await()
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(Exception("Failed to update course: ${e.message}", e))
    }
}
```

**How it's called in EditCourseFragment:**
```kotlin
private fun updateCourse() {
    // Get updated values from form
    val courseName = binding.etCourseNameEdit.text.toString().trim()
    val courseCode = binding.etCourseCodeEdit.text.toString().trim()
    val creditHours = binding.etCreditHoursEdit.text.toString().toIntOrNull()
    val courseType = if (binding.rbTheoryEdit.isChecked) "Theory" else "Lab"
    
    // Create updated course using copy()
    val updatedCourse = currentCourse.copy(
        courseName = courseName,
        courseCode = courseCode,
        creditHours = creditHours,
        courseType = courseType
    )
    
    lifecycleScope.launch {
        val result = courseRepository.updateCourse(updatedCourse)
        
        if (result.isSuccess) {
            Toast.makeText(context, "Course updated successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}
```

---

### 4. DELETE - Removing a Course
**File:** `CourseRepository.kt` → `deleteCourse()`

```kotlin
suspend fun deleteCourse(courseId: String): Result<Unit> {
    return try {
        if (courseId.isEmpty()) {
            return Result.failure(Exception("Course ID cannot be empty"))
        }
        
        // Check if course exists
        val snapshot = coursesRef.child(courseId).get().await()
        if (!snapshot.exists()) {
            return Result.failure(Exception("Course not found"))
        }
        
        // Delete from Firebase
        coursesRef.child(courseId).removeValue().await()
        
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(Exception("Failed to delete course: ${e.message}", e))
    }
}
```

**How it's called in HomeFragment:**
```kotlin
private fun deleteCourse(course: Course) {
    lifecycleScope.launch {
        val result = courseRepository.deleteCourse(course.id)
        
        if (result.isSuccess) {
            Toast.makeText(context, "Course deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_LONG).show()
        }
    }
}
```

---

## Delete Confirmation Dialog

**File:** `HomeFragment.kt` → `showDeleteConfirmationDialog()`

```kotlin
private fun showDeleteConfirmationDialog(course: Course) {
    AlertDialog.Builder(requireContext())
        .setTitle("Delete Course")                           // Dialog title
        .setMessage("Are you sure you want to delete \"${course.courseName}\"?\n\nThis action cannot be undone.")
        .setPositiveButton("Delete") { _, _ ->               // Confirm button
            deleteCourse(course)                             // Actually delete
        }
        .setNegativeButton("Cancel") { _, _ ->               // Cancel button
            // Do nothing, just dismiss
        }
        .setIcon(android.R.drawable.ic_dialog_alert)         // Warning icon
        .show()                                              // Display dialog
}
```

**When is it shown?**
```kotlin
// In CourseAdapter setup (HomeFragment)
courseAdapter = CourseAdapter(
    onDeleteClick = { course ->
        showDeleteConfirmationDialog(course)  // Called when delete button clicked
    }
)
```

**Dialog Flow:**
```
User clicks Delete button on course card
        ↓
showDeleteConfirmationDialog() called
        ↓
AlertDialog appears with:
  - Title: "Delete Course"
  - Message: "Are you sure you want to delete "Course Name"?"
  - Icon: Warning icon
  - Positive Button: "Delete"
  - Negative Button: "Cancel"
        ↓
User clicks "Delete"          User clicks "Cancel"
        ↓                              ↓
deleteCourse() called         Dialog dismissed
        ↓
Firebase removes data
        ↓
Toast: "Course deleted successfully"
```

---

## Navigation System

### Navigation Graph (res/navigation/mobile_navigation.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/mobile_navigation"
    app:startDestination="@+id/navigation_home">  <!-- Home is first screen -->

    <!-- Home Fragment - Course List -->
    <fragment
        android:id="@+id/navigation_home"
        android:name="com.example.coursemanagement.ui.home.HomeFragment"
        android:label="Course Management" />

    <!-- Add Course Fragment -->
    <fragment
        android:id="@+id/addCourseFragment"
        android:name="com.example.coursemanagement.ui.AddCourseFragment"
        android:label="Add Course" />

    <!-- Edit Course Fragment with Arguments -->
    <fragment
        android:id="@+id/editCourseFragment"
        android:name="com.example.coursemanagement.ui.EditCourseFragment"
        android:label="Edit Course">
        
        <!-- Arguments passed to this fragment -->
        <argument android:name="courseId" app:argType="string" />
        <argument android:name="courseName" app:argType="string" />
        <argument android:name="courseCode" app:argType="string" />
        <argument android:name="creditHours" app:argType="integer" />
        <argument android:name="courseType" app:argType="string" />
        <argument android:name="timestamp" app:argType="long" />
    </fragment>
</navigation>
```

### Navigation Code Examples

#### Navigate to Add Course
```kotlin
// In HomeFragment
private fun navigateToAddCourse() {
    findNavController().navigate(R.id.addCourseFragment)
}
```

#### Navigate to Edit Course (with data)
```kotlin
// In HomeFragment
private fun navigateToEditCourse(course: Course) {
    val bundle = Bundle().apply {
        putString("courseId", course.id)
        putString("courseName", course.courseName)
        putString("courseCode", course.courseCode)
        putInt("creditHours", course.creditHours)
        putString("courseType", course.courseType)
        putLong("timestamp", course.timestamp)
    }
    findNavController().navigate(R.id.editCourseFragment, bundle)
}
```

#### Navigate Back
```kotlin
// In AddCourseFragment/EditCourseFragment
findNavController().navigateUp()  // Goes back to previous screen
```

#### Receiving Arguments in EditCourseFragment
```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    arguments?.let { bundle ->
        val courseId = bundle.getString("courseId") ?: ""
        val courseName = bundle.getString("courseName") ?: ""
        val courseCode = bundle.getString("courseCode") ?: ""
        val creditHours = bundle.getInt("creditHours", 0)
        val courseType = bundle.getString("courseType") ?: "Theory"
        val timestamp = bundle.getLong("timestamp", System.currentTimeMillis())
        
        currentCourse = Course(courseId, courseName, courseCode, creditHours, courseType, timestamp)
        populateFields()  // Fill form with existing data
    }
}
```

---

## View Binding

### What is View Binding?
View Binding generates a binding class for each XML layout, providing type-safe access to views without `findViewById()`.

### Enabling View Binding (build.gradle.kts)
```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

### Generated Binding Classes
| Layout File | Generated Binding Class |
|-------------|------------------------|
| `activity_main.xml` | `ActivityMainBinding` |
| `fragment_home.xml` | `FragmentHomeBinding` |
| `fragment_add_course.xml` | `FragmentAddCourseBinding` |
| `fragment_edit_course.xml` | `FragmentEditCourseBinding` |
| `item_course.xml` | `ItemCourseBinding` |

### Usage in Activity (MainActivity.kt)
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding  // Declare binding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inflate layout using binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Set content view to root of binding
    }
}
```

### Usage in Fragment (HomeFragment.kt)
```kotlin
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!  // Non-null assertion
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Access views using binding
        binding.fabAddCourse.setOnClickListener { ... }
        binding.recyclerViewCourses.adapter = courseAdapter
        binding.tvEmptyState.visibility = View.GONE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }
}
```

### Why `_binding` and `binding`?
- `_binding` is nullable (`FragmentHomeBinding?`) - set to null in `onDestroyView()`
- `binding` is non-null getter - safe to use between `onViewCreated()` and `onDestroyView()`
- This pattern prevents memory leaks in Fragments

---

## RecyclerView & Adapter

### CourseAdapter.kt - Complete Implementation
```kotlin
class CourseAdapter(
    private val onEditClick: (Course) -> Unit,    // Callback for edit button
    private val onDeleteClick: (Course) -> Unit   // Callback for delete button
) : ListAdapter<Course, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return CourseViewHolder(binding)
    }

    // Called to display data at specified position
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)  // Get course at position
        holder.bind(course)              // Bind data to views
    }

    // ViewHolder - holds references to views in item_course.xml
    inner class CourseViewHolder(
        private val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(course: Course) {
            binding.apply {
                // Set text values
                tvCourseName.text = course.courseName
                tvCourseCode.text = course.courseCode
                tvCreditHours.text = "Credit Hours: ${course.creditHours}"
                chipCourseType.text = course.courseType

                // Set click listeners
                btnEdit.setOnClickListener {
                    onEditClick(course)  // Trigger callback
                }

                btnDelete.setOnClickListener {
                    onDeleteClick(course)  // Trigger callback
                }
            }
        }
    }

    // DiffUtil for efficient updates
    private class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        // Check if items represent the same course (by ID)
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        // Check if item contents are the same
        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem  // Uses data class equals()
        }
    }
}
```

### Setting up RecyclerView in HomeFragment
```kotlin
private fun setupRecyclerView() {
    // Create adapter with callbacks
    courseAdapter = CourseAdapter(
        onEditClick = { course ->
            navigateToEditCourse(course)
        },
        onDeleteClick = { course ->
            showDeleteConfirmationDialog(course)
        }
    )
    
    // Configure RecyclerView
    binding.recyclerViewCourses.apply {
        adapter = courseAdapter
        layoutManager = LinearLayoutManager(context)  // Vertical list
    }
}
```

### ListAdapter vs RecyclerView.Adapter
| Feature | ListAdapter | RecyclerView.Adapter |
|---------|-------------|---------------------|
| Data Updates | `submitList()` - automatic diffing | Manual `notifyDataSetChanged()` |
| Performance | Calculates minimal changes | Refreshes entire list |
| DiffUtil | Built-in support | Manual implementation |

---

## Text Display & Styling

### Text Appearance Styles Used

| Style | Font Size | Weight | Usage |
|-------|-----------|--------|-------|
| `Headline5` | 24sp | Normal | Screen titles ("Course Management", "Add New Course") |
| `Headline6` | 20sp | Medium | Course name in list |
| `Body1` | 16sp | Normal | Labels ("Course Type") |
| `Body2` | 14sp | Normal | Course code in list |
| `Caption` | 12sp | Normal | Credit hours info |

### Text Color Usage

| Attribute | Description | Example |
|-----------|-------------|---------|
| `?attr/colorPrimary` | Purple brand color | Titles |
| `?android:attr/textColorPrimary` | Main text (black/white) | Course names |
| `?android:attr/textColorSecondary` | Muted text (gray) | Course codes, credit hours |

### Examples from XML
```xml
<!-- Title with brand color -->
<TextView
    android:text="Course Management"
    android:textAppearance="@style/TextAppearance.MaterialComponents.Headline5"
    android:textColor="?attr/colorPrimary" />

<!-- Primary text (course name) -->
<TextView
    android:id="@+id/tvCourseName"
    android:textAppearance="@style/TextAppearance.MaterialComponents.Headline6"
    android:textColor="?android:attr/textColorPrimary" />

<!-- Secondary text (course code) -->
<TextView
    android:id="@+id/tvCourseCode"
    android:textAppearance="@style/TextAppearance.MaterialComponents.Body2"
    android:textColor="?android:attr/textColorSecondary" />
```

---

## Input Fields & Border Colors

### TextInputLayout with Outline Box
```xml
<com.google.android.material.textfield.TextInputLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Course Name"
    app:boxBackgroundMode="outline"
    app:boxCornerRadiusBottomEnd="8dp"
    app:boxCornerRadiusBottomStart="8dp"
    app:boxCornerRadiusTopEnd="8dp"
    app:boxCornerRadiusTopStart="8dp">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etCourseName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="textCapWords" />
</com.google.android.material.textfield.TextInputLayout>
```

### Border Color States (Material Design Default)
| State | Border Color |
|-------|--------------|
| Default (unfocused) | Gray outline |
| Focused | `colorPrimary` (Purple #FF6200EE) |
| Error | Red |
| Disabled | Light gray |

**How Border Color Changes:**
- Material TextInputLayout automatically handles border color changes
- When user taps field → Focused state → Border turns purple
- When validation fails → Error state → Border turns red

### Input Types Used
| Field | Input Type | Effect |
|-------|------------|--------|
| Course Name | `textCapWords` | Auto-capitalizes each word |
| Course Code | `textCapCharacters` | All uppercase |
| Credit Hours | `number` | Number keyboard only |

### Setting Error Programmatically
```kotlin
// In AddCourseFragment
if (courseName.isEmpty()) {
    binding.etCourseName.error = "Course name is required"
    return  // Stop form submission
}
```

---

## Buttons & Interactions

### Button Styles

#### 1. Primary Button (Filled)
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnAddCourse"
    android:layout_width="match_parent"
    android:layout_height="56dp"
    android:text="Add Course"
    android:textSize="16sp"
    app:cornerRadius="8dp" />
```
- **Background:** `colorPrimary` (Purple)
- **Text Color:** `colorOnPrimary` (White)
- **Corner Radius:** 8dp

#### 2. Outlined Button
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnEdit"
    android:text="Edit"
    android:layout_height="36dp"
    app:cornerRadius="18dp"
    style="@style/Widget.MaterialComponents.Button.OutlinedButton" />
```
- **Background:** Transparent
- **Border:** `colorPrimary` (Purple)
- **Text Color:** `colorPrimary` (Purple)

#### 3. Danger Button (Delete)
```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnDelete"
    android:text="Delete"
    app:backgroundTint="@android:color/holo_red_light"
    style="@style/Widget.MaterialComponents.Button.OutlinedButton" />
```
- **Background:** Red (`holo_red_light`)
- **Indicates destructive action**

### FloatingActionButton (FAB)
```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fabAddCourse"
    android:src="@android:drawable/ic_input_add"
    android:tint="@android:color/white"
    app:backgroundTint="?attr/colorPrimary"
    app:maxImageSize="32dp" />
```

### Button Click Handling
```kotlin
// In Fragment
binding.btnAddCourse.setOnClickListener {
    addCourse()
}

binding.fabAddCourse.setOnClickListener {
    navigateToAddCourse()
}
```

### Button State Changes
```kotlin
// Disable button during operation
binding.btnAddCourse.isEnabled = false
binding.btnAddCourse.text = "Adding..."

// Re-enable after operation
binding.btnAddCourse.isEnabled = true
binding.btnAddCourse.text = "Add Course"
```

---

## Error Handling

### Comprehensive Error Handling Pattern
```kotlin
private fun addCourse() {
    try {
        // Main logic
        lifecycleScope.launch {
            try {
                val result = courseRepository.addCourse(course)
                
                if (result.isSuccess) {
                    // Success handling
                } else {
                    // Handle failure result
                    showErrorToUser("Failed to add course", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                // Handle coroutine exceptions
                showErrorToUser("Unexpected error", e)
            }
        }
    } catch (e: Exception) {
        // Handle outer exceptions
        showErrorToUser("Critical error", e)
    }
}

private fun showErrorToUser(message: String, exception: Throwable?) {
    val fullMessage = if (exception != null) {
        "$message: ${exception.message}"
    } else {
        message
    }
    Toast.makeText(context, fullMessage, Toast.LENGTH_LONG).show()
    Log.e(TAG, "showErrorToUser: $fullMessage", exception)
}
```

### Validation Errors
```kotlin
// Empty field validation
if (courseName.isEmpty()) {
    binding.etCourseName.error = "Course name is required"
    return
}

// Invalid number validation
if (creditHours == null || creditHours <= 0) {
    binding.etCreditHours.error = "Please enter valid credit hours"
    return
}
```

---

## App Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        APP START                                │
│                            │                                    │
│                            ▼                                    │
│                    ┌───────────────┐                           │
│                    │  MainActivity │                           │
│                    │               │                           │
│                    │ • Firebase    │                           │
│                    │   Init        │                           │
│                    │ • Navigation  │                           │
│                    │   Setup       │                           │
│                    └───────┬───────┘                           │
│                            │                                    │
│                            ▼                                    │
│                    ┌───────────────┐                           │
│                    │ HomeFragment  │◄─────────────────────┐    │
│                    │               │                      │    │
│                    │ • Course List │                      │    │
│                    │ • FAB Button  │                      │    │
│                    │ • Empty State │                      │    │
│                    └───────┬───────┘                      │    │
│                            │                              │    │
│              ┌─────────────┼─────────────┐               │    │
│              │             │             │               │    │
│              ▼             ▼             ▼               │    │
│     ┌─────────────┐ ┌─────────────┐ ┌─────────────┐     │    │
│     │ FAB Click   │ │ Edit Click  │ │Delete Click │     │    │
│     └──────┬──────┘ └──────┬──────┘ └──────┬──────┘     │    │
│            │               │               │             │    │
│            ▼               ▼               ▼             │    │
│     ┌─────────────┐ ┌─────────────┐ ┌─────────────┐     │    │
│     │   Add       │ │    Edit     │ │   Delete    │     │    │
│     │   Course    │ │   Course    │ │   Dialog    │     │    │
│     │   Fragment  │ │   Fragment  │ │             │     │    │
│     └──────┬──────┘ └──────┬──────┘ └──────┬──────┘     │    │
│            │               │               │             │    │
│            ▼               ▼               ▼             │    │
│     ┌─────────────┐ ┌─────────────┐ ┌─────────────┐     │    │
│     │  Validate   │ │  Validate   │ │  Confirm    │     │    │
│     │  & Save     │ │  & Update   │ │  & Delete   │     │    │
│     └──────┬──────┘ └──────┬──────┘ └──────┬──────┘     │    │
│            │               │               │             │    │
│            ▼               ▼               ▼             │    │
│     ┌─────────────────────────────────────────────┐     │    │
│     │           Firebase Realtime Database         │     │    │
│     │                                              │     │    │
│     │  courses/                                    │     │    │
│     │    ├── -NxYz123ABC/                         │     │    │
│     │    │     ├── courseName: "..."              │     │    │
│     │    │     ├── courseCode: "..."              │     │    │
│     │    │     └── ...                            │     │    │
│     │    └── ...                                  │     │    │
│     └─────────────────────┬───────────────────────┘     │    │
│                           │                             │    │
│                           │ Real-time Sync              │    │
│                           │                             │    │
│                           ▼                             │    │
│                    ┌─────────────┐                      │    │
│                    │ CourseRepo  │                      │    │
│                    │ getAllCourses()────────────────────┘    │
│                    │ (Flow)      │                           │
│                    └─────────────┘                           │
└─────────────────────────────────────────────────────────────────┘
```

---

## Android Manifest

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Internet permission for Firebase -->
    <uses-permission android:name="android.permission.INTERNET" />
    
    <!-- Network state for offline handling -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.CourseManagement">
        
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <!-- Main entry point -->
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

---

## Viva Questions & Answers

### Q1: What is the purpose of `data class` in Kotlin?
**A:** Data class automatically generates `equals()`, `hashCode()`, `toString()`, and `copy()` functions. It's perfect for model classes like `Course` that primarily hold data.

### Q2: Why do we need a no-argument constructor in Course class?
**A:** Firebase uses reflection to deserialize JSON data into objects. It requires a no-argument constructor to create instances before setting property values.

### Q3: How does Firebase Realtime Database work?
**A:** Firebase Realtime Database stores data as JSON and synchronizes it in real-time across all connected clients. When data changes, all listeners receive updates automatically.

### Q4: What is View Binding and why use it?
**A:** View Binding generates binding classes for layouts, providing type-safe, null-safe access to views. It eliminates `findViewById()` calls and catches errors at compile time.

### Q5: What is the difference between `setValue()` and `updateChildren()` in Firebase?
**A:** `setValue()` overwrites the entire node. `updateChildren()` only updates specified fields, preserving other data.

### Q6: How does Navigation Component work?
**A:** Navigation Component uses a navigation graph XML to define destinations (fragments) and actions (transitions). `NavController` handles fragment transactions automatically.

### Q7: What is `Flow` in Kotlin?
**A:** Flow is a cold asynchronous stream that emits values over time. It's used for reactive programming, like observing database changes.

### Q8: Why use `ListAdapter` instead of `RecyclerView.Adapter`?
**A:** ListAdapter includes DiffUtil integration, which calculates minimal changes between lists for efficient updates without refreshing the entire list.

### Q9: What is the purpose of `lifecycleScope`?
**A:** `lifecycleScope` is a coroutine scope tied to the fragment's lifecycle. Coroutines launched in it are automatically cancelled when the fragment is destroyed.

### Q10: How is the delete confirmation dialog created?
**A:** Using `AlertDialog.Builder` with `setTitle()`, `setMessage()`, `setPositiveButton()`, `setNegativeButton()`, `setIcon()`, and `show()`.

---

## Summary Table

| Component | File | Purpose |
|-----------|------|---------|
| Main Activity | `MainActivity.kt` | App entry point, Firebase init, Navigation setup |
| Home Screen | `HomeFragment.kt` | Display course list, FAB, empty state |
| Add Course | `AddCourseFragment.kt` | Form to add new course |
| Edit Course | `EditCourseFragment.kt` | Form to edit existing course |
| Course Model | `Course.kt` | Data class for course entity |
| Repository | `CourseRepository.kt` | Firebase CRUD operations |
| Adapter | `CourseAdapter.kt` | RecyclerView adapter for course list |
| Colors | `colors.xml` | Color definitions |
| Theme | `themes.xml` | App theme and styling |
| Navigation | `mobile_navigation.xml` | Navigation graph |
| Main Layout | `activity_main.xml` | NavHostFragment container |
| Home Layout | `fragment_home.xml` | Course list UI |
| Add Layout | `fragment_add_course.xml` | Add form UI |
| Edit Layout | `fragment_edit_course.xml` | Edit form UI |
| Item Layout | `item_course.xml` | Course card in list |

---

**Good luck with your viva! 🎓**
"# coursemanagement" 
