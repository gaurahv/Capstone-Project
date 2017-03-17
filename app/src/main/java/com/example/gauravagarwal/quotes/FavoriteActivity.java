package com.example.gauravagarwal.quotes;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.gauravagarwal.quotes.quote.Quote;
import com.example.gauravagarwal.quotes.quote.QuoteAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private static Typeface quoteTypeface;

    private ArrayList<Quote> list;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        quoteTypeface = Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf");

        list = new ArrayList<Quote>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new QuoteAdapter(list, quoteTypeface);
        mRecyclerView.setAdapter(mAdapter);

        setFavorites();
    }

    private void setFavorites() {
        String userId = Utils.getAuth().getCurrentUser().getUid();
        final DatabaseReference rootRef = Utils.getDatabase().getReference();
        rootRef.child("users")
                .child(userId)
                .child("quotes")
                .orderByKey()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String id = dataSnapshot.getKey();
                        rootRef.child("quotes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String author = (String) dataSnapshot.child("author").getValue();
                                String quote = (String) dataSnapshot.child("quote").getValue();
                                list.add(new Quote(id, author, quote, "tag"));
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getId().compareTo(dataSnapshot.getKey()) == 0) {
                                list.remove(i);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
