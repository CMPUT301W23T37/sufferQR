package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityDashBoardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;

/**
 * This class is the dash board of the application
 * it will have a short cut to camera, the total number of the score collected
 * the total number of code been scanned, the highest score ever, and the score for
 * the last code scanned. Also, a small scale of map will display at the bottom with
 * indications of surrounding scannable code
 *
 */
public class DashBoard extends DrawerBase {

    ActivityDashBoardBinding activityDashBoardBinding;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(activityDashBoardBinding.getRoot());
        allocateActivityTitle("Suffer QR");

        // Get AAID
        String android_id = Settings.Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        DocumentReference userAAID = db.collection("Player").document(android_id);
        userAAID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot userInfo = task.getResult();
                    if(!userInfo.exists()){
                        Intent i = new Intent(DashBoard.this, RegisterPage.class);
                        startActivity(i);
                    }
                } else {
                    Log.d(TAG, "failed with ", task.getException());
                }
            }
        });


    }
}