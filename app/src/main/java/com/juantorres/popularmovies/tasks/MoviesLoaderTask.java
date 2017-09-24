package com.juantorres.popularmovies.tasks;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import com.juantorres.popularmovies.MovieListActivity;
import com.juantorres.popularmovies.db.MovieContract;
import com.juantorres.popularmovies.utils.NetworkUtils;

/**
 * Created by juantorres on 8/2/17.
 */

public class MoviesLoaderTask extends AsyncTask<String, Void, String> {

    private MovieListActivity activity;
    private String LOAD_FROM_DB = "LOAD_FROM_DB";


    public MoviesLoaderTask(MovieListActivity main) {
        this.activity = main;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showLoadingIndicator();

    }

    @Override
    protected String  doInBackground(String ... param ) {
        String jsonString = null;
        String searchParam = param[0];

        if(searchParam.equals(MovieListActivity.FAVORITE_MOVIES_PARAM)){
            jsonString = LOAD_FROM_DB;

        }else if(isDeviceOnline()){
                switch (param[0]){
                    case MovieListActivity.POPULAR_MOVIES_PARAM:
                        jsonString = NetworkUtils.getPopularMoviesJSONString();
                        break;

                    case MovieListActivity.TOP_RATED_PARAM:
                        jsonString = NetworkUtils.getTopRatedMoviesJSONString();
                        break;
                }
        }




        return jsonString;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        super.onPostExecute(jsonString);

        if(jsonString == null){
            activity.showSplashScreen();
        }else if(jsonString == LOAD_FROM_DB){
            Cursor cursor = activity.getContentResolver().query(Uri.parse(MovieContract.CONTENT_URI), null, null, null, null);
            activity.setupRecyclerView(cursor);
            activity.showRecyclerView();
        }else
        {
            activity.setupRecyclerView(jsonString);
            activity.showRecyclerView();

        }

    }

    public boolean isDeviceOnline(){
        //TODO: implement method properly
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }



}

