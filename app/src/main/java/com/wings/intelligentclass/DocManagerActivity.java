package com.wings.intelligentclass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wings.intelligentclass.domain.DocInfo;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocManagerActivity extends AppCompatActivity {

    @BindView(R.id.rv_classes)
    RecyclerView mDocRecyclerView;
    private List<DocInfo> mDocInfoList;
    private IUserBiz mIUserBiz;
    private DocListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_manager);
        ButterKnife.bind(this);
        mDocRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String classId = getIntent().getStringExtra("class_id");
        getDocData(classId);
    }

    private void getDocData(String classId) {
        mIUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<List<DocInfo>> docListCall = mIUserBiz.getDocList(classId);
        docListCall.enqueue(new Callback<List<DocInfo>>() {
            @Override
            public void onResponse(Call<List<DocInfo>> call, Response<List<DocInfo>> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(DocManagerActivity.this, "获取教案列表失败,请稍后再试");
                    return;
                }
                mDocInfoList = response.body();
                mAdapter = new DocListAdapter();
                mDocRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<DocInfo>> call, Throwable t) {
                ToastUtils.showToast(DocManagerActivity.this, "获取教案列表失败,请稍后再试");
            }
        });
    }

    private class DocListAdapter extends BaseQuickAdapter<DocInfo, BaseViewHolder> {

        DocListAdapter() {
            super(R.layout.item_doc_manager, mDocInfoList);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, final DocInfo item) {
            viewHolder.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_upload_time, "upload at :" + item.getUploadTime())
                    .setText(R.id.tv_size, item.getSize());
            viewHolder.getView(R.id.iv_delete_doc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(item);
                }
            });
        }

        private void showDialog(final DocInfo item) {
            new MaterialDialog.Builder(DocManagerActivity.this)
                    .title("删除教案")
                    .content("你确定要删除教案 : " + item.getName() + "吗?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SendDeleteDocRequest(item);
                        }
                    })
                    .show();
        }

        private void SendDeleteDocRequest(final DocInfo item) {
            Call<Result> deleteDocCall = mIUserBiz.deleteDoc(item.getId());
            deleteDocCall.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    if (response.body() == null || response.code() / 100 != 2) {
                        ToastUtils.showToast(DocManagerActivity.this, "删除教案失败,请稍后再试");
                        return;
                    }
                    ToastUtils.showToast(DocManagerActivity.this, "删除教案成功");
                    mDocInfoList.remove(item);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    ToastUtils.showToast(DocManagerActivity.this, "删除教案失败,请稍后再试");
                }
            });
        }
    }
}
