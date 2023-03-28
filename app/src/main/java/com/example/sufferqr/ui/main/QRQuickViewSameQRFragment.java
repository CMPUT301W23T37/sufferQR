package com.example.sufferqr.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sufferqr.QRQuickViewScrollingActivity;
import com.example.sufferqr.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class QRQuickViewSameQRFragment extends Fragment {
    Bundle bundle;
    ListView qrList;

    ArrayAdapter<ScanHistoryQRRecord> qrAdapter;

    ArrayList<ScanHistoryQRRecord> qrDataList;

    String UserName;

    FirebaseFirestore db;

    public QRQuickViewSameQRFragment(Bundle myBundle) {
        bundle = myBundle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_quick_view_same_q_r, container, false);

        db = FirebaseFirestore.getInstance();
        qrList = view.findViewById(R.id.scan_same_recycleView);
        // Check if the fragment arguments are not null before calling getString("user")
        Bundle args = getArguments();
        if (args != null) {
            UserName = args.getString("user");
        }

        qrDataList = new ArrayList<>();
        qrAdapter = new ScanHistoryCustomList(getActivity(), qrDataList);
        qrList.setAdapter(qrAdapter);

        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ScanHistoryQRRecord hsq = (ScanHistoryQRRecord) adapterView.getItemAtPosition(position);
                Intent scanIntent = new Intent(getActivity(), QRQuickViewScrollingActivity.class);
                scanIntent.putExtra("user", UserName);
                scanIntent.putExtra("qrID", hsq.getName());

                Bundle bundle = new Bundle();
                for (Map.Entry<String, Object> entry : hsq.getMap().entrySet()) {
                    bundle.putString(entry.getKey(), String.valueOf(entry.getValue()));
                }

                scanIntent.putExtra("MapData", bundle);
                startActivity(scanIntent);

                getActivity().overridePendingTransition(0, 0);
            }
        });

        final CollectionReference collectionReference = db.collection("GameQrCode");
        collectionReference.whereEqualTo("QRhash", bundle.getString("qrHash"))
                .orderBy("points", Query.Direction.DESCENDING)
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            qrDataList.clear();
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                String qrName = String.valueOf(doc.getData().get("QRname"));
                                String points = String.valueOf(doc.getData().get("points"));
                                String sDate = String.valueOf(doc.getData().get("date"));
                                String sAddress = String.valueOf(doc.getData().get("LocationName"));
                                qrDataList.add(new ScanHistoryQRRecord(qrName, points, sDate, sAddress, doc.getData()));
                            }
                            qrAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "no Other players have scanned this QRcode yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }
}