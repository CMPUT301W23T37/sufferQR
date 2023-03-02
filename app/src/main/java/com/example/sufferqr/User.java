package com.example.sufferqr;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

    public User(String name, String email, String QRid, String Region, int QRcount, int highestScore, int lowestScore, int sumScore, List<Long> scores){
        this.username = name;
        this.email = email;
        this.QRid = QRid;
        this.region = Region;
        this.QRcount = QRcount;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.sumScore = sumScore;
        this.scores = scores;
    }

    public User(){}

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

    public String getRegion(){
        return region;
    }
    public void setRegion(String region){
        this.region = region;
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

}
