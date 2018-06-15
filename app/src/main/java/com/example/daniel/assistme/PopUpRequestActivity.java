package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class PopUpRequestActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_request);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * 0.8f);
        int height = (int)(dm.heightPixels * 0.3f);

        getWindow().setLayout(width, height);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chats_pendientes");
        databaseReference2 = database.getReference("chat").child(MainActivity.sharedPreferences.getString("Username", null));

        text = findViewById(R.id.textView5);
    }

    public void cancelRequest(View view) {
        finish();
    }

    public void publishRequest(View view) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String date;

        if (minute >= 10) date = Integer.toString(hourOfDay) + ':' + Integer.toString(minute);
        else date = Integer.toString(hourOfDay) + ":0" + Integer.toString(minute);

        Message m = new Message(text.getText().toString(), MainActivity.sharedPreferences.getString("Username", null), MainActivity.getUser().getUrl_picture(), date, "1");

        databaseReference.push().setValue(m);
        databaseReference2.push().setValue(m);

        finish();
    }
}
