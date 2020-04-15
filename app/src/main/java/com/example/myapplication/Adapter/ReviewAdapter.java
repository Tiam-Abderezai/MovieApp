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
import com.example.myapplication.Model.Review;
import com.example.myapplication.R;
import com.example.myapplication.ReviewActivity;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> mReviews;
    private static final String INTENT_REVIEW_KEY = "reviewObject";
    Context mContext;


    public ReviewAdapter(Context mContext, List<Review> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.authorView.setText("''" + review.getAuthor() + "''" + " wrote:");
        holder.contentView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        final CardView mReviewCardView;
        TextView authorView;
        TextView contentView;
        TextView reviewView;
        Context mContext;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewCardView = (CardView) itemView.findViewById(R.id.card_view_review);
            authorView = (TextView) itemView.findViewById(R.id.tv_author_review);
            contentView = (TextView) itemView.findViewById(R.id.tv_content_review);
            reviewView = (TextView) itemView.findViewById(R.id.tv_detail_review);
            mContext = itemView.getContext();
        }

    }
}