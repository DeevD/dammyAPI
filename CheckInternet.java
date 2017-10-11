package com.comquas.mahar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by comquas on 8/3/17.
 */

public class CheckInternet {

    public static int TYPE;


    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

//    public static int isWifi(Context context) {
//        boolean isOnWifi;
//        boolean is3G;
//        int wifi = 1;
//        int cellular = 0;
//        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (mWifi != null) {
//            Toast.makeText(context, "wifi", Toast.LENGTH_SHORT).show();
//            isOnWifi = mWifi.isConnected();
//            return 1;
//        } else {
//            Toast.makeText(context, "cellular", Toast.LENGTH_SHORT).show();
//            is3G = m3G.isConnected();
//            return 0;
//        }
//    }

    public static int wifiOrCellular(Context context)
    {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                TYPE =  1;
                // connected to wifi
                Toast.makeText(context, activeNetwork.getTypeName() + TYPE, Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                TYPE = 0;
                // connected to the mobile provider's data plan
                Toast.makeText(context, activeNetwork.getTypeName() + TYPE, Toast.LENGTH_SHORT).show();
            }
        } else {

            // not connected to the internet
        }
        return TYPE;
    }
}
