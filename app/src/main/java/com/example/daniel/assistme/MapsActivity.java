package com.example.daniel.assistme;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mapPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mapPermission = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in cear and move the camera
        LatLng cear = new LatLng(41.379377, 2.171869);
        //mMap.addMarker(new MarkerOptions().position(cear).title("CEAR"));
        CameraPosition cearCamera = new CameraPosition.Builder().target(cear)
                .zoom(12f)
                .bearing(0)
                .tilt(25)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cearCamera));
        addMarkers();
    }
    private void addMarkers(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String markersString = extras.getString("points");
            try {
                JSONArray markers = new JSONArray(markersString);
                for (int i = 0; i < markers.length(); i++){
                    JSONObject marker = markers.getJSONObject(i);  //coger un marker
                    JSONObject locationJSON = marker.getJSONObject("location");
                    JSONObject titleJSON = marker.getJSONObject("text");
                    String latS = null, lngS = null, title = null;
                    if (locationJSON.has("lat")){
                        latS = marker.getString("lat");
                    }
                    if (locationJSON.has("lng")){
                        lngS = marker.getString("lng");
                    }
                    if (titleJSON.has("text")){
                        title = marker.getString("text");
                    }
                    Float latF = null, lngF = null;
                    if (latS != null) latF = Float.parseFloat(latS);
                    if (lngS != null) lngF = Float.parseFloat(lngS);
                    LatLng newMarker = null;
                    if (latF != null && lngF != null) newMarker = new LatLng(latF, lngF);
                    if (newMarker != null) mMap.addMarker(new MarkerOptions().position(newMarker).title(title));
                }
            } catch (JSONException e) {
                Toast t = Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT);
                t.show();
            }

        }
    }
}
