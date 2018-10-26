package com.example.dhwani.findyourmate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class PrefUtil {
    public static final String PRE_LOGINCHECK = "pref_logincheck";
    public static final String PRE_LOGOUT="pref_logoutcheck";
    public static final String PRF_USERNAME = "pref_username";
    public static final String PRF_EMAIL = "pref_email";
    public static final String PRF_GENDER = "pref_gender";
    public static final String PRF_BIRTHDATE = "pref_birthdate";
    public static final String PRF_PASSWORD = "pref_password";
    public static final String PRF_LATITUDE = "pref_latitude";
    public static final String PRF_LONGTITUDE = "pref_longtitude";



    public static void putstringPref(String key, String value, Context context) {
        SharedPreferences preferences=context.getSharedPreferences("PutString",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void RemoveString(Context context){
        SharedPreferences preferences=context.getSharedPreferences("PutString",Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    public static String getstringPref(String key, Context context) {
        SharedPreferences references=context.getSharedPreferences("PutString",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=references.edit();
        editor.commit();
        return references.getString(key, "");
    }

    public static void putintPref(String key, int value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getintPref(String key, Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key,0);
    }



    public static void putbooleanPref(String key, boolean value, Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }


    public static boolean getbooleanPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }
}