package com.example.googlemapdemo.model;

public class Location {
    private String ReportID;
    private String Origin;
    private double longitude;
    private double latitude;
    private String MemberID;

    public Location(String reportID, String origin, double longitude, double latitude, String memberID) {
        ReportID = reportID;
        Origin = origin;
        this.longitude = longitude;
        this.latitude = latitude;
        MemberID = memberID;
    }

    public String getReportID() {
        return ReportID;
    }

    public void setReportID(String reportID) {
        ReportID = reportID;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
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

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }
}


