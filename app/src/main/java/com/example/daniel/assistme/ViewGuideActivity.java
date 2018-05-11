package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewGuideActivity extends AppCompatActivity {

    String guideTitle, guideContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        Bundle extras = getIntent().getExtras();
        guideTitle = extras.getString("guideTitle");
        guideContent = extras.getString("guideContent");

        //String[] infoGuide = getInfoGuide();

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(guideTitle);

        TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(guideContent);
    }
/*
    private String[] getInfoGuide() {
        String[] aux = guideData.split(",");
        aux = aux[0].split(":");
        String[] info = new String[2];
        info[0] = aux[1];
        aux = aux[1].split(":");
        info[1] = aux[1];

        return info;
    }
*/
}
