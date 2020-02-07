package com.example.squapp;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {

    private String userPoolId = "eu-west-1_7pSdTUsaj";
    private String clientID = "ebco2ogbsdso0n775udi6qmd2";
    private String clientSecret = "1iv4qs65mrkv8ec9g27fhcnpko6chaugk3o11sqgnvesmdhqklf6";
    private Regions cognitoRegion = Regions.EU_WEST_1;

    private Context context;

    public CognitoSettings(Context context){
        this.context = context;
    }

    public String getUserPoolId(){
        return userPoolId;
    }

    public String getClientID(){
        return clientID;
    }

    public String getClientSecret(){
        return clientSecret;
    }

    public Regions getCognitoRegion(){
        return cognitoRegion;
    }

    //the entry point for all interactions with your user pool from your app
    public CognitoUserPool getUserPool(){
        return new CognitoUserPool(context, userPoolId, clientID,
                clientSecret, cognitoRegion);
    }
}
