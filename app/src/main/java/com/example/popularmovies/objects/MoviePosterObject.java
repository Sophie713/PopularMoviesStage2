package com.example.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class MoviePosterObject implements Parcelable {
    String imgUrl;
    String movieId;

    public MoviePosterObject(String imgUrl, String movieId) {
        this.imgUrl = imgUrl;
        this.movieId = movieId;
    }

    protected MoviePosterObject(Parcel in) {
        imgUrl = in.readString();
        movieId = in.readString();
    }

    public static final Creator<MoviePosterObject> CREATOR = new Creator<MoviePosterObject>() {
        @Override
        public MoviePosterObject createFromParcel(Parcel in) {
            return new MoviePosterObject(in);
        }

        @Override
        public MoviePosterObject[] newArray(int size) {
            return new MoviePosterObject[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public String getMovieId() {
        return movieId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(movieId);
    }
}
