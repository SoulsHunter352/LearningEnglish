package com.example.learningenglish;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    String CHANNEL_ID = "LE8712";
    int NOTIFICATION_ID = 5123;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                sendNotification();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hoursPicker = findViewById(R.id.hours_picker);
        minutesPicker = findViewById(R.id.minutes_picker);
        secondsPicker = findViewById(R.id.seconds_picker);
        configureNumberPickers();
        createNotificationChannel();
        sendNotification();
    }

    protected void configureNumberPickers(){
        configureHoursPicker();
        configureMinutesPicker();
        configureSecondsPicker();
    }

    protected void configureHoursPicker(){
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(24);
        String[] hours = new String[25];
        for(int i = 0; i < 25; i++){
            hours[i] = i + "ч";
        }
        hoursPicker.setDisplayedValues(hours);
    }

    protected void configureMinutesPicker(){
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        String[] minutes = new String[60];
        for(int i = 0; i < 60; i++){
            minutes[i] = i + "мин";
        }
        minutesPicker.setDisplayedValues(minutes);
    }
    protected void configureSecondsPicker(){
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        String[] seconds = new String[60];
        for(int i = 0; i < 60; i++){
            seconds[i] = i + "с";
        }
        secondsPicker.setDisplayedValues(seconds);
    }

    protected void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    protected void sendNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }
}