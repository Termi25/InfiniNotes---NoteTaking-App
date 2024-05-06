package com.ase.aplicatienotite.main.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.baze_date.local.database.NotiteDB;
import com.ase.aplicatienotite.clase.legaturi_db.ListaNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteJoin;
import com.ase.aplicatienotite.clase.legaturi_db.SectiuneNotiteListaJoin;
import com.ase.aplicatienotite.clase.notite.ElementLista;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.notite.NotitaLista;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM = "en.proft.alarms.ACTION_ALARM";
    public static final String ACTION_DELETE = "DELETE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_ALARM.equals(intent.getAction())) {
            setareNotificare(intent, context);
        }else{
            stergereNotite(context);
        }
    }

    private void setareNotificare(Intent intent, Context context) {
        try{
            Notita notita= null;
            Sectiune sectiune=null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                notita = intent.getSerializableExtra("notita", Notita.class);
                sectiune=intent.getSerializableExtra("sectiune",Sectiune.class);
            }else{
                notita= (Notita) intent.getSerializableExtra("notita");
                sectiune= (Sectiune) intent.getSerializableExtra("sectiune");
            }

            if(notita!=null && sectiune!=null){
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                        String.valueOf(notita.getNotitaId()))
                        .setSmallIcon(R.drawable.logo_centrat_app)
                        .setContentTitle("Reminder \""+notita.getTitlu()+"\" - ora "+sdf.format(calendar.getTime()))
                        .setContentText("Nu uita de notița \""+notita.getTitlu()
                                +"\" care se regăsește în secțiunea "
                                +sectiune.getDenumireSectiune()+'.')
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                        Context.NOTIFICATION_SERVICE);

                String channelId = String.valueOf(notita.getNotitaId());
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        notita.getTitlu(),
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);

                notificationManager.notify(notita.getNotitaId(),builder.build());
            }
        }catch (Exception e){
            Log.e("Error",context.getString(R.string.error_broadcast_receiver_notita_reminder));
        }
    }

    private void stergereNotite(Context context) {
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
