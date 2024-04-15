package com.ase.aplicatienotite.main.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ase.aplicatienotite.R;
import com.ase.aplicatienotite.clase.notite.Notita;
import com.ase.aplicatienotite.clase.sectiune.Sectiune;

public class AlarmBroadcastReceiverReminderNotita extends BroadcastReceiver {
    public static final String ACTION_ALARM = "en.proft.alarms.ACTION_ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_ALARM.equals(intent.getAction())) {
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
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                            String.valueOf(notita.getNotitaId()))
                            .setSmallIcon(R.drawable.logo_centrat_app)
                            .setContentTitle("Reminder \""+notita.getTitlu()+"\"")
                            .setContentText("Nu uita de notița \""+notita.getTitlu()
                                    +"\" care se regăsește în secțiunea "
                                    +sectiune.getDenumireSectiune()+'.')
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManager notificationManager = (NotificationManager)context.getSystemService(
                            context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        String channelId = String.valueOf(notita.getNotitaId());
                        NotificationChannel channel = new NotificationChannel(
                                channelId,
                                notita.getTitlu(),
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
                        builder.setChannelId(channelId);
                    }
                    notificationManager.notify(notita.getNotitaId(),builder.build());
                }
            }catch (Exception e){
                Log.e("Error",context.getString(R.string.error_broadcast_receiver_notita_reminder));
            }
        }
    }
}
