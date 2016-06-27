package com.xiroid.imovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiroid.imovie.model.Movies;

import java.util.List;

/**
 * @author xiaojunzhou
 * @date 16/6/19
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movies.Movie> movies;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleImageView posterImg;
        public TextView titleTxt;

        public ViewHolder(View view) {
            super(view);
            posterImg = (SimpleImageView) view.findViewById(R.id.iv_poster);
            titleTxt = (TextView) view.findViewById(R.id.txt_title);
        }
    }

    public MoviesAdapter(Context context, List<Movies.Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Movies.Movie movie = movies.get(position);
//        holder.titleTxt.setText(movie.getOriginal_title());
//        Picasso.with(context)
//                .load(movie.getPoster())
//                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
//                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
//                .into(holder.posterImg);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movies.Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
