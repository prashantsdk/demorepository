package com.blueplanet.smartcookieteacher;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

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

     /*   AutoErrorReporter.get(this)
                .setEmailAddresses("sayalir@roseland.com")
                .setEmailSubject("Auto Crash Report")
                .start();
*/
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
    public static void enableGPS(){

        try
        {

            String provider = Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                _context.sendBroadcast(poke);
            }
        }
        catch (Exception e) {

        }




    }

    public static void disableGPS(){
        String provider = Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            _context.sendBroadcast(poke);
        }
    }
}