package com.casper.creitive.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.casper.creitive.WCFHandlers.Communication.WCFHandler;

/**
 * Created by Desu on 2018-02-22.
 */

public class NetworkStatus
{
    private static NetworkStatus _instance;

    public static NetworkStatus getInstance()
    {
        if (_instance == null)
            _instance = new NetworkStatus();

        return _instance;
    }

    public boolean hasNetwork(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
