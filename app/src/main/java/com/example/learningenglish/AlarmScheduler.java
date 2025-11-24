package com.example.learningenglish;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Objects;

public class AlarmScheduler {
    public static void startNotifications(Context context, long intervalMillis, boolean mode){
        // Получаем доступ к службе оповещения
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long nextTrigger = System.currentTimeMillis() + intervalMillis;
        if(DictionaryManager.getDictSize() == 0){  // Если словарь не загружен, то загружаем
            DictionaryManager.loadDictionary(context);
        }
        String word = DictionaryManager.getRandomWord();
        String translation = DictionaryManager.getTranslation(word);

        // Создаем намерение для запуска BroadcastReceiver
        Intent intent = new Intent(context, TimeNotification.class);
        intent.putExtra("INTERVAL", intervalMillis);

        if(!mode){
            intent.putExtra("MODE", "LEARNING");
            intent.putExtra("TITLE", "Новое слово");
            intent.putExtra("TEXT", word + " - " + translation);
        }
        else{
            // Получаем второе слово, для создания неверного варианта ответа
            String word2;
            while(Objects.equals(word2 = DictionaryManager.getRandomWord(), word)){
            }

            String translation2 = DictionaryManager.getTranslation(word2);
            intent.putExtra("MODE", "TESTING");
            intent.putExtra("TITLE", "Переведите слово");
            intent.putExtra("TEXT", word);
            intent.putExtra("RIGHT_ANSWER", translation);
            intent.putExtra("WRONG_ANSWER", translation2);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
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
        Log.d("Alarm", "Оповещения остановлены");
    }
}
