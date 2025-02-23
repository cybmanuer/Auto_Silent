//package com.example.autosilent_madapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Button btnOn, btnOff;
//    private ImageButton imgSavedLocations, imgEditLocation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Initialize views
//        btnOn = findViewById(R.id.btnOn);
//        btnOff = findViewById(R.id.btnOff);
//        imgSavedLocations = findViewById(R.id.SavedLocations);
//        imgEditLocation = findViewById(R.id.imgEditLocation);
//
//        // Set up ------On ----------button to start the service for silent mode
//        btnOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Example: Start your silent mode service (uncomment and adjust as needed)
//                // Intent serviceIntent = new Intent(HomeActivity.this, SilentModeService.class);
//                // startService(serviceIntent);
//                Toast.makeText(MainActivity.this, "Service Started", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Set up Off button to stop the service
//        btnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Example: Stop your silent mode service (uncomment and adjust as needed)
//                // Intent serviceIntent = new Intent(HomeActivity.this, SilentModeService.class);
//                // stopService(serviceIntent);
//                Toast.makeText(MainActivity.this, "Service Stopped", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Open the Saved Locations Activity when the saved locations image button is clicked
//        imgSavedLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SavedLocationsActivity.class);//have to shange the class file
//                startActivity(intent);
//            }
//        });
//
//        // Open the Edit Locations Activity when the edit locations image button is clicked
//        imgEditLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, addlocation.class);
//                startActivity(intent);
//            }
//        });
//    }
//}


//  VERSION 2 IMPLIMENTING BACKEND LOGIC FOR SILIENCING THE DEVICE annd ading code for ON and OFF BUTTON.
//IT WORKING ---- IT TURNING TO SILENT MODE WHEN ON BUTTON IS CLICKED ..... AND RESTORING BACK   (MANUAL OPERATION , NO LINK OF LOCATIOS YET)


//
//package com.example.autosilent_madapp;
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Button btnOn, btnOff;
//    private ImageButton imgSavedLocations, imgEditLocation;
//    private AudioManager audioManager;
//
//    // Variables to store previous volume levels
//    private int previousRingVolume;
//    private int previousMusicVolume;
//    private int previousAlarmVolume;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Initialize views
//        btnOn = findViewById(R.id.btnOn);
//        btnOff = findViewById(R.id.btnOff);
//        imgSavedLocations = findViewById(R.id.SavedLocations);
//        imgEditLocation = findViewById(R.id.imgEditLocation);
//
//        // Initialize AudioManager
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        // Set up On button to activate silent mode
//        btnOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    if (notificationManager.isNotificationPolicyAccessGranted()) {
//                        activateSilentMode();
//                    } else {
//                        // Ask the user to grant access
//                        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//                        startActivityForResult(intent, 1); // Request code for later handling
//                    }
//                } else {
//                    activateSilentMode(); // For API levels below 23
//                }
//            }
//        });
//
//        // Set up Off button to restore normal volume
//        btnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                restoreNormalVolume();
//            }
//        });
//
//        // Open the Saved Locations Activity when the saved locations image button is clicked
//        imgSavedLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SavedLocationsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Open the Edit Locations Activity when the edit locations image button is clicked
//        imgEditLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, addlocation.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void activateSilentMode() {
//        // Save current volume before changing to silent
//        previousRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
//        previousMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        previousAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
//
//        // Set ringer mode to silent and all volumes to 0
//        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
//
//        Toast.makeText(MainActivity.this, "Silent Mode Activated", Toast.LENGTH_SHORT).show();
//    }
//
//    private void restoreNormalVolume() {
//        // Restore previous volume levels for each stream
//        audioManager.setStreamVolume(AudioManager.STREAM_RING, previousRingVolume, 0);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousMusicVolume, 0);
//        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, previousAlarmVolume, 0);
//
//        // Set ringer mode back to normal
//        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//
//        Toast.makeText(MainActivity.this, "Normal Mode Restored", Toast.LENGTH_SHORT).show();
//    }
//}



//---------------------------------VERSION 3 FETECHING THE LOCATION AND MERGING WITH PREVISION VERSION  and ITS WORKIG AS GOOD AS I REQURIED

