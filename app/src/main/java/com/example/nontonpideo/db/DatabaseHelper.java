package com.example.nontonpideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.PICTURE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.SCORE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_MOVIE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TABLE_FAVORITE_TV_SHOWS;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.example.nontonpideo.db.DatabaseContract.FavoriteColumns.YEAR;


class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbnontonpideo";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIES_NONTONPIDEO = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_FAVORITE_MOVIE,
            _ID,
            TITLE,
            OVERVIEW,
            PICTURE,
            SCORE,
            YEAR
    );

    private static final String SQL_CREATE_TABLE_TV_SHOWS_NONTONPIDEO = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_FAVORITE_TV_SHOWS,
            _ID,
            TITLE,
            OVERVIEW,
            PICTURE,
            SCORE,
            YEAR
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIES_NONTONPIDEO);
        db.execSQL(SQL_CREATE_TABLE_TV_SHOWS_NONTONPIDEO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_TV_SHOWS);
        onCreate(db);
    }
}
