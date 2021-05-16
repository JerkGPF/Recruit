package com.gpfei.recruit;

import android.app.Application;

import com.gpfei.recruit.utils.OKHttpUpdateHttpService;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;

public class EaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //bmob初始化key
        Bmob.initialize(this, "9512af567c7e2c0ee9c58c9598ec611d");
        EMOptions options = new EMOptions();
        EaseUI.getInstance().init(this,options);
        initOKHttpUtils();
        initUpdate();
    }


    private void initUpdate() {
        XUpdate.get()
                .debug(true)
                //默认设置只在wifi下检查版本更新
                .isWifiOnly(false)
                //默认设置使用get请求检查版本
                .isGet(false)
                //默认设置非自动模式，可根据具体使用配置
                .isAutoMode(false)
                //设置默认公共请求参数
                .param("versionCode", UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                //设置版本更新出错的监听
                .setOnUpdateFailureListener(error -> {
                    error.printStackTrace();
                    //对不同错误进行处理
//                    SmileToast smileToast = new SmileToast();
//                    smileToast.smile(error.toString());
                    ToastUtils.showTextToast(this,error.toString());
//                    if (error.getCode() != CHECK_NO_NEW_VERSION) {
//                        Toasty.success(this,error.toString(), Toast.LENGTH_SHORT).show();
//                    }
                })
                //设置是否支持静默安装，默认是true
                .supportSilentInstall(false)
                //这个必须设置！实现网络请求功能。
                .setIUpdateHttpService(new OKHttpUpdateHttpService())
                //这个必须初始化
                .init(this);

    }

    private void initOKHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
