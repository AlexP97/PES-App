package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

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
            Toast t = Toast.makeText(getApplicationContext(), "Language set to English", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (rg.getCheckedRadioButtonId() == R.id.radioButton2) {
            Toast t = Toast.makeText(getApplicationContext(), "Idioma canviat al Català", Toast.LENGTH_SHORT);
            t.show();
        }
        else if (rg.getCheckedRadioButtonId() == R.id.radioButton) {
            Toast t = Toast.makeText(getApplicationContext(), "Idioma cambiado al Español", Toast.LENGTH_SHORT);
            t.show();
        }

        finish();
    }
}
