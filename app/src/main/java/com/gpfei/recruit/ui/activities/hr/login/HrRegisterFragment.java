package com.gpfei.recruit.ui.activities.hr.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.hr.HrUpdateInfoActivity;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class HrRegisterFragment extends Fragment implements View.OnClickListener {
    private EditText et_phone;
    private EditText et_pwd;
    private Button btn_register;
    private EditText et_pwd2;
    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;


    String url = "http://114.117.0.103:8080/recruit/user/registuser";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hr_register, container, false);
        initView(view);
        sharedPreferences = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();

        return view;
    }

    private void initView(View view) {
        et_phone = view.findViewById(R.id.et_phone);
        et_pwd = view.findViewById(R.id.et_pwd);
        btn_register = view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        et_pwd2 = view.findViewById(R.id.et_pwd2);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                if (et_phone.getText().toString().isEmpty()) {
                    ToastUtils.showImageToast(getContext(), "账号不能为空哟~");
                }else if (et_phone.getText().toString().length()<6) {
                    ToastUtils.showImageToast(getContext(), "账号不能小于6位哟~");
                }else if (et_pwd.getText().toString().isEmpty()) {
                    ToastUtils.showImageToast(getContext(), "密码不能为空哟~");
                }else if (et_pwd.getText().toString().length()<6){
                    ToastUtils.showImageToast(getContext(), "密码不能小于6位哟~");
                }else if (et_pwd2.getText().toString().isEmpty()) {
                    ToastUtils.showImageToast(getContext(), "确认一下密码~");
                }else if (!et_pwd2.getText().toString().equals(et_pwd.getText().toString())){
                    ToastUtils.showImageToast(getContext(), "两次密码不一致~");
                }else {
                    String username = et_phone.getText().toString().trim();
                    String password = et_pwd.getText().toString().trim();
                    register(username,password);
                }
                break;
        }
    }

    private void register(String username, String password) {
        new Thread() {
            @Override
            public void run() {
                //注册环信
                try {
                    EMClient.getInstance().createAccount(username, password);
                    OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                    Map<String, Object> map = new HashMap<>();
                    map.put("username",username);
                    map.put("password",password);
                    map.put("isHR", "true");
                    okHttpUtil.getDataFromePostJson(url, map, new OkHttpUtil.OnCallback() {
                        @Override
                        public void callback(String result) {
                            editor.putString("username",username);
                            editor.putString("password",password);
                            editor.commit();
                            parseJson(result);
                        }

                        @Override
                        public void onFailure(String message) {

                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "注册失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }

    private void parseJson(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showImageToast(getContext(), "注册成功！！");
                        Intent intent = new Intent(getContext(), HrUpdateInfoActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}