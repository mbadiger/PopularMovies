package com.example.saanu.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.saanu.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;


public class MoviePosterAdapter extends CursorAdapter {

    Context mContext;
    String baseURL = "http://image.tmdb.org/t/p/w342/";

    public MoviePosterAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
        this.mContext = context;
        //Log.v("MHB_IN_POSTERADPTER", "in constructor");
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    class ViewHolder {
        ImageView myMovie;

        ViewHolder(View view){
            myMovie = (ImageView)view.findViewById(R.id.list_item_icon);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //Log.v("MHB_IN_POSTERADPTER", "in newView");

        View view = LayoutInflater.from(context).inflate(R.layout.list_grid_item, parent, false);

        MoviePosterAdapter.ViewHolder viewHolder = new MoviePosterAdapter.ViewHolder(view);
        view.setTag(viewHolder);

        /*LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_grid_item,parent,false);
        MoviePosterAdapter.ViewHolder holder = new ViewHolder(row);
        row.setTag(holder);
*/
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String imageURL;

        MoviePosterAdapter.ViewHolder   viewHolder = (MoviePosterAdapter.ViewHolder)view.getTag();

        int movieImagesCol = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        String movieThumbnail = cursor.getString(movieImagesCol);

        imageURL = baseURL + movieThumbnail;

        //Log.v("MHB_IN_POSTERADPTER", "URL is " +imageURL);
        //holder.myMovie.setImageResource(R.mipmap.ic_launcher);

        Picasso.with(mContext).load(imageURL).into(viewHolder.myMovie);
    }
}
