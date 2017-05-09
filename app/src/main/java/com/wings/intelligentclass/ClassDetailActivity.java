package com.wings.intelligentclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.domain.User;
import com.wings.intelligentclass.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassDetailActivity extends AppCompatActivity {

    @BindView(R.id.cv_start_check_in)
    CardView mCvStartCheckIn;
    @BindView(R.id.cv_upload_document)
    CardView mCvUploadDocument;
    @BindView(R.id.cv_class_question)
    CardView mCvClassQuestion;
    @BindView(R.id.cv_student_list)
    CardView mCvStudentList;
    @BindView(R.id.cv_usually_points)
    CardView mCvUsuallyPoints;
    @BindView(R.id.cv_check_in_result)
    CardView mCvCheckInResult;
    private Clazz mClazz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);
        ButterKnife.bind(this);
        Object aClass = getIntent().getExtras().get("class");
        if (!(aClass instanceof Clazz)) {
            return;
        }
        mClazz = (Clazz) aClass;
        setTitle(mClazz.getName());
    }

    @OnClick({R.id.cv_start_check_in,
            R.id.cv_upload_document,
            R.id.cv_class_question,
            R.id.cv_student_list,
            R.id.cv_usually_points,
            R.id.cv_check_in_result,
            R.id.cv_doc_manager})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_start_check_in:
                startCheckIn();
                break;
            case R.id.cv_upload_document:
                enterUploadDocActivity();
                break;
            case R.id.cv_class_question:
                break;
            case R.id.cv_student_list:
                enterStudentsListActivity();
                break;
            case R.id.cv_usually_points:
                break;
            case R.id.cv_check_in_result:
                enterCheckInResultActivity();
                break;
            case R.id.cv_doc_manager:
                enterDocManager();
                break;
        }
    }

    private void enterDocManager() {
        Intent intent = new Intent(this, DocManagerActivity.class);
        intent.putExtra("class_id", mClazz.getId());
        startActivity(intent);
    }

    private void enterUploadDocActivity() {
        Intent intent = new Intent(this, UploadDocActivity.class);
        intent.putExtra("class_id", mClazz.getId());
        startActivity(intent);
    }

    private void enterCheckInResultActivity() {
        Intent intent = new Intent(this, CheckInResultActivity.class);
        intent.putExtra("class_id", mClazz.getId());
        ArrayList<String> accountList = new ArrayList<>();
        for (User user : mClazz.getStudentList()) {
            accountList.add(user.getAccount());
        }
        intent.putStringArrayListExtra("account_list", accountList);
        startActivity(intent);
    }

    private void enterStudentsListActivity() {
        Intent intent = new Intent(this, StudentsListActivity.class);
        intent.putExtra("studentsList", (Serializable) mClazz.getStudentList());
        startActivity(intent);
    }

    //开始签到,弹出对话框让老师输入签到码
    private void startCheckIn() {
        new MaterialDialog.Builder(this)
                .title("请输入签到码")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .inputRangeRes(4, 4, R.color.material_red_500)
                .input("Check-in Code", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
                        final String password = input.toString();
                        sendCheckInCall(iUserBiz, password);
                    }
                }).show();
    }

    private void sendCheckInCall(IUserBiz iUserBiz, final String password) {
        Call<Result> checkInCall = iUserBiz.startCheckIn(mClazz.getId(), password);
        checkInCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(ClassDetailActivity.this, "开始签到失败,请稍后再试");
                    return;
                }
                ToastUtils.showToast(ClassDetailActivity.this, "开始签到成功,签到码为 : " + password);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                ToastUtils.showToast(ClassDetailActivity.this, "开始签到失败,请稍后再试");
            }
        });
    }
}
