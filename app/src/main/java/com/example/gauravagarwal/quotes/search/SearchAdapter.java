package com.example.gauravagarwal.quotes.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gauravagarwal.quotes.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

/**
 * Created by Gaurav Agarwal on 18-02-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    public void setList(ArrayList<SearchItem> list) {
        this.list = list;
    }
    private ArrayList<SearchItem> list;
    public Context context;
    private boolean isTag;

    public SearchAdapter(ArrayList<SearchItem> list, boolean isTag) {
        this.list = list;
        this.isTag = isTag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_search, parent, false);
        ViewHolder vh = new ViewHolder(v, isTag);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mText.setText(list.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return list.get(position).getText().substring(0, 2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mText;
        public boolean isTag;

        public ViewHolder(View v, boolean isTag) {
            super(v);
            this.isTag = isTag;
            mText = (TextView) v.findViewById(R.id.text);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("title", mText.getText());
            intent.putExtra("isTag", isTag);
            context.startActivity(intent);
        }
    }
}
