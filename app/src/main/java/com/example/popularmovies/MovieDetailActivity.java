package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.popularmovies.adapter.MovieDetailVidRevAdapter;
import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.objects.MoviePosterObject;
import com.example.popularmovies.objects.ReviewObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.utils.ParserUtil;
import com.example.popularmovies.utils.UrlBuilderTools;
import com.example.popularmovies.viewmodels.MainViewModel;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieDetailActivity extends AppCompatActivity implements GeneralUtils.ResponseReciever {

    //objects
    private ActivityMovieDetailBinding binding;
    private MovieDetailVidRevAdapter detailVidRevAdapter;
    private Executor executor = Executors.newFixedThreadPool(3);

    //saved state keys
    private static final String SAVED_STATE_ID = "movie_id";
    private static final String SAVED_STATE_VID = "state_vid";
    private static final String SAVED_STATE_REW = "state_rew";
    private static final String SAVED_STATE = "state";
    private static final String SAVED_STATE_FAV = "favorite";

    //data
    private String movieId;
    private boolean isFavorite = false;
    private boolean wasFavoriteONOpen = false;
    private MovieDetailObject movieDetailObject;
    private ArrayList<String> videos;
    private ArrayList<ReviewObject> reviews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent.hasExtra(Const.movieId))
            movieId = intent.getStringExtra(Const.movieId);

        //if I didn't get the ID, I cannot do anuthing
        if (movieId == null || movieId.length() < 1) {
            GeneralUtils.someProblemToast(this);
            onBackPressed();
        }

        //I need to find out if at this moment the movie is in favorites or not
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int contains = MovieDatabase.getInstance(MovieDetailActivity.this).movieDao().countOdIds(movieId);
                if (contains > 0) {
                    MovieDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isFavorite = true;
                            wasFavoriteONOpen = true;
                            setFavoriteStar();
                        }
                    });
                }
            }
        });

        /**
         * switch UI and favorite setting inside the app, wait with databse write until final (in onStop)
         */
        binding.activityMovieDetailStarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;
                setFavoriteStar();
            }
        });

        detailVidRevAdapter = new MovieDetailVidRevAdapter();
        binding.activityMovieDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.activityMovieDetailRecyclerView.setAdapter(detailVidRevAdapter);

        //no saved instance = I need to get data, otherwise I retrieve the saved ones in case of an error I load them again
        if (savedInstanceState == null) {
            if (Const.currentSort.equals(Const.FAVORITE)) {
                isFavorite = true;
                setFavoriteStar();
                getMovieFromDatabse();
                requestVideosandReviews();
            } else {
                setFavoriteStar();
                loadMovieDetails();
            }
        } else {
            try {
                movieId = savedInstanceState.getString(SAVED_STATE_ID);
                isFavorite = savedInstanceState.getBoolean(SAVED_STATE_FAV);
                setFavoriteStar();
                movieDetailObject = savedInstanceState.getParcelable(SAVED_STATE);
                if (movieDetailObject != null) {
                    fillUI(movieDetailObject);
                    videos = savedInstanceState.getStringArrayList(SAVED_STATE_VID);
                    if (videos == null) {
                        NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildMovieDetailsVideo(movieId)), this, Const.MOVIE_VIDEO_ID);
                    } else {
                        detailVidRevAdapter.setVideos(videos, this);
                    }
                    reviews = savedInstanceState.getParcelableArrayList(SAVED_STATE_REW);
                    if (reviews == null) {
                        NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildMovieDetailsReview(movieId)), this, Const.MOVIE_REVIEWS_ID);
                    } else {
                        detailVidRevAdapter.setReviews(reviews, this);
                    }
                } else {
                    loadMovieDetails();
                }
            } catch (Exception unexpected) {
                loadMovieDetails();
                Log.e(Const.MOVIE_DETAIL_ACTIVITY, "" + unexpected.getMessage());
            }

        }
    }

    /**
     * get viewholder and get my movie
     */
    private void getMovieFromDatabse() {
        binding.activityMovieDetailProgressBar.setVisibility(View.VISIBLE);
        final MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMovie(movieId).observe(MovieDetailActivity.this, new Observer<MovieDetailObject>() {
            @Override
            public void onChanged(MovieDetailObject movieDetailObject) {
                fillUI(movieDetailObject);
                MovieDetailActivity.this.movieDetailObject = movieDetailObject;
            }
        });
        binding.activityMovieDetailProgressBar.setVisibility(View.GONE);
    }

    /**
     * send network request for movie details
     */
    private void loadMovieDetails() {
        if (GeneralUtils.isOnline(this)) {
            try {
                binding.activityMovieDetailProgressBar.setVisibility(View.VISIBLE);
                NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildMovieDetailsGeneral(movieId)), this, Const.MOVIE_DETAILS_ID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            GeneralUtils.noConnectionToast(this);
        }
    }

    /**
     * parses the JSON and fill the data into the UI function
     *
     * @param response = json from the server
     * @param taskId   = id of the task
     */
    @Override
    public void responseRecieved(String response, int taskId) {
        switch (taskId) {
            case Const.MOVIE_DETAILS_ID: {
                if (!response.equals(Const.message_error)) {
                    movieDetailObject = ParserUtil.movieDetails(response);
                    movieDetailObject.setMovieId(movieId);
                    fillUI(movieDetailObject);
                    //I know my object exists, I request additional parameters
                    requestVideosandReviews();
                } else {
                    binding.activityMovieDetailProgressBar.setVisibility(View.GONE);
                    GeneralUtils.someProblemToast(this);
                    onBackPressed();
                }
                break;
            }
            case Const.MOVIE_VIDEO_ID: {
                //movieDetails.addVideos();
                videos = ParserUtil.videoUrls(response);
                detailVidRevAdapter.setVideos(videos, this);
                break;
            }
            case Const.MOVIE_REVIEWS_ID: {
                //movieDetails.addReviews();
                reviews = ParserUtil.reviews(response);
                detailVidRevAdapter.setReviews(reviews, this);
                break;
            }
            case Const.MOVIE_VIDEO_REVIEW_UPDATED: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detailVidRevAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    /**
     * send Network request to recieve movies and reviews (Not in the database, because reviews change and take up a lot of space and movie links are useless without connection anyway
     */
    private void requestVideosandReviews() {
        if (GeneralUtils.isOnline(this)) {
            try {
                NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildMovieDetailsVideo(movieId)), this, Const.MOVIE_VIDEO_ID);
                NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildMovieDetailsReview(movieId)), this, Const.MOVIE_REVIEWS_ID);
            } catch (Exception e) {
                Toast.makeText(MovieDetailActivity.this, R.string.error_videos_reviews_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            GeneralUtils.noConnectionToast(this);
        }
    }

    /**
     * put the basic movie details into the UI
     *
     * @param movieDetails
     */
    private void fillUI(MovieDetailObject movieDetails) {
        String title = movieDetails.getTitle();
        setTitle(title);
        binding.activityMovieDetailReleaseTV.setText(movieDetails.getReleaseDate());
        binding.activityMovieDetailVotesTV.setText(movieDetails.getVoteAverage());
        binding.activityMovieDetailPlotTV.setText(movieDetails.getPlot());
        if (title.equals(Const.sophie)) {
            //There has been some error parsing data, normally I would inform the user, for the fun of it, in this app, this is the placeholder
            Picasso.with(this).load(movieDetails.getPosterUrl()).placeholder(R.drawable.unnamed).into(binding.activityMovieDetailImageIV);
        } else {
            Picasso.with(this).load(new UrlBuilderTools().buildUrlForPoster(movieDetails.getPosterUrl())).placeholder(R.drawable.unnamed).into(binding.activityMovieDetailImageIV);
        }
        binding.activityMovieDetailProgressBar.setVisibility(View.GONE);
    }

    /**
     * function to set the star based on if it is or is not a favorite movie
     */
    private void setFavoriteStar() {
        if (isFavorite)
            binding.activityMovieDetailStarIV.setImageResource(R.drawable.ic_star_on);
        else
            binding.activityMovieDetailStarIV.setImageResource(R.drawable.ic_star_off);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieDetailObject != null)
            outState.putParcelable(SAVED_STATE, movieDetailObject);
        if (videos != null)
            outState.putStringArrayList(SAVED_STATE_VID, videos);
        if (reviews != null)
            outState.putParcelableArrayList(SAVED_STATE_REW, reviews);
        outState.putBoolean(SAVED_STATE_FAV, isFavorite);
        outState.putString(SAVED_STATE_ID, movieId);
    }

    /**
     * save movie to favorites if it wasn't and vice versa
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (isFavorite && (!wasFavoriteONOpen)) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    MovieDatabase.getInstance(MovieDetailActivity.this).movieDao().makeMovieFavorite(movieDetailObject);
                }
            });
        } else if ((!isFavorite) && wasFavoriteONOpen) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    MovieDatabase.getInstance(MovieDetailActivity.this).movieDao().removeMovieFromFavorites(movieDetailObject);
                }
            });
        }
    }
}
