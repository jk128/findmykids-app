package com.thesis.findmykidsparents.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}