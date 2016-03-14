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


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.saanu.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_REVIEWS = "reviews";

    /* Inner class that defines the table contents of the movies table */
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        //Movies table columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_FAVORITE = "is_favorite";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getPosterImagePath(Uri uri) {

            String imageURL = uri.getQueryParameter(COLUMN_POSTER_PATH);

            return imageURL;
        }

        /**
         * Parse the ID of a record, or return -1 instead
         *
         * @param uri The Uri of the record
         * @return The Id of the record or -1 if this doesn't apply
         */
        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

    }


    /* Inner class that defines the table contents of the movies table */
    public static final class TrailerEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailers";

        //Trailer table columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_ISO = "iso_639_1";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;


        public static Uri buildTrailersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildTrailersMovieidUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /**
         * Parse the ID of a record, or return -1 instead
         *
         * @param uri The Uri of the record
         * @return The Id of the record or -1 if this doesn't apply
         */
        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static String getActualMovieId(Uri uri) {

            String movie_id = uri.getQueryParameter(COLUMN_MOVIE_ID);

            return movie_id;
        }
    }


    /* Inner class that defines the table contents of the movies table */
    public static final class ReviewsEntry implements BaseColumns {

        public static final String TABLE_NAME = "reviews";

        //Reviews table columns
        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        //  public static final String COLUMN_TOTAL_PAGES = "total_pages";
        // public static final String COLUMN_TOTAL_RESULTS = "total_results";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;


        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        /**
         * Parse the ID of a record, or return -1 instead
         *
         * @param uri The Uri of the record
         * @return The Id of the record or -1 if this doesn't apply
         */
        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }


    }

}