package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyPassWordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_old_pwd;
    private EditText et_new_pwd;
    private Button btn_modify_pwd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass_word);
        initView();
    }

    private void initView() {
        sharedPreferences = getSharedPreferences("userinfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("修改密码");
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        btn_modify_pwd = (Button) findViewById(R.id.btn_modify_pwd);
        btn_modify_pwd.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_pwd:
                String pwd_lod = et_old_pwd.getText().toString().trim();
                String pwd_new = et_new_pwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd_lod)){
                    ToastUtils.showTextToast(ModifyPassWordActivity.this,"输入旧密码！");
                }else if (TextUtils.isEmpty(pwd_new)){
                    ToastUtils.showTextToast(ModifyPassWordActivity.this,"输入新密码！");
                }else if (pwd_lod.length()<6){
                    ToastUtils.showTextToast(ModifyPassWordActivity.this,"旧密码不能小于6位！");
                }else if (pwd_new.length()<6){
                    ToastUtils.showTextToast(ModifyPassWordActivity.this,"新密码不能小于6位！");
                }else{
                    String oldpass = sharedPreferences.getString("password",null);
                    if (oldpass.equals(pwd_lod)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                                String username = sharedPreferences.getString("username",null);
                                String url = "http://114.117.0.103:8080/recruit/user/modifyPass";
                                Map<String, Object> map = new HashMap<>();
                                map.put("username",username);
                                map.put("password",pwd_new);
                                editor.putString("password",pwd_new);
                                editor.commit();
                                okHttpUtil.getDataFromePostJson(url, map, new OkHttpUtil.OnCallback() {
                                    @Override
                                    public void callback(String result) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtils.showImageToast(ModifyPassWordActivity.this,"修改密码成功,请重新登录！");
                                                Intent intent=new Intent(ModifyPassWordActivity.this, LoginAndRegisterActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    @Override
                                    public void onFailure(String message) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ModifyPassWordActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).start();

                    }else {
                        ToastUtils.showTextToast(ModifyPassWordActivity.this,"密码输入不正确！");
                    }
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }
}