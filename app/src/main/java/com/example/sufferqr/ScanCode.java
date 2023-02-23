package com.example.sufferqr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityScanCodeBinding;

public class ScanCode extends DrawerBase {

    ActivityScanCodeBinding activityScanCodeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityScanCodeBinding = ActivityScanCodeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityScanCodeBinding.getRoot());
        allocateActivityTitle("Suffer QR");
    }
}