package com.example.sufferqr;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GameHighScorePlayers extends AppCompatActivity {
    FirebaseFirestore db;
    ListView highScorePlayerList;
    ArrayAdapter<HighScorePlayer> highScorePlayerAdapter;
    ArrayList<HighScorePlayer> highScorePlayerDataList;
    HighScorePlayer selectHighScorePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_players);

        db = FirebaseFirestore.getInstance();

        highScorePlayerList = findViewById(R.id.high_score_player_list);
        highScorePlayerDataList = new ArrayList<>();
        highScorePlayerAdapter = new HighScorePlayerList(this, highScorePlayerDataList);
        highScorePlayerList.setAdapter(highScorePlayerAdapter);

        Query query = db.collection("Player").orderBy("sumScore", Query.Direction.DESCENDING).limit(5);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable
            FirebaseFirestoreException error) {
                highScorePlayerDataList.clear();
                int i = 0;
                for(QueryDocumentSnapshot doc: value) {
                    Log.d("Sample", String.valueOf(doc.getData().get("name")));
                    Log.d("Sample", String.valueOf(doc.getData().get("sumScore")));

                    String name = (String) doc.getData().get("name");
                    String score = (String) doc.getData().get("sumScore").toString();
                    int intScore = Integer.valueOf(score);
                    i += 1;
                    int rank = i;

                    highScorePlayerDataList.add(new HighScorePlayer(rank, name, intScore));
                    highScorePlayerAdapter.notifyDataSetChanged();

                }

            }
        });
    }
}
