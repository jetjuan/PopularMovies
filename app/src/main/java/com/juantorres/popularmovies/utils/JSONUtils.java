package com.juantorres.popularmovies.utils;

import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.model.MoviePoster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juantorres on 8/3/17.
 */

public class JSONUtils {

    public static ArrayList<MoviePoster> getMoviePostersFromJSONString(String jsonString) throws JSONException{
        JSONObject json = new JSONObject(jsonString);

        ArrayList<MoviePoster> moviePosters = new ArrayList<MoviePoster>();

        JSONArray movies = json.getJSONArray("results");

        for(int i = 0; i< movies.length(); i++){
            JSONObject movie = movies.getJSONObject(i);

            int movieID = movie.getInt("id");
            String title = movie.getString("title");
            String posterPath = movie.getString("poster_path");

            moviePosters.add( new MoviePoster(movieID, title, posterPath));

        }

        return moviePosters;
    }

    public static Movie getMovieFromJSONString (String jsonString) {
        throw new UnsupportedOperationException();
    }
}
