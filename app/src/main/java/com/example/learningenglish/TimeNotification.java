package com.example.learningenglish;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Objects;
import java.util.Random;

public class TimeNotification extends BroadcastReceiver {
    int NOTIFICATION_ID = 5123;
    String CHANNEL_ID = "LE8712";
    @Override
    public void onReceive(Context context, Intent intent) {
        String mode = intent.getStringExtra("MODE");

        if(Objects.equals(mode, "LEARNING") | Objects.equals(mode, "CHECK_ANSWER")){
            receiveSimpleNotification(context, intent);
        }
        else{
            receiveTestNotification(context, intent);
        }
    }

    public void receiveSimpleNotification(Context context, Intent intent){
        String title = intent.getStringExtra("TITLE");
        String text = intent.getStringExtra("TEXT");
        String mode = intent.getStringExtra("MODE");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
        AlarmScheduler.startNotifications(context, intent.getLongExtra("INTERVAL", 10000),
                Objects.equals(mode, "CHECK_ANSWER"));
    }

    public void receiveTestNotification(Context context, Intent intent){
        String title = intent.getStringExtra("TITLE");
        String text = intent.getStringExtra("TEXT");
        String rightTranslation = intent.getStringExtra("RIGHT_ANSWER");
        String wrongTranslation = intent.getStringExtra("WRONG_ANSWER");
        long interval = intent.getLongExtra("INTERVAL", 10000);

        Intent intentRight = new Intent(context, TimeNotification.class);
        intentRight.putExtra("TITLE", "Верно!");
        intentRight.putExtra("TEXT", text + " - " + rightTranslation);
        intentRight.putExtra("MODE", "CHECK_ANSWER");
        intentRight.putExtra("INTERVAL", interval);

        Intent intentWrong = new Intent(context, TimeNotification.class);
        intentWrong.putExtra("TITLE", "Неверно!");
        intentWrong.putExtra("TEXT", text + " - " + rightTranslation);
        intentWrong.putExtra("MODE", "CHECK_ANSWER");
        intentWrong.putExtra("INTERVAL", interval);

        PendingIntent pendingRightIntent = PendingIntent.getBroadcast(context, 0, intentRight,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingWrongIntent = PendingIntent.getBroadcast(context, 1, intentWrong,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Random random = new Random();
        int rightPosition = random.nextInt(2);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.addAction(
                0,
                rightPosition == 0 ? rightTranslation : wrongTranslation,
                rightPosition == 0 ? pendingRightIntent : pendingWrongIntent
        );
        builder.addAction(
                0,
                rightPosition == 1 ? rightTranslation : wrongTranslation,
                rightPosition == 1 ? pendingRightIntent : pendingWrongIntent
        );

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
