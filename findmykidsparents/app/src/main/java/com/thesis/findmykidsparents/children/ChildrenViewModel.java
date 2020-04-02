package com.thesis.findmykidsparents.children;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thesis.findmykidsparents.utils.ApiResponse;
import com.thesis.findmykidsparents.utils.Repository;
import com.thesis.findmykidsparents.utils.SessionManager;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChildrenViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    SessionManager session;
    Context context;
    private Repository repository;

    public ChildrenViewModel(Repository repository) {
        this.repository = repository;
    }

    public void setContext(Context context) {
        this.context = context;
        session = new SessionManager(context);
    }

    public MutableLiveData<ApiResponse> childrenResponse() {
        return responseLiveData;
    }

    public void hitGetChildren(int count) {
        HashMap<String, String> user = session.getUserDetails();
        disposables.add(repository.executeGetChildren(user.get(SessionManager.KEY_ID), count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }

    public void getNewChildren(int count) {
        new CountDownTimer(5 * 1000 * 60, 5000) {
            public void onTick(long millisUntilFinished) {
                try {
                    if (session.isLoggedIn()) {
                        hitGetChildren(count);
                    }
                } catch (Exception e) {
                }
            }

            public void onFinish() {
            }
        }.start();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
