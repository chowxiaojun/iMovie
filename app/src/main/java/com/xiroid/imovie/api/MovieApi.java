package com.xiroid.imovie.api;

import com.xiroid.imovie.model.Movies;
import com.xiroid.imovie.model.Reviews;
import com.xiroid.imovie.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author xiaojunzhou
 * @date 16/6/14
 */
public interface MovieApi {

    @GET("/discover/movie") Call<Movies> getMovies(
            @Path("sort_by") String sortBy,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("/movie/{id}/videos") Call<Trailers> getTrailers(
            @Path("id") long movieId,
            @Query("api_key") String apiKey

    );

    @GET("/movie/{id}/reviews") Call<Reviews> getReviews(
            @Path("id") long movieId,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

}
