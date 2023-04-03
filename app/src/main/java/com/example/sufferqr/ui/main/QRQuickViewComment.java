package com.example.sufferqr.ui.main;

import java.io.Serializable;

/**
 * comment information storage
 */
public class QRQuickViewComment implements Serializable {
    private String userName;
    private String cDate;
    private String comment;
    private String androidId;

    /**
     * class set up
     * @param user userName
     * @param dat date
     * @param com wrote message
     * @param androidId device id
     */
    public QRQuickViewComment(String user, String dat, String com, String androidId) {
        this.userName = user;
        this.cDate = dat;
        this.comment = com;
        this.androidId = androidId;
    }

    /**
     * get usernane
     * @return username
     */
    public String getUserName() {
        if (userName == null) {
            return "";
        }
        else {
            return userName;
        }
    }

    /**
     * @param userName setUsername
     */
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

    /**
     * @param date setDate
     */
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

    /**
     * @param comment set comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * get android id
     * @return string
     */
    public String getAndroidId() {
        if (androidId == null) {
            return "";
        }
        else {
            return androidId;
        }
    }

    /**
     * set android id
     * @param androidId device id
     */
    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

}
