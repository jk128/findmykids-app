package com.thesis.findmykidsparents.entity;

import com.google.gson.annotations.SerializedName;

public class MemberLocation {
    @SerializedName("memberID")
    public String memberID;
    public MyLocation location;
}

