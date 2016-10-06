package com.udacitynanodegreeapps.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abhishek.dixit on 9/18/2016.
 */
public class MovieTrailer implements Parcelable {

    //    String id = null;
    String trailer_title = null;
    String trailer_source = null;


    public MovieTrailer(String trailer_title, String trailer_source) {
//        this.id = id;
        this.trailer_title = trailer_title;
        this.trailer_source = trailer_source;
    }


    public MovieTrailer(Parcel in) {

//        id = in.readString();
        trailer_title = in.readString();
        trailer_source = in.readString();

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

    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel sourceParcel) {
            return new MovieTrailer(sourceParcel);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };
}
