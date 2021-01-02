package com.nurbk.ps.finalproject.Utlis;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {


    SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferenceHelper instance;

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferenceHelper(context);
        return instance;
    }

    public static final String PREF_FILE_NAME = "Preference";


    public static final String PREF_BASE = "base";
    public static final String PREF_SYMBOLS = "symbols";
    public static final String PREF_IMAGE_BASE = "imageBase";
    public static final String PREF_IMAGE_SYMBOLS = "imageSymbols";
    public static final String PREF_PRICE = "price";
    public static final String PREF_TEXT = "text";
    public static final String PREF_IMAGE_VISABLE = "visbility";
    public static final String PREF_CONVERT = "convert";


    public void setData(String base,
                        String symbols,
                        int imageBase,
                        int imageSymbols

    ) {

        sharedPreferences.edit().putString(PREF_BASE, base).apply();
        sharedPreferences.edit().putString(PREF_SYMBOLS, symbols).apply();
        sharedPreferences.edit().putInt(PREF_IMAGE_BASE, imageBase).apply();
        sharedPreferences.edit().putInt(PREF_IMAGE_SYMBOLS, imageSymbols).apply();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
