package com.ase.aplicatienotite.baze_date.local.view.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.notite.NotitaLista;


public class SectiuneNotiteListaJoinViewHolder extends RecyclerView.ViewHolder {
    private static Context context;
    private TextView tvTitluLista;
    private Button btnStergereLista;
    public SectiuneNotiteListaJoinViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvTitluLista=itemView.findViewById(R.id.tvDenumireLista);
        this.btnStergereLista=itemView.findViewById(R.id.btnStergereLista);
    }

    public void bind(NotitaLista notitaLista){
        this.tvTitluLista.setText(notitaLista.getTitlu());
        this.btnStergereLista.setOnClickListener(v->{

        });
    }

    public static SectiuneNotiteListaJoinViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_lista,parent,false);
        context=parent.getContext();
        return new SectiuneNotiteListaJoinViewHolder(view);
    }
}
