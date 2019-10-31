package com.example.nontonpideo.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.nontonpideo.entity.Video;

import java.util.ArrayList;
import java.util.Objects;

import static android.provider.BaseColumns._ID;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.PICTURE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.SCORE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_MOVIE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_TV_SHOWS;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.YEAR;


public class VideoHelper {
    private static final String DATABASE_MOVIES_TABLE = TABLE_FAVORITE_MOVIE;
    private static final String DATABASE_TV_SHOWS_TABLE = TABLE_FAVORITE_TV_SHOWS;
    private static DatabaseHelper databaseHelper;
    @SuppressLint("StaticFieldLeak")
    private static VideoHelper INSTANCE;
    private static SQLiteDatabase database;
    private final Context context;

    private VideoHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public static VideoHelper getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VideoHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        if(databaseHelper != null)
            databaseHelper.close();

        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public ArrayList<Video> getFavoriteMoviesData() {
        ArrayList<Video> videos = new ArrayList<>();
        Video video;
        Cursor cursor = getMoviesViewCursor();
        Objects.requireNonNull(cursor).moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                video = getMovieFromCursor(cursor);
                videos.add(video);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return videos;
    }

    public ArrayList<Video> getFavoriteTVShowsData() {
        ArrayList<Video> videos = new ArrayList<>();
        Video video;

        Cursor cursor = getTVShowsViewCursor();
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                video = getTVShowFromCursor(cursor);
                videos.add(video);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return videos;
    }

    private Cursor getMoviesViewCursor() {
        try{
            return context.getContentResolver().query(Uri.parse(CONTENT_URI + "/" + TABLE_FAVORITE_MOVIE),
                    null, null, null, null);
        }catch(Exception e){
            return null;
        }
    }

    private Cursor getTVShowsViewCursor() {
        return context.getContentResolver().query(Uri.parse(CONTENT_URI + "/" + TABLE_FAVORITE_TV_SHOWS),
                null, null, null, null);
    }

    public boolean getMovieFavoritedStatus(int id) {
        Video video;
        Cursor cursor = getMoviesSearchCursor(id);

        cursor.moveToFirst();
        video = getMovieFromCursor(cursor);
        cursor.close();

        return video != null;
    }

    public boolean getTVShowFavoritedStatus(int id) {
        Video video;
        Cursor cursor = getTVShowsSearchCursor(id);

        cursor.moveToFirst();
        video = getMovieFromCursor(cursor);
        cursor.close();

        return video != null;
    }

    private Video getMovieFromCursor(Cursor cursor) {
        Video video = null;

        if(cursor.getCount() > 0){
            video = new Video(
                    Video.MOVIE,
                    cursor.getInt(cursor.getColumnIndexOrThrow(_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PICTURE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(YEAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SCORE))
            );
        }
        return video;
    }

    private Video getTVShowFromCursor(Cursor cursor) {
        Video video = null;

        if(cursor.getCount() > 0){
            video = new Video(
                    Video.TV_SHOW,
                    cursor.getInt(cursor.getColumnIndexOrThrow(_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PICTURE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(YEAR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SCORE))
            );
        }
        return video;
    }

    private Cursor getMoviesSearchCursor(int id) {
        return context.getContentResolver().query(Uri.parse(CONTENT_URI + "/"
                        + TABLE_FAVORITE_MOVIE + "/" + id),null, null, null,
                null);
    }

    private Cursor getTVShowsSearchCursor(int id) {
        return context.getContentResolver().query(Uri.parse(CONTENT_URI + "/"
                + TABLE_FAVORITE_TV_SHOWS + "/" + id),null, null, null,
                null);
    }

    public void insertMovie(Video video) {
        context.getContentResolver().insert(Uri.parse(CONTENT_URI + "/"
                + TABLE_FAVORITE_MOVIE), getContentFromVideo(video));
    }

    public void insertTVShow(Video video) {
        context.getContentResolver().insert(Uri.parse(CONTENT_URI + "/"
                + TABLE_FAVORITE_TV_SHOWS), getContentFromVideo(video));
    }

    private ContentValues getContentFromVideo(Video video) {
        ContentValues args = new ContentValues();
        args.put(_ID, video.getId());
        args.put(TITLE, video.getTitle());
        args.put(PICTURE, video.getPicture());
        args.put(YEAR, video.getYear());
        args.put(OVERVIEW, video.getOverview());
        args.put(SCORE, video.getScore());
        return args;
    }

    public void deleteMovie(int id) {
        context.getContentResolver().delete(Uri.parse(CONTENT_URI + "/"
                + TABLE_FAVORITE_MOVIE + "/" + id), _ID + " = ?", new String[]{
                String.valueOf(id)});
    }

    public void deleteTVShow(int id) {
        context.getContentResolver().delete(Uri.parse(CONTENT_URI + "/"
                + TABLE_FAVORITE_TV_SHOWS + "/" + id), _ID + " = ?", new String[]{
                String.valueOf(id)});
    }

    public Cursor queryByIdMovieProvider(String id){
        return database.query(DATABASE_MOVIES_TABLE, null, _ID + " = ?",
                new String[]{id}, null, null, null, null);
    }

    public Cursor queryByIdTVShowProvider(String id){
        return database.query(DATABASE_TV_SHOWS_TABLE, null, _ID + " = ?",
                new String[]{id}, null, null, null, null);
    }

    public Cursor queryMovieProvider(){
        return database.query(DATABASE_MOVIES_TABLE, null, null, null,
                null, null, _ID + " ASC");
    }

    public Cursor queryTVShowProvider(){
        return database.query(DATABASE_TV_SHOWS_TABLE, null, null, null,
                null, null, _ID + " ASC");
    }

    public long insertMovieProvider(ContentValues values) {
        return database.insert(DATABASE_MOVIES_TABLE, null, values);
    }

    public long insertTVShowProvider(ContentValues values) {
        return database.insert(DATABASE_TV_SHOWS_TABLE, null, values);
    }

    public int updateMovieProvider(String id, ContentValues values) {
        return database.update(DATABASE_MOVIES_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int updateTVShowProvider(String id, ContentValues values) {
        return database.update(DATABASE_TV_SHOWS_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteMovieProvider(String id) {
        return database.delete(DATABASE_MOVIES_TABLE, _ID + " = ?", new String[]{id});
    }

    public int deleteTVShowProvider(String id) {
        return database.delete(DATABASE_TV_SHOWS_TABLE, _ID + " = ?", new String[]{id});
    }
}
