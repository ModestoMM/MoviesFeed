package com.modesto.moviesfeed.movies;

import com.modesto.moviesfeed.http.apimodel.Result;

import io.reactivex.Observable;


public interface Repository {


    Observable<Result> getResultFromNetwork();
    Observable<Result> getResultFromCache();
    //Este metodo traera las peliculas
    Observable<Result> getResultData();

    //Este metodo traera los paises
    Observable<String> getCountryData();
    Observable<String> getCountryFromNetwork();
    Observable<String> getCountryFromCache();

}
