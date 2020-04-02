package com.thesis.findmykidschildren.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.gson.Gson;
import com.thesis.findmykidschildren.MyApplication;
import com.thesis.findmykidschildren.R;
import com.thesis.findmykidschildren.entity.Auth;
import com.thesis.findmykidschildren.entity.MyLocation;
import com.thesis.findmykidschildren.utils.ApiResponse;
import com.thesis.findmykidschildren.utils.Constant;
import com.thesis.findmykidschildren.utils.DataStore;
import com.thesis.findmykidschildren.utils.SessionManager;
import com.thesis.findmykidschildren.utils.TransitionSimpleLayoutFragment;
import com.thesis.findmykidschildren.utils.ViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final String TAG = "Report";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 5000;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private static final SparseIntArray LAYOUT_RES_MAP = new SparseIntArray();

    static {
        LAYOUT_RES_MAP.append(R.id.action_parent, R.layout.parent_activity);
        LAYOUT_RES_MAP.append(R.id.action_connect, R.layout.connect_activity);
    }

    @Inject
    ViewModelFactory viewModelFactory;
    MainViewModel viewModel;
    ProgressDialog progressDialog;
    @BindView(R.id.bottomnavigation)
    BottomNavigationView bottomNavigationView;
    // Session Manager Class
    SessionManager session;
    private Location location;
    private TextView locationTv;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    @LayoutRes
    private static int getLayoutForItemId(@IdRes int itemId) {
        return LAYOUT_RES_MAP.get(itemId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        progressDialog = Constant.getProgressDialog(this, getString(R.string.process));

        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
        viewModel.reportResponse().observe(this, this::consumeResponse);
        viewModel.setContext(getApplicationContext());

        replaceFragment(R.id.action_parent);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        progressDialog.show();
                        final int previousItem = bottomNavigationView.getSelectedItemId();
                        final int nextItem = item.getItemId();
                        if (previousItem != nextItem) {
                            replaceFragment(item.getItemId());
                        }
                        progressDialog.dismiss();
                        return true;
                    }
                }
        );

        // #02
        session.checkLogin();

        locationTv = findViewById(R.id.tvLocation);
        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        viewModel.reportLocationRealTime();
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private void replaceFragment(@IdRes int itemId) {
        Fragment fragment = TransitionSimpleLayoutFragment.newInstance(getLayoutForItemId(itemId));
        fragment.setEnterTransition(createTransition());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private MaterialFadeThrough createTransition() {
        MaterialFadeThrough fadeThrough = MaterialFadeThrough.create(this);
        fadeThrough.addTarget(R.id.parent_activity);
        fadeThrough.addTarget(R.id.connect_activity);
        return fadeThrough;
    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                viewModel.reportLocationRealTime();
                // renderSuccessResponse((Auth) apiResponse.data);
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(Auth response) {
//        if (!response.isJsonNull()) {
//            Log.d("response=", response.toString());
//        } else {
//            Toast.makeText(LoginActivity.this, getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkPlayServices()) {
            locationTv.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop location updates
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@androidx.annotation.Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            locationTv.setText("onConnected Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@androidx.annotation.NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        MyLocation myLocation = new MyLocation();
        myLocation.latitude = location.getLatitude();
        myLocation.longitude = location.getLongitude();
        // Lưu vị trí đầu tiên
        DataStore dataStore = new DataStore(getApplicationContext());
        Gson gson = new Gson();
        dataStore.StoreData(getString(R.string.myLocationInit), gson.toJson(myLocation, MyLocation.class));
        locationTv.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(MainActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }
                break;
        }
    }
}