package com.dario.mensapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.internal.constants.NotificationChannel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dario on 24/03/2018.
 */

public class MyService extends IntentService {
    public MyService() {
        super("MyServiceName");
    }
    @Override
    protected void onHandleIntent(Intent intent) {



try {

    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ITALY);
   Date date = new Date();
    String timestamp=format.format(date);
    for (int i = 0; i < UserSession.getListaMense().size(); i++) {
        Mensa mensa = UserSession.getListaMense().get(i);
        String pranzo = mensa.getOrarioAperturaPranzo();
        String cena = mensa.getOrarioAperturaCena();
        String indirizzo = mensa.getIndirizzo();


        java.util.Date d1 = (java.util.Date) format.parse(timestamp);
        java.util.Date d2 = (java.util.Date) format.parse(pranzo);
        Date newDate = new Date(d1.getTime() + TimeUnit.HOURS.toMillis(2)); // Adds 2 hours

        if (newDate.before(d2)
                ) {


            this.sendNotification(this, "Tra poco scade il tempo per prenotarti a pranzo", indirizzo);

        }
        if (!cena.equals("")) {
            java.util.Date d = (java.util.Date) format.parse(timestamp);
            java.util.Date dCena = (java.util.Date) format.parse(cena);
            Date nuovaData = new Date(d.getTime() + TimeUnit.HOURS.toMillis(2)); // Adds 2 hours

            if (nuovaData.before(dCena)) {
                this.sendNotification(this, "Tra poco scade il tempo per prenotarti a cena", indirizzo);

            }
        }

    }
}catch (ParseException ex){
    ex.printStackTrace();
}

    }

    private void sendNotification(Context context, String paramOutput, String mensa) {
        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Mensa di "+mensa)
                        .setContentText(paramOutput);


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;


                mNotificationManager.notify(m, mBuilder.build());
    }
}