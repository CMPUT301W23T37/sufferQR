package com.example.sufferqr;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class nearbyQrCodeList extends AppCompatActivity implements LocationListener {
    private Double latitude,longitude;
    private LocationManager locationManager;
    FirebaseFirestore db;
    ListView qrCodeList;
    ArrayAdapter<QrCode> qrCodeAdapter;
    ArrayList<QrCode> qrCodeDataList;
    QrCode selectQrCode;

    private Button buttonBack;

    /*
    get the location of current user
    get those useful QRcode from firebase
    @see list which tells us those nearby QRcode.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_qrcode_list);

        qrCodeList = findViewById(R.id.qrCode_list);
        qrCodeDataList = new ArrayList<>();
        qrCodeAdapter = new QrCodeList(this, qrCodeDataList);
        qrCodeList.setAdapter(qrCodeAdapter);

        if (ActivityCompat.checkSelfPermission(nearbyQrCodeList.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(nearbyQrCodeList.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        getLocation();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        db = FirebaseFirestore.getInstance();

        Double temp = new Double(0.009); // 1/111 = 0.009
        double temp1 = temp.doubleValue();
        double lat = latitude.doubleValue();
        double maxLat = lat + temp1;
        double minLat = lat - temp1;


        Query query = db.collection("GameQrCode")
                .whereEqualTo("LocationExist",true)
                .whereLessThanOrEqualTo("LocationLatitude",maxLat)
                .whereGreaterThanOrEqualTo("LocationLatitude",minLat);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable
            FirebaseFirestoreException error) {
                qrCodeDataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    Log.d("Sample", String.valueOf(doc.getData().get("LocationAddress")));
                    Log.d("Sample", String.valueOf(doc.getData().get("LocationExist")));
                    Log.d("Sample", String.valueOf(doc.getData().get("LocationLatitude")));
                    Log.d("Sample", String.valueOf(doc.getData().get("LocationLongitude")));
                    Log.d("Sample", String.valueOf(doc.getData().get("LocationName")));
                    Log.d("Sample", String.valueOf(doc.getData().get("QRname")));
                    Log.d("Sample", String.valueOf(doc.getData().get("QRtext")));
                    Log.d("Sample", String.valueOf(doc.getData().get("date")));
                    Log.d("Sample", String.valueOf(doc.getData().get("imageExist")));
                    Log.d("Sample", String.valueOf(doc.getData().get("points")));
                    Log.d("Sample", String.valueOf(doc.getData().get("user")));

                    String LocationAddress = (String) doc.getData().get("LocationAddress");
                    boolean LocationExist = (boolean) doc.getData().get("LocationExist");
                    String LocationLatitude = (String) doc.getData().get("LocationLatitude").toString();
                    String LocationLongitude = (String) doc.getData().get("LocationLongitude").toString();
                    String LocationName = (String) doc.getData().get("LocationName");
                    String QrName = (String) doc.getData().get("QRname");
                    String QrText = (String) doc.getData().get("QRtext");
                    String date = (String) doc.getData().get("date");
                    boolean imageExist = (boolean) doc.getData().get("imageExist");
                    String points = (String) doc.getData().get("points").toString();
                    String user = (String) doc.getData().get("user");

                    double tempLat = (double) doc.getData().get("LocationLatitude");
                    double tempLon = (double) doc.getData().get("LocationLongitude");
                    boolean whetherInOneKmDis = isWithinOneKilometer(latitude, longitude, tempLat, tempLon);
                    if ( whetherInOneKmDis == true ) {

                        qrCodeDataList.add(new QrCode(LocationAddress, LocationExist, LocationLatitude,
                                LocationLongitude, LocationName, QrName, QrText, date, imageExist, points, user)); // Adding
                        qrCodeAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

        qrCodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectQrCode = qrCodeAdapter.getItem(position);
                QrCodeDetailFragment myFragment = new QrCodeDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key1",selectQrCode.getLocationAddress());
                bundle.putString("key2",selectQrCode.getLocationLatitude());
                bundle.putString("key3",selectQrCode.getLocationLongitude());
                bundle.putString("key4",selectQrCode.getDate());
                bundle.putString("key5",selectQrCode.getPoints());
                myFragment.setArguments(bundle);
                myFragment.show(getSupportFragmentManager(),"Detail of QR");
            }
        });

        buttonBack = findViewById(R.id.back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nearbyQrCodeList.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
    get location
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, nearbyQrCodeList.this);

    }
    /*
    get the latitude and longitude of current user.
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();  // get current lat
        longitude = location.getLongitude();  // get current lon

    }
    /*
    check whether the given QRcode is in the required area (the distance between
    the current user and the given QRcode is less than 1km).
    @param userLatitude: a double which represent the latitude of current user
    @param userLongitude: a double which represent the longitude of current user
    @param qrLatitude: a double which represent the latitude of nearby QRcode.
    @param qrLongitude: a double which represent the longitude of nearby QRcode.
    @return return the boolean that represented whether in the required area.
     */
    public boolean isWithinOneKilometer(double userLatitude, double userLongitude, double qrLatitude, double qrLongitude) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(userLatitude - qrLatitude);
        double lonDistance = Math.toRadians(userLongitude - qrLongitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(qrLatitude))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance <= 1000; // return true if distance is less than or equal to 1km
    }



}

