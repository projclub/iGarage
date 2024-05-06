package com.example.smartparking.fragmant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.smartparking.BookingDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.smartparking.R;

public class FragmentBooking extends Fragment {

    private DatabaseReference parkingPlaceRef;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView freeSpacesTextView;

    private CardView  bookingCardView;

    public FragmentBooking() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // Initialize Firebase database reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        parkingPlaceRef = firebaseDatabase.getReference("parking_places").child("place1");

        // Find TextViews in the layout
        nameTextView = view.findViewById(R.id.name_text_view);
        addressTextView = view.findViewById(R.id.address_text_view);
        freeSpacesTextView = view.findViewById(R.id.free_spaces_text_view);
        bookingCardView = view.findViewById(R.id.booking_card_view1);

        // Set click listener to the card view
        bookingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookingDetails activity
                startActivity(new Intent(getActivity(), BookingDetails.class));
            }
        });

        // Fetch data from Firebase and update card view
        fetchParkingPlaceData();

        return view;
    }

    // Method to fetch data from Firebase and update card view
    private void fetchParkingPlaceData() {
        parkingPlaceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if dataSnapshot exists and contains data
                if (dataSnapshot.exists()) {
                    // Retrieve data from Firebase
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    Long freeSpaces = dataSnapshot.child("free_spaces").getValue(Long.class);

                    // Update TextViews with fetched data
                    nameTextView.setText(name != null ? name : "");
                    addressTextView.setText(address != null ? address : "");
                    freeSpacesTextView.setText(freeSpaces != null ? "Free Spaces: " + freeSpaces : "Free Spaces: N/A");
                } else {
                    // Handle the case where no data exists in the dataSnapshot
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(getActivity(), "Failed to fetch data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
