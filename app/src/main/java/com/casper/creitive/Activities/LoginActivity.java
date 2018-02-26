package com.casper.creitive.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.casper.creitive.Dialogs.NoticeDialog;
import com.casper.creitive.R;
import com.casper.creitive.Tasks.Tasks;
import com.casper.creitive.Utils.NetworkStatus;
import com.casper.creitive.Utils.Utils;
import com.casper.creitive.WCFHandlers.Communication.WCFHandler;
import com.casper.creitive.WCFHandlers.Communication.WCFResponse;
import com.casper.creitive.WCFHandlers.DataClasses.LoginDataHolder;
import com.casper.creitive.User.UserProfile;
import com.casper.creitive.User.UserSerializable;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{
    private UserSerializable _userSer;
    private AutoCompleteTextView _emailView;
    private EditText _passwordView;
    private View _progressView;
    private View _loginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _userSer = new UserSerializable(LoginActivity.this);

        _loginFormView = findViewById(R.id.login_form);
        _progressView = findViewById(R.id.login_progress);

        if (TextUtils.isEmpty(_userSer.accessToken))
        {
            _emailView = findViewById(R.id.email);
            _passwordView = findViewById(R.id.password);
            _passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
                {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL)
                    {
                        checkLoginCredentials();
                        return true;
                    }
                    return false;
                }
            });

            Button emailSignInButton = findViewById(R.id.email_sign_in_button);
            emailSignInButton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    checkLoginCredentials();
                }
            });
        }
        else
        {
            UserProfile.accessToken = _userSer.accessToken;
            startNextActivity();
        }
    }

    private void checkLoginCredentials()
    {
        if (!NetworkStatus.getInstance().hasNetwork(LoginActivity.this))
        {
            NoticeDialog nd = new NoticeDialog(LoginActivity.this, getString(R.string.error_no_network), getString(R.string.error_no_network_desc));
            nd.show();
            return;
        }

        _emailView.setError(null);
        _passwordView.setError(null);

        String email = _emailView.getText().toString().trim();
        String password = _passwordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !Utils.validatePassword(password))
        {
            _passwordView.setError(getString(R.string.error_invalid_password));
            focusView = _passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email))
        {
            _emailView.setError(getString(R.string.error_field_required));
            focusView = _emailView;
            cancel = true;
        }
        else if (!Utils.validateEmail(email))
        {
            _emailView.setError(getString(R.string.error_invalid_email));
            focusView = _emailView;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
            return;
        }

        showProgress(true);
        doLogin(email, password);
    }

    private void doLogin(String email, String password)
    {
        try
        {
            WCFHandler.getInstance().sendPostRequest(new WCFResponse()
            {
                @Override
                public void getResponse(String response)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("token"))
                        {
                            UserProfile.accessToken = jsonObject.getString("token");

                            _userSer.accessToken = UserProfile.accessToken;
                            _userSer.saveValues();

                            startNextActivity();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String response, int error)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            showProgress(false);
                        }
                    });
                }
            }, "login", LoginDataHolder.getJson(email, password));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void showProgress(final boolean show)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                _progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                _loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void startNextActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        showProgress(false);
        finish();
    }
}
