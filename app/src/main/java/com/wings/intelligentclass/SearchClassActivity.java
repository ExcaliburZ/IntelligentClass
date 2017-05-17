package com.wings.intelligentclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchClassActivity extends AppCompatActivity {

    @BindView(R.id.login_progress)
    ProgressBar mLoginProgress;
    @BindView(R.id.key)
    AutoCompleteTextView mKey;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.rv_classes)
    RecyclerView mRvClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_class);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        String key = mKey.getText().toString();
        if (TextUtils.isEmpty(key)) {
            mKey.setError("搜索关键词不能为空");
            return;
        }
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<List<Clazz>> searchClass = iUserBiz.searchClass(key);
        searchClass.enqueue(new Callback<List<Clazz>>() {
            @Override
            public void onResponse(Call<List<Clazz>> call, Response<List<Clazz>> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(SearchClassActivity.this, "搜索失败");
                    return;
                }
                
            }

            @Override
            public void onFailure(Call<List<Clazz>> call, Throwable t) {
                ToastUtils.showToast(SearchClassActivity.this, "搜索失败");
            }
        });
        mLoginProgress.setVisibility(View.VISIBLE);
        mRvClasses.setVisibility(View.GONE);
    }


}