// need update where if the phone alredy in silent mode it turns off--whant to correct iit
//package com.example.autosilent_madapp;
//
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.location.Location;
//import android.media.AudioManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Looper;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Button btnOn, btnOff;
//    private ImageButton imgSavedLocations, imgEditLocation;
//    private AudioManager audioManager;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private boolean isMonitoring = false;
//
//    // Variables to store previous volume levels
//    private int previousRingVolume;
//    private int previousMusicVolume;
//    private int previousAlarmVolume;
//
//    // Database helper
//    private DatabaseHelper databaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Initialize views
//        btnOn = findViewById(R.id.btnOn);
//        btnOff = findViewById(R.id.btnOff);
//        imgSavedLocations = findViewById(R.id.SavedLocations);
//        imgEditLocation = findViewById(R.id.imgEditLocation);
//
//        // Initialize AudioManager
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//
//        // Initialize DatabaseHelper
//        databaseHelper = new DatabaseHelper(this);
//
//        // Initialize FusedLocationProviderClient
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        // Set up On button to start monitoring
//        btnOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isMonitoring) {
//                    startLocationMonitoring();
//                    isMonitoring = true;
//                    Toast.makeText(MainActivity.this, "Monitoring started", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Set up Off button to stop monitoring and restore normal volume
//        btnOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isMonitoring) {
//                    stopLocationMonitoring();
//                    isMonitoring = false;
//                    restoreNormalVolume();
//                    Toast.makeText(MainActivity.this, "Monitoring stopped", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        // Open the Saved Locations Activity when the saved locations image button is clicked
//        imgSavedLocations.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, SavedLocationsActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Open the Edit Locations Activity when the edit locations image button is clicked
//        imgEditLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, addlocation.class);
//                startActivity(intent);
//            }
//        });
//
//        // Initialize location callback
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) return;
//                for (Location location : locationResult.getLocations()) {
//                    checkLocationAndActivateSilentMode(location);
//                }
//            }
//        };
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            // Check if the user granted the Notification Policy Access permission
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                if (notificationManager.isNotificationPolicyAccessGranted()) {
//                    activateSilentMode();
//                } else {
//                    Toast.makeText(this, "Permission denied. Cannot activate silent mode.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private void startLocationMonitoring() {
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000); // 10 seconds
//        locationRequest.setFastestInterval(5000); // 5 seconds
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//            } else {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
//            }
//        } else {
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//        }
//    }
//
//    private void stopLocationMonitoring() {
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//
//
//
//    private void checkLocationAndActivateSilentMode(Location currentLocation) {
//        Cursor cursor = databaseHelper.getAllLocations();
//        if (cursor != null) {
//            try {
//                int latitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
//                int longitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);
//
//                // Check if column indices are valid
//                if (latitudeIndex < 0 || longitudeIndex < 0) {
//                    Toast.makeText(this, "Error: Invalid column index in database.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (cursor.moveToFirst()) {
//                    do {
//                        double latitude = cursor.getDouble(latitudeIndex);
//                        double longitude = cursor.getDouble(longitudeIndex);
//                        Location storedLocation = new Location("");
//                        storedLocation.setLatitude(latitude);
//                        storedLocation.setLongitude(longitude);
//
//                        float distance = currentLocation.distanceTo(storedLocation);
//                        if (distance <= 50) { // Within 50 meters
//                            activateSilentMode();
//                            return;
//                        }
//                    } while (cursor.moveToNext());
//                }
//            } catch (Exception e) {
//                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            } finally {
//                cursor.close();
//            }
//        }
//        restoreNormalVolume();
//    }
//
//    int c=0;
//
//    private void activateSilentMode() {
//        if (audioManager == null) {
//            Toast.makeText(this, "AudioManager is not available.", Toast.LENGTH_SHORT).show();
//            return;
//        }
////        int c=0;
//        try {
//            // Save current volume before changing to silent
//            previousRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
//            previousMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            previousAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
//
//            // Set ringer mode to silent and all volumes to 0
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//            audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
//            c=c+1;
//            if(c==1){
//                Toast.makeText(MainActivity.this, "Silent Mode Activated", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        catch (Exception e) {
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
//
//    int j=0;
//
//    private void restoreNormalVolume() {
//        if (audioManager == null) {
//            Toast.makeText(this, "AudioManager is not available.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            // Restore previous volume levels for each stream
//            audioManager.setStreamVolume(AudioManager.STREAM_RING, previousRingVolume, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousMusicVolume, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, previousAlarmVolume, 0);
//
//            // Set ringer mode back to normal
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//            j=j+1;
//            if(j==1){
//                Toast.makeText(MainActivity.this, "Normal Mode Restored", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//}


//---------------------------------  VERSION 4:   making the code to run in background and created a new file Locationservice to hold the background processing code
//it working good as requried fial version

//
package com.example.autosilent_madapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnOn, btnOff;
    private ImageButton imgSavedLocations, imgEditLocation;
    private boolean isMonitoring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
        imgSavedLocations = findViewById(R.id.SavedLocations);
        imgEditLocation = findViewById(R.id.imgEditLocation);

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMonitoring) {
                    startLocationMonitoringService();
                    isMonitoring = true;
                    Toast.makeText(MainActivity.this, "Monitoring started", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMonitoring) {
                    stopLocationMonitoringService();
                    isMonitoring = false;
                    Toast.makeText(MainActivity.this, "Monitoring stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgSavedLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SavedLocationsActivity.class);
                startActivity(intent);
            }
        });

        imgEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addlocation.class);
                startActivity(intent);
            }
        });
    }

    private void startLocationMonitoringService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.isNotificationPolicyAccessGranted()) {
                Intent serviceIntent = new Intent(this, LocationService.class);
                ContextCompat.startForegroundService(this, serviceIntent);
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, 1);
            }
        } else {
            Intent serviceIntent = new Intent(this, LocationService.class);
            startService(serviceIntent);
        }
    }

    private void stopLocationMonitoringService() {
        Intent serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.setAction("STOP_SERVICE"); // Optional: to tell the service to stop
        stopService(serviceIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager.isNotificationPolicyAccessGranted()) {
                    startLocationMonitoringService();
                } else {
                    Toast.makeText(this, "Permission denied. Cannot activate silent mode.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
