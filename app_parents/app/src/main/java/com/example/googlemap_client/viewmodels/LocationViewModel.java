package com.example.googlemap_client.viewmodels;

import com.example.googlemap_client.model.Location;

public class LocationViewModel {
    private Location location;

    public LocationViewModel() {
        location.setLatitude(0.0);
        location.setLongitude(0.0);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Double Longitude, Double Latitude) {
        this.location.setLongitude(Longitude);
        this.location.setLatitude(Latitude);
    }
}
