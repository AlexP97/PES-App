package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView userImage;
    TextView userName;
    RecyclerView messageList;
    EditText messageText;
    Button sendButton;

    MessageAdapter messageAdapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userImage = (CircleImageView) findViewById(R.id.fotoPerfil);
        userName = (TextView) findViewById(R.id.userName);
        messageList = (RecyclerView) findViewById(R.id.messageList);
        messageText = (EditText) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);

        userName.setText(MainActivity.sharedPreferences.getString("Username", null));

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat");

        messageAdapter = new MessageAdapter(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        messageList.setLayoutManager(l);
        messageList.setAdapter(messageAdapter);

        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message m = dataSnapshot.getValue(Message.class);
                messageAdapter.addMessage(m);
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
    }

    private void setScrollbar() {
        messageList.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    public void SendMessage(View view){

        Message m = new Message(messageText.getText().toString(), userName.getText().toString(), "", "00:00", "1");

        databaseReference.push().setValue(m);

        messageText.setText("");
    }
}
