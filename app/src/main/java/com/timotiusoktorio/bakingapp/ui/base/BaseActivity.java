package com.timotiusoktorio.bakingapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.timotiusoktorio.bakingapp.MyApplication;
import com.timotiusoktorio.bakingapp.di.component.ActivityComponent;
import com.timotiusoktorio.bakingapp.di.component.DaggerActivityComponent;
import com.timotiusoktorio.bakingapp.di.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = DaggerActivityComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent getComponent() {
        return component;
    }
}