package com.casper.creitive.WCFHandlers.Communication;

import android.os.AsyncTask;

import com.casper.creitive.User.UserProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Desu on 2018-02-21.
 */

public class WCFGetRequest extends AsyncTask<Void,Void,Void>
{
    private WCFResponse _response;
    private String _url;

    public WCFGetRequest(WCFResponse response, String url)
    {
        this._response = response;
        this._url = url;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            URL connectUrl = new URL(WCFHandler.wcfLocation + _url);
            HttpURLConnection connection = (HttpURLConnection)connectUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-Authorize", UserProfile.accessToken);

            StringBuilder stringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String responseStr;
                while ((responseStr = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(responseStr);
                }
                bufferedReader.close();
                _response.getResponse(stringBuilder.toString());
            }
            else
            {
                _response.onError("Invalid status code", connection.getResponseCode());
            }

            connection.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            _response.onError(e.getMessage(), -1);
        }

        return null;
    }
}