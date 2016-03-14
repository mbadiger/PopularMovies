package com.example.saanu.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.example.saanu.popularmovies.data.MovieContract;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.example.saanu.popularmovies.model.Cinemas;
import com.example.saanu.popularmovies.model.MovieReviews;
import com.example.saanu.popularmovies.model.Videos;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class FetchMoviesData extends AsyncTask<Void, Void, Void> {

    public static final String LOG_TAG = FetchMoviesData.class.getSimpleName();
    final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    final String APPID_PARAM = "api_key";
    private final Context mContext;

    //HttpURLConnection urlConnection = null;
    //BufferedReader reader = null;
    //String forecastJsonStr = null;



    public FetchMoviesData(Context context) {
        mContext = context;
    }


    /**
     *
     * @param params
     * @return
     */
    @Override
    protected Void doInBackground(Void... params) {

        Uri movieURI = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM,BuildConfig.OPEN_MOVIE_API_KEY).build();


        if ( Utility.isOnline(mContext) == false) {

            Log.v(LOG_TAG, "NO INTERNET, Please check your Network connection");
            return null;
        }

        storeMovieDataToDb(movieURI.toString());

        //Log.v(LOG_TAG, "MHB The movie URL is " +movieURI.toString());
        return null;
    }


    /**
     *
     * Get movies from URL and store it to the database
     *
     * @param uri
     */
    private void storeMovieDataToDb(String uri) {

        List <Cinemas.MovieDataFormat> myMovieList;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(uri)
                .build();
        Response responses = null;

        try {
            responses = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Cinemas myCinemas = null;
        try {
            myCinemas = gson.fromJson(responses.body().charStream(), Cinemas.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myMovieList = myCinemas.getMovieList();
        Vector<ContentValues> cVVector = new Vector<ContentValues>(myMovieList.size());


        for (Cinemas.MovieDataFormat movieItem : myMovieList ) {

          /*  Log.v(LOG_TAG, "MHB GSON original title is " +movieItem.getTitle());
            Log.v(LOG_TAG, "MHB From GSON Poster path is " + movieItem.getPosterPath());

            Log.v(LOG_TAG, "MHB From GSON Movie id is " + movieItem.getMovieId());
*/
            ContentValues movieValues = new ContentValues();


            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieItem.getMovieId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieItem.getTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movieItem.getDescription());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieItem.getReleaseDate());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieItem.getPosterPath());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movieItem.getPopularity());
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieItem.getRating());
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieItem.getVoteCount());

            cVVector.add(movieValues);

            //storeTrailers(movieItem.getMovieId(), client);
            //storeMovieReviews(movieItem.getMovieId(), client);

        }

        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }

//        Log.v(LOG_TAG, "FetchMoviesData Complete. " + inserted + " Inserted");


