package com.example.smartparking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BookingDetails extends AppCompatActivity {

    private int selectedDuration = 0; // Initially no duration selected
    private boolean durationSelected = false; // Flag to track if duration button is clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Find the back button
        ImageButton backButton = findViewById(R.id.backButton);

        // Find duration buttons
        Button hrOneButton = findViewById(R.id.Hr_one_Button);
        Button hrTwoButton = findViewById(R.id.Hr_Two_Button);
        Button hrThreeButton = findViewById(R.id.Hr_Three_Button);

        // Set click listeners for duration buttons
        hrOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDuration = 1;
                showToast("1hr booking selected");
                durationSelected = true;
            }
        });

        hrTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDuration = 2;
                showToast("2hr booking selected");
                durationSelected = true;
            }
        });

        hrThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDuration = 3;
                showToast("3hr booking selected");
                durationSelected = true;
            }
        });

        // Set click listener for confirm button
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (durationSelected) {
                    showConfirmationDialog();
                } else {
                    showToast("Please select the time");
                }
            }
        });

        // Set click listener for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your back button logic here
                onBackPressed();
            }
        });
    }

    // Method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to show a confirmation dialog
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage(selectedDuration + "hr booking selected");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform any action on OK button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.create().show(); // Show the dialog
    }
}
