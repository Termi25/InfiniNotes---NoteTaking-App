package com.ase.aplicatienotite.baze_date.local.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;

public class SectiuneViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvNumeSectiune;
    public SectiuneViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNumeSectiune=itemView.findViewById(R.id.tvNumeSectiune);
    }
    public void bind(String text){
        tvNumeSectiune.setText(text);
    }

    static SectiuneViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sectiune,parent,false);
        return new SectiuneViewHolder(view);
    }
}
