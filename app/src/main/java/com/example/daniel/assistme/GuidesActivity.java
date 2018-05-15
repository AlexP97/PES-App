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
        String[] aux = s.split(":");
        aux = aux[1].split(",");
        for (int i = 0; i < aux.length-1; i+=2) {
            String id = aux[i].substring(aux[i].indexOf("[")+1);
            String title;
            if (i == aux.length-2) title = aux[i+1].substring(1, aux[i+1].length()-2);
            else title = aux[i+1].substring(1, aux[i+1].length()-1);
            g = new Guide(id, title);
            guidesList.add(g);
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

                    String jsonString = sb.toString();
                    Log.d("result", jsonString);

                    myConnection.disconnect();

                    ChangeScene("", jsonString);
                    /*guideAdapter.clear();
                    getGuides(jsonString);
                    guideAdapter = new GuideAdapter(GuidesActivity.this, guidesList);
                    recyclerView.setAdapter(guideAdapter);*/
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

                    int index = sb.indexOf("\\");
                    Log.d("index", index+"");

                    while (index > 0) {
                        sb = sb.deleteCharAt(index);
                        index = sb.indexOf("\\");
                    }

                    String jsonString = sb.toString();
                    Log.d("result", jsonString);

                    myConnection.disconnect();

                    ChangeScene("viewGuide", jsonString);
                }
                catch(Exception e){
                    Log.d("error", e.toString());
                }

            }
        });
    }

    void ChangeScene(String act, String response) {
        if (act.equals("viewGuide")) {
            Intent intent = new Intent(context, ViewGuideActivity.class);
            intent.putExtra("infoGuide", response);
            startActivity(intent);
        } else {Intent intent = new Intent(this, GuidesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("guides", response);
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
