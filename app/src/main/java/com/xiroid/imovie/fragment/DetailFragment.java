package com.xiroid.imovie.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.data.MovieContract;
import com.xiroid.imovie.model.MovieInfo;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_DATA = "detail_data";

    private ImageButton mFavoriteBtn;

    private MovieInfo movieInfo;
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
        if (intent != null && intent.getParcelableExtra("data") != null) {
            movieInfo = intent.getParcelableExtra("data");
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
            }
        }
        return rootView;
    }
}
