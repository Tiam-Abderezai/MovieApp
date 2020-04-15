package com.example.myapplication.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.myapplication.Model.FavoriteMovie;

import java.util.List;

public class FavoriteMovieRepository {
    private FavoriteMovieDao mFavoriteMovieDao;
    private LiveData<List<FavoriteMovie>> mAllMovies;

    public FavoriteMovieRepository(Application application) {
        FavoriteMovieDB db = FavoriteMovieDB.getDatabase(application);
        mFavoriteMovieDao = db.favoriteMovieDao();
        mAllMovies = mFavoriteMovieDao.loadAllMovies();
    }

    public LiveData<List<FavoriteMovie>> getAllFavoriteMovies() {
        return mAllMovies;
    }

    public void insertFavoriteMovie (FavoriteMovie favMovie) {
        new insertAsyncTask(mFavoriteMovieDao).execute(favMovie);
    }

    public void deleteFavoriteMovie(FavoriteMovie favoriteMovie) {
        new deleteFavMovieAsyncTask(mFavoriteMovieDao).execute(favoriteMovie);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {

        private FavoriteMovieDao mAsyncTaskDao;

        insertAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteMovie... params) {
            mAsyncTaskDao.insertFavoriteMovie(params[0]);
            return null;
        }
    }

    private static class deleteFavMovieAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {
        private FavoriteMovieDao mAsyncTaskDao;

        deleteFavMovieAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteMovie... params) {
            mAsyncTaskDao.deleteFavoriteMovie(params[0]);
            return null;
        }
    }

}
