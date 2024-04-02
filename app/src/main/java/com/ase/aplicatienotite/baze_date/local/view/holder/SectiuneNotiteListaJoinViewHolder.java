package com.ase.aplicatienotite.baze_date.local.view.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.notite.NotitaLista;


public class SectiuneNotiteListaJoinViewHolder extends RecyclerView.ViewHolder {
    private static Context context;
    public SectiuneNotiteListaJoinViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(NotitaLista notitaLista){

    }

    public static SectiuneNotiteJoinViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_notita,parent,false);
        context=parent.getContext();
        return new SectiuneNotiteJoinViewHolder(view);
    }
}
