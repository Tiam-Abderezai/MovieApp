package com.example.myapplication.Model;


import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.myapplication.R;

@Entity(tableName = "table_favorite_movies")
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String releaseDate;
    private String poster;
    private String ratingAverage;
    private String plotSynopsis;
    private static final String MOVIEDB_POSTER_IMG_URL = "https://image.tmdb.org/t/p/";


    public FavoriteMovie(int id, String title, String releaseDate, String poster, String ratingAverage, String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.ratingAverage = ratingAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(String ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
    public String buildPosterPath(Context context) {
        String posterWidth = context.getResources().getString(R.string.poster_size);
        return MOVIEDB_POSTER_IMG_URL + posterWidth + getPoster();
    }

}