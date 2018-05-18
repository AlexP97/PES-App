package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;
import org.json.JSONObject;
import android.util.Log;
import android.widget.Button;
import android.view.View;


public class ViewGuideActivity extends AppCompatActivity {

    android.content.Context context;
    String guideInfo, guideTitle, guidePoints;
    public static String guideContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        Bundle extras = getIntent().getExtras();
        guideTitle = extras.getString("titleGuide");
        guidePoints = extras.getString("pointsGuide");


        TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(guideTitle);

        WebView contentHtmlView = (WebView) findViewById(R.id.content_html);
        contentHtmlView.loadData(guideContent, "text/html; charset=utf-8", "utf-8");

        if (guidePoints != "-1") {
            View b = findViewById(R.id.mapbutton);
            b.setVisibility(View.VISIBLE);
        }else {
            View b = findViewById(R.id.mapbutton);
            b.setVisibility(View.GONE);
        }
    }

    public void MapButton(android.view.View view) {
        if (guidePoints != "EMPTY") {
            android.content.Intent intent = new android.content.Intent(context, MapsActivity.class);
            intent.putExtra("points", guidePoints);
            startActivity(intent);
        }
    }
}
