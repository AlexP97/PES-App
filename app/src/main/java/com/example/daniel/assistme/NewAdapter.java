package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewHolder> {
    List<New> news = new ArrayList<>();
    Context context;


    public NewAdapter(Context c){
        context = c;
    }

    public void addNew(New n){
        news.add(n);
        notifyItemChanged(news.size());
    }
    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news, parent, false);
        return new NewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewHolder holder, int position) {
        holder.getNewTv().setText(news.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
