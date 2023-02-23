package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityUserProfileBinding;

/**
 * This class contains all the user information, the total number of the score collected
 * the total number of code been scanned, the highest score ever, and the lowest score
 * with player's username, email, QR id and the QR code from QR id. Modification to
 * Username and email is also available
 */
public class UserProfile extends DrawerBase {

    ActivityUserProfileBinding activityUserProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        allocateActivityTitle("Profile");
    }
}