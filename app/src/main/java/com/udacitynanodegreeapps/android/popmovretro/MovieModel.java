package com.udacitynanodegreeapps.android.popmovretro;

import java.util.ArrayList;

/**
 * Created by abhishek.dixit on 2/18/2016.
 */
public class MovieModel {

    private Integer  page;

    private ArrayList<MyMovie> results ;

    public ArrayList<MyMovie> getMovieList() {
        return results;
    }

    public void setMovieList(ArrayList<MyMovie> movieList) {
        this.results = results;
    }

    public MovieModel(ArrayList<MyMovie> movieList) {
        this.results = results;
    }

    public MovieModel() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
