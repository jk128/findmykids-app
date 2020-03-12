package com.example.findmykidappparents.models;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.findmykidappparents.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Children {
    private LatLng latLng;
    private String name;
    private String id;

    private RequestQueue mQueue;
    private GoogleMap mMap;
    private Marker marker;

    private boolean hasChangeLocation = false;

    public Children()
    {

    }

    public Children(LatLng latLng, String name, String id) {
        this.latLng = latLng;
        this.name = name;
        this.id = id;
    }

    public RequestQueue getmQueue() {
        return mQueue;
    }

    public void setmQueue(RequestQueue mQueue) {
        this.mQueue = mQueue;
    }

    public LatLng getLatLng() {
        //return new LatLng(10.81667, 106.63333);
        return latLng;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
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

    public void GetData() {
        String url = "http://192.168.1.101:5004/teams/681c9af4-7780-4647-b180-ab7e01cb8617/members/875ea426-6317-4945-8dbf-b775fb3e9287";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public  void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("location");
                            Double lat = Double.parseDouble(jsonObject.getString("latitude"));
                            double lon = Double.parseDouble(jsonObject.getString("longitude"));


                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title("test")
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_kids_foreground));

                            marker = mMap.addMarker(markerOptions);

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lat, lon),
                                    15));

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
        mQueue.add(request);
    }
}