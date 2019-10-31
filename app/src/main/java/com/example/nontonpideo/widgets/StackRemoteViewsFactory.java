package com.example.nontonpideo.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nontonpideo.R;
import com.example.nontonpideo.db.VideoHelper;
import com.example.nontonpideo.entity.Video;
import com.example.nontonpideo.util.ParcelableUtil;

import java.util.ArrayList;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final ArrayList<Video> videos = new ArrayList<>();
    private final Context context;

    StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        insertFavoriteMoviesAndTVShowsToList();
    }

    private void insertFavoriteMoviesAndTVShowsToList() {
        ArrayList<Video> favoriteMovies, favoriteTVShows;
        VideoHelper helper = VideoHelper.getInstance(context);
        helper.open();

        final long token = Binder.clearCallingIdentity();
        try {
            favoriteMovies = helper.getFavoriteMoviesData();
            favoriteTVShows = helper.getFavoriteTVShowsData();
        } finally {
            Binder.restoreCallingIdentity(token);
        }

        videos.addAll(favoriteMovies);
        videos.addAll(favoriteTVShows);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Bitmap bitmap;

        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(videos.get(position).getPicture())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();

            rv.setImageViewBitmap(R.id.imageView, bitmap);
        } catch (Exception e) {
        }

        Bundle extras = new Bundle();
        byte[] convertedVideo = ParcelableUtil.toByteArray(videos.get(position));
        extras.putByteArray(FavoritesWidgetProvider.EXTRA_ITEM, convertedVideo);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
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
