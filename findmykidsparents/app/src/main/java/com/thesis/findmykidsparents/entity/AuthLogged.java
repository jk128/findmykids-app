package com.thesis.findmykidsparents.entity;

import com.google.gson.annotations.SerializedName;

public class AuthLogged {
    @SerializedName("id")
    public String id;
//    @SerializedName("name")
//    public String name;
    @SerializedName("authenticated")
    public Boolean authenticated;
    @SerializedName("accessToken")
    public String accessToken;
    @SerializedName("refreshToken")
    public String refreshToken;
}
