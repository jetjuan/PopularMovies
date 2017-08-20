package com.juantorres.popularmovies;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    /**
     * The movie content this fragment is presenting.
     */
    private Movie mItem;

    @BindView(R.id.tv_movie_release_year)  public TextView   mtvReleaseYear;
//    @BindView(R.id.tv_movie_duration)      public TextView   mtvDuration;
    @BindView(R.id.tv_movie_overview)      public TextView   mtvMovieOverview;
    @BindView(R.id.tv_movie_rating)        public TextView   mtvRating;
    @BindView(R.id.iv_movie_poster_small)  public ImageView  mivSmallPoster;





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

            mItem = getArguments().getParcelable(ARG_MOVIE);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =  activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Display movie data.
        if (mItem != null) {
            String voteAverageString = String.valueOf(mItem.getVoteAverage()) + "/10";
            String releaseYear       = String.valueOf(mItem.getReleaseYear());

            String posterUrl         = NetworkUtils.getPosterUrl(mItem.getPosterPath());
            mtvReleaseYear.setText(releaseYear);
            mtvMovieOverview.setText(mItem.getOverview());
            mtvRating.setText(voteAverageString);

            Picasso.with(getContext())
                    .load(posterUrl)
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(mivSmallPoster);

        }

        return rootView;
    }
}
