package com.udacitynanodegreeapps.android.popularmovies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

/**
 * Created by abhishek.dixit on 2/9/2016.
 */
public class MyMovie implements Parcelable{

    String id = null;
    String title = null;
    String overview = null;
    String releaseDate = null;
    String posterPath = null;
    String backdropPath = null;
    double voteAvg = 0;

//    private Bitmap posterImage = null;

    public MyMovie(String id,String posterPath)
    {
        this.id = id;
        this.posterPath = posterPath;
    }

   public MyMovie(String id, String title, String Overview, String releaseDate, String posterPath, String backdropPath, double voteAvg)
    {
        this.id = id;
        this.title = title;
        this.overview = Overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.voteAvg = voteAvg;
    }

//    public Bitmap getPosterImage() {
//        return posterImage;
//    }
//
//    public void setPosterImage(Bitmap posterImage) {
//        this.posterImage = posterImage;
//    }

    public MyMovie(Parcel in) {

        id = in.readString();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        voteAvg = in.readDouble();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destParcel, int flags) {
        destParcel.writeString(id);
        destParcel.writeString(title);
        destParcel.writeString(overview);
        destParcel.writeString(releaseDate);
        destParcel.writeString(posterPath);
        destParcel.writeString(backdropPath);
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
