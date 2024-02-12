package com.example.eventfinder.Client;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static RetrofitClient instance = null;
    private APIs apIs;

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        String url = request.url().toString();
                        Log.d("Retrofit URL", url);
                        return chain.proceed(request);
                    }
                })
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(APIs.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();

        apIs = retrofit.create(APIs.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public APIs getApIs() {
        return apIs;
    }
}
