package com.casper.creitive.WCFHandlers.Communication;

import android.os.AsyncTask;

/**
 * Created by Desu on 2018-02-21.
 */

public class WCFHandler
{
    public static String wcfLocation = "http://blogsdemo.creitiveapps.com/";

    private static WCFHandler _instance;

    public static WCFHandler getInstance()
    {
        if (_instance == null)
            _instance = new WCFHandler();

        return _instance;
    }

    public void sendGetRequest(WCFResponse response, String url)
    {
        WCFGetRequest getRequest = new WCFGetRequest(response, url);
        getRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void sendPostRequest(WCFResponse response, String url, String json)
    {
        WCFPostRequest postRequest = new WCFPostRequest(response, url, json);
        postRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
