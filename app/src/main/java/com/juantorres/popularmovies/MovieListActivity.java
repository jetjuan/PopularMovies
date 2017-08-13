package com.juantorres.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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


import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.utils.JSONUtils;
import com.juantorres.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public static String CURRENT_SORT_PARAM = POPULAR_MOVIES_PARAM;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

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

        loadMovies(POPULAR_MOVIES_PARAM);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
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
        List moviePosters = JSONUtils.getMoviesFromJSONString(json);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(moviePosters));

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
            int movieID = movie.getId();
            String posterURL = NetworkUtils.getPosterUrl( movie.getPosterPath());

            holder.mView.setTag(movieID);
            Picasso.with(holder.mView.getContext()).load(posterURL).into(holder.mPosterImageView);



            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(MovieDetailFragment.ARG_ITEM_ID, (int) holder.mView.getTag());
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, (int) holder.mView.getTag());

                    }
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
