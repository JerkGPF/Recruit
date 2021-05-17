package com.gpfei.recruit.ui.activities.hr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.SmileToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private EditText et_hr_company_job;
    private EditText et_hr_company_salary;
    private EditText et_hr_company_name;
    private EditText et_hr_company_place;
    private EditText et_hr_company_url;
    private TextView tv_title;
    private CheckBox cb_full,cb_part,cb_pratice;
    private Button btn;
    private Button cancel;

    String url = "http://114.117.0.103:8080/recruit/jobinfo/addJob";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        getCompany();
    }



    private void initView() {
        sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        cb_full = findViewById(R.id.cb_full);
        cb_part = findViewById(R.id.cb_part);
        cb_pratice = findViewById(R.id.cb_pratice);
        cancel = findViewById(R.id.cancel);
        et_hr_company_job = findViewById(R.id.et_hr_company_job);
        et_hr_company_salary = findViewById(R.id.et_hr_company_salary);
        et_hr_company_name = findViewById(R.id.et_hr_company_name);
        et_hr_company_place = findViewById(R.id.et_hr_company_place);
        et_hr_company_url = findViewById(R.id.et_hr_company_url);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btn = findViewById(R.id.submit);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("职位发布");
        btn.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    //判断输入是否为空
    private Boolean isEmpty() {
        String job = et_hr_company_job.getText().toString().trim();
        String salary = et_hr_company_salary.getText().toString().trim();
        String place = et_hr_company_place.getText().toString().trim();
        String url = et_hr_company_url.getText().toString().trim();
        if (TextUtils.isEmpty(job)) {
            Toast.makeText(this, "职位不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(salary)) {
            Toast.makeText(this, "薪资不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(place)) {
            Toast.makeText(this, "工作地点不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "工作详情不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private List<String> list = new ArrayList<>();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.cancel:
                finish();
                break;
            case R.id.submit:
                list.clear();
                int callnumber = 0;
                if (isEmpty()){
                    if (cb_part.isChecked()){
                        Log.d("兼职", "onClick:兼职 ");
                        list.add("兼职");
                    }
                    if (cb_full.isChecked()){
                        Log.d("兼职", "onClick:全职 ");
                        list.add("全职");

                    }
                    if (cb_pratice.isChecked()){
                        list.add("实习");
                        Log.d("兼职", "onClick:实习 ");
                    }
                    for (String s : list){
                        if (saveJob(s) == 0){
                            callnumber++;
                            Log.d("qwert", "onClick: for callnumber " + callnumber);
                            if (callnumber == list.size()){
                                SmileToast smileToast = new SmileToast();
                                smileToast.smile("发布成功");
                                finish();
                            }
                        }
                    }
                }
                Log.d("qwert", "onClick: callnumber :" + callnumber);
                Log.d("qwert", "onClick: listsize :" + list.size());
                break;
        }
    }

    private void getCompany() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlCompany = "http://114.117.0.103:8080/recruit/companyinfo/getCompanyInfo";
                String username = sharedPreferences.getString("username",null);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(urlCompany+"?username="+username).build();
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
                            String companyname = object.getString("name");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    et_hr_company_name.setText(companyname);
                                    et_hr_company_name.setEnabled(false);
                                }
                            });
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

    int number = 0;

    private int saveJob(String kind) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = sharedPreferences.getString("username",null);
                String salary = et_hr_company_salary.getText().toString().trim();
                Double temp = Double.parseDouble(salary)/1000;
                DecimalFormat df = new DecimalFormat("0.00");
                salary = df.format(temp);
                Random rand = new Random();
                int randNumber =rand.nextInt(800 - 200 + 1) + 200;
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String,Object> map = new HashMap<>();
                map.put("count",randNumber);
                map.put("detail",et_hr_company_url.getText().toString().trim());
                map.put("kind",kind);
                map.put("place",et_hr_company_place.getText().toString().trim());
                map.put("salary",salary+"K");
                map.put("title",et_hr_company_job.getText().toString().trim());
                map.put("companyname",et_hr_company_name.getText().toString().trim());
                map.put("username",username);
                okHttpUtil.getDataFromePostJson(url, map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                number = 0;

                            }
                        });
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
            }
        }).start();

        return number;
    }
}