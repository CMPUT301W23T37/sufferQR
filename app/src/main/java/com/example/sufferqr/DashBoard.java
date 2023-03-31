package com.example.sufferqr;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;


import com.example.sufferqr.databinding.ActivityDashBoardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is the dash board of the application
 * it will have a short cut to camera, the total number of the score collected
 * the total number of code been scanned, the highest score ever, and the score for
 * the last code scanned. Also, a small scale of map will display at the bottom with
 * indications of surrounding scannable code
 *
 */
public class DashBoard extends DrawerBase {

    ActivityDashBoardBinding activityDashBoardBinding;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();



    private void getUserRank(String userID, long userHighestScore,TextView highest_rank) {
        db.collection("Player")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int rank = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<Long> scoresList = (List<Long>) document.get("scores");
                                if (scoresList != null && !scoresList.isEmpty()) {
                                    long highestScore = Collections.max(scoresList);
                                    if (highestScore > userHighestScore) {
                                        rank++;
                                    }
                                }
                            }
                            highest_rank.setText(String.valueOf(rank));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashBoardBinding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(activityDashBoardBinding.getRoot());
        allocateActivityTitle("Suffer QR");





        // click qr code icon to scan code
        ImageView qrScan = findViewById(R.id.qr_image);
        qrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, ScanCode.class));
            }
        });
        // click the top left rectangle to see details
        TextView topRec = findViewById(R.id.blue_rec);
        topRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, UserProfile.class));
            }
        });

        // click rank to see leader board
        TextView rank = findViewById(R.id.blue_rec_t);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, LeaderBoard.class));
            }
        });

        // click highest score to see user profile
        TextView highestScore = findViewById(R.id.blue_rec_m);
        highestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this, UserProfile.class));
            }
        });

        // click last scan to see scan history
        TextView lastScan = findViewById(R.id.blue_rec_b);
        lastScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(getContentResolver(), Secure.ANDROID_ID);
                Intent HistIntent = new Intent(DashBoard.this, ScanHistory.class);
                HistIntent.putExtra("user",android_id);
                startActivity(HistIntent);
            }
        });

        TextView totalQR = findViewById(R.id.total_qr_number);
        TextView totalPoint = findViewById(R.id.total_point_number);
        TextView pointPercent = findViewById(R.id.point_percent);
        TextView hScore = findViewById(R.id.highest_score_number);
        TextView lScan = findViewById(R.id.last_scan_number);
        TextView highest_rank = findViewById(R.id.user_highest_rank);
        TextView total_rank = findViewById(R.id.your_rank_number);

        // Get AAID
        String android_id = Settings.Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        DocumentReference userAAID = db.collection("Player").document(android_id);
//        userAAID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @SuppressLint("StringFormatInvalid")
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isSuccessful()){
//                    DocumentSnapshot userInfo = task.getResult();
//                    if(!userInfo.exists()){
//                        Intent i = new Intent(DashBoard.this, RegisterPage.class);
//                        startActivity(i);
//                    }
//                    else {
//                        // get the score list
//                        List<Long> scoresList = (List<Long>) userInfo.get("scores");
//                        int length = 0;
//                        if (scoresList.size() != 0) {
//                            length = scoresList.size();
//                        }
//                        // check if there is any code record
//                        if (length != 0) {
//                            // set last scan
//                            lScan.setText(getString(R.string.last_scan_number, String.valueOf(scoresList.get(0))));
//
//                            // set percentage that point increased
//                            double pPercent;
//
//                            String sum = String.valueOf(userInfo.get("sumScore"));
//
//                            pPercent = (scoresList.get(0)/(Double.parseDouble(sum)-scoresList.get(0))) * 100;
//
//                            pointPercent.setText(getString(R.string.point_percent, pPercent));
//
//                            // sort the score list in reverse order
//                            List<Long> scoresSorted = scoresList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//
//                            // set total qr code scanned
//                            if (length == 1 && scoresSorted.get(0) == 0) {
//                                totalQR.setText(String.valueOf(0));
//                            } else {
//                                totalQR.setText(String.valueOf(length));
//                            }
//
//                            // set total points
//                            long sumScores = 0;
//                            for (int i = 0; i < length; i++) {
//                                sumScores += scoresList.get(i);
//                            }
//                            totalPoint.setText(String.valueOf(sumScores));
//
//                            // set highest score
//                            hScore.setText(String.valueOf(scoresSorted.get(0)));
//                        }
//                    }
//                } else {
//                    Log.d(TAG, "failed with ", task.getException());
//                }
//            }
//        });
        userAAID.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }
                DocumentSnapshot userInfo = value;
                if (value != null && value.exists()) {
                    if(!userInfo.exists()){
                        Intent i = new Intent(DashBoard.this, RegisterPage.class);
                        startActivity(i);
                    }
                    else {
                        // get the score list
                        List<Long> scoresList = (List<Long>) userInfo.get("scores");
                        int length = 0;
                        if (scoresList.size() != 0) {
                            length = scoresList.size();
                        }
                        // check if there is any code record
                        if (length != 0) {
                            // set last scan
                            lScan.setText(getString(R.string.last_scan_number, String.valueOf(scoresList.get(0))));

                            // set percentage that point increased
                            double pPercent;

                            String sum = String.valueOf(userInfo.get("sumScore"));

                            pPercent = (scoresList.get(0)/(Double.parseDouble(sum)-scoresList.get(0))) * 100;

                            if (scoresList.size() == 1){
                                pointPercent.setText("+100%");
                            } else{
                                pointPercent.setText(getString(R.string.point_percent, pPercent));
                            }


                            // sort the score list in reverse order
                            List<Long> scoresSorted = scoresList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

                            // set total qr code scanned
                            if (length == 1 && scoresSorted.get(0) == 0) {
                                totalQR.setText(String.valueOf(0));
                            } else {
                                totalQR.setText(String.valueOf(length));
                            }

                            // set total points
                            long sumScores = 0;
                            for (int i = 0; i < length; i++) {
                                sumScores += scoresList.get(i);
                            }
                            totalPoint.setText(String.valueOf(sumScores));

                            // set highest score
                            long userHighestScore = scoresSorted.get(0);
                            hScore.setText(String.valueOf(userHighestScore));

                            // set highest rank
                            getUserRank(android_id, userHighestScore,highest_rank);

                            // set total rank
                            Query query = db.collection("Player").orderBy("sumScore", Query.Direction.DESCENDING);
                            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable
                                FirebaseFirestoreException error) {
                                    //Data.clear();
                                    int i = 0;
                                    for(QueryDocumentSnapshot doc: value) {
                                        Log.d("Sample", String.valueOf(doc.getData().get("sumScore")));
                                        String tempScore = (String) doc.getData().get("sumScore").toString();
                                        int intScore = Integer.valueOf(tempScore);
                                        i += 1;
                                        int totalRank = i;
                                        if (tempScore.equals(sum)){
                                            total_rank.setText(String.valueOf(totalRank));
                                        }
                                    }
                                }
                            });

                        }
                    }
                } else {
                    Intent i = new Intent(DashBoard.this, RegisterPage.class);
                    startActivity(i);
                }

            }
        });



    }


}