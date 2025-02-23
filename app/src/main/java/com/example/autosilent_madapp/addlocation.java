package com.example.autosilent_madapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//
//public class addlocation extends AppCompatActivity {
//
//    private Button buttonAddLocation;
//    private ListView listViewLocations;
//    private ArrayList<String> locations;
//    private ArrayAdapter<String> adapter;
//    // Use ActivityResultLauncher to get the result from SelectLocationActivity
//    private ActivityResultLauncher<Intent> selectLocationLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addlocation);
//
//        // Initialize views
//        buttonAddLocation = findViewById(R.id.buttonAddLocation);
//        listViewLocations = findViewById(R.id.listViewLocations);
//
//        // Initialize the list and adapter
//        locations = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
//        listViewLocations.setAdapter(adapter);
//
//        // Register the ActivityResultLauncher for launching the map selector
//        selectLocationLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                            double latitude = result.getData().getDoubleExtra("latitude", 0.0);
//                            double longitude = result.getData().getDoubleExtra("longitude", 0.0);
//                            String locationString = "Lat: " + latitude + ", Lng: " + longitude;
//                            addNewLocation(locationString);
//                        }
//                    }
//                }
//        );
//
//        // When "Add Location" button is clicked, launch SelectLocationActivity
//        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(addlocation.this, SelectLocationActivity.class);
//                selectLocationLauncher.launch(intent);
//            }
//        });
//
//        // Set up an item click listener to delete locations from the list
//        listViewLocations.setOnItemClickListener((parent, view, position, id) -> {
//            String locationToDelete = locations.get(position);
//            deleteLocation(locationToDelete);
//        });
//    }
//
//    private void addNewLocation(String location) {
////        locations.add(location);
////        adapter.notifyDataSetChanged();
////        Toast.makeText(this, "Location Added", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(addlocation.this, SelectLocationActivity.class);
//        selectLocationLauncher.launch(intent);
//    }
//
//    private void deleteLocation(String location) {
//        locations.remove(location);
//        adapter.notifyDataSetChanged();
//        Toast.makeText(this, "Location Deleted", Toast.LENGTH_SHORT).show();
//    }
//}


// --------------- VERSION 2 - IT CAN FETCH THE EXACT DETAILES OF THE CURRENT LOCATION it before the DATA BASE -

//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class addlocation extends AppCompatActivity {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
//    private FusedLocationProviderClient fusedLocationClient;
//    private TextView textViewLocation;
//    private Button buttonAddLocation;
//    private Geocoder geocoder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addlocation);
//
//        textViewLocation = findViewById(R.id.textViewLocation);
//        buttonAddLocation = findViewById(R.id.buttonAddLocation);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        buttonAddLocation.setOnClickListener(v -> getCurrentLocation());
//    }
//
//    private void getCurrentLocation() {
//        // Check for location permissions
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Request permissions if not granted
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            // Permission already granted, get the location
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                // Get latitude and longitude
//                                double latitude = location.getLatitude();
//                                double longitude = location.getLongitude();
//
//                                // Get the address using Geocoder
//                                String address = getAddressFromLocation(latitude, longitude);
//
//                                // Display location details
//                                String locationDetails = "Latitude: " + latitude +
//                                        "\nLongitude: " + longitude +
//                                        "\nAddress: " + address;
//                                textViewLocation.setText(locationDetails);
//                            } else {
//                                textViewLocation.setText("Unable to retrieve location.");
//                            }
//                        }
//                    });
//        }
//    }
//
//    private String getAddressFromLocation(double latitude, double longitude) {
//        try {
//            // Get the address list using Geocoder
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//
//                // Construct the address string
//                StringBuilder addressText = new StringBuilder();
//                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                    addressText.append(address.getAddressLine(i)).append("\n");
//                }
//                return addressText.toString();
//            } else {
//                return "No address found";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error fetching address";
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, get the location
//                getCurrentLocation();
//            } else {
//                // Permission denied, show a message
//                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}



//------------------ VERSION 3 - USED TO STORE LOCATION DATA TO BACKEDND-----------------


