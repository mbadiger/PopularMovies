package com.example.saanu.popularmovies.data;


/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
   * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 102;
    public static final int MOVIE_TRAILER = 103;
    public static final int MOVIE_REVIEWS = 104;

    private static final SQLiteQueryBuilder movieQueryBuilder;
    private static final SQLiteQueryBuilder trailerQueryBuilder;
    private static final SQLiteQueryBuilder reviewQueryBuilder;

    static{
        movieQueryBuilder = new SQLiteQueryBuilder();
        movieQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME );

        trailerQueryBuilder = new SQLiteQueryBuilder();
        trailerQueryBuilder.setTables(MovieContract.TrailerEntry.TABLE_NAME );

        reviewQueryBuilder = new SQLiteQueryBuilder();
        reviewQueryBuilder.setTables(MovieContract.ReviewsEntry.TABLE_NAME );


    }

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_TRAILERS, MOVIE_TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS, MOVIE_REVIEWS);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            case MOVIE_TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;


            case MOVIE_REVIEWS:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_WITH_ID: {
                long _id = MovieContract.MovieEntry.getIdFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null,
                        null,
                        sortOrder);
                break;

            }
                case MOVIE_TRAILER: {
                    /*String movie_id = MovieContract.TrailerEntry.getActualMovieId(uri);
                    retCursor = mOpenHelper.getReadableDatabase().query(
                            MovieContract.TrailerEntry.TABLE_NAME,
                            projection,
                            MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{movie_id},
                            null,
                            null,
                            sortOrder);
*/
                    retCursor = mOpenHelper.getReadableDatabase().query(
                            MovieContract.TrailerEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;

                }

            case MOVIE_REVIEWS:{

                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            }
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return retCursor;
        }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
        @Override
        public Uri insert(Uri uri, ContentValues values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            Uri returnUri;

            switch (match) {
                case MOVIE: {
                    long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }

                case MOVIE_TRAILER: {
                    long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = MovieContract.TrailerEntry.buildTrailersUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }
                case MOVIE_REVIEWS: {
                    long _id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, values);
                    if ( _id > 0 )
                        returnUri = MovieContract.ReviewsEntry.buildReviewsUri(_id);
                    else
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    break;
                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsDeleted;
            // this makes delete all rows return the number of rows deleted
            if ( null == selection ) selection = "1";
            switch (match) {
                case MOVIE:
                    rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                    break;

                case MOVIE_TRAILER:
                    rowsDeleted = db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                    break;

                case MOVIE_REVIEWS:
                    rowsDeleted = db.delete(MovieContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            // Because a null deletes all rows
            if (rowsDeleted != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsDeleted;
        }


        @Override
        public int update(
                Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsUpdated;

            switch (match) {
                case MOVIE:
                    rowsUpdated = db.update(
                            MovieContract.MovieEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);
                    break;

                case MOVIE_TRAILER:
                    rowsUpdated = db.update(
                            MovieContract.TrailerEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);
                    break;

                case MOVIE_REVIEWS:
                    rowsUpdated = db.update(
                            MovieContract.ReviewsEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs);
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
        }

        @Override
        public int bulkInsert(Uri uri, ContentValues[] values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case MOVIE:
                    db.beginTransaction();
                    int returnCount = 0;
                    try {
                        for (ContentValues value : values) {

                            long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return returnCount;


                case MOVIE_TRAILER:
                    db.beginTransaction();
                    int trailerCount = 0;
                    try {
                        for (ContentValues value : values) {

                            long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                trailerCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return trailerCount;


                case MOVIE_REVIEWS:
                    db.beginTransaction();
                    int reviewsCount = 0;
                    try {
                        for (ContentValues value : values) {

                            long _id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                reviewsCount++;
                            }
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return reviewsCount;

                default:
                    return super.bulkInsert(uri, values);
            }
        }

        // You do not need to call this method. This is a method specifically to assist the testing
        // framework in running smoothly. You can read more at:
        // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
        @Override
        @TargetApi(11)
        public void shutdown() {
            mOpenHelper.close();
            super.shutdown();
        }
    }