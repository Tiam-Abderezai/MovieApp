package com.example.myapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Model.FavoriteMovie;


@Database(entities = {FavoriteMovie.class}, version = 1, exportSchema = false)
public abstract class FavoriteMovieDB extends RoomDatabase {

    public abstract FavoriteMovieDao favoriteMovieDao();
    private static FavoriteMovieDB INSTANCE;

    static FavoriteMovieDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteMovieDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteMovieDB.class, "database_favorite_movies")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
