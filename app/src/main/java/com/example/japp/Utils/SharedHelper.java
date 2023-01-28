package com.example.japp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static String firstName = "FIRST_NAME";
    public static String lastName = "LAST_NAME";
    public static String uid = "UID";
    public static String type = "TYPE";
    public static String email = "EMAIL";
    public static String phone = "PHONE";
    public static String dateOfBirth = "DATEOFBIRTH";
    public static String country = "COUNTRY";
    public static String nationality = "NATIONALITY";
    public static String city = "CITY";
    public static String user = "USER";
    public static String photo = "PHOTO";
    public static String pdf = "PDF";

    public SharedHelper() {

    }

    SharedPreferences pref;

    private SharedPreferences getSharedPref(Context context) {
        if (pref == null) {
            pref = context.getSharedPreferences("app_data", Context.MODE_PRIVATE);
        }
        return pref;
    }

    private SharedPreferences.Editor getSharedPrefEditor(Context context) {
        return getSharedPref(context).edit();
    }

    public void saveString(Context context, String key, String value) {
        getSharedPrefEditor(context).putString(key, value).apply();
    }

    public void saveInt(Context context, String key, int value) {
        getSharedPrefEditor(context).putInt(key, value).apply();
    }

    public void saveBoolean(Context context, String key, boolean value) {
        getSharedPrefEditor(context).putBoolean(key, value).apply();
    }

    public String getString(Context context, String key) {
        return getSharedPref(context).getString(key, "").toString();
    }

    public int getInt(Context context, String key) {
        return getSharedPref(context).getInt(key, -1);
    }

    public boolean getBoolean(Context context, String key) {
        return getSharedPref(context).getBoolean(key, false);
    }
}
