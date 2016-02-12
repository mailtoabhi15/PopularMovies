package com.udacitynanodegreeapps.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

/**
 * Created by abhishek.dixit on 2/9/2016.
 */
public class MyMovie implements Parcelable{

    String title = null;
    String overview = null;
    String releaseDate = null;
    String posterPath = null;
    double voteAvg = 0;

   public MyMovie(String title, String Overview, String releaseDate, String posterPath, double voteAvg)
    {
        this.title = title;
        this.overview = Overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAvg = voteAvg;
    }

    public MyMovie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteAvg = in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destParcel, int flags) {
        destParcel.writeString(title);
        destParcel.writeString(overview);
        destParcel.writeString(releaseDate);
        destParcel.writeString(posterPath);
        destParcel.writeDouble(voteAvg);

    }

    public static final Parcelable.Creator<MyMovie> CREATOR = new Parcelable.Creator<MyMovie>(){
        @Override
        public MyMovie createFromParcel(Parcel sourceParcel) {
            return new MyMovie(sourceParcel);
        }

        @Override
        public MyMovie[] newArray(int size) {
            return new MyMovie[size];
        }
    };
}
