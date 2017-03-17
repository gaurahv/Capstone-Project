package com.example.gauravagarwal.quotes.search;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.gauravagarwal.quotes.quote.Quote;
import com.example.gauravagarwal.quotes.quote.QuoteAdapter;
import com.example.gauravagarwal.quotes.R;
import com.example.gauravagarwal.quotes.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Quote> list;
    private RecyclerView.Adapter mAdapter;
    private Toolbar toolbar;
    private TextView title;
    boolean isFollowed;
    private CheckBox checkBox;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mRecyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.title);
        list = new ArrayList<Quote>();
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        setSupportActionBar(toolbar);

        Typeface quoteTypeface = Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf");
        Intent i = getIntent();
        final String heading = i.getStringExtra("title");
        final boolean isTag = i.getBooleanExtra("isTag", false);

        title.setText(heading);
//        list.add(new Quote("1", "Gaurav Agarwal", "You are on right track", "motivate"));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new QuoteAdapter(list, quoteTypeface);
        mRecyclerView.setAdapter(mAdapter);

        final String filter;
        if (isTag) {
            filter = "tagWise";
        } else {
            filter = "authorWise";
        }

        final DatabaseReference rootRef = Utils.getDatabase().getReference();
        rootRef.child(filter)
                .child(heading)
                .limitToFirst(50)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String id = (String) dataSnapshot.getValue();
                        rootRef.child("quotes")
                                .child(id)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String author = (String) dataSnapshot.child("author").getValue();
                                        String quote = (String) dataSnapshot.child("quote").getValue();
//                                        String tags =  dataSnapshot.child("tags").toString();
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

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        isFollowed = Utils.quotes.contains(heading);
        if (isFollowed) {
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String userId = Utils.getUserId();
                if (isChecked) {
                    Utils.getDatabase().getReference("users").child(userId).child(filter).child(heading).setValue(true);
                } else {
                    Utils.getDatabase().getReference("users").child(userId).child(filter).child(heading).setValue(null);
                }
            }
        });
    }
}
