package com.example.sufferqr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sufferqr.databinding.ActivityScanHistoryBinding;
import com.example.sufferqr.ui.main.ScanHistoryQRRecord;
import com.example.sufferqr.ui.main.ScanHistoryCustomList;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScanHistory extends DrawerBase {

    ActivityScanHistoryBinding activityScanHistoryBinding;

    ListView qrList;
    ArrayAdapter<ScanHistoryQRRecord> qrAdapter;
    ArrayList<ScanHistoryQRRecord> qrDataList;

    ScanHistoryCustomList customList;
    String UserName;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityScanHistoryBinding = ActivityScanHistoryBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityScanHistoryBinding.getRoot());
        allocateActivityTitle("Scan History");
        db = FirebaseFirestore.getInstance();

        qrList=findViewById(R.id.scan_history_recycleView);

        Intent myNewIntent = getIntent();
        UserName = myNewIntent.getStringExtra("user");

        qrDataList = new ArrayList<>();

        qrAdapter = new ScanHistoryCustomList(this,qrDataList);

        qrList.setAdapter(qrAdapter);

        final CollectionReference collectionReference = db.collection("GameQrCode");
        final Query query= collectionReference.whereEqualTo("user",UserName).orderBy("time");

        update();

        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ScanHistoryQRRecord hsq = (ScanHistoryQRRecord) adapterView.getItemAtPosition(position);
                //new/modified/viewer(mode) for QR detail activity
                Intent scanIntent = new Intent(getApplicationContext(),QRDetailActivity.class);
                scanIntent.putExtra("user","example");
                scanIntent.putExtra("qrID",hsq.getName());
                scanIntent.putExtra("mode","modified");
                startActivity(scanIntent);
                overridePendingTransition(0,0);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        update();

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        update();

    }

    private void update(){
        final CollectionReference collectionReference = db.collection("GameQrCode");
        final Query query= collectionReference.whereEqualTo("user",UserName).orderBy("time");
        
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    System.err.println("Listen failed: " + error);
                }
                if (value != null && !value.isEmpty()){
                    qrDataList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String qrName = String.valueOf(doc.getData().get("QRname"));
                        String points = String.valueOf(doc.getData().get("points"));
                        String sDate = String.valueOf(doc.getData().get("date"));
                        qrDataList.add(new ScanHistoryQRRecord(qrName, points,sDate)); // Adding the cities and provinces from FireStore
                    }
                    qrAdapter.notifyDataSetChanged();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"no result", Toast.LENGTH_SHORT);
                }

            }


        });
    }
}