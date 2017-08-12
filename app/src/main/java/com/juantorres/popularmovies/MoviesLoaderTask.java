package com.juantorres.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.TextView;

import com.juantorres.popularmovies.utils.NetworkUtils;

/**
 * Created by juantorres on 8/2/17.
 */

public class MoviesLoaderTask extends AsyncTask<String, Void, String> {

    private MovieListActivity activity;


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

        if(isDeviceOnline()){
            switch (param[0]){
                case MovieListActivity.POPULAR_MOVIES_PARAM:
                    jsonString = NetworkUtils.getPopularMoviesJSONString();
                    break;

                case MovieListActivity.TOP_RATED_PARAM:
                    jsonString = NetworkUtils.getTopRatedMoviesJSONString();
                    break;
            }
        }else{
            //TODO Add code to display error message when device offline
        }

        //TODO Fix code below

        return jsonString;
    }

    @Override
    protected void onPostExecute(String jsonString) {
        super.onPostExecute(jsonString);

        if(jsonString == null){
            activity.showSplashScreen();
        }else {
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

