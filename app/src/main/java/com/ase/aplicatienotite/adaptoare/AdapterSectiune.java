package com.ase.aplicatienotite.adaptoare;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.util.ArrayList;
import java.util.List;

public class AdapterSectiune extends ArrayAdapter<Sectiune> {
    private Context context;
    private int resource;

    private List<Sectiune> listaSectiuni;
    private LayoutInflater inflater;
    public AdapterSectiune(@NonNull Context context, int resource,
                           List<Sectiune> objects, LayoutInflater inflater) {
        super(context,resource,objects);
        this.context=context;
        this.listaSectiuni=objects;
        this.resource=resource;
        this.inflater=inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(resource,parent,false);
        Sectiune sectiune=listaSectiuni.get(position);
        if(sectiune!=null){
            TextView tvNumeSectiune=view.findViewById(R.id.tvNumeSectiune);
            ListView lvNotite=view.findViewById(R.id.lvListe);
            if(sectiune.getNotite()!=null){
                if(sectiune.getNotite().size()>2){
                    List<Notita>listaRedusa=new ArrayList<>();
                    listaRedusa.add(sectiune.getNotite().get(0));
                    listaRedusa.add(sectiune.getNotite().get(1));
                    AdapterNotita adapter=new AdapterNotita(context,R.layout.view_notita,listaRedusa,inflater);
                    lvNotite.setAdapter(adapter);
                }
                else{
                    AdapterNotita adapter=new AdapterNotita(context,R.layout.view_notita,sectiune.getNotite(),inflater);
                    lvNotite.setAdapter(adapter);
                }
            }
            if(sectiune.getDenumireSectiune().length()<20){
                tvNumeSectiune.setText(sectiune.getDenumireSectiune());
                tvNumeSectiune.setTextColor(Color.parseColor("#653024"));
            }else{
                tvNumeSectiune.setText(sectiune.getDenumireSectiune());
                tvNumeSectiune.setTextColor(Color.BLACK);
            }
        }
        return view;
    }
}
