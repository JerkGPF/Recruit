package com.gpfei.recruit.ui.activities.hr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.MessageActivity;
import com.gpfei.recruit.ui.activities.common.FileWebDetailsActivity;
import com.gpfei.recruit.ui.activities.common.MyDataActivity;
import com.gpfei.recruit.utils.DownloadUtil;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.easeui.EaseConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HrCheckUserInfoActivity extends AppCompatActivity {
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_user_head;
    private TextView tv_name,tv_phone,tv_sex,tv_birth,tv_qq,tv_email,tv_induce,tv_experience;
    private FloatingActionButton fab,check_file,down_file;
    String username;
    String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_check_user_info);
        initView();
        loadData();
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        check_file = findViewById(R.id.check_file);
        down_file = findViewById(R.id.down_file);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("???????????????");
        iv_user_head = findViewById(R.id.iv_user_head);

        tv_phone = findViewById(R.id.tv_phone);
        tv_name = findViewById(R.id.tv_name);
        tv_sex = findViewById(R.id.tv_sex);
        tv_birth = findViewById(R.id.tv_birthday);
        tv_qq = findViewById(R.id.tv_qq);
        tv_email = findViewById(R.id.tv_email);
        tv_induce = findViewById(R.id.tv_induce);
        tv_experience = findViewById(R.id.tv_experience);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //??????
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat = new Intent(HrCheckUserInfoActivity.this, MessageActivity.class);
                chat.putExtra(EaseConstant.EXTRA_USER_ID,username);  //????????????
                chat.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE); //????????????
                startActivity(chat);
            }
        });
        //????????????
        check_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HrCheckUserInfoActivity.this, FileWebDetailsActivity.class).putExtra("fileUrl",fileUrl));
            }
        });
        //????????????
        down_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DownloadUtil.get().download(fileUrl, getDiskCacheDir(HrCheckUserInfoActivity.this).toString(), username+"????????????.doc", new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HrCheckUserInfoActivity.this, "????????????,????????????:"+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.d("onDownloadSuccess: ",file.getName());
                                Log.d("onDownloadSuccess: ",file.getPath());
                            }

                            @Override
                            public void onDownloading(int progress) {

                            }

                            @Override
                            public void onDownloadFailed(Exception e) {

                            }
                        });
                    }
                }).start();
            }
        });
    }



    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    private void loadData() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (username !=null){
            new Thread() {
                public void run() {
                    //??????????????????
                    queryOne(username);
                }
            }.start();
        }
    }


    private void queryOne(String mObjectId) {
        String getUrl = "http://114.117.0.103:8080/recruit/userinfo/getUserInfo";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getUrl+"?username="+mObjectId).build();
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
            int flag = jsonObject.getInt("code");//???????????????flag?????????
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //???????????????
                JSONArray items = data.optJSONArray("data");
                //???????????????
                for (int i =0;i<items.length();i++){
                    JSONObject object = items.getJSONObject(i);
                    username = object.getString("username");
                    fileUrl = object.getString("file");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (fileUrl.equals("")){
                                check_file.setVisibility(View.GONE);
                                down_file.setVisibility(View.GONE);
                                ToastUtils.showTextToast(HrCheckUserInfoActivity.this,"??????????????????????????????~");
                            }
                            try {
                                tv_name.setText(object.getString("name"));
                                tv_phone.setText(object.getString("phone"));
                                tv_email.setText(object.getString("email"));
                                tv_experience.setText(object.getString("experience"));
                                tv_induce.setText(object.getString("profile"));
                                tv_qq.setText(object.getString("qq"));
                                if (object.getString("sex").equals("M")){
                                    tv_sex.setText("???");
                                }else {
                                    tv_sex.setText("???");
                                }
                                tv_birth.setText(object.getString("birthday"));

                                String head = object.getString("head");
                                //????????????
                                Glide.with(HrCheckUserInfoActivity.this).load(head).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_user_head) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(HrCheckUserInfoActivity.this.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        iv_user_head.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
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


}