package com.example.daniel.assistme;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageHolder extends RecyclerView.ViewHolder {

    TextView userName;
    TextView messageBody;
    TextView date;
    ImageView picture;
    LinearLayout messageBox;
    CardView cardBox;

    public MessageHolder(View itemView) {
        super(itemView);

        userName = (TextView) itemView.findViewById(R.id.userName);
        messageBody = (TextView) itemView.findViewById(R.id.messageBody);
        date = (TextView) itemView.findViewById(R.id.date);
        picture = (ImageView) itemView.findViewById(R.id.picture);
        messageBox = (LinearLayout) itemView.findViewById(R.id.messageBox);
        cardBox = (CardView) itemView.findViewById(R.id.cardBox);
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

    public ImageView getPicture() {
        return picture;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

    public LinearLayout getMessageBox() {
        return messageBox;
    }

    public void setMessageBox(LinearLayout messageBox) {
        this.messageBox = messageBox;
    }

    public CardView getCardBox() {
        return cardBox;
    }

    public void setCardBox(CardView cardBox) {
        this.cardBox = cardBox;
    }
}
