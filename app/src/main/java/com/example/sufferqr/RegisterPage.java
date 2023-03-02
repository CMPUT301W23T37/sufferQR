package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class RegisterPage extends AppCompatActivity {

    private TextInputEditText usernameRegisterText;
    private Button changeQrIdButton;
    private Button confirmRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usernameRegisterText = findViewById(R.id.user_name_register);
        changeQrIdButton = findViewById(R.id.qr_id_register);
        confirmRegisterButton = findViewById(R.id.confirm_register);

        setContentView(R.layout.activity_register_page);

//        String inputText = Objects.requireNonNull(usernameRegisterText.getEditableText()).toString();
    }
}