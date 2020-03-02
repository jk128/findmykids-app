package com.example.googlemapdemo.views;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.googlemapdemo.R;
import com.example.googlemapdemo.viewmodels.ChildCoordinates;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private RequestQueue mQueue;
    private ChildCoordinates  childCoordinates = new ChildCoordinates();

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

        childCoordinates.setLatitude(latLng.latitude);
        childCoordinates.setLongitude(latLng.longitude);

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
        params.put("ReportID", childCoordinates.getReportID());
        params.put("Origin", childCoordinates.getOrigin());
        params.put("Latitude", childCoordinates.getLatitude().toString());
        params.put("Longitude", childCoordinates.getLongitude().toString());
        params.put("MemberID", childCoordinates.getMemberID());

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
