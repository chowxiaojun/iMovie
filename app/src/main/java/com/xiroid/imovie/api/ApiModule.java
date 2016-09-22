package com.xiroid.imovie.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author xiaojunzhou
 * @date 16/9/22
 */
public class ApiModule {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static MovieApi movieApi;

    public static MovieApi getMovieApi() {
        if (movieApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ApiModule.BASE_URL)
                    .build();
            movieApi = retrofit.create(MovieApi.class);
        }

        return movieApi;
    }
}
