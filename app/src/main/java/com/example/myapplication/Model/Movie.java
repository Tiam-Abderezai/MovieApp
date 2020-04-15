package com.example.myapplication.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.R;

public class Movie implements Parcelable {

        private String id;
    private String title;
    private String releaseDate;
    private String poster;
    private String ratingAverage;
    private String plotSynopsis;

    private static final String MOVIEDB_POSTER_IMG_URL = "https://image.tmdb.org/t/p/";


    public Movie(String id, String title, String releaseDate, String poster, String ratingAverage, String plotSynopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.ratingAverage = ratingAverage;
        this.plotSynopsis = plotSynopsis;
    }

    private Movie(Parcel parcel) {
        id = parcel.readString();
        title = parcel.readString();
        releaseDate = parcel.readString();
        poster = parcel.readString();
        ratingAverage = parcel.readString();
        plotSynopsis = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(poster);
        parcel.writeString(ratingAverage);
        parcel.writeString(plotSynopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(String ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String buildPosterPath(Context context) {
        String posterWidth = context.getResources().getString(R.string.poster_size);
        return MOVIEDB_POSTER_IMG_URL + posterWidth + getPoster();
    }
}
