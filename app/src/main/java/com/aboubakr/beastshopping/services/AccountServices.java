package com.aboubakr.beastshopping.services;

import android.app.ProgressDialog;

import com.aboubakr.beastshopping.infrastructure.ServiceResponse;
import com.facebook.AccessToken;

// Event argument class (the data to be passed through event bus
public class AccountServices {
    private AccountServices() {
    }

    public static class RegisterUserRequest{
        public String userName;
        public String userEmail;
        public ProgressDialog progressDialog;

        public RegisterUserRequest(String userName, String userEmail, ProgressDialog progressDialog) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.progressDialog = progressDialog;
        }
    }

    public static class RegisterUserResponse extends ServiceResponse{

    }

    public static class LogInUserRequest{
        public String userPassword;
        public String userEmail;
        public ProgressDialog progressDialog;

        public LogInUserRequest( String userEmail,String userPassword, ProgressDialog progressDialog) {
            this.userEmail = userEmail;
            this.userPassword = userPassword;
            this.progressDialog = progressDialog;
        }
    }

    public static class LogInUserResponse extends ServiceResponse{

    }
    public static class LogUserInFacebookRequest{
        public AccessToken accessToken;
        public ProgressDialog progressDialog;
        public String userName;
        public String userEmail;

        public LogUserInFacebookRequest(AccessToken accessToken, ProgressDialog progressDialog, String userName, String userEmail) {
            this.accessToken = accessToken;
            this.progressDialog = progressDialog;
            this.userName = userName;
            this.userEmail = userEmail;
        }
    }
}
