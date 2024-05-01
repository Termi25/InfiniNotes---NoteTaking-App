package com.ase.aplicatienotite.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;

import java.util.List;

public class RegularDeleteReceiverNotite extends BroadcastReceiver {
    public static final String ACTION_ALARM = "en.proft.alarms.ACTION_ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION_ALARM.equals(intent.getAction())){
            NotiteDB.databaseWriteExecutor.execute(()->{
                try{
                    NotiteDB db=NotiteDB.getInstance(context);
                    List<Notita> listaNotiteChecked=db.getNotitaDao().getNotiteChecked();
                    for(Notita notita:listaNotiteChecked){
                        stergereNotita(notita,context);
                    }

                    List<NotitaLista> listaNotiteListaChecked=db.getNotitaListaDao().getNotiteListaChecked();
                    for(NotitaLista notitaLista: listaNotiteListaChecked){
                        stergereNotitaLista(notitaLista,context);
                    }

                    List<ElementLista> listaElementeListaChecked=db.getElementListaDao().getElementeListaChecked();
                    for(ElementLista elementLista:listaElementeListaChecked){
                        stergereElementLista(elementLista,context);
                    }
                }catch (Exception e){
                    Log.e("Error","Periodic delete error in broadcastReceiver");
                }
            });
        }
    }

    private void stergereNotita(Notita notita,Context context){
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB db=NotiteDB.getInstance(context);
            List<SectiuneNotiteJoin> listaLegaturi=db.getSectiuneNotiteJoinDao()
                    .getLegaturiCuNotita(notita.getNotitaId());

            for(int i=0;i<listaLegaturi.size();i++){
                db.getSectiuneNotiteJoinDao().deleteLegatura(listaLegaturi.get(i));
            }
            db.getNotitaDao().deleteNotita(notita);
        });
    }

    private void stergereNotitaLista(NotitaLista notitaLista,Context context){
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
    }

    private void stergereElementLista(ElementLista elementLista, Context context) {
        NotiteDB.databaseWriteExecutor.execute(()->{
            NotiteDB db=NotiteDB.getInstance(context);
            List<ListaNotiteJoin> listaLegaturi=db.getListaNotiteJoinDao()
                    .getLegaturiCuElementulLista(elementLista.getNotitaId());

            for(int i=0;i<listaLegaturi.size();i++){
                db.getListaNotiteJoinDao().deleteLegatura(listaLegaturi.get(i));
            }
            db.getElementListaDao().deleteElementListaDupaId(elementLista.getNotitaId());
        });
    }
}
