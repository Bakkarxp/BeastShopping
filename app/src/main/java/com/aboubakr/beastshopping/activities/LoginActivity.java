package com.aboubakr.beastshopping.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aboubakr.beastshopping.R;
import com.aboubakr.beastshopping.services.AccountServices;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.activity_login_linear_layout)
    LinearLayout linearLayout;

    @BindView(R.id.activity_login_registerButton)
    Button registerButton;

    @BindView(R.id.activity_login_loginButton)
    Button logInButton;

    @BindView(R.id.activity_login_facebook_button)
    LoginButton facebookButton;

    @BindView(R.id.activity_login_userEmail)
    TextView userEmail;

    @BindView(R.id.activity_login_userPassword)
    TextView userPassword;

    ProgressDialog mProgressDialog;
    CallbackManager mCallbackManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        linearLayout.setBackgroundResource(R.drawable.background_screen_two);
        // Ensure that user is signed out
        // FirebaseAuth.getInstance().signOut();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Registering......");
        mProgressDialog.setMessage("Attempeting to login");
        mProgressDialog.setCancelable(false);
    }

    @OnClick(R.id.activity_login_registerButton)
    public void setRegisterButton(){
        startActivity(new Intent(this,RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.activity_login_loginButton)
    public void setLogInButton(){
        bus.post(new AccountServices.LogInUserRequest(userEmail.getText().toString(),
        userPassword.getText().toString(),
        mProgressDialog));
    }
    @OnClick(R.id.activity_login_facebook_button)
    public void setFacebookButton(){
        mCallbackManager = CallbackManager.Factory.create();
        facebookButton.setReadPermissions(Arrays.asList("email","public_profile"));
        facebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                            String name = object.getString("name");
                            bus.post(new AccountServices.LogUserInFacebookRequest(
                                    loginResult.getAccessToken(),
                                    mProgressDialog,
                                    name,
                                    email));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(),"Unknown error occurred",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplication(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Subscribe
    public void LogInUser(AccountServices.LogInUserResponse response){
        if(!response.didSucceed()){
            userEmail.setError(response.getPropertyErrors("email"));
            userPassword.setError(response.getPropertyErrors("password"));
        }
    }
}
