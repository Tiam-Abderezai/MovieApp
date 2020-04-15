package com.example.myapplication;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.myapplication.Adapter.ReviewAdapter;
import com.example.myapplication.Adapter.TrailerAdapter;
import com.example.myapplication.Loaders.MovieLoader;
import com.example.myapplication.Loaders.ReviewLoader;
import com.example.myapplication.Loaders.TrailerLoader;
import com.example.myapplication.Model.FavoriteMovie;
import com.example.myapplication.Model.Movie;
import com.example.myapplication.Model.Review;
import com.example.myapplication.Model.Trailer;
import com.example.myapplication.ViewModel.FavoriteMovieViewModel;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailer>> {

    // Favorite Movies member variables
    private List<FavoriteMovie> favoriteMovies;

    // Movie member variables
    private static final String INTENT_MOVIE_KEY = "movieObject";
    private final static String LABEL_TEXT_TITLE = "Title: ";
    private final static String LABEL_TEXT_VOTE_AVERAGE = "Vote Average: ";
    private final static String LABEL_TEXT_RELEASE_DATE = "Release Date: ";
    private final static String LABEL_TEXT_OVERVIEW = "Overview: ";
    private Movie movieId;
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private FavoriteMovieViewModel mViewModel;

    // Trailer member variables
    private final static String LABEL_TEXT_TRAILER = "Trailer: ";
    private static final String INTENT_TRAILER_KEY = "trailerObject";
    private static final int TRAILER_LOADER_ID = 2;
    private ArrayList<Trailer> trailers;
    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;

    // Review member variables
    private final static String LABEL_TEXT_REVIEW = "Reviews";
    private ImageView mFavButton;
    private Boolean isFav = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent parentIntent = getIntent();
        if (parentIntent != null) {
            if (parentIntent.hasExtra(INTENT_MOVIE_KEY)) {
                getSupportActionBar().setHomeButtonEnabled(true);
                setContentView(R.layout.activity_detail);
                ImageView moviePosterImageView = (ImageView) findViewById(R.id.iv_detail_poster);
                final ProgressBar moviePosterProgressBar = (ProgressBar) findViewById(R.id.pb_movie_detail_poster);
                TextView movieTitleTV = (TextView) findViewById(R.id.tv_detail_title);
                TextView movieVoteAverageTV = (TextView) findViewById(R.id.tv_detail_vote_average);
                TextView movieReleaseDateTV = (TextView) findViewById(R.id.tv_detail_release_date);
                TextView movieOverviewTV = (TextView) findViewById(R.id.tv_detail_overview);
                TextView movieTrailerTV = (TextView) findViewById(R.id.tv_detail_trailer);
                TextView movieReviewTV = (TextView) findViewById(R.id.tv_detail_review);
                final TextView moviePosterErrorTV = (TextView) findViewById(R.id.tv_detail_poster_error);
                mViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
                loadTrailers();

                Movie movie = getIntent().getExtras().getParcelable(INTENT_MOVIE_KEY);
                Context context = getApplicationContext();
                Picasso.get()
                        .load(movie.buildPosterPath(context))
                        .into(moviePosterImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                moviePosterProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                moviePosterProgressBar.setVisibility(View.GONE);
                                moviePosterErrorTV.setRotation(-20);
                                moviePosterErrorTV.setVisibility(View.VISIBLE);
                            }
                        });
                mViewModel.getAllFavs().observe(this, new Observer<List<FavoriteMovie>>() {
                    @Override
                    public void onChanged(@Nullable List<FavoriteMovie> favs) {
                        favoriteMovies = favs;
                        int i;
                        Movie movieId = getIntent().getExtras().getParcelable(INTENT_MOVIE_KEY);
                        for (i = 0; i < favoriteMovies.size(); i++) {
                            if (favoriteMovies.get(i).getId() == Integer.parseInt(movieId.getId())) {
                                mFavButton.setImageResource(R.drawable.ic_favorite_true);
                                isFav = true;
                            } else {
                                System.out.println("NOT MATCHED");
                            }
                        }
                    }
                });
                mFavButton = findViewById(R.id.iv_favButton);
                mFavButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFav == false) {
                            isFav = true;
                            setFavorite(isFav);
                        } else {
                            isFav = false;
                            setFavorite(isFav);
                        }
                    }
                });
                movieTitleTV.append(makeBold(LABEL_TEXT_TITLE));
                movieTitleTV.append(movie.getTitle());
                movieVoteAverageTV.append(makeBold(LABEL_TEXT_VOTE_AVERAGE));
                movieVoteAverageTV.append(movie.getRatingAverage());
                movieReleaseDateTV.append(makeBold(LABEL_TEXT_RELEASE_DATE));
                movieReleaseDateTV.append(movie.getReleaseDate());
                movieOverviewTV.append(makeBold(LABEL_TEXT_OVERVIEW));
                movieOverviewTV.append(movie.getPlotSynopsis());
                movieTrailerTV.append(makeBold(LABEL_TEXT_TRAILER));
                movieReviewTV.append(makeBold(LABEL_TEXT_REVIEW));

                setTitle(movie.getTitle());
            }
        }
    }


    private void loadTrailers() {
        trailers = new ArrayList<>();
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mTrailerAdapter = new TrailerAdapter(this, trailers);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(LayoutManager);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(TRAILER_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        }

    }

    @Override
    public Loader<List<Trailer>> onCreateLoader(int i, Bundle bundle) {
        Movie movie = getIntent().getExtras().getParcelable(INTENT_MOVIE_KEY);
        return new TrailerLoader(this, movie.getId());
    }

    @Override
    public void onLoadFinished
            (Loader<List<Trailer>> loader, List<Trailer> trailerArticles) {
        mTrailerRecyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailerArticles));
        if (trailerArticles != null && !trailerArticles.isEmpty()) {
            mTrailerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Trailer>> loader) {
        mTrailerRecyclerView.setAdapter(null);
    }

    private SpannableString makeBold(String string) {
        SpannableString boldText = new SpannableString(string);
        boldText.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), 0);
        return boldText;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadReviewActivity(View view) {
        Intent intent = new Intent(view.getContext(), ReviewActivity.class);
        Movie movie = getIntent().getExtras().getParcelable(INTENT_MOVIE_KEY);
        intent.putExtra(INTENT_MOVIE_KEY, movie.getId());
        view.getContext().startActivity(intent);
    }

    private void setFavorite(Boolean fav) {
        Movie movie = getIntent().getExtras().getParcelable(INTENT_MOVIE_KEY);
        int favId = Integer.parseInt(movie.getId());
        String favTitle = movie.getTitle();
        String favReleaseDate = movie.getReleaseDate();
        String favPoster = movie.getPoster();
        String favRatingAverage = movie.getRatingAverage();
        String favPlotSynopsis = movie.getPlotSynopsis();
        FavoriteMovie favMovies = new FavoriteMovie(favId, favTitle, favReleaseDate, favPoster, favRatingAverage, favPlotSynopsis);
        if (fav == true) {
            mViewModel.insert(favMovies);
            mFavButton.setImageResource(R.drawable.ic_favorite_true);
        } else {
            mViewModel.delete(favMovies);
            mFavButton.setImageResource(R.drawable.ic_favorite_false);
        }
    }

}
