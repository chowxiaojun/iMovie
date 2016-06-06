package com.xiroid.imovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiroid.imovie.R;
import com.xiroid.imovie.fragment.DetailFragment;
import com.xiroid.imovie.fragment.MoviesFragment;
import com.xiroid.imovie.model.MovieInfo;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "detail_tag";
    private boolean mTwoPane;

    private LinearLayout mLoadingView;
    private LinearLayout mContentView;
    private TextView errorTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoadingView = (LinearLayout) findViewById(R.id.progress_view);
        mContentView = (LinearLayout) findViewById(R.id.content);
       if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            // 当设备旋转时，Fragment不会重复创建，因为Activity会重建
            if (savedInstanceState == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG);
                ft.commit();
            }
        } else {
            mTwoPane = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0f);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initDetail(MovieInfo movieInfo) {
        if (mTwoPane) {
            FrameLayout detailContainer = (FrameLayout) findViewById(R.id.movie_detail_container);
            if (movieInfo != null) {
                if (detailContainer != null) {
                    detailContainer.setVisibility(View.VISIBLE);
                }
                Bundle arguments = new Bundle();
                arguments.putParcelable(DetailFragment.DETAIL_DATA, movieInfo);

                DetailFragment fragment =  new DetailFragment();
                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment)
                        .commit();
            } else {
                if (detailContainer != null) {
                    detailContainer.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onResult() {
        mLoadingView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(MovieInfo movieInfo) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_DATA, movieInfo);

            DetailFragment fragment =  new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            Log.d("MoviesFragment", movieInfo.toString());
            intent.putExtra("data", movieInfo);
            startActivity(intent);
        }
    }

}
