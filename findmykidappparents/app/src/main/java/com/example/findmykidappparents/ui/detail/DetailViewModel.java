package com.example.findmykidappparents.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.findmykidappparents.models.Children;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class DetailViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private Children children = new Children();
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

    public DetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is xxxxxxx fragment");
        this.children = new Children(new LatLng(10.81667, 106.63333), "Lê Tuấn Anh", "681c9af4-7780-4647-b180-ab7e01cb8617");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void MoveMyLocation() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                children.getLatLng(),
                15));
    }
}