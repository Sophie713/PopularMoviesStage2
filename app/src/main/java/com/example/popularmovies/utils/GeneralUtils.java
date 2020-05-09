package com.example.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.popularmovies.MainActivity;
import com.example.popularmovies.R;

public class GeneralUtils {
    /**
     * tests if the devise is online
     * @param context
     * @return true if it is online false if there is an error or is offline
     */
    public static boolean isOnline(Context context) {
        //I used android documentation to write this part of code
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Toast message for no connection
     * @param context
     */
    public static void noConnectionToast(Context context) {
        Toast.makeText(context, R.string.no_internet_info, Toast.LENGTH_LONG).show();
    }

    /**
     * toast message for general error
     * @param context
     */
    public static void someProblemToast(Context context) {
        Toast.makeText(context, R.string.uknwn_error, Toast.LENGTH_LONG).show();
    }

    /**
     * toast message for no favorite movies in the database
     * @param context
     */
    public static void noFavoriteMoviesToast(Context context) {
        Toast.makeText(context, R.string.no_favorite_movies, Toast.LENGTH_LONG).show();

    }

    /**
     * interface to recieve asynchronous data
     */
    public interface ResponseReciever {
        void responseRecieved(String response, int taskId);
    }
}
