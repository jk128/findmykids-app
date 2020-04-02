package com.thesis.findmykidsparents.utils;

import com.thesis.findmykidsparents.entity.Auth;
import com.thesis.findmykidsparents.entity.AuthLogged;
import com.thesis.findmykidsparents.entity.Children;
import com.thesis.findmykidsparents.entity.MemberLocation;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiCallInterface {
    @POST(Urls.LOGIN)
    Observable<AuthLogged> login(@Body Auth auth);

    @GET
    Observable<MemberLocation> getLocation(@Url String url);

    @GET(Urls.GENPASSWORD)
    Observable<String> getPasswordConnect();

    @GET(Urls.CHILDREN + "/{memberId}/{count}")
    Observable<ArrayList<Children>> getChildren(@Path(value = "memberId") String memberId, @Path(value = "count") int count);
}