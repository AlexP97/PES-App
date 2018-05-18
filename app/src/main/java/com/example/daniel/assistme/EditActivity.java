package com.example.daniel.assistme;

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
import java.util.ArrayList;


public class EditActivity extends AppCompatActivity {

    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Bundle bundle = getIntent().getExtras();
        userData = MainActivity.getUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        EditText nameEdit = (EditText) findViewById(R.id.name);
        nameEdit.setText(userData.getName());

        EditText surnameEdit = (EditText) findViewById(R.id.surname);
        surnameEdit.setText(userData.getSurname());

        EditText emailEdit = (EditText) findViewById(R.id.email);
        emailEdit.setText(userData.getMail());

        EditText countryEdit = (EditText) findViewById(R.id.country);
        countryEdit.setText(userData.getCountry());
    }

    public void change_password(View view) throws  IOException, JSONException{
        Toast t = Toast.makeText(getApplicationContext(), "Work in Progress", Toast.LENGTH_SHORT);
        t.show();
    }

    public void EditButton(View view) throws IOException, JSONException {

        EditText nameEdit = (EditText) findViewById(R.id.name);
        String name = nameEdit.getText().toString();

        EditText surnameEdit = (EditText) findViewById(R.id.surname);
        String surname = surnameEdit.getText().toString();

        EditText emailEdit = (EditText) findViewById(R.id.email);
        String email = emailEdit.getText().toString();

        //EditText passEdit = (EditText) findViewById(R.id.password);
        //String pass = passEdit.getText().toString();

        EditText passRepEdit = (EditText) findViewById(R.id.repeatPassword);
        String passRep = passRepEdit.getText().toString();

        EditText countryEdit = (EditText) findViewById(R.id.country);
        String country = countryEdit.getText().toString();

        if (name.equals("") || surname.equals("") || email.equals("") ||
                passRep.equals("") || country.equals("")){

            Toast t = Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT);
            t.show();
        }
        else {

            /*if (!pass.equals(passRep)) {

                Toast t = Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT);
                t.show();
            }
            else {*/

                if (email.contains("@")) {

                    String data = URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(userData.getUsername(), "UTF-8");

                   // data += "&" + URLEncoder.encode("user_password", "UTF-8") + "="
                            //+ URLEncoder.encode(pass, "UTF-8");

                    data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                            + URLEncoder.encode(email, "UTF-8");

                    data += "&" + URLEncoder.encode("name", "UTF-8")
                            + "=" + URLEncoder.encode(name, "UTF-8");

                    data += "&" + URLEncoder.encode("surname", "UTF-8")
                            + "=" + URLEncoder.encode(surname, "UTF-8");

                    data += "&" + URLEncoder.encode("country", "UTF-8") + "="
                            + URLEncoder.encode(country, "UTF-8");

                    //Poner la peticion http aqui
                    AsyncEdit asyncEdit = new AsyncEdit();
                    asyncEdit.execute(data);
                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT);
                    t.show();
                }
            //}
        }
    }

    private class AsyncEdit extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... data) {
            BufferedReader reader=null;

            try {
                URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/edit_profile.php");

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
                Log.d("correct", jsonString);
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
            if (result == null)
                t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
            else if (result.contains("true")) {
                t = Toast.makeText(getApplicationContext(), "Edition successful", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userData", userData);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else if (result.contains("false"))
                t = Toast.makeText(getApplicationContext(), "Edition failed", Toast.LENGTH_SHORT);
            else
                t = Toast.makeText(getApplicationContext(), "Nothing happened", Toast.LENGTH_SHORT);
            t.show();

            super.onPostExecute(result);
        }
    }
}

