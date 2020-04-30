package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.popularmovies.adapter.AdapterMovies;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.objects.MoviePosterObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.utils.ParserUtil;
import com.example.popularmovies.utils.UrlBuilderTools;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GeneralUtils.ResponseReciever {

    private static final String SAVED_STATE = "state";
    private ActivityMainBinding binding;

    private String currentSort = Const.POPULAR_SORT;
    private ArrayList<MoviePosterObject> moviePosterObjectArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set up spinner
        binding.activityMainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        currentSort = Const.POPULAR_SORT;
                        break;
                    case 1:
                        currentSort = Const.TOP_SORT;
                        break;
                }
                loadMovies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //set layout manager to recyclerview
        binding.activityMainRecView.setLayoutManager(new GridLayoutManager(this, getNumberOfColumns()));

        //is I have saved instance I use it else I load movies
        if (savedInstanceState == null) {
            loadMovies();
        } else {
            try {
                moviePosterObjectArrayList = savedInstanceState.getParcelableArrayList(SAVED_STATE);
                binding.activityMainRecView.setAdapter(new AdapterMovies(moviePosterObjectArrayList, this));
            } catch (Exception unexpected) {
                loadMovies();
                Log.e(Const.MAIN_ACTIVITY, unexpected.getMessage());
            }
        }
    }

    /**
     * load movies start ; show loading spinner
     */
    private void loadMovies() {
        if (GeneralUtils.isOnline(this)) {
            try {
                binding.activityMainProgressBar.setVisibility(View.VISIBLE);
                NetworkUtils.getServerResponse(new URL(UrlBuilderTools.buildUrlForMovies(currentSort)), this);
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
     */
    @Override
    public void responseRecieved(String response) {
        if (response != Const.message_error) {
            moviePosterObjectArrayList = ParserUtil.moviePosterObjects(response);
            binding.activityMainRecView.setAdapter(new AdapterMovies(moviePosterObjectArrayList, this));
            binding.activityMainProgressBar.setVisibility(View.GONE);
        } else {
            GeneralUtils.someProblemToast(this);
        }
    }

    /**
     * save the data if we already have it
     * @param outState
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (moviePosterObjectArrayList != null)
            outState.putParcelableArrayList(SAVED_STATE, moviePosterObjectArrayList);
    }
}
