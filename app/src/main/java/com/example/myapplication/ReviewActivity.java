package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Loader;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ReviewAdapter;
import com.example.myapplication.Loaders.ReviewLoader;
import com.example.myapplication.Model.Movie;
import com.example.myapplication.Model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Review>> {

    // Movie member variables
    private static final String INTENT_MOVIE_KEY = "movieObject";

    private static final String BASE_URL = "https://api.themoviedb.org/3";

    // Review member variables

    private final static String LABEL_TEXT_REVIEW = "Review: ";
    private static final String INTENT_REVIEW_KEY = "reviewObject";
    private static final int REVIEW_LOADER_ID = 3;
    private ArrayList<Review> reviews;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent parentIntent = getIntent();
        if (parentIntent != null) {
            if (parentIntent.hasExtra(INTENT_MOVIE_KEY)) {
                getSupportActionBar().setHomeButtonEnabled(true);
                setContentView(R.layout.activity_review);
                loadReviews();

            }
        }
    }

    private void loadReviews() {
        reviews = new ArrayList<>();
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);
        mReviewAdapter = new ReviewAdapter(this, reviews);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(LayoutManager);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(REVIEW_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int i, Bundle bundle) {
        Intent intent = getIntent();
        String movieId = intent.getStringExtra(INTENT_MOVIE_KEY);
        Movie movie = getIntent().getExtras().getParcelable(INTENT_MOVIE_KEY);
        System.out.println("LOGICIEL: " + movieId);
        return new ReviewLoader(this, movieId);
//        return new MovieLoader(this, BASE_URL + MOVIE_POPULAR);

    }

    @Override
    public void onLoadFinished
            (Loader<List<Review>> loader, List<Review> reviewArticles) {
        mReviewRecyclerView.setAdapter(new ReviewAdapter(getApplicationContext(), reviewArticles));
        if (reviewArticles != null && !reviewArticles.isEmpty()) {
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {
        mReviewRecyclerView.setAdapter(null);
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

}