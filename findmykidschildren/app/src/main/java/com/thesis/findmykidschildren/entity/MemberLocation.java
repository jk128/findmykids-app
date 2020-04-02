package com.thesis.findmykidschildren.entity;

import com.google.gson.annotations.SerializedName;

public class MemberLocation {
    @SerializedName("MemberID")
    public String memberID;
    @SerializedName("Location")
    public MyLocation location;
}

