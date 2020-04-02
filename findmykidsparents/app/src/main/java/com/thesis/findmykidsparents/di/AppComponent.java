package com.thesis.findmykidsparents.di;

import com.thesis.findmykidsparents.children.ChildrenActivity;
import com.thesis.findmykidsparents.login.LoginActivity;
import com.thesis.findmykidsparents.main.MainActivity;
import com.thesis.findmykidsparents.utils.TransitionSimpleLayoutFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {
    void doInjection(LoginActivity loginActivity);
    void doInjection(MainActivity mainActivity);
    void doInjection(ChildrenActivity childrenActivity);
    void doInjection(TransitionSimpleLayoutFragment transitionActivity);
}