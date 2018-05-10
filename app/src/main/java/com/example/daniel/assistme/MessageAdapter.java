package com.example.daniel.assistme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
