package com.example.sufferqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.sufferqr.databinding.ActivityQrdetailBinding;
import com.example.sufferqr.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import java.util.List;
import java.util.Set;

public class QRDetailActivity extends AppCompatActivity implements PermissionsListener {

    private ActivityQrdetailBinding binding;

    Button CancelBt,ConfirmBt;

    Bundle mapBundle,imageBundle,GeneralBundle;

    View mapView;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        binding = ActivityQrdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.



        Intent myNewIntent = getIntent();

        Set<String> passingcat=  myNewIntent.getCategories();
        // response for insufficent event;

        String qr = "qr " + myNewIntent.getStringExtra("content");

        String UserName = myNewIntent.getStringExtra("UserName");
        String Mode = myNewIntent.getStringExtra("mode");

        mapBundle = new Bundle();
        imageBundle = new Bundle();
        GeneralBundle = new Bundle();
//        mapBundle.putString("mode",Mode);



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),mapBundle);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        CancelBt = findViewById(R.id.Activity_qet_detail_cancel_button);
        ConfirmBt = findViewById(R.id.activity_qr_detail_bottom_bar_confirm_bt);

        CancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //mapView = findViewById(R.id.content_map_view);

    }




    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }



//    // Activity lifecycle methods
//    @Override
//    public void onStart() {
//        super.onStart();
//        mapView.onStart();
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        mapView.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }
}