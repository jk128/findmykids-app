package com.example.googlemap_client.views;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.googlemap_client.R;
import com.example.googlemap_client.viewmodels.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private RequestQueue mQueue;
    LocationViewModel locationViewModel = new LocationViewModel();
    LatLng myPosition = new LatLng(locationViewModel.getLocation().getLongitude(),
                                    locationViewModel.getLocation().getLatitude());

    String name = "Lê Tuấn Anh";

    private Timer myTimer;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(Timer_Tick);
            }

        }, 0, 3000);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            RealTimePosition();
        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(15);
        mMap.setMaxZoomPreference(2000f);

        // Set position init
        RealTimePosition();
    }

    private void RealTimePosition() {
        jsonParse();

        mMap.addMarker(new MarkerOptions().position(myPosition).title(name).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_panda)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
    }

    private  void jsonParse() {
        String url = "http://192.168.1.103:5004/families/681c9af4-7780-4647-b180-ab7e01cb8617/members/875ea426-6317-4945-8dbf-b775fb3e9287";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public  void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("location");
                            locationViewModel.setLocation(Double.parseDouble(jsonObject.getString("latitude")),
                                    Double.parseDouble(jsonObject.getString("longitude")) );
                            myPosition = new LatLng(locationViewModel.getLocation().getLongitude(), locationViewModel.getLocation().getLatitude());
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