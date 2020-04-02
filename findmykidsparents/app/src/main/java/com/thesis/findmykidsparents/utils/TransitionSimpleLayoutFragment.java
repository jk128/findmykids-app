package com.thesis.findmykidsparents.utils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thesis.findmykidsparents.MyApplication;
import com.thesis.findmykidsparents.R;
import com.thesis.findmykidsparents.children.ChildrenViewModel;
import com.thesis.findmykidsparents.detail.DetailViewModel;
import com.thesis.findmykidsparents.entity.Children;
import com.thesis.findmykidsparents.entity.MemberLocation;
import com.thesis.findmykidsparents.genpassword.GenPasswordViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class TransitionSimpleLayoutFragment extends Fragment implements OnMapReadyCallback {
    private static final String KEY_LAYOUT_RES_ID = "KEY_LAYOUT_RES_ID";
    @Inject
    ViewModelFactory viewModelFactory;
    ChildrenViewModel childrenViewModel;
    DetailViewModel detailViewModel;
    GenPasswordViewModel genPasswordViewModel;
    ProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    Marker marker;
    int layoutResId;
    GoogleMap mMap;
    Children children;
    RVAdapter rvAdapter;

    ChildrenListener childrenListener;

    TextInputEditText textInputEditText;

    Boolean firstOpenChildrenDetail = true;

    public static TransitionSimpleLayoutFragment newInstance(@LayoutRes int layoutResId) {
        TransitionSimpleLayoutFragment fragment = new TransitionSimpleLayoutFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT_RES_ID, layoutResId);
        fragment.setArguments(args);
        return fragment;
    }

    public void RegisterChildrenListener(ChildrenListener childrenListener) {
        this.childrenListener = childrenListener;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Bundle args = getArguments();
        if (args != null) {
            layoutResId = args.getInt(KEY_LAYOUT_RES_ID);
        }

        progressDialog = Constant.getProgressDialog(getContext(), getString(R.string.process));
        ButterKnife.bind(getActivity());
        ((MyApplication) getActivity().getApplication()).getAppComponent().doInjection(this);

        switch (layoutResId) {
            case R.layout.children_activity:
                childrenViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChildrenViewModel.class);
                childrenViewModel.childrenResponse().observe(this, this::consumeResponse);
                childrenViewModel.setContext(getContext());
                break;
            case R.layout.detail_activity:
                detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
                detailViewModel.detailResponse().observe(this, this::consumeResponse);
                break;
            case R.layout.password_activity:
                genPasswordViewModel = ViewModelProviders.of(this, viewModelFactory).get(GenPasswordViewModel.class);
                genPasswordViewModel.genPasswordResponse().observe(this, this::consumeResponse);
                break;
        }
    }

    /**
     * Nhận thông báo trả về khi gọi API
     *
     * @param apiResponse
     */
    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                if (layoutResId != R.layout.detail_activity)
                    progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                renderSuccessResponse(apiResponse.data);
                break;

            case ERROR:
                if (layoutResId != R.layout.detail_activity)
                    progressDialog.dismiss();
                //Toast.makeText(ChildrenActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    /**
     * Nhận kết quả trả về của API
     *
     * @param response
     * @param <T>
     */
    private <T> void renderSuccessResponse(T response) {
        DataStore dataStore = new DataStore(getContext());
        Gson gson = new Gson();
        switch (layoutResId) {
            case R.layout.children_activity:
                // Nếu có trẻ mới thì add
                ArrayList<Children> listChild = (ArrayList<Children>) response;
                // Add vào bộ nhớ shared
                String newChild = dataStore.GetData(getString(R.string.newChild));
                if (newChild != null) {
                    String childList = dataStore.GetData(getString(R.string.childList));
                    ArrayList<Children> childArrayList = new ArrayList<>();
                    if (childList != null) {
                        childArrayList = gson.fromJson(childList, new TypeToken<ArrayList<Children>>() {
                        }.getType());
                    }
                    childArrayList.addAll(listChild);
                    dataStore.StoreData(getString(R.string.childList), gson.toJson(childArrayList, new TypeToken<ArrayList<Children>>() {
                    }.getType()));
                    dataStore.StoreData(getString(R.string.newChild), null);

                    rvAdapter = new RVAdapter(childArrayList, getContext(), childrenListener);
                    mRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(rvAdapter);
                }
                break;
            case R.layout.detail_activity:
                children.lat = ((MemberLocation) response).location.latitude;
                children.lon = ((MemberLocation) response).location.longitude;
                detailViewModel.getMemberLocation(1000, (MemberLocation) response, children);

                if (firstOpenChildrenDetail) {
                    onMyLocationClicked();
                    firstOpenChildrenDetail = false;
                }
                break;
            case R.layout.password_activity:
                if (response instanceof String) {
                    textInputEditText.setText(response.toString());
                    dataStore = new DataStore(getContext());
                    dataStore.StoreData(getString(R.string.newChild), getString(R.string.newChild));
                }
                break;
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View view = layoutInflater.inflate(layoutResId, viewGroup, false);
        switch (layoutResId) {
            case R.id.children_activity:
                break;
            case R.layout.detail_activity:
                ImageView btn_my_location = view.findViewById(R.id.btn_my_location);
                btn_my_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onMyLocationClicked();
                    }
                });
                SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapDetail);
                mMapFragment.getMapAsync(this);
                break;
            case R.layout.password_activity:
                textInputEditText = view.findViewById(R.id.passwordgen);
                Button btn_passWordGen = view.findViewById(R.id.btn_passWordGen);
                btn_passWordGen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genPasswordViewModel.hitGenPasswordApi();
                    }
                });
                break;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataStore dataStore = new DataStore(getContext());
        Gson gson = new Gson();

        switch (layoutResId) {
            case R.layout.children_activity:
                mRecyclerView = view.findViewById(R.id.recyclerView);
                // Load danh sách trẻ từ bộ nhớ
                // LoadChildrenRunnable runnable = new LoadChildrenRunnable(view, this.childrenListener);
                // new Thread(runnable).start();

                String newChild = dataStore.GetData(getString(R.string.newChild));

                String childList = dataStore.GetData(getString(R.string.childList));
                int count = 0;

                if (childList != null) {
                    ArrayList<Children> childArrayList = gson.fromJson(childList, new TypeToken<ArrayList<Children>>() {
                    }.getType());
                    count = childArrayList.size();

                    rvAdapter = new RVAdapter(childArrayList, getContext(), childrenListener);
                } else {
                    rvAdapter = new RVAdapter(new ArrayList<Children>(), getContext(), childrenListener);
                }

                if (newChild != null) {
                    childrenViewModel.getNewChildren(count);
                } else {
                    mRecyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(rvAdapter);
                }
                break;
            case R.layout.detail_activity:
                String json = dataStore.GetData("location_selected");
                if (json != null) {
                    children = gson.fromJson(json, Children.class);
                }
                break;
        }
    }

    /**
     * Sự kiện quay về vị trí ban đầu của màn hình Chi tiết vị trí
     */
    void onMyLocationClicked() {
        switch (layoutResId) {
            case R.layout.detail_activity:
                if (mMap == null) return;
                if (children != null) {
                    LatLng loc = new LatLng(children.lat, children.lon);
                    if (marker != null) marker.remove();
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(loc)
                            .title(children.name)
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point_foreground));
                    markerOptions.anchor(0.5f, 0.5f);
                    marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            loc,
                            15));
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        switch (layoutResId) {
            case R.layout.detail_activity:
                detailViewModel.setMarker(marker);
                detailViewModel.setmMap(mMap);

                if (children != null) {
                    detailViewModel.hitDetailApi(children.id);
                }
                break;
        }
    }
}