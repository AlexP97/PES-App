package com.example.daniel.assistme;

public class User {
    private String username;
    private String name;
    private String surname;
    private String mail;
    private String password;

    public User(){
        super();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userN) {
        this.username = userN;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

