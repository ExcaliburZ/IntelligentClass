package com.wings.intelligentclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentOperationActivity extends AppCompatActivity {

    private String mClassId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_operation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mClassId = intent.getStringExtra("class_id");
    }

    @OnClick({R.id.cv_student_check_in, R.id.cv_check_in_result, R.id.cv_download_document, R.id.cv_class_question, R.id.cv_usually_points})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_student_check_in:
                studentCheckIn();
                break;
            case R.id.cv_check_in_result:
                break;
            case R.id.cv_download_document:
                break;
            case R.id.cv_class_question:
                break;
            case R.id.cv_usually_points:
                break;
        }
    }

    private void studentCheckIn() {
        new MaterialDialog.Builder(this)
                .title("请输入老师公布的4位签到码")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .inputRangeRes(4, 4, R.color.material_red_500)
                .input("Check-in Code", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
                        final String password = input.toString();
                        StudentCheckInCall(iUserBiz, password);
                    }
                }).show();
    }

    private void StudentCheckInCall(IUserBiz iUserBiz, String password) {
        Call<Result> checkInCall = iUserBiz.studentCheckIn(mClassId, password,
                GlobalPara.getInstance().account,
                GlobalPara.getInstance().name,
                GlobalPara.getInstance().number);
        checkInCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(StudentOperationActivity.this, "签到失败");
                    return;
                }
                ToastUtils.showToast(StudentOperationActivity.this, "签到成功");
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                ToastUtils.showToast(StudentOperationActivity.this, "签到失败");
            }
        });
    }
}
