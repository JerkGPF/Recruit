package com.gpfei.recruit.ui.activities.common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_title;
    private EditText et_feedback;
    private RadioButton rb_feedback1;
    private RadioButton rb_feedback2;
    private RadioButton rb_feedback3;
    private Button btn_commit_feedback;
    private RadioGroup rg_feedback;
    private TextView tv_show_feedback_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("对话产品汪");
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        et_feedback.setOnClickListener(this);
        rb_feedback1 = (RadioButton) findViewById(R.id.rb_feedback1);
        rb_feedback1.setOnClickListener(this);
        rb_feedback2 = (RadioButton) findViewById(R.id.rb_feedback2);
        rb_feedback2.setOnClickListener(this);
        rb_feedback3 = (RadioButton) findViewById(R.id.rb_feedback3);
        rb_feedback3.setOnClickListener(this);
        btn_commit_feedback = (Button) findViewById(R.id.btn_commit_feedback);
        btn_commit_feedback.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rg_feedback = (RadioGroup) findViewById(R.id.rg_feedback);
        tv_show_feedback_class = (TextView) findViewById(R.id.tv_show_feedback_class);
        //设置默认点击项
        rb_feedback1.setChecked(true);

        rg_feedback.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId) {
                switch (checkedId) {
                    case R.id.rb_feedback1:
                        tv_show_feedback_class.setText("产品功能意见");
                        break;
                    case R.id.rb_feedback2:
                        tv_show_feedback_class.setText("投诉建议");
                        break;
                    case R.id.rb_feedback3:
                        tv_show_feedback_class.setText("其他问题");
                        break;
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit_feedback:
                if (TextUtils.isEmpty(et_feedback.getText().toString().trim())) {
                    ToastUtils.showTextToast(FeedBackActivity.this, "请填写反馈信息！");
                } else {
                    setFeedBack();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //添加反馈信息
    private void setFeedBack() {
        String url = "http://114.117.0.103:8080/recruit/feedback/addFeedBackInfo";

        String content = et_feedback.getText().toString();
        String title = tv_show_feedback_class.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                Map<String, Object> map = new HashMap<>();
                map.put("content",content);
                map.put("title",title);
                okHttpUtil.getDataFromePostJson(url,map, new OkHttpUtil.OnCallback() {
                    @Override
                    public void callback(String result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showImageToast(FeedBackActivity.this, "反馈成功！");
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onFailure(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showImageToast(FeedBackActivity.this, "反馈失败！");
                            }
                        });
                    }
                });
            }
        }).start();

    }
}