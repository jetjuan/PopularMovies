package com.juantorres.popularmovies;

import android.os.AsyncTask;
import android.widget.TextView;

import com.juantorres.popularmovies.utils.NetworkUtils;

/**
 * Created by juantorres on 8/2/17.
 */

public class MoviesLoaderTask extends AsyncTask<String, String, String> {

    TextView textView;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String  doInBackground(String ... param ) {
//        switch (param[0]){
//            case "MOVIE_POSTERS":
//
//                break;
//
//            case "MOVIE_BY_ID":
//
//                break;
//
//            default:
//                return null;
//        }


//TODO complete the above

        return NetworkUtils.getPopularMoviesJSONString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }



}
