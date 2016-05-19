package com.xiroid.imovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.model.MovieInfo;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private MovieInfo movieInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
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
            SimpleImageView poster = (SimpleImageView) findViewById(R.id.iv_poster);
            TextView title = (TextView) findViewById(R.id.title);
            TextView rating = (TextView) findViewById(R.id.rating);
            TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
            TextView overview = (TextView) findViewById(R.id.overview);

            Picasso.with(getApplicationContext())
                    .load(movieInfo.getPoster())
                    .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                    .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                    .into(poster);
            title.setText(movieInfo.getOriginalTitle());
            overview.setText(movieInfo.getOverview());
            rating.setText(Double.toString(movieInfo.getVoteAverage()));
            releaseDate.setText(movieInfo.getReleaseDate());

        }
    }

}
