package com.example.sufferqr;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * saving qrcode clss for nearby 1km qr
 */
public class QrCode {

    private  double dist;
    private Map<String,Object> doc;

    /**
     * lauch class
     * @param dis distance
     * @param data document
     */
    QrCode(double dis, Map<String,Object> data){
        this.dist = dis;
        this.doc = data;
    }

    /**
     * @return address
     */
    String getLocationAddress(){ return String.valueOf(this.doc.get("LocationAddress")); }

    /**
     * @return privicy settings
     */
    boolean getlocationExist(){ return (Boolean) this.doc.get("LocationExist"); }

    /**
     * @return get Latitude
     */
    String getLocationLatitude(){ return String.valueOf(doc.get("LocationLatitude")); }

    /**
     * @return get Longtitude
     */
    String getLocationLongitude(){ return String.valueOf(doc.get("LocationLongitude")); }

    /**
     * @return get address name
     */
    String getLocationName(){ return String.valueOf(doc.get("LocationName")); }

    /**
     * @return get unique QR id
     */
    String getQrName(){ return String.valueOf(doc.get("QRname")); }

    /**
     * @return get Date
     */
    String getDate(){ return String.valueOf(doc.get("date")); }

    /**
     * @return get points
     */
    String getPoints(){ return String.valueOf(doc.get("points")); }

    /**
     * @return get distance
     */
    String getDis(){
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(dist));
    }

    /**
     * @return document
     */
    Map<String,Object> getData(){
        return this.doc;
    }

}


