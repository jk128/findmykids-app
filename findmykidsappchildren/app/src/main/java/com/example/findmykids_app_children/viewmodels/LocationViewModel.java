package com.example.findmykids_app_children.viewmodels;

import androidx.databinding.BaseObservable;

import com.example.findmykids_app_children.model.Location;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class LocationViewModel extends BaseObservable {
    private Location location;

    public LocationViewModel() {
        this.location = new Location(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                106.645331,
                10.768748,
                UUID.randomUUID().toString()
        );
    }

    public LatLng GetLatLng() {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void SetLatLng(LatLng latLng) {
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
    }
}
