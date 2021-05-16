package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;




public class ModifyUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_nick;
    private EditText et_motto;
    private Button btn_commit_info;
    private String tag;

    String url = "http://114.117.0.103:8080/recruit/userinfo/updateuserinfo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back.setOnClickListener(this);
        et_nick = (EditText) findViewById(R.id.et_nick);
        et_motto = (EditText) findViewById(R.id.et_motto);
        btn_commit_info = (Button) findViewById(R.id.btn_commit_info);
        btn_commit_info.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("allInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.commit();

        tag = getIntent().getExtras().getString("tag");
        String nick = getIntent().getExtras().getString("nick");
        String motto = getIntent().getExtras().getString("motto");
        if (tag.equals("nick")) {
            tv_title.setText("修改昵称");
            et_nick.setVisibility(View.VISIBLE);
            //获取用户昵称
            if (nick != null) {
                et_nick.setText(nick);
                //显示光标位置在最后
                et_nick.setSelection(nick.length());
            }
        } else if (tag.equals("motto")) {
            tv_title.setText("修改个性签名");
            et_motto.setVisibility(View.VISIBLE);
            //获取个性签名
            if (motto != null) {
                et_motto.setText(motto);
                //显示光标位置在最后
                et_motto.setSelection(motto.length());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit_info:
                //修改昵称
                if (tag.equals("nick")){
                    modifyMotto("nick");
                }
                //修改昵称
                if (tag.equals("motto")){
                    modifyMotto("motto");
                }
                break;
        }
    }
    private void modifyMotto(String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = sharedPreferences.getString("username",null);
                String phone = sharedPreferences.getString("phone",null);
                String qq = sharedPreferences.getString("qq",null);
                String sex = sharedPreferences.getString("sex",null);
                String name = sharedPreferences.getString("name",null);
                String email = sharedPreferences.getString("email",null);
                String experience = sharedPreferences.getString("experience",null);
                String birthday = sharedPreferences.getString("birthday",null);
                String profile = sharedPreferences.getString("profile",null);
                String head = sharedPreferences.getString("head",null);
                String file = sharedPreferences.getString("file",null);
                String motto = sharedPreferences.getString("motto",null);
                String nick = sharedPreferences.getString("nick",null);

                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String, Object> map = new HashMap<>();
                if (s.equals("motto")){
                    map.put(s,et_motto.getText().toString());
                    map.put("nick",nick);
                    editor.putString("motto",et_motto.getText().toString());
                }else if (s.equals("nick")){
                    map.put(s,et_nick.getText().toString());
                    map.put("motto",motto);
                    editor.putString("nick",et_nick.getText().toString());
                }
                editor.commit();
                map.put("username",username);
                map.put("phone",phone);
                map.put("qq",qq);
                map.put("sex",sex);
                map.put("name",name);
                map.put("email",email);
                map.put("experience",experience);
                map.put("birthday",birthday);
                map.put("profile",profile);
                map.put("head",head);
                map.put("file",file);
                okHttpUtil.getDataFromePostJson(url, map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (s.equals("motto")){
                                    ToastUtils.showImageToast(ModifyUserInfoActivity.this,"个性签名更新成功！");
                                    setResult(200);
                                    finish();
                                }else if (s.equals("nick")){
                                    ToastUtils.showImageToast(ModifyUserInfoActivity.this,"昵称更新成功！");
                                    setResult(200);
                                    finish();
                                }

                            }
                        });
                    }
                    @Override
                    public void onFailure(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showTextToast(ModifyUserInfoActivity.this,"更新失败！");
                            }
                        });
                    }
                });
            }
        }).start();
    }

//    private void modifyNick() {
//        BmobUser myUser=BmobUser.getCurrentUser(MyUser.class);
//        myUser.setValue("nick",et_nick.getText().toString());
//        myUser.update(myUser.getObjectId(), new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e==null){
//                    ToastUtils.showImageToast(ModifyUserInfoActivity.this,"昵称更新成功！");
//                    setResult(200);
//                    finish();
//                }else {
//                    ToastUtils.showTextToast(ModifyUserInfoActivity.this,"更新失败！"+e.getMessage());
//                }
//            }
//        });
//    }

}