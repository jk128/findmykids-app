package com.thesis.findmykidsparents.utils;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.thesis.findmykidsparents.children.ChildrenViewModel;
import com.thesis.findmykidsparents.detail.DetailViewModel;
import com.thesis.findmykidsparents.genpassword.GenPasswordViewModel;
import com.thesis.findmykidsparents.login.LoginViewModel;
import com.thesis.findmykidsparents.main.MainViewModel;

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
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(repository);
        }
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ChildrenViewModel.class)) {
            return (T) new ChildrenViewModel(repository);
        }
        if (modelClass.isAssignableFrom(DetailViewModel.class)) {
            return (T) new DetailViewModel(repository);
        }
        if (modelClass.isAssignableFrom(GenPasswordViewModel.class)) {
            return (T) new GenPasswordViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}