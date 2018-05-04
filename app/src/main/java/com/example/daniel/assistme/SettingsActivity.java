package com.example.daniel.assistme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        userData = (User)bundle.getSerializable("userData");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void LanguageOptions(View view){

        Intent intent = new Intent(this, PopUpLanguageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userData", userData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void ToggleNotifications(View view){

        SwitchCompat switchCompat = findViewById(R.id.switch1);
        switchCompat.setChecked(!switchCompat.isChecked());
    }
}
