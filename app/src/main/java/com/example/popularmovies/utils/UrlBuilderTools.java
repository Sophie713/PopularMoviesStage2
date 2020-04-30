package com.example.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

public class UrlBuilderTools {

    private static String MOVIE_DB_API_KEY = "e7afd50f939fdba0f7e1c6c15efa654a";

    public static String buildUrlForMovies(String sort) {
        /*
         * sort should be either popular or top_rated
         */
        if (!(sort.equals(Const.POPULAR_SORT) || sort.equals(Const.TOP_SORT))) {
            sort = Const.POPULAR_SORT;
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sort)
                .appendQueryParameter("api_key", MOVIE_DB_API_KEY);
        return builder.build().toString();
    }

    public String buildUrlForPoster(String imageUrl){
        // after hours of googling I din't manage to find a way to use Builder and have "//" inside, I am sorry
        return "http://image.tmdb.org/t/p/w185/" + imageUrl;
    }

    public static String buildMovieDetailsGeneral(String movieId){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendQueryParameter("api_key", MOVIE_DB_API_KEY);
        return builder.build().toString();
    }
}
