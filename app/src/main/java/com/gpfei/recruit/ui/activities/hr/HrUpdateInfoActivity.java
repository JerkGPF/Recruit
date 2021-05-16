package com.gpfei.recruit.ui.activities.hr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.hr.login.HrLoginAndRegisterActivity;
import com.gpfei.recruit.utils.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HrUpdateInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_compamy_birthday;
    private EditText et_company_name,et_company_phone,
                     et_company_email,et_company_introduce,et_company_free;
    private Button btn_submit;
    private LinearLayout ll_select_company_age;
    private SharedPreferences sharedPreferences;

    String url = "http://114.117.0.103:8080/recruit/companyinfo/setcompanyinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_update_info);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("完善公司基本资料");
        iv_back = findViewById(R.id.iv_back);
        tv_compamy_birthday = findViewById(R.id.tv_show_company_birthday);
        et_company_name = findViewById(R.id.et_company_name);
        et_company_phone = findViewById(R.id.et_edit_company_phone);
        et_company_email = findViewById(R.id.et_edit_company_email);
        et_company_introduce = findViewById(R.id.et_company_introduce);
        et_company_free = findViewById(R.id.et_company_free);
        btn_submit = findViewById(R.id.btn_submit);
        ll_select_company_age = findViewById(R.id.ll_select_company_age);


        ll_select_company_age.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        insertSign();
    }

    private void insertSign() {
        String insertUrl = "http://114.117.0.103:8080/recruit/sign/addSign";
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
                String username = sharedPreferences.getString("username",null);
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String, Object> map = new HashMap<>();
                map.put("username",username);
                map.put("intergal",0);
                okHttpUtil.getDataFromePostJson(insertUrl, map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_select_company_age:
                showDatePickDlg();
                break;
            case R.id.btn_submit:
                saveInfo();
                break;
        }
    }
    /**
     * 添加一对一关联，保存公司信息
     */
    private void saveInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
                String username = sharedPreferences.getString("username",null);
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String, Object> map = new HashMap<>();
                map.put("name",et_company_name.getText().toString());
                map.put("birthday",tv_compamy_birthday.getText().toString());
                map.put("phone",et_company_phone.getText().toString());
                map.put("email",et_company_email.getText().toString());
                map.put("profile",et_company_introduce.getText().toString());
                map.put("free",et_company_free.getText().toString());
                map.put("username",username);
                okHttpUtil.getDataFromePostJson(url, map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                        parseJson(result);
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });

            }
        }).start();
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HrUpdateInfoActivity.this, "公司基本信息完善成功", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(HrUpdateInfoActivity.this, HrLoginAndRegisterActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HrUpdateInfoActivity.this, "信息完善出现问题", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showDatePickDlg () {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(HrUpdateInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tv_compamy_birthday.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
    }
}