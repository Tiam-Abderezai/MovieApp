package com.example.myapplication.Loaders;


import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.myapplication.Model.Review;
import com.example.myapplication.Utils.NetworkUtils;

import java.util.List;

public class ReviewLoader extends AsyncTaskLoader {

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
        List<Review> reviewArticles = NetworkUtils.fetchReviewData(mUrl);
        return reviewArticles;
    }
    public ReviewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }



}
