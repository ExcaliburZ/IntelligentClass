package com.wings.intelligentclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wings.intelligentclass.domain.Class;
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
    private List<Class> mClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_manager);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        Call<List<Class>> resultCall = iUserBiz.getClasses(GlobalPara.getInstance().class_id);
        resultCall.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {

                ToastUtils.showToast(ClassManagerActivity.this, "load classes success");
                mSrlRefresh.setRefreshing(false);
                mClassList = response.body();
                ClassListAdapter adapter = new ClassListAdapter();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ClassManagerActivity.this));
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                ToastUtils.showToast(ClassManagerActivity.this, "load classes failed");
            }
        });
    }

    private class ClassListAdapter extends RecyclerView.Adapter<ClassHolder> {

        @Override
        public ClassHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater
                    .from(ClassManagerActivity.this).inflate(R.layout.class_item_view, parent, false);
            return new ClassHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ClassHolder holder, int position) {
            Class item = mClassList.get(position);
            holder.tvName.setText(item.getName());
            holder.tvLimit.setText(getStudentListNum(item) + "/" + item.getLimitNum());
            holder.tvTime.setText(item.getCreateTime());
        }

        private int getStudentListNum(Class item) {
            if (item.getStudentList() == null) {
                return 0;
            }
            return item.getStudentList().size();
        }

        @Override
        public int getItemCount() {
            return mClassList.size();
        }
    }

    class ClassHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvLimit;
        private TextView tvTime;

        public ClassHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvLimit = (TextView) itemView.findViewById(R.id.tv_limit);
            tvTime = (TextView) itemView.findViewById(R.id.tv_create_time);
        }
    }

}
