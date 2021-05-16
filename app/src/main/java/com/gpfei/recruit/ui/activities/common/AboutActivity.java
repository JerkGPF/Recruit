package com.gpfei.recruit.ui.activities.common;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.ToastUtils;
import com.gpfei.recruit.utils.VersionUtils;
import com.xuexiang.xupdate.XUpdate;

import org.jetbrains.annotations.NotNull;

import constant.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

public class AboutActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_version;
    private TextView tv_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        UpdateAppUtils.init(this);
        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("关于我们");
        tv_version = findViewById(R.id.tv_version);
        //设置版本号
        String versions = "version " + VersionUtils.getVersionName(AboutActivity.this);
        tv_version.setText(versions);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_update = findViewById(R.id.tv_update);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XUpdate.newBuild(AboutActivity.this)
                        .updateUrl("http://114.117.0.103:1111/mock/update/checkVersion")
                        .promptButtonTextColor(Color.WHITE)
                        .promptWidthRatio(0.7F)
                        .supportBackgroundUpdate(true)
                        .update();
            }
        });
    }
}