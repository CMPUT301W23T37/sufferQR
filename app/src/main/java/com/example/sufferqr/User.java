package com.example.sufferqr;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * user info saving
 */
public class User {
    private String username;
    private String email;
    private String QRid;
    private String region;
    private int QRcount;
    private int highestScore;
    private int lowestScore;
    private int sumScore;
    private List<Long> scores;

    private Boolean allowViewEmail;
    private Boolean allowViewQrid;
    private Boolean allowViewScanRecord;

    public User(String name, String email, String QRid, int QRcount, int highestScore, int lowestScore, int sumScore, List<Long> scores, boolean allowEmail, boolean allowQrid, boolean allowScan){
        this.username = name;
        this.email = email;
        this.QRid = QRid;
        this.QRcount = QRcount;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.sumScore = sumScore;
        this.scores = scores;
        this.allowViewEmail = allowEmail;
        this.allowViewQrid = allowQrid;
        this.allowViewScanRecord = allowScan;
    }

    public User(String name, String qrid){
        this.username = name;
        this.QRid = qrid;
    }



    public String getName(){
        return username;
    }
    public void setName(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getQRid(){
        return QRid;
    }
    public void setQRid(String QRid){
        this.QRid = QRid;
    }

    public int getQRcount(){
        return QRcount;
    }
    public void setQRcount(int QRcount){
        this.QRcount = QRcount;
    }

    public int getHighestScore(){
        return highestScore;
    }
    public void setHighestScore(int highestScore){
        this.highestScore = highestScore;
    }

    public int getLowestScore(){
        return lowestScore;
    }
    public void setLowestScore(int lowestScore){
        this.lowestScore = lowestScore;
    }

    public int getSumScore(){
        return sumScore;
    }
    public void setSumScore(int sumScore){
        this.sumScore = sumScore;
    }

    public List<Long> getScores(){return scores;}
    public void addScores(long score){scores.add(score);}

    public Boolean getAllowViewEmail() {
        return allowViewEmail;
    }

    public Boolean getAllowViewQrid() {
        return allowViewQrid;
    }

    public Boolean getAllowViewScanRecord() {
        return allowViewScanRecord;
    }

    public void setAllowViewEmail(Boolean allowViewEmail) {
        this.allowViewEmail = allowViewEmail;
    }

    public void setAllowViewQrid(Boolean allowViewQrid) {
        this.allowViewQrid = allowViewQrid;
    }

    public void setAllowViewScanRecord(Boolean allowViewScanRecord) {
        this.allowViewScanRecord = allowViewScanRecord;
    }
}