//
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class addlocation extends AppCompatActivity {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
//    private FusedLocationProviderClient fusedLocationClient;
//    private TextView textViewLocation;
//    private Button buttonAddLocation;
//    private Geocoder geocoder;
//    private DatabaseHelper databaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addlocation);
//
//        textViewLocation = findViewById(R.id.textViewLocation);
//        buttonAddLocation = findViewById(R.id.buttonAddLocation);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        geocoder = new Geocoder(this, Locale.getDefault());
//        databaseHelper = new DatabaseHelper(this);
//
//        buttonAddLocation.setOnClickListener(v -> getCurrentLocation());
//    }
//
//
//
//    private void getCurrentLocation() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                double latitude = location.getLatitude();
//                                double longitude = location.getLongitude();
//                                String address = getAddressFromLocation(latitude, longitude);
//
//                                // Check if the location already exists in the database
//                                if (databaseHelper.isLocationExists(latitude, longitude)) {
//                                    Toast.makeText(addlocation.this, "Location already exists", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // Store location in the database
//                                    long id = databaseHelper.insertLocation(address, latitude, longitude);
//                                    if (id != -1) {
//                                        Toast.makeText(addlocation.this, "Location saved to database", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    // Display location details
//                                    String locationDetails = "Latitude: " + latitude +
//                                            "\nLongitude: " + longitude +
//                                            "\nAddress: " + address;
//                                    textViewLocation.setText(locationDetails);
//                                }
//                            } else {
//                                textViewLocation.setText("Unable to retrieve location.");
//                            }
//                        }
//                    });
//        }
//    }
//
//
//
//
//    private String getAddressFromLocation(double latitude, double longitude) {
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//                StringBuilder addressText = new StringBuilder();
//                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                    addressText.append(address.getAddressLine(i)).append("\n");
//                }
//                return addressText.toString();
//            } else {
//                return "No address found";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error fetching address";
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            } else {
//                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}




//-----  it opens google maps
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//public class addlocation extends AppCompatActivity {
//
//    private FusedLocationProviderClient fusedLocationClient;  // For accessing location services
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addlocation); // Replace with your layout file
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);  // Initialize location services
//
//        Button addLocationButton = findViewById(R.id.AddLocationfrommap);
//        addLocationButton.setOnClickListener(v -> openMapWithCurrentLocation());
//    }
//
//    // Function to open Google Maps with the user's current location
//    private void openMapWithCurrentLocation() {
//        // Check for location permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // Request location permission if not granted
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        } else {
//            // Get the last known location
//            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        // Open Google Maps with the user's current location
//                        double latitude = location.getLatitude();
//                        double longitude = location.getLongitude();
//                        String geoUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
//                        intent.setPackage("com.google.android.apps.maps");
//
//                        // Check if there's an app to handle the intent
//                        if (intent.resolveActivity(getPackageManager()) != null) {
//                            startActivity(intent);
//                        } else {
//                            System.out.println("Google Maps is not installed on this device.");
//                        }
//                    } else {
//                        System.out.println("Could not retrieve location.");
//                    }
//                }
//            });
//        }
//    }
//}
//



//VERSION 4 INCLUDED GOOGLE MAP BUTTON TO FETCH DATA FROM GOOGLE MAP


//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class addlocation extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addlocation);
//
//        // Find the button by its ID
//        Button fetchLocationButton = findViewById(R.id.AddLocationfrommap);
//
//        // Set an OnClickListener for the button
//        fetchLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Create an Intent to launch the MapActivity
//                Intent intent = new Intent(addlocation.this, MapActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//}


//VERSION 5 ------- MERGING LOCATION FROM GOOGLE AND CURRENT LOCATION


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class addlocation extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewLocation;
    private Button buttonAddLocation, AddLocationfrommap;
    private Geocoder geocoder;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlocation);

        textViewLocation = findViewById(R.id.textViewLocation);
        buttonAddLocation = findViewById(R.id.buttonAddLocation);
        AddLocationfrommap = findViewById(R.id.AddLocationfrommap);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        databaseHelper = new DatabaseHelper(this);

        buttonAddLocation.setOnClickListener(v -> getCurrentLocation());

        // Set up OnClickListener for "AddLocationfrommap" button
        AddLocationfrommap.setOnClickListener(v -> openMapActivity());

    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        mapActivityLauncher.launch(intent);
