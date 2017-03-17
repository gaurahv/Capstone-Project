package com.example.gauravagarwal.quotes;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gauravagarwal.quotes.auth.SignupActivity;
import com.example.gauravagarwal.quotes.quote.Quote;
import com.example.gauravagarwal.quotes.quote.QuoteAdapter;
import com.example.gauravagarwal.quotes.search.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static Typeface quoteTypeface;

    private ArrayList<Quote> followlist;
    private RecyclerView mFollowRecyclerView;
    private RecyclerView.Adapter mFollowAdapter;
    private RecyclerView.LayoutManager mFollowLayoutManager;
    private Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
        new SetFilterTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quoteTypeface = Typeface.createFromAsset(getAssets(), "fonts/pacifico.ttf");
        followlist = new ArrayList<Quote>();

        mFollowRecyclerView = (RecyclerView) findViewById(R.id.follow_recycler_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFollowRecyclerView.setHasFixedSize(true);

        mFollowLayoutManager = new LinearLayoutManager(this);
        mFollowRecyclerView.setLayoutManager(mFollowLayoutManager);
        mFollowAdapter = new QuoteAdapter(followlist, quoteTypeface);
        mFollowRecyclerView.setAdapter(mFollowAdapter);

        setFeed("authorWise");
        setFeed("tagWise");
    }

    private void setFeed(final String filter) {
        String userId = Utils.getAuth().getCurrentUser().getUid();
        final DatabaseReference rootRef = Utils.getDatabase().getReference();
        rootRef.child("users").child(userId).child(filter)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String id1 = dataSnapshot.getKey();
                        rootRef.child(filter)
                                .child(id1)
                                .limitToFirst(5)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (final String id : (ArrayList<String>) dataSnapshot.getValue()) {
                                            rootRef.child("quotes")
                                                    .child(id)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String author = (String) dataSnapshot.child("author").getValue();
                                                            String quote = (String) dataSnapshot.child("quote").getValue();
                                                            if (filter.compareTo("tagWise") == 0)
                                                                followlist.add((int) Math.random() * followlist.size(), new Quote(id, author, quote, id1));
                                                            else
                                                                followlist.add((int) Math.random() * followlist.size(), new Quote(id, author, quote, "tag"));
                                                            mFollowAdapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                        }
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
                        for (int i = 0; i < followlist.size(); i++) {
                            if (filter.compareTo("tagWise") == 0 && followlist.get(i).getTags().compareTo(dataSnapshot.getKey()) == 0) {
                                followlist.remove(i);
                                i--;
                            } else if (filter.compareTo("authorWise") == 0 && followlist.get(i).getAuthor().compareTo(dataSnapshot.getKey()) == 0) {
                                followlist.remove(i);
                                i--;
                            }
                        }
                        mFollowAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFavorite:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.myFavorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            case R.id.sign:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
