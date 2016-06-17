package com.xiroid.imovie.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.xiroid.imovie.activity.MainActivity;
import com.xiroid.imovie.api.MovieService;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.MovieInfo;
import com.xiroid.imovie.model.MoviesResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private ArrayList<MovieInfo> movieInfos;
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
        mGridView = (GridView) rootView.findViewById(R.id.movies_gridview);
        TextView textView = (TextView) rootView.findViewById(R.id.empty_view);
        mGridView.setEmptyView(textView);
        mImageAdapter = new ImageAdapter(getActivity());
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo movieInfo = (MovieInfo) mImageAdapter.getItem(position);
                ((MainActivity) getActivity()).onItemClick(movieInfo);
                mPosition = position;
            }
        });

        if (savedInstanceState != null
                && savedInstanceState.containsKey(SELECT_KEY)
                && savedInstanceState.containsKey(MOVIE_LIST)) {
            mPosition = savedInstanceState.getInt(SELECT_KEY, GridView.INVALID_POSITION);
            movieInfos = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
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
        Call<MoviesResult> call = service.getMovies(sortBy, BuildConfig.THE_MOVIE_API_KEY);
        call.enqueue(new retrofit2.Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                if (response.isSuccessful()) {
                    MoviesResult result = response.body();
                    List<MoviesResult.Movie> movies = result.getResults();
                    mImageAdapter.add(movies);
                    ((Callback) getActivity()).onResult();
                }
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {

            }
        });
    }

    private void loadMovies() {
        if (mPosition == GridView.INVALID_POSITION || movieInfos == null) {
        } else {
            if (movieInfos.size() > 0) {
                ((Callback) getActivity()).initDetail(movieInfos.get(mPosition));
            } else {
                ((Callback) getActivity()).initDetail(null);
            }
            mGridView.smoothScrollToPosition(mPosition);
            ((Callback) getActivity()).onResult();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECT_KEY, mPosition);
            outState.putParcelableArrayList(MOVIE_LIST, movieInfos);
        }
        super.onSaveInstanceState(outState);
    }

    // SUGGESTION:
    // In order to make your codes reusable and structural, you can consider to refactor your codes
    // and put this class in a separate Java file.

    // To learn more, you can also try to use Retrofit library in your future projects to handle
    // fetching data using third party API and network operations. It can make your life easier.
    // Ref: http://square.github.io/retrofit/
    class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = sharedPref.getString(
                    getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
            String favorite = getResources().getString(R.string.pref_sort_favorite);
            if (sortBy.equals(favorite)) {
                getFavoritesFromDb();
            } else {
                getMoviesFromNetwork(sortBy);
            }

            return null;
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
                    movieInfos = new ArrayList<>();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        int movieId = cursor.getInt(COL_FAVORITE_MOVIE_ID);
                        MovieInfo movieInfo = new MovieInfo();
                        movieInfo.setFavorite(1);
                        movieInfo.setId(cursor.getInt(COL_FAVORITE_MOVIE_ID));
                        movieInfo.setOriginalTitle(cursor.getString(COL_FAVORITE_MOVIE_TITLE));
                        movieInfo.setOverview(cursor.getString(COL_FAVORITE_MOVIE_OVERVIEW));
                        movieInfo.setReleaseDate(cursor.getString(COL_FAVORITE_MOVIE_RELEASE_DATE));
                        movieInfo.setPosterPath(cursor.getString(COL_FAVORITE_MOVIE_POSTER));
                        movieInfo.setVoteAverage(cursor.getDouble(COL_FAVORITE_MOVIE_VOTE_AVERAGE));
                        movieInfos.add(movieInfo);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        /**
         * 从网络上获取数据
         *
         * @param sortBy
         */
        private void getMoviesFromNetwork(String sortBy) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesStr = null;
            try {
                // /movie/top_rated
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + sortBy;
                final String APIKEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIE_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() >= 0) {
                    moviesStr = buffer.toString();
                }

            } catch (IOException e) {
                return;
            }
            if (!TextUtils.isEmpty(moviesStr)) {
                Cursor cursor = null;
                try {
                    JSONObject jsonObject = new JSONObject(moviesStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    movieInfos = parseData(jsonArray);
                    if (getContext().getContentResolver() != null) {
                        cursor = getContext().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,
                                FAVORITE_PROJECTION,
                                null,
                                null,
                                null
                        );
                    }

                    if (cursor != null) {
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                            int movieId = cursor.getInt(COL_FAVORITE_MOVIE_ID);
                            for (MovieInfo item : movieInfos) {
                                if (item.getId() == movieId) {
                                    item.setFavorite(1);
                                    break;
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (movieInfos != null) {
                if (movieInfos.size() > 0) {
                    ((Callback) getActivity()).initDetail(movieInfos.get(0));
                } else {
                    ((Callback) getActivity()).initDetail(null);
                }
            }
            updateEmptyView();
            ((Callback) getActivity()).onResult();
        }

        private void updateEmptyView() {
            if (mImageAdapter.getCount() <= 0) {
                if (getView() != null) {
                    int message = R.string.txt_empty_network_err;
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String sortBy = sharedPref.getString(
                            getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
                    String favorite = getResources().getString(R.string.pref_sort_favorite);
                    if (sortBy.equals(favorite)) {
                        message = R.string.txt_empty_no_favorites;
                    }
                   TextView emptyTxt = (TextView) getView().findViewById(R.id.empty_view);
                    emptyTxt.setText(getResources().getString(message));
                }
            }
        }

        /**
         * 解析JSON数据
         *
         * @param data json data
         * @return a List of MovieInfo
         */
        private ArrayList<MovieInfo> parseData(JSONArray data) {
            ArrayList<MovieInfo> infos = new ArrayList<MovieInfo>();
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject item = (JSONObject) data.get(i);
                        infos.add(parseMovieInfo(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return infos;
        }

        private MovieInfo parseMovieInfo(JSONObject item) {
            MovieInfo movieInfo = new MovieInfo();
            if (item != null) {
                try {
                    // MovieInfo中的很多字段不一定有用，看需求
                    movieInfo.setId(item.getInt("id"));
                    movieInfo.setOriginalTitle(item.getString("original_title"));
                    movieInfo.setPosterPath(item.getString("poster_path"));
                    movieInfo.setOverview(item.getString("overview"));
                    movieInfo.setVoteAverage(item.getDouble("vote_average"));
                    movieInfo.setReleaseDate(item.getString("release_date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return movieInfo;
        }
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<MoviesResult.Movie> movies = new ArrayList<>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public void add(List<MoviesResult.Movie> movies) {
            this.movies = movies;
            notifyDataSetChanged();
        }

        public int getCount() {
            return movies.size();
        }

        public Object getItem(int position) {
            return movieInfos.get(position);
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
            textView.setText(movies.get(position).getTitle());

            return container;
        }
    }

    public interface Callback {
        void initDetail(MovieInfo movieInfo);
        void onResult();
        void onItemClick(MovieInfo movieInfo);
    }
}
