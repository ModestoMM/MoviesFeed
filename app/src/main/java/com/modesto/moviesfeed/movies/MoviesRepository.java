package com.modesto.moviesfeed.movies;

import com.modesto.moviesfeed.http.MoviesApisService;
import com.modesto.moviesfeed.http.MoviesExtraInfoApisService;
import com.modesto.moviesfeed.http.apimodel.OmdbApi;
import com.modesto.moviesfeed.http.apimodel.Result;
import com.modesto.moviesfeed.http.apimodel.TopMoviesRated;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MoviesRepository implements Repository {

    private MoviesApisService moviesApisService;
    private MoviesExtraInfoApisService extraInfoApisService;

    private List<String> countries;
    private List<Result> results;
    private long lastTimestamp;

    private static final long CACHE_LIFETIME = 20 * 1000; //Cache que durara 20 segundos

    public MoviesRepository(MoviesApisService service, MoviesExtraInfoApisService extra){
        this.moviesApisService = service;
        this.extraInfoApisService = extra;

        //Indicamos que se refresco ahora misma la api ya que hacemos la llamada en esta
        //parte a los servidores, NOTA!! currentTimeMillis() nos dara el tiempoactual
        this.lastTimestamp = System.currentTimeMillis();

        this.countries = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    //Con este metodo nos darempos cuenta si ya pasaron los 20 segundos de la ultima vez
    //que se actualizo la informacion de la API y si es asi mandaremos un true y volveremos a
    //actualizar y si no sera un false y seguiremos esperando esto es para que sea mas dinamica
    //la actualizacion y no se ande actualizando a cada rato
    public boolean isUpdate(){
        return (System.currentTimeMillis() - lastTimestamp) < CACHE_LIFETIME;
    };

    @Override
    public Observable<Result> getResultFromNetwork() {

        //Obtendremos las peliculas de esta manera al utilizar el concat pedimos otra pagina
        //para asi hacer que me traega no 20 si no 60 peliculas
        Observable<TopMoviesRated> topMoviesRatedObservable = moviesApisService.getTopMoviesRated(1)
                .concatWith(moviesApisService.getTopMoviesRated(2))
                .concatWith(moviesApisService.getTopMoviesRated(3));

        //En este caso no usaremos FlatMap ya que no conserva el orden y ocupamos el orden para
        // que vaya sacando el pais de la pelicula una por una sin orden tendremos problemas
        return topMoviesRatedObservable
                .concatMap(new Function<TopMoviesRated, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(@NonNull TopMoviesRated topMoviesRated){
                        return Observable.fromIterable( topMoviesRated.getResults());
                    }
                    //Un consumer significa hacer algo con el consuirlo o utilizarlo en cierto modo
                }).doOnNext(new Consumer<Result>() {
                    @Override
                    public void accept(Result result){
                        //Al final cuando saquemos los resultados los guardaremos en la lista
                        //que creamos anteriormente
                        results.add(result);
                    }
                });
    }

    @Override
    public Observable<Result> getResultFromCache() {
        if(isUpdate()){
            return Observable.fromIterable(results);
        }else{
            lastTimestamp = System.currentTimeMillis();
            results.clear();
            return Observable.empty();
        }
    }

    @Override
    public Observable<Result> getResultData() {
        //El switchIfEmpty significa que si estavacio en lugar de hacer el metodo
        //anterior se hara el siguiente
        return getResultFromCache().switchIfEmpty(getResultFromNetwork());
    }

    @Override
    public Observable<String> getCountryData() {
        return getCountryFromCache().switchIfEmpty(getCountryFromNetwork());
    }

    @Override
    public Observable<String> getCountryFromNetwork() {
        //Utilizaos el resultado del metodo de resultados que traera el titulo de la pelicula
        //para utilizarla en este metodo y mandar de parametro el titulo que es el dato
        //que necesitamos para poder sacar el pais de la otra API que estamos usando
        return getResultFromNetwork().concatMap(new Function<Result, Observable<OmdbApi>>() {
            @Override
            public Observable<OmdbApi> apply(@NonNull Result result){
                return extraInfoApisService.getExtraInfoMovie(result.getTitle());
            }
            //Volvemos a utilizar este concat para sacar el string del observable y asi
            //de esta manera evitar el error que nos daba arriba ya con este concat sacaremos
            //el nombre del pais dela pelicula
        }).concatMap(new Function<OmdbApi, Observable<String>>() {
            @Override
            public Observable<String> apply(@NonNull OmdbApi omdbApi){
                //El fromIterable se utiliza cuando es una coleccion de elementos mientras
                //que el just se utiliza cuando solo es un elemento
                if(omdbApi == null || omdbApi.getCountry() == null){
                    return Observable.just("No Encontramos el Pais :-(");
                }else{
                    return Observable.just(omdbApi.getCountry());
                }
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String country){
                countries.add(country);
            }
        });
    }

    @Override
    public Observable<String> getCountryFromCache() {
        if(isUpdate()){
            return Observable.fromIterable(countries);
        }else{
            lastTimestamp = System.currentTimeMillis();
            countries.clear();
            return Observable.empty();
        }
    }
}
