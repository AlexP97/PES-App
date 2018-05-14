package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MenuActivity extends AppCompatActivity {

    Context context;
    String guides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.account_settings:
                if (checkSession()) {
                    Intent intent = new Intent(this, EditActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, PopUpLoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.app_settings:
                Intent intent2 = new Intent(this, SettingsActivity.class);
                startActivity(intent2);
                break;
            case R.id.log_out:
                MainActivity.Logout();
                Intent intent3 = new Intent(this, MainActivity.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

    public void ChatButton(View view) {
        if (checkSession()) {
            Intent intent = new Intent(context, ChatActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PopUpLoginActivity.class);
            startActivity(intent);
        }
    }

    public void ForumButton(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void GuidesButton(View view) throws UnsupportedEncodingException {
        getGuides("");
    }

    public void NewsButton(View view) {
        Intent intent = new Intent(context, NewsActivity.class);
        startActivity(intent);
    }

    private boolean checkSession(){
        String username = MainActivity.sharedPreferences.getString("Username", null);
        String password = MainActivity.sharedPreferences.getString("Password", null);
        return username != null && password != null;
    }

    private void getGuides(final String search) {
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

                    ChangeScene(jsonString);
                }
                catch(Exception e){
                    Log.d("error", e.toString());
                }

            }
        });
    }

    private void getGuides2() throws UnsupportedEncodingException {

        String data = URLEncoder.encode("contains", "UTF-8")
                + "=" + URLEncoder.encode("t", "UTF-8");

        MenuActivity.AsyncGuides asyncGuides = new MenuActivity.AsyncGuides();
        asyncGuides.execute(data);
    }

    private void ChangeScene(String guides) {
        Intent i = new Intent(context, GuidesActivity.class);
        i.putExtra("guides", guides);
        startActivity(i);
    }

    private class AsyncGuides extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/search_guide.php");

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
                ChangeScene(result);
            }
            else if (result.contains("false")) {
                t = Toast.makeText(context, "Guides not found", Toast.LENGTH_SHORT);
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
