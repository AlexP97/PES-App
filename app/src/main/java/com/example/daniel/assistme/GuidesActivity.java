package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
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
import java.util.List;

public class GuidesActivity extends AppCompatActivity {

    Context context;
    String guideTitle, guideContent, result;
    Guide g;
    List<Guide> guides = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides);

        Bundle extras = getIntent().getExtras();
        getGuides(extras.getString("guides"));
        printGuides();

        TextView guidesView = (TextView) findViewById(R.id.textView5);
        guidesView.setText(result);
    }

    private void getGuides(String s) {
        String[] aux = s.split(":");
        aux = aux[1].split(",");
        for (int i = 0; i < aux.length-1; i+=2) {
            String id = aux[i].substring(aux[i].indexOf("[")+1);
            String title = aux[i+1].substring(1, aux[i+1].length()-1);
            g = new Guide(id, title);
            guides.add(g);
        }
    }

    private void printGuides() {
        //result = "Guia 1" + "\n id: " + g.getId() + " title: " + g.getTitle() + "\n";
        result = "";
        for (int i = 0; i < guides.size(); i++) {
            result += "Guia " + i + "\n id: " + guides.get(i).getId() + " title: " + guides.get(i).getTitle() + "\n";
        }
    }

    public void VerButton (View view) throws IOException {
        viewGuide();
    }

    public void verGuide() throws UnsupportedEncodingException {
        String data = URLEncoder.encode("id_guide", "UTF-8")
                + "=" + URLEncoder.encode("28", "UTF-8");

        AsyncGuide asyncGuide = new AsyncGuide();
        asyncGuide.execute(data);
    }

    private void viewGuide() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // Create URL
                    URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/get_data_guide.php");

                    // Create connection
                    HttpURLConnection myConnection =
                            (HttpURLConnection) url.openConnection();

                    myConnection.setRequestProperty("id_guide", "14");

                    InputStream responseBody = myConnection.getInputStream();

                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, "UTF-8");

                    JsonReader jsonReader = new JsonReader(responseBodyReader);

                    jsonReader.beginObject(); // Start processing the JSON object
                    while (jsonReader.hasNext()) { // Loop through all keys
                        String key = jsonReader.nextName(); // Fetch the next key
                        if (key.equals("title")) {
                            guideTitle = jsonReader.nextString();
                            // Do something with the value
                            Log.d(key, guideTitle);
                        }
                        else if (key.equals("content")) {
                            guideContent = jsonReader.nextString();
                            // Do something with the value
                            Log.d(key, guideTitle);
                        } else {
                            jsonReader.skipValue(); // Skip values of other keys
                        }
                    }

                    jsonReader.close();

                    myConnection.disconnect();

                    ChangeScene();
                }
                catch(Exception e){
                    Log.d("error", e.toString());
                }

            }
        });
    }

    void ChangeScene() {
        Intent intent = new Intent(context, ViewGuideActivity.class);
        intent.putExtra("guideTitle", guideTitle);
        intent.putExtra("guideConent", guideContent);
        startActivity(intent);
    }

    private class AsyncGuide extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/get_data_guide.php");

                // Send POST data request

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                String d = "";
                for (int i = 0; i < data.length; ++i) d += data[i];
                wr.write( d );
                wr.flush();
                Log.d("data", d);

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
                Log.d("result", jsonString);
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

            Toast t = null;
            if (result == null) {
                t = Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT);
                t.show();
            }
            else if (result.contains("true")) {
                ChangeScene();
            }
            else if (result.contains("false")) {
                t = Toast.makeText(context, "Guide not found", Toast.LENGTH_SHORT);
                t.show();
            }
            else {
                t = Toast.makeText(context, "Nothing happened", Toast.LENGTH_SHORT);
                t.show();
            }

            super.onPostExecute(result);
        }



    }
}
