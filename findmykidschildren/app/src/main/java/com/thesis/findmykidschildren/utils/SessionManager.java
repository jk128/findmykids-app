package com.thesis.findmykidschildren.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.auth0.android.jwt.JWT;
import com.thesis.findmykidschildren.entity.AuthLogged;

import java.util.HashMap;

public class SessionManager {
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AccessToken = "accessToken";
    public static final String KEY_RefreshToken = "refreshToken";
    // Sharedpref file name
    private static final String PREF_NAME = "ea38eed0-735e-11ea-bc55-0242ac130003";

    private static final String IS_LOGIN = "authenticated";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(AuthLogged authLogged) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, authLogged.id);
        editor.putString(KEY_AccessToken, authLogged.accessToken);
        editor.putString(KEY_RefreshToken, authLogged.refreshToken);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(_context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_AccessToken, pref.getString(KEY_AccessToken, null));
        user.put(KEY_RefreshToken, pref.getString(KEY_RefreshToken, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
//        // Clearing all data from Shared Preferences
//        editor.clear();
//        editor.commit();
//
//        // After logout redirect user to Loing Activity
//        Intent i = new Intent(_context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        // Staring Login Activity
//        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Kiểm tra token đã hết hạn chưa
     *
     * @return
     */
    public boolean isExpired() {
        String accessToken = pref.getString(KEY_AccessToken, "");

        JWT jwt = new JWT(accessToken);

        // Nếu session hết hạn
        return jwt.isExpired(0);
    }
}