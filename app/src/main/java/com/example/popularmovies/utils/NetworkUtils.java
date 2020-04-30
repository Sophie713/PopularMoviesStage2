package com.example.popularmovies.utils;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static void getServerResponse(URL url, final GeneralUtils.ResponseReciever responseReciever) {
        new AsyncTask<URL, Void, String>() {
            String response = "";

            @Override
            protected String doInBackground(URL... urls) {
                URL url = urls[0];
                try {
                    response = getJson(url);
                } catch (Exception e) {
                    responseReciever.responseRecieved(Const.message_error);
                }
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                responseReciever.responseRecieved(response);
                super.onPostExecute(s);
            }
        }.execute(url);
    }

    public static String getJson(URL url) {
        String json = "";
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                json = scanner.next();
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
