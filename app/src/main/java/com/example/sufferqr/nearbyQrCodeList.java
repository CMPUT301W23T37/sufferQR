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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        latitude = location.getLatitude();  // get current lat
        longitude = location.getLongitude();

        db = FirebaseFirestore.getInstance();

        Double temp = new Double(0.009); // 1/111 = 0.009
        double temp1 = temp.doubleValue();
        double lat = latitude.doubleValue();
        double maxLat = lat + temp1;
        double minLat = lat - temp1;

        double lon = longitude.doubleValue();
        Double temp2 = new Double(1/(111*cos(lon)));
        double temp3 = temp2.doubleValue();
        double maxLon = lon + abs(temp3);
        double minLon = lon - abs(temp3);



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

                    double tempLon = (double) doc.getData().get("LocationLongitude");
                    if ( tempLon <= maxLon ) {
                        if (tempLon >= minLon) {
                            qrCodeDataList.add(new QrCode(LocationAddress, LocationExist, LocationLatitude,
                                    LocationLongitude, LocationName, QrName, QrText, date, imageExist, points, user)); // Adding
                            qrCodeAdapter.notifyDataSetChanged();
                        }
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
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, nearbyQrCodeList.this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();  // get current lat
        longitude = location.getLongitude();  // get current lon

    }

}

