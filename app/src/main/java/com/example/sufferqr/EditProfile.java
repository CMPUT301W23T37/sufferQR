package com.example.sufferqr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * allow user to edit their profile
 */
public class EditProfile extends AppCompatActivity {

    private TextInputEditText username;
    private TextInputEditText email;

    private Button delButton;
    private Button cancelButton;
    private Button applyButton;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        username = findViewById(R.id.userName_editProfile);
        email = findViewById(R.id.userEmail_editProfile);

        // Get AAID and give it to the db
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference userInfo = db.collection("Player").document(android_id);

        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                username.setText((String) document.get("name"));
                email.setText((String) document.get("email"));

            }
        });

        // cancel action
        cancelButton = findViewById(R.id.cancelButton_editProfile);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // apply change action
        applyButton = findViewById(R.id.applyButton_editProfile);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String changedUserName_editProfile = username.getEditableText().toString();
                String changedUserEmail_editProfile = email.getEditableText().toString();

                userInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User u = documentSnapshot.toObject(User.class);
                        u.setName(changedUserName_editProfile);
                        u.setEmail(changedUserEmail_editProfile);
                        db.collection("Player").document(android_id).set(u);
                    }
                });
                // Go to dash board if apply changes
                Intent i = new Intent(EditProfile.this, DashBoard.class);
                startActivity(i);
                finish();
            }
        });

        // delete Action
        delButton = findViewById(R.id.delButton_editProfile);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.delete();

                Intent i = new Intent(EditProfile.this, DashBoard.class);
                startActivity(i);
                finish();
            }
        });
    }

}
