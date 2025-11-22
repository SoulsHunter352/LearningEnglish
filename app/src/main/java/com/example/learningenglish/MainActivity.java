package com.example.learningenglish;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private NumberPicker hoursPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;

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
}