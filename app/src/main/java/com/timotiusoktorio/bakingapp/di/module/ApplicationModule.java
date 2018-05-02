package com.timotiusoktorio.bakingapp.di.module;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.timotiusoktorio.bakingapp.BuildConfig;
import com.timotiusoktorio.bakingapp.MyApplication;
import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.data.source.RecipeDataManager;
import com.timotiusoktorio.bakingapp.data.source.db.AppDatabase;
import com.timotiusoktorio.bakingapp.data.source.db.DbHelper;
import com.timotiusoktorio.bakingapp.data.source.db.RecipeDbHelper;
import com.timotiusoktorio.bakingapp.data.source.network.ApiHelper;
import com.timotiusoktorio.bakingapp.data.source.network.RecipeApiHelper;
import com.timotiusoktorio.bakingapp.data.source.preferences.PreferencesHelper;
import com.timotiusoktorio.bakingapp.data.source.preferences.RecipePreferencesHelper;
import com.timotiusoktorio.bakingapp.di.qualifier.ApiInfo;
import com.timotiusoktorio.bakingapp.di.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

@Module
public class ApplicationModule {

    private final MyApplication application;

    public ApplicationModule(MyApplication application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @SuppressLint("LogNotTimber")
            @Override
            public void log(@NonNull String message) {
                Log.v("OkHttp", message);
            }
        });
        interceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return interceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @ApiInfo
    public String provideBaseUrl() {
        return "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    }

    @Provides
    @Singleton
    public ApiHelper provideApiHelper(RecipeApiHelper recipeApiHelper) {
        return recipeApiHelper;
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    public DbHelper provideDbHelper(RecipeDbHelper recipeDbHelper) {
        return recipeDbHelper;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public PreferencesHelper providePreferencesHelper(RecipePreferencesHelper recipePreferencesHelper) {
        return recipePreferencesHelper;
    }

    @Provides
    @Singleton
    public DataManager provideDataManager(RecipeDataManager recipeDataManager) {
        return recipeDataManager;
    }
}