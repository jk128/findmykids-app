package com.thesis.findmykidsparents.di;

import android.content.Context;

import com.google.gson.Gson;
import com.thesis.findmykidsparents.entity.AuthLogged;
import com.thesis.findmykidsparents.utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {
    SessionManager sessionManager;
    OkHttpClient httpClient;

    public HttpInterceptor(OkHttpClient httpClient) {
        this.sessionManager = SessionManager.getInstance(null);
        this.httpClient = httpClient;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        //Build new request
        Request.Builder builder = request.newBuilder();
        builder.header("Accept", "application/json"); //if necessary, say to consume JSON
        HashMap<String, String> info = sessionManager.getUserDetails();
        String token = info.get(SessionManager.KEY_AccessToken); //save token of this request for future
        setAuthHeader(builder, token); //write current token to request

        request = builder.build(); //overwrite old request
        Response response = chain.proceed(request); //perform request, here original request will be executed

        if (response.code() == 401) { //if unauthorized
            synchronized (httpClient) { //perform all 401 in sync blocks, to avoid multiply token updates
                String currentToken = info.get(SessionManager.KEY_AccessToken); //get currently stored token

                if (currentToken != null && currentToken.equals(token)) { //compare current token with token that was stored before, if it was not updated - do update
                    int code = refreshToken(info.get(SessionManager.KEY_RefreshToken)) / 100; //refresh token
                    if (code != 2) { //if refresh token failed for some reason
                        if (code == 4) //only if response is 400, 500 might mean that token was not updated
                            sessionManager.logoutUser(); //go to login screen
                        return response; //if token refresh failed - show error to user
                    }
                }

                if (info.get(SessionManager.KEY_AccessToken) != null) { //retry requires new auth token,
                    setAuthHeader(builder, info.get(SessionManager.KEY_AccessToken)); //set auth token to updated
                    request = builder.build();
                    return chain.proceed(request); //repeat request with new token
                }
            }
        }

        return response;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token));
    }

    private int refreshToken(String refreshToken) throws IOException {
        //Refresh token, synchronously, save it, and return result code
        //you might use retrofit here
        URL refreshUrl = new URL("http://192.168.1.117:5012/api/account/renewToken/" + refreshToken);
        HttpURLConnection urlConnection = (HttpURLConnection) refreshUrl.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Content-Type", "application/json");

//        urlConnection.setUseCaches(false);
//        String urlParameters = "grant_type=refresh_token&client_id="
//                + cid
//                + "&client_secret="
//                + csecret
//                + "&refresh_token="
//                + refresh;
//
//        urlConnection.setDoOutput(true);
//        DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//        wr.writeBytes(urlParameters);
//        wr.flush();
//        wr.close();

        int responseCode = urlConnection.getResponseCode();

        if (responseCode == 201) {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // this gson part is optional , you can read response directly from Json too
            Gson gson = new Gson();
            AuthLogged authLogged = gson.fromJson(response.toString(), AuthLogged.class);
            sessionManager.createLoginSession(authLogged);
        }
        return responseCode;
    }
}
