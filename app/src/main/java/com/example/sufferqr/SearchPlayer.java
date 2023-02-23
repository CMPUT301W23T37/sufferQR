package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivitySearchPlayerBinding;

public class SearchPlayer extends DrawerBase {

    ActivitySearchPlayerBinding activitySearchPlayerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activitySearchPlayerBinding = ActivitySearchPlayerBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activitySearchPlayerBinding.getRoot());
        allocateActivityTitle("SearchPlayer");
    }
}