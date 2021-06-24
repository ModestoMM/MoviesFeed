package com.modesto.moviesfeed.root;

import android.app.Application;
import android.content.Context;
import android.view.ViewGroup;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Singleton
    @Provides
    public Context providerContext(){
        return application;
    }

}
