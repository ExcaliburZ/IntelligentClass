package com.wings.intelligentclass;

import com.wings.intelligentclass.domain.Class;
import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wing on 2017/4/28.
 */

public interface IUserBiz {
    @POST("register.json")
    Call<Result> registerUser(@Body User user);

    @POST("login.json")
    Call<LoginInfo> Login(@Body User user);

    @GET("class_list.json")
    Call<List<Class>> getClasses(@Query("id") String id);
}
