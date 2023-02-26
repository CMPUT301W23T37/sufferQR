package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityLeaderBoardBinding;

public class LeaderBoard extends DrawerBase {
    ActivityLeaderBoardBinding activityLeaderBoardBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityLeaderBoardBinding = ActivityLeaderBoardBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityLeaderBoardBinding.getRoot());
        allocateActivityTitle("Leader Board");
    }
}