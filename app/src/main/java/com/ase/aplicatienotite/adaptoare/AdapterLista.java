package com.ase.aplicatienotite.adaptoare;

import static com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneViewHolder.context;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneNotiteListaJoinViewHolder;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteListaJoinViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.ArrayList;
import java.util.List;

public class AdapterLista extends ListAdapter<NotitaLista, SectiuneNotiteListaJoinViewHolder> {
    private SectiuneNotiteListaJoinViewModel sectiuneNotiteListaJoinViewModel;
    public AdapterLista(@NonNull DiffUtil.ItemCallback<NotitaLista> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SectiuneNotiteListaJoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SectiuneNotiteListaJoinViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SectiuneNotiteListaJoinViewHolder holder, int position) {
        NotitaLista curenta=getItem(position);
        holder.bind(curenta);
    }

    public static class NotitaListaDiff extends DiffUtil.ItemCallback<NotitaLista>{

        @Override
        public boolean areItemsTheSame(@NonNull NotitaLista oldItem, @NonNull NotitaLista newItem) {
            return oldItem.getNotitaId()== newItem.getNotitaId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NotitaLista oldItem, @NonNull NotitaLista newItem) {
            return oldItem.getTitlu().equals(newItem.getTitlu());
        }
    }
}
