package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.MessageActivity;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.easeui.EaseConstant;
import com.mob.MobSDK;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class JobWebDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private WebView mWebView;
    private LinearLayout ll_error_state;
    private ImageView iv_sharing;
    private LinearLayout ll_collect;
    private Button btn_load;
    private TextView tv_collect;
    private String objectId;
    String title;
    String url;
    private TextView tv_check;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_web_details);
        objectId = (String) getIntent().getExtras().get("objectId");
        url = (String) getIntent().getExtras().get("url");
        title = (String) getIntent().getExtras().get("title");
        initView();
        loadWeb();
    }

    private void initView() {
        MobSDK.submitPolicyGrantResult(true, null);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        ll_collect = findViewById(R.id.ll_collect);
        tv_check = findViewById(R.id.tv_check);
        ll_error_state = findViewById(R.id.ll_error_state);
        mWebView = findViewById(R.id.mWebView);
        iv_sharing = findViewById(R.id.iv_sharing);
        btn_load = findViewById(R.id.btn_load);
        tv_collect = findViewById(R.id.tv_collect);
        btn_load.setOnClickListener(this);
        tv_title.setText("??????");

        sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JobWebDetailsActivity.this, CheckHrActivity.class).putExtra("companyId",objectId));
            }
        });
        ll_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = sharedPreferences.getString("username",null);
                if (username !=null){
                    Intent chat = new Intent(JobWebDetailsActivity.this, MessageActivity.class);
                    chat.putExtra(EaseConstant.EXTRA_USER_ID, objectId);  //????????????
                    chat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE); //????????????
                    startActivity(chat);
                }else {
                    ToastUtils.showTextToast(JobWebDetailsActivity.this, "????????????!");
                    startActivity(new Intent(JobWebDetailsActivity.this, LoginAndRegisterActivity.class));
                }
            }
        });

    }

    private void share() {
        OnekeyShare oks = new OnekeyShare();
        // title??????????????????QQ???QQ?????????????????????
        //oks.setTitle(getString(R.string.share));
        // titleUrl QQ???QQ??????????????????
        oks.setTitleUrl(url);
        // text???????????????????????????????????????????????????
        oks.setText(title+"?????????");
        // setImageUrl??????????????????url
        //oks.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
        // url????????????Facebook??????????????????
        oks.setUrl(url);
        // ????????????GUI
        oks.show(MobSDK.getContext());
    }

    private void loadWeb() {
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
                //????????????
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //????????????
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //???????????????????????????????????????
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
        }
    }
}