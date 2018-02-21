package com.casper.creitive.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.casper.creitive.Activities.BlogActivity;
import com.casper.creitive.R;
import com.casper.creitive.WCFHandlers.DataClasses.BlogPostHolder;
import com.joooonho.SelectableRoundedImageView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

/**
 * Created by Desu on 2018-02-21.
 */

public class BlogItemAdapter extends RecyclerView.Adapter
{
    private Context _context;
    private ArrayList<BlogPostHolder> _items;

    public BlogItemAdapter() {
        _items = new ArrayList<>();
    }

    private BlogPostHolder getItem(int position)
    {
        return _items.get(position);
    }

    public void setItems(ArrayList<BlogPostHolder> matches) {
        _items = matches;
        notifyItemInserted(_items.size() - 1);
    }

    @Override
    public int getItemCount()
    {
        return _items.size();
    }

    @Override
    public long getItemId(int position)
    {
        return getItem(position).id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        _context = parent.getContext();
        return new ItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final BlogItemAdapter.ItemViewHolder viewHolder = (BlogItemAdapter.ItemViewHolder) holder;
        final BlogPostHolder item = getItem(position);

        viewHolder.view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent blogIntent = new Intent(_context, BlogActivity.class);
                blogIntent.putExtra("id", item.id);
                blogIntent.putExtra("image", item.imageUrl);
                blogIntent.putExtra("title", item.title);
                _context.startActivity(blogIntent);
            }
        });

        Glide.with(_context).load(item.imageUrl).into(viewHolder.image);
        viewHolder.title.setText(item.title);
        viewHolder.description.setHtml(item.description);
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        HtmlTextView description;
        SelectableRoundedImageView image;

        private ItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_blog_item, parent, false));
            view = itemView;
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            description = itemView.findViewById(R.id.description);
        }
    }
}
