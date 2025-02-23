//-----VERSION 1 USED TO DISPLAY ALL DATA FROM DATABASE

//package com.example.autosilent_madapp;
//
//
//import android.database.Cursor;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import java.util.ArrayList;
//
//public class SavedLocationsActivity extends AppCompatActivity {
//
//    private ListView listViewSavedLocations;
//    private DatabaseHelper databaseHelper;
//    private static final String TAG = "SavedLocationsActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.savedlocations);
//
//        listViewSavedLocations = findViewById(R.id.listViewSavedLocations);
//        databaseHelper = new DatabaseHelper(this);
//
//        // Fetch saved locations from the database
//        displaySavedLocations();
//    }
//
//    private void displaySavedLocations() {
//        Cursor cursor = null;
//        ArrayList<String> savedLocations = new ArrayList<>();
//
//        try {
//            cursor = databaseHelper.getAllLocations();
//
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
//                    // Get column indices, with error checking
//                    int addressColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS);
//                    int latitudeColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
//                    int longitudeColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);
//
//                    // Check if the columns exist
//                    if (addressColumnIndex == -1 || latitudeColumnIndex == -1 || longitudeColumnIndex == -1) {
//                        Log.e(TAG, "One or more columns not found in the cursor");
//                        continue; // Skip this row
//                    }
//
//                    // Extract data from the cursor
//                    String address = cursor.getString(addressColumnIndex);
//                    double latitude = cursor.getDouble(latitudeColumnIndex);
//                    double longitude = cursor.getDouble(longitudeColumnIndex);
//
//                    // Create a string representation of the location
//                    String locationString = "Address: " + address +
//                            "\nLatitude: " + latitude +
//                            "\nLongitude: " + longitude;
//
//                    savedLocations.add(locationString);
//                } while (cursor.moveToNext());
//            } else {
//                Log.i(TAG, "No locations found in the database");
//                savedLocations.add("No locations saved yet."); // Display a message if no locations are saved
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Error fetching locations from database", e);
//            savedLocations.add("Error fetching locations."); // Display an error message
//        } finally {
//            if (cursor != null) {
//                cursor.close(); // Close the cursor in a finally block
//            }
//        }
//
//        // Display saved locations in the ListView
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedLocations);
//        listViewSavedLocations.setAdapter(adapter);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//}



//---------------------- V2 ADDED DELETE ACTION FOR THE LIST ITEM if the iteam is hold for a long


package com.example.autosilent_madapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SavedLocationsActivity extends AppCompatActivity {

    private ListView listViewSavedLocations;
    private DatabaseHelper databaseHelper;
    private ArrayList<LocationItem> savedLocations;
    private ArrayAdapter<LocationItem> adapter;
    private static final String TAG = "SavedLocationsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocations);

        listViewSavedLocations = findViewById(R.id.listViewSavedLocations);
        databaseHelper = new DatabaseHelper(this);
        savedLocations = new ArrayList<>();

        loadLocations();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedLocations);
        listViewSavedLocations.setAdapter(adapter);

        // Set a long-click listener to show delete option
        listViewSavedLocations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final LocationItem selectedItem = savedLocations.get(position);
                new AlertDialog.Builder(SavedLocationsActivity.this)
                        .setTitle("Delete Location")
                        .setMessage("Do you want to delete this location?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int rowsDeleted = databaseHelper.deleteLocation(selectedItem.getId());
                                if (rowsDeleted > 0) {
                                    savedLocations.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(SavedLocationsActivity.this, "Location deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SavedLocationsActivity.this, "Failed to delete location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;  // Indicates the long-click event is consumed
            }
        });
    }

    // Load locations from the database into the savedLocations list
    private void loadLocations() {
        Cursor cursor = null;
        try {
            cursor = databaseHelper.getAllLocations();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get the column indices
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    int addressIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ADDRESS);
                    int latitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
                    int longitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);

                    if (addressIndex == -1 || latitudeIndex == -1 || longitudeIndex == -1) {
                        Log.e(TAG, "One or more columns not found in the cursor");
                        continue;
                    }

                    // Extract the data
                    String address = cursor.getString(addressIndex);
                    double latitude = cursor.getDouble(latitudeIndex);
                    double longitude = cursor.getDouble(longitudeIndex);

                    // Create a display string for the item
                    String details =
                            "Latitude: " + latitude +
                            "\nLongitude: " + longitude +
                             "\nAddress: " + address  + "\n";

                    // Add the item (with its ID) to the list
                    savedLocations.add(new LocationItem(id, details));
                } while (cursor.moveToNext());
            } else {
                Log.i(TAG, "No locations found in the database");
                savedLocations.add(new LocationItem(-1, "No locations saved yet."));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching locations from database", e);
            savedLocations.add(new LocationItem(-1, "Error fetching locations."));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Inner class representing a location item
    private class LocationItem {
        private int id;
        private String details;

        public LocationItem(int id, String details) {
            this.id = id;
            this.details = details;
        }

        public int getId() {
            return id;
        }

        // The adapter will call toString() to display the item in the list
        @Override
        public String toString() {
            return details;
        }
    }
}
