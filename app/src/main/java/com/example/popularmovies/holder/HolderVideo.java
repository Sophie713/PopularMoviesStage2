package com.example.popularmovies.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;

public class HolderVideo extends RecyclerView.ViewHolder {

    public TextView videoNumber;
    public View layout;

    public HolderVideo(@NonNull View itemView) {
        super(itemView);
        videoNumber = itemView.findViewById(R.id.item_movie_trailer_text);
        layout = itemView.findViewById(R.id.item_movie_trailer_layout);
    }
}
