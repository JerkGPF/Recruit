package com.gpfei.recruit.ui.activities.common;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.MessageActivity;
import com.gpfei.recruit.ui.fragments.common.FindFragment;
import com.gpfei.recruit.ui.fragments.common.HomeFragment;
import com.gpfei.recruit.ui.fragments.common.MessageFragment;
import com.gpfei.recruit.ui.fragments.common.UserFragment;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;


import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, MessageFragment.MyListener {
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private RadioGroup rg_bottom_bar;
    private HomeFragment homeFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private UserFragment userFragment;
    private Button btn;//红点显示

    QBadgeView badge;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        //初始化控件
        rg_bottom_bar = findViewById(R.id.rg_bottom_bar);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        btn= findViewById(R.id.bt);


        //设置大小比例
        Drawable dr1=getResources().getDrawable(R.drawable.bottom_bar_home_selector);
        Drawable dr2=getResources().getDrawable(R.drawable.bottom_bar_find_selector);
        Drawable dr3=getResources().getDrawable(R.drawable.bottom_bar_message_selector);
        Drawable dr4=getResources().getDrawable(R.drawable.bottom_bar_my_selector);
        //改变图片的比例大小
        Rect r1 = new Rect(0, 0, dr1.getMinimumWidth() * 2 / 9, dr1.getMinimumHeight() * 2 / 9);
        Rect r2 = new Rect(0, 0, dr2.getMinimumWidth() * 2 / 9, dr2.getMinimumHeight() * 2 / 9);
        Rect r3 = new Rect(0, 0, dr3.getMinimumWidth() * 2 / 9, dr3.getMinimumHeight() * 2 / 9);
        Rect r4 = new Rect(0, 0, dr4.getMinimumWidth() * 2 / 9, dr4.getMinimumHeight() * 2 / 9);
        dr1.setBounds(r1);
        dr2.setBounds(r2);
        dr3.setBounds(r3);
        dr4.setBounds(r4);
        rb1.setCompoundDrawables(null,dr1,null,null);
        rb2.setCompoundDrawables(null,dr2,null,null);
        rb3.setCompoundDrawables(null,dr3,null,null);
        rb4.setCompoundDrawables(null,dr4,null,null);

        //注册RadioGroup的事件监听
        rg_bottom_bar.setOnCheckedChangeListener(this);
        rb1.setChecked(true);


        badge = (QBadgeView) new QBadgeView(MainActivity.this).bindTarget(btn);
        badge.setShowShadow(false);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        //创建碎片的事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //先隐藏掉所以的碎片
        hideAllFragmen(fragmentTransaction);
        switch (checkedId){
            case R.id.rb1:
                if (homeFragment==null){
                    homeFragment=new HomeFragment();
                    fragmentTransaction.add(R.id.fl_content,homeFragment);
                }else {
                    fragmentTransaction.show(homeFragment);
                }
                break;
            case R.id.rb2:
                if (findFragment==null){
                    findFragment=new FindFragment();
                    fragmentTransaction.add(R.id.fl_content,findFragment);
                }else {
                    fragmentTransaction.show(findFragment);
                }
                break;
            case R.id.rb3:

                if (messageFragment==null){

                    messageFragment=new MessageFragment();
                    messageFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                        @Override
                        public void onListItemClicked(EMConversation conversation) {
                            startActivity(new Intent(getApplicationContext(), MessageActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId()));
                        }
                    });
                    fragmentTransaction.add(R.id.fl_content,messageFragment);
                }else {
                    fragmentTransaction.show(messageFragment);
                }
                break;
            case R.id.rb4:
                if (userFragment ==null){
                    userFragment =new UserFragment();
                    fragmentTransaction.add(R.id.fl_content, userFragment);
                }else {
                    fragmentTransaction.show(userFragment);
                }
                break;
        }

        //提交事务
        fragmentTransaction.commit();

    }

    private void hideAllFragmen(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (findFragment != null) {
            fragmentTransaction.hide(findFragment);
        }
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (userFragment != null) {
            fragmentTransaction.hide(userFragment);
        }
    }
    @Override
    public void sendContent(int info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (info!=0){
                    //btn.setVisibility(View.GONE);
                    System.out.println("info>>>>"+info);
                    badge.setBadgeNumber(info);
                }else {
                    badge.hide(true);
                    System.out.println("info<<<<"+info);
                }
            }
        });
    }
}