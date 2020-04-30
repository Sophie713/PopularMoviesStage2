package com.example.popularmovies.utils;

import android.util.JsonReader;

import com.example.popularmovies.MovieDetail;
import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.objects.MoviePosterObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserUtil {
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String M_ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE = "release_date";
    private static final String VOTE_AVRG = "vote_average";
    private static final String PLOT = "overview";

    /**
     * takes JSON of movies and returns list of objects with movie id and porter url
     * @param json takes JSON of movies
     * @return list of objects with movie id and porter url
     */
    public static ArrayList<MoviePosterObject> moviePosterObjects(String json) {
        ArrayList<MoviePosterObject> posterUrls = new ArrayList<>();
        try {
            JSONObject fullMoviesJson = new JSONObject(json);
            JSONArray moviesArray = fullMoviesJson.optJSONArray(RESULTS);
            if (moviesArray != null && moviesArray.length() > 0) {
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    posterUrls.add(new MoviePosterObject(movie.optString(POSTER_PATH), String.valueOf(movie.optInt(M_ID))));
                }
            }
        } catch (Exception e) {
            //todo do somehing ??
        }
        return posterUrls;
    }

    /**
     * takes JSON of a movie and returns the movie details
     * @param json JSON of movie details
     * @return object with movie poster, title, release date, votes and plot
     */
    public static MovieDetailObject movieDetails(String json) {
        MovieDetailObject movieDetails = null;
        try {
            JSONObject movieJson = new JSONObject(json);
            movieDetails = new MovieDetailObject(movieJson.optString(POSTER_PATH), movieJson.optString(TITLE), movieJson.optString(RELEASE), movieJson.optString(PLOT), String.valueOf(movieJson.optDouble(VOTE_AVRG)));

        } catch (Exception e) {
            //todo do somehing ??
        }
        if (movieDetails != null)
            return movieDetails;
        else
            return new MovieDetailObject("https://scontent-prg1-1.xx.fbcdn.net/v/t1.0-1/p160x160/82768450_10221294541782980_2544801194128179200_n.jpg?_nc_cat=101&_nc_sid=dbb9e7&_nc_oc=AQnVSKnWZdtsIRjAiBaDWQBdVWf8R2ayarnC5HwgWLRdX03hiyRs3B1dieW3f-vz3fA&_nc_ht=scontent-prg1-1.xx&_nc_tp=6&oh=6b08419a45359e9e50c4eb939172d32c&oe=5ECC4896", Const.sophie, "28.4.1991", "A starving Android Developer who loves learning new things.", "5.5");
    }
}