/*
        String jsonData = null;
        try {
            jsonData = responses.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*JSONObject Jobject = null;
        try {
            Jobject = new JSONObject(jsonData);
            JSONArray Jarray = Jobject.getJSONArray("results");

            String title;
            String poster_path;

            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject object     = Jarray.getJSONObject(i);

                title = object.getString("original_title");
                poster_path = object.getString("poster_path");

                Log.v(LOG_TAG, "MHB the title is " +title  +" The poster path is  " +poster_path);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    /**
     *
     * Get movies Trailers from URL and store it to the database
     *
     * @param movieId
     * @param client
     */
    private void storeTrailers(int movieId, OkHttpClient client) {

        //http://api.themoviedb.org/3/movie/131631/videos?api_key=8e2a01ba363daf749ce201cbb1a61290

        final String TRAILER_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/videos?" ;


        Uri videoURI = Uri.parse(TRAILER_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM,BuildConfig.OPEN_MOVIE_API_KEY).build();

  //      Log.v(LOG_TAG, "MHB The Video URL is " +videoURI.toString());


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


        for ( Videos.ResultsEntity trailerItem : myTrailersList) {

    /*        Log.v(LOG_TAG, "MHB GSON the Name is  " +trailerItem.getName());
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
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, cvArray);
        }

        //Log.v(LOG_TAG, "FetchMoviesData Trailer data Complete. " + inserted + " Inserted");

        //myMovieList = myCinemas.getMovieList();
        //Vector<ContentValues> cVVector = new Vector<ContentValues>(myMovieList.size());

    }




    private void storeMovieReviews(int movieId, OkHttpClient client) {


        final String REVIEWS_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews?" ;


        Uri reviewURI = Uri.parse(REVIEWS_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM,BuildConfig.OPEN_MOVIE_API_KEY).build();

        //Log.v(LOG_TAG, "MHB The Movie Review URL is " + reviewURI.toString());


        Request request = new Request.Builder()
                .url(reviewURI.toString())
                .build();
        Response responses = null;

        try {
            responses = client.newCall(request).execute();

          //  Log.v(LOG_TAG, "The response code is " +responses.code());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        MovieReviews movieReviews = null;


        try {
            movieReviews = gson.fromJson(responses.body().charStream(), MovieReviews.class);
          //  Log.v(LOG_TAG, "The movie review response is " +movieReviews.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        int mymovieId = movieReviews.getId();

        //Log.v(LOG_TAG, "MHB GSON The movie id is " + movieReviews.getId());

        List<MovieReviews.ResultsEntity> myReviewList = movieReviews.getResults();

//        Vector<ContentValues> cVVector = new Vector<ContentValues>(myReviewList.size());


        /*for ( MovieReviews.ResultsEntity reviewItem : myReviewList) {

            Log.v(LOG_TAG, "MHB GSON the Author is  " + reviewItem.getAuthor());
            Log.v(LOG_TAG, "MHB GSON The review is " + reviewItem.getContent());
        }

        Log.v(LOG_TAG, "MHB GSON Total review pages  is " + movieReviews.getTotal_pages());
*/

        //myMovieList = myCinemas.getMovieList();
        //Vector<ContentValues> cVVector = new Vector<ContentValues>(myMovieList.size());

    }


    //OLD code DON'T REVIEW THIS...
    /*private void getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_MOVIE_ID = "id";
            final String OWM_ORIG_TITLE = "original_title";
            final String OWM_DESCRIPTION = "overview";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_POPULARITY = "popularity";
            final String OWM_VOTE_AVG="vote_average";
            final String OWM_VOTE_COUNT="vote_count";


            if ( Utility.isOnline(mContext) == false) {

                Log.v(LOG_TAG, "Hey There is no internet connection");
                return;
            }

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);


            String resultStrs[] = new String[movieArray.length()];
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            for(int i = 0; i < movieArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                int movie_id;
                String title;
                String description;
                String rel_date;
                String poster_path;
                double popularity;
                double vote_avg;
                int vote_count;

                *//*
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_FAVORITE + " BOOLEAN DEFAULT FALSE, " +
*//*


                        // Get the JSON object representing the day
                JSONObject singleMovie = movieArray.getJSONObject(i);

                movie_id = singleMovie.getInt(OWM_MOVIE_ID);
                title = singleMovie.getString(OWM_ORIG_TITLE);
                description = singleMovie.getString(OWM_DESCRIPTION);
                rel_date = singleMovie.getString(OWM_RELEASE_DATE);
                poster_path = singleMovie.getString(OWM_POSTER_PATH);
                popularity = singleMovie.getDouble(OWM_POPULARITY);
                vote_avg = singleMovie.getDouble(OWM_VOTE_AVG);
                vote_count = singleMovie.getInt(OWM_VOTE_COUNT);

                ContentValues movieValues = new ContentValues();



                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie_id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                movieValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, description);
                movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, rel_date);
                movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, poster_path);
                movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
                movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, vote_avg);
                movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, vote_count);

                cVVector.add(movieValues);

                //resultStrs[i] = title + " - " + description + " - " + rel_date + " - "+poster_path + "-" +popularity + "-" +vote_avg;
                resultStrs[i] = poster_path;
                Log.v(LOG_TAG, resultStrs[i]);
            }

        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }

        Log.v(LOG_TAG, "FetchMoviesData Complete. " + inserted + " Inserted");

        //return resultStrs;


        }

    *//** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() *//*
    // protected String[] doInBackground(String... urls) {
    protected Void doInBackground(Void... params) {


        try {
            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=");
            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?api_key=");

            //Uri movieURI = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM,"").build();
            Uri movieURI = Uri.parse(MOVIE_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM,BuildConfig.OPEN_MOVIE_API_KEY).build();

            //URL url = new URL(urls[0]);

            URL url = new URL(movieURI.toString());


            Log.v(LOG_TAG, "THE MOVIE URL is " +url);

            storeMovieDataToDb(movieURI.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();
            //Log.v("MHB_MOVIEDB", forecastJsonStr);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*//*
            try {
                getMovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
*//*

*//*
            String formatted_whether_str[] = null;
            try {
                formatted_whether_str = getMovieDataFromJson(forecastJsonStr);
            }catch (JSONException e) {
                e.printStackTrace();
            }

            if ( formatted_whether_str != null) {
                for (int j = 0; j < formatted_whether_str.length; j++) {
                    Log.v("MHB_FINAL", formatted_whether_str[j]);
                }
            }
*//*
        //return formatted_whether_str;
        return null;
    }*/
}
