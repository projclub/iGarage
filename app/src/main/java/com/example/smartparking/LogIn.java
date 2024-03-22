package com.example.smartparking;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    ProgressDialog progressDialog ;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonSignup);
        //forgot password
        TextView textView = findViewById(R.id.my_text_view);

        auth = FirebaseAuth.getInstance();

        //for loading animation
        progressDialog = new ProgressDialog(LogIn.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to your account");


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for showing the loding
                progressDialog.show();
                //auth
                auth.signInWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       progressDialog.dismiss();
                       if(task.isSuccessful()){
                           Intent intent = new Intent(LogIn.this,MainActivity.class);
                           startActivity(intent);
                       }
                       else {
                           Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                });

            }
        });
        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(LogIn.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
