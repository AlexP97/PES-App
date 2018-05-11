package com.example.daniel.assistme;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NewHolder extends RecyclerView.ViewHolder {
    TextView newTv;

    public NewHolder(View itemView) {
        super(itemView);

        newTv = (TextView) itemView.findViewById(R.id.textViewNew);
    }
    public TextView getNewTv() { return newTv; }
}
