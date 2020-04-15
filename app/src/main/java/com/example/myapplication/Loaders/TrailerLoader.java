package com.example.myapplication.Loaders;


import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.myapplication.Model.Trailer;
import com.example.myapplication.Utils.NetworkUtils;

import java.util.List;

public class TrailerLoader extends AsyncTaskLoader {

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
        List<Trailer> trailerArticles = NetworkUtils.fetchTrailerData(mUrl);
        return trailerArticles;
    }
    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }



}
