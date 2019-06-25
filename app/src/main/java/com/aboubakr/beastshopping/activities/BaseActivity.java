package com.aboubakr.beastshopping.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aboubakr.beastshopping.infrastructure.BeastShoppingApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity {

    protected BeastShoppingApplication application;
    protected Bus bus;
    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BeastShoppingApplication) getApplication();
        bus = application.getBus();
        bus.register(this);

        auth = FirebaseAuth.getInstance();
        if(!((this instanceof LoginActivity)||(this instanceof RegisterActivity)||(this instanceof SplashScreenActivity))){
            authStateListener =new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user==null){
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }
            };

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
