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
    String guideInfo, guideTitle, guideContent, guidePoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        Bundle extras = getIntent().getExtras();
        guideInfo = extras.getString("infoGuide");

        getInfoGuide();


        TextView contentView = (TextView) findViewById(R.id.content);
        contentView.setText(guideTitle);

        WebView contentHtmlView = (WebView) findViewById(R.id.content_html);
        contentHtmlView.loadData(guideContent, "text/html; charset=utf-8", "utf-8");

        if (!guidePoints.equals("null")) {
            Button checkMap = (Button)findViewById(R.id.mapbutton);
            checkMap.setVisibility(View.VISIBLE);
        }
        else {
            Button checkMap = (Button)findViewById(R.id.mapbutton);
            checkMap.setVisibility(View.GONE);
        }
    }

    private void getInfoGuide()  {
        String[] aux = guideInfo.split(",");
        String[] aux2 = aux[0].split(":");
        guideTitle = " " + aux2[1].substring(1, aux2[1].length()-1);
        aux2 = aux[1].split(":");
        guideContent = aux2[1].substring(1, aux2[1].length()-1);

        try {
            JSONObject jsonObject = new JSONObject(guideInfo);
            guideTitle = "EMPTY";
            guideContent = "EMPTY";
            guidePoints = "EMPTY";
            if (jsonObject.has("title")) guideTitle = jsonObject.getString("title");
            if (jsonObject.has("data")) guideContent = jsonObject.getString("data");
            if (jsonObject.has("points")) guidePoints = jsonObject.getString("points");

        }
        catch (Exception e) {}
    }

    public void MapButton(View view) {
        if (guidePoints != "EMPTY") {
            android.content.Intent intent = new android.content.Intent(context, MapsActivity.class);
            intent.putExtra("points", guidePoints);
            startActivity(intent);
        }
    }
}
