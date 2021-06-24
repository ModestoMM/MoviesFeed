package com.modesto.moviesfeed.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.modesto.moviesfeed.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder>  {

    private List<ViewModel> list;

    public ListAdapter(List<ViewModel> lists){
        this.list = lists;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ListItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        //Pasamos los parametros a los textView de los textos que usaremos
        //en este caso nuestro modelo nos ayudara para consegior el titulo y pais
        //ya que sacaremos a esos datos gracias a la posicion del elemento
        holder.tittleMovie.setText(list.get(position).getTitle());
        holder.countryName.setText(list.get(position).getCountry());
    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text_view_fragment_tittle)
        public TextView tittleMovie;

        @BindView(R.id.text_view_fragment_country)
        public TextView countryName;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            //Para bindear ocupo deos cosas el target y la lista objetivo ya que nos encontramos
            //en un adapter y no en una actividad
            ButterKnife.bind(this, itemView);
        }
    }
}
