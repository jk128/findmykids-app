package com.thesis.findmykidsparents.detail;

import android.graphics.Color;
import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thesis.findmykidsparents.R;
import com.thesis.findmykidsparents.entity.Children;
import com.thesis.findmykidsparents.entity.MemberLocation;
import com.thesis.findmykidsparents.utils.ApiResponse;
import com.thesis.findmykidsparents.utils.Repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    Marker marker;
    private Repository repository;
    private GoogleMap mMap;
    private List<LatLng> mGeoPoints = new ArrayList<>();

    public DetailViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> detailResponse() {
        return responseLiveData;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    /*
     * method to call normal login api with $(email + password)
     * */
    public void hitDetailApi(String memberId) {
        disposables.add(repository.executeDetail(memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    public void getMemberLocation(final long timetoupdate, MemberLocation memberLocation, Children children) {
        LatLng loc = new LatLng(memberLocation.location.latitude, memberLocation.location.longitude);

        if (marker != null) marker.remove();

        mGeoPoints.add(loc);
        mMap.addPolyline(new PolylineOptions()
                .addAll(mGeoPoints)
                .width(10)
                .color(Color.RED));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(loc)
                .title(children.name)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point_foreground));
        markerOptions.anchor(0.5f, 0.5f);
        marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();
        new CountDownTimer(timetoupdate, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    hitDetailApi(children.id);
                } catch (Exception e) {
                    //volleyProcess();
                }
            }
        }.start();
    }
}
