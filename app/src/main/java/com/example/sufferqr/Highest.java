package com.example.sufferqr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;


public class Highest extends Fragment {
    FirebaseFirestore db;
    private ListView highScorePlayerArrayList;
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


        Query query = db.collection("GameQrCode").orderBy("points", Query.Direction.DESCENDING).limit(10);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable
            FirebaseFirestoreException error) {
                //Data.clear();
                int i = 0;
                for(QueryDocumentSnapshot doc: value) {
                    Log.d("Sample", String.valueOf(doc.getData().get("user")));
                    Log.d("Sample", String.valueOf(doc.getData().get("points")));
                    String name = (String) doc.getData().get("user");
                    String score = (String) doc.getData().get("points").toString();
                    int intScore = Integer.valueOf(score);
                    i += 1;
                    int rank = i;
                    if (i == 1) {
                        FirstUsername.setText(name);
                        FirstScore.setText(score);
                    }
                    else if (i == 2) {
                        SecondUsername.setText(name);
                        SecondScore.setText(score);
                    }
                    else if (i == 3) {
                        ThirdUsername.setText(name);
                        ThirdScore.setText(score);
                    }
                    else if (i > 3) {
                        Data.add(new HighScorePlayer(rank,name,intScore));
                        HighScorePlayerList adapter = new HighScorePlayerList(requireContext(), Data);
                        highScorePlayerArrayList = view.findViewById(R.id.ranks_listview);
                        highScorePlayerArrayList.setAdapter(adapter);
                    }


                }

            }
        });

        return view;

    }
}