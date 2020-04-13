package com.thesis.findmykidsparents.entity;

import com.google.gson.annotations.SerializedName;

public class Parent {
    @SerializedName("id")
    public String email;
    @SerializedName("passWord")
    public String passWord;

    public Parent(String email, String passWord) {
        this.email = email;
        this.passWord = passWord;
    }
}
