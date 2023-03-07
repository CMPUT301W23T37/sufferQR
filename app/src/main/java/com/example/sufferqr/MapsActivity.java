package com.example.sufferqr;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.sufferqr.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//extends FragmentActivity,Drawerbase implements OnMapReadyCallback
public class MapsActivity extends DrawerBase implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    boolean isPermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        allocateActivityTitle("Map");
        //get permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            isPermissionGranted = true;
        }

        //map open if permission granted, else close
        if (isPermissionGranted) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            finish();
            //shows a toast message
            Toast.makeText(this, "Please turn on the location permission", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
        }

        // move camera to current location
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("GameQrCode");

        ArrayList<Double> latitudeList = new ArrayList<>();
        ArrayList<Double> longitudeList = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        // arraylist of scores
        ArrayList<Long> scores = new ArrayList<>();




        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Double latitude = document.getDouble("LocationLatitude");
                        Double longitude = document.getDouble("LocationLongitude");
                        String id = document.getId();
                        Long score = document.getLong("points");

                        if (latitude != null && longitude != null) {
                            latitudeList.add(latitude);
                            longitudeList.add(longitude);
                            ids.add(id);
                            scores.add(score);

                        }
                    }
            
                    // add markers if they are within 1km of the current location
                    for (int i = 0; i < latitudeList.size(); i++) {
                        LatLng latLng = new LatLng(latitudeList.get(i), longitudeList.get(i));
                        if (isWithinOneKilometer(currentLocation, latLng)) {
                            //if score is not null, add marker
                            if (scores.get(i) != null) {
                                mMap.addMarker(new MarkerOptions().position(latLng).title(ids.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet("Points: " + scores.get(i).toString() + ""));
                            }else {
                            //if score is null, add marker
                            mMap.addMarker(new MarkerOptions().position(latLng).title(ids.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).snippet("Points: ???"));
                        }}
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        // Set up the info window click listener
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Handle info window click event
                if (marker.getTitle() != null && !marker.getTitle().isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Marker Title Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public boolean isWithinOneKilometer(LatLng location1, LatLng location2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(location2.latitude - location1.latitude);
        double lonDistance = Math.toRadians(location2.longitude - location1.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(location1.latitude)) * Math.cos(Math.toRadians(location2.latitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance <= 1000; // return true if distance is less than or equal to 1km
    }

}


