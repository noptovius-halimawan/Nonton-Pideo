package com.example.nontonpideo.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.nontonpideo.R;
import com.example.nontonpideo.activity.MovieDetailActivity;
import com.example.nontonpideo.adapter.VideoAdapter;

public class FavoritesWidgetProvider extends AppWidgetProvider {
    private static final String TOAST_ACTION = "com.dicoding.nontonpideo.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.dicoding.nontonpideo.EXTRA_ITEM";

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorites_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoritesWidgetProvider.class);
        toastIntent.setAction(FavoritesWidgetProvider.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0,
                toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

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
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                byte[] videoByteArray = intent.getByteArrayExtra(EXTRA_ITEM);

                Intent newIntent = new Intent(context, MovieDetailActivity.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra(VideoAdapter.EXTRA_VIDEO_BYTE_ARRAY, videoByteArray);
                context.startActivity(newIntent);
            }else if(intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){

            }
        }
    }
}

