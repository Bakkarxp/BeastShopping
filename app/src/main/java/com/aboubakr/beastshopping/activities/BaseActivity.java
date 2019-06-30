package com.aboubakr.beastshopping.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aboubakr.beastshopping.infrastructure.BeastShoppingApplication;
import com.aboubakr.beastshopping.infrastructure.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity {

    protected BeastShoppingApplication application;
    protected Bus bus;
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;
    protected String userName, userEmail;
    protected SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BeastShoppingApplication) getApplication();
        bus = application.getBus();
        bus.register(this);


        sharedPreferences = getSharedPreferences(Utils.MY_PREFERENCE, Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString(Utils.EMAIL,"");
        userName = sharedPreferences.getString(Utils.USERNAME,"");


        auth = FirebaseAuth.getInstance();

        if(!((this instanceof LoginActivity)||(this instanceof RegisterActivity)||(this instanceof SplashScreenActivity))){
            authStateListener =new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user==null){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Utils.EMAIL, null).apply();
                        editor.putString(Utils.USERNAME, null).apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            };
             if(userEmail.equals("")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Utils.EMAIL, null).apply();
                editor.putString(Utils.USERNAME, null).apply();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!((this instanceof LoginActivity)||(this instanceof RegisterActivity)||(this instanceof SplashScreenActivity))){
            auth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        if(!((this instanceof LoginActivity)||(this instanceof RegisterActivity)||(this instanceof SplashScreenActivity))){
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
