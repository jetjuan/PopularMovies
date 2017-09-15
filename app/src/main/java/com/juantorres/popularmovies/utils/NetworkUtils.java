package com.juantorres.popularmovies.utils;


import com.juantorres.popularmovies.BuildConfig;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by juantorres on 7/31/17.
 */

public class NetworkUtils {
    private final static String BASE_URL = "http://api.themoviedb.org/3";
    private final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";

    private final static String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN; //TODO: Get your own APY KEY here: https://www.themoviedb.org/faq/api


    private final static String API_KEY_PARAM = "?api_key=" + API_KEY;
    private final static String POSTER_SIZE_PARAM = "w185";

    private final static String POPULAR_MOVIES_ENDPOINT = "/movie/popular";
    private final static String TOP_RATED_ENDPOINT = "/movie/top_rated";
    private final static String TRAILERS_ENDPOINT = "/movie/%s/videos";
    private final static String REVIEWS_ENDPOINT  = "/movie/%s/reviews";

    private final static String POPULAR_MOVIES_URL = BASE_URL + POPULAR_MOVIES_ENDPOINT+ API_KEY_PARAM;
    private final static String TOP_RATED_MOVIES_URL = BASE_URL + TOP_RATED_ENDPOINT + API_KEY_PARAM;
    private final static String TRAILERS_URL = BASE_URL + TRAILERS_ENDPOINT + API_KEY_PARAM;
    private final static String REVIEWS_URL = BASE_URL + REVIEWS_ENDPOINT + API_KEY_PARAM;

    private final static OkHttpClient client = new OkHttpClient();

    private static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String getPopularMoviesJSONString() {
        try {
            return run(POPULAR_MOVIES_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTopRatedMoviesJSONString() {
        try {
            return run(TOP_RATED_MOVIES_URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTrailersJSONString(String movieID){
        String trailerURL = String.format(TRAILERS_URL, movieID);

        try {
            return run(trailerURL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPosterUrl(String posterPath){
        return POSTER_BASE_URL + POSTER_SIZE_PARAM + posterPath;
    }

    public static String getReviewsJSONString(String movieID){
        String reviewsURL = String.format(REVIEWS_URL, movieID);

        try {
            return run(reviewsURL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getYoutubeURL(String youtubeKey){
        return YOUTUBE_VIDEO_URL + youtubeKey;
    }


}

