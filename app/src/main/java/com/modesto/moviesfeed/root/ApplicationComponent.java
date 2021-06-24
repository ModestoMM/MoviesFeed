package com.modesto.moviesfeed.root;

import com.modesto.moviesfeed.MainActivity;
import com.modesto.moviesfeed.http.MovieExtraInfoApiModule;
import com.modesto.moviesfeed.http.MovieTittleApiModule;
import com.modesto.moviesfeed.movies.MoviesModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules =
        {ApplicationModule.class,
        MoviesModule.class,
        MovieTittleApiModule.class,
        MovieExtraInfoApiModule.class
        })
public interface ApplicationComponent {

    void inject(MainActivity tarjet);

}
