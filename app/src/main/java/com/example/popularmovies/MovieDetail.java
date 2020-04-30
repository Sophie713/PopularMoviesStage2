package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.utils.ParserUtil;
import com.example.popularmovies.utils.UrlBuilderTools;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetail extends AppCompatActivity implements GeneralUtils.ResponseReciever {

    ActivityMovieDetailBinding binding;

    String movieId;
    MovieDetailObject movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra(Const.movieId))
            movieId = intent.getStringExtra(Const.movieId);

        if (movieId == null || movieId.length() < 1) {
            GeneralUtils.someProblemToast(this);
            onBackPressed();
        }
        loadMovieDetails();
    }

    private void loadMovieDetails() {
        if (GeneralUtils.isOnline(this)) {
            try {
                binding.activityMovieDetailProgressBar.setVisibility(View.VISIBLE);
                NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildMovieDetailsGeneral(movieId)), this);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            GeneralUtils.noConnectionToast(this);
        }
    }

    @Override
    public void responseRecieved(String response) {
        if (!response.equals(Const.message_error)) {
            movieDetails = ParserUtil.movieDetails(response);
            String title = movieDetails.getTitle();
            if (title.equals(Const.sophie)) {
                //There has been some error parsing data, normally I would inform the user, for the fun of it, in this app, this is the placeholder
                setTitle(title);
                binding.activityMovieDetailReleaseTV.setText(movieDetails.getReleaseDate());
                binding.activityMovieDetailVotesTV.setText(movieDetails.getVoteAverage());
                binding.activityMovieDetailPlotTV.setText(movieDetails.getPlot());
                Picasso.with(this).load(movieDetails.getPosterUrl()).placeholder(R.drawable.unnamed).into(binding.activityMovieDetailImageIV);
            } else {
                if (title.length() > 0)
                    setTitle(title);
                // if it was not parsed, it is the default "", so I use Unknown instead
                if (title.length() > 0)
                    binding.activityMovieDetailReleaseTV.setText(movieDetails.getReleaseDate());
                else
                    binding.activityMovieDetailReleaseTV.setText(R.string.uknown);

                if (title.length() > 0)
                    binding.activityMovieDetailVotesTV.setText(movieDetails.getVoteAverage());
                else
                    binding.activityMovieDetailVotesTV.setText(R.string.uknown);


                if (title.length() > 0)
                    binding.activityMovieDetailPlotTV.setText(movieDetails.getPlot());
                else
                    binding.activityMovieDetailPlotTV.setText(R.string.uknown);
                Picasso.with(this).load(new UrlBuilderTools().buildUrlForPoster(movieDetails.getPosterUrl())).placeholder(R.drawable.unnamed).into(binding.activityMovieDetailImageIV);

            }
            binding.activityMovieDetailProgressBar.setVisibility(View.GONE);
        } else {
            binding.activityMovieDetailProgressBar.setVisibility(View.GONE);
            GeneralUtils.someProblemToast(this);
            onBackPressed();
        }

        //Picasso.with(context).load(new UrlBuilderTools().buildUrlForPoster(movies.get(position).getImgUrl())).placeholder(R.drawable.ic_launcher_background).into(holder.image);

    }
}
