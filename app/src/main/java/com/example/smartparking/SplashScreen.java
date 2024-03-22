package com.example.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000; // Splash screen timeout in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Start MainActivity after the splash timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity
                Intent intent = new Intent(SplashScreen.this, LogIn.class);
                startActivity(intent);

                // Close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
