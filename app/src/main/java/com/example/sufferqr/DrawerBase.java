package com.example.sufferqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * This class is used to set up the base of the drawer functionality
 * it will create the side bar drawer and navigate between activities.
 * All other class that needs a drawer will extends from this class
 * @version 1.0
 */

public class DrawerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    /**
     * This method is use to set up modified drawer content view to the activities that extends from this
     * calls the set content view method.
     * @param view
     * view will be the binding of that class
     */
    @Override
    public void setContentView(View view) {
//        Set the actual view on top of the frame layout
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

//        creating the tool bar
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * This method is used to detect when the item in the navigation view is clicked
     * and take action correspondingly
     * @param item The selected item/ the clicked item
     * @return false
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.nav_user:
                startActivity(new Intent(this, UserProfile.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_dash:
                startActivity(new Intent(this, DashBoard.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_search:
                startActivity(new Intent(this, SearchPlayer.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_map:
                startActivity(new Intent(this, Map.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_scan:
                //new/modified/viewer
                // remember tochange ScanCode.class

                //normal new code start up
//                Intent scanIntent = new Intent(DrawerBase.this, ScanCode.class);
//                scanIntent.putExtra("user","example");
//                scanIntent.putExtra("mode","new");
//                startActivity(scanIntent);

                // modifier
                Intent scanIntent2 = new Intent(DrawerBase.this, QRDetailActivity.class);
                scanIntent2.putExtra("user","example");
                scanIntent2.putExtra("mode","modified");
                scanIntent2.putExtra("qrID","gcc");
                startActivity(scanIntent2);


                // visiter
//                Intent scanIntent3 = new Intent(DrawerBase.this, QRDetailActivity.class);
//                scanIntent3.putExtra("user","example2");
//                scanIntent3.putExtra("mode","modified");
//                scanIntent3.putExtra("qrID","ppsc");
//                startActivity(scanIntent3);

                overridePendingTransition(0,0);
                break;

            case R.id.nav_LeaderBoard:
                startActivity(new Intent(this, LeaderBoard.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_History:
                startActivity(new Intent(this, ScanHistory.class));
                overridePendingTransition(0,0);
                break;

        }


        return false;
    }

    /**
     * This method is used to set up all the titles for the class that extends drawerbase class
     * @param title name of the activity
     */
    protected void allocateActivityTitle(String title){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }
}