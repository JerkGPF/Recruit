package com.gpfei.recruit.ui.activities.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.DayAdapter;
import com.gpfei.recruit.beans.DayBean;
import com.gpfei.recruit.beans.SelectAndResume;
import com.gpfei.recruit.beans.User;
import com.gpfei.recruit.ui.fragments.common.FullTimeFragment;
import com.gpfei.recruit.ui.fragments.common.HomeFragment;
import com.gpfei.recruit.ui.fragments.common.PartTimeFragment;
import com.gpfei.recruit.ui.fragments.common.PracticeFragment;
import com.gpfei.recruit.ui.fragments.common.home.Fragment1;
import com.gpfei.recruit.ui.fragments.common.home.Fragment2;
import com.gpfei.recruit.ui.fragments.common.home.Fragment3;
import com.gpfei.recruit.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyApplyActivity extends AppCompatActivity {
    private TabLayout my_tab;
    private ViewPager my_viewpager;
    private List<Fragment> fragments;
    private List<String> titles;
    private FullTimeFragment fragment_full;
    private PartTimeFragment fragment_part;
    private PracticeFragment fragment_practice;
    FragmentPagerAdapter fragmentPagerAdapter;
    private ImageView iv_back;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);
        initView();
    }
    private void initView() {
        my_tab = findViewById(R.id.my_tab);
        my_viewpager = findViewById(R.id.my_viewpager);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("我的投递");
        fragment_full = new FullTimeFragment();
        fragment_part = new PartTimeFragment();
        fragment_practice = new PracticeFragment();
        //添加fragment
        fragments = new ArrayList<>();
        fragments.add(fragment_full);
        fragments.add(fragment_part);
        fragments.add(fragment_practice);
        //添加标题
        titles = new ArrayList<>();
        titles.add("全职");
        titles.add("兼职");
        titles.add("实习");

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        };

        my_viewpager.setAdapter(fragmentPagerAdapter);
        my_tab.setupWithViewPager(my_viewpager);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}