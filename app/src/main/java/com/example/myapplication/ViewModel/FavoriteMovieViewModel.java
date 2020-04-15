package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.Database.FavoriteMovieRepository;
import com.example.myapplication.Model.FavoriteMovie;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {
    private FavoriteMovieRepository mRepository;
    private LiveData<List<FavoriteMovie>> mAllFavs;

    public FavoriteMovieViewModel(Application application) {
        super(application);
        mRepository = new FavoriteMovieRepository(application);
        mAllFavs = mRepository.getAllFavoriteMovies();
    }

    public LiveData<List<FavoriteMovie>> getAllFavs() {
        return mAllFavs;
    }

    public void insert(FavoriteMovie favoriteMovie) {
        mRepository.insertFavoriteMovie(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie) {
        mRepository.deleteFavoriteMovie(favoriteMovie);
    }


}
