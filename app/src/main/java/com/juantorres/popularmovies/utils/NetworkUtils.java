package com.juantorres.popularmovies.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by juantorres on 7/31/17.
 */

public class NetworkUtils {
    final static String BASE_URL = "http://api.themoviedb.org/3";

    final static String API_KEY = ""; //TODO: Get your own APY KEY here: https://www.themoviedb.org/faq/api
    final static String API_KEY_PARAM = "?api_key=" + API_KEY;

    final static String POPULAR_MOVIES_ENDPOINT = "/movie/popular";
    final static String TOP_RATED_ENDPOINT = "/movie/top_rated";

    final static String POPULAR_MOVIES_URL = BASE_URL + POPULAR_MOVIES_ENDPOINT+ API_KEY_PARAM;
    final static String TOP_RATED_MOVIES_URL = BASE_URL + TOP_RATED_ENDPOINT + API_KEY_PARAM;

    final static OkHttpClient client = new OkHttpClient();

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

}

