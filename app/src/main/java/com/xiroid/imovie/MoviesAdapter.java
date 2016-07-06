package com.xiroid.imovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiroid.imovie.model.Movies;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojunzhou
 * @date 16/6/19
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movies> moviesList = new ArrayList<>();
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleImageView firstPosterImg;
        public SimpleImageView secondPosterImg;
        public SimpleImageView thirdPosterImg;
        public TextView groupTitleTxt;

        public ViewHolder(View view) {
            super(view);
            firstPosterImg = (SimpleImageView) view.findViewById(R.id.first_movie);
            secondPosterImg = (SimpleImageView) view.findViewById(R.id.second_movie);
            thirdPosterImg = (SimpleImageView) view.findViewById(R.id.third_movie);
            groupTitleTxt = (TextView) view.findViewById(R.id.group_title);
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
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movies movies = moviesList.get(position);
        holder.groupTitleTxt.setText(movies.getGroupTitle());
        Picasso.with(context)
                .load(movies.getResults().get(0).getPoster())
                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                .into(holder.firstPosterImg);
        Picasso.with(context)
                .load(movies.getResults().get(1).getPoster())
                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                .into(holder.secondPosterImg);
        Picasso.with(context)
                .load(movies.getResults().get(2).getPoster())
                .placeholder(R.drawable.placeholder) // TODO: 需要一套合适的占位图
                .error(R.drawable.placeholder) // TODO: 需要一套合适的错误图
                .into(holder.thirdPosterImg);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public void addMovies(Movies movies, int index) {
        moviesList.add(index, movies);
        notifyDataSetChanged();
    }
}
