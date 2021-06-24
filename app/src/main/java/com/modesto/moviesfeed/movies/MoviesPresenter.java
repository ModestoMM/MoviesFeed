package com.modesto.moviesfeed.movies;

import androidx.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MoviesPresenter implements MoviesMVP.Presenter{

    @NonNull
    private MoviesMVP.View view;
    private MoviesMVP.Model model;

    //Esta es la que vamos a utilizar para suscribirnos a los flujos de eventos
    private Disposable subscription =null;

    public MoviesPresenter(MoviesMVP.Model model){
        this.model = model;
    }

    @Override
    public void loadData() {
        subscription = model.result()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //Aqui se utiliza este sibscribe ya que no hay yna desuscripcion directa
                .subscribeWith(new DisposableObserver<ViewModel>() {

                //Aqui identificamos si hay datos y hacemos que se actualiza
                // el modelo y el adapter
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ViewModel viewModel) {
                        if(view != null){
                            view.updateData(viewModel);
                        }
                    }
                //Aqui identificamos si hubo un error al descargar las peliculas
                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                        if(view != null){
                            view.showSnackBar("Error al Descargar las Peliculas");
                        }
                    }

                    @Override
                    public void onComplete() {
                        if(view != null){
                            view.showSnackBar("Informacion descargada con exito!!");
                        }
                    }
                });
    }

    @Override
    public void rxJavaUnsuscribe() {
        if (subscription != null && !subscription.isDisposed()){
            subscription.dispose();
        }
    }

    @Override
    public void setView(MoviesMVP.View view) {
        this.view = view;
    }
}
