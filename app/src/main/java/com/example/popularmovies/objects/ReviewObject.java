package com.example.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewObject implements Parcelable {
    private String reviewAuthor;
    private String review;
    private String reviewLink;

    public ReviewObject(String reviewAuthor, String review, String reviewLink) {
        this.reviewAuthor = reviewAuthor;
        this.review = review;
        this.reviewLink = reviewLink;
    }

    protected ReviewObject(Parcel in) {
        reviewAuthor = in.readString();
        review = in.readString();
        reviewLink = in.readString();
    }

    public static final Creator<ReviewObject> CREATOR = new Creator<ReviewObject>() {
        @Override
        public ReviewObject createFromParcel(Parcel in) {
            return new ReviewObject(in);
        }

        @Override
        public ReviewObject[] newArray(int size) {
            return new ReviewObject[size];
        }
    };

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReview() {
        return review;
    }

    public String getReviewLink() {
        return reviewLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewAuthor);
        dest.writeString(review);
        dest.writeString(reviewLink);
    }
}
