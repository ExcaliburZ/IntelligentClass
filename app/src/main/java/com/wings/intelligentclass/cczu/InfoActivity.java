package com.wings.intelligentclass.cczu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wings.intelligentclass.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.wv_cczu)
    WebView mWvCczu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cczu);
        ButterKnife.bind(this);
        mWvCczu.loadUrl("http://jsjx.cczu.edu.cn/");
        WebSettings wSet = mWvCczu.getSettings();
        wSet.setJavaScriptEnabled(true);
        mWvCczu.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
