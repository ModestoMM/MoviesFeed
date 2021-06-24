package com.modesto.moviesfeed.http;

import com.modesto.moviesfeed.http.apimodel.TopMoviesRated;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesApisService {
    
    @GET("top_rated")
    Observable<TopMoviesRated> getTopMoviesRated(@Query("page") Integer page);
}
