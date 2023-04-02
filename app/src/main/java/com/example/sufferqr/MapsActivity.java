package com.example.sufferqr;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
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
import android.widget.Button;
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
import android.location.Address;
import android.location.Geocoder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//extends FragmentActivity,Drawerbase implements OnMapReadyCallback

/**
 * map that users location is shown
 * marker that represents the nearby QR code
 * when the marker is clicked, the QR code name and points are shown
 */
public class MapsActivity extends DrawerBase implements OnMapReadyCallback, search_location.SearchDialogListener,GoogleMap.OnCameraIdleListener {
    //current camera position
    private LatLng currentCameraPosition;


    /**
     This method gets called when the user searches for a location by entering an address.
     It geocodes the address to get a LatLng object, moves the camera to the location,
     and updates the markers within one kilometer of the searched location.
     @param address The address entered by the user as a string.
     */
    @Override
    public void onSearch(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng searchedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 16));
                updateMarkersWithinOneKilometer(searchedLocation);
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     This method updates the markers within one kilometer of the searched location.
     It fetches the QR codes from the Firebase Firestore database and adds them
     to the cluster manager if they are within one kilometer of the searched location.
     @param searchedLocation The LatLng object representing the searched location.
     */
    private void updateMarkersWithinOneKilometer(LatLng searchedLocation) {
        clusterManager.clearItems();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("GameQrCode");

        collectionRef.whereEqualTo("allowViewScanRecord", true)
                .whereEqualTo("LocationExist",true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Double latitude = document.getDouble("LocationLatitude");
                        Double longitude = document.getDouble("LocationLongitude");
                        String name = document.getString("QRname");
                        String points = (String) document.getData().get("points").toString();

                        if (latitude != null && longitude != null) {
                            LatLng latLng = new LatLng(latitude, longitude);

                            if (isWithinOneKilometer(searchedLocation, latLng)) {
                                String id = name;
                                String point = points;
                                MyClusterItem clusterItem = new MyClusterItem(latLng.latitude, latLng.longitude, id, "Points: " + point);
                                clusterManager.addItem(clusterItem);
                            }
                        }
                    }

                    // Force the cluster manager to render the clusters
                    clusterManager.cluster();

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    /**
     This method is called when the camera position is changed.
     It updates the markers within one kilometer of the new camera position.
     */
    @Override
    public void onCameraIdle() {
        LatLng center = mMap.getCameraPosition().target;
        updateMarkersWithinOneKilometer(center);
    }



    /**
     A class that represents a cluster item, which holds information about the position,
     title, and snippet of a marker on the map.
     */
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
        // Initialize Places SDK
        String apiKey = "AIzaSyBl3Acwz4pkNuDzIkvEVW-wZIVKFHg19hs";
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

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


    /**
     This method initializes the cluster manager, sets up the map and its properties,
     and fetches the QR codes from the Firebase Firestore database.
     @param googleMap The GoogleMap object representing the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);



        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
        }

        setUpClusterManager();

        // Get the last known location using the FusedLocationProviderClient
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("GameQrCode");

                ArrayList<Double> latitudeList = new ArrayList<>();
                ArrayList<Double> longitudeList = new ArrayList<>();
                ArrayList<String> QRnames = new ArrayList<>();
                // arraylist of scores
                ArrayList<String> scores = new ArrayList<>();

                currentCameraPosition = currentLocation;


                collectionRef.whereEqualTo("allowViewScanRecord", true)
                        .whereEqualTo("LocationExist",true)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Double latitude = document.getDouble("LocationLatitude");
                                Double longitude = document.getDouble("LocationLongitude");
                                String name = document.getString("QRname");
                                String points = (String) document.getData().get("points").toString();

                                if (latitude != null && longitude != null) {
                                    latitudeList.add(latitude);
                                    longitudeList.add(longitude);
                                    QRnames.add(name);
                                    scores.add(points);
                                }
                            }

                            // add markers if they are within 1km of the current location
                            for (int i = 0; i < latitudeList.size(); i++) {
                                LatLng latLng = new LatLng(latitudeList.get(i), longitudeList.get(i));
                                if (isWithinOneKilometer(currentLocation, latLng)) {

                                    String id = QRnames.get(i);
                                    String points = scores.get(i);
                                    MyClusterItem clusterItem = new MyClusterItem(latLng.latitude, latLng.longitude, id, "Points: " + points);
                                    clusterManager.addItem(clusterItem);
                                    Log.d(TAG, "Added MyClusterItem: id=" + id );
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

        // search button clicked, ask for location
        binding.SeachLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the search_location DialogFragment
                search_location searchLocationDialog = new search_location();

                // Show the DialogFragment
                searchLocationDialog.show(getSupportFragmentManager(), "search_location");
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

    /**
     This method sets up the cluster manager, which handles clustering markers on the map.
     It also sets the listener for the camera idle event.
     */
    private void setUpClusterManager() {
        clusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);
    }



}


