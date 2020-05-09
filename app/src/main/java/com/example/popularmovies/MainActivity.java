package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.popularmovies.adapter.AdapterMovies;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.objects.MoviePosterObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.utils.ParserUtil;
import com.example.popularmovies.utils.UrlBuilderTools;
import com.example.popularmovies.viewmodels.MainViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GeneralUtils.ResponseReciever {

    private static final String SAVED_STATE = "state";
    private int firstRun = 0;

    private ActivityMainBinding binding;
    private AdapterMovies moviesAdapter;
    private ArrayList<MoviePosterObject> moviePosterObjectArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set layout manager to recyclerview
        binding.activityMainRecView.setLayoutManager(new GridLayoutManager(this, getNumberOfColumns()));
        moviesAdapter = new AdapterMovies(moviePosterObjectArrayList, this);
        binding.activityMainRecView.setAdapter(moviesAdapter);

        // set up spinner
        binding.activityMainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firstRun++;
                if (firstRun > 1) {
                    switch (position) {
                        case 0:
                            Const.currentSort = Const.FAVORITE;
                            loadMoviesFromDatabase();
                            break;
                        case 1:
                            Const.currentSort = Const.POPULAR_SORT;
                            loadMovies();
                            break;
                        case 2:
                            Const.currentSort = Const.TOP_SORT;
                            loadMovies();
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //is I have saved instance I use it else I load movies
        if (savedInstanceState == null) {
            //load data based on preference
            if (Const.currentSort.equals(Const.FAVORITE)) {
                loadMoviesFromDatabase();
            } else {
                loadMovies();
            }

        } else {
            try {
                //get the movies from saved instance
                moviePosterObjectArrayList = savedInstanceState.getParcelableArrayList(SAVED_STATE);
                updateAdapterData();
            } catch (Exception unexpected) {
                //in case of an error load them again
                if (Const.currentSort.equals(Const.FAVORITE))
                    loadMoviesFromDatabase();
                else
                    loadMovies();
                Log.e(Const.MAIN_ACTIVITY, "" + unexpected.getMessage());
            }
        }
    }

    private void loadMoviesFromDatabase() {
        binding.activityMainProgressBar.setVisibility(View.VISIBLE);
        moviePosterObjectArrayList.clear();
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getAllMovies().observe(MainActivity.this, new Observer<List<MovieDetailObject>>() {
            @Override
            public void onChanged(List<MovieDetailObject> movieDetailObjects) {
                if (Const.currentSort.equals(Const.FAVORITE)) {
                    if (movieDetailObjects.size() > 0) {
                        moviePosterObjectArrayList.clear();
                        for (int i = 0; i < movieDetailObjects.size(); i++) {
                            moviePosterObjectArrayList.add(new MoviePosterObject(movieDetailObjects.get(i).getPosterUrl(), movieDetailObjects.get(i).getMovieId()));
                            updateAdapterData();
                        }
                    } else {
                        GeneralUtils.noFavoriteMoviesToast(MainActivity.this);
                    }
                }
            }
        });
        binding.activityMainProgressBar.setVisibility(View.GONE);
    }

    private void updateAdapterData() {
        moviesAdapter.insertNewData(moviePosterObjectArrayList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                moviesAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * load movies start ; show loading spinner
     */
    private void loadMovies() {
        if (GeneralUtils.isOnline(this)) {
            try {
                binding.activityMainProgressBar.setVisibility(View.VISIBLE);
                NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildUrlForMovies(Const.currentSort)), this, Const.MOVIE_POSTER_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            GeneralUtils.noConnectionToast(this);
        }
    }

    /**
     * set number of columns based on screen size
     *
     * @return number of columns
     */
    private int getNumberOfColumns() {
        int columns = 1;
        // make a grid with a certain number of columns
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (width > 2000) {
            return 7;
        } else if (width > 1700) {
            return 6;
        } else if (width > 1357) {
            return 4;
        } else if (width > 719) {
            return 3;
        } else if (width > 200) {
            return 2;
        }
        return columns;
    }

    /**
     * server response - parse and display the data in the recyler view ; hide loading spinner
     *
     * @param response
     * @param taskId
     */
    @Override
    public void responseRecieved(String response, int taskId) {
        if (!response.equals(Const.message_error) && taskId == Const.MOVIE_POSTER_ID) {
            moviePosterObjectArrayList = ParserUtil.moviePosterObjects(response);
        } else {
            moviePosterObjectArrayList.clear();
            GeneralUtils.someProblemToast(this);
        }
        binding.activityMainProgressBar.setVisibility(View.GONE);
        updateAdapterData();
    }

    /**
     * save the data if we already have it
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (moviePosterObjectArrayList != null)
            outState.putParcelableArrayList(SAVED_STATE, moviePosterObjectArrayList);
    }
}
