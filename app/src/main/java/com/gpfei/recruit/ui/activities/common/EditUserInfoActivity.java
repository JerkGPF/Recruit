package com.gpfei.recruit.ui.activities.common;

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
import android.widget.RadioButton;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private LinearLayout ll_select_age;
    private TextView tv_show_birthday;
    private EditText et_name, et_edit_phone, et_edit_qq,
            et_edit_email, et_introduce, et_exper;
    private RadioButton rb_sex_male, rb_sex_female;
    private Button btn_submit;
    private SharedPreferences sharedPreferences;

    String url = "http://114.117.0.103:8080/recruit/userinfo/setuserinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("完善基本资料");
        ll_select_age = findViewById(R.id.ll_select_age);
        iv_back.setOnClickListener(this);
        ll_select_age.setOnClickListener(this);
        tv_show_birthday = findViewById(R.id.tv_show_birthday);

        et_name = findViewById(R.id.et_name);
        et_edit_phone = findViewById(R.id.et_edit_phone);
        et_edit_qq = findViewById(R.id.et_edit_qq);
        et_edit_email = findViewById(R.id.et_edit_email);
        et_introduce = findViewById(R.id.et_introduce);
        et_exper = findViewById(R.id.et_exper);
        rb_sex_male = findViewById(R.id.rb_sex_male);
        rb_sex_female = findViewById(R.id.rb_sex_female);
        btn_submit = findViewById(R.id.btn_submit);
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
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_select_age:
                showDatePickDlg();
                break;
            case R.id.btn_submit:
                toInsert();
                break;
        }
    }
    private void toInsert() {
        // 耗时操作要在子线程中操作
        new Thread() {
            public void run() {
                sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
                String username = sharedPreferences.getString("username",null);
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String, Object> map = new HashMap<>();
                map.put("birthday",tv_show_birthday.getText().toString());
                map.put("email",et_edit_email.getText().toString());
                map.put("experience",et_exper.getText().toString());
                map.put("file","");
                map.put("head","");
                map.put("motto","");
                map.put("nick","");
                map.put("name",et_name.getText().toString());
                map.put("phone",et_edit_phone.getText().toString());
                map.put("profile",et_introduce.getText().toString());
                map.put("qq",et_edit_qq.getText().toString());
                if (rb_sex_male.isChecked()) {
                    map.put("sex","M");
                } else if (rb_sex_female.isChecked()) {
                    map.put("sex","F");
                }
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
        }.start();
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(EditUserInfoActivity.this, "基本资料完善成功！");
                        Intent intent = new Intent(EditUserInfoActivity.this, LoginAndRegisterActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(EditUserInfoActivity.this, "基本资料完善失败！");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditUserInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tv_show_birthday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

}