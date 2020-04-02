package com.thesis.findmykidschildren.di;

import com.thesis.findmykidschildren.main.MainActivity;
import com.thesis.findmykidschildren.utils.TransitionSimpleLayoutFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {
    void doInjection(MainActivity mainActivity);

    void doInjection(TransitionSimpleLayoutFragment transitionActivity);
}