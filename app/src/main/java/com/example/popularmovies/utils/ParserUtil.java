package com.example.popularmovies.utils;

import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.objects.MoviePosterObject;
import com.example.popularmovies.objects.ReviewObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;

public class ParserUtil {
    private static final String RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String M_ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE = "release_date";
    private static final String VOTE_AVRG = "vote_average";
    private static final String PLOT = "overview";
    private static final String KEY = "key";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";

    /**
     * takes JSON of movies and returns list of objects with movie id and porter url
     *
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
        } catch (Exception ignored) {
        }
        return posterUrls;
    }

    /**
     * takes JSON of a movie and returns the movie details (or a placeholder if there is any error)
     *
     * @param json JSON of movie details
     * @return object with movie poster, title, release date, votes and plot
     */
    public static MovieDetailObject movieDetails(String json) {
        MovieDetailObject movieDetails = null;
        try {
            JSONObject movieJson = new JSONObject(json);
            movieDetails = new MovieDetailObject(movieJson.optString(POSTER_PATH, "Unknown"), movieJson.optString(TITLE, "Unknown"), movieJson.optString(RELEASE, "Unknown"), movieJson.optString(PLOT, "Unknown"), String.valueOf(movieJson.optDouble(VOTE_AVRG)));
        }catch (Exception ignored) {
        }
        if (movieDetails != null)
            return movieDetails;
        else
            return new MovieDetailObject("https://scontent-prg1-1.xx.fbcdn.net/v/t1.0-1/p160x160/82768450_10221294541782980_2544801194128179200_n.jpg?_nc_cat=101&_nc_sid=dbb9e7&_nc_oc=AQnVSKnWZdtsIRjAiBaDWQBdVWf8R2ayarnC5HwgWLRdX03hiyRs3B1dieW3f-vz3fA&_nc_ht=scontent-prg1-1.xx&_nc_tp=6&oh=6b08419a45359e9e50c4eb939172d32c&oe=5ECC4896", Const.sophie, "28.4.1991", "A starving Android Developer who loves learning new things.", "5.5");
    }

    /**
     * returns array of strings with url addresses of video trailers
     *
     * @param json
     * @return
     */
    public static ArrayList<String> videoUrls(String json) {
        ArrayList<String> videoUrls = new ArrayList<>();
        try {
            JSONObject videoJson = new JSONObject(json);
            JSONArray vidoesArray = videoJson.optJSONArray(RESULTS);
            if (vidoesArray != null) {
                for (int i = 0; i < vidoesArray.length(); i++) {
                    JSONObject vidoeInfo = vidoesArray.optJSONObject(i);
                    String videoCode = vidoeInfo.optString(KEY);
                    videoUrls.add(UrlBuilderTools.buildVideoUrl(videoCode));
                }
            }
        } catch (Exception ignored) {
        }
        return videoUrls;
    }

    public static ArrayList<ReviewObject> reviews(String json) {
        ArrayList<ReviewObject> reviews = new ArrayList<>();
        try {
            JSONObject reviewsJson = new JSONObject(json);
            JSONArray reviewsArray = reviewsJson.optJSONArray(RESULTS);
            if (reviewsArray != null) {
                for (int i = 0; i < reviewsArray.length(); i++){
                    JSONObject review = reviewsArray.optJSONObject(i);
                    reviews.add(new ReviewObject(review.optString(AUTHOR), review.optString(CONTENT), review.optString(URL)));
                }
            }
        } catch (Exception ignored) {
        }
        return reviews;
    }
}
