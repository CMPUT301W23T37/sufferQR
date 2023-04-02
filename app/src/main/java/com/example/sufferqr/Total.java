package com.example.sufferqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * This class is the first part of the leader board of the application
 * It will display a list of game-wide high total score.
 * The top 10 users with the highest total score are displayed in descending order based on the scores.
 * user can see those users' username, scores and QR code (generate by qrid)
 */

public class Total extends Fragment {
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


        Query query = db.collection("Player").whereEqualTo("allowViewScanRecord",true).orderBy("sumScore", Query.Direction.DESCENDING).limit(10);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable
            FirebaseFirestoreException error) {
                //Data.clear();
                int i = 0;
                for(QueryDocumentSnapshot doc: value) {
                    Log.d("Sample", String.valueOf(doc.getData().get("name")));
                    Log.d("Sample", String.valueOf(doc.getData().get("sumScore")));
                    Log.d("Sample", String.valueOf(doc.getData().get("qrid")));
                    String name = (String) doc.getData().get("name");

                    String score = (String) doc.getData().get("sumScore").toString();
                    int intScore = Integer.valueOf(score);

                    String userQRid = (String) doc.getData().get("qrid");
                    i += 1;
                    int rank = i;

                    if (i == 1) {
                        FirstUsername.setText(name);
                        FirstScore.setText(score);
                        userIdQrImage = view.findViewById(R.id.total_first_qr);
                        userIdQrImage.setClickable(true);
                        userIdQrImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = (String) FirstUsername.getText();
                                Intent intent = new Intent(requireActivity(),OtherUser.class);
                                intent.putExtra("username",name);
                                startActivity(intent);
                            }
                        });
                    }
                    else if (i == 2) {
                        SecondUsername.setText(name);
                        SecondScore.setText(score);
                        userIdQrImage = view.findViewById(R.id.total_second_qr);
                        userIdQrImage.setClickable(true);
                        userIdQrImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = (String) SecondUsername.getText();
                                Intent intent = new Intent(requireActivity(),OtherUser.class);
                                intent.putExtra("username",name);
                                startActivity(intent);
                            }
                        });
                    }
                    else if (i == 3) {
                        ThirdUsername.setText(name);
                        ThirdScore.setText(score);
                        userIdQrImage = view.findViewById(R.id.total_thrid_qr);
                        userIdQrImage.setClickable(true);
                        userIdQrImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = (String) ThirdUsername.getText();
                                Intent intent = new Intent(requireActivity(),OtherUser.class);
                                intent.putExtra("username",name);
                                startActivity(intent);
                            }
                        });
                    } else {
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

        highScorePlayerArrayList = view.findViewById(R.id.ranks_listview);
        highScorePlayerArrayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HighScorePlayer highScorePlayer = (HighScorePlayer) parent.getItemAtPosition(position);
                String name = highScorePlayer.getUsername();
                Intent intent = new Intent(requireActivity(),OtherUser.class);
                intent.putExtra("username",name);
                startActivity(intent);
            }
        });

        return view;

    }
}