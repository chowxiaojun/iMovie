package com.xiroid.imovie.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.BuildConfig;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.api.MovieService;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.Movies;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();
    private static final String SELECT_KEY = "select_key";
    private static final String MOVIE_LIST = "movie_list";

    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private ArrayList<Movies.Movie> movies;
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
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movies_gridView);
        TextView textView = (TextView) rootView.findViewById(R.id.empty_view);
        mGridView.setEmptyView(textView);
        mImageAdapter = new ImageAdapter(getActivity());
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movies.Movie movie = (Movies.Movie) mImageAdapter.getItem(position);
                mPosition = position;
            }
        });

        if (savedInstanceState != null
                && savedInstanceState.containsKey(SELECT_KEY)
                && savedInstanceState.containsKey(MOVIE_LIST)) {
            mPosition = savedInstanceState.getInt(SELECT_KEY, GridView.INVALID_POSITION);
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = sharedPref.getString(
                getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MovieService.BASE_URL)
                .build();

        MovieService service = retrofit.create(MovieService.class);
        Call<Movies> call = service.getMovies(sortBy, BuildConfig.THE_MOVIE_API_KEY, null);
        call.enqueue(new retrofit2.Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    Movies result = response.body();
                    movies = (ArrayList<Movies.Movie>) result.getResults();
                    updateView();
                } else {

                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

            }
        });
    }

    private void updateView() {
        if (movies != null) {
            mImageAdapter.add(movies);
        }
        if (getView() != null) {
            LinearLayout progress = (LinearLayout) getView().findViewById(R.id.progress_view);
            LinearLayout content = (LinearLayout) getView().findViewById(R.id.content);
            progress.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }

    }

    private void loadMovies() {
        if (mPosition == GridView.INVALID_POSITION || movies == null) {
        } else {
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECT_KEY, mPosition);
            outState.putParcelableArrayList(MOVIE_LIST, movies);
        }
        super.onSaveInstanceState(outState);
    }

    private void getFavoritesFromDb() {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,
                    FAVORITE_PROJECTION,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                movies = new ArrayList<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    int movieId = cursor.getInt(COL_FAVORITE_MOVIE_ID);
                    Movies.Movie movieInfo = new Movies.Movie();
                    movieInfo.setFavorite(1);
                    movieInfo.setId(cursor.getInt(COL_FAVORITE_MOVIE_ID));
                    movieInfo.setOriginal_title(cursor.getString(COL_FAVORITE_MOVIE_TITLE));
                    movieInfo.setOverview(cursor.getString(COL_FAVORITE_MOVIE_OVERVIEW));
                    movieInfo.setRelease_date(cursor.getString(COL_FAVORITE_MOVIE_RELEASE_DATE));
                    movieInfo.setPoster_path(cursor.getString(COL_FAVORITE_MOVIE_POSTER));
                    movieInfo.setVote_average(cursor.getDouble(COL_FAVORITE_MOVIE_VOTE_AVERAGE));
                    movies.add(movieInfo);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<Movies.Movie> movies = new ArrayList<>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public void add(List<Movies.Movie> movies) {
            this.movies = movies;
            notifyDataSetChanged();
        }

        public int getCount() {
            return movies.size();
        }

        public Object getItem(int position) {
            return MoviesFragment.this.movies.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout container;
            if (convertView == null) {
                container = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.grid_movies_item, null, false);

            } else {
                container = (LinearLayout) convertView;
            }

            SimpleImageView imageView = (SimpleImageView) container.findViewById(R.id.iv_poster);
            Picasso.with(mContext).load(movies.get(position).getPoster()).into(imageView);
            TextView textView = (TextView) container.findViewById(R.id.txt_title);
            textView.setText(movies.get(position).getOriginal_title());

            return container;
        }
    }
}
