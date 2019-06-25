package com.aboubakr.beastshopping.infrastructure;

public class Utils {
    public static final String FIREBASE_USER_REFERENCE = "users/";


   

    public static String encodeEmail(String userEmail){
        return  userEmail.replace(".",",");
    }
}
