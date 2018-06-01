package com.example.daniel.assistme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;


public class PopUpLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_language);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * 0.8f);
        int height = (int)(dm.heightPixels * 0.4f);

        getWindow().setLayout(width, height);
    }

    public void ApplyChanges(View view){

        RadioGroup rg = findViewById(R.id.LanguageGroup);
        if (rg.getCheckedRadioButtonId() == R.id.radioButton3) {
            setLocale("eng");
            Toast t = Toast.makeText(getApplicationContext(), "Language set to English", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (rg.getCheckedRadioButtonId() == R.id.radioButton2) {
            setLocale("cat");
            Toast t = Toast.makeText(getApplicationContext(), "Idioma canviat al Català", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (rg.getCheckedRadioButtonId() == R.id.radioButton) {
            setLocale("spa");
            Toast t = Toast.makeText(getApplicationContext(), "Idioma cambiado al Español", Toast.LENGTH_SHORT);
            t.show();
        }
        super.recreate();
        finish();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
