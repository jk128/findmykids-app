package com.example.googlemapdemo.viewmodels;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.googlemapdemo.model.Location;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChildCoordinates {

    private String ReportID;
    private String Origin;
    private Double Latitude;
    private Double Longitude;
    private String MemberID;

    public ChildCoordinates(){
        ReportID = UUID.randomUUID().toString();
        Origin = UUID.randomUUID().toString();
        Latitude = 0.0;
        Longitude = 0.0;
        MemberID = "875ea426-6317-4945-8dbf-b775fb3e9287";
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

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }
}
