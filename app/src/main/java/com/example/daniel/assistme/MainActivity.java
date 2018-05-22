package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    private static User userData;
    String URL_s = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        checkSession();
    }

    private void checkSession(){
        String username = sharedPreferences.getString("Username", null);
        String password = sharedPreferences.getString("Password", null);
        //URL_s = "http://172.17.1.243:8080/PES_BackEnd/peticiones_php/check_login.php";
        URL_s = "http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/check_login.php";
        if (username != null && password != null){
            try {
                login(username, password);
            } catch (UnsupportedEncodingException e) {
                Toast t = Toast.makeText(getApplicationContext(), "Error desconocido", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    public void login(String username, String pass) throws UnsupportedEncodingException {
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(username, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                + URLEncoder.encode(pass, "UTF-8");

        //userData = new User(username, pass);
        //userData = null;

        //Poner la peticion http aqui
        MainActivity.AsyncLogin asyncLogin = new MainActivity.AsyncLogin();
        asyncLogin.execute(data);

    }

    private class AsyncLogin extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                //URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/login.php");
                URL url = new URL(URL_s);

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
                Log.e("result", jsonString);
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
            Log.e("WTF","");
            try {
                Toast t = null;
                if (result == null) {
                    t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
                    t.show();
                } else if (result.contains("true")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String username = "EMPTY";
                        String name = "EMPTY";
                        String surname = "EMPTY";
                        String email = "EMPTY";
                        String country = "EMPTY";
                        String usertype = "EMPTY";
                        String password = "EMPTY";
                        String url_picture = "EMPTY";
                        if (jsonObject.has("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if(data.has("username")){
                                username = data.getString("username");
                            }
                            if(data.has("name")){
                                name = data.getString("name");
                            }
                            if(data.has("surname")){
                                surname = data.getString("surname");
                            }
                            if(data.has("email")){
                                email = data.getString("email");
                            }
                            if(data.has("country")){
                                country = data.getString("country");
                            }
                            if(data.has("usertype")){
                                usertype = data.getString("usertype");
                            }
                            if(data.has("password")){
                                password = data.getString("password");
                            }
                            if(data.has("url_picture")){
                                url_picture = data.getString("url_picture");
                            }
                        }

                        if(username != "EMPTY"){
                            userData = new User(username, email, name, surname, country, usertype, password, url_picture);
                            setSharedPreferences(userData);
                        }
                        Log.e("Checking Pass",userData.getPassword());
                        ChangeScene();
                    }
                    catch(Exception e){
                        Log.d("error", e.toString());
                    }
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

    public void Register(View view){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void Login(View view){

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void Guest(View view){

        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", "");
        startActivity(intent);
    }

    public static void Logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void setSharedPreferences(User user){
        userData = user;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Username", userData.getUsername());
        editor.putString("Password", userData.getPassword());
        editor.putString("Mail", userData.getMail());
        editor.apply();
    }
    public static User getUser(){
        return userData;
    }
}

