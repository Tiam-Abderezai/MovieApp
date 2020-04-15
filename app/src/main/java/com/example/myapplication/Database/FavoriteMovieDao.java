package com.example.myapplication.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.Model.FavoriteMovie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM table_favorite_movies ORDER BY id")
    LiveData<List<FavoriteMovie>> loadAllMovies();

    @Insert
    void insertFavoriteMovie(FavoriteMovie favMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovie favoriteMovie);


//    @Query("SELECT * FROM table_favorite_movies WHERE id = :id")
//    FavoriteMovie loadMovieById(int id);
}
