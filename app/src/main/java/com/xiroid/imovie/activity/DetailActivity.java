package com.xiroid.imovie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.R;
import com.xiroid.imovie.SimpleImageView;
import com.xiroid.imovie.model.MovieInfo;

public class DetailActivity extends AppCompatActivity {

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
            Log.d("DetailActivity", movieInfo.toString());
            SimpleImageView poster = (SimpleImageView) findViewById(R.id.iv_poster);
            TextView title = (TextView) findViewById(R.id.title);
            TextView rating = (TextView) findViewById(R.id.rating);
            TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
            TextView overview = (TextView) findViewById(R.id.overview);
            Picasso.with(getApplicationContext()).load(movieInfo.getPoster()).into(poster);
            title.setText(movieInfo.getOriginalTitle());
            overview.setText(movieInfo.getOverview());
            rating.setText(Double.toString(movieInfo.getVoteAverage()));
            releaseDate.setText(movieInfo.getReleaseDate());

        }
    }

}
