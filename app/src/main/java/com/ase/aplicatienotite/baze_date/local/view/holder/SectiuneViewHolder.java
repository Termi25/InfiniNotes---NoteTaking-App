package com.ase.aplicatienotite.baze_date.local.view.holder;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.main.ActivitateVizualNotiteSectiune;


public class SectiuneViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvNumeSectiune;
    private final TextView tvNumeNotita1;
    private final TextView tvNumeNotita2;
    private final Button btnVizualizareNotiteDinSectiune;
    private static Context context;
    public SectiuneViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNumeSectiune=itemView.findViewById(R.id.tvNumeSectiune);
        tvNumeNotita1=itemView.findViewById(R.id.tvNotita1);
        tvNumeNotita2=itemView.findViewById(R.id.tvNotita2);
        btnVizualizareNotiteDinSectiune=itemView.findViewById(R.id.btnVizualNotiteSectiune);
    }
    public void bind(Sectiune sectiune){
        tvNumeSectiune.setText(sectiune.getDenumireSectiune());
        if(sectiune.getNotite()!=null){
            if(sectiune.getNotite().size()>1){
                tvNumeNotita1.setText(sectiune.getNotite().get(0).getTitlu());
                tvNumeNotita2.setText(sectiune.getNotite().get(1).getTitlu());
            }else{
                if(sectiune.getNotite().size()>0){
                    tvNumeNotita1.setText(sectiune.getNotite().get(0).getTitlu());
                }
            }
        }
        btnVizualizareNotiteDinSectiune.setOnClickListener(v -> {
            Intent intent=new Intent(context, ActivitateVizualNotiteSectiune.class);
            intent.putExtra("codSectiune",sectiune.getSectiuneId());
            startActivity(context,intent,null);
        });
    }

    public static SectiuneViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sectiune,parent,false);
        context=parent.getContext();
        return new SectiuneViewHolder(view);
    }
}
