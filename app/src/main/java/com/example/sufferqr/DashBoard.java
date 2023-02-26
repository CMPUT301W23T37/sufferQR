package com.example.sufferqr;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sufferqr.databinding.ActivityDashBoardBinding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(activityDashBoardBinding.getRoot());
        allocateActivityTitle("Suffer QR");
    }
}