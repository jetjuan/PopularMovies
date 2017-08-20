package com.juantorres.popularmovies.utils;

import android.support.annotation.NonNull;

import com.juantorres.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by juantorres on 8/3/17.
 */

public class JSONUtils {

    public static ArrayList<Movie> getMoviesFromJSONString (@NonNull String jsonString) {
        ArrayList<Movie> movies = null;

        try {
            movies = new ArrayList<>();
            JSONObject json = new JSONObject(jsonString);


            JSONArray moviesInJSON = json.getJSONArray("results");

            for (int i = 0; i < moviesInJSON.length(); i++) {
                JSONObject jsonMovie = moviesInJSON.getJSONObject(i);
                Movie newMovie = new Movie();

                int movieID = jsonMovie.getInt("id");
                String title = jsonMovie.getString("title");
                String posterPath = jsonMovie.getString("poster_path");
                int voteCount = jsonMovie.getInt("vote_count");
                boolean hasVideo = jsonMovie.getBoolean("video");
                double voteAverage  = jsonMovie.getDouble("vote_average");
                double popularity = jsonMovie.getDouble("popularity");
                String originalLanguage = jsonMovie.getString("original_language");
                String originalTitle = jsonMovie.getString("original_title");
                String backdropPath = jsonMovie.getString("backdrop_path");
                String releaseDateString = jsonMovie.getString("release_date");
                Date releaseDate = new SimpleDateFormat("YYYY-MM-DD" ).parse(releaseDateString);
                String overview = jsonMovie.getString("overview");

                newMovie.setId(movieID);
                newMovie.setTitle(title);
                newMovie.setPosterPath(posterPath);
                newMovie.setVoteCount(voteCount);
                newMovie.setHasVideo(hasVideo);
                newMovie.setVoteAverage(voteAverage);
                newMovie.setPopularity(popularity);
                newMovie.setOriginalLanguage(originalLanguage);
                newMovie.setOriginalTitle(originalTitle);
                newMovie.setBackDropPath(backdropPath);
                newMovie.setReleaseDate(releaseDate);
                newMovie.setOverview(overview);


                movies.add(newMovie);

            }

        }catch (JSONException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
