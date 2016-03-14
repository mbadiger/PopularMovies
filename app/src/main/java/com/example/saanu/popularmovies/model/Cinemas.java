package com.example.saanu.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Cinemas {


    @SerializedName("results")
    List<MovieDataFormat> movieList;

    public List<MovieDataFormat> getMovieList() {
        return movieList;
    }

    public static class MovieDataFormat {


        @SerializedName("original_title")
        private String mTitle;

        @SerializedName("id")
        private int mMovieId;

        @SerializedName("release_date")
        private String mReleaseDate;

        @SerializedName("poster_path")
        private String mPosterPath;

        @SerializedName("vote_average")
        private double mRating;

        @SerializedName("vote_count")
        private int mVoteCount;

        @SerializedName("overview")
        private String mDescription;

        @SerializedName("popularity")
        private double mPopularity;

        private int mRuntime = 0;

        public String getTitle() {
            return mTitle;
        }

        public int getMovieId() {
            return mMovieId;
        }

        public String getReleaseDate() {
            return mReleaseDate;
        }

        public String getPosterPath() {
            return mPosterPath;
        }

        public double getRating() {
            return mRating;
        }

        public int getVoteCount() {
            return mVoteCount;
        }

        public String getDescription() {
            return mDescription;
        }

        public double getPopularity() {
            return mPopularity;
        }

        public void setRuntime(int runtime) {
            mRuntime = runtime;
        }

        public int getRuntime() {
            return mRuntime;
        }

    }

}

