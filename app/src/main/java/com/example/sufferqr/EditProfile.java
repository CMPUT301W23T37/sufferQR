package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * allow user to edit their profile
 * Change in username
 * Change in email
 * Change in privacy setting: allow other user to view owner email, qr id, and code scanned
 */
public class EditProfile extends AppCompatActivity {

    private TextInputEditText username;
    TextInputLayout userName_editProfile_layout;
    private TextInputEditText email;

    private Button delButton;
    private Button cancelButton;
    private Button applyButton;

    SwitchMaterial allowEmail;
    SwitchMaterial allowQrid;
    SwitchMaterial allowScanRecord;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Bundle extras = getIntent().getExtras();
        String oldName = extras.getString("username");

        username = findViewById(R.id.userName_editProfile);
        userName_editProfile_layout = findViewById(R.id.userName_editProfile_layout);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /*
            Auto detect name and email changed
             */
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0){
                    final CollectionReference collectionReference = db.collection("Player");
                    collectionReference.whereNotEqualTo("name", null).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                System.err.println("Listen failed: " + error);
                            }
                            String ms = editable.toString();
                            for (DocumentSnapshot doc : value.getDocuments()){
//                                Log.d(TAG, "name: " + doc.get("name"));
                                if(!ms.equals(doc.get("name")) || ms.equals(oldName)){
                                    Log.d(TAG, "not find");
                                    userName_editProfile_layout.setErrorEnabled(false);
                                    userName_editProfile_layout.setError("");
                                    userName_editProfile_layout.setHelperText("Username looks Good!");
                                } else {
                                    userName_editProfile_layout.setErrorEnabled(true);
                                    userName_editProfile_layout.setError("Username already exists");
                                    break;
                                }
                            }
                        }
                    });
                } else{
                    userName_editProfile_layout.setError("Username cannot be empty");
                }
            }
        });

        email = findViewById(R.id.userEmail_editProfile);
        allowEmail = findViewById(R.id.allow_email);
        allowQrid = findViewById(R.id.allow_qrid);
        allowScanRecord = findViewById(R.id.allow_scan_record);

        // Get AAID and give it to the db
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DocumentReference userInfo = db.collection("Player").document(android_id);

        userInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                username.setText((String) document.get("name"));
                email.setText((String) document.get("email"));
                allowEmail.setChecked((Boolean) document.get("allowViewEmail"));
                allowQrid.setChecked((Boolean) document.get("allowViewQrid"));
                allowScanRecord.setChecked((Boolean) document.get("allowViewScanRecord"));

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
                        u.setAllowViewEmail(allowEmail.isChecked());
                        u.setAllowViewQrid(allowQrid.isChecked());
                        u.setAllowViewScanRecord(allowScanRecord.isChecked());
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
