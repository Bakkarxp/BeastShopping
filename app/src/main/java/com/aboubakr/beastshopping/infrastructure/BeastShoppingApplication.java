package com.aboubakr.beastshopping.infrastructure;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Bus;

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

        bus.register(this);
    }

}
