package com.juantorres.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.juantorres.popularmovies.db.MovieContract;
import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.tasks.MoviesLoaderTask;
import com.juantorres.popularmovies.utils.JSONUtils;
import com.juantorres.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    public static final String POPULAR_MOVIES_PARAM = "POPULAR_MOVIES";
    public static final String TOP_RATED_PARAM = "TOP_RATED";
    public static final String FAVORITE_MOVIES_PARAM = "FAVORITE_MOVIES";
    public static String CURRENT_SORT_PARAM = POPULAR_MOVIES_PARAM;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
//    private boolean mTwoPane;

    @BindView(R.id.loading_indicator)
    public ProgressBar mLoadingIndicator;
    @BindView(R.id.movie_list)
    public RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.splash_screen)
    public View mSplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        loadMovies(CURRENT_SORT_PARAM);

//        if (findViewById(R.id.movie_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_most_popular:
                CURRENT_SORT_PARAM = POPULAR_MOVIES_PARAM;
                loadMovies(CURRENT_SORT_PARAM);
                return true;

            case R.id.action_sort_top_rated:
                CURRENT_SORT_PARAM = TOP_RATED_PARAM;
                loadMovies(CURRENT_SORT_PARAM);
                return true;

            case R.id.action_sort_top_favorites:
                CURRENT_SORT_PARAM = FAVORITE_MOVIES_PARAM;
                loadMovies(FAVORITE_MOVIES_PARAM);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @OnClick(R.id.try_again_button)
    public void tryAgain(View v){
        //TODO find a way to load the previously selected
        loadMovies(CURRENT_SORT_PARAM);
    }

    public void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSplashScreen.setVisibility(View.INVISIBLE);

    }

    public void showRecyclerView(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mSplashScreen.setVisibility(View.INVISIBLE);

    }

    public void showSplashScreen(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSplashScreen.setVisibility(View.VISIBLE);
    }

    public void setupRecyclerView(@NonNull String json) {
        List movies = JSONUtils.getMoviesFromJSONString(json);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(movies));

    }

    public void setupRecyclerView(@NonNull Cursor cursor) {
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

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(movies));

    }

    public void loadMovies(String movieParam){
        MoviesLoaderTask loader = new MoviesLoaderTask(this);

        try {
            loader.execute( new String[] {movieParam});

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Movie> mMovies;

        public SimpleItemRecyclerViewAdapter(List<Movie> items) {
            mMovies = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Movie movie = mMovies.get(position);
//            int movieID = movie.getId();
            String posterURL = NetworkUtils.getPosterUrl( movie.getPosterPath());

            holder.mView.setTag(movie);
            Picasso.with(holder.mView.getContext()).load(posterURL).into(holder.mPosterImageView);



            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mTwoPane) {
//                        Bundle arguments = new Bundle();
//                        arguments.putParcelable(MovieDetailFragment.ARG_MOVIE, (Movie) holder.mView.getTag());
//                        MovieDetailFragment fragment = new MovieDetailFragment();
//                        fragment.setArguments(arguments);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.movie_detail_container, fragment)
//                                .commit();
//                    } else {
                    Context context = v.getContext();
                    Movie selectedMovie = (Movie) holder.mView.getTag();
                    Intent intent = new Intent(context, MovieDetailActivity.class);

                    intent.putExtra(MovieDetailFragment.ARG_MOVIE, selectedMovie);
                    intent.putExtra(MovieDetailFragment.ARG_MOVIE_LOADED_FROM_DB, selectedMovie.isFavorite());

                    startActivity(intent);
                    // }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mPosterImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPosterImageView = view.findViewById(R.id.iv_movie_poster);

            }

//            @Override
//            public String toString() {
//                return super.toString() + " '" + mContentView.getText() + "'";
//            }
        }

    }
}
