package com.example.daniel.assistme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Calendar;

import android.view.*;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestListActivity extends AppCompatActivity {

    ListView messageList;

    ArrayList<Message> requests = new ArrayList<>();

    boolean active;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        active = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        messageList = findViewById(R.id.request_list);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chats_pendientes");
        databaseReference2 = database.getReference(MainActivity.sharedPreferences.getString("Username", null));

        final CustomAdapter adapter = new CustomAdapter();
        messageList.setAdapter(adapter);

        Query requestQuery = databaseReference2.orderByChild("userName").equalTo(MainActivity.sharedPreferences.getString("Username", null));

        requestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {

                    if (active) {

                        active = false;

                        Message m = requestSnapshot.getValue(Message.class);

                        Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                        intent.putExtra("Chat_ID", m.getMessageBody());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message m = dataSnapshot.getValue(Message.class);
                adapter.addMessage(m);
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

        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                Query requestQuery = databaseReference.orderByChild("userName").equalTo(requests.get(position).getUserName());

                requestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {

                            if (active) {

                                active = false;

                                requestSnapshot.getRef().removeValue();

                                Calendar calendar = Calendar.getInstance();
                                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                                int minute = calendar.get(Calendar.MINUTE);

                                String date;

                                if (minute >= 10) date = Integer.toString(hourOfDay) + ':' + Integer.toString(minute);
                                else date = Integer.toString(hourOfDay) + ":0" + Integer.toString(minute);

                                Message m = new Message(requests.get(position).getUserName(), MainActivity.sharedPreferences.getString("Username", null), MainActivity.getUser().getUrl_picture(), date, "1");

                                databaseReference2.push().setValue(m);

                                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                                intent.putExtra("Chat_ID", requests.get(position).getUserName());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    class CustomAdapter extends android.widget.BaseAdapter {

        @Override
        public int getCount() {
            return requests.size();
        }

        @Override
        public Message getItem(int i) {
            return null;
        }

        public void addMessage(Message m) {
            requests.add(m);
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.chat_request_model, null);

            TextView username = view.findViewById(R.id.username);
            TextView messageBody = view.findViewById(R.id.messageBody);
            CircleImageView profileImage = view.findViewById(R.id.profileImage);

            username.setText(requests.get(i).getUserName());
            messageBody.setText(requests.get(i).getMessageBody());

            if (requests.get(i).getUserImage() != null) {
                if (!requests.get(i).getUserImage().matches("")) {
                    Uri u = Uri.parse(requests.get(i).getUserImage());

                    Glide.with(RequestListActivity.this).load(u).into(profileImage);
                }
            }
            return view;
        }
    }
}
