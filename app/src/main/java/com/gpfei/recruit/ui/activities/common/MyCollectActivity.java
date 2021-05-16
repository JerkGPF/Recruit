package com.gpfei.recruit.ui.activities.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.fragments.common.FullTimeCollectFragment;
import com.gpfei.recruit.ui.fragments.common.PartTimeColloectFragment;
import com.gpfei.recruit.ui.fragments.common.PracticeCollectFragment;

import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends AppCompatActivity {


    private TabLayout my_tab;
    private ViewPager my_viewpager;
    private List<Fragment> fragments;
    private List<String> titles;
    private FullTimeCollectFragment fullTimeCollectFragment;
    private PracticeCollectFragment practiceCollectFragment;
    private PartTimeColloectFragment partTimeColloectFragment;

    FragmentPagerAdapter fragmentPagerAdapter;
    private ImageView iv_back;
    private TextView tv_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initView();

    }
    private void initView() {
        my_tab = findViewById(R.id.my_tab);
        my_viewpager = findViewById(R.id.my_viewpager);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);

        tv_title.setText("我的收藏");


        fullTimeCollectFragment = new FullTimeCollectFragment();
        partTimeColloectFragment = new PartTimeColloectFragment();
        practiceCollectFragment = new PracticeCollectFragment();

        //添加fragment
        fragments = new ArrayList<>();
        fragments.add(fullTimeCollectFragment);
        fragments.add(partTimeColloectFragment);
        fragments.add(practiceCollectFragment);
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