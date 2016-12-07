package com.blueplanet.smartcookieteacher.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.blueplanet.smartcookieteacher.MainApplication;


/**
 * Created by web on 02-07-2015.
 * @author dhanashree.ghayal
 * Class to check the availability of network
 */
public class NetworkManager {

    private static Context _context;
    public static void setApplicationContext ( Context context ) {

        _context = context;
    }
    public static Context getApplicationContext () {
        return _context;
    }

    public static boolean isNetworkAvailable () {

        ConnectivityManager manager =
                ( ConnectivityManager ) _context.getSystemService ( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = manager.getActiveNetworkInfo ();
        if ( networkInfo == null ) {
            return false;
        }
        return networkInfo.isConnectedOrConnecting ();

    }// end of isNetworkAvailable()


    /**
     * Checks if device is connected to a wi fi network
     *
     * @return true if connected to wi fi, false if otherwise.
     */
    public boolean isWifiAvailable( ) {
        ConnectivityManager connMgr =
                (ConnectivityManager) MainApplication.getContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connMgr.getNetworkInfo( ConnectivityManager.TYPE_WIFI );
        return networkInfo.isConnected( );
    }

    /**
     * Checks if device is connected to a wi fi network
     *
     * @return true if connected to wi fi, false if otherwise.
     */
    public boolean isMobileDataNetworkAvailable( ) {
        ConnectivityManager connMgr =
                (ConnectivityManager) MainApplication.getContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo.isConnected();
    }

    /**
     * Method checks if device is connected to either wi fi or mobile data. To check individual
     * connectivity of wi fi and mobile data separate methods provided should be used
     *
     * @return true if device is connected to internet, false otherwise.
     */
    public boolean isConnected( ) {
        return isWifiAvailable( ) || isMobileDataNetworkAvailable( );
    }


}
