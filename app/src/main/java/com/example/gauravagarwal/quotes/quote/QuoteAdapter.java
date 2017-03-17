package com.example.gauravagarwal.quotes.quote;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gauravagarwal.quotes.R;
import com.example.gauravagarwal.quotes.Utils;

import java.util.ArrayList;

/**
 * Created by Gaurav Agarwal on 17-02-2017.
 */
public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {

    private ArrayList<Quote> quoteList;
    private Typeface typeface;

    public QuoteAdapter(ArrayList<Quote> quoteList, Typeface typeface) {
        this.quoteList = quoteList;
        this.typeface = typeface;
    }

    @Override
    public QuoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quote, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mQuote.setText("\"" + quoteList.get(position).getQuote() + "\"");
        holder.mAuthor.setText(quoteList.get(position).getAuthor());
        holder.mQuote.setTypeface(typeface);

        final boolean isQuoteFavorite = Utils.quotes.contains(quoteList.get(position).getId());
        if (isQuoteFavorite) {
            holder.mImageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.mImageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton imageButton = (ImageButton) v;
                String userId = Utils.getAuth().getCurrentUser().getUid();
                String quoteId = quoteList.get(position).getId();
                if (isQuoteFavorite) {
                    imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Utils.getDatabase().getReference("users").child(userId).child("quotes").child(quoteId).removeValue();
                } else {
                    imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Utils.getDatabase().getReference("users").child(userId).child("quotes").child(quoteId).setValue(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mQuote;
        public TextView mAuthor;
        public ImageButton mImageButton;

        public ViewHolder(View v) {
            super(v);
            mQuote = (TextView) v.findViewById(R.id.quote);
            mAuthor = (TextView) v.findViewById(R.id.author);
            mImageButton = (ImageButton) v.findViewById(R.id.favorite_button);
        }
    }
}