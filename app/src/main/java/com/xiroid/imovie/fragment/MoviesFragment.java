package com.xiroid.imovie.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.BuildConfig;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.activity.DetailActivity;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    private static final String TAG = MoviesFragment.class.getSimpleName();

    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    private List<MovieInfo> movieInfos;

    private static final String[] FAVORITE_PROJECTION = {
            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID
    };

    private static final int COL_FAVORITE_MOVIE_ID = 0;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movies_gridview);
        mImageAdapter = new ImageAdapter(getActivity());
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                MovieInfo movieInfo = (MovieInfo) mImageAdapter.getItem(position);
                Log.d("MoviesFragment", movieInfo.toString());
                intent.putExtra("data", movieInfo);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        loadMovies();
    }

    private void loadMovies() {
        new FetchMoviesTask().execute();
    }

    // SUGGESTION:
    // In order to make your codes reusable and structural, you can consider to refactor your codes
    // and put this class in a separate Java file.

    // To learn more, you can also try to use Retrofit library in your future projects to handle
    // fetching data using third party API and network operations. It can make your life easier.
    // Ref: http://square.github.io/retrofit/
    class FetchMoviesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesStr = null;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = sharedPref.getString(
                    getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
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
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() >= 0) {
                    moviesStr = buffer.toString();
                }

            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            if (!TextUtils.isEmpty(moviesStr)) {
                Cursor cursor = null;
                try {
                    JSONObject jsonObject = new JSONObject(moviesStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    movieInfos = parseData(jsonArray);
                    cursor = getContext().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,
                            FAVORITE_PROJECTION,
                            null,
                            null,
                            null
                    );
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

            return moviesStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (movieInfos != null) {
                mImageAdapter.add(movieInfos);
                mGridView.setVisibility(View.VISIBLE);
                ((Callback) getActivity()).onResult(0);
            } else {
                ((Callback) getActivity()).onResult(-1);
            }
        }

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
        private List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public void add(List<MovieInfo> movieInfos) {
            this.movieInfos = movieInfos;
            notifyDataSetChanged();
        }

        public int getCount() {
            return movieInfos.size();
        }

        public Object getItem(int position) {
            return movieInfos.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            SimpleImageView imageView;
            if (convertView == null) {
                imageView = new SimpleImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                imageView = (SimpleImageView) convertView;
            }

            Picasso.with(mContext).load(movieInfos.get(position).getPoster()).into(imageView);
            return imageView;
        }
    }

    public interface Callback {
        void onResult(int errCode);
    }
}
