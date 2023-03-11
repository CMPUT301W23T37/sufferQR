package com.example.sufferqr.ui.main;

public class ScanHistoryQRRecord {

    private String QRname;
    private String points;
    private String date;

    private String loc1;

    public ScanHistoryQRRecord(String name, String point, String d1,String loc){
        QRname = name;
        points = point;
        date = d1;
        loc1=loc;
    }

    public String getName(){
        if (QRname==null){
            return "";
        } else {
            return QRname;
        }
    }

    public String getPoints(){
        if (points==null){
            return "";
        } else {
            return points;
        }
    }

    public String getDate(){
        if (date==null){
            return "";
        } else {
            return date;
        }
    }

    public String getLoc(){
        if (date==null){
            return "";
        } else {
            return loc1;
        }
    }
}
