package com.ase.aplicatienotite.baze_date.local.view.holder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.adaptoare.AdapterNotita;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class SectiuneViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvNumeSectiune;
    private final TextView tvNumeNotita1;
    private final TextView tvNumeNotita2;
    public SectiuneViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNumeSectiune=itemView.findViewById(R.id.tvNumeSectiune);
        tvNumeNotita1=itemView.findViewById(R.id.tvNotita1);
        tvNumeNotita2=itemView.findViewById(R.id.tvNotita2);
    }
    public void bind(Sectiune sectiune){
        tvNumeSectiune.setText(sectiune.getDenumireSectiune());
        if(sectiune.getNotite()!=null){
            Log.w("TEST",sectiune.getNotite().get(0).getTitlu());
            Log.w("TEST", String.valueOf(sectiune.getNotite().size()));
            if(sectiune.getNotite().size()>1){
                Log.w("TEST", sectiune.getNotite().toString());
                tvNumeNotita1.setText(sectiune.getNotite().get(0).getTitlu());
                tvNumeNotita2.setText(sectiune.getNotite().get(1).getTitlu());
            }else{
                if(sectiune.getNotite().size()>0){
                    tvNumeNotita1.setText(sectiune.getNotite().get(0).getTitlu());
                }
            }
        }
    }

    public static SectiuneViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sectiune,parent,false);
        return new SectiuneViewHolder(view);
    }
}
