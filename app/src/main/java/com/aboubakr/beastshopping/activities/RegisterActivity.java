package com.aboubakr.beastshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aboubakr.beastshopping.R;
import com.aboubakr.beastshopping.services.AccountServices;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.activity_register_loginButton)
    Button loginButton;
    @BindView(R.id.activity_register_linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.activity_register_userName)
    TextView userName;
    @BindView(R.id.activity_register_userEmail)
    TextView userEmail;
    @BindView(R.id.activity_register_registerButton)
    Button registerButton;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        linearLayout.setBackgroundResource(R.drawable.background_screen_two);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Registering......");
        mProgressDialog.setMessage("Attempeting to register");
        mProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.activity_register_loginButton)
    public void setLoginButton(){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    @OnClick(R.id.activity_register_registerButton)
    public void setRegisterButton(){
        bus.post(new AccountServices.RegisterUserRequest(userName.getText().toString(),
                userEmail.getText().toString(),
                mProgressDialog));
    }
    @Subscribe
    public void RegisterUser(AccountServices.RegisterUserResponse response){
        if(!response.didSucceed()){
            userEmail.setError(response.getPropertyErrors("email"));
            userName.setError(response.getPropertyErrors("userName"));
        }
    }
}