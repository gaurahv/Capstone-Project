package com.example.gauravagarwal.quotes.Widget;

import android.content.Intent;
import android.os.RemoteCallbackList;
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
