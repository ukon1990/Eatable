package net.jonaskf.eatable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import net.jonaskf.eatable.gui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class EatableWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.eatable_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent (context, MainActivity.class);
            //TODO: Se på seinere...  - PendingIntent pendingIntent = PendingIntent().getActivity(context, 0, intent,0);
            // SO: http://stackoverflow.com/questions/23220757/android-widget-onclick-listener-for-several-buttons
            // Doc: http://developer.android.com/reference/android/widget/Button.html
            Log.d("widget","widget-> " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void initScan(View view){
        //Scan logic
        Log.d("test", "Clicked!");
    }
    public void initSearch(View view){
        Log.d("test", "Clicked!");
    }
}

