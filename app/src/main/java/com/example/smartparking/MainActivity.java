package com.example.smartparking;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceControl;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartparking.fragmant.FragmentBooking;
import com.example.smartparking.fragmant.FragmentHome;
import com.example.smartparking.fragmant.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnView = findViewById(R.id.bottomNav);
        //for clicking tha item
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                //for each icon clicked in anvigation bar
                 if (id==R.id.btnHome) {

                     loadFrag(new FragmentHome(),true);
                } else if (id == R.id.btnBooking) {

                     loadFrag(new FragmentBooking(),false);

                } else if (id == R.id.btnProfile) {
                     loadFrag(new FragmentProfile(),false);

                }
                return true;
            }
        });

        // by defautl nav button selection
        bnView.setSelectedItemId(R.id.btnHome);

    }
    public void loadFrag(Fragment fragment , boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft  = fm.beginTransaction();
        if(flag){
        ft.add(R.id.fmLayout,fragment );

        }
        else {
        ft.replace(R.id.fmLayout ,fragment);
        }

        ft.commit();

    }
}