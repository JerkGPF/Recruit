package com.gpfei.recruit.ui.fragments.common;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.MyUser;
import com.gpfei.recruit.beans.SelectAndResume;
import com.gpfei.recruit.beans.SignInBean;
import com.gpfei.recruit.beans.User;
import com.gpfei.recruit.ui.activities.common.AboutActivity;
import com.gpfei.recruit.ui.activities.common.FeedBackActivity;
import com.gpfei.recruit.ui.activities.common.HelpActivity;
import com.gpfei.recruit.ui.activities.common.MyApplyActivity;
import com.gpfei.recruit.ui.activities.common.MyAttentionActivity;
import com.gpfei.recruit.ui.activities.common.MyCollectActivity;
import com.gpfei.recruit.ui.activities.common.MyDataActivity;
import com.gpfei.recruit.ui.activities.common.MyFileActivity;
import com.gpfei.recruit.ui.activities.common.MyInfoActivity;
import com.gpfei.recruit.ui.activities.common.MyIntegralActivity;
import com.gpfei.recruit.ui.activities.common.SettingActivity;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.ui.activities.hr.PostActivity;
import com.gpfei.recruit.utils.SmileToast;
import com.gpfei.recruit.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class UserFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_user_head;
    private RelativeLayout rl_menu_item1;
    private RelativeLayout rl_menu_item2;
    private RelativeLayout rl_menu_item3;
    private RelativeLayout rl_menu_item4;
    private RelativeLayout rl_menu_item5;
    private RelativeLayout rl_menu_item6;
    private RelativeLayout rl_menu_item_jianli;
    private LinearLayout ll_menu1;
    private LinearLayout ll_menu2;
    private LinearLayout ll_menu3;
    private LinearLayout ll_menu4;
    private TextView tv_username;
    private TextView tv_motto;
    private RelativeLayout rl_user;
    private LinearLayout ll_class;
    private static final int INFO_CODE = 1;

    int intergal;
    String updatedAt;
    String url = "http://114.117.0.103:8080/recruit/userinfo/getUserInfo";
    private SharedPreferences sharedPreferences;

    String username;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);

        return view;
    }


    private void initView(View view) {
        //rl_menu_item1 = view.findViewById(R.id.rl_menu_item1);
        //rl_menu_item1.setOnClickListener(this);
        rl_menu_item2 = view.findViewById(R.id.rl_menu_item2);
        rl_menu_item2.setOnClickListener(this);
        rl_menu_item3 = view.findViewById(R.id.rl_menu_item3);
        rl_menu_item3.setOnClickListener(this);
        rl_menu_item4 = view.findViewById(R.id.rl_menu_item4);
        rl_menu_item4.setOnClickListener(this);
        rl_menu_item5 = view.findViewById(R.id.rl_menu_item5);
        rl_menu_item5.setOnClickListener(this);
        rl_menu_item6 = view.findViewById(R.id.rl_menu_item6);
        rl_menu_item6.setOnClickListener(this);
        rl_menu_item_jianli = view.findViewById(R.id.rl_menu_item_jianli);
        rl_menu_item_jianli.setOnClickListener(this);
        ll_menu1 = view.findViewById(R.id.ll_menu1);
        ll_menu1.setOnClickListener(this);
        ll_menu2 = view.findViewById(R.id.ll_menu2);
        ll_menu2.setOnClickListener(this);
        ll_menu3 = view.findViewById(R.id.ll_menu3);
        ll_menu3.setOnClickListener(this);
        ll_menu4 = view.findViewById(R.id.ll_menu4);
        ll_menu4.setOnClickListener(this);
        tv_username = view.findViewById(R.id.tv_username);
        iv_user_head = view.findViewById(R.id.iv_user_head);
        tv_motto = view.findViewById(R.id.tv_motto_user);
        rl_user = view.findViewById(R.id.rl_user);
        rl_user.setOnClickListener(this);
        ll_class = view.findViewById(R.id.ll_class);

        sharedPreferences = getActivity().getSharedPreferences("userinfo",MODE_PRIVATE);


        showInfo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                queryIntegral();
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        showInfo();

    }

    private void showInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                username = sharedPreferences.getString("username",null);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String name = object.getString("name");
                            String head = object.getString("head");
                            String motto = object.getString("motto");
                            userId = object.getString("id");
                            System.out.println("userId:"+userId);
                            getInfo(name,head,motto);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Log.d("s","失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getInfo(String name, String head, String motto) {
        //获取头像地址
        if (head != null&& !(TextUtils.isEmpty(head))) {
            //圆形头像
            Glide.with(getActivity()).load(head).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_user_head) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    iv_user_head.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else {
            iv_user_head.setImageResource(R.mipmap.icon_user_head);
        }
        //获取用户名
        tv_username.setText(name);
        //获取个性签名
        tv_motto.setText(motto);
        //显示等级
        ll_class.setVisibility(View.VISIBLE);
    }

    //查询出积分
    private void queryIntegral() {
        String sigUrl = "http://114.117.0.103:8080/recruit/sign/getSignInfo";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(sigUrl+"?username="+username).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    jx(responsedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void jx(String responsedata) {
        try {
            JSONObject jsonObject = new JSONObject(responsedata);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //第二层解析
                JSONArray items = data.optJSONArray("data");
                //第三层解析
                JSONObject object = items.getJSONObject(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            intergal = Integer.parseInt(object.getString("intergal"));//签到积分
                            updatedAt = object.getString("updateTime");//签到时间
                            updatedAt = updatedAt.substring(0,updatedAt.indexOf(" "));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Log.d("s","失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user:
                //判断登录是否为空
                if (username != null) {
                    Intent intent = new Intent(getContext(), MyInfoActivity.class);
                    startActivityForResult(intent, INFO_CODE);
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                }
                break;
            case R.id.ll_menu1:
                if (username != null) {
                    startActivity(new Intent(getContext(), MyDataActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.ll_menu2:
                if (username != null) {
                    System.out.println("useID:"+userId);
                    startActivity(new Intent(getContext(), MyApplyActivity.class).putExtra("userId",userId));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.ll_menu3:
                if (username != null) {
                    startActivity(new Intent(getContext(), MyIntegralActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.ll_menu4://签到
                if (username != null) {
                    updateIntergal();
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.rl_menu_item2:
                if (username != null) {
                    startActivity(new Intent(getContext(), MyCollectActivity.class).putExtra("userId",userId));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.rl_menu_item_jianli:
                if (username != null) {
                    startActivity(new Intent(getContext(), MyFileActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.rl_menu_item3:
                startActivity(new Intent(getContext(), FeedBackActivity.class));
                break;
            case R.id.rl_menu_item4:
                Intent intent4 = new Intent(getContext(), HelpActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_menu_item5:
                Intent intent5 = new Intent(getContext(), AboutActivity.class);
                startActivity(intent5);
                break;
            case R.id.rl_menu_item6:
                Intent intent6 = new Intent(getContext(), SettingActivity.class);
                startActivity(intent6);
                break;
            default:
                break;
        }

    }

    //更新签到信息
    private void updateIntergal() {
        //添加当前时间
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String createTime = dateFormat.format(now);//格式化然后放入字符串中
        System.out.println(createTime);
        System.out.println(updatedAt.equals(createTime));
        if (updatedAt.equals(createTime)&&intergal!=0){
            ToastUtils.showImageToast(getActivity(),"已经签到过了！");
        }
        if (updatedAt.equals(createTime)&&intergal == 0){
            intergal +=2;
            toSign(intergal);
        }
        if (!updatedAt.equals(createTime)&& intergal != 0){
            intergal +=2;
            toSign(intergal);
        }
    }
    private void toSign(int intergal) {
        String upSign = "http://114.117.0.103:8080/recruit/sign/updateSign";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add("username", username);
                formBodyBuilder.add("intergal", String.valueOf(intergal));
                Request request = new Request.Builder()
                        .url(upSign)
                        .post(formBodyBuilder.build())
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                signIn();
                            }
                        });
                    }
                });

            }
        }).start();
    }

    private void signIn() {
        final Dialog dialog = new Dialog(UserFragment.this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(UserFragment.this.getContext()).inflate(R.layout.dialog_sign, null);
        dialog.setContentView(contentView);
        Button cancel = contentView.findViewById(R.id.submit_bt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryIntegral();
                dialog.dismiss();
            }
        });
        //背景透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        dialog.setCanceledOnTouchOutside(true);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == INFO_CODE) {
//            //获取用户名
//            tv_username.setText(user.getNick().toString());
//            //获取个性签名
//            tv_motto.setText(user.getMotto().toString());
//
//        }
//    }
}