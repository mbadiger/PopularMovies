package com.example.saanu.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saanu.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieActivityDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = MovieActivityDetailFragment.class.getSimpleName();
    private static final int MOVIE_DETAIL_LOADER = 1;
    private static final int MOVIE_TRAILER_LOADER = 2;
    private static final int MOVIE_REVIEWS_LOADER = 3;

    private Uri mUri;
    private String cur_movie_id;

    ListView trailerListView;
    TrailerAdapter trailerAdapter;

    ListView reviewsListView;
    ReviewsAdapter reviewsAdapter;


    static final String[] DETAILS_MOVIE_COLUMNS = {
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


    static final String[] DETAILS_TRAILER_COLUMNS = {
            MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry._ID,
            MovieContract.TrailerEntry.COLUMN_MOVIE_ID,
            MovieContract.TrailerEntry.COLUMN_TRAILER_ID,
            MovieContract.TrailerEntry.COLUMN_ISO,
            MovieContract.TrailerEntry.COLUMN_KEY,
            MovieContract.TrailerEntry.COLUMN_NAME,
            MovieContract.TrailerEntry.COLUMN_SITE,
            MovieContract.TrailerEntry.COLUMN_SIZE,
            MovieContract.TrailerEntry.COLUMN_TYPE
    };


    static final String[] DETAILS_REVIEWS_COLUMNS = {
            MovieContract.ReviewsEntry.TABLE_NAME + "." + MovieContract.ReviewsEntry._ID,
            MovieContract.ReviewsEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewsEntry.COLUMN_REVIEW_ID,
            MovieContract.ReviewsEntry.COLUMN_AUTHOR,
            MovieContract.ReviewsEntry.COLUMN_CONTENT,
            MovieContract.ReviewsEntry.COLUMN_URL
    };


    ImageView mIconView;
    TextView mMovieyear;
    TextView mMovielength;
    TextView mMovierating;
    ImageButton mBtnfavrt;
    TextView mDesc;

    ImageButton mBtnfavrt2;

    public MovieActivityDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MainActivity.MOVIE_DETAIL_URI);
            cur_movie_id = arguments.getString(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        } else {

            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }

            mUri = intent.getData();
            cur_movie_id = intent.getStringExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

            Log.v(LOG_TAG, "MHB the current movie id is " + cur_movie_id);
        }

        //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg


        mIconView = (ImageView) rootView.findViewById(R.id.detail_thumbnail);
        mMovieyear = (TextView) rootView.findViewById(R.id.movie_year);
        mMovielength = (TextView) rootView.findViewById(R.id.movie_length);
        mMovierating = (TextView) rootView.findViewById(R.id.movie_rating);
        mBtnfavrt = (ImageButton) rootView.findViewById(R.id.btn_favrt);
        mDesc = (TextView) rootView.findViewById(R.id.movie_desc);


        //Set the on click listener for favorite button
        mBtnfavrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Favorite button is clicked", Toast.LENGTH_SHORT).show();
                markMovieAsFavorite();
            }
        });

        trailerAdapter = new TrailerAdapter(getActivity(), null, 0);
        trailerListView = (ListView) rootView.findViewById(R.id.listview_trailers);
        trailerListView.setAdapter(trailerAdapter);


        //Set the item click listener for trailerListView

        trailerListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {

                    String trailer_key = cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY));
//                    Log.v(LOG_TAG, "The Trailer key that is clicked is " + trailer_key);

