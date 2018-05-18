package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {

    User userData;
    Context context;
    String errorT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void RegisterButton(View view) throws IOException, JSONException {

        EditText usernameEdit = (EditText) findViewById(R.id.username);
        String username = usernameEdit.getText().toString();

        EditText nameEdit = (EditText) findViewById(R.id.name);
        String name = nameEdit.getText().toString();

        EditText surnameEdit = (EditText) findViewById(R.id.surname);
        String surname = surnameEdit.getText().toString();

        EditText emailEdit = (EditText) findViewById(R.id.email);
        String email = emailEdit.getText().toString();

        EditText passEdit = (EditText) findViewById(R.id.password);
        String pass = passEdit.getText().toString();

        EditText passRepEdit = (EditText) findViewById(R.id.repeatPassword);
        String passRep = passRepEdit.getText().toString();

        EditText countryEdit = (EditText) findViewById(R.id.country);
        String country = countryEdit.getText().toString();

        if (username.equals("") || name.equals("") || surname.equals("") || email.equals("") ||
                pass.equals("") || passRep.equals("") || country.equals("")){

            Toast t = Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT);
            t.show();
        }
        else {

            if (!pass.equals(passRep)) {

                Toast t = Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT);
                t.show();
            }
            else {

                if (email.contains("@")) {

                    String data = URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(username, "UTF-8");

                    data += "&" + URLEncoder.encode("user_password", "UTF-8") + "="
                            + URLEncoder.encode(pass, "UTF-8");

                    data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                            + URLEncoder.encode(email, "UTF-8");

                    data += "&" + URLEncoder.encode("name", "UTF-8")
                            + "=" + URLEncoder.encode(name, "UTF-8");

                    data += "&" + URLEncoder.encode("surname", "UTF-8")
                            + "=" + URLEncoder.encode(surname, "UTF-8");

                    data += "&" + URLEncoder.encode("country", "UTF-8") + "="
                            + URLEncoder.encode(country, "UTF-8");

                    userData = new User(username, pass, email, name, surname, country, pass);

                    //Poner la peticion http aqui
                    AsyncRegister asyncRegister = new AsyncRegister();
                    asyncRegister.execute(data);

                    if (!errorT.isEmpty()) {
                        Toast t = null;

                        if (errorT.contains(username)) {
                            t = Toast.makeText(getApplicationContext(), "Username already taken.", Toast.LENGTH_SHORT);
                            t.show();
                        }
                        if (errorT.contains(email)) {
                            t = Toast.makeText(getApplicationContext(), "Email already used.", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
    }

    private class AsyncRegister extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/register.php");

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
        protected void onPostExecute(String response) {
            try {
                String correct = "EMPTY";
                String result = "EMPTY";
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("correct")) correct = jsonObject.getString("correct");
                if (jsonObject.has("result")) result = jsonObject.getString("result");

                Toast t = null;
                if (result == null) {
                    t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
                    t.show();
                }
                else if (correct.contains("true")){
                    MainActivity.setSharedPreferences(userData);
                    ChangeScene();
                }
                else if (correct.contains("false")) {
                    errorT = result;
                }
                else {
                    t = Toast.makeText(getApplicationContext(), "Nothing happened", Toast.LENGTH_SHORT);
                    t.show();
                }
            } catch (Exception e) {}

            super.onPostExecute(response);
        }
    }

    void ChangeScene() {
        Intent intent = new Intent(context, MenuActivity.class);
        startActivity(intent);
    }
}

