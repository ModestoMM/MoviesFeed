package com.modesto.moviesfeed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.modesto.moviesfeed.movies.ListAdapter;
import com.modesto.moviesfeed.movies.MoviesMVP;
import com.modesto.moviesfeed.movies.ViewModel;
import com.modesto.moviesfeed.root.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesMVP.View {

    private static final String TAG = MainActivity.class.getName();

    //Utilizando el Butterknife en lugardel fiendViewId par las referencias
    @BindView(R.id.activity_root_view)
    ViewGroup mRootView;

    @BindView(R.id.recycler_view_movies)
    RecyclerView mRecyclerView;

    @Inject
    MoviesMVP.Presenter presenter;

    private ListAdapter listAdapter;
    //Aqui guardaremos lo que recuperemos del servidor
    private List<ViewModel> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);

        //Configuracion ADAPTER y RECYCLERVIEW//
        listAdapter = new ListAdapter(resultList);
        mRecyclerView.setAdapter(listAdapter);
        //Ponemos una decoracion al recyclerView
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //Para que todas las celdas sean del mismo tamaño
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    //////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //En esta parte finalizamos la descarga y actualizacion cnstante
        presenter.rxJavaUnsuscribe();
        resultList.clear();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateData(ViewModel viewModel) {
        //Agregamos ese modelo que nos manden a la lista que tenemos y agregamos al adapter
        resultList.add(viewModel);
        //Notificamos que ha llegado un nuevo modelo de vista y hay un cambio
        listAdapter.notifyItemInserted(resultList.size()-1);
        Log.d(TAG, "Informacion Nueva: "+viewModel.getTitle());
    }

    @Override
    public void showSnackBar(String mensaje) {
        //Agregamos un ScackBar
        Snackbar.make(mRootView, mensaje, Snackbar.LENGTH_LONG).show();
    }
}