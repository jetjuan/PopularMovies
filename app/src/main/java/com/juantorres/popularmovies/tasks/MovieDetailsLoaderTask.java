package com.juantorres.popularmovies.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.juantorres.popularmovies.MovieDetailFragment;
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
            trailersJsonString = NetworkUtils.getTrailersJSONString(movieID);
            reviewsJsonString  = NetworkUtils.getReviewsJSONString(movieID);
        }else {

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
                fragment.showRetryButton();
            }else {
                fragment.displayTrailers(trailers);
                fragment.displayReviews(reviews);

//            activity.setupRecyclerView(jsonString);
//                activity.showMovieData();

            }
        }
    }

    public boolean isDeviceOnline(){
        ConnectivityManager cm =
                (ConnectivityManager) fragment.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
}
