package com.udacitynanodegreeapps.android.popularmovies;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by abhishek.dixit on 2/18/2016.
 */
public interface MovieApiService {

   @GET("discover/movie")
    Call<MovieModel> getMovieList(
           @Query("sort_by") String sortalgo,
           @Query("api_key") String appid
   );

}
