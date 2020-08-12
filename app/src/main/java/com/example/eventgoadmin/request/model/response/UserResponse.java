package com.example.eventgoadmin.request.model.response;

import com.example.eventgoadmin.request.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserResponse {
    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data_user")
    @Expose
    private List<User> userList;

    @SerializedName("user")
    @Expose
    private User user;


    public UserResponse(int value, String message, List<User> userList, User user) {
        this.value = value;
        this.message = message;
        this.userList = userList;
        this.user = user;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
