package com.thesis.findmykidschildren.entity;

import com.google.gson.annotations.SerializedName;

public class Parent {
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;

    public Parent(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
