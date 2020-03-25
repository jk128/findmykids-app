package com.example.findmykidappparents.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.findmykidappparents.R;
import com.example.findmykidappparents.data.model.LoggedInUser;
import com.example.findmykidappparents.utils.API;
import com.example.findmykidappparents.utils.VolleySingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    LoggedInUser loggedInUser = null;
    Boolean waitting = true;
    public Result<LoggedInUser> login(final String username, final String password) {
        try {
            waitting = true;
            String url = API.HOST_LOGIN + "members/login";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Boolean authenticated = (Boolean) response.get("authenticated");

                                if (authenticated) {
                                    String Id = (String) response.get("id");
                                    String name = (String) response.get("name");
                                    String AccessToken = (String) response.get("accessToken");
                                    String RefreshToken = (String) response.get("refreshToken");
                                    loggedInUser = new LoggedInUser(Id, name, authenticated, AccessToken, RefreshToken);
                                }
                                else
                                {
                                    loggedInUser = new LoggedInUser("Id", "name", authenticated, "AccessToken", "RefreshToken");
                                }

                                waitting = false;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                        }
                    }) {
                @Override
                public byte[] getBody() {
                    JSONObject jsonBodyObj = new JSONObject();
                    try {
                        jsonBodyObj.put("UserName", username);
                        jsonBodyObj.put("PassWord", password);
                    } catch (
                            JSONException e) {
                        e.printStackTrace();
                    }
                    String requestBody = jsonBodyObj.toString();  //The request body goes in here.

                    try {
                        return requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };
            jsonObjectRequest.setTag("LOGIN");
            VolleySingleton.getInstance(null).addToRequestQueue(jsonObjectRequest);

            // TODO: handle loggedInUser authentication
            if (loggedInUser != null && loggedInUser.getAuthenticated()) {
                return new Result.Success<>(loggedInUser);
            }
            return new Result.Error(new IOException("Đăng nhập không thành công!"));
        } catch (Exception e) {
            return new Result.Error(new IOException("Lỗi đăng nhập!", e));
        }
        finally {

        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
