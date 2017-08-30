package com.blueplanet.smartcookieteacher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by web on 05-12-2015.
 * dhanashree.ghayal
 */
public class SmartCookieSharedPreferences {

    /**
     * SharedPreference Object
     */
    private static SharedPreferences _sharedPreferences;

    /**
     * Name of SharedPreferences file
     */
    private static final String PREFERENCE_NAME = "SmartCookiePreferences";

    /**
     * Key for default audio
     */
    private static final boolean EMPTY_BOOLEAN_DEFAULT_VALUE = false;

    private static final String EMPTY_STRING_DEFAULT_VALUE = "";

    /**
     * Key for offline login
     */
    private static final String IS_LOGIN = "IS_LOGIN";

    private static final String IS_TEST= "IS_TEST";

    private static final String REMEMBER_ME_KEY = "REMEMBER_ME_KEY";

    private static final String USER_NAME_KEY = "USER_NAME";

    private static final String PASS_WORD_KEY = "PASS_WORD";

    private static final String USER_ID = "USER_ID";



    private static final String PASS_PRN = "PASS_PRN";
    /**
     * Initialize public class SmartCookieSharedPreferences {
     * . This is one time initialization. It should be done from the
     * Application.onCreate().
     *
     * @param context - Application context
     */
    public static void init(Context context) {

        if (_sharedPreferences == null) {
            /** Get the shared preferences object for RO. */
            _sharedPreferences =
                    context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        //clear all from phone storage
        _sharedPreferences.edit().clear().commit();
    }

    public static boolean getLoginFlag() {
        return getBooleanSharedPreference(IS_LOGIN);
    }

    /**
     * @param loginFlag
     */
    public static void setLoginFlag(boolean loginFlag) {
        setBooleanSharedPreference(IS_LOGIN, loginFlag);


    }
    public static void setTestProduction(boolean Flag) {
        setBooleanSharedPreference(IS_TEST, Flag);
    }


    public static void setUserName(String userName) {
        setStringSharedPreference(USER_NAME_KEY, userName);
    }

    public static void setUserID(String userid) {
        setStringSharedPreference(USER_ID, userid);
    }

    public static String getUserName() {
        return getStringSharedPreference(USER_NAME_KEY);
    }

    public static void setPassowrdKey(String password) {
        setStringSharedPreference(PASS_WORD_KEY, password);
    }

    public static String getPasswordKey() {
        return getStringSharedPreference(PASS_WORD_KEY);
    }

    public static String getUserId() {
        return getStringSharedPreference(USER_ID);
    }

    public static boolean getRememberMeFlag() {
        return getBooleanSharedPreference(REMEMBER_ME_KEY);
    }


    public static void setRememberMeFlag(boolean rememberMeFlag) {
        setBooleanSharedPreference(REMEMBER_ME_KEY, rememberMeFlag);

    }
    public static void setPRNKey(String PRN) {
        setStringSharedPreference(PASS_PRN, PRN);
    }
    /**
     * Retrieves encrypted key-value and adds to SharedPreferences list.
     *
     * @param key   - Preference key
     * @param value - Preference value
     */
    public static void setStringSharedPreference(String key, String value) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key, value);
        _editor.commit();

    }

    public static void setBooleanSharedPreference(String key, boolean value) {

        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putBoolean(key, value);
        _editor.commit();

    }

    public static boolean getBooleanSharedPreference(String key) {
        boolean flag = _sharedPreferences.getBoolean(key, EMPTY_BOOLEAN_DEFAULT_VALUE);
        return flag;
    }

    public static String getStringSharedPreference(String key) {
        String value = _sharedPreferences.getString(key, EMPTY_STRING_DEFAULT_VALUE);
        return value;
    }

    public static void setGCMSharedPreference(String key, String value) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putString(key, value);
        _editor.commit();
    }



    public static String getGCMSharedPreference(String key) {
        String Gcm = _sharedPreferences.getString(key, EMPTY_STRING_DEFAULT_VALUE);
        return Gcm;
    }


    public static boolean getDeviceRegisteredOnServer() {
        boolean flag = _sharedPreferences.getBoolean(WebserviceConstants.IS_GCM_REGISTERED, EMPTY_BOOLEAN_DEFAULT_VALUE);
        return flag;
    }

    public static void setDeviceRegisteredOnServer(boolean value) {
        SharedPreferences.Editor _editor = _sharedPreferences.edit();
        _editor.putBoolean(WebserviceConstants.IS_GCM_REGISTERED, value);
        _editor.commit();
    }

}
