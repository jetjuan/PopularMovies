package com.juantorres.popularmovies.db;

import android.provider.BaseColumns;

/**
 * Created by juantorres on 20/9/17.
 */

public class MovieContract {

    private MovieContract() {}

    private static final String SCHEME = "content://";
    private static final String PATH = "/movie";

    public static final String CONTENT_AUTHORITY = "com.juantorres.popularmovies";
    public static final String BASE_CONTENT_URI = SCHEME + CONTENT_AUTHORITY;
    public static final String CONTENT_URI = BASE_CONTENT_URI + PATH;

    public static final String SQL_CREATE_MOVIES =
            "CREATE TABLE " + Movie.TABLE_NAME + " (" +
                    Movie._ID                           + " INTEGER PRIMARY KEY," +
                    Movie.COLUMN_NAME_MOVIEDB_ID        + " INTEGER," +
                    Movie.COLUMN_NAME_TITLE             + " TEXT," +
                    Movie.COLUMN_NAME_POPULARITY        + " REAL," +
                    Movie.COLUMN_NAME_PORTER_PATH       + " TEXT," +
                    Movie.COLUMN_NAME_ORIGINAL_TITLE    + " TEXT," +
                    Movie.COLUMN_NAME_OVERVIEW          + " TEXT," +
                    Movie.COLUMN_NAME_RELEASE_DATE      + " TEXT," +
                    Movie.COLUMN_NAME_VOTE_AVERAGE      + " REAL," +
                    Movie.COLUMN_NAME_IS_FAVORITE + " BOOLEAN)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Movie.TABLE_NAME;

    /* Inner class that defines the table contents */
    public static class Movie implements BaseColumns {
        public static final String TABLE_NAME                   = "movie";
        public static final String COLUMN_NAME_MOVIEDB_ID       = "moviedbID";
        public static final String COLUMN_NAME_TITLE            = "title";
        public static final String COLUMN_NAME_POPULARITY       = "popularity";
        public static final String COLUMN_NAME_PORTER_PATH      = "posterPath";
        public static final String COLUMN_NAME_ORIGINAL_TITLE   = "originalTitle";
        public static final String COLUMN_NAME_OVERVIEW         = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE     = "releaseDate";
        public static final String COLUMN_NAME_IS_FAVORITE      = "isFavorite";
        public static final String COLUMN_NAME_VOTE_AVERAGE     = "voteAverage";

    }
}


