package com.example.daniel.assistme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    CircleImageView userImage;
    TextView userName;
    RecyclerView messageList;
    EditText messageText;
    Button sendButton;
    ImageButton sendImageButton;

    MessageAdapter messageAdapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    static final int SEND_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String id = getIntent().getStringExtra("Chat_ID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userImage = (CircleImageView) findViewById(R.id.fotoPerfil);
        userName = (TextView) findViewById(R.id.userName);
        messageList = (RecyclerView) findViewById(R.id.messageList);
        messageText = (EditText) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendImageButton = (ImageButton) findViewById(R.id.sendImageButton);

        userName.setText(MainActivity.sharedPreferences.getString("Username", null));

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chat").child(id);
        storage = FirebaseStorage.getInstance();

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
                setScrollbar();
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

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String date;

        if (minute >= 10) date = Integer.toString(hourOfDay) + ':' + Integer.toString(minute);
        else date = Integer.toString(hourOfDay) + ":0" + Integer.toString(minute);

        Message m = new Message(messageText.getText().toString(), userName.getText().toString(), "", date, "1");

        databaseReference.push().setValue(m);

        messageText.setText("");
    }

    public void SendPicture(View view) {

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(i, "Select a picture"), SEND_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEND_PHOTO && resultCode == RESULT_OK) {

            Uri u = data.getData();
            storageReference = storage.getReference("chat_images");
            final StorageReference photoReference = storageReference.child(u.getLastPathSegment());
            photoReference.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri u = taskSnapshot.getDownloadUrl();

                    Calendar calendar = Calendar.getInstance();
                    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    String date;

                    if (minute >= 10) date = Integer.toString(hourOfDay) + ':' + Integer.toString(minute);
                    else date = Integer.toString(hourOfDay) + ":0" + Integer.toString(minute);

                    Message m = new Message("", u.toString(), userName.getText().toString(), "", date, "2");
                    databaseReference.push().setValue(m);

                    setScrollbar();
                }
            });
        }
    }

    public void CloseChat(View view) {
        finish();
    }
}
