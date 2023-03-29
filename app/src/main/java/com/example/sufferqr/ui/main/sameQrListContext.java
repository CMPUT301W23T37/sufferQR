package com.example.sufferqr.ui.main;

import java.util.Map;

public class sameQrListContext {


    private String user;

    /**
     * load info into list
     */
    public sameQrListContext(String myUser){
        user = myUser;
    }

    /**
     * get name
     * @return qr name
     */
    public String getName(){
        if (user==null){
            return "";
        } else {
            return user;
        }
    }
}
