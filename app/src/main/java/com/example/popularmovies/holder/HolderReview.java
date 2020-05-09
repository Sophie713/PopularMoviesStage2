package com.example.popularmovies.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;

public class HolderReview extends RecyclerView.ViewHolder {

    public TextView author;
    public TextView review;
    public View layout;


    public HolderReview(@NonNull View itemView) {
        super(itemView);
        layout = itemView.findViewById(R.id.item_movie_review_layout);
        author = itemView.findViewById(R.id.item_movie_review_author);
        review = itemView.findViewById(R.id.item_movie_review_review);
    }
}
