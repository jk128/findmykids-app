package com.example.findmykidappparents.model;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class Children {
    private LatLng latLng;
    private String name;
    private String id;

    private boolean hasChangeLocation = false;

    public Children(LatLng latLng, String name, String id) {
        this.latLng = latLng;
        this.name = name;
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasChangeLocation() {
        return hasChangeLocation;
    }

    public void setHasChangeLocation(boolean hasChangeLocation) {
        this.hasChangeLocation = hasChangeLocation;
    }

    public JsonObjectRequest GetData() {
        String url = "http://192.168.1.103:5004/families/681c9af4-7780-4647-b180-ab7e01cb8617/members/875ea426-6317-4945-8dbf-b775fb3e9287";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public  void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("location");
                            Double lat = Double.parseDouble(jsonObject.getString("latitude"));
                            double lon = Double.parseDouble(jsonObject.getString("longitude"));

                            if (lat != latLng.latitude || lon != latLng.longitude) {
                                hasChangeLocation = true;
                            } else {
                                hasChangeLocation = false;
                            }

                            latLng = new LatLng(lat, lon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        return request;
    }
}