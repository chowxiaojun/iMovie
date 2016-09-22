package com.xiroid.imovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.xiroid.imovie.R;
import com.xiroid.imovie.activity.DetailActivity;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.Movies;
import com.xiroid.imovie.widget.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static final String SELECT_KEY = "select_key";
    private static final String MOVIE_LIST = "movie_list";

    View progressView;
    ViewGroup mContainer;
    private List<Movies> moviesList = new ArrayList<>();

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
        mContainer = (ViewGroup) rootView.findViewById(R.id.container);
        progressView = rootView.findViewById(R.id.progress_view);
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
//        Call<Movies> popular = service.getMovies(getString(R.string.pref_sort_popular), BuildConfig.THE_MOVIE_API_KEY, 1);
//        popular.enqueue(new retrofit2.Callback<Movies>() {
//            @Override
//            public void onResponse(Call<Movies> call, Response<Movies> response) {
//                if (response.isSuccessful()) {
//                    Movies result = response.body();
//                    result.setGroupTitle(getString(R.string.pref_sort_popular));
//                    updateView(result);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Movies> call, Throwable t) {
//
//            }
//        });
    }

    private void updateView(Movies movies) {
        if (progressView.getVisibility() == View.VISIBLE) {
            progressView.setVisibility(View.GONE);
        }
        moviesList.add(movies);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_layout, mContainer, false);
        TextView titleTxt = (TextView) view.findViewById(R.id.group_title);
        AspectRatioImageView firstPosterImg = (AspectRatioImageView) view.findViewById(R.id.first_movie);
        AspectRatioImageView secondPosterImg = (AspectRatioImageView) view.findViewById(R.id.second_movie);
        AspectRatioImageView thirdPosterImg = (AspectRatioImageView) view.findViewById(R.id.third_movie);
        thirdPosterImg.setTag(movies.getResults().get(2));
        thirdPosterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movies.Movie movie = (Movies.Movie) v.getTag();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("data", movie);
                startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        titleTxt.setText(movies.getGroupTitle());
        Picasso.with(getActivity())
                .load(movies.getResults().get(0).getPoster())
                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                .into(firstPosterImg);
        Picasso.with(getActivity())
                .load(movies.getResults().get(1).getPoster())
                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                .into(secondPosterImg);
        Picasso.with(getActivity())
                .load(movies.getResults().get(2).getPoster())
                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                .into(thirdPosterImg);
        mContainer.addView(view);
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
