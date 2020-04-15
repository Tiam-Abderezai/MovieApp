package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailActivity;
import com.example.myapplication.Model.Trailer;
import com.example.myapplication.R;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> mTrailers;
    private static final String INTENT_TRAILER_KEY = "trailerObject";
    Context mContext;


    public TrailerAdapter(Context mContext, List<Trailer> mTrailers) {
        this.mContext = mContext;
        this.mTrailers = mTrailers;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Trailer trailer = mTrailers.get(position);
        holder.nameView.setText(trailer.getName());
//        holder.site.setText(trailer.getSite());
//        holder.key.setText(trailer.getKey());
        final String youtubeLink = "https://www.youtube.com/watch?v=" + trailer.getKey();
        holder.mTrailerCardView.setTag(R.id.trailer_item, position);
        holder.mTrailerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                int position = (int) view.getTag(R.id.trailer_item);
                intent.putExtra(INTENT_TRAILER_KEY, mTrailers.get(position));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        final CardView mTrailerCardView;
        TextView nameView;
        //        TextView siteView;
//        TextView keyView;
//        TextView ratingView;
        Context mContext;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrailerCardView = (CardView) itemView.findViewById(R.id.card_view_trailer);
            nameView = (TextView) itemView.findViewById(R.id.tv_name_trailer);
//            siteView
            mContext = itemView.getContext();
        }

    }
}