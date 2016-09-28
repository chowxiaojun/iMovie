package com.xiroid.imovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.xiroid.imovie.model.Movies;
import com.xiroid.imovie.widget.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojunzhou
 * @date 16/6/19
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movies.Movie> movies = new ArrayList<>();
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public AspectRatioImageView posterImg;
        public TextView titleTxt;

        public ViewHolder(View view) {
            super(view);
            titleTxt = (TextView) view.findViewById(R.id.txt_title);
            posterImg = (AspectRatioImageView) view.findViewById(R.id.iv_poster);
        }
    }

    public MoviesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_movies_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        Logger.d("onBindViewHolder " + "position: " + position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Logger.d("onBindViewHolder" + movies.size() + " position: " + position);
        Movies.Movie movie = movies.get(position);
        holder.titleTxt.setText(movie.getTitle());
        Picasso.with(context).load(movie.getPoster()).into(holder.posterImg);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void addMovies(List<Movies.Movie> movies) {
        Logger.d("addMovies");
        this.movies = movies;
        notifyItemInserted(this.movies.size() - 1);
    }
}
