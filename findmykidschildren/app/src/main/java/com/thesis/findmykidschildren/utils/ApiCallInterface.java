package com.thesis.findmykidschildren.utils;

import com.google.gson.JsonElement;
import com.thesis.findmykidschildren.entity.Auth;
import com.thesis.findmykidschildren.entity.AuthLogged;
import com.thesis.findmykidschildren.entity.LocationReport;
import com.thesis.findmykidschildren.entity.MemberLocation;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiCallInterface {
    @POST(Urls.LOGIN)
    Observable<AuthLogged> login(@Body Auth auth);

    @POST(Urls.LOCATION + "/{memberId}")
    Observable<MemberLocation> getLocation(@Path(value = "memberId") String memberId);

    @POST(Urls.CONNECT)
    Observable<AuthLogged> connect(@Body Auth auth);

    @POST
    Observable<JsonElement> locationReports(@Url String url, @Body LocationReport locationReport);
}