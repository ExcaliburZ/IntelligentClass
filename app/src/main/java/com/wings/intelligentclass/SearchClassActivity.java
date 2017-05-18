package com.wings.intelligentclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.domain.Result;
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
    private List<Clazz> mClassList;

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
                mLoginProgress.setVisibility(View.GONE);
                mRvClasses.setVisibility(View.VISIBLE);
                mClassList = response.body();
                ClassListAdapter adapter = new ClassListAdapter();
                mRvClasses.setLayoutManager(new LinearLayoutManager(SearchClassActivity.this));
                mRvClasses.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Clazz>> call, Throwable t) {
                ToastUtils.showToast(SearchClassActivity.this, "搜索失败");
            }
        });
        mLoginProgress.setVisibility(View.VISIBLE);
        mRvClasses.setVisibility(View.GONE);
    }

    private class ClassListAdapter extends RecyclerView.Adapter<ClassHolder> {

        @Override
        public ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(SearchClassActivity.this).inflate(R.layout.item_class_manager_view, parent, false);
            return new ClassHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ClassHolder holder, int position) {
            final Clazz item = mClassList.get(position);
            holder.tvName.setText(item.getName());
            holder.tvLimit.setText("create at : " + item.getCreate_user_account());
            holder.tvTime.setText("create at : " + item.getCreateTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(SearchClassActivity.this)
                            .title("加入班级")
                            .content("你确定要加入班级 : " + item.getName() + "吗?")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    sendJoinClassRequest(item);
                                }
                            })
                            .show();
                }
            });
        }


        @Override
        public int getItemCount() {
            if (mClassList == null || mClassList.size() == 0) {
                return 0;
            }
            return mClassList.size();
        }
    }

    private void sendJoinClassRequest(Clazz item) {
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<Result> joinClass = iUserBiz.joinClass(item.getId(), GlobalPara.getInstance().account);
        joinClass.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(SearchClassActivity.this, "加入班级失败,请稍后再试");
                    return;
                }
                if (response.body().code == 302) {
                    ToastUtils.showToast(SearchClassActivity.this, response.body().message);
                    return;
                }
                ToastUtils.showToast(SearchClassActivity.this, "加入班级成功");
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                ToastUtils.showToast(SearchClassActivity.this, "加入班级失败,请稍后再试");
            }
        });
    }


    class ClassHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvLimit;
        private TextView tvTime;

        ClassHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvLimit = (TextView) itemView.findViewById(R.id.tv_limit);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

}
