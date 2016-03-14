package com.example.saanu.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.saanu.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String movieList[];
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int MOVIE_LOADER = 0;
    static boolean firsttime = true;

    GridView gridview;

    MoviePosterAdapter moviePosterAdapter;

    int mitemSelected = -1;

    Callback mListener;
    static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)

            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_DESCRIPTION,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_FAVORITE
    };


    public MainActivityFragment() {


    }


    @Override
    public void onStart() {
        super.onStart();

        if (firsttime) {
            FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity());
            fetchMoviesData.execute();
            firsttime = false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Callback) activity;
            if (firsttime) {
                FetchMoviesData fetchMoviesData = new FetchMoviesData(getActivity());
                fetchMoviesData.execute();
                firsttime = false;
            }


        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Callback");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(MOVIE_LOADER, null, this);

/*
        Log.v(LOG_TAG, "MHB the mitemselected is " + mitemSelected);
        Log.v(LOG_TAG, "MHB the mtwopane is " + MainActivity.mTwoPane);
*/

        if (moviePosterAdapter.getCursor() != null) {

            if (mitemSelected == -1 && MainActivity.mTwoPane && gridview != null) {
                Log.v(LOG_TAG, "MHB in two pane trying to trigger the click action");
                gridview.performItemClick(null, 0, gridview.getFirstVisiblePosition());

                //gridview.performItemClick(
                //      gridview.getAdapter().getView(0, null, null), 0, 0);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //movieDataList = new ArrayList<String>();

        setHasOptionsMenu(true);
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);


        if (Utility.isOnline(getActivity()) == false) {

            Log.v(LOG_TAG, "Hey There is no internet connection");
            Toast.makeText(getActivity(), "Hey! There is no intenet connection, Please check your network connection!",
                    Toast.LENGTH_LONG).show();

        }

        moviePosterAdapter = new MoviePosterAdapter(getActivity(), null, 0);
        gridview = (GridView) rootview.findViewById(R.id.gridView);
        gridview.setAdapter(moviePosterAdapter);

        //ArrayList<>


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent intent = new Intent(getActivity(), MovieActivityDetail.class);
                //startActivity(intent);

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {

                    mitemSelected = position;
                    Uri moviedetailuri = MovieContract.MovieEntry.buildMovieUri(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));


                    String cur_movie_id = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));

                    //Log.v("MHB_IN_ITEM", "The current of movie id is " + cur_movie_id);

                    FetchTrailerReviews fetchTrailerReviews = new FetchTrailerReviews(getActivity(), cur_movie_id);
                    fetchTrailerReviews.execute();
                    mListener.onItemSelected(moviedetailuri, cur_movie_id);

                }

            }
        });

        return (rootview);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrderSetting = Utility.getPreferredSortOrder(getActivity());
        //Log.v(LOG_TAG, "The sortorderSetting is " +sortOrderSetting);

        String sortOrder;
        if (sortOrderSetting.equals(getString(R.string.pref_sortby_value_popular))) {
            sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " ASC";
        } else if (sortOrderSetting.equals(getString(R.string.pref_sortby_value_high_rated))) {
            //sort by rating
            sortOrder = MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " ASC";
        } else {
            sortOrder = MovieContract.MovieEntry.COLUMN_FAVORITE + " DESC";
        }


        //Log.v(LOG_TAG, "The sort order is " + sortOrder);

        //String sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " ASC";

        return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, MainActivityFragment.FORECAST_COLUMNS, null, null, sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Log.v(LOG_TAG, "Loaded cursor data " + data.getCount() + "rows fetched");
        moviePosterAdapter.swapCursor(data);
        //gridview.setAdapter(moviePosterAdapter);
        //moviePosterAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviePosterAdapter.swapCursor(null);
    }

    public void sortOrderChanged() {
        //Log.v(LOG_TAG, "In sortOrderchanged");
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        //moviePosterAdapter.notifyDataSetChanged();
    }


    /*private class GetMovieData extends AsyncTask<String, Void, String[]> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        *//** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() *//*
       // protected String[] doInBackground(String... urls) {
            protected String[]  doInBackground(String... urls) {


            try {
            URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=8e2a01ba363daf749ce201cbb1a61290");
            //URL url = new URL(urls[0]);


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
                Log.v("MHB_MOVIEDB", forecastJsonStr );
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            return formatted_whether_str;
        }


        *//** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() *//*
        protected void onPostExecute(String formattedMovieStr[]) {

            if (formattedMovieStr != null) {
                System.arraycopy(formattedMovieStr, 0, movieList, 0, formattedMovieStr.length);



                for (String s : movieList) {
                    Log.v("MHB_POSTEXEC", "Forecast entry: " + s);
                }
                //      forecastAdapter.clear();
                //    forecastAdapter.addAll(Arrays.asList(formattedWheather));
            }


        }

        private String[] getMovieDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_ORIG_TITLE = "original_title";
            final String OWM_DESCRIPTION = "overview";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_POPULARITY = "popularity";
            final String OWM_VOTE_AVG="vote_average";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);


            String resultStrs[] = new String[movieArray.length()];
            for(int i = 0; i < movieArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String title;
                String description;
                String rel_date;
                String poster_path;
                String popularity;
                String vote_avg;

                // Get the JSON object representing the day
                JSONObject singleMovie = movieArray.getJSONObject(i);

                title = singleMovie.getString(OWM_ORIG_TITLE);
                description = singleMovie.getString(OWM_DESCRIPTION);
                rel_date = singleMovie.getString(OWM_RELEASE_DATE);
                poster_path = singleMovie.getString(OWM_POSTER_PATH);
                popularity = singleMovie.getString(OWM_POPULARITY);
                vote_avg = singleMovie.getString(OWM_VOTE_AVG);

                //resultStrs[i] = title + " - " + description + " - " + rel_date + " - "+poster_path + "-" +popularity + "-" +vote_avg;
                resultStrs[i] = poster_path;
            }

            movieList = new String[resultStrs.length];
            System.arraycopy(resultStrs,0,movieList,0,resultStrs.length);
            for (String s : resultStrs) {
                Log.v("MHB2", "Forecast entry: " + s);
            }
            return resultStrs;

        }

    }

*/

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri, String cur_movie_id);
    }

}
