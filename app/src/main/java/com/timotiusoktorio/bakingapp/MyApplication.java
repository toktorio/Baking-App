package com.timotiusoktorio.bakingapp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.timotiusoktorio.bakingapp.di.component.ApplicationComponent;
import com.timotiusoktorio.bakingapp.di.component.DaggerApplicationComponent;
import com.timotiusoktorio.bakingapp.di.module.ApplicationModule;

import timber.log.Timber;

public class MyApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = createComponent();
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    protected ApplicationComponent createComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    public static MyApplication get(@NonNull Context context) {
        return (MyApplication) context.getApplicationContext();
    }
}