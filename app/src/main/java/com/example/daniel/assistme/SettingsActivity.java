package com.example.daniel.assistme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void LanguageOptions(View view){

        Intent intent = new Intent(this, PopUpLanguageActivity.class);
        startActivity(intent);
    }

    public void ToggleNotifications(View view){

        SwitchCompat switchCompat = findViewById(R.id.switch1);
        switchCompat.setChecked(!switchCompat.isChecked());
    }
}
