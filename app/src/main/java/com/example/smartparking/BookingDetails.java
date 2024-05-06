package com.example.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class BookingDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Find the back button
        ImageButton backButton = findViewById(R.id.backButton);

        // Set click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform the redirection to the main activity
                Intent intent = new Intent(BookingDetails.this, MainActivity.class);

                // Set flags to clear the back stack and start the main activity as a new task
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // Start the main activity
                startActivity(intent);

                // Finish the current activity
                finish();
                onBackPressed();
            }
        });
    }
}
