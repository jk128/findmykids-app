package com.example.findmykids_app_children.model;

import androidx.annotation.NonNull;

public class Location {
    @NonNull
    public String getReportID() {
        return ReportID;
    }

    public void setReportID(@NonNull String reportID) {
        ReportID = reportID;
    }

    @NonNull
    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(@NonNull String origin) {
        Origin = origin;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(@NonNull String memberID) {
        MemberID = memberID;
    }

    @NonNull
    private String ReportID;
    @NonNull
    private String Origin;
    @NonNull
    private double longitude;
    @NonNull
    private double latitude;
    @NonNull
    private String MemberID;

    public Location(@NonNull String reportID, @NonNull String origin, double longitude, double latitude, @NonNull String memberID) {
        ReportID = reportID;
        Origin = origin;
        this.longitude = longitude;
        this.latitude = latitude;
        MemberID = memberID;
    }
}
