package com.udacitynanodegreeapps.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhishek.dixit on 2/9/2016.
 */
public class MyMovie implements Parcelable{

    @SerializedName("original_title")String title = null;
    String overview = null;
    @SerializedName("release_date")String releaseDate = null;
    @SerializedName("poster_path")String posterPath = null;
    @SerializedName("vote_average")double voteAvg = 0;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(double voteAvg) {
        this.voteAvg = voteAvg;
    }
}
