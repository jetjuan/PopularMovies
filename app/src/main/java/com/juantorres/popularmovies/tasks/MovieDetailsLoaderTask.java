package com.juantorres.popularmovies.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.juantorres.popularmovies.MovieDetailActivity;
import com.juantorres.popularmovies.MovieDetailFragment;
import com.juantorres.popularmovies.MovieListActivity;
import com.juantorres.popularmovies.utils.NetworkUtils;

/**
 * Created by juantorres on 11/9/17.
 */

public class MovieDetailsLoaderTask extends AsyncTask<String, Void, String[]> {

    private MovieDetailFragment fragment;

    public MovieDetailsLoaderTask(MovieDetailFragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.showLoadingIndicator();

    }

    @Override
    protected String[]  doInBackground(String ... param ) {
        String trailersJsonString = null;
        String reviewsJsonString  = null;
        String movieID = param[0];

        if(isDeviceOnline()){
            trailersJsonString = NetworkUtils.getTrailersJSONString(param[0]);
            reviewsJsonString  = NetworkUtils.getReviewsJSONString(param[0]);
        }

        return new String[]{trailersJsonString, reviewsJsonString};
    }

    @Override
    protected void onPostExecute(String[] jsonStrings) {
        super.onPostExecute(jsonStrings);

        if(jsonStrings != null){
            String trailers = jsonStrings[0];
            String reviews  = jsonStrings[1];


            if(trailers == null || reviews == null){
                fragment.showSplashScreen();
            }else {
                fragment.displayTrailers(trailers);
//                fragment.mtvMovieReviews.setText(reviews);

//            activity.setupRecyclerView(jsonString);
//                activity.showMovieData();

            }
        }
    }

    public boolean isDeviceOnline(){
        //TODO: implement method properly
        ConnectivityManager cm =
                (ConnectivityManager) fragment.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
