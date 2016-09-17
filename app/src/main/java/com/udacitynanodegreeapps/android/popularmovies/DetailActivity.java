package com.udacitynanodegreeapps.android.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.udacitynanodegreeapps.android.popularmovies.dummy.DummyContent;

public class DetailActivity extends AppCompatActivity
        implements ReviewFragment.OnListFragmentInteractionListener
{

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
    }

    public  void setFavourite(View view){

        CheckBox favourite = (CheckBox) view.findViewById(R.id.chkbox_favourite);
        if(favourite.isChecked()) {
            Log.i("Detail:", "Removing Favourite");
            favourite.setText("Remove From Favourites");
//            favourite.setChecked(false);
//            favourite.setButtonDrawable(android.R.drawable.btn_star_big_on);
        }
        else {
            favourite.setText("Add to Favourites");
//            favourite.setChecked(true);
//            favourite.setButtonDrawable(android.R.drawable.btn_star_big_off);
        }
//        SharedPreferences.Editor setFav = this.getActivity().getSharedPreferences("favourite",Context.MODE_PRIVATE).edit();
//        setFav.putBoolean("fav",true);
//        setFav.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Toast toast = Toast.makeText(this,item.id,Toast.LENGTH_SHORT);
        toast.show();
    }
}
