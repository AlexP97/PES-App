package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuidesActivity extends AppCompatActivity {

    Context context;
    Guide g;
    ArrayList<Guide> guidesList = new ArrayList<>();
    ListView recyclerView;
    GuideAdapter guideAdapter;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);

        Bundle extras = getIntent().getExtras();

        recyclerView = (ListView) findViewById(R.id.listGuides);

        guideAdapter = new GuideAdapter(this, guidesList);

        recyclerView.setAdapter(guideAdapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                viewGuide(guidesList.get(pos).getId());
            }
        });

        getGuides(extras.getString("guides"));
    }

    private void getGuides(String s) {
        try {
            JSONObject js = new JSONObject(s);
            data = "EMTPY";
            if (js.has("data")) data = js.getString("data");
        }
        catch (Exception e) {}

        data = data.substring(1, data.length()-1);

        String aux[] = data.split(",");
        for (int i = 0; i < aux.length; i+=2) {
            String id = aux[i];
            String title = aux[i+1].substring(1, aux[i+1].length()-1);
            guidesList.add(new Guide(id, title));
        }

        Collections.sort(guidesList, new GuideTitleComparator());
    }

    public void SearchGuidesButton (View view) {

        EditText searchEdit = (EditText) findViewById(R.id.search);
        String search = searchEdit.getText().toString();

        searchGuides(search);
    }

    private void searchGuides (final String search) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // Create URL
                    String s_url = "http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/search_guide.php?contains="+search;
                    URL url = new URL(s_url);

                    // Create connection
                    HttpURLConnection myConnection =
                            (HttpURLConnection) url.openConnection();

                    InputStream responseBody = myConnection.getInputStream();

                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    BufferedReader reader=null;
                    reader = new BufferedReader(responseBodyReader);
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        sb.append(line + "\n");
                    }

                    String jsonString[] = new String[1];
                    jsonString[0] = sb.toString();
                    Log.d("result", jsonString[0]);

                    myConnection.disconnect();

                    ChangeScene("", jsonString);
                }
                catch(Exception e){
                    Log.d("error", e.toString());
                }

            }
        });
    }

    private void viewGuide(final String id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    String s_url = "http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/get_data_guide.php?id_guide="+id;
                    // Create URL
                    URL url = new URL(s_url);

                    // Create connection
                    HttpURLConnection myConnection =
                            (HttpURLConnection) url.openConnection();

                    InputStream responseBody = myConnection.getInputStream();

                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    BufferedReader reader=null;
                    reader = new BufferedReader(responseBodyReader);
                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        // Append server response in string
                        sb.append(line + "\n");
                    }

                    String jsonString = sb.toString();
                    Log.d("result", jsonString);

                    myConnection.disconnect();

                    JSONObject jsonObject = new JSONObject(jsonString);
                    String[] guide = new String[3];
                    if (jsonObject.has("title")) guide[0] = jsonObject.getString("title");
                    if (jsonObject.has("data")) guide[1] = jsonObject.getString("data");
                    if (jsonObject.has("points") || !jsonObject.isNull("points")) guide[2] = jsonObject.getString("points");
                    else guide[2] = "-1";

                    ChangeScene("viewGuide", guide);
                }
                catch(Exception e){
                    Log.d("error", e.toString());
                }

            }
        });
    }

    void ChangeScene(String act, String[] response) {
        if (act.equals("viewGuide")) {
            Intent intent = new Intent(context, ViewGuideActivity.class);
            intent.putExtra("titleGuide", response[0]);
            ViewGuideActivity.guideContent = response[1];
            intent.putExtra("pointsGuide", response[2]);
            startActivity(intent);
        } else {Intent intent = new Intent(this, GuidesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("guides", response[0]);
            startActivityForResult(intent, 0);
            overridePendingTransition(0,0); //0 for no animation
            //startActivity(intent);
        }
    }

    public class GuideTitleComparator implements Comparator<Guide>
    {
        public int compare(Guide left, Guide right) {
            String s1 = left.getTitle();
            String s2 = right.getTitle();
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    }
}
