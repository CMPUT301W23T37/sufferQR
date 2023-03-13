package com.example.sufferqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;
import android.view.View;
import android.widget.Button;


import java.io.IOException;
import java.util.HashMap;

/**
 * currently not in use
 */
public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.nav_drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompat.START);
//        } else{
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.navigation_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
////        onNavigationItemSelected(item);
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_user){
//            Toast.makeText(getApplicationContext(), "Camera is clicked", Toast.LENGTH_SHORT).show();
//        }
//
//        DrawerLayout drawer = findViewById(R.id.nav_drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

}
