package com.wings.intelligentclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

public class ClassManagerActivity extends AppCompatActivity {

    @BindView(R.id.rv_classes)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;
    private List<Clazz> mClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_manager);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClassManagerActivity.this, AddClassActivity.class);
                ClassManagerActivity.this.startActivity(i);
            }
        });
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
        Call<List<Clazz>> resultCall = iUserBiz.getClassManager(GlobalPara.getInstance().account);
        resultCall.enqueue(new Callback<List<Clazz>>() {
            @Override
            public void onResponse(Call<List<Clazz>> call, Response<List<Clazz>> response) {
                mSrlRefresh.setRefreshing(false);
                mClassList = response.body();
                ClassListAdapter adapter = new ClassListAdapter();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ClassManagerActivity.this));
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Clazz>> call, Throwable t) {
                ToastUtils.showToast(ClassManagerActivity.this, "load classes failed");
            }
        });
    }


    private class ClassListAdapter extends RecyclerView.Adapter<ClassHolder> {

        @Override
        public ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(ClassManagerActivity.this).inflate(R.layout.item_class_manager_view, parent, false);
            return new ClassHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ClassHolder holder, int position) {
            final Clazz item = mClassList.get(position);
            holder.tvName.setText(item.getName());
            holder.tvLimit.setText(getStudentListNum(item) + "/" + item.getLimitNum());
            holder.tvTime.setText("create at : " + item.getCreateTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ClassManagerActivity.this, ClassDetailActivity.class);
                    intent.putExtra("class", item);
                    ClassManagerActivity.this.startActivity(intent);
                }
            });
        }

        private int getStudentListNum(Clazz item) {
            if (item.getStudentList() == null) {
                return 0;
            }
            return item.getStudentList().size();
        }

        @Override
        public int getItemCount() {
            if (mClassList == null || mClassList.size() == 0) {
                return 0;
            }
            return mClassList.size();
        }
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