//                    Log.v(LOG_TAG, "The youtube trailer link is " + Uri.parse("http://www.youtube.com/watch?v=" + trailer_key));
                    //Launch the trailer
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer_key)));
                }
            }
        });


        reviewsAdapter = new ReviewsAdapter(getActivity(), null, 0);
        reviewsListView = (ListView) rootView.findViewById(R.id.listview_reviews);
        reviewsListView.setAdapter(reviewsAdapter);

        //ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_thumbnail);

        ListView lv = (ListView) rootView.findViewById(R.id.listview_reviews);  // your listview inside scrollview
        lv.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        //Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);
        return (rootView);
    }

    private void markMovieAsFavorite() {


        if (mUri != null) {

            //First get the record so that we can find out the value of is_favorite field
            Cursor movieCursor = getActivity().getContentResolver().query(mUri, DETAILS_MOVIE_COLUMNS, null, null, null);

            if (movieCursor != null && movieCursor.moveToFirst()) {

                String current_movie_id = movieCursor.getString(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                boolean is_favorite = (movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE)) > 0);

                //              Log.v(LOG_TAG, "The value of is_favorite is " + is_favorite +"for the movie id " +current_movie_id);


                // Defines an object to contain the updated values
                ContentValues mUpdateValues = new ContentValues();

                String mSelectionClause = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = {current_movie_id};


                // Defines a variable to contain the number of updated rows
                int mRowsUpdated = 0;

    /*
    * Sets the updated value and updates the selected words.
    */
                //Note: we are setting/resetting value of favorite
                mUpdateValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, !is_favorite);

                mRowsUpdated = getActivity().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, mUpdateValues, mSelectionClause, mSelectionArgs);
                //            Log.v(LOG_TAG, "Number of rows updated is " + mRowsUpdated);

                int imgsrc = R.drawable.ic_favorite_outline;

                if (!is_favorite) {
                    imgsrc = R.drawable.ic_favorite_red_big;
                }

                mBtnfavrt.setImageResource(imgsrc);
            }

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(MOVIE_REVIEWS_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {

            if (id == MOVIE_DETAIL_LOADER) {
                String curMovieid = mUri.getQueryParameter(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

                //Log.v(LOG_TAG, "MHB_MOVIE The movie id is " +curMovieid);

                return new CursorLoader(getActivity(), mUri, DETAILS_MOVIE_COLUMNS, null, null, null);
            } else if (id == MOVIE_TRAILER_LOADER) {

                if (cur_movie_id != null) {
                    String selection = MovieContract.TrailerEntry.COLUMN_MOVIE_ID + "=?";
                    String[] selectionargs = {cur_movie_id};

                    return new CursorLoader(getActivity(), MovieContract.TrailerEntry.CONTENT_URI, DETAILS_TRAILER_COLUMNS, selection, selectionargs, null);
                }
            } else if (id == MOVIE_REVIEWS_LOADER) {

                if (cur_movie_id != null) {
                    String selection = MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + "=?";
                    String[] selectionargs = {cur_movie_id};

                    return new CursorLoader(getActivity(), MovieContract.ReviewsEntry.CONTENT_URI, DETAILS_REVIEWS_COLUMNS, selection, selectionargs, null);
                }
            }
        }

        return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        String baseURL = "http://image.tmdb.org/t/p/w342/";
        String imageURL;

        //TextView mMovielength;

        //Button mBtnfavrt;


        //Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);

        int loaderId = loader.getId();


        if (loaderId == MOVIE_DETAIL_LOADER) {


            if (cursor != null && cursor.moveToFirst()) {
                int movieImagesCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                String movieThumbnail = cursor.getString(movieImagesCol);

                imageURL = baseURL + movieThumbnail;

                //Log.v(LOG_TAG, "URL is " + imageURL);
                //holder.myMovie.setImageResource(R.mipmap.ic_launcher);

                Picasso.with(getActivity()).load(imageURL).into(mIconView);

                String release_date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                String[] rel_temp = release_date.split("-");

                mMovieyear.setText(rel_temp[0]);
                String rating = String.valueOf(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));


                //String voting_count = String.valueOf(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));


                mMovierating.setText(rating + "/" + "10");

                String desc = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION));


                mDesc.setText(desc);

                boolean is_favorite = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE)) > 0;
                //          Log.v(LOG_TAG, "In onload finished is_favorit is " + is_favorite);

                int imgsrc = R.drawable.ic_favorite_outline;

                if (is_favorite) {
                    imgsrc = R.drawable.ic_favorite_red_big;
                }

                mBtnfavrt.setImageResource(imgsrc);

//                Log.v(LOG_TAG, "The Description is:  " + desc);
            }
        } else if (loaderId == MOVIE_TRAILER_LOADER) {

            //          Log.v(LOG_TAG, "MHB in trailer loader");
            if (cursor != null && cursor.moveToFirst()) {

                trailerAdapter.swapCursor(cursor);

                String movie_id = cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_MOVIE_ID));
                setListViewHeightBasedOnItems(trailerAdapter);

//                Log.v(LOG_TAG, "NUmber of rows in cursor is " + cursor.getCount());
//                Log.v(LOG_TAG, "The movie id in onpostload is " + movie_id);

            }
        } else if (loaderId == MOVIE_REVIEWS_LOADER) {

//            Log.v(LOG_TAG, "MHB in reviews loader");
            if (cursor != null && cursor.moveToFirst()) {

                reviewsAdapter.swapCursor(cursor);

                String author = cursor.getString(cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_AUTHOR));
                setListViewHeightBasedOnItems(reviewsAdapter);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public boolean setListViewHeightBasedOnItems(CursorAdapter cursorAdapter) {


        if (cursorAdapter instanceof TrailerAdapter) {

            if (trailerAdapter != null) {

                int numberOfItems = trailerAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = trailerAdapter.getView(itemPos, null, trailerListView);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = trailerListView.getDividerHeight() *
                        (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = trailerListView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                trailerListView.setLayoutParams(params);
                trailerListView.requestLayout();

                return true;

            }
        } else {

            if (reviewsAdapter != null) {

                int numberOfItems = reviewsAdapter.getCount();

                //              Log.v(LOG_TAG, "MHB In item adjust The number of Items " + numberOfItems);

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View item = reviewsAdapter.getView(itemPos, null, reviewsListView);
                    item.measure(0, 0);
                    totalItemsHeight += item.getMeasuredHeight();
                }

//                Log.v(LOG_TAG, "MHB In item adjust  The total item is " + totalItemsHeight);
                // Get total height of all item dividers.
                int totalDividersHeight = reviewsListView.getDividerHeight() *
                        (numberOfItems - 1);

                if (totalDividersHeight == 0) {
                    totalDividersHeight = numberOfItems;
                }


                //              Log.v(LOG_TAG, "MHB In item adjust The review item count is " + totalDividersHeight);

                // Set list height.
                ViewGroup.LayoutParams params = reviewsListView.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;

                //            Log.v(LOG_TAG, "MHB In item adjust the review params.heigher " + params.height);
                reviewsListView.setLayoutParams(params);
                reviewsListView.requestLayout();

                return true;

            }
        }

        return false;

    }


}