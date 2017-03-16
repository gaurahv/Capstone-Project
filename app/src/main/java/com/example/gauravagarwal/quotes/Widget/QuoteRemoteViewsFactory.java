package com.example.gauravagarwal.quotes.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.gauravagarwal.quotes.Quote.Quote;
import com.example.gauravagarwal.quotes.R;
import com.example.gauravagarwal.quotes.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Gaurav Agarwal on 16-03-2017.
 */

class QuoteRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Quote> myArrayList = new ArrayList<Quote>();
    private Context context;
    private Intent intent;
    private int widgetId;

    public QuoteRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;
        this.intent = intent;
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        String userId = Utils.getAuth().getCurrentUser().getUid();
        final DatabaseReference rootRef = Utils.getDatabase().getReference();

        if(userId == null)  return;
        rootRef.child("users")
                .child(userId)
                .child("quotes")
                .orderByKey()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String id = ((String) dataSnapshot.getKey());
                        rootRef.child("quotes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String author = (String) dataSnapshot.child("author").getValue();
                                String quote = (String) dataSnapshot.child("quote").getValue();
                                String tags = dataSnapshot.child("tags").toString();
                                myArrayList.add(new Quote(id, author, quote, tags));
                                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list_view);
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
                        for(int i=0; i<myArrayList.size(); i++){
                            if (myArrayList.get(i).getId().compareTo(dataSnapshot.getKey()) == 0) {
                                myArrayList.remove(i);
                                break;
                            }
                        }
                        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(widgetId, R.id.widget_list_view);
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
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        myArrayList.clear();
    }

    @Override
    public int getCount() {
        return myArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.quote, myArrayList.get(position).getQuote());
        remoteViews.setTextViewText(R.id.author, myArrayList.get(position).getAuthor());

//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtra(Intent.EXTRA_TEXT, myArrayList.get(position));
//        remoteViews.setOnClickFillInIntent(R.id.quote, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
