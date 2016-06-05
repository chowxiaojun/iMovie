package com.xiroid.imovie.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.BuildConfig;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.MovieInfo;
import com.xiroid.imovie.model.VideoInfo;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_DATA = "detail_data";

    private ImageButton mFavoriteBtn;

    private MovieInfo movieInfo;
    private List<VideoInfo> videoInfos;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mFavoriteBtn = (ImageButton) rootView.findViewById(R.id.btn_favorite);
        mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieInfo.getFavorite() == 0) {
                    movieInfo.setFavorite(1);
                    mFavoriteBtn.setImageResource(R.drawable.ic_favorite_black);
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, movieInfo.getId());
                    values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW, movieInfo.getOverview());
                    values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER, movieInfo.getPosterPath());
                    values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE, movieInfo.getReleaseDate());
                    values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE, movieInfo.getOriginalTitle());
                    values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_VOTE_AVERAGE, movieInfo.getVoteAverage());
                    getContext().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, values);
                } else {
                    movieInfo.setFavorite(0);
                    mFavoriteBtn.setImageResource(R.drawable.ic_favorite_border_black);
                    getContext().getContentResolver().delete(
                            MovieContract.FavoriteEntry.CONTENT_URI,
                            MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{Integer.toString(movieInfo.getId())}
                    );
                }
            }
        });

        Intent intent = getActivity().getIntent();
        Bundle args = getArguments();
        if (args != null && args.getParcelable(DETAIL_DATA) != null) {
            movieInfo = args.getParcelable(DETAIL_DATA);
            // SUGGESTION:
            // Since from your codes, I can see that you are a really advanced student, in order to
            // learn more, you could also check a package called "butterknife". In the future, you
            // can find and automatically cast the corresponding view in your layout easily. This
            // will save you a lot of time.
            // Supp:
            // http://jakewharton.github.io/butterknife/
            // Also, a video tutorial:
            // https://www.youtube.com/watch?v=1A4LY8gUEDs
            SimpleImageView poster = (SimpleImageView) rootView.findViewById(R.id.image_poster);
            TextView title = (TextView) rootView.findViewById(R.id.txt_title);
            TextView rating = (TextView) rootView.findViewById(R.id.txt_rating);
            TextView releaseDate = (TextView) rootView.findViewById(R.id.txt_releaseDate);
            TextView overview = (TextView) rootView.findViewById(R.id.txt_overview);

            if (movieInfo != null) {

                if (movieInfo.getFavorite() == 1) {
                    mFavoriteBtn.setImageResource(R.drawable.ic_favorite_black);
                }

                if (title != null) {
                    title.setText(movieInfo.getOriginalTitle());
                }

                Picasso.with(getActivity())
                        .load(movieInfo.getPoster())
                        .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                        .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                        .into(poster);
                if (releaseDate != null) {
                    releaseDate.setText(movieInfo.getReleaseDate().split("-")[0]);
                }

                if (rating != null) {
                    rating.setText(getString(R.string.txt_rating_format, Double.toString(movieInfo.getVoteAverage())));
                }

                if (overview != null) {
                    overview.setText(movieInfo.getOverview());
                }
                new FetchTrailersTask().execute(Integer.toString(movieInfo.getId()));
            }
        }
        return rootView;
    }

    class FetchTrailersTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            if (!TextUtils.isEmpty(params[0])) {
                getTrailers(params[0]);
            }
            return null;
        }

        /**
         * 获取预览片信息
         */
        private void getTrailers(String movieId) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesStr = null;
            try {
                // /movie/{id}/videos
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/videos";
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
                try {
                    JSONObject jsonObject = new JSONObject(moviesStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    videoInfos = parseData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (videoInfos != null && videoInfos.size() > 0 && getView() != null) {
                LinearLayout container = (LinearLayout) getView()
                        .findViewById(R.id.trailers_container);
                for (VideoInfo videoInfo : videoInfos) {
                    LinearLayout itemRoot = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                            R.layout.layout_trailer_item, null, false);
                    TextView nameTxt = (TextView) itemRoot.findViewById(R.id.txt_name);
                    nameTxt.setText(videoInfo.getName());
                    itemRoot.setTag(videoInfo);
                    itemRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            VideoInfo info = (VideoInfo) v.getTag();
                            String url = info.getUrl();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(info.getUrl()));
                            startActivity(intent);
                        }
                    });
                    container.addView(itemRoot);
                }
            }
        }

        /**
         * 解析JSON数据
         *
         * @param data json data
         * @return a List of VideoInfo
         */
        private ArrayList<VideoInfo> parseData(JSONArray data) {
            ArrayList<VideoInfo> infos = new ArrayList<>();
            if (data != null) {
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject item = (JSONObject) data.get(i);
                        infos.add(parseVideoInfo(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return infos;
        }

        private VideoInfo parseVideoInfo(JSONObject item) {
            VideoInfo videoInfo = new VideoInfo();
            if (item != null) {
                try {
                    videoInfo.setId(item.getString("id"));
                    videoInfo.setName(item.getString("name"));
                    videoInfo.setKey(item.getString("key"));
                    videoInfo.setType(item.getString("type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return videoInfo;
        }
    }
}
