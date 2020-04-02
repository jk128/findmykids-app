package com.thesis.findmykidschildren.entity;

import com.google.gson.annotations.SerializedName;

public class LocationReport {
    @SerializedName("ReportID")
    public String ReportID;
    @SerializedName("Origin")
    public String Origin;
    @SerializedName("Latitude")
    public double Latitude;
    @SerializedName("Longitude")
    public double Longitude;
    @SerializedName("MemberID")
    public String MemberID;
}
