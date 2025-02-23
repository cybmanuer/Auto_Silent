package com.example.autosilent_madapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Timer for 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Redirect to MainActivity after 3 seconds
                Intent intent = new Intent(splash.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000); // 3000 ms = 3 seconds
    }
}
