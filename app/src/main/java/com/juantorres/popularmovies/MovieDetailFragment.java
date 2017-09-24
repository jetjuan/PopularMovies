package com.juantorres.popularmovies;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.FormatException;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juantorres.popularmovies.db.MovieContract;
import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.model.Review;
import com.juantorres.popularmovies.model.Trailer;
import com.juantorres.popularmovies.tasks.MovieDetailsLoaderTask;
import com.juantorres.popularmovies.utils.JSONUtils;
import com.juantorres.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item that this fragment
     * represents.
     */
    public static final String ARG_MOVIE = "movie";
    public static final String ARG_MOVIE_LOADED_FROM_DB = "ARG_MOVIE_LOADED_FROM_DB";

    /**
     * The movie content this fragment is presenting.
     */
    private Movie mMovie;
    private boolean loadedFromDB;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review>  mReviews;

    @BindView(R.id.tv_movie_release_year)  public TextView     mtvReleaseYear;
    @BindView(R.id.tv_movie_overview)      public TextView     mtvMovieOverview;
    @BindView(R.id.tv_movie_rating)        public TextView     mtvRating;
    @BindView(R.id.iv_movie_poster_small)  public ImageView    mivSmallPoster;
    @BindView(R.id.trailers_list)          public LinearLayout mTrailersList;
    @BindView(R.id.reviews_list)           public LinearLayout mReviewList;
    @BindView(R.id.btn_favorite)           public ImageButton  mFavoriteButton;
    @BindView(R.id.tv_reviews_label)       public TextView     mTrailersLabel;
    @BindView(R.id.tv_trailers_label)      public TextView     mReviewsLabel;
