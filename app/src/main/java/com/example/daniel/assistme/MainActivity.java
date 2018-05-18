package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
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

    public static void setSharedPreferences(User userData){
        user = userData;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("Username", userData.getUsername());
        editor.putString("Password", userData.getPassword());
        editor.putString("Mail", userData.getMail());
        editor.apply();
    }
    public static User getUser(){
        return user;
    }
}

