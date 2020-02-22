package com.example.googlemapdemo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {
    private class Location {
        public String ReportID = UUID.randomUUID().toString();
        public String Origin = UUID.randomUUID().toString();
        public Double Latitude = 0.0;
        public Double Longitude = 0.0;
        public String MemberID = "875ea426-6317-4945-8dbf-b775fb3e9287";

        /*@Override
        public String toString() {
            JSONObject jsonObject= new JSONObject();

            try {
                jsonObject.put("ReportID", ReportID);
                jsonObject.put("Origin", Origin);
                jsonObject.put("Latitude", Latitude);
                jsonObject.put("Longitude", Longitude);
                jsonObject.put("MemberID", MemberID);
                return jsonObject.toString();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        }*/
    }

    private GoogleMap mMap;
    private RequestQueue mQueue;
    private Location loc = new Location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        mMap.setOnMapClickListener(this);
        //mMap.setOnMapLongClickListener(this);
        LatLng sydney = new LatLng(10.81667, 106.63333);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_foreground)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }

    Marker marker;
    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(MainActivity.this,
                "onMapClick:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();

        if(marker != null){
            marker.remove();
        }

        loc.Latitude = latLng.latitude;
        loc.Longitude = latLng.longitude;

        //Add marker on Click position
        MarkerOptions markerOptions =
                new MarkerOptions().position(latLng).title(latLng.toString()).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_foreground));
        marker = mMap.addMarker(markerOptions);
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng,15)
        );

        SendData();
    }


    private  void SendData() {
        String url = "http://192.168.1.103:5002/api/members/875ea426-6317-4945-8dbf-b775fb3e9287/locationreports";

        Map<String, String> params = new HashMap<>();
        params.put("ReportID", loc.ReportID);
        params.put("Origin", loc.Origin);
        params.put("Latitude", loc.Latitude.toString());
        params.put("Longitude", loc.Longitude.toString());
        params.put("MemberID", loc.MemberID);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public  void onResponse(JSONObject response) {

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
