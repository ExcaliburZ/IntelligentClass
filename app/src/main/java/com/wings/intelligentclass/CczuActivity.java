package com.wings.intelligentclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CczuActivity extends AppCompatActivity {

    @BindView(R.id.wv_cczu)
    WebView mWvCczu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cczu);
        ButterKnife.bind(this);
        mWvCczu.loadUrl("http://www.cczu.edu.cn/");
        WebSettings wSet = mWvCczu.getSettings();
        wSet.setJavaScriptEnabled(true);
        mWvCczu.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }
}
