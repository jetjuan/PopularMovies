package com.juantorres.popularmovies.tasks;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by juantorres on 21/9/17.
 */

public class FavoriteMoviesLoader extends CursorLoader {
    private final static String TEST = "" ;


    public FavoriteMoviesLoader(Context context) {
        super(context);
    }

    public FavoriteMoviesLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }



    @Override
    public Cursor loadInBackground() {
        return super.loadInBackground();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public void deliverResult(Cursor cursor) {
        super.deliverResult(cursor);
    }
}
