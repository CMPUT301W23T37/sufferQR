package com.example.sufferqr.ui.main;

import java.util.HashMap;
import java.util.Map;

/**
 * item data for the list
 *
 */
public class ScanHistoryQRRecord {

    private String QRname;
    private String points;
    private String date;

    private String loc1;

    private Map<String,Object> data;

    /**
     * load info into list
     * @param d1 date in yyy-mm-dd
     * @param loc location name such as ccis
     * @param name qr code name
     * @param point score of qrcode
     * @param data1 doc
     */
    public ScanHistoryQRRecord(String name, String point, String d1,String loc,Map<String,Object> data1){
        QRname = name;
        points = point;
        date = d1;
        loc1=loc;
        data = data1;
    }

    /**
     * get name
     * @return qr name
     */
    public String getName(){
        if (QRname==null){
            return "";
        } else {
            return QRname;
        }
    }

    /**
     * add in data setup
     * @return qr score
     */
    public String getPoints(){
        if (points==null){
            return "";
        } else {
            return points;
        }
    }

    /**
     * add in data setup
     * @return date
     */
    public String getDate(){
        if (date==null){
            return "";
        } else {
            return date;
        }
    }

    /**
     * add in data setup
     * @return location name
     */
    public String getLoc(){
        if (date==null){
            return "";
        } else {
            return loc1;
        }
    }

    /**
     * get entire collection
     * @return data
     */
    public Map<String,Object> getMap(){
        return data;
    }
}
