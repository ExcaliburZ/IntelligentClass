package com.wings.intelligentclass;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wings.intelligentclass.domain.UploadDocument;
import com.wings.intelligentclass.download.ProgressListener;
import com.wings.intelligentclass.download.ProgressResponseBody;
import com.wings.intelligentclass.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadDocActivity extends AppCompatActivity implements ProgressListener {

    @BindView(R.id.rv_classes)
    RecyclerView mDocRecyclerView;
    private List<UploadDocument> mDocInfoList;
    private IUserBiz mIUserBiz;
    private DocListAdapter mAdapter;
    private MaterialDialog mDownloadDialog;

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
        Call<List<UploadDocument>> docListCall = mIUserBiz.getDocList(classId);
        docListCall.enqueue(new Callback<List<UploadDocument>>() {
            @Override
            public void onResponse(Call<List<UploadDocument>> call, Response<List<UploadDocument>> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(DownloadDocActivity.this, "获取教案列表失败,请稍后再试");
                    return;
                }
                mDocInfoList = response.body();
                mAdapter = new DocListAdapter();
                mDocRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<UploadDocument>> call, Throwable t) {
                ToastUtils.showToast(DownloadDocActivity.this, "获取教案列表失败,请稍后再试");
            }
        });
    }

    private class DocListAdapter extends BaseQuickAdapter<UploadDocument, BaseViewHolder> {

        DocListAdapter() {
            super(R.layout.item_doc_download, mDocInfoList);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, final UploadDocument item) {
            viewHolder.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_upload_time, "upload at :" + item.getUpload_time())
                    .setText(R.id.tv_size, item.getSize());
            if (item.getName().endsWith(".doc") || item.getName().endsWith(".docx")) {
                viewHolder.setImageResource(R.id.iv_icon, R.drawable.doc);
            } else if (item.getName().endsWith(".ppt")) {
                viewHolder.setImageResource(R.id.iv_icon, R.drawable.ppt);
            } else if (item.getName().endsWith(".rar")) {
                viewHolder.setImageResource(R.id.iv_icon, R.drawable.rar);
            } else if (item.getName().endsWith(".pdf")) {
                viewHolder.setImageResource(R.id.iv_icon, R.drawable.pdf_2);
            } else if (item.getName().endsWith(".txt")) {
                viewHolder.setImageResource(R.id.iv_icon, R.drawable.txt);
            }
            viewHolder.getView(R.id.iv_download_doc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(item);
                }
            });
        }

        private void showDialog(final UploadDocument item) {
            new MaterialDialog.Builder(DownloadDocActivity.this)
                    .title("下载教案")
                    .content("你确定要下载教案 : " + item.getName() + "吗?")
                    .positiveText("确定")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SendDownloadRequest(item);
                        }
                    })
                    .show();
        }

        private void SendDownloadRequest(final UploadDocument item) {
            showDownloadDialog();

            int index = item.getDownload_url().indexOf("document");
            String substring = item.getDownload_url().substring(index);
            String extraUrl = substring.replace("\\", "/") + "/";
            String realUrl = RetrofitManager.BASE_URL + extraUrl + item.getUuidname();
            Request request = new Request.Builder()
                    .url(realUrl)
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            okhttp3.Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    .body(new ProgressResponseBody(originalResponse.body(), DownloadDocActivity.this))
                                    .build();
                        }
                    })
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    mDownloadDialog.dismiss();
                    ToastUtils.showToast(DownloadDocActivity.this, "下载失败");
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    InputStream in = response.body().byteStream();
                    int len = 0;
                    byte buffer[] = new byte[1024];
                    String path = "/storage/emulated/0/Download/";
                    File file = new File("/storage/emulated/0/Download");
                    String message = item.getName() + "已被下载到Download中";
                    if (!file.exists()) {
                        path = Environment.getExternalStorageDirectory().getPath() + "/";
                        message = item.getName() + "已被下载到内存卡根目录中";
                    }
                    OutputStream out = new FileOutputStream(path + item.getName());
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    in.close();
                    out.close();
                    ToastUtils.showToast(DownloadDocActivity.this, message);
                }
            });
        }

    }

    private void showDownloadDialog() {
        mDownloadDialog = new MaterialDialog.Builder(this)
                .title("下载进度")
                .progress(false, 100, true)
                .show();
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        if (done) {
            mDownloadDialog.dismiss();
            return;
        }
        System.out.println("bytesRead ::" + bytesRead);
        double percent = (double) bytesRead / (double) contentLength;
        mDownloadDialog.setProgress((int) (percent * 100));
    }
}
