package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;

import com.example.popularmovies.adapter.AdapterMovies;
import com.example.popularmovies.databinding.ActivityMainBinding;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.example.popularmovies.utils.ParserUtil;
import com.example.popularmovies.utils.UrlBuilderTools;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements GeneralUtils.ResponseReciever {

    private ActivityMainBinding binding;

    private String currentSort = Const.POPULAR_SORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        loadMovies();

        binding.activityMainRecView.setLayoutManager(new GridLayoutManager(this, getNumberOfColumns()));
    }

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

    @Override
    public void responseRecieved(String response) {
        if (response != Const.message_error) {
            binding.activityMainRecView.setAdapter(new AdapterMovies(ParserUtil.moviePosterObjects(response), this));
            binding.activityMainProgressBar.setVisibility(View.GONE);
        } else {
            GeneralUtils.someProblemToast(this);
        }
    }
}
