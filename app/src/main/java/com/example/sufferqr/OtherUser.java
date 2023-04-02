package com.example.sufferqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sufferqr.DrawerBase;
import com.example.sufferqr.QRQuickViewScrollingActivity;
import com.example.sufferqr.R;
import com.example.sufferqr.User;
import com.example.sufferqr.databinding.ActivityOtherUserLayoutBinding;
import com.example.sufferqr.ui.main.ScanHistoryCustomList;
import com.example.sufferqr.ui.main.ScanHistoryQRRecord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

/**
 * Other user profile contain all the info that this user allow other to view
 */
public class OtherUser extends DrawerBase {

    ActivityOtherUserLayoutBinding activityOtherUserLayoutBinding;

    ImageView userCode_otherProfile;
    TextView userName_otherProfile;
    TextView userEmail_otherProfile;
    TextView userQrId_otherProfile;
    Chip highest_otherProfile;
    Chip lowest_otherProfile;
    Chip sum_otherProfile;
    Chip code_otherProfile;

    ListView qrCode_otherProfile;
    ArrayAdapter<ScanHistoryQRRecord> qrAdapter;
    ArrayList<ScanHistoryQRRecord> qrDataList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtherUserLayoutBinding = ActivityOtherUserLayoutBinding.inflate(getLayoutInflater());
        setContentView(activityOtherUserLayoutBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        userName_otherProfile = findViewById(R.id.userName_otherProfile);
        userEmail_otherProfile = findViewById(R.id.userEmail_otherProfile);
        userQrId_otherProfile = findViewById(R.id.userQRid_otherProfile);
        highest_otherProfile = findViewById(R.id.highest_otherProfile);
        lowest_otherProfile = findViewById(R.id.lowest_otherProfile);
        sum_otherProfile = findViewById(R.id.sum_otherProfile);
        code_otherProfile = findViewById(R.id.number_of_code_otherProfile);
        userCode_otherProfile = findViewById(R.id.userQRImage_otherProfile);
        qrCode_otherProfile=findViewById(R.id.other_user_profile_list);

        Bundle value = getIntent().getExtras();
        String name = value.getString("username");

        allocateActivityTitle(name + "'s Profile");

//        userName_otherProfile.setText(name);

        final CollectionReference collectionReference = db.collection("Player");
        collectionReference.whereEqualTo("name", name).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    System.err.println("Listen failed: " + error);
                }

                DocumentSnapshot doc = value.getDocuments().get(0);
                User u = doc.toObject(User.class);
                assert u != null;

                userName_otherProfile.setText(u.getName());

                if(u.getAllowViewEmail()){
                    userEmail_otherProfile.setText(u.getEmail());
                } else {
                    userEmail_otherProfile.setText("");
                }

                if(u.getAllowViewQrid()){
                    userQrId_otherProfile.setText(u.getQRid());
                } else{
                    userQrId_otherProfile.setText("");
                }

                if(u.getAllowViewScanRecord()){
                    String highest_text = "Highest: " + doc.get("highestScore");
                    highest_otherProfile.setText(highest_text);
                    String lowest_text = "Lowest: " + doc.get("lowestScore");
                    lowest_otherProfile.setText(lowest_text);
                    String sum_text = "Sum: " + doc.get("sumScore");
                    sum_otherProfile.setText(sum_text);
                    String code_text = "Code: " + doc.get("qrcount");
                    code_otherProfile.setText(code_text);
                } else{
                    highest_otherProfile.setText("N/A");
                    lowest_otherProfile.setText("N/A");
                    sum_otherProfile.setText("N/A");
                    code_otherProfile.setText("N/A");
                    qrCode_otherProfile.setVisibility(View.INVISIBLE);
                }


            }
        });

        // generate qr code

        String qrCode = userQrId_otherProfile.getText().toString().trim() + name;
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(qrCode, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            userCode_otherProfile.setImageBitmap(mBitmap);//Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // list
        db = FirebaseFirestore.getInstance();

        qrDataList = new ArrayList<>();

        qrAdapter = new ScanHistoryCustomList(this,qrDataList);

        qrCode_otherProfile.setAdapter(qrAdapter);

        qrCode_otherProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ScanHistoryQRRecord hsq = (ScanHistoryQRRecord) adapterView.getItemAtPosition(position);

                Intent scanIntent = new Intent(getApplicationContext(), QRQuickViewScrollingActivity.class);
                scanIntent.putExtra("localUser",name);
                scanIntent.putExtra("qrID",hsq.getName());

                Bundle bundle = new Bundle();
                for (Map.Entry<String, Object> entry : hsq.getMap().entrySet()) {
                    bundle.putString(entry.getKey(),String.valueOf(entry.getValue()));
                }

                scanIntent.putExtra("MapData",bundle);
                startActivity(scanIntent);

                overridePendingTransition(0,0);
            }
        });
        final CollectionReference qr_collectionReference = db.collection("GameQrCode");
        qr_collectionReference.whereEqualTo("userName",name).orderBy("time", Query.Direction.DESCENDING)
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
                                qrDataList.add(new ScanHistoryQRRecord(qrName, points,sDate,sAddress,doc.getData())); // Adding the cities and provinces from FireStore
                            }
                            System.out.println("sioze"+qrDataList.size());
                            qrAdapter.notifyDataSetChanged();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),"no result", Toast.LENGTH_SHORT);
                        }
                    }
                });

    }
}