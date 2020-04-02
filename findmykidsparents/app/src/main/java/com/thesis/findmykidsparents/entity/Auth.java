package com.thesis.findmykidsparents.entity;

import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("email")
    public String email;
    @SerializedName("passWord")
    public String passWord;
    @SerializedName("lat")
    public Double lat;
    @SerializedName("lon")
    public Double lon;

    public Auth(String email, String passWord) {
        this.email = email;
        this.passWord = passWord;
    }
}
