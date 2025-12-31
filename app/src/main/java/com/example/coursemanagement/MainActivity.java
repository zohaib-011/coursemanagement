package com.example.coursemanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.coursemanagement.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "onCreate: Starting MainActivity initialization");
            super.onCreate(savedInstanceState);

            initializeFirebase();
            setupUI();

            Log.i(TAG, "onCreate: MainActivity initialization completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Critical error during MainActivity initialization", e);
            showCriticalError("Failed to initialize application", e);
        }
    }

    private void initializeFirebase() {
        try {
            Log.d(TAG, "initializeFirebase: Starting Firebase initialization");

            FirebaseApp firebaseApp;
            try {
                firebaseApp = FirebaseApp.initializeApp(this);
            } catch (Exception e) {
                Log.e(TAG, "initializeFirebase: Error initializing FirebaseApp", e);
                throw new Exception("Failed to initialize Firebase App: " + e.getMessage(), e);
            }

            Log.d(TAG, "initializeFirebase: Firebase App initialized: " + (firebaseApp != null ? firebaseApp.getName() : "default"));

            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                Log.d(TAG, "initializeFirebase: Firebase Database persistence enabled");
            } catch (Exception e) {
                Log.e(TAG, "initializeFirebase: Error enabling Firebase persistence", e);
                throw new Exception("Failed to enable Firebase persistence: " + e.getMessage(), e);
            }

            Log.i(TAG, "initializeFirebase: Firebase initialization completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "initializeFirebase: Critical Firebase initialization error", e);
            throw new RuntimeException("Firebase initialization failed: " + e.getMessage(), e);
        }
    }

    private void setupUI() {
        try {
            Log.d(TAG, "setupUI: Starting UI setup");

            try {
                binding = ActivityMainBinding.inflate(getLayoutInflater());
            } catch (Exception e) {
                Log.e(TAG, "setupUI: Error inflating layout", e);
                throw new Exception("Failed to inflate main layout: " + e.getMessage(), e);
            }

            setContentView(binding.getRoot());
            Log.d(TAG, "setupUI: Layout inflated and set");

            setupNavigation();

            Log.d(TAG, "setupUI: UI setup completed successfully");
        } catch (Exception e) {
            Log.e(TAG, "setupUI: Error during UI setup", e);
            throw new RuntimeException("UI setup failed: " + e.getMessage(), e);
        }
    }

    private void setupNavigation() {
        try {
            Log.d(TAG, "setupNavigation: Setting up simple navigation");

            NavController navController;
            try {
                navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            } catch (Exception e) {
                Log.e(TAG, "setupNavigation: Error finding navigation controller", e);
                throw new Exception("Failed to find navigation controller: " + e.getMessage(), e);
            }

            Log.d(TAG, "setupNavigation: Navigation controller found: " + navController);
            Log.d(TAG, "setupNavigation: Navigation setup completed");
        } catch (Exception e) {
            Log.e(TAG, "setupNavigation: Navigation setup failed", e);
            throw new RuntimeException("Navigation setup failed: " + e.getMessage(), e);
        }
    }

    private void showCriticalError(@NonNull String message, @NonNull Exception exception) {
        try {
            String fullMessage = message + ": " + exception.getMessage();
            Toast.makeText(this, fullMessage, Toast.LENGTH_LONG).show();
            Log.e(TAG, "showCriticalError: " + fullMessage, exception);
        } catch (Exception e) {
            Log.e(TAG, "showCriticalError: Error showing critical error message", e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            Log.d(TAG, "onDestroy: Cleaning up MainActivity");
            super.onDestroy();
        } catch (Exception e) {
            Log.e(TAG, "onDestroy: Error during cleanup", e);
        }
    }
}
