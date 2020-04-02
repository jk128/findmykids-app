package com.thesis.findmykidschildren.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyParents {
    @SerializedName("parents")
    public ArrayList<Parent> parents = new ArrayList<>();
}
