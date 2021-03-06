package com.example.daniel.assistme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


public class ViewGuideActivity extends AppCompatActivity {

    android.content.Context context;
    String guideTitle, guidePoints;
    public static String guideContent;
    private Spinner spinner1;
    private static String leng_to;
    private static String leng_from = "es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_guide);

        Bundle extras = getIntent().getExtras();
        leng_to = "en";
        guideTitle = extras.getString("titleGuide");
        guidePoints = extras.getString("pointsGuide");


        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(guideTitle);

        WebView contentHtmlView = (WebView) findViewById(R.id.content_html);
        contentHtmlView.loadData(guideContent, "text/html; charset=utf-8", "utf-8");

        /*
        if (guidePoints != "[]") {
            View b = findViewById(R.id.mapbutton);
            b.setVisibility(View.VISIBLE);
        }else {
            View b = findViewById(R.id.mapbutton);
            b.setVisibility(View.GONE);
        }
*/
        // Listener a la lista de Idiomas
        addListenerOnSpinnerItemSelection();
    }

    public void MapButton(android.view.View view) {
        try {
            JSONArray points = new JSONArray(guidePoints);
            if (points.length() > 0) {
                android.content.Intent intent = new android.content.Intent(context, MapsActivity.class);
                intent.putExtra("points", guidePoints);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "There aren't points of interest in this guide.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void AppearTranslateButton(android.view.View view) {
        View b = findViewById(R.id.translation);
        if (b.getVisibility() == View.GONE) b.setVisibility(View.VISIBLE);
        else b.setVisibility(View.GONE);
    }

    public void AppearMapButton(android.view.View view) {
        try {
            JSONArray points = new JSONArray(guidePoints);
            if (points.length() > 0) {
                View b = findViewById(R.id.mapa_layout);
                if (b.getVisibility() == View.GONE) b.setVisibility(View.VISIBLE);
                else b.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), "There aren't points of interest in this guide.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void translate(android.view.View view) throws UnsupportedEncodingException {


        String html = guideContent;
        String txt = org.jsoup.Jsoup.parse(html).text();


        //Poner la peticion http aqui
        ViewGuideActivity.AsyncTrans asyncTrans = new ViewGuideActivity.AsyncTrans();
        asyncTrans.execute(txt);
    }

    private class AsyncTrans extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                String mKey = "trnsl.1.1.20180601T105748Z.dc0633591153717a.53d870f1d138ff601a4850c3894c477aa44c814f";
                String sourceText = data[0];

                //URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?lang=en-ru&key=trnsl.1.1.20180601T105748Z.dc0633591153717a.53d870f1d138ff601a4850c3894c477aa44c814f");
                //String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + mKey
                        //+ "&text=" + sourceText + "&lang=" + leng_from + "-" + leng_to;
                String encodedText = java.net.URLEncoder.encode(sourceText,"UTF-8");
                String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + mKey +
                        "&lang=" + leng_from + "-" + leng_to + "&text=" + encodedText;

                URL url = new URL(yandexUrl);
                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);


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

                return jsonString;
            }
            catch(Exception ex) {
                ex.printStackTrace();
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

                JSONObject jsonObject = new JSONObject(result);

                WebView contentHtmlView = (WebView) findViewById(R.id.content_html);
                String txt = jsonObject.getString("text");

                StringBuilder sb = new StringBuilder(txt);
                sb.deleteCharAt(0);
                sb.deleteCharAt(0);
                sb.setLength(sb.length() - 2);
                txt = sb.toString();

                contentHtmlView.loadData(txt, "text/html; charset=utf-8", "utf-8");

                super.onPostExecute(result);
            }
            catch(Exception e){}
        }

    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public static String getLen_to(){
        return leng_to;
    }

    public static void setLen_to(String value){
        leng_to = value;
    }

    public static String getLen_from(){
        return leng_from;
    }

    public static void setLen_from(String value){
        leng_from = value;
    }

    public void reset_traduction(android.view.View view){
        WebView contentHtmlView = (WebView) findViewById(R.id.content_html);
        contentHtmlView.loadData(guideContent, "text/html; charset=utf-8", "utf-8");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guia, menu);
        return true;
    }

    public void download(android.view.View view) {
        File localFolder = new File(Environment.getExternalStorageDirectory() + "/AssistMeGuides/");
        if (!localFolder.exists()){
            localFolder.mkdirs();
        }
        File pdfFile = new File(localFolder, guideTitle + ".pdf");
        try {
            pdfFile.createNewFile();
            OutputStream opStream = new FileOutputStream(pdfFile);
            pdfFile.setWritable(true);
            byte[] b = guideContent.getBytes();
            opStream.write(b);
            opStream.flush();
            opStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
