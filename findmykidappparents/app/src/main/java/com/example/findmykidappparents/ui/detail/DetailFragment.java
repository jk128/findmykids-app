package com.example.findmykidappparents.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.findmykidappparents.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private DetailViewModel detailViewModel;
    private RequestQueue mQueue;
    private Marker marker;
    private GoogleMap mMap;

    private Timer myTimer;
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            GetData();
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailViewModel =
                ViewModelProviders.of(this).get(DetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        mQueue = Volley.newRequestQueue(this.getContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ImageView imgMyLocation = root.findViewById(R.id.imgMyLocation);
        imgMyLocation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailViewModel.MoveMyLocation();
                    }
                }
        );

//        myTimer = new Timer();
//        myTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                getActivity().runOnUiThread(Timer_Tick);
//            }
//        }, 0, 3000);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.detailViewModel.setmMap(googleMap);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(10.81667, 106.63333))
                .title("Test")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_kids_foreground));

        marker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(10.81667, 106.63333),
                15));

        //GetData();
    }

    public void GetData() {
        String url = "http://localhost:5000/teams/681c9af4-7780-4647-b180-ab7e01cb8617/members/875ea426-6317-4945-8dbf-b775fb3e9287";
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public  void onResponse(JSONObject response) {
//                        try {
//                            JSONObject jsonObject = response.getJSONObject("location");
//                            Double lat = Double.parseDouble(jsonObject.getString("latitude"));
//                            Double lon = Double.parseDouble(jsonObject.getString("longitude"));
//
//                            MarkerOptions markerOptions = new MarkerOptions()
//                                    .position(new LatLng(lat, lon))
//                                    .title("Test")
//                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_kids_foreground));
//
//                            marker = mMap.addMarker(markerOptions);
//
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(lat, lon),
//                                    15));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        this.mQueue.add(request);
    }


}