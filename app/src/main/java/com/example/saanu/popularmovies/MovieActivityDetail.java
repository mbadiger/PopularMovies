package com.example.saanu.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MovieActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        /*if (savedInstanceState == null){

            Bundle args = new Bundle();
                   args.putParcelable(MainActivity.MOVIE_DETAIL_URI, getIntent().getData());

            MovieActivityDetailFragment dtlfragment = new MovieActivityDetailFragment();
            dtlfragment.setArguments(args);

            //getSupportFragmentManager().beginTransaction()
              //      .add(R.id.movie_detail_container, dtlfragment)
                //    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, dtlfragment)
                    .commit();
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_activity_detail, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
