package com.example.gauravagarwal.quotes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;

/**
 * Created by Gaurav Agarwal on 27-02-2017.
 */

public class Utils {
    private static FirebaseDatabase mDatabase;
    private static FirebaseAuth mAuth;
    public static HashSet<String> quotes = new HashSet<String>();

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static FirebaseAuth getAuth() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static void signOut() {
        mAuth.signOut();
        mAuth = null;
        quotes = null;
        mDatabase = null;
    }

    public static String getUserId() {
        return getAuth().getCurrentUser().getUid();

    }

    public static void changeFilter(String filter) {
        getDatabase().getReference()
                .child("users")
                .child(getUserId())
                .child(filter)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        quotes.add(dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        quotes.remove(dataSnapshot.getKey());
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
}
