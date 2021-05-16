package com.gpfei.recruit.ui.activities.hr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HrDataActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_to_edit;
    private ImageView iv_company_head;
    private TextView tv_company_name,tv_company_phone,tv_company_birth,
            tv_company_email,tv_company_induce,tv_company_benfits;
    private PullToRefreshLayout refresh_data;


    String url = "http://114.117.0.103:8080/recruit/companyinfo/getCompanyInfo";
    private SharedPreferences sharedPreferences,sp;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_data);
        initView();
        sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);

        sp = getSharedPreferences("companyInfo", MODE_PRIVATE);
        editor = sp.edit();
        editor.commit();

        showCompanyInfo();
    }
    private void initView() {
        iv_back =findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("公司基本资料");
        tv_to_edit = findViewById(R.id.tv_to_edit);
        iv_company_head = findViewById(R.id.iv_company_head);
        tv_company_name = findViewById(R.id.tv_company_name);
        tv_company_phone = findViewById(R.id.tv_company_phone);
        tv_company_birth = findViewById(R.id.tv_company_birthday);
        tv_company_email = findViewById(R.id.tv_company_email);
        tv_company_induce = findViewById(R.id.tv_company_induce);
        tv_company_benfits = findViewById(R.id.tv_company_benfits);
        iv_back.setOnClickListener(this);
        tv_to_edit.setOnClickListener(this);
        refresh_data = findViewById(R.id.refresh_data);
        iv_company_head.setVisibility(View.GONE);


        refresh_data.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showCompanyInfo();
                        //结束刷新
                        refresh_data.finishRefresh();
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
                        refresh_data.finishLoadMore();
                    }
                }, 2000);

            }
        });
    }
    private void showCompanyInfo() {
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
                editor.putString("phone",object.getString("phone"));
                editor.putString("birthday",object.getString("birthday"));
                editor.putString("email",object.getString("email"));
                editor.putString("profile",object.getString("profile"));
                editor.putString("free",object.getString("free"));
                editor.commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv_company_name.setText(object.getString("name"));
                            tv_company_phone.setText(object.getString("phone"));
                            tv_company_birth.setText(object.getString("birthday"));
                            tv_company_email.setText(object.getString("email"));
                            tv_company_induce.setText(object.getString("profile"));
                            tv_company_benfits.setText(object.getString("free"));
                            Log.d("companyInfo", object.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(HrDataActivity.this, "获取失败！");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_to_edit:
                startActivity(new Intent(HrDataActivity.this,HrEditInfoActivity.class));
        }

    }
}