package com.ase.aplicatienotite.adaptoare;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ase.aplicatienotite.baze_date.local.view.holder.SectiuneNotiteJoinViewHolder;
import com.ase.aplicatienotite.baze_date.local.view.model.SectiuneNotiteJoinViewModel;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.Objects;

public class AdapterNotita extends ListAdapter<Notita, SectiuneNotiteJoinViewHolder> {

    private SectiuneNotiteJoinViewModel sectiuneNotiteJoinViewModel;

    public AdapterNotita(@NonNull DiffUtil.ItemCallback<Notita> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public SectiuneNotiteJoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SectiuneNotiteJoinViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SectiuneNotiteJoinViewHolder holder, int position) {
        Notita curenta=getItem(position);
        holder.bind(curenta);
    }

    public static class NotiteDiff extends DiffUtil.ItemCallback<Notita>{

        @Override
        public boolean areItemsTheSame(@NonNull Notita oldItem, @NonNull Notita newItem) {
            return Objects.equals(oldItem.getTitlu(), newItem.getTitlu());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notita oldItem, @NonNull Notita newItem) {
            return oldItem.getCorp().equals(newItem.getCorp());
        }
    }
}
