package com.example.nontonpideo.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.dicoding.nontonpideo.provider";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class FavoriteColumns implements BaseColumns{
        public static final String TABLE_FAVORITE_MOVIE = "favorite_movies";
        public static final String TABLE_FAVORITE_TV_SHOWS = "favorite_tv_shows";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String PICTURE = "picture";
        public static final String YEAR = "year";
        public static final String SCORE = "score";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .build();

//        public static final Uri FAVORITE_MOVIE_URI = new Uri.Builder().scheme(SCHEME)
//                .authority(AUTHORITY)
//                .appendPath(TABLE_FAVORITE_MOVIE)
//                .build();
//
//        public static final Uri TV_SHOW_URI = new Uri.Builder().scheme(SCHEME)
//                .authority(AUTHORITY)
//                .appendPath(TABLE_FAVORITE_TV_SHOWS)
//                .build();
    }
}
