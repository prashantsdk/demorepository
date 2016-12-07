package com.blueplanet.smartcookieteacher.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


/**
 * Created by web on 02-07-2015.
 *
 * @author dhanashree.ghayal
 *         class to register a broadcast event whenever there is a change in INTERNET connection
 */
public class NetworkReceiver extends BroadcastReceiver {

    private final String _TAG = NetworkReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetworkManager.isNetworkAvailable() == false) {
            // fire event when network goes off
            Log.i(_TAG, " " + "In onReceive Network not available");

            EventNotifier notifieralarm =
                    NotifierFactory.getInstance().getNotifier(
                            NotifierFactory.EVENT_NOTIFIER_NETWORK);
            notifieralarm.eventNotify(EventTypes.EVENT_NETWORK_UNAVAILABLE, null);
        } else {
            // fire event if network available
            Log.i(_TAG, " " + "In onReceive Network available");

            EventNotifier notifieralarm =
                    NotifierFactory.getInstance().getNotifier(
                            NotifierFactory.EVENT_NOTIFIER_NETWORK);
            notifieralarm.eventNotify(EventTypes.EVENT_NETWORK_AVAILABLE, null);
        }
    }// end of onReceive
}

