package com.example.daniel.assistme;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageHolder extends RecyclerView.ViewHolder {

    TextView userName;
    TextView messageBody;
    TextView date;

    public MessageHolder(View itemView) {
        super(itemView);

        userName = (TextView) itemView.findViewById(R.id.userName);
        messageBody = (TextView) itemView.findViewById(R.id.messageBody);
        date = (TextView) itemView.findViewById(R.id.date);
    }

    public TextView getUserName() {
        return userName;
    }

    public void setUserName(TextView userName) {
        this.userName = userName;
    }

    public TextView getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(TextView messageBody) {
        this.messageBody = messageBody;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

}
