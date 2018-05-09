package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    Context context;
    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        userData = (User)bundle.getSerializable("userData");
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("userData", userData);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.app_settings:
                Intent intent2 = new Intent(this, SettingsActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("userData", userData);
                intent2.putExtras(bundle2);
                startActivity(intent2);
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
        Intent intent = new Intent(context, NewsActivity.class);
        startActivity(intent);
    }
}
