package com.example.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.popularmovies.MainActivity;
import com.example.popularmovies.R;

public class GeneralUtils {
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

    public static void noConnectionToast(Context context) {
        Toast.makeText(context, R.string.no_internet_info, Toast.LENGTH_LONG).show();
    }

    public static void someProblemToast(Context context) {
        Toast.makeText(context, R.string.uknwn_error, Toast.LENGTH_LONG).show();
    }

    public interface ResponseReciever {
        public void responseRecieved(String response);
    }
}
