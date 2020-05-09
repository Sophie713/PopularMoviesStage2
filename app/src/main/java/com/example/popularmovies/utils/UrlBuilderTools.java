package com.example.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

public class UrlBuilderTools {

    /**
     * function that bulds URL for the movie posters with movie ids (for list of movies)
     * @param sort sorting preference
     * @return URL
     */
    public static String buildUrlForMovies(String sort) {
        // sort should be either popular or top_rated
        if (!(sort.equals(Const.POPULAR_SORT) || sort.equals(Const.TOP_SORT))) {
            sort = Const.POPULAR_SORT;
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sort)
                .appendQueryParameter("api_key", Const.MOVIE_DB_API_KEY);
        return builder.build().toString();
    }

    public static String buildMovieDetailsVideo(String movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter("api_key", Const.MOVIE_DB_API_KEY);
        return builder.build().toString();
    }

    public static String buildMovieDetailsReview(String movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", Const.MOVIE_DB_API_KEY);
        return builder.build().toString();
    }

    /**
     * returns youtube video link from video key
     * https://www.youtube.com/watch?v=VtnLpHUu2U0
     * @param videoCode
     */
    public static String buildVideoUrl(String videoCode) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.youtube.com")
                .appendPath("watch")
                .appendQueryParameter("v", videoCode);
        return builder.build().toString();
    }

    /**
     * function that builds a url of a movie image
     * @param imageUrl final piece of address where the image is stored
     * @return image URL
     */
    public String buildUrlForPoster(String imageUrl) {
        // after hours of googling I din't manage to find a way to use Builder and have "//" inside, I am sorry
        return "http://image.tmdb.org/t/p/w185/" + imageUrl;
    }

    /**
     * function that creater URL for main movie detail info
     * @param movieId number in a String format of a movie
     * @return URL to get general movie info
     */
    public static String buildMovieDetailsGeneral(String movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendQueryParameter("api_key", Const.MOVIE_DB_API_KEY);
        return builder.build().toString();
    }
}
