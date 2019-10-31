package com.example.nontonpideo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.nontonpideo.db.VideoHelper;

import java.util.Objects;

import static com.example.nontonpideo.db.DatabaseContract.AUTHORITY;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_MOVIE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_TV_SHOWS;

public class VideoProvider extends ContentProvider {

    private static final int MOVIE_URI_ID = 1;
    private static final int TV_SHOW_URI_ID = 2;
    private static final int MOVIE_URI = 3;
    private static final int TV_SHOW_URI = 4;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private VideoHelper videoHelper;

    static {
        // content://com.dicoding.nontonpideo/favorite_movies
        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE, MOVIE_URI);
        // content://com.dicoding.nontonpideo/favorite_tv_shows
        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_TV_SHOWS, TV_SHOW_URI);
        // content://com.dicoding.nontonpideo/favorite_movies/1
        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE + "/#", MOVIE_URI_ID);
        // content://com.dicoding.nontonpideo/favorite_tv_shows/1
        uriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_TV_SHOWS + "/#", TV_SHOW_URI_ID);
    }

    @Override
    public boolean onCreate() {
        videoHelper = VideoHelper.getInstance(getContext());
        return true;
    }

    @androidx.annotation.Nullable
    @Override
    public Cursor query(@androidx.annotation.NonNull Uri uri,
                        @androidx.annotation.Nullable String[] projection,
                        @androidx.annotation.Nullable String selection,
                        @androidx.annotation.Nullable String[] selectionArgs,
                        @androidx.annotation.Nullable String sortOrder) {
        videoHelper.open();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case MOVIE_URI:
                cursor = videoHelper.queryMovieProvider();
                break;
            case TV_SHOW_URI:
                cursor = videoHelper.queryTVShowProvider();
                break;
            case MOVIE_URI_ID:
                cursor = videoHelper.queryByIdMovieProvider(uri.getLastPathSegment());
                break;
            case TV_SHOW_URI_ID:
                cursor = videoHelper.queryByIdTVShowProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @androidx.annotation.Nullable
    @Override
    public String getType(@androidx.annotation.NonNull Uri uri) {
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    public Uri insert(@androidx.annotation.NonNull Uri uri,
                      @androidx.annotation.Nullable ContentValues values) {
        videoHelper.open();
        long added;
        switch (uriMatcher.match(uri)) {
            case MOVIE_URI:
                added = videoHelper.insertMovieProvider(values);
                break;
            case TV_SHOW_URI:
                added = videoHelper.insertTVShowProvider(values);
                break;
            default:
                added = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI,null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@androidx.annotation.NonNull Uri uri,
                      @androidx.annotation.Nullable String selection,
                      @androidx.annotation.Nullable String[] selectionArgs) {
        videoHelper.open();
        int deleted;
        switch (uriMatcher.match(uri)) {
            case MOVIE_URI_ID:
                deleted = videoHelper.deleteMovieProvider(uri.getLastPathSegment());
                break;
            case TV_SHOW_URI_ID:
                deleted = videoHelper.deleteTVShowProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI,null);
        return deleted;
    }

    @Override
    public int update(@androidx.annotation.NonNull Uri uri,
                      @androidx.annotation.Nullable ContentValues values,
                      @androidx.annotation.Nullable String selection,
                      @androidx.annotation.Nullable String[] selectionArgs) {
        videoHelper.open();
        int updated;
        switch (uriMatcher.match(uri)) {
            case MOVIE_URI_ID:
                updated = videoHelper.updateMovieProvider(uri.getLastPathSegment(), values);
                break;
            case TV_SHOW_URI_ID:
                updated = videoHelper.updateTVShowProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI,null);
        return updated;
    }
}
