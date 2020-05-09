package com.example.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.popularmovies.utils.Const;

@Entity(tableName = Const.TABLE_NAME)
public class MovieDetailObject implements Parcelable {
    private String posterUrl = "";
    private String title = "";
    private String releaseDate = "";
    private String plot = "";
    private String voteAverage = "";
    //id needs to be saved if the movie is added to the database so that videos and reviews can be downloaded
    @NonNull
    @PrimaryKey
    private String movieId;

    /**
     * Parcelable Movie detail with main information
     *
     * @param posterUrl
     * @param title
     * @param releaseDate
     * @param plot
     * @param voteAverage
     */
    @Ignore
    public MovieDetailObject(String posterUrl, String title, String releaseDate, String plot, String voteAverage) {
        if (plot != null)
            this.plot = plot;
        if (posterUrl != null)
            this.posterUrl = posterUrl;
        if (releaseDate != null)
            this.releaseDate = releaseDate;
        if (title != null)
            this.title = title;
        if (voteAverage != null)
            this.voteAverage = voteAverage;
    }

    public MovieDetailObject(@NonNull String movieId, String posterUrl, String title, String releaseDate, String plot, String voteAverage) {
        this.movieId = movieId;
        if (plot != null)
            this.plot = plot;
        if (posterUrl != null)
            this.posterUrl = posterUrl;
        if (releaseDate != null)
            this.releaseDate = releaseDate;
        if (title != null)
            this.title = title;
        if (voteAverage != null)
            this.voteAverage = voteAverage;
    }

    @Ignore
    protected MovieDetailObject(Parcel in) {
        posterUrl = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        plot = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<MovieDetailObject> CREATOR = new Creator<MovieDetailObject>() {
        @Override
        public MovieDetailObject createFromParcel(Parcel in) {
            return new MovieDetailObject(in);
        }

        @Override
        public MovieDetailObject[] newArray(int size) {
            return new MovieDetailObject[size];
        }
    };

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPlot() {
        return plot;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(plot);
        dest.writeString(voteAverage);
    }

    /**
     * id needs to be added if the movie is saved to the database
     *
     * @param movieId
     */
    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }
}
