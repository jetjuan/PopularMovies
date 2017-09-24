package com.juantorres.popularmovies.utils;

import android.database.Cursor;

import com.juantorres.popularmovies.db.MovieContract;
import com.juantorres.popularmovies.model.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by juantorres on 24/9/17.
 */

public class CursorUtils {
    public static List<MovieContract.Movie> getMoviesFromCursor(Cursor cursor){
        List movies = new ArrayList();

        while(cursor.moveToNext()){
            String id               = cursor.getString( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_MOVIEDB_ID));
            String title            = cursor.getString( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_TITLE));
            double popularity       = cursor.getDouble( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_POPULARITY));
            String posterPath       = cursor.getString( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_PORTER_PATH));
            String originalTitle    = cursor.getString( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_ORIGINAL_TITLE));
            String overview         = cursor.getString( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_OVERVIEW));
            String releaseDate      = cursor.getString( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_RELEASE_DATE));
            double voteAverage      = cursor.getDouble( cursor.getColumnIndex(MovieContract.Movie.COLUMN_NAME_VOTE_AVERAGE));
            boolean favorite        = true;
            Date date = null;

            DateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
            try {
                date = formatter.parse(releaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Movie movie = new Movie();
            movie.setFavorite(favorite);
            movie.setId( Integer.parseInt(id) );
            movie.setTitle( title);
            movie.setPopularity(popularity);
            movie.setPosterPath(posterPath);
            movie.setOriginalTitle(originalTitle);
            movie.setOverview(overview);
            movie.setReleaseDate(date );
            movie.setVoteAverage(voteAverage);

            movies.add(movie);
        }

        return movies;
    }
}
