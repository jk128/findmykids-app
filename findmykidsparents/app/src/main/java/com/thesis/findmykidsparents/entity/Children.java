package com.thesis.findmykidsparents.entity;

import com.google.gson.annotations.SerializedName;

public class Children {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("lat")
    public Double lat;
    @SerializedName("lon")
    public Double lon;

    public Children(String id, String name, Double lat, Double lon) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
}
