package com.example.daniel.assistme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void LoginButton (View view) throws IOException, JSONException {

        EditText usernameEdit = (EditText) findViewById(R.id.username);
        String username = usernameEdit.getText().toString();

        EditText passEdit = (EditText) findViewById(R.id.password);
        String pass = passEdit.getText().toString();

        if (username.equals("") || pass.equals("")){

            Toast t = Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT);
            t.show();
        }
        else {

            String data = URLEncoder.encode("username", "UTF-8")
                    + "=" + URLEncoder.encode(username, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                    + URLEncoder.encode(pass, "UTF-8");

            //Poner la peticion http aqui
            JSONObject json = doLogin(data);
            Toast t = null;
            if (json == null)
                t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
            else if (json.getBoolean("correct"))
                t = Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT);
            else if (!json.getBoolean("correct"))
                t = Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT);
            else
                t = Toast.makeText(getApplicationContext(), "Nothing happened", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public static JSONObject doLogin(String data) throws IOException, JSONException {

        BufferedReader reader=null;

        try {
            URL url = new URL("http://ec2-35-180-58-81.eu-west-3.compute.amazonaws.com/PES_AssistMe_BackEnd/peticiones_php/login.php");

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
            System.out.print("JSON: " + jsonString);
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


