package com.example.sufferqr;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * saving qrcode clss for nearby 1km qr
 */
public class QrCode {

    private  double dist;
    private Map<String,Object> doc;


    QrCode(double dis, Map<String,Object> data){
        this.dist = dis;
        this.doc = data;
    }


    String getLocationAddress(){ return (String) this.doc.get("LocationAddress"); }
    boolean getlocationExist(){ return (boolean) this.doc.get("LocationExist"); }
    String getLocationLatitude(){ return (String) doc.get("LocationLatitude"); }
    String getLocationLongitude(){ return (String)doc.get("LocationLongitude"); }
    String getLocationName(){ return (String) doc.get("LocationName"); }
    String getQrName(){ return (String) doc.get("QRname"); }
    String getDate(){ return (String) doc.get("date"); }
    String getPoints(){ return (String) doc.get("points"); }

    String getDis(){
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(dist));
    }

    Map<String,Object> getData(){
        return this.doc;
    }

}


