package com.xiroid.imovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.activity.DetailActivity;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private GridView mGridView;
    private ImageAdapter mImageAdapter;
    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        new FetchMoviesTask().execute();
    }

    class FetchMoviesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String api_key = "";
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
                        .appendQueryParameter(APIKEY_PARAM, api_key)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return moviesStr;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() >= 0) {
                    moviesStr = buffer.toString();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviesStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                mImageAdapter.add(parseData(jsonArray));
            } catch (JSONException e) {
                e.printStackTrace();
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
        private ArrayList<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public void add(ArrayList<MovieInfo> movieInfos) {
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
}
