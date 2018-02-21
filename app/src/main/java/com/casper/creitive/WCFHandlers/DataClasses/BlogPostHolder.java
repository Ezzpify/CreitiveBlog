package com.casper.creitive.WCFHandlers.DataClasses;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Desu on 2018-02-21.
 */

public class BlogPostHolder
{
    public int id;
    public String title;
    public String imageUrl;
    public String description;

    public BlogPostHolder deserializeData(JSONObject jsonObject) throws JSONException
    {
        this.id = jsonObject.getInt("id");
        this.title = jsonObject.getString("title");
        this.imageUrl = jsonObject.getString("image_url");
        this.description = jsonObject.getString("description");
        return this;
    }
}
