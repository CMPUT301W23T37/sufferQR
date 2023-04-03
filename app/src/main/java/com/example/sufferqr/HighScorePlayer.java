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


    /**
     * launch class
     * @param rank ranking
     * @param username username
     * @param score total score
     * @param qrid qrid
     */
    HighScorePlayer(int rank, String username, int score, String qrid){
        this.rank = rank;
        this.username = username;
        this.score = score;
        this.qrid = qrid;
    }

    /**
     * get rank
     * @return integer
     */
    int getRank(){ return this.rank;}

    /**
     * @return userName
     */
    String getUsername(){ return this.username; }

    /**
     * @return score
     */
    int getScore(){ return this.score; }

    /**
     * @return qr id
     */
    String gerQrid(){ return this.qrid;}


}
