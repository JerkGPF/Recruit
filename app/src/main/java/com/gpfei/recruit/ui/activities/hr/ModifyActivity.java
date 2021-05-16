package com.gpfei.recruit.ui.activities.hr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.DayBean;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.SmileToast;
import com.gpfei.recruit.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private EditText et_hr_company_job;
    private EditText et_hr_company_salary;
    private EditText et_hr_company_name;
    private EditText et_hr_company_place;
    private EditText et_hr_company_url;
    private TextView tv_title;
    private Button btn,cancel;

    String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initView();
    }

    private void initView() {
        cancel = findViewById(R.id.cancel);
        et_hr_company_job = findViewById(R.id.et_hr_company_job);
        et_hr_company_salary = findViewById(R.id.et_hr_company_salary);
        et_hr_company_name = findViewById(R.id.et_hr_company_name);
        et_hr_company_place = findViewById(R.id.et_hr_company_place);
        et_hr_company_url = findViewById(R.id.et_hr_company_url);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btn = findViewById(R.id.submit);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("职位修改");
        btn.setOnClickListener(this);
        cancel.setOnClickListener(this);
        loadFull();//全职
    }
    private void loadFull() {
        objectId = getIntent().getStringExtra("modify");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://114.117.0.103:8080/recruit/jobinfo/getJobById";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url+"?id="+objectId).build();
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
                JSONArray object = data.getJSONArray("data");

                for (int i = 0; i < object.length();i++){
                    JSONObject jsonObject1 = object.getJSONObject(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                et_hr_company_job.setText(jsonObject1.getString("title"));
                                et_hr_company_salary.setText(jsonObject1.getString("salary"));
                                et_hr_company_name.setText(jsonObject1.getString("companyname"));
                                et_hr_company_place.setText(jsonObject1.getString("place"));
                                et_hr_company_url.setText(jsonObject1.getString("detail"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
            case R.id.cancel:
                finish();
                break;
            case R.id.submit:
                updateJobInfo();
                break;
        }
    }
    private void updateJobInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                String url = "http://114.117.0.103:8080/recruit/jobinfo/updateJobById";
                Map<String, Object> map = new HashMap<>();
                map.put("id",objectId);
                map.put("salary",et_hr_company_salary.getText().toString());
                map.put("title",et_hr_company_job.getText().toString());
                map.put("detail",et_hr_company_url.getText().toString());
                map.put("companyname",et_hr_company_name.getText().toString());
                map.put("place",et_hr_company_place.getText().toString());
                okHttpUtil.getDataFromePostJson(url, map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SmileToast smileToast = new SmileToast();
                                smileToast.smile("修改成功");
                                finish();
                            }
                        });
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        }).start();
    }
}