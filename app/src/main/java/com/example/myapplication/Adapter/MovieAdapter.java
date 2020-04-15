package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.DetailActivity;
import com.example.myapplication.Model.Movie;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private String poster = "https://image.tmdb.org/t/p/w500";
    private static final String INTENT_MOVIE_KEY = "movieObject";
    Context mContext;

    public MovieAdapter(Context mContext, List<Movie> mMovies) {
        this.mContext = mContext;
        this.mMovies = mMovies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Picasso.get()
                .load(movie.buildPosterPath(holder.mContext))
                .into(holder.imageView);
        holder.titleView.setText(movie.getTitle());
        holder.titleView.setText(mMovies.get(position).getTitle());
        holder.ratingView.setText(mMovies.get(position).getRatingAverage());
        holder.mMovieCardView.setTag(R.id.movie_item, position);
        holder.mMovieCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                int position = (int) view.getTag(R.id.movie_item);
                intent.putExtra(INTENT_MOVIE_KEY, mMovies.get(position));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        final CardView mMovieCardView;
        TextView titleView;
        ImageView imageView;
        TextView ratingView;
        Context mContext;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieCardView = (CardView) itemView.findViewById(R.id.card_view_movie);
            titleView = (TextView) itemView.findViewById(R.id.tv_title);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
            ratingView = (TextView) itemView.findViewById(R.id.tv_rating_average);
            mContext = itemView.getContext();
        }

    }
}


