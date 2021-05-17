package com.gpfei.recruit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.common.MainActivity;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.ui.activities.hr.HrMainActivity;
import com.gpfei.recruit.utils.SmileToast;
import com.hyphenate.chat.EMClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 开屏广告，延迟两秒进入Main
 * 先查询出是否为HR，然后判断跳转不同的activity
 */

public class WelcomeActivity extends AppCompatActivity {

    private final long SPLASH_LENGTH = 2000;
    Timer timer = new Timer();
    private Boolean isHR = false;
    private SharedPreferences sharedPreferences;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        String url = "http://114.117.0.103:8080/recruit/user/getIsHr";
        sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        username = sharedPreferences.getString("username",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url+"?username="+username).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    parseJson(responsedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        timer.schedule(new TimerTask() {
            /**
             * 首先对环信服务器进行判断，
             *然后对Bmob进行登录判读
             */
            @Override
            public void run() {
               // startActivity(new Intent(WelcomeActivity.this,MessageActivity.class));
                new Thread(){
                    @Override
                    public void run() {
                        if (EMClient.getInstance().isLoggedInBefore()){
                            //环信已登录
                            if (username!=null){
                                if (isHR){
                                    Intent intent = new Intent(WelcomeActivity.this, HrMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(WelcomeActivity.this,LoginAndRegisterActivity.class));
                                        //Toast.makeText(WelcomeActivity.this, "Bmob未登录", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else {
                            //环信未登录
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SmileToast smileToast = new SmileToast();
                                    smileToast.smile("请先登录");
                                    startActivity(new Intent(WelcomeActivity.this,LoginAndRegisterActivity.class));
                                }
                            });
                        }
                    }
                }.start();
            }
        }, SPLASH_LENGTH);
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //第二层解析
                JSONArray items = data.optJSONArray("data");
                //第三层解析
                JSONObject object = items.getJSONObject(0);
                isHR = Boolean.valueOf(object.getString("isHR"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}