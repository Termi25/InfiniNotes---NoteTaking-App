package com.ase.aplicatienotite.baze_date.local.view.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.notite.Notita;

public class SectiuneNotiteJoinViewHolder extends RecyclerView.ViewHolder{
    private final TextView tvTitluNotita;
    private final TextView tvCorpPreviewNotita;
    private static Context context;
    public SectiuneNotiteJoinViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitluNotita=itemView.findViewById(R.id.tvNumeNotita);
        tvCorpPreviewNotita=itemView.findViewById(R.id.tvCorpPreviewNotita);
    }

    public void bind(Notita notita){
        tvTitluNotita.setText(notita.getTitlu());
        tvCorpPreviewNotita.setText(notita.getCorp());
    }

    public static SectiuneNotiteJoinViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_notita,parent,false);
        context=parent.getContext();
        return new SectiuneNotiteJoinViewHolder(view);
    }
}
