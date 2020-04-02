package com.thesis.findmykidschildren.utils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.thesis.findmykidschildren.Parent.ParentViewModel;
import com.thesis.findmykidschildren.connect.ConnectViewModel;
import com.thesis.findmykidschildren.main.MainViewModel;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ConnectViewModel.class)) {
            return (T) new ConnectViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ParentViewModel.class)) {
            return (T) new ParentViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}