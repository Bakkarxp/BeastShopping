package com.aboubakr.beastshopping.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.aboubakr.beastshopping.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {
    // firebase reference url for users collection
    public static final String FIREBASE_USER_REFERENCE = "users/";

    // string for shared preferences
    // shared preferences here used to save user name and email to be used in main activity
    public static final String MY_PREFERENCE ="MY_PREFERENCE";
    public static final String EMAIL ="EMAIL";
    public static final String USERNAME ="USERNAME";


    public static String encodeEmail(String userEmail){
        return  userEmail.replace(".",",");
    }
}


