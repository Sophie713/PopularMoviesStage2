package com.example.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.MovieDetail;
import com.example.popularmovies.R;
import com.example.popularmovies.holder.HolderMovie;
import com.example.popularmovies.objects.MoviePosterObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.UrlBuilderTools;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMovies extends RecyclerView.Adapter<HolderMovie> {

    private final ArrayList<MoviePosterObject> movies;
    private Context context;

    public AdapterMovies(ArrayList<MoviePosterObject> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderMovie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        HolderMovie movie = new HolderMovie(view);
        return movie;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMovie holder, int position) {
        final int positionF = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GeneralUtils.isOnline(context)) {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra(Const.movieId, movies.get(positionF).getMovieId());
                    context.startActivity(intent);
                } else {
                    GeneralUtils.noConnectionToast(context);
                }
            }
        });
        Picasso.with(context).load(new UrlBuilderTools().buildUrlForPoster(movies.get(position).getImgUrl())).placeholder(R.drawable.unnamed).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }
}
