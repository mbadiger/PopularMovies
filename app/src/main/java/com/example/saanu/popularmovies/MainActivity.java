package com.example.saanu.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.saanu.popularmovies.data.MovieContract;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {


    static final String MOVIE_DETAIL_URI = "URI";
    String mSortorder;
    public static boolean mTwoPane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSortorder = Utility.getPreferredSortOrder(this);
        //Log.v("MHB_SORT_ORDER", "On Create " + mSortorder);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieActivityDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri moviedtlUri, String cur_movie_id) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MOVIE_DETAIL_URI, moviedtlUri);
            args.putString(MovieContract.MovieEntry.COLUMN_MOVIE_ID, cur_movie_id);

            MovieActivityDetailFragment fragment = new MovieActivityDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, new MovieActivityDetailFragment(), DETAILFRAGMENT_TAG)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, MovieActivityDetail.class)
                    .setData(moviedtlUri);

            intent.putExtra(MovieContract.MovieEntry.COLUMN_MOVIE_ID, cur_movie_id);
            startActivity(intent);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String moviesortorder = Utility.getPreferredSortOrder(this);

        if (moviesortorder != null && !moviesortorder.equals(mSortorder)) {
            MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            mainActivityFragment.sortOrderChanged();
        }

        mSortorder = moviesortorder;
    }
}
