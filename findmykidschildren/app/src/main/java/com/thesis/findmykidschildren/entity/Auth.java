package com.thesis.findmykidschildren.entity;

import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("parentID")
    public String parentID;
    @SerializedName("passwordConnect")
    public String passwordConnect;
    @SerializedName("lat")
    public Double lat;
    @SerializedName("lon")
    public Double lon;

    public Auth(String parentID, String passwordConnect, Double lat, Double lon) {
        this.parentID = parentID;
        this.passwordConnect = passwordConnect;
        this.lat = lat;
        this.lon = lon;
    }
}
