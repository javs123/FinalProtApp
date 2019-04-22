package com.example.javed.finalproapp;

public class Users {

    public String name, lastName, image, bio, userID;

    public Users() {
    }

    public Users(String name, String lastName, String image, String bio, String userID) {
        this.name = name;
        this.lastName = lastName;
        this.image = image;
        this.bio = bio;
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}