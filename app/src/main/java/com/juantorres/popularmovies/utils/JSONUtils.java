package com.juantorres.popularmovies.utils;

import android.support.annotation.NonNull;

import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.model.Review;
import com.juantorres.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by juantorres on 8/3/17.
 */

public class JSONUtils {

    private final static String EXPECTED_SITE = "YouTube";
    private final static String EXPECTED_VIDEO_TYPE   = "Trailer";

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
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH ).parse(releaseDateString);
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

    public static ArrayList<Trailer> getTrailersFromJSONString (@NonNull String jsonString) {
        ArrayList<Trailer> trailers = null;

        try {
            trailers = new ArrayList<>();
            JSONObject json = new JSONObject(jsonString);


            JSONArray trailersInJSON = json.getJSONArray("results");

            for (int i = 0; i < trailersInJSON.length(); i++) {
                JSONObject jsonTrailer = trailersInJSON.getJSONObject(i);

                if( (!jsonTrailer.get("type").equals(EXPECTED_VIDEO_TYPE) || (!jsonTrailer.get("site").equals(EXPECTED_SITE))) ){
                    continue;
                }

                Trailer newTrailer = new Trailer();

                String trailerID = jsonTrailer.getString("id");
                String name      = jsonTrailer.getString("name");
                String key       = jsonTrailer.getString("key");

                newTrailer.setTrailerID(trailerID);
                newTrailer.setName(name);
                newTrailer.setYoutubeKey(key);

                trailers.add(newTrailer);

            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return trailers;
    }

    public static ArrayList<Review> getReviewsFromJSONString (@NonNull String jsonString) {
        ArrayList<Review> reviews = null;

        try {
            reviews = new ArrayList<>();
            JSONObject json = new JSONObject(jsonString);


            JSONArray reviewsInJSON = json.getJSONArray("results");

            for (int i = 0; i < reviewsInJSON.length(); i++) {
                JSONObject jsonReview = reviewsInJSON.getJSONObject(i);

                String reviewID      = jsonReview.getString("id");
                String author        = jsonReview.getString("author");
                String content       = jsonReview.getString("content");
                String url           = jsonReview.getString("url");

                Review newReview = new Review(reviewID, author, content, url);
                reviews.add(newReview);

            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return reviews;
    }
}
