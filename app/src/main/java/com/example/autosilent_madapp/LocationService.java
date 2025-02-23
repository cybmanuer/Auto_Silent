//package com.example.autosilent_madapp;
//
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.location.Location;
//import android.media.AudioManager;
//import android.os.Build;
//import android.os.IBinder;
//import android.os.Looper;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.app.NotificationCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//
//public class LocationService extends Service {
//
//    private static final String CHANNEL_ID = "LocationMonitoringService";
//    private boolean isMonitoring = false;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private DatabaseHelper databaseHelper;
//    private AudioManager audioManager;
//
//    private int previousRingVolume;
//    private int previousMusicVolume;
//    private int previousAlarmVolume;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        databaseHelper = new DatabaseHelper(this);
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
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
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && "STOP_SERVICE".equals(intent.getAction())) {
//            stopLocationMonitoring();
//            stopSelf();
//        } else {
//            startLocationMonitoring();
//            createNotification();
//        }
//        return START_STICKY;
//    }
//
//    private void createNotification() {
//        // Create a notification channel for Android Oreo and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel serviceChannel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Location Monitoring Service Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(serviceChannel);
//        }
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Location Monitoring Service")
//                .setContentText("Monitoring location for automatic silent mode activation.")
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(1, notification);
//    }
//
//    private void startLocationMonitoring() {
//        if (isMonitoring) return;
//        isMonitoring = true;
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000); // 10 seconds
//        locationRequest.setFastestInterval(5000); // 5 seconds
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.e("LocationService", "Location permission not granted.");
//            return;
//        }
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//    }
//
//    private void stopLocationMonitoring() {
//        if (!isMonitoring) return;
//        isMonitoring = false;
//
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//
//    private void checkLocationAndActivateSilentMode(Location currentLocation) {
//        Cursor cursor = databaseHelper.getAllLocations();
//        if (cursor != null) {
//            try {
//                int latitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
//                int longitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);
//
//                if (latitudeIndex < 0 || longitudeIndex < 0) {
//                    Log.e("LocationService", "Invalid column index in database.");
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
//                Log.e("LocationService", "Error: " + e.getMessage());
//            } finally {
//                cursor.close();
//            }
//        }
//        restoreNormalVolume();
//    }
//
//    private void activateSilentMode() {
//        if (audioManager == null) {
//            Log.e("LocationService", "AudioManager is not available.");
//            return;
//        }
//
//        try {
//            previousRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
//            previousMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            previousAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
//
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//            audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
//
//            Log.d("LocationService", "Silent Mode Activated");
//        } catch (Exception e) {
//            Log.e("LocationService", "Error: " + e.getMessage());
//        }
//    }
//
//    private void restoreNormalVolume() {
//        if (audioManager == null) {
//            Log.e("LocationService", "AudioManager is not available.");
//            return;
//        }
//
//        try {
//            audioManager.setStreamVolume(AudioManager.STREAM_RING, previousRingVolume, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousMusicVolume, 0);
//            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, previousAlarmVolume, 0);
//
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//
//            Log.d("LocationService", "Normal Mode Restored");
//        } catch (Exception e) {
//            Log.e("LocationService", "Error: " + e.getMessage());
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopLocationMonitoring();
//        Log.d("LocationService", "Service Destroyed");
//    }
//}
/////   ---------------- CORECTION AFTER THE UPDATE ----------------------


package com.example.autosilent_madapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    private static final String CHANNEL_ID = "LocationMonitoringService";
    private boolean isMonitoring = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private DatabaseHelper databaseHelper;
    private AudioManager audioManager;

    private int previousRingVolume;
    private int previousMusicVolume;
    private int previousAlarmVolume;

    // Variable to track if the app activated silent mode
    private boolean appActivatedSilentMode = false;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseHelper = new DatabaseHelper(this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    checkLocationAndActivateSilentMode(location);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "STOP_SERVICE".equals(intent.getAction())) {
            stopLocationMonitoring();
            restoreNormalVolume(); // Restore volume when service is stopped
            stopSelf();
        } else {
            // Save current volume settings before starting location monitoring
            saveCurrentVolumeSettings();
            startLocationMonitoring();
            createNotification();
        }
        return START_STICKY;
    }

    private void createNotification() {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Monitoring Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Monitoring Service")
                .setContentText("Monitoring location for automatic silent mode activation.")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    private void startLocationMonitoring() {
        if (isMonitoring) return;
        isMonitoring = true;

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LocationService", "Location permission not granted.");
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationMonitoring() {
        if (!isMonitoring) return;
        isMonitoring = false;

        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void saveCurrentVolumeSettings() {
        previousRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        previousMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        previousAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
    }

    private void checkLocationAndActivateSilentMode(Location currentLocation) {
        Cursor cursor = databaseHelper.getAllLocations();
        if (cursor != null) {
            try {
                int latitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
                int longitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);

                if (latitudeIndex < 0 || longitudeIndex < 0) {
                    Log.e("LocationService", "Invalid column index in database.");
                    return;
                }

                if (cursor.moveToFirst()) {
                    do {
                        double latitude = cursor.getDouble(latitudeIndex);
                        double longitude = cursor.getDouble(longitudeIndex);
                        Location storedLocation = new Location("");
                        storedLocation.setLatitude(latitude);
                        storedLocation.setLongitude(longitude);

                        float distance = currentLocation.distanceTo(storedLocation);
                        if (distance <= 50) { // Within 50 meters
                            // Check if the user has already enabled silent mode
                            if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                                activateSilentMode();
                                appActivatedSilentMode = true;
                            }
                            return;
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("LocationService", "Error: " + e.getMessage());
            } finally {
                cursor.close();
            }
        }
        // If outside silent zone, restore volume only if the app activated silent mode
        if (appActivatedSilentMode) {
            restoreNormalVolume();
            appActivatedSilentMode = false;
        }
    }

    private void activateSilentMode() {
        if (audioManager == null) {
            Log.e("LocationService", "AudioManager is not available.");
            return;
        }

        try {
            // Set ringer mode to silent and all volumes to 0
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);

            Log.d("LocationService", "Silent Mode Activated");
        } catch (Exception e) {
            Log.e("LocationService", "Error: " + e.getMessage());
        }
    }

    private void restoreNormalVolume() {
        if (audioManager == null) {
            Log.e("LocationService", "AudioManager is not available.");
            return;
        }

        try {
            // Restore previous volume levels for each stream
            audioManager.setStreamVolume(AudioManager.STREAM_RING, previousRingVolume, 0);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousMusicVolume, 0);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, previousAlarmVolume, 0);

            // Set ringer mode back to normal
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

            Log.d("LocationService", "Normal Mode Restored");
        } catch (Exception e) {
            Log.e("LocationService", "Error: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationMonitoring();
        Log.d("LocationService", "Service Destroyed");
    }
}
