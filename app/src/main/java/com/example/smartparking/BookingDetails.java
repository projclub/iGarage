package com.example.smartparking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingDetails extends AppCompatActivity {

    private int selectedDuration = 0; // Initially no duration selected
    private boolean durationSelected = false; // Flag to track if duration button is clicked
    private Calendar selectedDateTime = Calendar.getInstance(); // Calendar instance to store selected date and time

    private FirebaseFirestore db; // Firestore instance

    private TextView bookingDateTimeTextView;
    private TextView bookingDurationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Find the back button
        ImageButton backButton = findViewById(R.id.backButton);

        // Find duration buttons
        Button hrOneButton = findViewById(R.id.Hr_one_Button);
        Button hrTwoButton = findViewById(R.id.Hr_Two_Button);
        Button hrThreeButton = findViewById(R.id.Hr_Three_Button);

        // Find the TextViews and Button for displaying booking data and cancellation
        bookingDateTimeTextView = findViewById(R.id.bookingDateTime);
        bookingDurationTextView = findViewById(R.id.bookingDuration);
        Button cancelButton = findViewById(R.id.buttonBookingCancel);

        // Set click listeners for duration buttons
        hrOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDuration = 1;
                durationSelected = true;
                showDurationDialog(selectedDuration);
            }
        });

        hrTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDuration = 2;
                durationSelected = true;
                showDurationDialog(selectedDuration);
            }
        });

        hrThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDuration = 3;
                durationSelected = true;
                showDurationDialog(selectedDuration);
            }
        });

        // Set click listener for confirm button
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!durationSelected) {
                    showToast("Please select the duration");
                    return;
                }
                showDateTimePicker();
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

        // Set click listener for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking();
            }
        });

        // Fetch and display booking details on activity start
        fetchBookingDetails();
    }

    // Method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to show a duration dialog
    private void showDurationDialog(int duration) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selected Duration");
        builder.setMessage(duration + " hr booking selected");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform any action on OK button click
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.create().show(); // Show the dialog
    }

    // Method to show date and time picker dialog
    private void showDateTimePicker() {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, monthOfYear);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showTimePicker();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    // Method to show time picker dialog
    private void showTimePicker() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);

                // After selecting date and time, show the custom dialog
                showCustomDialog();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    // Method to show custom dialog with selected date, time, and duration
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Details");
        builder.setMessage("Date: " + selectedDateTime.getTime().toString() + "\n" +
                "Duration: " + selectedDuration + " hr");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog

                // Update booking details in Firestore
                updateBookingDetails();
            }
        });
        builder.create().show(); // Show the dialog
    }

    // Method to update booking details in Firestore
    private void updateBookingDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            Map<String, Object> booking = new HashMap<>();
            booking.put("date", selectedDateTime.getTime().toString());
            booking.put("duration", selectedDuration);

            db.collection("bookings").document(userId)
                    .set(booking)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Booking details updated successfully");
                        fetchBookingDetails(); // Fetch and display the updated details
                    })
                    .addOnFailureListener(e -> showToast("Failed to update booking details"));
        } else {
            showToast("User not logged in");
        }
    }

    // Method to fetch booking details from Firestore and display them
    private void fetchBookingDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("bookings").document(userId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String date = documentSnapshot.getString("date");
                                Long duration = documentSnapshot.getLong("duration");

                                bookingDateTimeTextView.setText(date != null ? date : "N/A");
                                bookingDurationTextView.setText(duration != null ? duration + " hr" : "N/A");
                            } else {
                                showToast("No booking found");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to fetch booking details");
                        }
                    });
        }
    }

    // Method to cancel the booking
    private void cancelBooking() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("bookings").document(userId).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("Booking cancelled successfully");
                            bookingDateTimeTextView.setText("N/A");
                            bookingDurationTextView.setText("N/A");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to cancel booking");
                        }
                    });
        } else {
            showToast("User not logged in");
        }
    }
}
