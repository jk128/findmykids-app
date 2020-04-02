package com.thesis.findmykidschildren.utils;

import com.google.gson.JsonElement;
import com.thesis.findmykidschildren.entity.Auth;
import com.thesis.findmykidschildren.entity.AuthLogged;
import com.thesis.findmykidschildren.entity.LocationReport;
import com.thesis.findmykidschildren.entity.MemberLocation;

import io.reactivex.Observable;

public class Repository {

    private ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    public Observable<AuthLogged> executeConncect(String email, String passWord, Double lat, Double lon) {
        return this.apiCallInterface.connect(new Auth(email, passWord, lat, lon));
    }

    public Observable<JsonElement> executeReportLocation(String memberId, LocationReport locationReport) {
        // api/members/7e344e4c-8f16-4918-b42c-cbfa19cf497f/locationreports
        return this.apiCallInterface.locationReports(Urls.REPORTLOCATION + "/members/locationreports", locationReport);
    }
}