package com.aboubakr.beastshopping.infrastructure;

import java.util.HashMap;
    // This class is used to keep track if user made errors
public class ServiceResponse {
    private HashMap<String,String> propertyErrors;

    public ServiceResponse() {
        propertyErrors = new HashMap<>();
    }
    public void setPropertyErrors(String property, String error){
        propertyErrors.put(property, error);
    }
    public String getPropertyErrors(String property){
        return propertyErrors.get(property);
    }
    public  boolean didSucceed(){
        // if size is 0 so no errors made
        return  (propertyErrors.size()==0);
    }
}
