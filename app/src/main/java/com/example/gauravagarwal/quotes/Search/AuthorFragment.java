package com.example.gauravagarwal.quotes.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gauravagarwal.quotes.R;
import com.example.gauravagarwal.quotes.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class AuthorFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<SearchItem> authList;
    private FastScrollRecyclerView fastScrollRecyclerView;
    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authList = new ArrayList<SearchItem>();
        mSearchAdapter = new SearchAdapter(authList, false);
        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mDatabaseReference = Utils.getDatabase().getReference().child("authors");
        mDatabaseReference.orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("Quotes", dataSnapshot.toString());
                authList.add(new SearchItem((String) dataSnapshot.getValue()));
                mSearchAdapter.notifyDataSetChanged();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author, container, false);
        fastScrollRecyclerView = (FastScrollRecyclerView) view.findViewById(R.id.auth_recycler_view);
        if (fastScrollRecyclerView != null) {
            fastScrollRecyclerView.setLayoutManager(mLinearLayoutManager);
            fastScrollRecyclerView.setAdapter(mSearchAdapter);
        }
        return view;
    }
}
