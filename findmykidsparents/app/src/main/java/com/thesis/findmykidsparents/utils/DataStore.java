package com.thesis.findmykidsparents.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataStore {
    // Sharedpref file name
    private static final String PREF_NAME = "b04ea8b8-6922-11ea-bc55-0242ac130003";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public DataStore(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void StoreData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String GetData(String key) {
        return pref.getString(key, null);
    }
}
