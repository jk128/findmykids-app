package com.thesis.findmykidsparents.genpassword;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thesis.findmykidsparents.utils.ApiResponse;
import com.thesis.findmykidsparents.utils.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GenPasswordViewModel extends ViewModel {
    private Repository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();

    public GenPasswordViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> genPasswordResponse() {
        return responseLiveData;
    }

    public void hitGenPasswordApi() {
        disposables.add(repository.executeGenPassword()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }
}
