package com.gpfei.recruit.ui.fragments.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.gpfei.recruit.R;
import com.gpfei.recruit.ui.activities.common.SearchActivity;
import com.gpfei.recruit.ui.fragments.common.home.Fragment1;
import com.gpfei.recruit.ui.fragments.common.home.Fragment2;
import com.gpfei.recruit.ui.fragments.common.home.Fragment3;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private Fragment1 fg1;
    private Fragment2 fg2;
    private Fragment3 fg3;
    private TabLayout my_tab;
    private ViewPager my_viewpager;
    private List<Fragment> fragments;
    private List<String> titles;
    private LinearLayout ll_search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        my_tab = (TabLayout) view.findViewById(R.id.my_tab);
        my_viewpager = (ViewPager) view.findViewById(R.id.my_viewpager);
        fg1 = new Fragment1();
        fg2 = new Fragment2();
        fg3 = new Fragment3();
        //添加fragment
        fragments = new ArrayList<>();
        fragments.add(fg1);
        fragments.add(fg2);
        fragments.add(fg3);
        //添加标题
        titles = new ArrayList<>();
        titles.add("全职");
        titles.add("兼职");
        titles.add("实习");
        MyAdapter adapter = new MyAdapter(getChildFragmentManager());
        my_viewpager.setAdapter(adapter);
        my_tab.setupWithViewPager(my_viewpager);

//        ll_search = view.findViewById(R.id.ll_search);
//        ll_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), SearchActivity.class));
//            }
//        });
    }

    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


}