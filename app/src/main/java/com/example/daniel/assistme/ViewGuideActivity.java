package com.example.daniel.assistme;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;
import org.json.JSONObject;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class ViewGuideActivity extends AppCompatActivity {

    android.content.Context context;
    String guideTitle, guidePoints;
    public static String guideContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        Bundle extras = getIntent().getExtras();
        guideTitle = extras.getString("titleGuide");
        guidePoints = extras.getString("pointsGuide");


        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(guideTitle);

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

    public void translate(android.view.View view) throws UnsupportedEncodingException {
        Toast t = Toast.makeText(getApplicationContext(), "Translate!!", Toast.LENGTH_SHORT);
        t.show();


        //Poner la peticion http aqui
        ViewGuideActivity.AsyncTrans asyncTrans = new ViewGuideActivity.AsyncTrans();
        asyncTrans.execute("");
    }

    private class AsyncTrans extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                String mKey = "trnsl.1.1.20180601T105748Z.dc0633591153717a.53d870f1d138ff601a4850c3894c477aa44c814f";
                String sourceText = "My name is Elias";
                String sourceLang = "en";
                String destinationLang = "es";
                //URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?lang=en-ru&key=trnsl.1.1.20180601T105748Z.dc0633591153717a.53d870f1d138ff601a4850c3894c477aa44c814f");
                String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + mKey
                        + "&text=" + sourceText + "&lang=" + sourceLang + "-" + destinationLang;
                URL url = new URL(yandexUrl);
                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                String d = "";
                for (int i = 0; i < data.length; ++i) d += data[i];
                wr.write( d );
                wr.flush();
                Log.e("data", d);

                // Get the server response

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                String jsonString = sb.toString();
                Log.e("result-translate", jsonString);
                return jsonString;
            }
            catch(Exception ex) {

            }
            finally
            {
                try
                {
                    reader.close();
                }

                catch(Exception ex) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Toast t = null;
                t = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                t.show();

                super.onPostExecute(result);
            }
            catch(Exception e){}
        }

    }
}
