package com.casper.creitive.WCFHandlers.Communication;

import android.os.AsyncTask;

import com.casper.creitive.User.UserProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Desu on 2018-02-21.
 */

public class WCFPostRequest extends AsyncTask<Void,Void,Void>
{
    private WCFResponse _response;
    private String _jsonData;
    private String _url;

    public WCFPostRequest(WCFResponse response, String url, String jsonData)
    {
        this._response = response;
        this._url = url;
        this._jsonData = jsonData;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            URL connectUrl = new URL(WCFHandler.wcfLocation + _url);
            HttpURLConnection connection = (HttpURLConnection)connectUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(_jsonData.length()));
            connection.setRequestProperty("X-Authorize", UserProfile.accessToken);

            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            streamWriter.write(_jsonData);
            streamWriter.flush();

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
