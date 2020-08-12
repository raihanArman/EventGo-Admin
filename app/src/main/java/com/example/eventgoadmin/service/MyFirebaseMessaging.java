package com.example.eventgoadmin.service;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.eventgoadmin.receiver.UsulanReceiver;
import com.example.eventgoadmin.util.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null){
            Map<String, String> data = remoteMessage.getData();
            String title = data.get("title");
            if (title.equals("usulan")){
                String idUserPengusul = data.get("id_user");
                String idEvent = data.get("id_event");
                String tokenUserPengusul = data.get("token_user");
                Intent intent = new Intent(this, UsulanReceiver.class);
                intent.putExtra("title", title);
                intent.putExtra("id_user_pengusul", idUserPengusul);
                intent.putExtra("token_user_pengusul", tokenUserPengusul);
                intent.putExtra("id_event", idEvent);
                intent.setAction(Utils.NOTIF_EVENT_USULAN);
                sendBroadcast(intent);
            }
        }
    }
}
