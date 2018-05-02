package com.timotiusoktorio.bakingapp.di.component;

import android.app.Application;
import android.content.Context;

import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.di.module.ApplicationModule;
import com.timotiusoktorio.bakingapp.di.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext Context getContext();

    Application getApplication();

    DataManager getDataManager();
}