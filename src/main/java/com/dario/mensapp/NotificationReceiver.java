package com.dario.mensapp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Dario on 19/03/2018.
 */

public class NotificationReceiver extends BroadcastReceiver
{
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        Bundle extras = intent.getExtras();

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty())
        {

            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {

                // emette una notifica sul dispositivo
                sendNotification(context,"E' arrivata la tua prima notifica attraverso GCM!");

            }
        }
    }

    private void sendNotification(Context ctx,String msg)
    {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // scelta suoneria per notifica
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_home)
                        .setContentTitle("Push Notifications: primo esperimento")
                        .setContentText(msg)
                        .setSound(sound);

        // effettua la notifica
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}