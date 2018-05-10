package com.example.daniel.assistme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView userImage;
    TextView userName;
    RecyclerView messageList;
    EditText messageText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userImage = (CircleImageView) findViewById(R.id.fotoPerfil);
        userName = (TextView) findViewById(R.id.userName);
        messageList = (RecyclerView) findViewById(R.id.messageList);
        messageText = (EditText) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);
    }
}
