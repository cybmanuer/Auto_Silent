package com.example.autosilent_madapp;


//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private LatLng selectedLocation;
//    private String selectedAddress;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.selectfrommap);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        Button saveLocationButton = findViewById(R.id.saveLocationButton);
//        saveLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedLocation != null) {
//                    DatabaseHelper dbHelper = new DatabaseHelper(MapActivity.this);
//                    long result = dbHelper.insertLocation(selectedAddress, selectedLocation.latitude, selectedLocation.longitude);
//                    if (result != -1) {
//                        Toast.makeText(MapActivity.this, "Location saved successfully!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(MapActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(MapActivity.this, "Failed to save location.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(MapActivity.this, "Please select a location on the map.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Set a default location (e.g., New York)
//        LatLng defaultLocation = new LatLng(40.7128, -74.0060);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                selectedLocation = latLng;
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
//
//                // Get the address from the selected location
//                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                    if (addresses != null && !addresses.isEmpty()) {
//                        Address address = addresses.get(0);
//                        selectedAddress = address.getAddressLine(0);
//                    } else {
//                        selectedAddress = "Unknown Address";
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    selectedAddress = "Unknown Address";
//                }
//            }
//        });
//    }
//}


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng selectedLocation;
    private String selectedAddress;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectfrommap);

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Save location button
        Button saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLocation != null) {
                    DatabaseHelper dbHelper = new DatabaseHelper(MapActivity.this);

                    // Check if the location already exists in the database
                    if (!dbHelper.isLocationExists(selectedLocation.latitude, selectedLocation.longitude)) {
                        // Insert the location into the database
                        long result = dbHelper.insertLocation(selectedAddress, selectedLocation.latitude, selectedLocation.longitude);
                        if (result != -1) {
                            Toast.makeText(MapActivity.this, "Location saved successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MapActivity.this, "Failed to save location.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MapActivity.this, "Location already exists in the database.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapActivity.this, "Please select a location on the map.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get the user's current location and move the camera to it
        getCurrentLocation();

        // Set a listener for map clicks
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedLocation = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

                // Get the address from the selected location
                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        selectedAddress = address.getAddressLine(0);
                    } else {
                        selectedAddress = "Unknown Address";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    selectedAddress = "Unknown Address";
                }
            }
        });
    }

    // Method to get the user's current location
    private void getCurrentLocation() {
        try {
            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        // Move the camera to the current location
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                    } else {
                        Toast.makeText(MapActivity.this, "Unable to get current location.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}



// search function
