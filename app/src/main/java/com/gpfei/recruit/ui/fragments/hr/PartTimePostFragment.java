package com.gpfei.recruit.ui.fragments.hr;

import android.content.Intent;
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
import android.widget.LinearLayout;

import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.PostAndUserPartAdapter;
import com.gpfei.recruit.ui.activities.hr.HrCheckUserInfoActivity;
import com.gpfei.recruit.utils.DividerItemDecoration;
import com.gpfei.recruit.utils.SmileToast;
import com.gpfei.recruit.utils.ToastUtils;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class PartTimePostFragment extends Fragment {

    private RecyclerView rRecyclerview;
    private LinearLayout ll_myCollect;
    private PullToRefreshLayout refresh_job;
    private List<Map<String, String>> maps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_part_time_post, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        ll_myCollect = view.findViewById(R.id.ll_myCollect);
        rRecyclerview = view.findViewById(R.id.rRecyclerview);
        refresh_job = view.findViewById(R.id.refresh_job);
        refresh_job.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryAuthor();
                        //结束刷新
                        refresh_job.finishRefresh();
                        SmileToast smileToast = new SmileToast();
                        smileToast.smile("加载完成");
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast(getContext(), "没有更多内容了哟~");
                        //结束加载更多
                        refresh_job.finishLoadMore();
                    }
                }, 2000);
            }
        });
        queryAuthor();


    }

    private void queryAuthor() {

        String url = "http://114.117.0.103:8080/recruit/resume/getbycompany";
        String companyId = (String) getActivity().getIntent().getExtras().get("companyId");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url + "?companyid=" + companyId).build();
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
        maps = new ArrayList<>();
        try {
            Map<String, String> map;
            JSONObject jsonObject = new JSONObject(responsedata);
            int flag = jsonObject.getInt("code");//获取返回值flag的内容
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //第二层解析
                JSONArray items = data.optJSONArray("resume");
                JSONArray job = data.optJSONArray("jobinfo");
                JSONArray userInfo = data.optJSONArray("userinfo");
                //第三层解析
                for (int i = 0; i < items.length(); i++) {
                    map = new HashMap<>();
                    JSONObject object = items.getJSONObject(i);
                    JSONObject jobObject = job.getJSONObject(i);
                    JSONObject userObject = userInfo.getJSONObject(i);
                    String kindPart = jobObject.getString("kind");
                    if (kindPart.equals("兼职")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll_myCollect.setVisibility(View.GONE);
                            }
                        });
                        map.put("title", jobObject.getString("title"));
                        map.put("salary", jobObject.getString("salary"));
                        map.put("place", jobObject.getString("place"));
                        map.put("companyname", jobObject.getString("companyname"));
                        map.put("useid",object.getString("userid"));
                        map.put("name",userObject.getString("name"));
                        map.put("sex",userObject.getString("sex"));
                        map.put("phone",userObject.getString("phone"));
                        map.put("username", userObject.getString("username"));
                        map.put("kind", jobObject.getString("kind"));
                        maps.add(map);
                    }
                }
                Log.d("兼职", maps.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        show();
    }
    private void show() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                PostAndUserPartAdapter adapter = new PostAndUserPartAdapter(getApplicationContext(), maps, "兼职");
                System.out.println(getActivity().getLocalClassName() + "兼职");
                rRecyclerview.setItemAnimator(new DefaultItemAnimator());
                //添加分割线
                rRecyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
                rRecyclerview.setAdapter(adapter);
                adapter.setOnItemClickLitener(new PostAndUserPartAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //点击事件
                        Intent intent = new Intent(getContext(), HrCheckUserInfoActivity.class);
                        intent.putExtra("username", maps.get(position).get("username"));
                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(View view, int pos) {

                    }
                });
            }
        });
    }
}