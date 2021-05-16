package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.ToastUtils;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.mob.MobSDK;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class FindsWebDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private WebView mWebView;
    private LinearLayout ll_error_state;
    private ImageView iv_sharing;
    private Button btn_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finds_web_details);
        initView();
        loadWeb();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("返回");
        ll_error_state = findViewById(R.id.ll_error_state);
        btn_load = findViewById(R.id.btn_load);
        btn_load.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        mWebView = findViewById(R.id.mWebView);
        iv_sharing = findViewById(R.id.iv_sharing);
        iv_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();//分享
            }
        });

    }

    private void share() {
        String url = (String) getIntent().getExtras().get("url");
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        //oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
//        oks.setText(title+"招聘！");
        // setImageUrl是网络图片的url
        //oks.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
        // url在微信、Facebook等平台中使用
        oks.setUrl(url);
        // 启动分享GUI
        oks.show(MobSDK.getContext());
    }

    private void loadWeb() {
        String url = (String) getIntent().getExtras().get("url");
        if (url != null) {
            mWebView.loadUrl(url);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                initView();
                loadWeb();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}