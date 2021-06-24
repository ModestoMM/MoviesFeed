package com.modesto.moviesfeed.http;

import com.google.gson.Gson;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieExtraInfoApiModule {

    public final String BASE_URL = "http://www.omdbapi.com/";
    public final String API_KEY = "69388739";

    @Provides
    public OkHttpClient provideClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //Esta parte es mas larga porque en el interceptor tendremos que configurarlo desde cero
        //ya que mandaremos aqui la clave del API_KEY
        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //Sacamos la peticion chase
                Request request = chain.request();
                //Y aqui pasamos nuestra API_KEY desde un interceptor y asi ya no tener que llamarla
                //en el main a la hora de traer los dats
                HttpUrl url = request.url().newBuilder().addQueryParameter("apikey", API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        }).build();
    }

    @Provides
    public Retrofit provideRetrofit(String baseUrl, OkHttpClient cliente){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(cliente)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    public MoviesExtraInfoApisService provideApisService(){
        return provideRetrofit(BASE_URL, provideClient()).create(MoviesExtraInfoApisService.class);
    }
}
