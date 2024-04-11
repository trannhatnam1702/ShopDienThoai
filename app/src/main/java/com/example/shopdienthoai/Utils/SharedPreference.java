package com.example.shopdienthoai.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String USER_PREF = "user_login";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREF, Activity.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public int getValueInt(String key){
        return sharedPreferences.getInt(key, 0);
    }

    public void setValueInt(String key, int value){
        editor.putInt(key, value).commit();
    }

    public String getValueString(String key){
        return sharedPreferences.getString(key, "");
    }

    public void setValueString(String key, String value){
        editor.putString(key, value).commit();
    }

    public boolean getValueBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public void setValueBoolean(String key, boolean value){
        editor.putBoolean(key, value).commit();
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }
}
