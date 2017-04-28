package com.wings.intelligentclass;

import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by wing on 2017/4/28.
 */

public interface IUserBiz {
    @POST("register.json")
    Call<Result> registerUser(@Body User user);

    @POST("login.json")
    Call<LoginInfo> Login(@Body User user);
}
