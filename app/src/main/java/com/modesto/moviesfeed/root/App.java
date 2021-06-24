package com.modesto.moviesfeed.root;

import android.app.Application;

import com.modesto.moviesfeed.http.MovieExtraInfoApiModule;
import com.modesto.moviesfeed.http.MovieTittleApiModule;
import com.modesto.moviesfeed.movies.MoviesModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .movieExtraInfoApiModule(new MovieExtraInfoApiModule())
                .moviesModule(new MoviesModule())
                .movieTittleApiModule(new MovieTittleApiModule())
                .build();
    }

    public ApplicationComponent getComponent(){
        return component;
    }

}
