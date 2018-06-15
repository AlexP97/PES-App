package com.example.daniel.assistme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SolicitudChatActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    User userData;

    CircleImageView profileImage;

    int i;

    LinearLayout cancelRequest, sendRequest;

    boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        userData = MainActivity.getUser();

        active = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_chat);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chats_pendientes");
        databaseReference2 = database.getReference("chat").child(MainActivity.sharedPreferences.getString("Username", null));

        cancelRequest = findViewById(R.id.CancelRequest);
        sendRequest = findViewById(R.id.SendRequest);

        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        if (!userData.getUrl_picture().matches("")) {
            Uri u = Uri.parse(userData.getUrl_picture());

            Glide.with(SolicitudChatActivity.this).load(u).into(profileImage);
        }

        databaseReference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                i++;
                if (i > 1 && active) {
                    active = false;
                    sendRequest.setVisibility(View.GONE);
                    cancelRequest.setVisibility(View.GONE);
                    Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                    intent.putExtra("Chat_ID", MainActivity.sharedPreferences.getString("Username", null));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                sendRequest.setVisibility(View.GONE);
                cancelRequest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (i == 0 && active){
            sendRequest.setVisibility(View.VISIBLE);
            cancelRequest.setVisibility(View.GONE);
        }
    }

    public void AbrirPopUp(View view) {

        Intent intent = new Intent(this, PopUpRequestActivity.class);
        startActivity(intent);
    }

    public void DeleteRequest(View view) {

        databaseReference2.removeValue();

        Query requestQuery = databaseReference.orderByChild("userName").equalTo(MainActivity.sharedPreferences.getString("Username", null));

        requestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {

                    requestSnapshot.getRef().removeValue();
                    Toast t = Toast.makeText(getApplicationContext(), "Request canceled", Toast.LENGTH_SHORT);
                    t.show();
                    sendRequest.setVisibility(View.VISIBLE);
                    cancelRequest.setVisibility(View.GONE);
                    i = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
