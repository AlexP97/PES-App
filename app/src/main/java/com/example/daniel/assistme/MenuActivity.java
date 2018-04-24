package com.example.daniel.assistme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity {

    String username;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        username = getIntent().getStringExtra("EXTRA_SESSION_ID");

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
}
