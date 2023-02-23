package com.example.sufferqr;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityDashBoardBinding;

public class DashBoard extends DrawerBase {

    ActivityDashBoardBinding activityDashBoardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(activityDashBoardBinding.getRoot());
        allocateActivityTitle("DashBoard");
    }
}