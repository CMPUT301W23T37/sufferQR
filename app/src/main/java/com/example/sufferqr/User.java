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

    /**
     * setUp class
     * @param name username
     * @param email email
     * @param QRid unique qr id
     * @param QRcount  number of qr
     * @param highestScore highest score point
     * @param lowestScore lowest score point
     * @param sumScore total score
     * @param scores list of score
     * @param allowEmail privacy
     * @param allowQrid privacy
     * @param allowScan privacy
     */
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

    /**
     * get username
     * @param name username
     * @param qrid qrcode id
     */
    public User(String name, String qrid){
        this.username = name;
        this.QRid = qrid;
    }

    /**
     * no argument constructor of user objecty
     */
    public User(){}


    /**
     * get user name
     * @return user name
     */
    public String getName(){
        return username;
    }

    /**
     * set Username
     * @param username string
     */
    public void setName(String username){
        this.username = username;
    }

    /**
     * get email
     * @return email
     */
    public String getEmail(){
        return email;
    }

    /**
     * set new email
     * @param email xx@xxx.com
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * get qrcode id
     * @return qrcode od
     */
    public String getQRid(){
        return QRid;
    }
    public void setQRid(String QRid){
        this.QRid = QRid;
    }

    /**
     * get QRcount
     * @return count
     */
    public int getQRcount(){
        return QRcount;
    }

    /**
     * set number of count
     * @param QRcount qrcount
     */
    public void setQRcount(int QRcount){
        this.QRcount = QRcount;
    }

    /**
     * @return highest QRcode score
     */
    public int getHighestScore(){
        return highestScore;
    }

    /**
     * @param highestScore number of highest QRcode
     */
    public void setHighestScore(int highestScore){
        this.highestScore = highestScore;
    }

    /**
     * @return return lowest score
     */
    public int getLowestScore(){
        return lowestScore;
    }

    /**
     * @param lowestScore set the point of lowest score
     */
    public void setLowestScore(int lowestScore){
        this.lowestScore = lowestScore;
    }

    /**
     * @return get sum of user's total score
     */
    public int getSumScore(){
        return sumScore;
    }

    /**
     * @param sumScore set the sum of the score
     */
    public void setSumScore(int sumScore){
        this.sumScore = sumScore;
    }

    /**
     * @return get all the QRcode score
     */
    public List<Long> getScores(){return scores;}
    public void addScores(long score){scores.add(score);}

    /**
     * @return current privacy setting
     */
    public Boolean getAllowViewEmail() {
        return allowViewEmail;
    }

    /**
     * @return current privacy setting
     */
    public Boolean getAllowViewQrid() {
        return allowViewQrid;
    }

    /**
     * @return current privacy setting
     */
    public Boolean getAllowViewScanRecord() {
        return allowViewScanRecord;
    }

    /**
     * @param allowViewEmail update privicy setting
     */
    public void setAllowViewEmail(Boolean allowViewEmail) {
        this.allowViewEmail = allowViewEmail;
    }

    /**
     * @param allowViewQrid update privicy setting
     */
    public void setAllowViewQrid(Boolean allowViewQrid) {
        this.allowViewQrid = allowViewQrid;
    }

    /**
     * @param allowViewScanRecord update privicy setting
     */
    public void setAllowViewScanRecord(Boolean allowViewScanRecord) {
        this.allowViewScanRecord = allowViewScanRecord;
    }
}
