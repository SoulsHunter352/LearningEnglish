package com.example.learningenglish;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private ToggleButton mode;
    String CHANNEL_ID = "LE8712";
    int NOTIFICATION_ID = 5123;
    SharedPreferences prefs;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                startNotifications();
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

        prefs = getSharedPreferences("LearningEnglish", MODE_PRIVATE);
        hoursPicker = findViewById(R.id.hours_picker);
        minutesPicker = findViewById(R.id.minutes_picker);
        secondsPicker = findViewById(R.id.seconds_picker);

        mode = findViewById(R.id.mode);
        mode.setChecked(prefs.getBoolean("mode", false));

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        //DictionaryManager.loadDictionary(this);

        configureNumberPickers();
        createNotificationChannel();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
        else{
            AlarmScheduler.startNotifications(this, getCurrentInterval(), mode.isChecked());
        }
    }

    protected void saveSettings(){
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt("hours", hoursPicker.getValue());
        prefEditor.putInt("minutes", minutesPicker.getValue());
        prefEditor.putInt("seconds", secondsPicker.getValue());
        prefEditor.putBoolean("mode", mode.isChecked());
        prefEditor.apply();
        AlarmScheduler.stopAlarms(this);
        AlarmScheduler.startNotifications(this, getCurrentInterval(), mode.isChecked());
    }

    protected void startNotifications(){
        AlarmScheduler.startNotifications(this, getCurrentInterval(), mode.isChecked());
    }

    protected int getCurrentInterval(){
        int hours = hoursPicker.getValue();
        int minutes = minutesPicker.getValue();
        int seconds = secondsPicker.getValue();
        return seconds * 1000 + minutes * 60 * 1000 + hours * 60 * 60 * 1000;
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
        int currentHours = prefs.getInt("hours", 0);
        hoursPicker.setValue(currentHours);
    }

    protected void configureMinutesPicker(){
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        String[] minutes = new String[60];
        for(int i = 0; i < 60; i++){
            minutes[i] = i + "мин";
        }
        minutesPicker.setDisplayedValues(minutes);
        int currentMinutes = prefs.getInt("minutes", 0);
        minutesPicker.setValue(currentMinutes);
    }
    protected void configureSecondsPicker(){
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        String[] seconds = new String[60];
        for(int i = 0; i < 60; i++){
            seconds[i] = i + "с";
        }
        secondsPicker.setDisplayedValues(seconds);
        int currentSeconds = prefs.getInt("seconds", 0);
        secondsPicker.setValue(currentSeconds);
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
}