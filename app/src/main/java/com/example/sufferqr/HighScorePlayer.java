package com.example.sufferqr;

import java.util.Map;

/**
 * highest total scores user info saving
 * highest unique QR code score user info saving
 */
public class HighScorePlayer {
    private int rank;
    private String username;
    private int score;
    private String qrid;



    HighScorePlayer(int rank, String username, int score, String qrid){
        this.rank = rank;
        this.username = username;
        this.score = score;
        this.qrid = qrid;
    }

    int getRank(){ return this.rank;}
    String getUsername(){ return this.username; }
    int getScore(){ return this.score; }
    String gerQrid(){ return this.qrid;}


}
