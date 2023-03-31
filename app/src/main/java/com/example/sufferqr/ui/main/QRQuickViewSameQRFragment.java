package com.example.sufferqr.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sufferqr.OtherUser;
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
import java.util.Objects;

/**
 * a list where show the people who also scaned the qrcode
 */
public class QRQuickViewSameQRFragment extends Fragment {
    Bundle bundle;
    ListView qrList;

    TextView scanCountTextView;
    ArrayAdapter<sameQrListContext> qrAdapter;

    ArrayList<sameQrListContext> qrDataList;
    ArrayList<String> name;

    String UserName;

    FirebaseFirestore db;

    /**
     * datta passing and launch the class
     * @param myBundle data transfer
     */
    public QRQuickViewSameQRFragment(Bundle myBundle) {
        bundle = myBundle;
        UserName = bundle.getString("localUser");
    }

    /**
     * draw the view and resbonses
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_quick_view_same_q_r, container, false);

        db = FirebaseFirestore.getInstance();
        qrList = view.findViewById(R.id.scan_same_recycleView);
        // Initialize scanCountTextView
        scanCountTextView = view.findViewById(R.id.scan_count_textview);
        // Check if the fragment arguments are not null before calling getString("user")

        qrDataList = new ArrayList<>();
        name = new ArrayList<>();
        qrAdapter = new SameQrListAdapter(getActivity(), qrDataList);
        qrList.setAdapter(qrAdapter);

        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                sameQrListContext hsq = (sameQrListContext) adapterView.getItemAtPosition(position);
                Intent scanIntent = new Intent(getActivity(), OtherUser.class);
                scanIntent.putExtra("username", hsq.getName());

                startActivity(scanIntent);

                getActivity().overridePendingTransition(0, 0);
            }
        });

        final CollectionReference collectionReference = db.collection("GameQrCode");
        collectionReference.whereEqualTo("QRhash", bundle.getString("QRhash")).whereEqualTo("allowViewScanRecord",true)
                .orderBy("points", Query.Direction.DESCENDING)
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }
                        if (value != null && !value.isEmpty()) {
                            // Get the number of scans

                            qrDataList.clear();
                            name.clear();
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                String qrName = String.valueOf(Objects.requireNonNull(doc.getData()).get("userName"));
                                if (!name.contains(qrName)){
                                    name.add(qrName);
                                    qrDataList.add(new sameQrListContext(qrName));
                                }
                            }
                            int scanCount = name.size();
                            scanCountTextView.setText("The Number of user this QRcode has been Scanned: " + scanCount);
                            qrAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "no Other players have scanned this QRcode yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }
}