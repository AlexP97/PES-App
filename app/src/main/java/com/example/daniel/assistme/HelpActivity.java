package com.example.daniel.assistme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void ExternalLinkButton1 (View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.bcn.cat/novaciutadania/index_en.html")));
    }

    public void ExternalLinkButton2 (View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.cear.es/")));
    }

    public void ExternalLinkButton3 (View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://mossos.gencat.cat/ca/inici")));
    }

    public void ExternalLinkButton4 (View view) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://web.gencat.cat/en/temes/salut/")));
    }
}