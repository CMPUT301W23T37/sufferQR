package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterPage extends AppCompatActivity {

    TextInputEditText usernameRegister = findViewById(R.id.user_name_register);
    TextInputEditText emailRegister = findViewById(R.id.email_register);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }
}