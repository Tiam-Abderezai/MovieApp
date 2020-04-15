package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.myapplication.Adapter.FavoriteMovieAdapter;
import com.example.myapplication.Adapter.MovieAdapter;
import com.example.myapplication.Loaders.TrailerLoader;
import com.example.myapplication.Model.FavoriteMovie;
import com.example.myapplication.Model.Movie;
import com.example.myapplication.Loaders.MovieLoader;
import com.example.myapplication.ViewModel.FavoriteMovieViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private FavoriteMovieAdapter mFavoriteAdapter;
    private List<Movie> mMovies;
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE_POPULAR = "/movie/popular";
    private static final String MOVIE_TOP_RATED = "/movie/top_rated";
    private static final String INTENT_MOVIE_KEY = "movieObject";
    private boolean isPopular = true;
    private boolean isFavorite = false;
    private TextView mNoConnectionTextView;
    private static final int MOVIE_LOADER_ID = 1;
    private FavoriteMovieViewModel mFavViewModel;
    private List<FavoriteMovie> mFavMovies;
    private ArrayList<FavoriteMovie> favMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        mMovies = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mMovieAdapter = new MovieAdapter(this, mMovies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        mNoConnectionTextView = findViewById(R.id.empty_view);
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mNoConnectionTextView.setText(R.string.internet_unavailable);
        }
    }

    private void loadFavorites() {
        mFavMovies = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mFavoriteAdapter = new FavoriteMovieAdapter(this, mFavMovies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mFavoriteAdapter);
        mFavoriteAdapter.notifyDataSetChanged();
        mFavViewModel = ViewModelProviders.of(this).get(FavoriteMovieViewModel.class);
        mFavViewModel.getAllFavs().observe(this, new Observer<List<FavoriteMovie>>() {
                    @Override
                    public void onChanged(@Nullable List<FavoriteMovie> favs) {
                        mFavMovies = favs;
                        favMovieList = new ArrayList<FavoriteMovie>();
                        for (int i = 0; i < mFavMovies.size(); i++) {
                            FavoriteMovie mov = new FavoriteMovie(
                                    favs.get(i).getId(),
                                    favs.get(i).getTitle(),
                                    favs.get(i).getReleaseDate(),
                                    favs.get(i).getPoster(),
                                    favs.get(i).getRatingAverage(),
                                    favs.get(i).getPlotSynopsis()
                            );
                            favMovieList.add(mov);
                        }
                        mFavoriteAdapter.setMovieData(favMovieList);
                        mFavoriteAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        if (isPopular) {
            return new MovieLoader(this, BASE_URL + MOVIE_POPULAR);
        } else return new MovieLoader(this, BASE_URL + MOVIE_TOP_RATED);
    }

    @Override
    public void onLoadFinished
            (Loader<List<Movie>> loader, List<Movie> movieArticles) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mNoConnectionTextView.setText(R.string.no_movies);
        mRecyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movieArticles));
        if (movieArticles != null && !movieArticles.isEmpty()) {
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();
        if (selectedMenuItem == R.id.action_sortMP) {
            isPopular = true;
            getLoaderManager().restartLoader(0, null, this);
        } else if (selectedMenuItem == R.id.action_sortTR) {
            isPopular = false;
            getLoaderManager().restartLoader(0, null, this);
        } else if (selectedMenuItem == R.id.action_sortFV) {
            loadFavorites();
        }
        return super.onOptionsItemSelected(item);
    }

}