package com.udacitynanodegreeapps.android.popmovretro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ArrayList<MyMovie> movieList;

    private Call<MovieModel> callMyMovieModel;

    private ImageAdapter mGridImageAdapter;

    private Retrofit retrofit;
    private MovieApiService myMovieService;

    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("moviedetails")){
            movieList = new ArrayList<MyMovie>();

        }
        else{
            movieList = savedInstanceState.getParcelableArrayList("moviedetails");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("moviedetails", movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myMovieService = retrofit.create(MovieApiService.class);

        mGridImageAdapter = new ImageAdapter(getActivity(),
                R.layout.grid_item_movies,
                R.id.grid_item_movies_imageview,
                movieList);


        GridView gridview = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridview.setAdapter(mGridImageAdapter);

         gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getActivity(),"Movies Pop",Toast.LENGTH_SHORT).show();
                MyMovie movieList = mGridImageAdapter.getItem(position);
                Intent detailAct = new Intent(getActivity(),DetailActivity.class);
                detailAct.putExtra("movielist",movieList);
                startActivity(detailAct);

            }
        });

        return rootView;
    }

    public void fetchMovie()
    {
//        //Dixit::Fetching Sort value from Shared Prefernces & default is "popularity.desc"
        SharedPreferences locPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortAlgo = locPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

        callMyMovieModel = myMovieService.getMovieList(sortAlgo,BuildConfig.OPEN_MOVIE_DB_API_KEY);

        callMyMovieModel.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {

                if (response.isSuccess()) {

                    MovieModel movieModel = response.body();
                    movieList = movieModel.getMovieList();
                    mGridImageAdapter.clear();
                    for (MyMovie movies : movieList)
                        mGridImageAdapter.addAll(movies);
                } else {
                    int statusCode = response.code();

                    Toast.makeText(getActivity(),"Error in Response with code: " + statusCode,Toast.LENGTH_SHORT).show();
                    ResponseBody errorBody = response.errorBody();
                }

            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.e("getMovieList threw", t.getMessage());
                Toast.makeText(getActivity(),"Please Check Network Connection",Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("Please check your internet connectivity");
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovie();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
