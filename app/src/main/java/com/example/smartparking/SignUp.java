package com.example.smartparking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smartparking.Models.Users;
import com.example.smartparking.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    TextView loginD;
    EditText etEmail,etPassword,etUserName;
    Button signUpBtn;
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
FirebaseDatabase database;
//for loading
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_sign_up);
        loginD = findViewById(R.id.etLogin);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        signUpBtn = findViewById(R.id.buttonSignup);
        etUserName = findViewById(R.id.editTextProfile);

        //for loading animation
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

        // Initialize Firebase Auth
                auth = FirebaseAuth.getInstance();

                //realtime databace
        database = FirebaseDatabase.getInstance();
        //sign up button after click effect
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for show progressdialog or loding animation
                progressDialog.show();

                //for aut using email and password
                auth.createUserWithEmailAndPassword
                        ( etEmail.getText().toString(),etPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //turn off the progressDialog or loading
                        progressDialog.dismiss();

                      //if signup successfull
                        if(task.isSuccessful()){

                            //for storing user information in databace

                            Users user = new Users(etUserName.getText().toString() , etEmail.getText().toString(),etPassword.getText().toString());
                            //getting the id of an user and identify uniquly
                            String id = task.getResult().getUser().getUid();
                            //creating the child
                            database.getReference().child("Users").child(id).setValue(user);

                            Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });



        //for sign up page to login page
       loginD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });

  //  getSupportActionBar().hide();
    }
}