package com.blueplanet.smartcookieteacher.utils;

import android.net.TrafficStats;
import android.os.Handler;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;

/**
 * Created by prashantj on 3/29/2018.
 */

public class FindInternetSpeed {

    final  static double [] RXOld = new double [1];
    static  double currentDataRate;

    public static double findInternetSpeed(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {



            @Override
            public void run() {


                ////////////////////////Code to be executed every second////////////////////////////////////////


                double overallTraffic = TrafficStats.getMobileRxBytes();

                 currentDataRate = overallTraffic - RXOld [0];



                RXOld [0] = overallTraffic;

                handler.postDelayed(this, 1000);
            }

        }, 1000 );
        return currentDataRate;
    }
}
