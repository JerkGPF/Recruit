package com.gpfei.recruit.ui.activities.hr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.MessageActivity;
import com.gpfei.recruit.ui.activities.common.MainActivity;
import com.gpfei.recruit.ui.fragments.common.FindFragment;
import com.gpfei.recruit.ui.fragments.common.HomeFragment;
import com.gpfei.recruit.ui.fragments.common.MessageFragment;
import com.gpfei.recruit.ui.fragments.common.UserFragment;
import com.gpfei.recruit.ui.fragments.hr.HrIndexFragment;
import com.gpfei.recruit.ui.fragments.hr.HrMessageFragment;
import com.gpfei.recruit.ui.fragments.hr.HrMyFragment;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class HrMainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener,MessageFragment.MyListener {
    private RadioButton rb_index, rb_message, rb_my,rb_find;
    private RadioGroup rg_bottom_bar;


    private HrIndexFragment indexFragment;
    private MessageFragment messageFragment;
    private HrMyFragment myFragment;
    private FindFragment findFragment;

    private Button btn;//红点显示

    QBadgeView badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_main);

        initView();
    }


    private void initView() {
        rb_index = findViewById(R.id.rb_index);
        rb_message = findViewById(R.id.rb_message);
        rb_my = findViewById(R.id.rb_my);
        btn= findViewById(R.id.bt);
        rg_bottom_bar = findViewById(R.id.rg_bottom_bar);
        rb_find = findViewById(R.id.rb_find);
        //设置大小比例
        Drawable drIndex = getResources().getDrawable(R.drawable.bottom_bar_home_selector);
        Drawable drMessage = getResources().getDrawable(R.drawable.bottom_bar_message_selector);
        Drawable drMy = getResources().getDrawable(R.drawable.bottom_bar_my_selector);
        Drawable drFind = getResources().getDrawable(R.drawable.bottom_bar_find_selector);

        //改变图片的比例大小
        Rect rIndex = new Rect(0, 0, drIndex.getMinimumWidth() * 2 / 9, drIndex.getMinimumHeight() * 2 / 9);
        Rect rMessage = new Rect(0, 0, drMessage.getMinimumWidth() * 2 / 9, drMessage.getMinimumHeight() * 2 / 9);
        Rect rMy = new Rect(0, 0, drMy.getMinimumWidth() * 2 / 9, drMy.getMinimumHeight() * 2 / 9);
        Rect rFind = new Rect(0, 0, drMy.getMinimumWidth() * 2 / 9, drFind.getMinimumHeight() * 2 / 9);
        drIndex.setBounds(rIndex);
        drMessage.setBounds(rMessage);
        drMy.setBounds(rMy);
        drFind.setBounds(rFind);
        rb_index.setCompoundDrawables(null, drIndex, null, null);
        rb_message.setCompoundDrawables(null, drMessage, null, null);
        rb_my.setCompoundDrawables(null, drMy, null, null);
        rb_find.setCompoundDrawables(null, drFind, null, null);
        //注册RadioGroup的事件监听
        rg_bottom_bar.setOnCheckedChangeListener(this);
        rb_index.setChecked(true);

        badge = (QBadgeView) new QBadgeView(HrMainActivity.this).bindTarget(btn);
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
            case R.id.rb_index:
                if (indexFragment==null){
                    indexFragment=new HrIndexFragment();
                    fragmentTransaction.add(R.id.hr_fraglayout,indexFragment);
                }else {
                    fragmentTransaction.show(indexFragment);
                }
                break;
            case R.id.rb_find:
                if (findFragment==null){
                    findFragment=new FindFragment();
                    fragmentTransaction.add(R.id.hr_fraglayout,findFragment);
                }else {
                    fragmentTransaction.show(findFragment);
                }
                break;
            case R.id.rb_message:
                if (messageFragment==null){
                    messageFragment=new MessageFragment();
                    messageFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                        @Override
                        public void onListItemClicked(EMConversation conversation) {
                            startActivity(new Intent(getApplicationContext(), MessageActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId()));
                        }
                    });
                    fragmentTransaction.add(R.id.hr_fraglayout,messageFragment);
                }else {
                    fragmentTransaction.show(messageFragment);
                }
                break;
            case R.id.rb_my:
                if (myFragment==null){
                    myFragment=new HrMyFragment();
                    fragmentTransaction.add(R.id.hr_fraglayout,myFragment);
                }else {
                    fragmentTransaction.show(myFragment);
                }
                break;
        }
        //提交事务
        fragmentTransaction.commit();

    }

    private void hideAllFragmen(FragmentTransaction fragmentTransaction) {
        if (indexFragment != null) {
            fragmentTransaction.hide(indexFragment);
        }
        if(findFragment!=null){
            fragmentTransaction.hide(findFragment);
        }
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (myFragment != null) {
            fragmentTransaction.hide(myFragment);
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