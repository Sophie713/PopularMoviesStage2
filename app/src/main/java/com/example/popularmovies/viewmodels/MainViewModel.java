package com.example.popularmovies.viewmodels;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.popularmovies.database.MovieDatabase;
import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.objects.MoviePosterObject;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    MovieDatabase database;
    private LiveData<List<MovieDetailObject>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MovieDatabase.getInstance(this.getApplication());
        movies = database.movieDao().allFavoriteMovies();
    }

    public LiveData<List<MovieDetailObject>> getAllMovies() {
        return movies;
    }
    public LiveData<MovieDetailObject> getMovie(String movieId){
        return database.movieDao().getMoviebyId(movieId);
    }

}