//    private ProgressBar progressBar;




    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            // Load the movie content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mMovie = DummyContent.ITEM_MAP.get(getArguments().getInt(ARG_ITEM_ID));

            mMovie = getArguments().getParcelable(ARG_MOVIE);

            if(getArguments().getBoolean(ARG_MOVIE_LOADED_FROM_DB)){
                loadedFromDB = true;
                mMovie.setFavorite(true);

            }else {
                mMovie.setFavorite(
                        existsInDB(mMovie.getIdAsString())
                );
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =  activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mMovie.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);



        // Display movie data.
        displayMovieData();
        //Loading trailers and reviews...
        if(loadedFromDB){
            hideDetails();
        }else{
            loadDetails(mMovie.getIdAsString());
        }
        return rootView;
    }

    private void displayMovieData(){
        if (mMovie != null) {
            String voteAverageString = String.valueOf(mMovie.getVoteAverage()) + "/10";
            String releaseYear       = String.valueOf(mMovie.getReleaseYear());

            String posterUrl         = NetworkUtils.getPosterUrl(mMovie.getPosterPath());
            mtvReleaseYear.setText(releaseYear);
            mtvMovieOverview.setText(mMovie.getOverview());
            mtvRating.setText(voteAverageString);

            Picasso.with(getContext())
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(mivSmallPoster);

        }

        showFavoriteButtonPressed(mMovie.isFavorite());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void loadDetails(String movieID){
        MovieDetailsLoaderTask loader = new MovieDetailsLoaderTask(this);

        try {
            loader.execute( new String[] {movieID});

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void displayTrailers(String json){
        mTrailersList.removeAllViews();
        mTrailers = JSONUtils.getTrailersFromJSONString(json);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());


        for (final Trailer currentTrailer : mTrailers) {
            View inflatedLayout= inflater.inflate(R.layout.movie_trailer, null, false);
            inflatedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String youtubeURL = NetworkUtils.getYoutubeURL(currentTrailer.getYoutubeKey());

                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL));
                    startActivity(youtubeIntent);
                }
            });

            TextView trailerName = inflatedLayout.findViewById(R.id.tv_trailer_name);
            trailerName.setText(currentTrailer.getName());

            mTrailersList.addView(inflatedLayout);

        }
    }



    public void displayReviews(String json){
        mReviewList.removeAllViews();
        mReviews = JSONUtils.getReviewsFromJSONString(json);
        LayoutInflater inflater = LayoutInflater.from(this.getContext());


        for(final Review currentReview : mReviews) {
            View inflatedLayout     = inflater.inflate(R.layout.movie_review, null, false);
            TextView author         = inflatedLayout.findViewById(R.id.tv_review_author);
            TextView content        = inflatedLayout.findViewById(R.id.tv_review_content);
            Button readMoreButton   = inflatedLayout.findViewById(R.id.btn_read_more);

            author.setText(currentReview.getAuthor());
            content.setText(currentReview.getContent());

            readMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = currentReview.getUrl();

                    Intent openReviewURLIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(openReviewURLIntent);
                }
            });

            mReviewList.addView(inflatedLayout);

        }
    }

    public void showLoadingIndicator(){
//        progressBar = new ProgressBar(this.getContext(), null, android.R.attr.progressBarStyleSmall);
    }

    public void showMovieData(){

    }

    public void showSplashScreen(){

    }

    @OnClick(R.id.btn_favorite)
    public void favoriteButtonClicked(View v){

        String movieID = mMovie.getIdAsString();

        if(mMovie.isFavorite()){
            deleteMovie(movieID);
            mMovie.setFavorite(false);
        }else{
            saveMovie(movieID);
            mMovie.setFavorite(true);
        }

        showFavoriteButtonPressed(mMovie.isFavorite());

    }

    private void showFavoriteButtonPressed(boolean pressed){
        if(pressed){
            mFavoriteButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_star_yellow_24dp));
        }else{
            mFavoriteButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_star_border_yellow_24dp));
        }
    }

    private boolean existsInDB(String ID){
        ContentResolver resolver = getContext().getContentResolver();
        Cursor result = resolver.query( Uri.parse(MovieContract.CONTENT_URI + "/"+ID), null, null, null, null);

        if(result == null){
            return false;
        }else {
            return result.getCount() > 0;
        }

    }

    private long deleteMovie(String ID){
        ContentResolver resolver = getContext().getContentResolver();
        return resolver.delete(Uri.parse(MovieContract.CONTENT_URI + "/" + ID), null, null);
    }

    private Uri saveMovie(String ID){
        ContentResolver resolver = getContext().getContentResolver();
        DateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
        Date date = mMovie.getReleaseDate();
        String dateString;

        dateString = formatter.format(date);

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.Movie.COLUMN_NAME_TITLE, mMovie.getTitle());
        cv.put(MovieContract.Movie.COLUMN_NAME_IS_FAVORITE, mMovie.isFavorite());
        cv.put(MovieContract.Movie.COLUMN_NAME_MOVIEDB_ID, ID);
        cv.put(MovieContract.Movie.COLUMN_NAME_ORIGINAL_TITLE, mMovie.getOriginalTitle());
        cv.put(MovieContract.Movie.COLUMN_NAME_OVERVIEW, mMovie.getOverview());
        cv.put(MovieContract.Movie.COLUMN_NAME_POPULARITY, mMovie.getPopularity());
        cv.put(MovieContract.Movie.COLUMN_NAME_PORTER_PATH, mMovie.getPosterPath());
        cv.put(MovieContract.Movie.COLUMN_NAME_RELEASE_DATE, dateString);
        cv.put(MovieContract.Movie.COLUMN_NAME_VOTE_AVERAGE, mMovie.getVoteAverage());

        return resolver.insert( Uri.parse(MovieContract.CONTENT_URI), cv);
    }

    private void hideDetails(){
        mTrailersLabel.setVisibility(View.GONE);
        mTrailersList.setVisibility(View.GONE);
        mReviewsLabel.setVisibility(View.GONE);
        mReviewList.setVisibility(View.GONE);

    }
}
