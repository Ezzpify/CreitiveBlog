package com.casper.creitive.WCFHandlers.DataClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Desu on 2018-02-21.
 */

public class LoginDataHolder
{
    public static String getJson(String email, String password) throws JSONException
    {
        JSONObject jObject = new JSONObject();
        jObject.put("email", email);
        jObject.put("password", password);
        return jObject.toString();
    }
}