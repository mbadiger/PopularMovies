package com.example.saanu.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saanu.popularmovies.data.MovieContract;

/**
 * Created by Saanu_mac on 2/23/16.
 */
public class TrailerAdapter extends CursorAdapter {

    Context mContext;
    public static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    public TrailerAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
        this.mContext = context;
        //Log.v("MHB_IN_POSTERADPTER", "in constructor");
    }


    class ViewHolder {
        TextView myTrailerTxt;

        ViewHolder(View view){
            myTrailerTxt = (TextView)view.findViewById(R.id.trailerText);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item_list, parent, false);

        TrailerAdapter.ViewHolder viewHolder = new TrailerAdapter.ViewHolder(view);
        view.setTag(viewHolder);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String imageURL;

        TrailerAdapter.ViewHolder   viewHolder = (TrailerAdapter.ViewHolder)view.getTag();

        int rowno = cursor.getPosition();


        String movie_key = cursor.getString(cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY));
/*
        Log.v(LOG_TAG, "NUmber of rows in cursor is in adapter " +cursor.getCount());

        Log.v(LOG_TAG, "The row position is " + rowno);
        Log.v(LOG_TAG, "The movie key is  " + movie_key);*/

        String trailerTxt = "Trailer " + (rowno +1);


        viewHolder.myTrailerTxt.setText(trailerTxt);

    }
}
