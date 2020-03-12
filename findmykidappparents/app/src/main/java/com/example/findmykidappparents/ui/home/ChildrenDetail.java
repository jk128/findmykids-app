package com.example.findmykidappparents.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.findmykidappparents.R;
import com.example.findmykidappparents.databinding.ActivityChildrendetailBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class ChildrenDetail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue mQueue;
    Marker marker;
    ChildrenDetailViewModel childrenDetailViewModel = new ChildrenDetailViewModel();

    ImageView imageViewListBack;

    private Timer myTimer;
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            childrenDetailViewModel.getChildren().GetData();
            if (childrenDetailViewModel.getChildren().isHasChangeLocation()) {
                if (marker != null) {
                    marker.remove();
                }

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(childrenDetailViewModel.getChildren().getLatLng())
                        .title(childrenDetailViewModel.getChildren().getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_kids_foreground));

                marker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        childrenDetailViewModel.getChildren().getLatLng(),
                        15));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChildrendetailBinding activityChildrendetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_childrendetail);
        activityChildrendetailBinding.setViewModelChildrenDetail(childrenDetailViewModel);
        activityChildrendetailBinding.executePendingBindings();

        imageViewListBack = findViewById(R.id.imgListBack);

        mQueue = Volley.newRequestQueue(getApplicationContext());
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

//        imageViewListBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChildrenDetail.this, Main.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        childrenDetailViewModel.getChildren().GetData();
        childrenDetailViewModel.setmMap(googleMap);
        mMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(childrenDetailViewModel.getChildren().getLatLng())
                .title(childrenDetailViewModel.getChildren().getName())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_kids_foreground));
        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                childrenDetailViewModel.getChildren().getLatLng(),
                15));
    }
}