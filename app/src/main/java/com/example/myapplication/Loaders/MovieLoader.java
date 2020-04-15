package com.example.myapplication.Loaders;


import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.myapplication.Model.Movie;
import com.example.myapplication.Utils.NetworkUtils;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader {

    private String mUrl;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public Object loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Movie> movieArticles = NetworkUtils.fetchMovieData(mUrl);
        return movieArticles;
    }
    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }



}
