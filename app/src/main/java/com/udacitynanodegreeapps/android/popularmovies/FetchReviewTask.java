package com.udacitynanodegreeapps.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abhishek.dixit on 9/18/2016.
 */
public class FetchReviewTask extends AsyncTask<String,Void,MovieReview[]> {


    public static final String LOG_TAG = FetchReviewTask.class.getSimpleName();


    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie?";

    final String APPID = "api_key";
    final String APEEND_TO_RESPONSE = "append_to_response";
    final String EXTRA_PARAMS ="reviews";

    private MovieReview[] getReviewDataFromJson(String reviewJsonStr)
            throws JSONException {

        final String REVIEWS = "reviews";
        final String REVIEWS_RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        JSONObject reviewJsonObject = reviewJson.getJSONObject(REVIEWS);

        JSONArray reviewArray = reviewJsonObject.getJSONArray(REVIEWS_RESULTS);

        MovieReview[] reviewList = new MovieReview[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++)
        {

            JSONObject trailerObject = reviewArray.getJSONObject(i);

            reviewList[i] = new MovieReview(
                    trailerObject.getString(AUTHOR),
                    trailerObject.getString(CONTENT),
                    trailerObject.getString(URL));

        }

        return reviewList;
    }

    @Override
    protected MovieReview[] doInBackground(String... params) {

        if(params.length==0)
        {
            return null;
        }

        //Note:Below Code for Fetching data is being rerenced form the Sunshine App Course.

        //Dixit: These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

//            StethoURLConnectionManager stethoManager;

        // Will contain the raw JSON response as a string.
        String reviewJsonStr = null;

        try {

            Uri movieUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(APPID, BuildConfig.OPEN_MOVIE_DB_API_KEY)
                    .appendQueryParameter(APEEND_TO_RESPONSE, EXTRA_PARAMS)
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
                reviewJsonStr = null;
            }
            reviewJsonStr = buffer.toString();

            Log.v(LOG_TAG,"Review JSON String: " + reviewJsonStr);


        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error",e);
            reviewJsonStr = null;
        }

        try{
            //Dixit: to parse(as required) response data from server we call below function
            if(reviewJsonStr !=null)
                return getReviewDataFromJson(reviewJsonStr);
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


}
