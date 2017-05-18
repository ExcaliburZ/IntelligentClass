package com.wings.intelligentclass;

import com.wings.intelligentclass.domain.Answer;
import com.wings.intelligentclass.domain.CheckInRequest;
import com.wings.intelligentclass.domain.CheckInResultData;
import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.domain.LoginInfo;
import com.wings.intelligentclass.domain.MyCheckInData;
import com.wings.intelligentclass.domain.Question;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.UploadDocument;
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
    @POST("register")
    Call<Result> registerUser(@Body User user);

    @POST("login")
    Call<LoginInfo> Login(@Body User user);

    @GET("get_class_list")
    Call<List<Clazz>> getClassManager(@Query("account") String account);

    @POST("add_class")
    Call<Result> addClass(@Body Clazz clazz);

    @GET("doc_list")
    Call<List<UploadDocument>> getDocList(@Query("clazz_id") String classID);

    @GET("remove_doc")
    Call<Result> deleteDoc(@Query("id") String id);

    @GET("my_class")
    Call<List<Clazz>> getMyClasses(@Query("account") String account);

    @GET("search_class")
    Call<List<Clazz>> searchClass(@Query("key") String key);

    @GET("start_check_in")
    Call<Result> startCheckIn(@Query("clazz_id") String classID, @Query("password") String password);

    @GET("join_class")
    Call<Result> joinClass(@Query("clazz_id") String classID, @Query("account") String account);

    @POST("check_in_result")
    Call<CheckInResultData> getCheckInResult(@Body CheckInRequest request);

    @POST("add_question")
    Call<Result> addQuestion(@Body Question question);

    @POST("add_answer")
    Call<Result> addAnswer(@Body Answer answer);

    @GET("get_question_list")
    Call<List<Question>> getQuestionList(@Query("clazz_id") String classID);

    @GET("get_my_check_in")
    Call<MyCheckInData> getMyCheckIn(@Query("clazz_id") String classID, @Query("account") String account);

    @Multipart
    @POST("uploadMulti")
    Call<Result> uploadMulti(@Part MultipartBody.Part file);

    @POST("upload")
    Call<Result> upload(@Query("clazz_id") String classID, @Body File file);

    @Multipart
    @POST("upload")
    Call<Result> uploadDoc(@Part MultipartBody.Part file,
                           @Query("clazz_id") String classID,
                           @Query("size") String size);

    @GET("student_check_in")
    Call<Result> studentCheckIn(@Query("clazz_id") String classID,
                                @Query("password") String password,
                                @Query("student_id") String studentID,
                                @Query("student_name") String studentName,
                                @Query("student_number") String studentNumber);

    @GET("get_answer_list")
    Call<List<Answer>> getAnswerList(@Query("question_id") String questionId);
}