//        startActivity(intent);
    }
    ActivityResultLauncher<Intent> mapActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            double latitude = intent.getDoubleExtra("latitude", 0.0);
                            double longitude = intent.getDoubleExtra("longitude", 0.0);
                            String address = getAddressFromLocation(latitude, longitude);

                            // Check if the location already exists in the database
                            if (databaseHelper.isLocationExists(latitude, longitude)) {
                                Toast.makeText(addlocation.this, "Location already exists in the database", Toast.LENGTH_SHORT).show();
                            } else {
                                // Store location in the database
                                long id = databaseHelper.insertLocation(address, latitude, longitude);

                                // Display location details
                                String locationDetails = "Latitude: " + latitude +
                                        "\nLongitude: " + longitude +
                                        "\nAddress: " + address;
                                textViewLocation.setText(locationDetails);
                                Toast.makeText(addlocation.this, "Location saved to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });



    //Define result launcher
//    ActivityResultLauncher<Intent> mapActivityLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent intent = result.getData();
//                        if (intent != null) {
//                            double latitude = intent.getDoubleExtra("latitude", 0.0);
//                            double longitude = intent.getDoubleExtra("longitude", 0.0);
//                            String address = getAddressFromLocation(latitude, longitude);
//
//                            // Store location in the database
//                            long id = databaseHelper.insertLocation(address, latitude, longitude);
//
//                            // Display location details
//                            String locationDetails = "Latitude: " + latitude +
//                                    "\nLongitude: " + longitude +
//                                    "\nAddress: " + address;
//                            textViewLocation.setText(locationDetails);
//                            Toast.makeText(addlocation.this, "Location saved to database", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                }
//            });




    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                String address = getAddressFromLocation(latitude, longitude);

                                // Check if the location already exists in the database
                                if (databaseHelper.isLocationExists(latitude, longitude)) {
                                    Toast.makeText(addlocation.this, "Location already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Store location in the database
                                    long id = databaseHelper.insertLocation(address, latitude, longitude);
                                    if (id != -1) {
                                        Toast.makeText(addlocation.this, "Location saved to database", Toast.LENGTH_SHORT).show();
                                    }

                                    // Display location details
                                    String locationDetails = "Latitude: " + latitude +
                                            "\nLongitude: " + longitude +
                                            "\nAddress: " + address;
                                    textViewLocation.setText(locationDetails);
                                }
                            } else {
                                textViewLocation.setText("Unable to retrieve location.");
                            }
                        }
                    });
        }
    }


    private String getAddressFromLocation(double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressText.append(address.getAddressLine(i)).append("\n");
                }
                return addressText.toString();
            } else {
                return "No address found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching address";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




//
//
////-----------------------------  VERSION 6  STORING BOTH DATA TO DATABASE
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class addlocation extends AppCompatActivity {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
//    private FusedLocationProviderClient fusedLocationClient;
//    private TextView textViewLocation;
//    private Button buttonAddLocation;
//    private Button buttonAddLocationFromMap;
//    private static final int SELECT_LOCATION_REQUEST_CODE = 1002;
//    private Geocoder geocoder;
//    private DatabaseHelper databaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addlocation);
//
//        textViewLocation = findViewById(R.id.textViewLocation);
//        buttonAddLocation = findViewById(R.id.buttonAddLocation);
//        buttonAddLocationFromMap = findViewById(R.id.AddLocationfrommap);
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        geocoder = new Geocoder(this, Locale.getDefault());
//        databaseHelper = new DatabaseHelper(this);
//
//        buttonAddLocation.setOnClickListener(v -> getCurrentLocation());
//        buttonAddLocationFromMap.setOnClickListener(v -> openMapForLocationSelection());
//    }
//
//    private void openMapForLocationSelection() {
//        Intent intent = new Intent(this, MapActivity.class);
//        startActivityForResult(intent, SELECT_LOCATION_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SELECT_LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            double latitude = data.getDoubleExtra("latitude", 0.0);
//            double longitude = data.getDoubleExtra("longitude", 0.0);
//            String address = data.getStringExtra("address"); // Retrieve the address from Intent
//            saveLocationFromMap(latitude, longitude, address); // Pass the address to the method
//        }
//    }
//
//    private void saveLocationFromMap(double latitude, double longitude, String address) {
//        // Check if the location already exists in the database
//        if (databaseHelper.isLocationExists(latitude, longitude)) {
//            Toast.makeText(this, "Location already exists", Toast.LENGTH_SHORT).show();
//        } else {
//            // Store location in the database
//            long id = databaseHelper.insertLocation(address, latitude, longitude);
//            if (id != -1) {
//                Toast.makeText(this, "Location saved to database", Toast.LENGTH_SHORT).show();
//            }
//
//            // Display location details
//            String locationDetails = "Latitude: " + latitude +
//                    "\nLongitude: " + longitude +
//                    "\nAddress: " + address;
////            textViewLocation.setText(locationDetails);
//        }
//    }
//
//    private void getCurrentLocation() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        } else {
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                double latitude = location.getLatitude();
//                                double longitude = location.getLongitude();
//                                String address = getAddressFromLocation(latitude, longitude);
//
//                                // Check if the location already exists in the database
//                                if (databaseHelper.isLocationExists(latitude, longitude)) {
//                                    Toast.makeText(addlocation.this, "Location already exists", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    // Store location in the database
//                                    long id = databaseHelper.insertLocation(address, latitude, longitude);
//                                    if (id != -1) {
//                                        Toast.makeText(addlocation.this, "Location saved to database", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    // Display location details
//                                    String locationDetails = "Latitude: " + latitude +
//                                            "\nLongitude: " + longitude +
//                                            "\nAddress: " + address;
//                                    textViewLocation.setText(locationDetails);
//                                }
//                            } else {
//                                textViewLocation.setText("Unable to retrieve location.");
//                            }
//                        }
//                    });
//        }
//    }
//
//    private String getAddressFromLocation(double latitude, double longitude) {
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//                StringBuilder addressText = new StringBuilder();
//                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                    addressText.append(address.getAddressLine(i)).append("\n");
//                }
//                return addressText.toString();
//            } else {
//                return "No address found";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error fetching address";
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getCurrentLocation();
//            } else {
//                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}
