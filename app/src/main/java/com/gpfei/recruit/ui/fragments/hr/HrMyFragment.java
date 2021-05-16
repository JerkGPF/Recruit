package com.gpfei.recruit.ui.fragments.hr;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;


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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.DayBean;
import com.gpfei.recruit.beans.HrUser;
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
import com.gpfei.recruit.ui.activities.common.MyInfoActivity;
import com.gpfei.recruit.ui.activities.common.MyIntegralActivity;
import com.gpfei.recruit.ui.activities.common.SettingActivity;
import com.gpfei.recruit.ui.activities.common.login.LoginAndRegisterActivity;
import com.gpfei.recruit.ui.activities.hr.HrDataActivity;
import com.gpfei.recruit.ui.activities.hr.MyPublishActivity;
import com.gpfei.recruit.ui.activities.hr.PostActivity;
import com.gpfei.recruit.ui.fragments.common.UserFragment;
import com.gpfei.recruit.utils.ToastUtils;
import com.gpfei.recruit.utils.UploadUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HrMyFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_user_head;
    private RelativeLayout rl_menu_item1;
    private RelativeLayout rl_menu_item2;
    private RelativeLayout rl_menu_item3;
    private RelativeLayout rl_menu_item4;
    private RelativeLayout rl_menu_item5;
    private RelativeLayout rl_menu_item6;
    private LinearLayout ll_menu1;
    private LinearLayout ll_menu2;
    private LinearLayout ll_menu3;
    private LinearLayout ll_menu4;
    private TextView tv_username;
    private TextView tv_motto;
    private LinearLayout ll_class;
    private static final int INFO_CODE = 1;

    String url = "http://114.117.0.103:8080/recruit/companyinfo/getCompanyInfo";
    private SharedPreferences sharedPreferences;
    String username;


    int intergal;
    String updatedAt;
    String companyId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hr_my, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rl_menu_item2 =  view.findViewById(R.id.rl_menu_item2);
        rl_menu_item2.setOnClickListener(this);
        rl_menu_item3 =  view.findViewById(R.id.rl_menu_item3);
        rl_menu_item3.setOnClickListener(this);
        rl_menu_item4 =  view.findViewById(R.id.rl_menu_item4);
        rl_menu_item4.setOnClickListener(this);
        rl_menu_item5 =  view.findViewById(R.id.rl_menu_item5);
        rl_menu_item5.setOnClickListener(this);
        rl_menu_item6 = view.findViewById(R.id.rl_menu_item6);
        rl_menu_item6.setOnClickListener(this);
        ll_menu1 = view.findViewById(R.id.ll_menu1);
        ll_menu1.setOnClickListener(this);
        ll_menu2 =  view.findViewById(R.id.ll_menu2);
        ll_menu2.setOnClickListener(this);
        ll_menu3 =  view.findViewById(R.id.ll_menu3);
        ll_menu3.setOnClickListener(this);
        ll_menu4 =  view.findViewById(R.id.ll_menu4);
        ll_menu4.setOnClickListener(this);
        tv_username =  view.findViewById(R.id.tv_username);
        iv_user_head =  view.findViewById(R.id.iv_user_head);
        tv_motto =  view.findViewById(R.id.tv_motto_user);
        ll_class =  view.findViewById(R.id.ll_class);
        sharedPreferences = getActivity().getSharedPreferences("allCompanyInfo",MODE_PRIVATE);





        showInfo();
        queryIntegral();
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
                            companyId = object.getString("id");
                            tv_username.setText(name);
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
        String company = sharedPreferences.getString("username",null);
        switch (v.getId()) {
            case R.id.ll_menu1:
                if (company != null) {
                    startActivity(new Intent(getContext(), HrDataActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.ll_menu2:
                if (company != null) {
                    startActivity(new Intent(getContext(), MyPublishActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.ll_menu3:
                if (company != null) {
                    startActivity(new Intent(getContext(), MyIntegralActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.ll_menu4://签到
                if (company != null) {
                    updateIntergal();
                } else {
                    startActivity(new Intent(getContext(), LoginAndRegisterActivity.class));
                    ToastUtils.showTextToast(getContext(), "请先登录!");
                }
                break;
            case R.id.rl_menu_item2:
                if (company != null) {
                    startActivity(new Intent(getContext(), PostActivity.class).putExtra("companyId",companyId));
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
        final Dialog dialog = new Dialog(HrMyFragment.this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView = LayoutInflater.from(HrMyFragment.this.getContext()).inflate(R.layout.dialog_sign, null);
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

}