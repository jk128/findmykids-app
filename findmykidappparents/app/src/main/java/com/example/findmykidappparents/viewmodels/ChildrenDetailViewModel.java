package com.example.findmykidappparents.viewmodels;

import androidx.databinding.BaseObservable;

import com.example.findmykidappparents.model.Children;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class ChildrenDetailViewModel extends BaseObservable {
    private Children children;
    private GoogleMap mMap;

    public Children getChildren() {
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public ChildrenDetailViewModel() {
        this.children = new Children(new LatLng(10.81667, 106.63333), "Lê Tuấn Anh", "681c9af4-7780-4647-b180-ab7e01cb8617");
    }

    public void MoveMyLocation() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                children.getLatLng(),
                15));
    }
}
