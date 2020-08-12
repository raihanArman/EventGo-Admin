package com.example.eventgoadmin.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import com.example.eventgoadmin.MainActivity;
import com.example.eventgoadmin.R;
import com.example.eventgoadmin.ui.DetailEventActivity;
import com.example.eventgoadmin.util.Utils;

public class UsulanReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock =powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag");
        wakeLock.acquire();
        String action = intent.getAction();
        if (action.equals(Utils.NOTIF_EVENT_USULAN)){
            String title =intent.getStringExtra("title");
            String idEvent = intent.getStringExtra("id_event");
            sendNotification(context, title, idEvent);
        }
    }


    private void sendNotification(Context context, String title, String idEvent){
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("id_event", idEvent);
        intent.putExtra(Utils.NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String NOTIF_CHANNEL_ID = "event_channel_id";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int important = NotificationManager.IMPORTANCE_HIGH;
            String NOTIFICATION_CHANNEL_NAME = "event channel";
            NotificationChannel channel =new NotificationChannel(
                    NOTIF_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, important
            );
        }

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText("Lihat usulan event terbaru")
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(uri)
                .setAutoCancel(true);

        int NOTIF_ID = 177;
        notificationManager.notify(NOTIF_ID, builder.build());

    }

}
