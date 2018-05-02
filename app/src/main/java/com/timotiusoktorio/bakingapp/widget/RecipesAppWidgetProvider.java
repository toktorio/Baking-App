package com.timotiusoktorio.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.ui.recipes.RecipesActivity;

public class RecipesAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAllAppWidgets(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.layout_app_widget);
            rv.setRemoteAdapter(R.id.lv_ingredients, new Intent(context, RecipesRemoteViewsService.class));

            Intent recipesActivityIntent = new Intent(context, RecipesActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipesActivityIntent, 0);
            rv.setOnClickPendingIntent(R.id.widget_main_content, pendingIntent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_ingredients);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}