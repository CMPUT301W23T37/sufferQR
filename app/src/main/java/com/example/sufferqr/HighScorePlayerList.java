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

public class HighScorePlayerList extends ArrayAdapter<HighScorePlayer> {

    private ArrayList<HighScorePlayer> highScorePlayers;
    private Context context;

    public HighScorePlayerList(Context context, ArrayList<HighScorePlayer> highScorePlayer){
        super(context,0,highScorePlayer);
        this.highScorePlayers = highScorePlayer;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.high_score_player_content, parent,false);
        }

        HighScorePlayer highScorePlayer = highScorePlayers.get(position);
        TextView rank = view.findViewById(R.id.higher_score_player_rank_text);
        TextView userName = view.findViewById(R.id.higher_score_player_username_text);
        TextView score = view.findViewById(R.id.higher_score_player_score_text);
        int intRank = highScorePlayer.getRank();
        String strRank = Integer.toString(intRank);
        rank.setText(strRank);
        userName.setText("username: " + highScorePlayer.getUsername());
        score.setText("score: " + highScorePlayer.getScore());

        return view;

    }
}

