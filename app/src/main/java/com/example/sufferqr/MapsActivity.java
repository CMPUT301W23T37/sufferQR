package com.example.sufferqr;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * map that users location is shown
 * marker that represents the nearby QR code
 * when the marker is clicked, the QR code name and points are shown
 */
public class MapsActivity extends DrawerBase implements OnMapReadyCallback {

    public static class MyClusterItem implements ClusterItem {
        private final LatLng position;
        private final String title;
        private final String snippet;

        public MyClusterItem(double lat, double lng, String title, String snippet) {
            this.position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        public String getTitle() {
            return title;
        }

        public String getSnippet() {
            return snippet;
        }
    }

    private ClusterManager<MyClusterItem> clusterManager;
    private FusedLocationProviderClient fusedLocationClient;

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


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
        }

        setUpClusterManager();

        // move camera to current location
//        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));


        // Get the last known location using the FusedLocationProviderClient
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("GameQrCode");

                ArrayList<Double> latitudeList = new ArrayList<>();
                ArrayList<Double> longitudeList = new ArrayList<>();
                ArrayList<String> ids = new ArrayList<>();
                // arraylist of scores
                ArrayList<String> scores = new ArrayList<>();




                collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Double latitude = document.getDouble("LocationLatitude");
                                Double longitude = document.getDouble("LocationLongitude");
                                String id = document.getId();
                                String points = (String) document.getData().get("points").toString();

                                if (latitude != null && longitude != null) {
                                    latitudeList.add(latitude);
                                    longitudeList.add(longitude);
                                    ids.add(id);
                                    scores.add(points);
                                }
                            }

                            // add markers if they are within 1km of the current location
                            for (int i = 0; i < latitudeList.size(); i++) {
                                LatLng latLng = new LatLng(latitudeList.get(i), longitudeList.get(i));
                                if (isWithinOneKilometer(currentLocation, latLng)) {
                                    String id = ids.get(i);
                                    String points = scores.get(i);
                                    MyClusterItem clusterItem = new MyClusterItem(latLng.latitude, latLng.longitude, id, "Points: " + points);
                                    clusterManager.addItem(clusterItem);
                                }
                            }

                            // Force the cluster manager to render the clusters
                            clusterManager.cluster();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            } else {
                finish();
            }
        });










        /**
         * when list of nearby QR clicked, change to list activity
         */
        // if list of nearby QR clicked, change to list activity
        binding.nearbylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, nearbyQrCodeList.class);
                startActivity(intent);
            }
        });





    }

    /**
     * check if the location is within 1km of the current location
     * @param location1 something
     * @param location2 something
     * @return true if within 1km, false if not
     */
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

    private void setUpClusterManager() {
        clusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
    }


}


