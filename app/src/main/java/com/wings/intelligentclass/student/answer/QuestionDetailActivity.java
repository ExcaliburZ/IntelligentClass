package com.wings.intelligentclass.student.answer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wings.intelligentclass.R;
import com.wings.intelligentclass.api.IUserBiz;
import com.wings.intelligentclass.domain.Answer;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.utils.GlobalPara;
import com.wings.intelligentclass.utils.RetrofitManager;
import com.wings.intelligentclass.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    private String mQueId;
    private String mClassId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mTvTitle.setText(intent.getStringExtra("title"));
        mTvContent.setText(intent.getStringExtra("content"));
        mQueId = intent.getStringExtra("id");
        mClassId = intent.getStringExtra("class_id");
    }

    @OnClick(R.id.bt_answer)
    public void onViewClicked() {
        MaterialDialog titleDialog = new MaterialDialog.Builder(this)
                .title("请输入答案")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("不少于5个字符", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .inputRange(5, 400, Color.RED)
                .negativeText("取消")
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String answerStr = dialog.getInputEditText().getText().toString();
                        Answer answer = new Answer();
                        answer.setAnswer(answerStr);
                        answer.setAnswer_time(String.valueOf(System.currentTimeMillis()));
                        answer.setClazz_id(mClassId);
                        answer.setQuestion_id(mQueId);
                        answer.setUser_account(GlobalPara.getInstance().account);
                        sendAnswerCall(answer);
                    }
                })
                .show();
    }

    private void sendAnswerCall(Answer answer) {
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<Result> addAnswerCall = iUserBiz.addAnswer(answer);
        addAnswerCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(QuestionDetailActivity.this, "回答问题失败");
                    return;
                }
                if (response.body().code == 302) {
                    ToastUtils.showToast(QuestionDetailActivity.this, response.body().message);
                    return;
                }
                ToastUtils.showToast(QuestionDetailActivity.this, "回答问题成功");
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                ToastUtils.showToast(QuestionDetailActivity.this, "回答问题失败");
            }
        });
    }
}
