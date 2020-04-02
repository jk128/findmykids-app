package com.thesis.findmykidschildren.Parent;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.thesis.findmykidschildren.entity.MyParents;
import com.thesis.findmykidschildren.utils.ApiResponse;
import com.thesis.findmykidschildren.utils.DataStore;
import com.thesis.findmykidschildren.utils.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class ParentViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private Repository repository;

    private Context context;
    public void setContext(Context context) {
        this.context = context;
    }

    public ParentViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> parentResponse() {
        return responseLiveData;
    }

    public MyParents GetMyParents() {
        Gson gson = new Gson();
        DataStore dataStore = new DataStore(context);
        MyParents myParents;
        String my_parents = dataStore.GetData("my_parents");
        if (my_parents == null) {
            myParents = new MyParents();
        } else {
            myParents = gson.fromJson(my_parents, MyParents.class);
        }
        return myParents;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
