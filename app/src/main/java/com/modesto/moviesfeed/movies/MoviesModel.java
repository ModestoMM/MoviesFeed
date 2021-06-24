package com.modesto.moviesfeed.movies;

import com.modesto.moviesfeed.http.apimodel.Result;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

public class MoviesModel implements MoviesMVP.Model {

    private Repository repository;

    public MoviesModel(Repository repository){
        this.repository = repository;
    }

    @Override
    public Observable<ViewModel> result() {
        //Juntamos la informacion de las APIS en este metodo esa es la funcion del zip
        //Y la funcion BinFuction nos ayuda a juntar esos dos resultados de las API
        return Observable.zip(repository.getResultData(), repository.getCountryData(), new BiFunction<Result, String, ViewModel>() {
            @NonNull
            @Override
            public ViewModel apply(@NonNull Result result, @NonNull String country) {
                return new ViewModel (result.getTitle(), country);
            }
        });
    }
}
