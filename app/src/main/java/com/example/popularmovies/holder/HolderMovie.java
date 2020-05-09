package com.example.popularmovies.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;

public class HolderMovie extends RecyclerView.ViewHolder {

    public ImageView image;
    public CardView card;

    public HolderMovie(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.item_movie_image);
        card = itemView.findViewById(R.id.item_movie_card);
    }
}
