package com.aboubakr.beastshopping.entities;

import java.util.HashMap;

public class User {
    private String name;
    private String email;
    private HashMap<String, Object> dateJoined;
    private boolean hasLoggedInWithPassword;

    public User() {
    }

    public User(String name, String email, HashMap<String, Object> dateJoined, boolean hasLoggedInWithPassword) {
        this.name = name;
        this.email = email;
        this.dateJoined = dateJoined;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }




    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getDateJoined() {
        return dateJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }




}
