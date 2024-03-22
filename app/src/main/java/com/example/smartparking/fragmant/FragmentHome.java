package com.example.smartparking.fragmant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartparking.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


 //this is for map

public class FragmentHome extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment ;

    public FragmentHome() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;

       // mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Initialize map fragment
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}