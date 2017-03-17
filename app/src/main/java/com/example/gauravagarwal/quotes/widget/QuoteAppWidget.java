package com.example.gauravagarwal.quotes.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.gauravagarwal.quotes.R;

/**
 * Implementation of App Widget functionality.
 */
public class QuoteAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//         Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.quote_app_widget);
        // Bind this widget to a Remote Views Service.
        Intent intent = new Intent(context, QuoteRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(appWidgetId, R.id.widget_list_view,
                intent);


//        views.setEmptyView(R.id.widget_list_view, R.id.widget_empty_text);

//        Intent templateIntent = new Intent(Intent.ACTION_VIEW);
//        templateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        PendingIntent templatePendingIntent = PendingIntent.getActivity(
//                context, 0, templateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.widget_list_view,
//                templatePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

