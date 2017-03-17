package com.example.gauravagarwal.quotes.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Gaurav Agarwal on 16-03-2017.
 */

public class QuoteRemoteViewsService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new QuoteRemoteViewsFactory(getApplicationContext(), intent);
    }
}
