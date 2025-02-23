package com.example.autosilent_madapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String ACTION_START_MONITORING = "com.example.autosilent_madapp.ACTION_START_MONITORING";
    private static final String ACTION_STOP_MONITORING = "com.example.autosilent_madapp.ACTION_STOP_MONITORING";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("NotificationReceiver", "Received action: " + action); // Log the received action

        if (ACTION_START_MONITORING.equals(action)) {
            // Start the LocationService when the ON button is clicked
            Intent startIntent = new Intent(context, LocationService.class);
            startIntent.setAction(ACTION_START_MONITORING);
            context.startService(startIntent);
        } else if (ACTION_STOP_MONITORING.equals(action)) {
            // Stop the LocationService when the OFF button is clicked
            Intent stopIntent = new Intent(context, LocationService.class);
            stopIntent.setAction(ACTION_STOP_MONITORING);
            context.startService(stopIntent);
        }
    }
}
