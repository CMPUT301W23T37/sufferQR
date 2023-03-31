package com.example.sufferqr;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;


public class Highest extends Fragment {
    FirebaseFirestore db;
    private ListView highScorePlayerArrayList;
    private ImageView userIdQrImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_total, container, false);

        db = FirebaseFirestore.getInstance();

        TextView FirstUsername = view.findViewById(R.id.total_first_name);
        TextView FirstScore = view.findViewById(R.id.total_first_score);
        TextView SecondUsername = view.findViewById(R.id.total_second_name);
        TextView SecondScore = view.findViewById(R.id.total_second_score);
        TextView ThirdUsername = view.findViewById(R.id.total_third_name);
        TextView ThirdScore = view.findViewById(R.id.total_third_score);

        ArrayList<HighScorePlayer> Data = new ArrayList<>();


        Query query = db.collection("Player").orderBy("highestScore", Query.Direction.DESCENDING).limit(10);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable
            FirebaseFirestoreException error) {
                //Data.clear();
                int i = 0;
                for(QueryDocumentSnapshot doc: value) {
                    Log.d("Sample", String.valueOf(doc.getData().get("name")));
                    Log.d("Sample", String.valueOf(doc.getData().get("highestScore")));
                    Log.d("Sample", String.valueOf(doc.getData().get("qrid")));
                    String name = (String) doc.getData().get("name");
                    String score = (String) doc.getData().get("highestScore").toString();
                    int intScore = Integer.valueOf(score);
                    i += 1;
                    int rank = i;
                    String userQRid = (String) doc.getData().get("qrid");
                    if (i == 1) {
                        FirstUsername.setText(name);
                        FirstScore.setText(score);
                        userIdQrImage = view.findViewById(R.id.total_first_qr);
                    }
                    else if (i == 2) {
                        SecondUsername.setText(name);
                        SecondScore.setText(score);
                        userIdQrImage = view.findViewById(R.id.total_second_qr);
                    }
                    else if (i == 3) {
                        ThirdUsername.setText(name);
                        ThirdScore.setText(score);
                        userIdQrImage = view.findViewById(R.id.total_thrid_qr);
                    }
                    else if (i > 3) {
                        Data.add(new HighScorePlayer(rank,name,intScore,userQRid));
                        HighScorePlayerList adapter = new HighScorePlayerList(requireContext(), Data);
                        highScorePlayerArrayList = view.findViewById(R.id.ranks_listview);
                        highScorePlayerArrayList.setAdapter(adapter);
                    }
                    String qrCode = userQRid + name;
                    MultiFormatWriter mWriter = new MultiFormatWriter();
                    try {
                        //BitMatrix class to encode entered text and set Width & Height
                        BitMatrix mMatrix = mWriter.encode(qrCode, BarcodeFormat.QR_CODE, 300,300);
                        BarcodeEncoder mEncoder = new BarcodeEncoder();
                        Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                        userIdQrImage.setImageBitmap(mBitmap);//Setting generated QR code to imageView
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }
}