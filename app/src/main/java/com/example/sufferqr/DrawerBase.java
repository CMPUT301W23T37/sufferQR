package com.example.sufferqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBase extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

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
                startActivity(new Intent(this, ScanCode.class));
                overridePendingTransition(0,0);
                break;

            case R.id.nav_LeaderBoard:
                startActivity(new Intent(this, LeaderBoard.class));
                overridePendingTransition(0,0);
                break;

        }


        return false;
    }

    protected void allocateActivityTitle(String titles){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titles);
        }
    }
}