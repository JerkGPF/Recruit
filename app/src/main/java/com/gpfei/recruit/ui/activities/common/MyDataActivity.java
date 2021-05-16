package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.MyUser;
import com.gpfei.recruit.ui.activities.hr.HrDataActivity;
import com.gpfei.recruit.utils.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyDataActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_to_edit;
    private ImageView iv_user_head;
    private TextView tv_name,tv_phone,tv_sex,tv_birth,tv_qq,tv_email,tv_induce,tv_experience;
    private PullToRefreshLayout refresh_mydata;

    String url = "http://114.117.0.103:8080/recruit/userinfo/getUserInfo";
    private SharedPreferences sharedPreferences,sp;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        initView();

        sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);

        sp = getSharedPreferences("companyInfo", MODE_PRIVATE);
        editor = sp.edit();
        editor.commit();

        showUserInfo();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("基本资料");
        tv_to_edit = findViewById(R.id.tv_to_edit);
        iv_back.setOnClickListener(this);
        tv_to_edit.setOnClickListener(this);
        iv_user_head = findViewById(R.id.iv_user_head);
        iv_user_head.setOnClickListener(this);

        tv_phone = findViewById(R.id.tv_phone);
        tv_name = findViewById(R.id.tv_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_birth = findViewById(R.id.tv_birthday);
        tv_qq = findViewById(R.id.tv_qq);
        tv_email = findViewById(R.id.tv_email);
        tv_induce = findViewById(R.id.tv_induce);
        tv_experience = findViewById(R.id.tv_experience);
        refresh_mydata = findViewById(R.id.refresh_mydata);


        refresh_mydata.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showUserInfo();
                        //结束刷新
                        refresh_mydata.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast(getApplicationContext(),"没有更多内容了哟~");
                        //结束加载更多
                        refresh_mydata.finishLoadMore();
                    }
                }, 2000);

            }
        });
    }
    //显示用户资料
    private void showUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = sharedPreferences.getString("username",null);
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
                editor.putString("name",object.getString("name"));
                editor.putString("sex",object.getString("sex"));
                editor.putString("birthday",object.getString("birthday"));
                editor.putString("phone",object.getString("phone"));
                editor.putString("qq",object.getString("qq"));
                editor.putString("email",object.getString("email"));
                editor.putString("profile",object.getString("profile"));
                editor.putString("experience",object.getString("experience"));
                editor.commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv_name.setText(object.getString("name"));
                            if (object.getString("sex").equals("M")){
                                tv_sex.setText("男");
                            }else {
                                tv_sex.setText("女");
                            }
                            tv_birth.setText(object.getString("birthday"));
                            tv_phone.setText(object.getString("phone"));
                            tv_qq.setText(object.getString("qq"));
                            tv_email.setText(object.getString("email"));
                            tv_induce.setText(object.getString("profile"));
                            tv_experience.setText(object.getString("experience"));

                            String head = object.getString("head");
                            //圆形头像
                            Glide.with(MyDataActivity.this).load(head).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_user_head) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyDataActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    iv_user_head.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(MyDataActivity.this, "获取失败！");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_edit:
                startActivity(new Intent(MyDataActivity.this,UpdateUserInfoActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }
}