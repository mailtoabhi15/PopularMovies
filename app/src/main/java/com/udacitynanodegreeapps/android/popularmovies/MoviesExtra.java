package com.udacitynanodegreeapps.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhishek.dixit on 10/7/2016.
 */

public class MoviesExtra implements Parcelable {

    String trailer_title = null;
    String trailer_source = null;
    String review_author = null;
    String review_content = null;
    String review_url = null;

    public MoviesExtra(){

    }
    public MoviesExtra(String trailer_title, String trailer_source,String review_author, String review_content, String review_url) {
//        this.id = id;
        this.trailer_title = trailer_title;
        this.trailer_source = trailer_source;
        this.review_author = review_author;
        this.review_content = review_content;
        this.review_url = review_url;

    }

    public String getTrailer_title() {
        return trailer_title;
    }

    public void setTrailer_title(String trailer_title) {
        this.trailer_title = trailer_title;
    }

    public String getTrailer_source() {
        return trailer_source;
    }

    public void setTrailer_source(String trailer_source) {
        this.trailer_source = trailer_source;
    }

    public String getReview_author() {
        return review_author;
    }

    public void setReview_author(String review_author) {
        this.review_author = review_author;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public String getReview_url() {
        return review_url;
    }

    public void setReview_url(String review_url) {
        this.review_url = review_url;
    }

    public MoviesExtra(Parcel in) {

//        id = in.readString();
        trailer_title = in.readString();
        trailer_source = in.readString();
        review_author = in.readString();
        review_content = in.readString();
        review_url = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destParcel, int flags) {
//        destParcel.writeString(id);
        destParcel.writeString(trailer_title);
        destParcel.writeString(trailer_source);
        destParcel.writeString(review_author);
        destParcel.writeString(review_content);
        destParcel.writeString(review_url);

    }

    public static final Parcelable.Creator<MoviesExtra> CREATOR = new Parcelable.Creator<MoviesExtra>() {
        @Override
        public MoviesExtra createFromParcel(Parcel sourceParcel) {
            return new MoviesExtra(sourceParcel);
        }

        @Override
        public MoviesExtra[] newArray(int size) {
            return new MoviesExtra[size];
        }
    };
}

