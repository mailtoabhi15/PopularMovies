package com.udacitynanodegreeapps.android.popularmovies;

import android.widget.ArrayAdapter;

/**
 * Created by abhishek.dixit on 2/9/2016.
 */
public class MyMovie {

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
}
