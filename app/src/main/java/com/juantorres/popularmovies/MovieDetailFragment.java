package com.juantorres.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.juantorres.popularmovies.model.Movie;
import com.juantorres.popularmovies.model.Trailer;
import com.juantorres.popularmovies.tasks.MovieDetailsLoaderTask;
import com.juantorres.popularmovies.tasks.MoviesLoaderTask;
import com.juantorres.popularmovies.utils.JSONUtils;
import com.juantorres.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

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
    private ArrayList<Trailer> mTrailers;

    @BindView(R.id.tv_movie_release_year)  public TextView   mtvReleaseYear;
//    @BindView(R.id.tv_movie_duration)      public TextView   mtvDuration;
    @BindView(R.id.tv_movie_overview)      public TextView   mtvMovieOverview;
    @BindView(R.id.tv_movie_rating)        public TextView   mtvRating;
    @BindView(R.id.iv_movie_poster_small)  public ImageView  mivSmallPoster;
    @BindView(R.id.lv_trailers)            public ListView   mlvTrailers;




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
        displayMovieData();
        //
        loadDetails(String.valueOf(mItem.getId()));
        return rootView;
    }

    private void displayMovieData(){
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
        mTrailers = JSONUtils.getTrailersFromJSONString(json);

        List<String> trailerStrings = new ArrayList<String>();
        for (Trailer currentTrailer : mTrailers) {
            trailerStrings.add(currentTrailer.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),
                R.layout.movie_trailer, R.id.tv_trailer_name,
                trailerStrings.toArray(new String[trailerStrings.size()]));
        mlvTrailers.setAdapter(adapter);

//        mlvTrailers.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent,
//                                    View view,
//                                    int position,
//                                    long id) {
//                // Get clicked project.
//                Project project = mProjects.get(position);
//                // Open the activity for the selected project.
//                Intent projectIntent = new Intent(MainActivity.this, ProjectActivity.class);
//                projectIntent.putExtra("project_id", project.getId());
//                MainActivity.this.startActivity(projectIntent);
    }

    @OnItemClick(R.id.lv_trailers)
    public void playVideo(int position){

        Trailer trailer = mTrailers.get(position);
        String youtubeURL = NetworkUtils.getYoutubeURL(trailer.getYoutubeKey());

        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeURL));
        startActivity(youtubeIntent);
    }


    public void displayReviews(){

    }

    public void showLoadingIndicator(){

    }

    public void showMovieData(){

    }

    public void showSplashScreen(){

    }
}
