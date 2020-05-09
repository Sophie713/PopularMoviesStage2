package com.example.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.MovieDetailActivity;
import com.example.popularmovies.R;
import com.example.popularmovies.holder.HolderMovie;
import com.example.popularmovies.objects.MoviePosterObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.UrlBuilderTools;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMovies extends RecyclerView.Adapter<HolderMovie> {

    private ArrayList<MoviePosterObject> movies = new ArrayList<>();
    private Context context;

    public AdapterMovies(ArrayList<MoviePosterObject> movies, Context context) {
        this.movies.clear();
        this.movies.addAll(movies);
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
                //check I am online to download data
                if (GeneralUtils.isOnline(context)||Const.currentSort.equals(Const.FAVORITE)) {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    //pass movie id as an identifier so that I can download the movie details
                    intent.putExtra(Const.movieId, movies.get(positionF).getMovieId());
                    context.startActivity(intent);
                } else {
                    GeneralUtils.noConnectionToast(context);
                }
            }
        });
        //display movie poster or placeholder
        Picasso.with(context).load(new UrlBuilderTools().buildUrlForPoster(movies.get(position).getImgUrl())).placeholder(R.drawable.unnamed).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void insertNewData(ArrayList<MoviePosterObject> newMovies){
        this.movies = newMovies;
    }
}
