package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityDrawerBaseBinding;
import com.example.sufferqr.databinding.ActivityMainBinding;
import com.example.sufferqr.databinding.ActivityMapBinding;

public class Map extends DrawerBase {

    ActivityMapBinding activityMapBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityMapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityMapBinding.getRoot());
        allocateActivityTitle("Map");
    }
}