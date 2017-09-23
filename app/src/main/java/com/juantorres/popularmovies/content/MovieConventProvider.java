package com.juantorres.popularmovies.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.juantorres.popularmovies.db.MovieContract;
import com.juantorres.popularmovies.db.MovieDbHelper;

/**
 * Created by juantorres on 20/9/17.
 */

public class MovieConventProvider extends ContentProvider {

    private static final int FAVORITE_MOVIES = 1;
    private static final int MOVIE_ID = 2;



    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sURIMatcher.addURI(MovieContract.CONTENT_AUTHORITY, "movie", FAVORITE_MOVIES);
        sURIMatcher.addURI(MovieContract.CONTENT_AUTHORITY, "movie/#", MOVIE_ID);
    }

    private MovieDbHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {


        int match = sURIMatcher.match(uri);
        switch (match)
        {
            case FAVORITE_MOVIES:
                break;
            case MOVIE_ID:
                selection = MovieContract.Movie.COLUMN_NAME_MOVIEDB_ID + " = " + uri.getLastPathSegment();
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        db = mDbHelper.getReadableDatabase();

        //TODO: call query here
        return db.query(MovieContract.Movie.TABLE_NAME, null, selection, null, null, null, null );
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match)
        {
            case FAVORITE_MOVIES:
                return "vnd.android.cursor.dir/movies";
            case MOVIE_ID:
                return "vnd.android.cursor.item/movie";
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        db = mDbHelper.getWritableDatabase();

        long newRowID = db.insert(MovieContract.Movie.TABLE_NAME, null, contentValues);

        return uri.withAppendedPath(uri, String.valueOf(newRowID));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = sURIMatcher.match(uri);
        switch (match)
        {
            case MOVIE_ID:
                selection = MovieContract.Movie.COLUMN_NAME_MOVIEDB_ID + " = " + uri.getLastPathSegment();
                break;

            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        db = mDbHelper.getWritableDatabase();

        return db.delete(MovieContract.Movie.TABLE_NAME, selection, null);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException();
    }
}
