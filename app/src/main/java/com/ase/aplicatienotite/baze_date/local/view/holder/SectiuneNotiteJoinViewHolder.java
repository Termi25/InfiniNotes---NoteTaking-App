package com.ase.aplicatienotite.baze_date.local.view.holder;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.main.activitati.ActivitateEditeazaNotita;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SectiuneNotiteJoinViewHolder extends RecyclerView.ViewHolder{
    private final TextView tvTitluNotita;
    private final TextView tvCorpPreviewNotita;
    private final Button btnStergereNotita;
    private final Button btnEditareNotita;
    private final CheckBox cbChecked;
    private static Context context;

    public SectiuneNotiteJoinViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvTitluNotita=itemView.findViewById(R.id.tvNumeNotita);
        this.tvCorpPreviewNotita=itemView.findViewById(R.id.tvCorpPreviewNotita);
        this.btnStergereNotita=itemView.findViewById(R.id.btnStergereNotita);
        this.btnEditareNotita=itemView.findViewById(R.id.btnEditareNotita);
        this.cbChecked=itemView.findViewById(R.id.cbNotita);
    }

    public void bind(Notita notita){
        tvTitluNotita.setText(notita.getTitlu());
        tvCorpPreviewNotita.setText(notita.getCorp());

        setareButonEditareNotita(notita);

        setareButonStergere(notita);

        setareChecked(notita);
    }

    private void setareButonEditareNotita(Notita notita) {
        btnEditareNotita.setOnClickListener(v->{
            Intent intent=new Intent(context, ActivitateEditeazaNotita.class);
            intent.putExtra("Notita",notita);
            startActivity(context,intent,null);
        });
    }

    private void setareButonStergere(Notita notita) {
        btnStergereNotita.setOnClickListener(v->{
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                List<SectiuneNotiteJoin> listaLegaturi=new ArrayList<>();
                listaLegaturi=db.getSectiuneNotiteJoinDao().getLegaturiCuNotita(notita.getNotitaId());
                for(int i=0;i<listaLegaturi.size();i++){
                    db.getSectiuneNotiteJoinDao().deleteLegatura(listaLegaturi.get(i));
                }
                db.getNotitaDao().deleteNotita(notita);
            });
            Toasty.success(context,R.string.modificari_succes, Toast.LENGTH_LONG).show();
        });
    }

    private void setareChecked(Notita notita) {
        cbChecked.setChecked(notita.isChecked());
        cbChecked.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notita.setChecked(isChecked);
            NotiteDB.databaseWriteExecutor.execute(()->{
                NotiteDB db=NotiteDB.getInstance(context);
                db.getNotitaDao().updateNotita(notita);
            });
        });
    }

    public static SectiuneNotiteJoinViewHolder create(ViewGroup parent){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_notita,parent,false);
        context=parent.getContext();
        return new SectiuneNotiteJoinViewHolder(view);
    }
}
