package com.casper.creitive.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.casper.creitive.R;
import com.casper.creitive.WCFHandlers.Communication.WCFHandler;
import com.casper.creitive.WCFHandlers.Communication.WCFResponse;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * Created by Desu on 2018-02-21.
 */

public class BlogActivity extends AppCompatActivity
{
    private View _loadingView;
    private View _contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id", -1);
        String imageUrl = intent.getStringExtra("image");
        setTitle(intent.getStringExtra("title"));

        final SelectableRoundedImageView image = findViewById(R.id.image);
        Glide.with(BlogActivity.this).load(imageUrl).into(image);

        final HtmlTextView authorText = findViewById(R.id.author);
        final HtmlTextView blogText = findViewById(R.id.blogtext);
        final HtmlTextView timeText = findViewById(R.id.time);

        _loadingView = findViewById(R.id.loading);
        _contentView = findViewById(R.id.content);

        showLoading(true);

        WCFHandler.getInstance().sendGetRequest(new WCFResponse()
        {
            @Override
            public void getResponse(String response)
            {
                try
                {
                    final JSONObject jsonObject = new JSONObject(response);
                    final String content = jsonObject.getString("content");

                    Document doc = Jsoup.parse(content);

                    final Element author = doc.getElementsByClass("singleArticleBlog-author").get(0);
                    final Element desc = doc.getElementsByClass("singleArticleBlog-description").get(0);
                    final Element time = doc.getElementsByClass("singleArticleBlog-publishedDate").get(0);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            authorText.setHtml(author.html());
                            timeText.setHtml(time.html());
                            blogText.setHtml(desc.html(), new HtmlHttpImageGetter(blogText));

                            showLoading(false);
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String response, int code)
            {
                showLoading(false);
                blogText.setText(getString(R.string.error_blog_load));
            }
        }, "blogs/" + id);
    }

    private void showLoading(final boolean show)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                _loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
                _contentView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }
}
