package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gpfei.recruit.R;

public class FileWebDetailsActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private WebView mWebView;
    private LinearLayout ll_error_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_web_details);
        initView();

    }

    private void initView() {
        ll_error_state = findViewById(R.id.ll_error_state);

        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("个人简历");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWebView = findViewById(R.id.mWebView);
        loadWeb();

    }


    private void loadWeb() {
       String fileUrl = (String) getIntent().getExtras().get("fileUrl");
        if (fileUrl != null) {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.loadUrl("http://view.officeapps.live.com/op/view.aspx?src="+fileUrl);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //加载开始
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //加载结束
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //加载页面的服务器出错时调用
                mWebView.setVisibility(View.GONE);
                ll_error_state.setVisibility(View.VISIBLE);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

    }
}