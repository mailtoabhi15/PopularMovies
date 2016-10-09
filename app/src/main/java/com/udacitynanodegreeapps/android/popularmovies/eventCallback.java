package com.udacitynanodegreeapps.android.popularmovies;

import java.util.ArrayList;

/**
 * Created by abhishek.dixit on 10/7/2016.
 */

public interface EventCallback {
    public void onEventReady(ArrayList<MoviesExtra> result);
}
