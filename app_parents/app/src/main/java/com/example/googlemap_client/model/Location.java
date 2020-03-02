package com.example.googlemap_client.model;

public class Location {
    private Double Longitude;
    private Double latitude;

    public Location(Double longitude, Double latitude) {
        Longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
