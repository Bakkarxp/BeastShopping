package com.aboubakr.beastshopping.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.aboubakr.beastshopping.infrastructure.BeastShoppingApplication;
import com.aboubakr.beastshopping.infrastructure.Utils;
import com.squareup.otto.Bus;

public class BaseDialog extends DialogFragment {
    protected BeastShoppingApplication application;
    protected Bus bus;
    protected String userName, userEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BeastShoppingApplication) getActivity().getApplication();
        bus  = application.getBus();
        bus.register(this);
        userEmail = getActivity().getSharedPreferences(Utils.MY_PREFERENCE, Context.MODE_PRIVATE).getString(Utils.EMAIL,"");
        userName = getActivity().getSharedPreferences(Utils.MY_PREFERENCE, Context.MODE_PRIVATE).getString(Utils.USERNAME,"");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
