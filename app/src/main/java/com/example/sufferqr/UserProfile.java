package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityUserProfileBinding;

public class UserProfile extends DrawerBase {

    ActivityUserProfileBinding activityUserProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        allocateActivityTitle("UserProfile");
    }
}