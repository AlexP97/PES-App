package com.example.daniel.assistme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

/**
 * A login screen that offers login via email/password.
 * URL: https://dl.dropbox.com/s/nko3sset5rdj860/register.json?dl=0
 */
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                    //Poner la peticion http aqui
                    JSONObject json = doRegister(data);
                    Toast t = null;
                    if (json == null)
                        t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
                    else if (json.getBoolean("correct"))
                        t = Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_SHORT);
                    else if (!json.getBoolean("correct"))
                        t = Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT);
                    else
                        t = Toast.makeText(getApplicationContext(), "Nothing happened", Toast.LENGTH_SHORT);
                    t.show();
                }
                else {
                    Toast t = Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        }
    }

    public static JSONObject doRegister(String data) throws IOException, JSONException {

        BufferedReader reader=null;

        try {
            URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/register.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

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
            return new JSONObject(jsonString);
        }
        catch(Exception ex) {}
        finally
        {
            try
            {
                reader.close();
            }

            catch(Exception ex) {}
        }
        return null;
    }
}
