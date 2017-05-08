package com.wings.intelligentclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wings.intelligentclass.domain.Clazz;
import com.wings.intelligentclass.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClassActivity extends AppCompatActivity {
    @BindView(R.id.rv_classes)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;
    private List<Clazz> mClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        ButterKnife.bind(this);

        mSrlRefresh.setColorSchemeColors(Color.BLUE);
        mSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override //刷新时回调方法
            public void onRefresh() {
                fillClassesData();
            }
        });
        fillClassesData();
    }

    public void fillClassesData() {
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<List<Clazz>> resultCall = iUserBiz.getMyClasses(GlobalPara.getInstance().account);
        resultCall.enqueue(new Callback<List<Clazz>>() {
            @Override
            public void onResponse(Call<List<Clazz>> call, Response<List<Clazz>> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(MyClassActivity.this, "load classes failed");
                    return;
                }
                mSrlRefresh.setRefreshing(false);
                mClassList = response.body();
                ClassListAdapter adapter = new ClassListAdapter();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyClassActivity.this));
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Clazz>> call, Throwable t) {
                ToastUtils.showToast(MyClassActivity.this, "load classes failed");
            }
        });
    }


    private class ClassListAdapter extends RecyclerView.Adapter<ClassHolder> {

        @Override
        public ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(MyClassActivity.this).inflate(R.layout.item_my_class_view, parent, false);
            return new ClassHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ClassHolder holder, int position) {
            final Clazz clazzItem = mClassList.get(position);
            holder.tvName.setText(clazzItem.getName());
            holder.tvTime.setText("create at : " + clazzItem.getCreateTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyClassActivity.this, StudentOperationActivity.class);
                    intent.putExtra("class_id", clazzItem.getId());
                    MyClassActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mClassList.size();
        }
    }

    class ClassHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvTime;

        ClassHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }
}
