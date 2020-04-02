package com.thesis.findmykidschildren.entity;

import com.google.gson.annotations.SerializedName;

public class AuthLogged {
    @SerializedName("id")
    public String id;
    @SerializedName("accessToken")
    public String accessToken;
    @SerializedName("refreshToken")
    public String refreshToken;
    @SerializedName("parentID")
    public String parentID;
    @SerializedName("passwordConnect")
    public String passwordConnect;
}
