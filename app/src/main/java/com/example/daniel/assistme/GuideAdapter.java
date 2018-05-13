package com.example.daniel.assistme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GuideAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Guide> items;
    protected Context context;

    public GuideAdapter (Activity activity, ArrayList<Guide> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(Guide category) {
        items.add(category);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.guide_view, null);
        }

        TextView text = v.findViewById(R.id.textView_title);
        text.setText(items.get(position).getTitle());

        return v;
    }
}
