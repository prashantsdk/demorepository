package com.blueplanet.smartcookieteacher;

import android.app.Application;
import android.content.Context;

import com.blueplanet.smartcookieteacher.DatabaseManager.SQLDatabaseManager;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.ui.GPSTracker;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;


/**
 * Created by 1311 on 24-11-2015.
 */
public class MainApplication extends Application {

    private static Context _context;
    public static GPSTracker _gpsTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = this;
        NetworkManager.setApplicationContext(this);
        SmartCookieImageLoader.getInstance().initImageLoaderConfiguration(this);
        SmartCookieSharedPreferences.init(this);
        SQLDatabaseManager.getInstance().setApplicationContext(_context);


    }

    public static GPSTracker getGps() {
        _gpsTracker = new GPSTracker(_context);
        return _gpsTracker;
    }


    public static void setContext(Context context) {
        _context = context;
    }

    public static Context getContext() {
        return _context;
    }
}
