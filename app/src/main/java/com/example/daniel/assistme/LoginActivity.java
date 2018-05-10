package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import javax.net.ssl.HttpsURLConnection;


public class LoginActivity extends AppCompatActivity {

    User userData;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        checkSession();
        setContentView(R.layout.activity_login);
    }

    private void checkSession(){
        String username = MainActivity.sharedPreferences.getString("Username", null);
        String password = MainActivity.sharedPreferences.getString("Password", null);
        if (username != null && password != null){
            try {
                login(username, password);
            } catch (UnsupportedEncodingException e) {
                Toast t = Toast.makeText(getApplicationContext(), "Error desconocido", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    public void LoginButton (View view) throws IOException {

        EditText usernameEdit = (EditText) findViewById(R.id.username);
        String username = usernameEdit.getText().toString();

        EditText passEdit = (EditText) findViewById(R.id.password);
        String pass = passEdit.getText().toString();

        if (username.equals("") || pass.equals("")){

            Toast t = Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT);
            t.show();
        }
        else {
            login(username, pass);
        }
    }

    public void login(String username, String pass) throws UnsupportedEncodingException {
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(username, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                + URLEncoder.encode(pass, "UTF-8");

        userData = new User(username, pass);

        //Poner la peticion http aqui
        AsyncLogin asyncLogin = new AsyncLogin();
        asyncLogin.execute(data);
    }

    private class AsyncLogin extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/login.php");

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

            try {
                Toast t = null;
                if (result == null) {
                    t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
                    t.show();
                } else if (result.contains("true")) {
                    MainActivity.setSharedPreferences(userData);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                // Create URL
                                URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/user.php");

                                // Create connection
                                HttpURLConnection myConnection =
                                        (HttpURLConnection) url.openConnection();

                                InputStream responseBody = myConnection.getInputStream();

                                InputStreamReader responseBodyReader =
                                        new InputStreamReader(responseBody, "UTF-8");

                                JsonReader jsonReader = new JsonReader(responseBodyReader);

                                jsonReader.beginObject(); // Start processing the JSON object
                                while (jsonReader.hasNext()) { // Loop through all keys
                                    String key = jsonReader.nextName(); // Fetch the next key

                                    String value = jsonReader.nextString();
                                    // Do something with the value
                                    Log.d(key, value);
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
                } else if (result.contains("false")) {
                    t = Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    t = Toast.makeText(getApplicationContext(), "Nothing happened", Toast.LENGTH_SHORT);
                    t.show();
                }

                super.onPostExecute(result);
            }
            catch(Exception e){}
        }

    }

    void ChangeScene() {
        Intent intent = new Intent(context, MenuActivity.class);
        startActivity(intent);
    }
}


