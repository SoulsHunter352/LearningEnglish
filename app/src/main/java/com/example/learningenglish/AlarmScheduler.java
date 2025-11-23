package com.example.learningenglish;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmScheduler {
    public static void startNotifications(Context context, long intervalMillis, boolean mode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        String word = DictionaryManager.getRandomWord();
        String translation = DictionaryManager.getTranslation(word);

        Intent intent = new Intent(context, TimeNotification.class);
        intent.putExtra("INTERVAL", intervalMillis);
        intent.putExtra("MODE", mode);
        intent.putExtra("TITLE", "Новое слово");
        intent.putExtra("TEXT", word + " - " + translation);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // long intervalMillis = 10000;
        long nextTrigger = System.currentTimeMillis() + intervalMillis;

        // Устанавливаем повторяющийся алёрм
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
        //        firstTrigger, intervalMillis, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.w("AlarmScheduler", "Нет разрешения, пропускаем перезапуск");
                return;
            }
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                nextTrigger, pendingIntent);
    }

    public static void stopAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
        Log.d("Alarm", "Алёрмы остановлены");
    }
}
