package com.example.daniel.assistme;

public class Message {

    String messageBody;
    String userName;
    String userImage;
    String date;
    String msg_type;


    public Message() {

    }

    public Message(String messageBody, String userName, String userImage, String date, String msg_type) {
        this.messageBody = messageBody;
        this.userName = userName;
        this.userImage = userImage;
        this.date = date;
        this.msg_type = msg_type;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }
}
