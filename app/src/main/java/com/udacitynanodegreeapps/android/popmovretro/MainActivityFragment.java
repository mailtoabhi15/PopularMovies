package com.udacitynanodegreeapps.android.popmovretro;

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
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("moviedeails")){
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

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();

    }

    public void updateMovie()
    {
//        //Dixit::Fetching Sort value from Shared Prefernces & default is "popularity.desc"
        SharedPreferences locPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortAlgo = locPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieApiService myMovieService = retrofit.create(MovieApiService.class);

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

                    ResponseBody errorBody = response.errorBody();
                }

            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.e("getMovieList threw", t.getMessage());
            }
        });

    }




    //Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.
    public class FetchMovieTask extends AsyncTask<String,Void,MyMovie[]>
    {
//       BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";

//        final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
        final String SORT = "sort_by";
        final String APPID = "api_key";

        private MyMovie [] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            final String POSTER_PATH = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String TITLE = "original_title";
            final String VOTE_AVG = "vote_average";

            JSONObject movieJson = new JSONObject(movieJsonStr);

            JSONArray movieArray = movieJson.getJSONArray("results");

            MyMovie[] movieList = new MyMovie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++)
            {

                JSONObject movieObject = movieArray.getJSONObject(i);

                 movieList[i] = new MyMovie(movieObject.getString(TITLE),
                        movieObject.getString(OVERVIEW),
                        movieObject.getString(RELEASE_DATE),
                        movieObject.getString(POSTER_PATH),
                        movieObject.getDouble(VOTE_AVG));

            }

            return movieList;
        }

        @Override
        protected MyMovie[] doInBackground(String... params) {
            if(params.length==0)
            {
                return null;
            }

            //Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.

            //Dixit: These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {

                Uri movieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT, params[0])
                        .appendQueryParameter(APPID, BuildConfig.OPEN_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(movieUri.toString());
                Log.v(LOG_TAG, "URL: " + url);

                urlConnection =(HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader((inputStream)));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();

                Log.v(LOG_TAG,"Movie JSON String: " + movieJsonStr);


            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error",e);
                movieJsonStr = null;
            }

            try{
                //Dixit: to parse(as required) response data from server we call below function
                if(movieJsonStr !=null)
                    return getMovieDataFromJson(movieJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(MyMovie[] result) {
            if( result == null)
                Toast.makeText(getActivity(),"Please Check Network Connection",Toast.LENGTH_SHORT).show();
            else
            {
                mGridImageAdapter.clear();
                for(MyMovie movieList : result)
                    mGridImageAdapter.addAll(movieList);
            }
        }
    }



}
