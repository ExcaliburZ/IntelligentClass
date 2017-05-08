package com.wings.intelligentclass;

import com.wings.intelligentclass.domain.CheckInRequest;
import com.wings.intelligentclass.domain.CheckInResultData;
import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Call<List<Clazz>> getClassManager(@Query("id") String account);

    @GET("my_class.json")
    Call<List<Clazz>> getMyClasses(@Query("id") String account);

    @GET("start_check_in.json")
    Call<Result> startCheckIn(@Query("class_id") String classID, @Query("password") String password);

    @POST("check_in_result.json")
    Call<CheckInResultData> getCheckInResult(@Body CheckInRequest request);

    @Multipart
    @POST("uploadMulti")
    Call<Result> uploadMulti(@Part MultipartBody.Part file);

    @POST("upload")
    Call<Result> upload(@Query("class_id") String classID, @Body File file);

    @GET("student_check_in")
    Call<Result> studentCheckIn(@Query("class_id") String classID,
                                @Query("password") String password,
                                @Query("student_id") String studentID,
                                @Query("student_name") String studentName,
                                @Query("student_number") String studentNumber);
}
