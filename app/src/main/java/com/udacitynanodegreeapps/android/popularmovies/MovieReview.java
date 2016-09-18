package com.udacitynanodegreeapps.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhishek.dixit on 9/18/2016.
 */
public class MovieReview implements Parcelable {

    String review_author = null;
    String review_content = null;
    String review_url = null;


    public MovieReview(String review_author, String review_content, String review_url)    {

        this.review_author = review_author;
        this.review_content = review_content;
        this.review_url = review_url;

    }

    public MovieReview(Parcel in) {

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
        destParcel.writeString(review_author);
        destParcel.writeString(review_content);
        destParcel.writeString(review_url);

    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>(){
        @Override
        public MovieReview createFromParcel(Parcel sourceParcel) {
            return new MovieReview(sourceParcel);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
