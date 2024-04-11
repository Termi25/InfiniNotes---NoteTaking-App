package com.ase.aplicatienotite.baze_date.local.view.holder;

import static androidx.core.content.ContextCompat.getDrawable;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.main.activitati.ActivitateVizualNotiteSectiune;


public class SectiuneViewHolder extends RecyclerView.ViewHolder {
    static ActivityResultLauncher<Intent> launcher;
    private final TextView tvNumeSectiune;
    private final TextView tvNumeNotita1;
    private final TextView tvNumeLista;
    private final Button btnVizualizareNotiteDinSectiune;

    public static Context context;
    
    public SectiuneViewHolder(@NonNull View itemView) {
        super(itemView);
        tvNumeSectiune=itemView.findViewById(R.id.tvNumeSectiune);
        tvNumeNotita1=itemView.findViewById(R.id.tvNotita1);
        tvNumeLista=itemView.findViewById(R.id.tvLista);
        btnVizualizareNotiteDinSectiune=itemView.findViewById(R.id.btnVizualNotiteSectiune);
    }
    public void bind(Sectiune sectiune){
        tvNumeSectiune.setText(sectiune.getDenumireSectiune());
        tvNumeNotita1.setText("");
        tvNumeLista.setText("");

        try{
            incarcareUI(sectiune);
        }catch (Exception e){
            e.printStackTrace();
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

    private void incarcareUI(Sectiune sectiune){

        try{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                sectiune.setNotite(db.getSectiuneNotiteJoinDao()
                        .getNotitePentruSectiune(sectiune.getSectiuneId()));

                sectiune.setNotiteLista(db.getSectiuneNotiteListaJoinDao()
                        .getNotiteListaPentruSectiune(sectiune.getSectiuneId()));

                if(sectiune.getNotite()!=null){
                    if(!sectiune.getNotite().isEmpty()){
                        tvNumeNotita1.setText(sectiune.getNotite().get(0).getTitlu());
                    }
                }
                if(sectiune.getNotiteLista()!=null){
                    if(!sectiune.getNotiteLista().isEmpty()){
                        tvNumeLista.setText(String.format("Lista: %s", sectiune.getNotiteLista().get(0).getTitlu()));
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            switch (sectiune.getCuloareSectiune()){
                case MARO:{
                    itemView.setBackground(getDrawable(context,R.drawable.border_lv_principal));
                    break;
                }
                case ROSU:{
                    itemView.setBackground(getDrawable(context,R.drawable.border_lv_red));
                    break;
                }
                case ALBASTRU:{
                    itemView.setBackground(getDrawable(context,R.drawable.border_lv_blue));
                    break;
                }
                case VERDE:{
                    itemView.setBackground(getDrawable(context,R.drawable.border_lv_green));
                    break;
                }
                case VIOLET:{
                    itemView.setBackground(getDrawable(context,R.drawable.border_lv_purple));
                    break;
                }
                default:
                    itemView.setBackground(getDrawable(context,R.drawable.border_lv_principal));
            }
        }

    }
}
