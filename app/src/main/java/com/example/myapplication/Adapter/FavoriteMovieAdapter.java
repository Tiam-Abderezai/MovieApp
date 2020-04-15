package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.FavoriteMovieRepository;
import com.example.myapplication.DetailActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.FavoriteMovie;
import com.example.myapplication.Model.Movie;
import com.example.myapplication.R;
import com.example.myapplication.ViewModel.FavoriteMovieViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavMovieViewHolder> {

    private List<FavoriteMovie> mFavMovies;
    private String poster = "https://image.tmdb.org/t/p/w500";
    Context mContext;


    public FavoriteMovieAdapter(Context mContext, List<FavoriteMovie> mFavMovies) {
        this.mContext = mContext;
        this.mFavMovies = mFavMovies;
    }

    @NonNull
    @Override
    public FavMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie, parent, false);
        return new FavMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavMovieViewHolder holder, int position) {
        FavoriteMovie favMovie = mFavMovies.get(position);
        Picasso.get()
                .load(favMovie.buildPosterPath(holder.mContext))
                .into(holder.imageView);
        holder.titleView.setText(favMovie.getTitle());
        holder.titleView.setText(mFavMovies.get(position).getTitle());
        holder.ratingView.setText(mFavMovies.get(position).getRatingAverage());
        holder.mMovieCardView.setTag(R.id.movie_item, position);

    }

    @Override
    public int getItemCount() {
        return mFavMovies.size();
    }
    public void setMovieData(List<FavoriteMovie> movieItemList) {
        mFavMovies = movieItemList;
        notifyDataSetChanged();
    }
    public class FavMovieViewHolder extends RecyclerView.ViewHolder {
        final CardView mMovieCardView;
        TextView titleView;
        ImageView imageView;
        TextView ratingView;
        Context mContext;

        public FavMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieCardView = (CardView) itemView.findViewById(R.id.card_view_movie);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            ratingView = (TextView) itemView.findViewById(R.id.tv_rating_average);
            mContext = itemView.getContext();
        }

    }
}


