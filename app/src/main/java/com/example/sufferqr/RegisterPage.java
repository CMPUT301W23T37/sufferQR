package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This method is used to register page to the user
 * and ask user to set up their user name
 * give an email, choose an qr id, in order to create an account
 */
public class RegisterPage extends AppCompatActivity {

    private TextInputEditText usernameRegisterText;
    TextInputLayout usernameRegisterLayout;
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
        confirmRegisterButton.setEnabled(false);
        changeQrIdButton = findViewById(R.id.qr_id_register);

        // Username edit text
        usernameRegisterText = findViewById(R.id.user_name_register);
        usernameRegisterText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
        usernameRegisterLayout = findViewById(R.id.userName_register_layout);

        usernameRegisterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0){
                    CollectionReference collectionReference = db.collection("Player");
                    collectionReference.whereNotEqualTo("name", null).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                System.err.println("Listen failed: " + error);
                            }
                            String ms = editable.toString();
                            for (DocumentSnapshot doc : value.getDocuments()){
//                                Log.d(TAG, "name: " + doc.get("name"));
                                if(!ms.equals(doc.get("name"))){
//                                    Log.d(TAG, "not find");
                                    confirmRegisterButton.setEnabled(true);
                                    usernameRegisterLayout.setErrorEnabled(false);
                                    usernameRegisterLayout.setError("");
                                    usernameRegisterLayout.setHelperText("Username looks Good!");
                                } else {
                                    confirmRegisterButton.setEnabled(false);
                                    usernameRegisterLayout.setErrorEnabled(true);
                                    usernameRegisterLayout.setError("Username already exists");
                                    break;
                                }
                            }
                        }
                    });
                } else{
                    usernameRegisterLayout.setError("Username cannot be empty");
                }
            }
        });



        // User email edit text
        emailRegisterText = findViewById(R.id.email_register);
        String emailCheckAt = "@";
        String emailCheckCom = ".";

        // set up qr id
        qrIdGenerated = randomString();
        CollectionReference collectionReference = db.collection("Player");
        collectionReference.whereNotEqualTo("qrid", null).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    System.err.println("Listen failed: " + error);
                }

                for (DocumentSnapshot doc : value.getDocuments()){
                    if(!qrIdGenerated.equals(doc.get("qrid"))){
                        changeQrIdButton.setText(qrIdGenerated);
                    } else{
                        qrIdGenerated = randomString();
                    }
                }
            }
        });

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
                    int QRcount = 0;
                    int highest = 0;
                    int lowest = 0;
                    int sum = 0;
                    List<Long> scores = new ArrayList<Long>();
                    scores.add(0L);
                    boolean allowEmail = true;
                    boolean allowQrid = true;
                    boolean allowScan = true;

                    // Get AAID
                    String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    User user = new User(usernameEntered, emailEntered, qrIdGenerated, QRcount, highest, lowest, sum, scores, allowEmail, allowQrid, allowScan);
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