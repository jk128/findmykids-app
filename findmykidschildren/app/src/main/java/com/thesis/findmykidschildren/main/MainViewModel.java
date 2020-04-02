package com.thesis.findmykidschildren.main;

import android.content.Context;
import android.location.Location;
import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.thesis.findmykidschildren.R;
import com.thesis.findmykidschildren.entity.LocationReport;
import com.thesis.findmykidschildren.entity.MyLocation;
import com.thesis.findmykidschildren.utils.ApiResponse;
import com.thesis.findmykidschildren.utils.DataStore;
import com.thesis.findmykidschildren.utils.Repository;
import com.thesis.findmykidschildren.utils.SessionManager;

import java.util.HashMap;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    SessionManager session;
    Context context;
    private Repository repository;

    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    public void setContext(Context context) {
        this.context = context;
        session = new SessionManager(context);
    }

    public MutableLiveData<ApiResponse> reportResponse() {
        return responseLiveData;
    }

    public void hitReportLocationApi() {
        if (!session.isLoggedIn()) {
            responseLiveData.setValue(ApiResponse.success(null));
            return;
        }
        // Lưu vị trí đầu tiên
        DataStore dataStore = new DataStore(context);
        Gson gson = new Gson();
        String strLocation = dataStore.GetData(context.getResources().getString(R.string.myLocationInit));
        if (strLocation == null) {
            responseLiveData.setValue(ApiResponse.success(null));
            return;
        };
        MyLocation location = gson.fromJson(strLocation, MyLocation.class);
        HashMap<String, String> childrenData = session.getUserDetails();
        String memberId = childrenData.get(SessionManager.KEY_ID);
        LocationReport locationReport = new LocationReport();
        locationReport.MemberID = memberId;
        locationReport.ReportID = UUID.randomUUID().toString();
        locationReport.Latitude = location.latitude;
        locationReport.Longitude = location.longitude;
        locationReport.Origin = "Origin";
        disposables.add(repository.executeReportLocation(memberId, locationReport)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error((Throwable) throwable))
                ));
    }

    public void reportLocationRealTime() {
        new CountDownTimer(5000, 100) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                try {
                    hitReportLocationApi();
                } catch (Exception e) {
                }
            }
        }.start();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
