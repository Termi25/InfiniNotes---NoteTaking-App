package com.ase.aplicatienotite.adaptoare;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ase.aplicatienotite.baze_date.local.view.holder.ListaNotiteJoinViewHolder;
import com.ase.aplicatienotite.clase.notite.ElementLista;

public class AdapterElementeLista extends ListAdapter<ElementLista, ListaNotiteJoinViewHolder> {
    public AdapterElementeLista(@NonNull DiffUtil.ItemCallback<ElementLista> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ListaNotiteJoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ListaNotiteJoinViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNotiteJoinViewHolder holder, int position) {
        ElementLista elementListaCurent=getItem(position);
        holder.bind(elementListaCurent);
    }

    public static class ElementListaDiff extends DiffUtil.ItemCallback<ElementLista>{

        @Override
        public boolean areItemsTheSame(@NonNull ElementLista oldItem, @NonNull ElementLista newItem) {
            return oldItem.getTitlu().equals(newItem.getTitlu());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ElementLista oldItem, @NonNull ElementLista newItem) {
            return oldItem.getCorp().equals(newItem.getCorp());
        }
    }
}
