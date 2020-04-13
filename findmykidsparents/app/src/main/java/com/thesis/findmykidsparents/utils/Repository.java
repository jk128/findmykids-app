package com.thesis.findmykidsparents.utils;

import com.thesis.findmykidsparents.entity.Auth;
import com.thesis.findmykidsparents.entity.AuthLogged;
import com.thesis.findmykidsparents.entity.Children;
import com.thesis.findmykidsparents.entity.MemberLocation;
import com.thesis.findmykidsparents.entity.Parent;

import java.util.ArrayList;

import io.reactivex.Observable;

public class Repository {

    private ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    public Observable<Parent> executeRegisterLogin(String email, String passWord) {
        return this.apiCallInterface.registerLogin(new Parent(email, passWord));
    }

    public Observable<AuthLogged> executeLoginFinish(String email, String passWord) {
        // `Bearer
        return this.apiCallInterface.loginFinish(Urls.LOGIN_FINISH, new Parent(email, passWord));
    }

    public Observable<AuthLogged> executeLogin(String email, String passWord) {
        return this.apiCallInterface.login(new Auth(email, passWord));
    }

    public Observable<MemberLocation> executeDetail(String memberId) {
        return this.apiCallInterface.getLocation(Urls.LOCATION + "members/" + memberId);
    }

    public Observable<String> executeGenPassword() {
        return this.apiCallInterface.getPasswordConnect();
    }

    public Observable<ArrayList<Children>> executeGetChildren(String memberId, int count) {
        return this.apiCallInterface.getChildren(memberId, count);
    }
}