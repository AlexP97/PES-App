package com.example.daniel.assistme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Locale;

import android.content.Intent;


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
            setLocale("en","UK");
            Toast t = Toast.makeText(getApplicationContext(), "Language set to English", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (rg.getCheckedRadioButtonId() == R.id.radioButton2) {
            setLocale("ca","CA");
            Toast t = Toast.makeText(getApplicationContext(), "Idioma canviat al Català", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (rg.getCheckedRadioButtonId() == R.id.radioButton) {
            setLocale("es","ES");
            Toast t = Toast.makeText(getApplicationContext(), "Idioma cambiado al Español", Toast.LENGTH_SHORT);
            t.show();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void setLocale(String lang,String zone) {

        Locale locale = new Locale(lang,zone);
        Locale.setDefault(locale);

        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
            getBaseContext().getResources().getDisplayMetrics());
    }

}
