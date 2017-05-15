package com.wings.intelligentclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wings.intelligentclass.domain.CheckInItem;
import com.wings.intelligentclass.domain.CheckInRequest;
import com.wings.intelligentclass.domain.CheckInResultData;
import com.wings.intelligentclass.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckInResultActivity extends AppCompatActivity {

    @BindView(R.id.rv_check_in_result)
    RecyclerView mCheckInRecyclerView;
    private int mAmount;
    private List<CheckInItem> mCheckInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_result);
        ButterKnife.bind(this);
        mCheckInRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        String classID = intent.getStringExtra("class_id");
        ArrayList<String> accountList = intent.getStringArrayListExtra("account_list");
        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        final Call<CheckInResultData> checkInResultCall =
                iUserBiz.getCheckInResult(new CheckInRequest(accountList, classID));
        checkInResultCall.enqueue(new Callback<CheckInResultData>() {
            @Override
            public void onResponse(Call<CheckInResultData> call, Response<CheckInResultData> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(CheckInResultActivity.this, "查询失败");
                    return;
                }
                CheckInResultData checkInResultData = response.body();
                mAmount = checkInResultData.getAmount();
                mCheckInList = checkInResultData.getCheckInList();
                mCheckInRecyclerView.setAdapter(new CheckInResultAdapter());
            }

            @Override
            public void onFailure(Call<CheckInResultData> call, Throwable t) {
                ToastUtils.showToast(CheckInResultActivity.this, "failed");
            }
        });

    }

    private class CheckInResultAdapter extends BaseQuickAdapter<CheckInItem, BaseViewHolder> {
        CheckInResultAdapter() {
            super(R.layout.item_check_in_view, mCheckInList);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, CheckInItem item) {
            viewHolder.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_number, item.getNumber())
                    .setText(R.id.tv_count, item.getCount() + "/" + mAmount);
        }
    }
}
