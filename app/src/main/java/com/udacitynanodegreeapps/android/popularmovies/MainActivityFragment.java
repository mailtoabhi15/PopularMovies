package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private ArrayList<MyMovie> movieList;

    private ImageAdapter mGridImageAdapter;

    //Dixit:start:Added in lesson-5.40(2 Pane Ui)-Handling List Item Click
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface ListItemClickCallback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(MyMovie movieListItem);
    }
    //Dixit:end

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

        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

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
                MyMovie movieListItem = mGridImageAdapter.getItem(position);
                //Dixit:start:updated in lesson-5.40(2 Pane Ui)-Handling List Item Click
//                Intent detailAct = new Intent(getActivity(),DetailActivity.class);
//                detailAct.putExtra("movielist",movieListItem);
//                startActivity(detailAct);
                ((ListItemClickCallback) getActivity()).onItemSelected(movieListItem);
                //Dixit:End

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    public void updateMovie()
    {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity(), mGridImageAdapter);

        //Dixit::Fetching Sort value from Shared Prefernces & default is "popularity.desc"
        SharedPreferences locPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortAlgo = locPref.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_default));

        if(sortAlgo.equals("favourite"))
        {
            //show favourites
            mGridImageAdapter.clear();
            SharedPreferences mfavPrefs = getActivity().getSharedPreferences("fav_movie", Context.MODE_PRIVATE);
            Map<String, ?> keys = mfavPrefs.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                String id = entry.getKey();
                String jsonFav = entry.getValue().toString();
//                MyMovie favMovie = new MyMovie(id,poster_path);
                Gson gson = new Gson();
                //Fetch jsonFavorites from shared preferences
                Type type = new TypeToken<MyMovie>(){}.getType();
                //Tells gson that you want a List<MovieData>
                MyMovie favMovie = gson.fromJson(jsonFav, type);
                mGridImageAdapter.add(favMovie);

            }


        }
        else
            movieTask.execute(sortAlgo);
    }





}
