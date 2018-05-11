package com.example.daniel.assistme;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

    List<Message> messageList = new ArrayList<>();
    Context context;

    public MessageAdapter(Context c){
        context = c;
    }

    public void addMessage(Message m) {
        messageList.add(m);
        notifyItemChanged(messageList.size());
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.message_view, parent, false);
        return new MessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.getUserName().setText(messageList.get(position).getUserName());
        holder.getMessageBody().setText(messageList.get(position).getMessageBody());
        holder.getDate().setText(messageList.get(position).getDate());
        if (messageList.get(position).getMsg_type().equals("2")) {

            holder.getPicture().setVisibility(View.VISIBLE);
            holder.getMessageBody().setVisibility(View.GONE);
            Glide.with(context).load(messageList.get(position).getPhotoUrl()).into(holder.getPicture());
        }
        else {
            holder.getMessageBody().setVisibility(View.VISIBLE);
            holder.getPicture().setVisibility(View.GONE);
        }
        if (holder.getUserName().getText().toString().equals(MainActivity.sharedPreferences.getString("Username", null))){

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.getMessageBox().getLayoutParams();
            params.setMargins(200, 0, 0, 0);
            holder.getMessageBox().setLayoutParams(params);
            holder.getCardBox().setCardBackgroundColor(Color.parseColor("#BBEEFF"));
        }
        else {

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.getMessageBox().getLayoutParams();
            params.setMargins(0, 0, 200, 0);
            holder.getMessageBox().setLayoutParams(params);
            holder.getCardBox().setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
