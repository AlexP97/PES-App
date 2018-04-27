package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    String username;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {

        username = getIntent().getStringExtra("EXTRA_SESSION_ID");
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
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", username);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void ChatButton(View view) {
        Intent intent = new Intent(context, EditActivity.class);
        startActivity(intent);
    }

    public void ForumButton(View view) {
        Intent intent = new Intent(context, EditActivity.class);
        startActivity(intent);
    }

    public void GuidesButton(View view) {
        Intent intent = new Intent(context, EditActivity.class);
        startActivity(intent);
    }

    public void NewsButton(View view) {
        Intent intent = new Intent(context, EditActivity.class);
        startActivity(intent);
    }
}
