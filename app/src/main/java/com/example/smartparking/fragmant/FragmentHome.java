package com.example.smartparking.fragmant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.example.smartparking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentHome extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // This will call onMapReady when the map is ready to be used
        }

        return v;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission
        if (isLocationPermissionGranted()) {
            // Permission granted, display both current and fixed locations
            displayCurrentLocation();
            displayFixedLocation();
        } else {
            // Permission not granted, request it
            requestLocationPermission();
        }
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void displayCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            // Get current location
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng currentLocation = new LatLng(latitude, longitude);

                            // Convert vector drawable to bitmap
                            Bitmap customMarkerBitmap = getBitmapFromVectorDrawable(requireContext(), R.drawable.car);

                            // Add marker at current location with custom icon and move camera
                            mMap.addMarker(new MarkerOptions()
                                    .position(currentLocation)
                                    .title("Your Location")
                                    .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15)); // Zoom level is set to 15
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void displayFixedLocation() {
        // Fixed location coordinates for MSIT Parking
        double msitLatitude = 22.510519;
        double msitLongitude = 88.415305;
        LatLng msitParking = new LatLng(msitLatitude, msitLongitude);

        // Convert vector drawable to bitmap
        Bitmap customMarkerBitmap1 = getBitmapFromVectorDrawable(requireContext(), R.drawable.parking_sign_2526);


        // Add marker at MSIT Parking and move camera
        mMap.addMarker(new MarkerOptions().position(msitParking).title("MSIT PARKING").icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap1)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(msitParking, 15)); // Zoom level is set to 15
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, display both current and fixed locations
                displayCurrentLocation();
                displayFixedLocation();
            }
        }
    }

    // Utility method to convert vector drawable to bitmap
    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof VectorDrawableCompat) {
            drawable = (VectorDrawableCompat) drawable.mutate();
        } else if (drawable instanceof android.graphics.drawable.VectorDrawable) {
            drawable = (android.graphics.drawable.VectorDrawable) drawable.mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
