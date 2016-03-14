package com.example.saanu.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.saanu.popularmovies.data.MovieContract;
import com.example.saanu.popularmovies.model.MovieReviews;
import com.example.saanu.popularmovies.model.Videos;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Saanu_mac on 2/25/16.
 */
public class FetchTrailerReviews extends AsyncTask<Void, Void, Void> {

    private final Context mContext;
    public static final String LOG_TAG = FetchTrailerReviews.class.getSimpleName();
    final String APPID_PARAM = "api_key";
    String mymovieid;


    public FetchTrailerReviews(Context context, String movieid) {
        mContext = context;
        mymovieid = movieid;
    }

    @Override
    protected Void doInBackground(Void... params) {


        if (Utility.isOnline(mContext) == false) {

            Log.v(LOG_TAG, "NO INTERNET, Please check your Network connection");
            return null;
        }

        OkHttpClient client = new OkHttpClient();
        storeTrailers(mymovieid, client);
        storeMovieReviews(mymovieid, client);

        return null;
    }


    /**
     * Get movies Trailers from URL and store it to the database
     *
     * @param movieId
     * @param client
     */
    private void storeTrailers(String movieId, OkHttpClient client) {

        //http://api.themoviedb.org/3/movie/131631/videos?api_key=8e2a01ba363daf749ce201cbb1a61290

        final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?";


        Uri videoURI = Uri.parse(TRAILER_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY).build();

        //Log.v(LOG_TAG, "MHB The Video URL is " + videoURI.toString());


        //OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(videoURI.toString())
                .build();
        Response responses = null;

        try {
            responses = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Videos mTrailers = null;


        try {
            mTrailers = gson.fromJson(responses.body().charStream(), Videos.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        int mymovieId = mTrailers.getId();

        List<Videos.ResultsEntity> myTrailersList = mTrailers.getResults();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(myTrailersList.size());


        for (Videos.ResultsEntity trailerItem : myTrailersList) {

        /*    Log.v(LOG_TAG, "MHB GSON the Name is  " + trailerItem.getName());
            Log.v(LOG_TAG, "MHB GSON key is " + trailerItem.getKey());
            Log.v(LOG_TAG, "MHB GSON site is " + trailerItem.getSite());
*/
            ContentValues trailerValues = new ContentValues();

            trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, trailerItem.getId());
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_ISO, trailerItem.getIso_639_1());
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_KEY, trailerItem.getKey());
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_NAME, trailerItem.getName());
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_SITE, trailerItem.getSite());
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_SIZE, trailerItem.getSize());
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TYPE, trailerItem.getType());

            cVVector.add(trailerValues);
        }

        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, cvArray);
        }

        Log.v(LOG_TAG, "Trailer data Complete. " + inserted + " Inserted");

        //myMovieList = myCinemas.getMovieList();
        //Vector<ContentValues> cVVector = new Vector<ContentValues>(myMovieList.size());

    }


    private void storeMovieReviews(String movieId, OkHttpClient client) {

        //http://api.themoviedb.org/3/movie/131631/videos?api_key=8e2a01ba363daf749ce201cbb1a61290

        final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews?";


        Uri reviewURI = Uri.parse(REVIEWS_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_API_KEY).build();

//        Log.v(LOG_TAG, "MHB The Movie Review URL is " + reviewURI.toString());


        Request request = new Request.Builder()
                .url(reviewURI.toString())
                .build();
        Response responses = null;

        try {
            responses = client.newCall(request).execute();

            //          Log.v(LOG_TAG, "The response code is " + responses.code());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        MovieReviews movieReviews = null;


        try {
            movieReviews = gson.fromJson(responses.body().charStream(), MovieReviews.class);
            //        Log.v(LOG_TAG, "The movie review response is " + movieReviews.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        int mymovieId = movieReviews.getId();

        //  Log.v(LOG_TAG, "MHB GSON The movie id is " + movieReviews.getId());

        List<MovieReviews.ResultsEntity> myReviewList = movieReviews.getResults();
        Vector<ContentValues> cVVector = new Vector<ContentValues>(myReviewList.size());


        for (MovieReviews.ResultsEntity reviewItem : myReviewList) {

        /*    Log.v(LOG_TAG, "MHB GSON the Author is  " + reviewItem.getAuthor());
            Log.v(LOG_TAG, "MHB GSON The review is " + reviewItem.getContent());
        */
            ContentValues reviewValues = new ContentValues();

            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_MOVIE_ID, movieId);
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEW_ID, reviewItem.getId());
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_AUTHOR, reviewItem.getAuthor());
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_CONTENT, reviewItem.getContent());
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_URL, reviewItem.getUrl());

            cVVector.add(reviewValues);

        }

        //Log.v(LOG_TAG, "MHB GSON Total review pages  is " + movieReviews.getTotal_pages());


        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI, cvArray);
        }

        //Log.v(LOG_TAG, " Reviews data Complete. " + inserted + " Inserted");

    }

}
