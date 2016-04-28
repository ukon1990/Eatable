package net.jonaskf.eatable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import net.jonaskf.eatable.global.Vars;
import net.jonaskf.eatable.gui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class EatableWidget extends AppWidgetProvider {

    public static String widget_action = "";

    private static final String SEARCH_BTN_ACTION = "android.appwidget.action.OPEN_APP";
    private static final String SCAN_BTN_ACTION = "android.appwidget.action.SCAN_PROD";

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.eatable_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        //Defining onclick
        views.setOnClickPendingIntent(R.id.widget_scan_btn, getPendingSelfIntent(context, SCAN_BTN_ACTION));
        views.setOnClickPendingIntent(R.id.widget_search_btn, getPendingSelfIntent(context, SEARCH_BTN_ACTION));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(SEARCH_BTN_ACTION.equals(intent.getAction())){
            //Defining what fragment to open for MainActivity
            widget_action = Vars._SEARCH_FRAGMENT;
            //Starting MainActivity
            context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }else if(SCAN_BTN_ACTION.equals(intent.getAction())){
            //Defining what fragment to open for MainActivity
            widget_action = Vars._SCAN_PRODUCT;
            //Starting MainActivity
            context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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

    //Creating a pending intent from context and action
    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

