package com.gpfei.recruit.ui.activities.hr.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.common.MainActivity;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.ui.activities.hr.HrMainActivity;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class HrLoginFragment extends Fragment implements View.OnClickListener {
    private EditText et_phone_login;
    private EditText et_pwd_login;
    private Button btn_login;
    private TextView tv_HR;

    String url = "http://114.117.0.103:8080/recruit/user/login";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hr_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        et_phone_login = view.findViewById(R.id.et_phone_login);
        et_pwd_login = view.findViewById(R.id.et_pwd_login);
        btn_login = view.findViewById(R.id.btn_login);
        tv_HR = view.findViewById(R.id.tv_HR_login);
        btn_login.setOnClickListener(this);
        tv_HR.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("userinfo",MODE_PRIVATE);
        if (sharedPreferences.contains("username")){
            et_phone_login.setText(sharedPreferences.getString("username",null));
            et_pwd_login.setText(sharedPreferences.getString("password",null));
        }
        editor = sharedPreferences.edit();
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (et_phone_login.getText().toString().isEmpty()) {
                    ToastUtils.showTextToast(getContext(), "账号不能为空哟~");
                } else if (et_phone_login.getText().toString().length() < 6) {
                    ToastUtils.showTextToast(getContext(), "账号不能小于6位~");
                } else if (et_pwd_login.getText().toString().isEmpty()) {
                    ToastUtils.showTextToast(getContext(), "密码不能为空~");
                } else if (et_pwd_login.getText().toString().length() < 6) {
                    ToastUtils.showTextToast(getContext(), "密码不能小于6位哟~~");
                } else {
                    String username = et_phone_login.getText().toString().trim();
                    String password = et_pwd_login.getText().toString().trim();
                    //登录
                    new Thread(){
                        @Override
                        public void run() {
                            EMClient.getInstance().login(username, password, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    editor.putString("username",username);
                                    editor.putString("password",password);
                                    editor.commit();
                                    OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("username",username);
                                    map.put("password",password);
                                    Log.d("qwert", username + "," + password);
                                    okHttpUtil.getDataFromePostJsonBack(url, map, new OkHttpUtil.OnCallback() {
                                        @Override
                                        public void callback(String result) {
                                            System.out.println(result);
                                            parseJson(result);
                                        }
                                        @Override
                                        public void onFailure(String message) {
                                        }
                                    });
                                }
                                @Override
                                public void onError(int i, String s) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "登录失败"+s, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        }
                    }.start();

                }
                break;
            case R.id.tv_HR_login:
                Intent intent = new Intent(getContext(), LoginAndRegisterActivity.class);
                //清空栈底
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
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
                String isHR = object.getString("isHR");
                if (isHR.equals("false")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showImageToast(getContext(),"登录成功");
                            Intent intent=new Intent(getContext(), MainActivity.class);
                            //清空栈底
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showImageToast(getContext(),"登录成功");
                            Intent intent=new Intent(getContext(), HrMainActivity.class);
                            //清空栈底
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }

            } else {
                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}