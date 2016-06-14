package com.xiroid.imovie.api;

import com.xiroid.imovie.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author xiaojunzhou
 * @date 16/6/14
 */
public interface MovieService {
    @GET("")
    Call<Movies> getMovies();
}
