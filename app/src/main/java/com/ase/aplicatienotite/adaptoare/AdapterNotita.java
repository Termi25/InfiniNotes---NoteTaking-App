package com.ase.aplicatienotite.adaptoare;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.notite.Notita;

import java.util.List;

public class AdapterNotita extends ArrayAdapter<Notita> {
    private Context context;
    private int resource;
    private List<Notita> listaNotite;
    private LayoutInflater inflater;
    public AdapterNotita(@NonNull Context context, int resource,
                           List<Notita> objects, LayoutInflater inflater) {
        super(context,resource,objects);
        this.context=context;
        this.listaNotite=objects;
        this.resource=resource;
        this.inflater=inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(resource,parent,false);
        Notita notita=listaNotite.get(position);
        Log.println(Log.DEBUG,"NOTITA", String.valueOf(listaNotite.size()));
        if(notita!=null){
            TextView tvNumeNotita=view.findViewById(R.id.tvNumeNotita);

            tvNumeNotita.setText(notita.getTitlu());
            tvNumeNotita.setTextColor(Color.parseColor("#653024"));
        }
        return view;
    }
}
