package com.example.chatapplication.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone ;
    private String username;

    public UserModel() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    private Timestamp createdTimestamp;
    public UserModel(String phone, String username, Timestamp createdTimestamp) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
    }


}
