package com.casper.creitive.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.casper.creitive.Adapters.BlogItemAdapter;
import com.casper.creitive.Dialogs.NoticeDialog;
import com.casper.creitive.R;
import com.casper.creitive.WCFHandlers.Communication.WCFHandler;
import com.casper.creitive.WCFHandlers.Communication.WCFResponse;
import com.casper.creitive.WCFHandlers.DataClasses.BlogPostHolder;
import com.casper.creitive.WCFHandlers.User.UserSerializable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Desu on 2018-02-21.
 */

public class MainActivity extends AppCompatActivity
{
    private UserSerializable _userSer;
    private RecyclerView _recyclerView;
    private ArrayList<BlogPostHolder> _blogPosts;
    private ProgressBar _loadingProgress;
    private Button _retryButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _recyclerView = findViewById(R.id.blog_list);
        _recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        _recyclerView.setAdapter(new BlogItemAdapter());
        _recyclerView.setHasFixedSize(true);

        _loadingProgress = findViewById(R.id.blog_progress);

        _retryButton = findViewById(R.id.retry);
        _retryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                _retryButton.setVisibility(View.GONE);
                fetchBlogPosts();
            }
        });

        _userSer = new UserSerializable(MainActivity.this);
        fetchBlogPosts();
    }

    private void fetchBlogPosts()
    {
        _blogPosts = new ArrayList<>();
        WCFHandler.getInstance().sendGetRequest(new WCFResponse()
        {
            @Override
            public void getResponse(String response)
            {
                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++)
                        _blogPosts.add(new BlogPostHolder().deserializeData(jsonArray.getJSONObject(i)));

                    ((BlogItemAdapter)_recyclerView.getAdapter()).setItems(_blogPosts);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            _recyclerView.setVisibility(View.VISIBLE);
                            _loadingProgress.setVisibility(View.GONE);
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String response, final int code)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        switch (code)
                        {
                            //Unauthorized - Go back to login blyat
                            case 401:
                                _userSer.accessToken = "";
                                _userSer.saveValues();

                                NoticeDialog nd = new NoticeDialog(MainActivity.this, getString(R.string.token_expired), getString(R.string.token_expired_desc));
                                nd.setOnDismissListener(new DialogInterface.OnDismissListener()
                                {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface)
                                    {
                                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                        finish();
                                    }
                                });
                                nd.show();
                                break;

                            //Waduhek
                            default:
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        _loadingProgress.setVisibility(View.GONE);
                                        _retryButton.setVisibility(View.VISIBLE);
                                    }
                                });
                                break;
                        }
                    }
                });
            }
        }, "blogs");
    }
}
