package com.wings.intelligentclass.teacher.document;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wings.intelligentclass.api.IUserBiz;
import com.wings.intelligentclass.student.download.ProgressRequestBody;
import com.wings.intelligentclass.R;
import com.wings.intelligentclass.utils.RetrofitManager;
import com.wings.intelligentclass.domain.Result;
import com.wings.intelligentclass.utils.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    private static final int FILE_SELECT_CODE = 1;
    @BindView(R.id.bt_select_doc)
    Button mBtSelectDoc;
    @BindView(R.id.bt_start_upload)
    Button mBtStartUpload;
    @BindView(R.id.tv_doc_name)
    TextView mTvDocName;
    @BindView(R.id.iv_doc_icon)
    ImageView mIvDocIcon;
    @BindView(R.id.description)
    MultiAutoCompleteTextView mDescription;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private File mFile;
    private String mClassId;
    private String mFileSizeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_doc);
        ButterKnife.bind(this);
        mClassId = getIntent().getStringExtra("class_id");

    }

    @OnClick({R.id.bt_select_doc, R.id.bt_start_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_select_doc:
                selectDoc();
                break;
            case R.id.bt_start_upload:
                startUploadDoc();
                break;
        }
    }

    private void startUploadDoc() {
        if (mFile == null) {
            ToastUtils.showToast(this, "请先选择课件");
            return;
        }

        ProgressRequestBody fileBody = new ProgressRequestBody(mFile, this);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("doc", mFile.getName(), fileBody);

        IUserBiz iUserBiz = RetrofitManager.getInstance().getIUserBiz();
        Call<Result> upload = iUserBiz.uploadDoc(filePart, mClassId, mFileSizeStr);
        upload.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() == null || response.code() / 100 != 2) {
                    ToastUtils.showToast(UploadDocActivity.this, "上传教案失败");
                    return;
                }
                UploadDocActivity.this.onFinish();
                ToastUtils.showToast(UploadDocActivity.this, "上传教案成功");
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                ToastUtils.showToast(UploadDocActivity.this, "上传教案失败");
            }
        });

    }

    @Override
    public void onProgressUpdate(int percentage) {
        mProgressBar.setProgress(percentage);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        mProgressBar.setProgress(100);
    }

    private void selectDoc() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.putExtra("browseCoa", itemToBrowse);
        //Intent chooser = Intent.createChooser(intent, "Select a File to Upload");
        try {
            //startActivityForResult(chooser, FILE_SELECT_CODE);
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (Exception ex) {
            System.out.println("browseClick :" + ex);//android.content.ActivityNotFoundException ex
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    String path = getPath(this, data.getData());
                    assert path != null;
                    mFile = new File(path);
                    mFileSizeStr = getFileSizeStr(mFile);
                    String name = mFile.getName();
                    mTvDocName.setText(name + "  :  " + mFileSizeStr);
                    if (mFile.getName().endsWith(".doc") || mFile.getName().endsWith(".docx")) {
                        mIvDocIcon.setImageResource(R.drawable.doc);
                    } else if (mFile.getName().endsWith(".ppt")) {
                        mIvDocIcon.setImageResource(R.drawable.ppt);
                    } else if (mFile.getName().endsWith(".rar")) {
                        mIvDocIcon.setImageResource(R.drawable.rar);
                    } else if (mFile.getName().endsWith(".pdf")) {
                        mIvDocIcon.setImageResource(R.drawable.pdf_2);
                    } else if (mFile.getName().endsWith(".txt")) {
                        mIvDocIcon.setImageResource(R.drawable.txt);
                    }
                    mIvDocIcon.setVisibility(View.VISIBLE);
                    ToastUtils.showToast(this, "选择成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    private String getFileSizeStr(File file) {
        String fileSizeStr;
        long fileSize = getFolderSize(file) / 1024;//call function and convert bytes into Kb
        if (fileSize >= 1024)
            fileSizeStr = fileSize / 1024 + " MB";
        else
            fileSizeStr = fileSize + " KB";
        return fileSizeStr;
    }


    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
