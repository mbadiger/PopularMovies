package com.example.saanu.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.saanu.popularmovies.data.MovieContract;

/**
 * Created by Saanu_mac on 3/1/16.
 */
public class ReviewsAdapter extends CursorAdapter {

    Context mContext;
    public static final String LOG_TAG = ReviewsAdapter.class.getSimpleName();

    public ReviewsAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);
        this.mContext = context;

    }


    class ViewHolder {
        TextView authorName;
        TextView reviewTxt;


        ViewHolder(View view) {
            authorName = (TextView) view.findViewById(R.id.author);
            reviewTxt = (TextView) view.findViewById(R.id.reviewtxt);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.review_item_list, parent, false);

        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(view);
        view.setTag(viewHolder);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        String imageURL;

        ReviewsAdapter.ViewHolder viewHolder = (ReviewsAdapter.ViewHolder) view.getTag();

        int rowno = cursor.getPosition();

        String author = "Author: ";

        author = author + cursor.getString(cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_AUTHOR));

        //Log.v(LOG_TAG, "NUmber of rows in cursor is in adapter " + cursor.getCount());

        //Log.v(LOG_TAG, "The row position is " + rowno);
        // Log.v(LOG_TAG, "The author name is  " + author);


        String review_content = cursor.getString(cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_CONTENT));
        //Log.v(LOG_TAG, "The review content is " + review_content);

        viewHolder.authorName.setText(author);
        //viewHolder.reviewTxt.setText(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_CONTENT)));
        viewHolder.reviewTxt.setText(review_content);

    }

}
