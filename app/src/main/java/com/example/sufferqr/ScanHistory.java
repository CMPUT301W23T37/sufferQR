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
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * reconize qr code and take photo of sourrounds
 */
public class ScanHistory extends DrawerBase {

    ActivityScanHistoryBinding activityScanHistoryBinding;

    ListView qrList;
    ArrayAdapter<ScanHistoryQRRecord> qrAdapter;
    ArrayList<ScanHistoryQRRecord> qrDataList;

    ScanHistoryCustomList customList;
    String UserName;
    FirebaseFirestore db;

    /**
     * create view
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

        update();

        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ScanHistoryQRRecord hsq = (ScanHistoryQRRecord) adapterView.getItemAtPosition(position);
                //new/modified/viewer(mode) for QR detail activity
                Intent scanIntent = new Intent(getApplicationContext(),QRDetailActivity.class);
                scanIntent.putExtra("user",UserName);
                scanIntent.putExtra("qrID",hsq.getName());
                scanIntent.putExtra("mode","modified");
                startActivity(scanIntent);
                overridePendingTransition(0,0);
            }
        });
        final CollectionReference collectionReference = db.collection("GameQrCode");
        collectionReference.whereEqualTo("user",UserName).orderBy("time",Query.Direction.DESCENDING)
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
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
                                String sAddress = String.valueOf(doc.getData().get("LocationName"));
                                qrDataList.add(new ScanHistoryQRRecord(qrName, points,sDate,sAddress)); // Adding the cities and provinces from FireStore
                            }
                            qrAdapter.notifyDataSetChanged();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),"no result", Toast.LENGTH_SHORT);
                        }
                    }
                });


    }

    /**
     * update when comeback
     */
    @Override
    protected void onResume() {
        super.onResume();
        update();

    }

    /**
     * update when comeback
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        update();

    }

    /**
     * update info from fire base
     */
    private void update(){
        final CollectionReference collectionReference = db.collection("GameQrCode");
        final Query query= collectionReference.whereEqualTo("user",UserName).orderBy("time",Query.Direction.DESCENDING);
        
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
                        String sAddress = String.valueOf(doc.getData().get("LocationName"));
                        qrDataList.add(new ScanHistoryQRRecord(qrName, points,sDate,sAddress)); // Adding the cities and provinces from FireStore
                    }
                    qrAdapter.notifyDataSetChanged();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),"no result", Toast.LENGTH_SHORT);
                }

            }


        });
    }
}