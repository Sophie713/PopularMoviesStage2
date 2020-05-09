package com.example.popularmovies.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.utils.Const;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM " + Const.TABLE_NAME)
    LiveData<List<MovieDetailObject>> allFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void makeMovieFavorite(MovieDetailObject movie);

    @Delete
    void removeMovieFromFavorites(MovieDetailObject movie);

    @Query("SELECT COUNT(movieId) FROM " + Const.TABLE_NAME)
    LiveData<Integer> getDataCount();

    @Query("SELECT * FROM " + Const.TABLE_NAME + " WHERE movieId = :moviesId")
    LiveData<MovieDetailObject> getMoviebyId(String moviesId);

    @Query("SELECT COUNT(*) from " + Const.TABLE_NAME + " WHERE movieId = :moviesId")
     int countOdIds(String moviesId);
}
