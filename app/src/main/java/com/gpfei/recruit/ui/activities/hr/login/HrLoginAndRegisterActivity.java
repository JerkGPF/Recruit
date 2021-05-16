package com.gpfei.recruit.ui.activities.hr.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gpfei.recruit.R;

public class HrLoginAndRegisterActivity extends AppCompatActivity {
    private RadioButton rb_login;
    private RadioButton rb_register;
    private RadioGroup rg_login_register;
    private HrLoginFragment hrLoginFragment;
    private HrRegisterFragment hrRegisterFragment;
    private FrameLayout frame_content;
    private FragmentManager fm;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_r_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();
    }
    private void initView() {
        rb_login = (RadioButton) findViewById(R.id.rb_login);
        rb_register = (RadioButton) findViewById(R.id.rb_register);
        rg_login_register = (RadioGroup) findViewById(R.id.rg_login_register);
        frame_content = (FrameLayout) findViewById(R.id.frame_content);
        hrLoginFragment = new HrLoginFragment();
        //默认添加第一项
        fm = getSupportFragmentManager();
        rb_login.setChecked(true);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_content, hrLoginFragment);
        ft.commit();

        rg_login_register.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId) {
                fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //隐藏全部碎片
                hideAllFragment(ft);
                switch (checkedId) {
                    case R.id.rb_login:
                        if (hrLoginFragment == null) {
                            hrLoginFragment = new HrLoginFragment();
                            ft.add(R.id.frame_content, hrLoginFragment);
                        } else {
                            ft.show(hrLoginFragment);
                        }
                        break;
                    case R.id.rb_register:
                        if (hrRegisterFragment == null) {
                            hrRegisterFragment = new HrRegisterFragment();
                            ft.add(R.id.frame_content, hrRegisterFragment);
                        } else {
                            ft.show(hrRegisterFragment);
                        }
                        break;

                }
                ft.commit();
            }
        });

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void hideAllFragment(FragmentTransaction ft) {
        if (hrLoginFragment != null) {
            ft.hide(hrLoginFragment);
        }
        if (hrRegisterFragment != null) {
            ft.hide(hrRegisterFragment);
        }

    }

}