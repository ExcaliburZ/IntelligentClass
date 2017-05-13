package com.wings.intelligentclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClassActivity extends AppCompatActivity {

    @BindView(R.id.name)
    AutoCompleteTextView mName;
    @BindView(R.id.limit_num)
    AutoCompleteTextView mLimitNum;
    @BindView(R.id.description)
    MultiAutoCompleteTextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);
        setTitle("Create New Clazz");
    }

    @OnClick(R.id.email_sign_in_button)
    public void onViewClicked() {
        Clazz clazz = new Clazz(mName.getText().toString(),
                Integer.parseInt(mLimitNum.getText().toString()),
                GlobalPara.getInstance().account);
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<Result> addClassCall = iUserBiz.addClass(clazz);
        addClassCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() == null ||
                        response.code() / 100 != 2 ||
                        response.body().code / 100 != 2) {
                    ToastUtils.showToast(AddClassActivity.this, "创建失败");
                    return;
                }
                ToastUtils.showToast(AddClassActivity.this, "创建成功");
                AddClassActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}
