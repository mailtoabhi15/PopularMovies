package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


import static com.udacitynanodegreeapps.android.popularmovies.DetailActivityFragment.LIST_MOVIES_INDEX;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Dixit: Added for handling Single pain layout(normal case)
        if (savedInstanceState == null) {
            Intent intent = this.getIntent();
            if (intent != null && intent.hasExtra(LIST_MOVIES_INDEX)) {

                final MyMovie movieListItem = intent.getParcelableExtra(LIST_MOVIES_INDEX);

                Bundle args = new Bundle();
                args.putParcelable(LIST_MOVIES_INDEX, movieListItem);
                //now the bundled arguments are set & passed on by call to empty fragmant constructor
                //& once teh fragmen tis intialised we can't chnage teh arguments , we can only read form them(getArguments())
                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container, fragment)
                        .commit();
            }

        }
    }
//    public  void setFavourite(View view){
//
//        CheckBox favourite = (CheckBox) view.findViewById(R.id.chkbox_favourite);
//        if(favourite.isChecked()) {
//            Log.i("Detail:", "Removing Favourite");
//            favourite.setText("Remove From Favourites");
////            favourite.setChecked(false);
////            favourite.setButtonDrawable(android.R.drawable.btn_star_big_on);
//        }
//        else {
//            favourite.setText("Add to Favourites");
////            favourite.setChecked(true);
////            favourite.setButtonDrawable(android.R.drawable.btn_star_big_off);
//        }
////       SharedPreferences.Editor setFav = this.getActivity().getSharedPreferences("favourite", Context.MODE_PRIVATE).edit();
////       setFav.putBoolean("fav",true);
////        //setFav.
////        setFav.commit();
//    }

}
