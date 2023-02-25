package com.example.sufferqr;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.sufferqr.databinding.ActivityQrdetailBinding;
import com.example.sufferqr.ui.main.QRDetailGeneralFragment;
import com.example.sufferqr.ui.main.QRDetailImageFragment;
import com.example.sufferqr.ui.main.QRDetailLocationFragment;
import com.example.sufferqr.ui.main.SectionsPagerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class QRDetailActivity extends AppCompatActivity implements QRDetailLocationFragment.OnFragmentInteractionListener,
        QRDetailImageFragment.OnFragmentInteractionListener, QRDetailGeneralFragment.OnFragmentInteractionListener {

    private ActivityQrdetailBinding binding;

    // map and location settings
    private static final int REQUEST_CODE = 5678;
    private static final  int REQUEST_CHECK_SETTING = 10;
    private static  final long UPDATE_INTERVAL =10;
    private static final long FAST_UPDATE_IN_ML = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    MapboxMap mapboxMapGlobal;
    MapView mapView;
    MapboxMap map;

    Geocoder geocoder;

    TextView s1,s2,s3;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private com.google.android.gms.location.LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location locationCurrent;
    private boolean reqLocationUpdate = false;
    private HashMap <String,Object> data;
    String mode= "new",user="example",name;


    Button CancelBt,ConfirmBt;
    Bundle mapBundle,imageBundle,GeneralBundle;


    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        binding = ActivityQrdetailBinding.inflate(getLayoutInflater());
        Mapbox.getInstance(getApplicationContext(), getResources().getString(R.string.mapbox_access_token));
        setContentView(binding.getRoot());





        Intent myNewIntent = getIntent();

        Set<String> passingcat=  myNewIntent.getCategories();
        // response for insufficent event;

        String qr = "qr " + myNewIntent.getStringExtra("content");

        String UserName = myNewIntent.getStringExtra("UserName");
        String Mode = myNewIntent.getStringExtra("mode");

        mapBundle = new Bundle();
        mapBundle.putString("mode",Mode);
        imageBundle = new Bundle();
        GeneralBundle = new Bundle();

        data = new HashMap<>();


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),mapBundle,imageBundle,GeneralBundle);
        int limit = (sectionsPagerAdapter.getCount() > 1 ? sectionsPagerAdapter.getCount() -1 : 1);
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.beginFakeDrag(); // disable drag



        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
//        FloatingActionButton fab = binding.fab;

        CancelBt = findViewById(R.id.Activity_qet_detail_cancel_button);
        ConfirmBt = findViewById(R.id.activity_qr_detail_bottom_bar_confirm_bt);

        CancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ConfirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMapValidate("user",user);
                GameQrRecordDB DBconnect = new GameQrRecordDB();
                if (name.length()==0){
                    DBconnect.NewQRWithRandomGeneratedWords("",data);
                } else {
                    DBconnect.CheckUnique(name,true,data);
                    finish();
                }

            }
        });



    }

    private void HashMapValidate(String id,Object ob){
        if (data.containsKey(id)) {
            data.replace(id,ob);
        } else {
            data.put(id,ob);
        }
    }


    @Override
    public void onImageUpdate(String QRtext,Boolean imageOn) {
        // future representation
        int qr_le= QRtext.length();
        if (qr_le > 0 && mode.equals("new")) {
            TextInputEditText visual = findViewById(R.id.qr_detail_general_visual_text);
            visual.setText(QRtext);
            // future point updates length for now
            TextInputEditText points = findViewById(R.id.qr_detail_general_qrtext_points);
            String qr_le_str = String.valueOf(qr_le);
            points.setText(qr_le_str);
        }


        if(mode.equals("new")){
            HashMapValidate("points",qr_le);
            HashMapValidate("imageExist",imageOn);

            if (imageOn){
                HashMapValidate("QRtext",QRtext);

            } else {
                // future visual represent
                HashMapValidate("QRtext","");
            }
        }else{

        }
    }

    @Override
    public void onLocationUpdate(Boolean btOn, Double longitude, Double latitude, String name, String address) {
        if(mode.equals("new")){
            HashMapValidate("LocationExist",btOn);

            if (btOn){
                HashMapValidate("LocationLatitude",latitude);
                HashMapValidate("LocationLongitude",longitude);
                HashMapValidate("LocationName",name);
                HashMapValidate("LocationAddress",address);
            } else {
                // future visual represent
                HashMapValidate("LocationLatitude",0.0);
                HashMapValidate("LocationLongitude",0.0);
                HashMapValidate("LocationName","");
                HashMapValidate("LocationAddress","");
            }
        }else{

        }

    }

    @Override
    public void onGeneralUpdate(String QRcodename,String today) {
        HashMapValidate("QRname",QRcodename);
        HashMapValidate("date",today);
        name=QRcodename;
    }
}