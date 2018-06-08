package com.example.daniel.assistme;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        switch (parent.getItemAtPosition(pos).toString()){
            case "English":
                ViewGuideActivity.setLen_to("en");
                break;
            case "Catalan":
                ViewGuideActivity.setLen_to("ca");
                break;
            case "Spanish":
                ViewGuideActivity.setLen_to("es");
                break;
            case "French":
                ViewGuideActivity.setLen_to("fr");
                break;
            case "Italian":
                ViewGuideActivity.setLen_to("it");
                break;
            default:
                break;
        }
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
