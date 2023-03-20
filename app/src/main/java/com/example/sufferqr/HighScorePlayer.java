package com.example.sufferqr;

public class HighScorePlayer {
    private int rank;
    private String username;
    private int score;


    HighScorePlayer(int rank, String username, int score){
        this.rank = rank;
        this.username = username;
        this.score = score;
    }

    int getRank(){ return this.rank;}
    String getUsername(){ return this.username; }
    int getScore(){ return this.score; }

}
