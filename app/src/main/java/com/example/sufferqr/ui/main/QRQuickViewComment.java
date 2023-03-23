package com.example.sufferqr.ui.main;

import java.io.Serializable;

public class QRQuickViewComment implements Serializable {
    private String userName;
    private String cDate;
    private String comment;

    public QRQuickViewComment(String user, String dat, String com) {
        this.userName = user;
        this.cDate = dat;
        this.comment = com;
    }

    public String getUserName() {
        if (userName == null) {
            return "";
        }
        else {
            return userName;
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCDate() {
        if (cDate == null) {
            return "";
        }
        else {
            return cDate;
        }
    }

    public void setCDate(String date) {
        this.cDate = date;
    }

    public String getComment() {
        if (comment == null) {
            return "";
        }
        else {
            return comment;
        }
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
