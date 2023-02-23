package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityScanHistoryBinding;

public class ScanHistory extends DrawerBase {

    ActivityScanHistoryBinding activityScanHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityScanHistoryBinding = ActivityScanHistoryBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityScanHistoryBinding.getRoot());
        allocateActivityTitle("ScanHistory");
    }
}