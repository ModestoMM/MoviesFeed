package com.modesto.moviesfeed.movies;

import com.modesto.moviesfeed.http.MoviesApisService;
import com.modesto.moviesfeed.http.MoviesExtraInfoApisService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MoviesModule {

    @Provides
    public MoviesMVP.Presenter provideMoviesPresenter(MoviesMVP.Model model){
        return new MoviesPresenter(model);
    }

    @Provides
    public MoviesMVP.Model provideMoviesModel(Repository repository){
        return new MoviesModel(repository);
    }

    //Agregamos singleton ya que solo queremos un repositorio
    @Singleton
    @Provides
    public Repository provideMoviesRepository(MoviesApisService moviesApisService, MoviesExtraInfoApisService moviesExtraInfoApisService){
        return new MoviesRepository(moviesApisService, moviesExtraInfoApisService);
    }
}
