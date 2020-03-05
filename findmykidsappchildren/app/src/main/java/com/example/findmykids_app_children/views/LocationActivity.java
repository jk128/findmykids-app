package com.example.findmykids_app_children.views;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.findmykids_app_children.R;
import com.example.findmykids_app_children.databinding.ActivityLocationBinding;
import com.example.findmykids_app_children.databinding.ActivityLocationBindingImpl;
import com.example.findmykids_app_children.databinding.ActivityMainBinding;
import com.example.findmykids_app_children.viewmodels.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private GoogleMap mMap;
    private RequestQueue mQueue;
    private LocationViewModel locationViewModel = new LocationViewModel();
    Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLocationBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        activityMainBinding.setViewModelLocation(locationViewModel);
        activityMainBinding.executePendingBindings();

        mQueue = Volley.newRequestQueue(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(marker != null){
            marker.remove();
        }

        locationViewModel.SetLatLng(latLng);

        MarkerOptions markerOptions =
                new MarkerOptions().position(latLng).title(latLng.toString()).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_foreground));
        marker = mMap.addMarker(markerOptions);
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(latLng,15)
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.addMarker(new MarkerOptions().position(locationViewModel.GetLatLng()).title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_foreground)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationViewModel.GetLatLng(),15));
    }
}