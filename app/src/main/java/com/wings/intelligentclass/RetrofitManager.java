package com.wings.intelligentclass;

import android.util.Log;

import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wing on 2017/4/28.
 */

public class RetrofitManager {
    //private final static String BASE_URL = "http://182.254.156.215:8080/";
    //private final static String BASE_URL = "http://10.0.0.2:8088/kcsj/";
    public final static String BASE_URL = "http://192.168.1.111:8088/kcsj/";
    private final static String TAG = "RetrofitManager";
    private static RetrofitManager instance = new RetrofitManager();
    private final Retrofit mRetrofit;
    private final IUserBiz mIUserBiz;

    public static RetrofitManager getInstance() {
        return instance;
    }

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mIUserBiz = mRetrofit.create(IUserBiz.class);
    }

    public IUserBiz getIUserBiz() {
        return mIUserBiz;
    }

    public Result RegisterUser(User user) {
        Call<Result> registerUserCall = mIUserBiz.registerUser(user);
        try {
            return registerUserCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "getAsync: error");
        }
        return null;
    }

    public LoginInfo Login(User user) {
        Call<LoginInfo> loginInfoCall = mIUserBiz.Login(user);
        try {
            return loginInfoCall.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "getAsync: error");
        }
        return null;
    }

}
