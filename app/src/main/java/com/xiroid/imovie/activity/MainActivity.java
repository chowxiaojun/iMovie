package com.xiroid.imovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiroid.imovie.R;
import com.xiroid.imovie.fragment.DetailFragment;
import com.xiroid.imovie.fragment.MoviesFragment;

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
        errorTxt = (TextView) findViewById(R.id.txt_err);
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
    public void onResult(int errCode) {
        mLoadingView.setVisibility(View.GONE);
        if (errCode != 0) {

        } else {
            mContentView.setVisibility(View.VISIBLE);
        }
    }

}
