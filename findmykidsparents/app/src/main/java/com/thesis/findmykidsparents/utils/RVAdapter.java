package com.thesis.findmykidsparents.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.thesis.findmykidsparents.R;
import com.thesis.findmykidsparents.entity.Children;

import java.util.ArrayList;

class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super(context);
    }
}

public class RVAdapter extends RecyclerView.Adapter {
    ArrayList<Children> childrens = new ArrayList<>();
    Context context;

    ChildrenListener childrenListener;

    public RVAdapter(ArrayList<Children> childrens, Context context, ChildrenListener childrenListener) {
        super();
        this.childrens = childrens;
        this.context = context;
        this.childrenListener = childrenListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myHolder = (MyViewHolder) holder;
        if (myHolder == null) {
            return;
        }
        myHolder.bindView(position);
    }

    public void addItem(Children children) {
        childrens.add(childrens.size(), children);
        notifyItemInserted(childrens.size());
    }

    @Override
    public int getItemCount() {
        return childrens.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.children_row_activity, parent, false);
        return new MyViewHolder(container);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public TextView childrenName;
        CustomMap mapView;
        GoogleMap map;
        View layout;
        MaterialButton btnViewDetail;

        public MyViewHolder(View v) {
            super(v);
            layout = v;
            mapView = layout.findViewById(R.id.lite_listrow_map);
            childrenName = layout.findViewById(R.id.childrenName);
            btnViewDetail = layout.findViewById(R.id.btnViewDetail);

            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
            mapView.setClickable(false);

            btnViewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataStore dataStore = new DataStore(v.getContext());
                    Children children = (Children) btnViewDetail.getTag();
                    Gson gson = new Gson();
                    String json = gson.toJson(children);
                    dataStore.StoreData("location_selected", json);
                    childrenListener.ChooseChildren();
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " \"" + childrenName.getText() + "\"";
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            map = googleMap;
            setMapLocation();
        }

        private void setMapLocation() {
            if (map == null) return;

            Children data = (Children) btnViewDetail.getTag();
            if (data == null) return;

            // Add a marker for this item and set the camera
            LatLng loc = new LatLng(data.lat, data.lon);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 18f));
            map.addMarker(new MarkerOptions().position(loc).icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_point_foreground)));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        private void bindView(int pos) {
            Children item = childrens.get(pos);
            // Store a reference of the ViewHolder object in the layout.
            layout.setTag(this);
            // Store a reference to the item in the mapView's tag. We use it to get the
            // coordinate of a location, when setting the map location.
            btnViewDetail.setTag(item);
            childrenName.setText(item.name);
            childrenName.setTag(item.id);
        }
    }
}