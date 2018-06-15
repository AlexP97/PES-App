package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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

        final EditText edittext = (EditText) findViewById(R.id.search);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    EditText searchEdit = (EditText) findViewById(R.id.search);
                    String search = searchEdit.getText().toString();

                    searchGuides(search);
                    return true;
                }
                return false;
            }
        });

        getGuides(extras.getString("guides"));
    }

    private void getGuides(String s) {
        try {
            JSONObject js = new JSONObject(s);
            String dataString = "EMPTY";
            if (js.has("data")) dataString = js.getString("data");

            JSONArray guides = new JSONArray(dataString);
            for (int i = 0; i < guides.length(); i++) {
                JSONObject guide = guides.getJSONObject(i);  //coger una guia
                String id = guide.getString("id");
                String title = guide.getString("title");
                guidesList.add(new Guide(id, title));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
            t.show();
        }

        Collections.sort(guidesList, new GuideTitleComparator());
    }

    public void SearchGuidesButton (View view) {
        View b = findViewById(R.id.search);
        if (b.getVisibility() == View.GONE) b.setVisibility(View.VISIBLE);
        else b.setVisibility(View.GONE);
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
                    if (jsonObject.has("points")) guide[2] = jsonObject.getString("points");

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
