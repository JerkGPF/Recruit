package com.gpfei.recruit.ui.fragments.hr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.HRIndexAdapter;
import com.gpfei.recruit.beans.MyUser;
import com.gpfei.recruit.ui.activities.hr.HrCheckUserInfoActivity;
import com.gpfei.recruit.ui.activities.hr.PublishActivity;
import com.gpfei.recruit.utils.DividerItemDecoration;
import com.gpfei.recruit.utils.SmileToast;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HrIndexFragment extends Fragment implements View.OnClickListener {

    String url = "http://114.117.0.103:8080/recruit/companyinfo/getCompanyInfo";
    private SharedPreferences sharedPreferences,sp;
    private SharedPreferences.Editor editor;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private PullToRefreshLayout refresh;
    private List<Map<String,String>> maps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hr_index, container, false);
        initView(view);
        equal();
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rRecyclerview);
        refresh = view.findViewById(R.id.refresh);
        refresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        equal();
                        //结束刷新
                        refresh.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                SmileToast smileToast = new SmileToast();
                smileToast.smile("加载完成");
                refresh.finishLoadMore();
            }
        });
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("userinfo",MODE_PRIVATE);
        sp = getActivity().getSharedPreferences("allCompanyInfo", MODE_PRIVATE);
        editor = sp.edit();
        editor.commit();
        showInfo();
    }

    //显示用户资料
    private void showInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = sharedPreferences.getString("username",null);
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
                            String free = object.getString("free");
                            String birthday = object.getString("birthday");
                            String phone = object.getString("phone");
                            String profile = object.getString("profile");
                            String username = object.getString("username");
                            editor.putString("username",username);
                            editor.putString("phone",phone);
                            editor.putString("head",free);
                            editor.putString("name",name);
                            editor.putString("birthday",birthday);
                            editor.putString("profile",profile);
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void equal() {
        String getUrl = "http://114.117.0.103:8080/recruit/userinfo/getAll";

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getUrl).build();
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

    private void showUser() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HRIndexAdapter adapter = new HRIndexAdapter(getContext(), maps);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new HRIndexAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击事件
                Intent intent = new Intent(getContext(), HrCheckUserInfoActivity.class);
                intent.putExtra("username", maps.get(position).get("username"));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void jx(String responsedata) {
        maps = new ArrayList<>();
        try {
            Map<String,String> map;
            JSONObject jsonObject = new JSONObject(responsedata);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //第二层解析
                JSONArray items = data.optJSONArray("data");
                //第三层解析
                for (int i =0;i<items.length();i++){
                    map = new HashMap<>();
                    JSONObject object = items.getJSONObject(i);
                    map.put("name",object.getString("name"));
                    map.put("sex",object.getString("sex"));
                    map.put("birthday",object.getString("birthday"));
                    map.put("experience",object.getString("experience"));
                    map.put("phone",object.getString("phone"));
                    map.put("username",object.getString("username"));
                    map.put("head",object.getString("head"));
                    maps.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showUser();
            }
        });
    }

}