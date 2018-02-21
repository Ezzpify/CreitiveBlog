package com.casper.creitive.WCFHandlers.User;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.casper.creitive.R;

/**
 * Created by Desu on 2018-02-21.
 */

public class UserSerializable
{
    private Activity _context;
    private SharedPreferences sharedPreferences;

    public String accessToken;

    public UserSerializable(Activity activity)
    {
        _context = activity;

        sharedPreferences = activity.getSharedPreferences(_context.getResources().getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains("access_token"))
            accessToken = sharedPreferences.getString("access_token", accessToken);
    }

    public void saveValues()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", accessToken);
        editor.apply();
    }
}
