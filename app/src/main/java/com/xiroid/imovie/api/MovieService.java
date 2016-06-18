package com.xiroid.imovie.api;

import com.xiroid.imovie.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author xiaojunzhou
 * @date 16/6/14
 */
public interface MovieService {
    public static final String BASE_URL = "http://api.themoviedb.org/";

    @GET("/3/movie/{sortBy}")
    Call<Movies> getMovies(@Path("sortBy") String sortBy,
                           @Query("api_key") String apiKey,
                           @Query("page") Integer page
    );
}
