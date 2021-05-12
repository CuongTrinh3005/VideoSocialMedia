package com.example.myapplication.Entities;

public class User{
    String userID;
    String email;
    String name;
    String image;

    public User(String userID, String email, String name,  String image) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
