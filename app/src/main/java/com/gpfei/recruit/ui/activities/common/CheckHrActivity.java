package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.HrUser;
import com.gpfei.recruit.beans.User;
import com.gpfei.recruit.ui.activities.hr.HrDataActivity;
import com.gpfei.recruit.ui.activities.hr.HrEditInfoActivity;
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
import cn.bmob.v3.listener.QueryListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckHrActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_to_edit;
    private ImageView iv_company_head;
    private TextView tv_company_name,tv_company_phone,tv_company_birth,
            tv_company_email,tv_company_induce,tv_company_benfits;
    private PullToRefreshLayout refresh_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_data);
        initView();
        showCompanyInfo();
    }
    private void initView() {
        iv_back =findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("公司基本资料");
        tv_to_edit = findViewById(R.id.tv_to_edit);
        tv_to_edit.setVisibility(View.GONE);
        iv_company_head = findViewById(R.id.iv_company_head);
        tv_company_name = findViewById(R.id.tv_company_name);
        tv_company_phone = findViewById(R.id.tv_company_phone);
        tv_company_birth = findViewById(R.id.tv_company_birthday);
        tv_company_email = findViewById(R.id.tv_company_email);
        tv_company_induce = findViewById(R.id.tv_company_induce);
        tv_company_benfits = findViewById(R.id.tv_company_benfits);
        iv_company_head.setVisibility(View.GONE);
        iv_back.setOnClickListener(this);
        tv_to_edit.setOnClickListener(this);
        refresh_data = findViewById(R.id.refresh_data);



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
                String companyId = (String) getIntent().getExtras().get("companyId");
                String getUrl = "http://114.117.0.103:8080/recruit/companyinfo/getCompanyInfo";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getUrl+"?username="+companyId).build();
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        }

    }
}