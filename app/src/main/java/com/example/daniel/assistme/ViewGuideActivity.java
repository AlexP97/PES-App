package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;


public class ViewGuideActivity extends AppCompatActivity {

    String guideInfo, guideTitle, guideContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        Bundle extras = getIntent().getExtras();
        guideInfo = extras.getString("infoGuide");

        //String[] infoGuide = getInfoGuide();

        //TextView titleView = (TextView) findViewById(R.id.title);
        //titleView.setText(guideTitle);

        getInfoGuide();

        TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(guideTitle);

        WebView contentHtmlView = (WebView) findViewById(R.id.content_html);
        contentHtmlView.loadData(guideContent, "text/html; charset=utf-8", "utf-8");
    }

    private void getInfoGuide() {
        String[] aux = guideInfo.split(",");
        String[] aux2 = aux[0].split(":");
        guideTitle = " " + aux2[1].substring(1, aux2[1].length()-1);
        aux2 = aux[1].split(":");
        guideContent = aux2[1].substring(1, aux2[1].length()-1);
    }
}
