package com.example.sufferqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HighScorePlayerList extends ArrayAdapter<HighScorePlayer> {
    public HighScorePlayerList(Context context, List<HighScorePlayer> highScorePlayerArrayList){
        super(context,0,highScorePlayerArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.rank_detail,
                    parent, false);
        } else {
            view = convertView;
        }

        HighScorePlayer highScorePlayer = getItem(position);
        TextView rank = view.findViewById(R.id.rank);
        TextView userName = view.findViewById(R.id.total_fourth_username);
        TextView score = view.findViewById(R.id.total_fourth_score);

        int intRank = highScorePlayer.getRank();
        String strRank = Integer.toString(intRank);
        rank.setText(strRank);

        userName.setText(highScorePlayer.getUsername());

        int intScore = highScorePlayer.getScore();
        String strScore = Integer.toString(intScore);
        score.setText(strScore);

        return view;

    }
}

