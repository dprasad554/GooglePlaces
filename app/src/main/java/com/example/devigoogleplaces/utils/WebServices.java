package com.example.devigoogleplaces.utils;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServices<T> {
    public static String GY_Services = "https://maps.googleapis.com/";
    //http://gift.dsoftveda.com/WebServices/user_signup
    private static OkHttpClient.Builder builder;
    ApiType apiTypeVariable;
    Call<T> call = null;
    Context context;
    OnResponseListner<T> onResponseListner;
    android.app.ProgressDialog pdLoading;
    private T t;

    public WebServices(OnResponseListner<T> onResponseListner) {
        if (onResponseListner instanceof Activity) {
            this.context = (Context) onResponseListner;
        } else if (onResponseListner instanceof IntentService) {
            this.context = (Context) onResponseListner;
        } else if (onResponseListner instanceof android.app.DialogFragment) {
            android.app.DialogFragment dialogFragment = (android.app.DialogFragment) onResponseListner;
            this.context = dialogFragment.getActivity();
        } else {
            Fragment fragment = (Fragment) onResponseListner;
            this.context = fragment.getActivity();
        }

        this.onResponseListner = onResponseListner;
        builder = getHttpClient();
    }

    public WebServices(Context context, OnResponseListner<T> onResponseListner) {
        this.onResponseListner = onResponseListner;
        this.context = context;
        builder = getHttpClient();
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }


    public OkHttpClient.Builder getHttpClient() {
        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.readTimeout(60, TimeUnit.SECONDS);
            client.connectTimeout(60, TimeUnit.SECONDS);
            client.addInterceptor(loggingInterceptor);
            return client;
        }
        return builder;
    }
    //SignUp
    public void GoogleResponce(String api, final ApiType apiTypes,String location,int radius,String type,String sensor,String key) {
        ProgressDialog progressDialog = new ProgressDialog();
        try {
            this.pdLoading = progressDialog.getInstance(this.context);
            this.pdLoading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            if (this.pdLoading.isShowing()) {
                this.pdLoading.cancel();
            }
            this.pdLoading.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.apiTypeVariable = apiTypes;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(api).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        GitApi gi = retrofit.create(GitApi.class);
        call = (Call<T>) gi.googleresponce(location,radius,type,sensor,key);
        call.enqueue(new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                t = (T) response.body();
                if (pdLoading.isShowing())
                    pdLoading.cancel();
                onResponseListner.onResponse(t, apiTypeVariable, true);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.e(apiTypes.toString(), t.getMessage() + ".");
                if (pdLoading.isShowing())
                    pdLoading.cancel();
                onResponseListner.onResponse(null, apiTypeVariable, false);
            }

        });

    }


    public enum ApiType {
        googleresponce,

    }

}
