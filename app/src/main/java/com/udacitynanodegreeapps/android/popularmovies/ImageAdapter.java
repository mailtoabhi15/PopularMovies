package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by abhishek.dixit on 2/3/2016.
 */
public class ImageAdapter extends ArrayAdapter<MyMovie> {


    public ImageAdapter(Context context, int resource, int textViewResourceId, List<MyMovie> objects) {
        super(context, resource, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //to get the object from the ArrayAdapter at the required position
        MyMovie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movies, parent, false);
        }

        ImageView gridImageView = (ImageView) convertView.findViewById(R.id.grid_item_movies_imageview);
//        gridImageView.setLayoutParams(new GridView.LayoutParams(185, 185));
//        gridImageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //Controls how the image should be resized or moved to match the size of this ImageView.
//        gridImageView.setPadding(8, 8, 8, 8);
//        //gridImageView.setAdjustViewBounds(true); //to adjust the bounds of this view to preserve the original aspect ratio of the drawable.
//        gridImageView.setImageResource(nImage);


        String base_uri = "http://image.tmdb.org/t/p/";
        String size = "w185";

        String movieUri = base_uri + size + "/" + movie.posterPath;

        Log.v("ImageAdapter ", "Movie Uri: " + movieUri);

        Picasso.with(getContext())
                .load(movieUri)
                .placeholder(R.drawable.sample_0)
                .error(R.drawable.sample_7)
                .fit()
                .into(gridImageView);

//        try {
//            movie.setPosterImage(Picasso.with(getContext())
//                                .load(movieUri)
//                                .placeholder(R.drawable.sample_0)
//                                .error(R.drawable.sample_7).get());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return convertView;
    }
}
