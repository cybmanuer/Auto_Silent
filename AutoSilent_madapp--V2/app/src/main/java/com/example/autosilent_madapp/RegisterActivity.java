package com.example.autosilent_madapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etOtp;
    private Button btnSendOtp, btnRegister;
    private TextView tvStatus;
//    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize views
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etOtp = findViewById(R.id.et_otp);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnRegister = findViewById(R.id.btn_register);
        tvStatus = findViewById(R.id.tv_status);

        // Initialize database helper
//        dbHelper = new DatabaseHelper(this);

        // Send OTP Button Click Listener
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String phone = etPhone.getText().toString().trim();
//                if (phone.isEmpty() || phone.length() < 10) {
//                    tvStatus.setText("Please enter a valid phone number.");
//                } else {
//                    // Simulate sending OTP (in a real app, integrate with an OTP service like Firebase or Twilio)
//                    tvStatus.setText("OTP sent to " + phone);
//                }
            }
        });

        // Register Button Click Listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);

//                String name = etName.getText().toString().trim();
//                String phone = etPhone.getText().toString().trim();
//                String otp = etOtp.getText().toString().trim();
//
//                if (name.isEmpty() || phone.isEmpty() || otp.isEmpty()) {
//                    tvStatus.setText("Please fill all fields.");
//                } else if (otp.length() != 6) { // Assuming OTP is 6 digits
//                    tvStatus.setText("Invalid OTP.");
//                } else {
//                    // Save user details to the database
//                    long result = saveUserDetails(name, phone);
//                    if (result != -1) {
//                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
//                        tvStatus.setText("Registration successful!");
//                    } else {
//                        tvStatus.setText("Registration failed. Please try again.");
//                    }
//                }
            }
        });
    }

    // Method to save user details to the database
    private long saveUserDetails(String name, String phone) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_NAME, name);
//        values.put(DatabaseHelper.COLUMN_PHONE, phone);
//        return db.insert(DatabaseHelper.TABLE_NAME, null, values);
        return 0;
    }

}