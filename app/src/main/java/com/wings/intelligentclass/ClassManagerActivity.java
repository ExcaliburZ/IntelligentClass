package com.wings.intelligentclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    RecyclerView mRvClasses;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;

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
                System.out.println("zzz");
                mSrlRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                ToastUtils.showToast(ClassManagerActivity.this, "load classes failed");
            }
        });
    }
}
