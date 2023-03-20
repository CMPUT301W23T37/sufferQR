package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This method is used to register page to the user
 * and ask user to set up their user name
 * give an email, choose an qr id, in order to create an account
 */
public class RegisterPage extends AppCompatActivity {

    private TextInputEditText usernameRegisterText;
    private TextInputEditText emailRegisterText;
    private Button changeQrIdButton;
    private MaterialButton confirmRegisterButton;

    private String currentEnter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String qrIdGenerated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        confirmRegisterButton = findViewById(R.id.confirm_register);
        changeQrIdButton = findViewById(R.id.qr_id_register);

        // Username edit text
        usernameRegisterText = findViewById(R.id.user_name_register);
        usernameRegisterText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});

        // User email edit text
        emailRegisterText = findViewById(R.id.email_register);
        String emailCheckAt = "@";
        String emailCheckCom = ".";

        // set up qr id
        qrIdGenerated = randomString();
        changeQrIdButton.setText(qrIdGenerated);

        // qr id choice
        changeQrIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrIdGenerated = randomString();
                changeQrIdButton.setText(qrIdGenerated);
            }
        });

        // Click on confirm action
        confirmRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(usernameRegisterText.getEditableText().toString())){
                    Toast.makeText(RegisterPage.this, "Please enter an username!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(emailRegisterText.getEditableText().toString())) {
                    Toast.makeText(RegisterPage.this, "Please enter an email!", Toast.LENGTH_SHORT).show();
                } else if (!emailRegisterText.getEditableText().toString().contains(emailCheckAt)){
                    Toast.makeText(RegisterPage.this, "Please enter a correct email!", Toast.LENGTH_SHORT).show();
                } else if (emailRegisterText.getEditableText().toString().contains(emailCheckCom)) {

                    // set up new user
                    String usernameEntered = usernameRegisterText.getEditableText().toString();
                    String emailEntered = emailRegisterText.getEditableText().toString();
                    String region = "NAW";
                    int QRcount = 0;
                    int highest = 0;
                    int lowest = 0;
                    int sum = 0;
                    List<Long> scores = new ArrayList<Long>();
                    scores.add(0L);

                    // Get AAID
                    String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    User user = new User(usernameEntered, emailEntered, qrIdGenerated, region, QRcount, highest, lowest, sum, scores);
                    db.collection("Player").document(android_id).set(user);
                    finish();
                } else{
                    Toast.makeText(RegisterPage.this, "Please enter a correct email!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    /**
     * This method is used to generate random string for qr id
     * @return string of 12 random character
     */
    public String randomString(){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(12);
        for(int i = 0; i <= 12; i++){
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}