package com.ase.aplicatienotite.baze_date.local.view.holder;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;
import com.ase.aplicatienotite.main.activitati.ActivitateVizualElementeListaNotite;
import com.ase.aplicatienotite.main.activitati.ActivitateVizualNotiteSectiune;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SectiuneNotiteListaJoinViewHolder extends RecyclerView.ViewHolder {
    private static Context context;
    private final TextView tvCorpLista;
    private final TextView tvTitluLista;
    private final Button btnStergereLista;
    private final Button btnVizualizareElementeDinLista;
    public SectiuneNotiteListaJoinViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvTitluLista=itemView.findViewById(R.id.tvNumeNotita);
        this.tvCorpLista=itemView.findViewById(R.id.tvCorpPreviewNotita);
        this.btnStergereLista=itemView.findViewById(R.id.btnStergereNotita);
        this.btnVizualizareElementeDinLista = itemView.findViewById(R.id.btnEditareNotita);
    }

    public void bind(NotitaLista notitaLista){
        this.tvTitluLista.setText(notitaLista.getTitlu());
        this.tvCorpLista.setText(notitaLista.getCorp());

        setareButonStergereNotitaLista(notitaLista);

        setareButonVizualNotite(notitaLista);
    }

    private void setareButonStergereNotitaLista(NotitaLista notitaLista) {
        this.btnStergereLista.setOnClickListener(v->{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);

                List<ListaNotiteJoin> listaLegaturi=db.getListaNotiteJoinDao()
                        .getLegaturiCuLista(notitaLista.getNotitaId());

                for(int i=0;i<listaLegaturi.size();i++){
                    ElementLista element=db.getElementListaDao()
                            .getElementListaDupaId(listaLegaturi.get(i).notitaId);

                    db.getListaNotiteJoinDao().deleteLegatura(listaLegaturi.get(i));

                    db.getElementListaDao().deleteElementLista(element);
                }

                List<SectiuneNotiteListaJoin> listaLegaturiSectiune=db.getSectiuneNotiteListaJoinDao()
                        .getLegaturiCuNotitaLista(notitaLista.getNotitaId());

                for(int i=0;i<listaLegaturiSectiune.size();i++){
                    db.getSectiuneNotiteListaJoinDao().deleteLegatura(listaLegaturiSectiune.get(i));
                }

                db.getNotitaListaDao().deleteNotitaListaDupaId(notitaLista.getNotitaId());
            });
            Toasty.success(context,R.string.modificari_succes, Toast.LENGTH_LONG).show();
        });
    }

    private void setareButonVizualNotite(NotitaLista notitaLista) {
        this.btnVizualizareElementeDinLista.setOnClickListener(v -> {
            Intent intent=new Intent(context, ActivitateVizualElementeListaNotite.class);
            intent.putExtra("notitaLista",notitaLista);
            startActivity(context,intent,null);
        });
    }

    public static SectiuneNotiteListaJoinViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_notita,parent,false);
        context=parent.getContext();
        return new SectiuneNotiteListaJoinViewHolder(view);
    }
}
