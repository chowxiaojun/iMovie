package com.xiroid.imovie.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;
import com.xiroid.imovie.R;
import com.xiroid.imovie.utils.Utility;
import com.xiroid.imovie.fragment.MoviesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String MOVIES_FRAGMENT_TAG = "movies_fragment";

    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            setCheckedItem(navigationView);
        }
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.movies_container, new MoviesFragment(), MOVIES_FRAGMENT_TAG);
            ft.commit();
        }
    }

    /**
     * 设置当前电影分类
     *
     * @param navigationView 左边的抽屉菜单
     */
    private void setCheckedItem(NavigationView navigationView) {
        if (navigationView == null) {
            return;
        }

        category = Utility.getDataSourceCategory(getApplicationContext());

        switch (category) {
            case R.string.pref_sort_now_playing: {
                navigationView.setCheckedItem(R.id.nav_now_playing);
                setTitle(R.string.pref_sort_label_now_playing);
                break;
            }
            case R.string.pref_sort_upcoming: {
                navigationView.setCheckedItem(R.id.nav_upcoming);
                setTitle(R.string.pref_sort_label_upcoming);
                break;
            }
            case R.string.pref_sort_popular: {
                navigationView.setCheckedItem(R.id.nav_popular);
                setTitle(R.string.pref_sort_label_popular);
            }
            case R.string.pref_sort_top_rated: {
                navigationView.setCheckedItem(R.id.nav_top_rated);
                setTitle(R.string.pref_sort_label_top_rated);
            }
            case R.string.pref_sort_favorite: {
                navigationView.setCheckedItem(R.id.nav_favorite);
                setTitle(R.string.pref_sort_label_favorite);
            }
            default:
                break;
         }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d("onPause");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_now_playing: {
                category = R.string.pref_sort_now_playing;
                setTitle(R.string.pref_sort_label_now_playing);
                break;
            }
            case R.id.nav_upcoming: {
                setTitle(R.string.pref_sort_label_upcoming);
                break;
            }
            case R.id.nav_popular: {
                break;
            }
            case R.id.nav_top_rated: {
                break;
            }
            case R.id.nav_favorite: {
                break;
            }
            case R.id.nav_share: {
                break;
            }
            case R.id.nav_about: {
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
