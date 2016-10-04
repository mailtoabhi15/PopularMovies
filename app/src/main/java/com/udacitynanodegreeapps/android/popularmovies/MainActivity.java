package com.udacitynanodegreeapps.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import static com.udacitynanodegreeapps.android.popularmovies.DetailActivityFragment.LIST_MOVIES_INDEX;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.ListItemClickCallback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        //Dixit: imp- Added below code for stetho support in our app, so as to debug/analyse issues
        //using Chrome://inspect tool on desktop.
        Stetho.initialize(
                        Stetho.newInitializerBuilder(this)
                                      .enableDumpapp(
                                              Stetho.defaultDumperPluginsProvider(this))
                                       .enableWebKitInspector(
                                               Stetho.defaultInspectorModulesProvider(this))
                                       .build());

        if(findViewById(R.id.fragment_detail_container) != null)
        {
            mTwoPane = true;

            if (savedInstanceState == null) {
//               getSupportFragmentManager().beginTransaction()
//              .add(R.id.fragment_detail_container,fragment)
//               .commit();

            }
        }
        else{
            mTwoPane =false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MyMovie movieListItem) {

        if(mTwoPane){
            //on tablet, means, it will launch DetailFragment

            //Dixit-imp:Saving the clicked Uri as Bundle argument & setting it on to fragment
            //so that if the device  is rotated or activity starts again, it will
            //restore the clickuri/index form that point itself, as its in Bundle
            Bundle args = new Bundle();
            args.putParcelable(LIST_MOVIES_INDEX,movieListItem);
            //now the bundled arguments are set & passed on by call to empty fragmant constructor
            //& once teh fragmen tis intialised we can't chnage teh arguments , we can only read form them(getArguments())
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container,fragment)
                    .commit();

        }
        else{
            //on phone, means, it will launch DetailActivity
            Intent detailAct = new Intent(this,DetailActivity.class);
                detailAct.putExtra(LIST_MOVIES_INDEX,movieListItem);
                startActivity(detailAct);
        }



    }
}
