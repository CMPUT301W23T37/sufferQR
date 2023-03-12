package com.example.sufferqr;

public class QrCode {
    private String locationAddress;
    private boolean locationExist;
    private String locationLatitude;
    private String locationLongitude;
    private String locationName;
    private String QrName;
    private String QrText;
    private String date;
    private boolean imageExist;
    private String points;
    private String user;

    QrCode(String locationAddress, boolean locationExist, String locationLatitude,
           String locationLongitude, String locationName, String QrName, String QrText,
           String date, boolean imageExist, String points, String user){
        this.locationAddress = locationAddress;
        this.locationExist = locationExist;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationName = locationName;
        this.QrName = QrName;
        this.QrText = QrText;
        this.date = date;
        this.imageExist = imageExist;
        this.points = points;
        this.user = user;

    }

    String getLocationAddress(){ return this.locationAddress; }
    boolean getlocationExist(){ return this.locationExist; }
    String getLocationLatitude(){ return this.locationLatitude; }
    String getLocationLongitude(){ return this.locationLongitude; }
    String getLocationName(){ return this.locationName; }
    String getQrName(){ return this.QrName; }
    String getQrText(){ return this.QrText; }
    String getDate(){ return this.date; }
    boolean getImageExist(){ return this.imageExist; }
    String getPoints(){ return this.points; }
    String getUser(){ return this.user; }

}


