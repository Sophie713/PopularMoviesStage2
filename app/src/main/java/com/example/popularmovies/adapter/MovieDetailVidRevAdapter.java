package com.example.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.R;
import com.example.popularmovies.holder.HolderReview;
import com.example.popularmovies.holder.HolderVideo;
import com.example.popularmovies.objects.ReviewObject;
import com.example.popularmovies.utils.Const;
import com.example.popularmovies.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieDetailVidRevAdapter extends RecyclerView.Adapter {

    private ArrayList<String> videoUrls = new ArrayList<>();
    private ArrayList<ReviewObject> reviews = new ArrayList<>();
    private ArrayList<Object> items = new ArrayList<>();
    private static Executor executor = Executors.newSingleThreadExecutor();
    //view types
    private static final int VIDEO_VIEW_TYPE = 0;
    private static final int REVIEW_VIEW_TYPE = 1;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIDEO_VIEW_TYPE:
                View videoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_video, parent, false);
                HolderVideo video = new HolderVideo(videoView);
                return video;
            case REVIEW_VIEW_TYPE:
                View reviewView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_review, parent, false);
                HolderReview review = new HolderReview(reviewView);
                return review;
            default:
                View error = LayoutInflater.from(parent.getContext()).inflate(R.layout.error_layout, parent, false);
                return new ErrorViewHolder(error);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return 0;
        } else if (items.get(position) instanceof ReviewObject) {
            return 1;
        } else return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIDEO_VIEW_TYPE:
                //video - set on click listener, set trailer with number
                final HolderVideo videoHolder = (HolderVideo) holder;
                final String vidoeUrl = (String) items.get(position);
                final Context context = videoHolder.layout.getContext();
                videoHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(vidoeUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
                videoHolder.videoNumber.setText(String.format(context.getString(R.string.trailer), position + 1));
                break;
            case REVIEW_VIEW_TYPE:
                //review - set author, content and link
                final HolderReview reviewHolder = (HolderReview) holder;
                final ReviewObject reviewObject = (ReviewObject) items.get(position);
                reviewHolder.author.setText(reviewObject.getReviewAuthor());
                reviewHolder.review.setText(reviewObject.getReview());
                reviewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(reviewObject.getReviewLink());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        reviewHolder.layout.getContext().startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (reviews.size() + videoUrls.size());
    }

    /**
     * add videos to the list
     *
     * @param newVideoUrls
     */
    public void setVideos(final ArrayList<String> newVideoUrls, final GeneralUtils.ResponseReciever reciever) {
        //avoid concurrent modification exception
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //clear the lists
                items.clear();
                videoUrls.clear();
                //update videos
                videoUrls.addAll(newVideoUrls);
                //put the collection together again
                items.addAll(videoUrls);
                items.addAll(reviews);
                reciever.responseRecieved("", Const.MOVIE_VIDEO_REVIEW_UPDATED);
            }
        });
    }

    /**
     * add reviews to the list
     *
     * @param newReviews
     */
    public void setReviews(final ArrayList<ReviewObject> newReviews, final GeneralUtils.ResponseReciever reciever) {
        //avoid concurrent modification exception
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //clear the lists
                items.clear();
                reviews.clear();
                //update reviews
                reviews.addAll(newReviews);
                //put the collection together again
                items.addAll(videoUrls);
                items.addAll(reviews);
                reciever.responseRecieved("", Const.MOVIE_VIDEO_REVIEW_UPDATED);
            }
        });
    }

    class ErrorViewHolder extends RecyclerView.ViewHolder {
        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
