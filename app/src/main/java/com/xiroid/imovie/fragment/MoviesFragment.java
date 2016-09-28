package com.xiroid.imovie.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.orhanobut.logger.Logger;
import com.xiroid.imovie.BuildConfig;
import com.xiroid.imovie.MoviesAdapter;
import com.xiroid.imovie.R;
import com.xiroid.imovie.api.ApiModule;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.Movies;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static final String SELECT_KEY = "select_key";
    private static final String MOVIE_LIST = "movie_list";

    View progressView;
    RecyclerView recyclerView;
    MoviesAdapter moviesAdapter;
    private List<Movies.Movie> movies = new ArrayList<>();

    private int mPosition = GridView.INVALID_POSITION;

    private static final String[] FAVORITE_PROJECTION = {
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER
    };

    private static final int COL_FAVORITE_MOVIE_ID = 0;
    private static final int COL_FAVORITE_MOVIE_TITLE = 1;
    private static final int COL_FAVORITE_MOVIE_OVERVIEW = 2;
    private static final int COL_FAVORITE_MOVIE_RELEASE_DATE = 3;
    private static final int COL_FAVORITE_MOVIE_VOTE_AVERAGE = 4;
    private static final int COL_FAVORITE_MOVIE_POSTER = 5;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d("onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        progressView = rootView.findViewById(R.id.progress_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        moviesAdapter = new MoviesAdapter(getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moviesAdapter);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(SELECT_KEY)
                && savedInstanceState.containsKey(MOVIE_LIST)) {
            mPosition = savedInstanceState.getInt(SELECT_KEY, GridView.INVALID_POSITION);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d("onStart");
        Call<Movies> popular = ApiModule.getMovieApi()
                .getMovies("popularity.desc", BuildConfig.THE_MOVIE_API_KEY, 1);
        popular.enqueue(new retrofit2.Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    Movies result = response.body();
                    result.setGroupTitle(getString(R.string.pref_sort_popular));
                    updateView(result);
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Logger.d("onFailure");
            }
        });
    }

    private void updateView(Movies rsp) {
        if (progressView.getVisibility() == View.VISIBLE) {
            progressView.setVisibility(View.GONE);
        }
        this.movies = rsp.getResults();
        moviesAdapter.addMovies(movies);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECT_KEY, mPosition);
            //outState.putParcelableArrayList(MOVIE_LIST, (ArrayList<Movies.Movie>) movies);
        }
        super.onSaveInstanceState(outState);
    }

//    private void getFavoritesFromDb() {
//        Cursor cursor = null;
//        try {
//            cursor = getContext().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,
//                    FAVORITE_PROJECTION,
//                    null,
//                    null,
//                    null
//            );
//            if (cursor != null) {
//                movies = new ArrayList<>();
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    int movieId = cursor.getInt(COL_FAVORITE_MOVIE_ID);
//                    Movies.Movie movieInfo = new Movies.Movie();
//                    movieInfo.setFavorite(1);
//                    movieInfo.setId(cursor.getInt(COL_FAVORITE_MOVIE_ID));
//                    movieInfo.setOriginal_title(cursor.getString(COL_FAVORITE_MOVIE_TITLE));
//                    movieInfo.setOverview(cursor.getString(COL_FAVORITE_MOVIE_OVERVIEW));
//                    movieInfo.setRelease_date(cursor.getString(COL_FAVORITE_MOVIE_RELEASE_DATE));
//                    movieInfo.setPoster_path(cursor.getString(COL_FAVORITE_MOVIE_POSTER));
//                    movieInfo.setVote_average(cursor.getDouble(COL_FAVORITE_MOVIE_VOTE_AVERAGE));
//                    movies.add(movieInfo);
//                }
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

}
