package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.DayBean;
import com.gpfei.recruit.beans.HrUser;
import com.gpfei.recruit.beans.MyUser;
import com.gpfei.recruit.beans.SelectAndResume;
import com.gpfei.recruit.beans.User;
import com.gpfei.recruit.ui.activities.MessageActivity;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.ui.activities.hr.HrDataActivity;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.easeui.EaseConstant;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.I;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
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
        tv_title.setText("返回");

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
                    chat.putExtra(EaseConstant.EXTRA_USER_ID, objectId);  //对方账号
                    chat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE); //单聊模式
                    startActivity(chat);
                }else {
                    ToastUtils.showTextToast(JobWebDetailsActivity.this, "请先登录!");
                    startActivity(new Intent(JobWebDetailsActivity.this, LoginAndRegisterActivity.class));
                }
            }
        });

    }

    private void share() {
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        //oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title+"招聘！");
        // setImageUrl是网络图片的url
        //oks.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
        // url在微信、Facebook等平台中使用
        oks.setUrl(url);
        // 启动分享GUI
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
        }
    }
}