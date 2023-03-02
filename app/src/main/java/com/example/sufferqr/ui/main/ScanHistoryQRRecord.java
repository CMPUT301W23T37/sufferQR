package com.example.sufferqr.ui.main;

public class ScanHistoryQRRecord {

    private String QRname;
    private String points;
    private String date;

    public ScanHistoryQRRecord(String name, String point, String d1){
        QRname = name;
        points = point;
        date = d1;
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
}
