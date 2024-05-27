package com.example.smartparking.fragmant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartparking.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentProfile extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ShapeableImageView profileImgSelector;
    private TextView usernameTextView, carNameModelTextView, carNumberTextView, mobileNumberTextView, emailTextView, text_name;
    private Button saveButton;
    private ImageButton editButton;

    private boolean isEditing = false;
    private Bitmap selectedBitmap;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImgSelector = view.findViewById(R.id.profileImgSelector);
        usernameTextView = view.findViewById(R.id.username);
        text_name = view.findViewById(R.id.text_name);
        carNameModelTextView = view.findViewById(R.id.text_car_name_model);
        carNumberTextView = view.findViewById(R.id.text_car_number);
        mobileNumberTextView = view.findViewById(R.id.text_mobile_number);
        emailTextView = view.findViewById(R.id.text_email);
        editButton = view.findViewById(R.id.edit_all);
       // saveButton = view.findViewById(R.id.save_button); // Ensure this ID matches your layout

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        profileImgSelector.setOnClickListener(v -> openImageSelector());
        editButton.setOnClickListener(v -> openEditProfileDialog());
       // saveButton.setOnClickListener(v -> saveChanges());

        fetchUserInfo();

        return view;
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImgSelector.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openEditProfileDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);

        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editCarNameModel = dialogView.findViewById(R.id.edit_car_name_model);
        EditText editCarNumber = dialogView.findViewById(R.id.edit_car_number);
        EditText editMobileNumber = dialogView.findViewById(R.id.edit_mobile_number);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String carNameModel = editCarNameModel.getText().toString();
            String carNumber = editCarNumber.getText().toString();
            String mobileNumber = editMobileNumber.getText().toString();
            String email = editEmail.getText().toString();

            saveUserInfoToFirestore(name, carNameModel, carNumber, mobileNumber, email);

            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void saveUserInfoToFirestore(String name, String carNameModel, String carNumber, String mobileNumber, String email) {
        DocumentReference userDocRef = db.collection("users").document(currentUser.getUid());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("carNameModel", carNameModel);
        userInfo.put("carNumber", carNumber);
        userInfo.put("mobileNumber", mobileNumber);
        userInfo.put("email", email);

        userDocRef.set(userInfo, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "User information updated successfully", Toast.LENGTH_SHORT).show();
                    fetchUserInfo();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update user information", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveChanges() {
        Toast.makeText(getContext(), "Changes saved", Toast.LENGTH_SHORT).show();
    }

    private void fetchUserInfo() {
        DocumentReference userDocRef = db.collection("users").document(currentUser.getUid());

        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String carNameModel = documentSnapshot.getString("carNameModel");
                        String carNumber = documentSnapshot.getString("carNumber");
                        String mobileNumber = documentSnapshot.getString("mobileNumber");
                        String email = documentSnapshot.getString("email");

                        usernameTextView.setText(name);
                        text_name.setText("Name: " + name);
                        carNameModelTextView.setText("Car: " + carNameModel);
                        carNumberTextView.setText("Car Number: " + carNumber);
                        mobileNumberTextView.setText("Mobile Number: " + mobileNumber);
                        emailTextView.setText("Email: " + email);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch user information", Toast.LENGTH_SHORT).show();
                });
    }
}
