package com.aboubakr.beastshopping.infrastructure;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Bus;

import aboubakr.beastshopping.live.Module;

public class BeastShoppingApplication extends Application {
    private Bus bus;

    public BeastShoppingApplication() {
        this.bus = new Bus();
    }

    public Bus getBus() {
        return bus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(!FirebaseApp.getApps(this).isEmpty()){
            // Enable disk persistence
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        Module.Register(this);
    }

}
