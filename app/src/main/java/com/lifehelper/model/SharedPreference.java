package com.lifehelper.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.lifehelper.Constants;

public class SharedPreference {

    String getUsername, getPassword,getUserType;
    SharedPreferences sharedPreferences;

    public String getUsernameSharedPref(Context context){
        sharedPreferences =context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getUsername = sharedPreferences.getString(Constants.LOGIN_USER_NAME, "");
        return getUsername;
    }

    public String getPasswordSharedPref(Context context){
        sharedPreferences =context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getPassword = sharedPreferences.getString(Constants.LOGIN_PASSWORD, "");
        return getPassword;
    }

    public String getUserType(Context context){
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getUserType = sharedPreferences.getString(Constants.LOGIN_USER_TYPE,"");
        return getUserType;
    }

    public void saveToSharedPref(String username, String password, Context context,String userType){
        //Creating a shared preference

        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editor = sp.edit();
        //Adding values to editor
        editor.putString(Constants.LOGIN_USER_NAME, username);
        editor.putString(Constants.PASSWORD, password);
        editor.putString(Constants.USER_TYPE, userType);

        //Saving values to editor
        editor.apply();
    }

}
