package com.example.findmykidappparents.ui.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.findmykidappparents.R;
import com.example.findmykidappparents.utils.API;
import com.example.findmykidappparents.utils.VolleySingleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements OnMapReadyCallback {

    private DetailViewModel detailViewModel;
    private Marker marker;
    private GoogleMap mMap;
    private LatLng latLngSave;
    private List<LatLng> mGeoPoints = new ArrayList<>();

    public static final String TAG = "MyTag";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailViewModel =
                ViewModelProviders.of(this).get(DetailViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ImageView imgMyLocation = root.findViewById(R.id.imgMyLocation);
        imgMyLocation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (latLngSave != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngSave));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        }
                    }
                }
        );

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        volleyProcess();
    }

    public void volleyProcess() {
        String url = API.HOST_LOCATION + "members/681c9af4-7780-4647-b180-ab7e01cb8617";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.v("TAg",response.toString());
                            JSONObject jsonObject = response.getJSONObject("location");
                            Double lat = Double.parseDouble(jsonObject.getString("latitude"));
                            Double lon = Double.parseDouble(jsonObject.getString("longitude"));

                            if (latLngSave == null || latLngSave.latitude != lat || latLngSave.longitude != lon) {
                                mGeoPoints.add(new LatLng(lat, lon));
                                mMap.addPolyline(new PolylineOptions()
                                        .addAll(mGeoPoints)
                                        .width(10)
                                        .color(Color.RED));

                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                        .title("Test")
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_kids_foreground));

                                if (marker != null) marker.remove();

                                marker = mMap.addMarker(markerOptions);

                                if (latLngSave == null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(
                                            new LatLng(lat, lon)));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                }

                                latLngSave = new LatLng(lat, lon);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        refreshAllContent(1000);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.v("catch", error.toString());
                        volleyProcess();
                    }
                });
        jsonObjectRequest.setTag(TAG);
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void refreshAllContent(final long timetoupdate) {
        new CountDownTimer(timetoupdate, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                try {
                    volleyProcess();
                }
                catch (Exception e) {
                    volleyProcess();
                }
            }
        }.start();
    }
}