package com.example.daniel.assistme;

public class New {
    String content;
    String url;
    public New(){

    }
    public New(String c, String u){
        content = c;
        url = u;
    }
    public String getContent(){
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
