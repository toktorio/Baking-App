package com.timotiusoktorio.bakingapp.data.source.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timotiusoktorio.bakingapp.R;
import com.timotiusoktorio.bakingapp.data.model.Recipe;
import com.timotiusoktorio.bakingapp.data.source.DataManager;
import com.timotiusoktorio.bakingapp.di.qualifier.ApiInfo;
import com.timotiusoktorio.bakingapp.di.qualifier.ApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

@Singleton
public class RecipeApiHelper implements ApiHelper {

    private final Context context;
    private final OkHttpClient okHttpClient;
    private final String baseUrl;

    @Inject
    public RecipeApiHelper(@ApplicationContext Context context, OkHttpClient okHttpClient, @ApiInfo String baseUrl) {
        this.context = context;
        this.okHttpClient = okHttpClient;
        this.baseUrl = baseUrl;
    }

    @Override
    public void fetchRecipes(final DataManager.FetchRecipesCallback callback) {
        Request request = new Request.Builder().url(baseUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Timber.e(e, "fetchRecipes: onFailure");
                callback.onFailure(context.getString(R.string.message_get_recipes_failed_api_error));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Timber.d("fetchRecipes: Response = %s", response);
                ResponseBody responseBody = response.body();
                if (response.isSuccessful() && responseBody != null) {
                    Timber.i("fetchRecipes: onResponse: success");
                    Gson gson = new GsonBuilder().create();
                    Recipe[] recipes = gson.fromJson(responseBody.charStream(), Recipe[].class);
                    callback.onSuccess(Arrays.asList(recipes));
                } else {
                    Timber.e("fetchRecipes: onResponse: failed");
                    callback.onFailure(context.getString(R.string.message_get_recipes_failed_api_error));
                }
            }
        });
    }
}