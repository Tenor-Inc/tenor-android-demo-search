package com.tenor.android.demo.search.utils;

import android.app.Application;

import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.network.ApiService;
import com.tenor.android.core.network.IApiClient;

public class DemoApp extends Application {

    private static final String DEMO_KEY = "LIVDSRZULELA";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Tenor Core API
        ApiService.IBuilder<IApiClient> builder = new ApiService.Builder<>(this, IApiClient.class);
        builder.apiKey(DEMO_KEY);

        ApiClient.init(this, builder);
    }
}
