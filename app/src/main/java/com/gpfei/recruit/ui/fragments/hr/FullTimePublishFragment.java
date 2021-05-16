package com.gpfei.recruit.ui.fragments.hr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.DayAdapter;

import com.gpfei.recruit.ui.activities.hr.JobDetailsActivity;
import com.gpfei.recruit.ui.activities.hr.ModifyActivity;
import com.gpfei.recruit.utils.DividerItemDecoration;
import com.gpfei.recruit.utils.OkHttpUtil;
import com.gpfei.recruit.utils.SmileToast;
import com.gpfei.recruit.utils.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.longsh.optionframelibrary.OptionCenterDialog;

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

import static android.content.Context.MODE_PRIVATE;


public class FullTimePublishFragment extends Fragment {
    private RecyclerView rRecyclerview;
    private LinearLayout ll_myapply;
    private PullToRefreshLayout refresh_job;
    private TextView tv_publish;

    private SharedPreferences sharedPreferences;
    private List<Map<String,String>> maps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_time, container, false);
        initView(view);
        queryFullAuthor();
        return view;
    }

    private void initView(View view) {
        rRecyclerview = view.findViewById(R.id.rRecyclerview);
        ll_myapply = view.findViewById(R.id.ll_myapply);
        refresh_job = view.findViewById(R.id.refresh_job);
        tv_publish = view.findViewById(R.id.tv_publish);
        tv_publish.setText("您还没有发布过岗位哟~");
        refresh_job.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryFullAuthor();
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
                        ToastUtils.showTextToast(getContext(),"没有更多内容了哟~");
                        //结束加载更多
                        refresh_job.finishLoadMore();
                    }
                }, 2000);
            }
        });
        sharedPreferences = getActivity().getSharedPreferences("userinfo",MODE_PRIVATE);

    }

    /**
     * 查询一对一关联，查询当前用户发表的所有帖子
     */
    private void queryFullAuthor() {
        String url = "http://114.117.0.103:8080/recruit/jobinfo/getJob";
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = sharedPreferences.getString("username",null);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url+"?username="+username+"&&kind=全职").build();
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
                    map.put("count",object.getString("count"));
                    map.put("detail",object.getString("detail"));
                    map.put("place",object.getString("place"));
                    map.put("salary",object.getString("salary"));
                    map.put("title",object.getString("title"));
                    map.put("id",object.getString("id"));
                    map.put("companyname",object.getString("companyname"));
                    maps.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showJob();
            }
        });
    }

    private void showJob() {
        ll_myapply.setVisibility(View.GONE);
        rRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        DayAdapter adapter = new DayAdapter(getContext(), maps,"全职");
        rRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        rRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickLitener(new DayAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击事件
                Intent intent = new Intent(getContext(), JobDetailsActivity.class);
                intent.putExtra("url", maps.get(position).get("detail"));
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int pos) {
                //长按弹出列表提示框
                final ArrayList<String> list = new ArrayList<>();
                list.add("修改");
                list.add("删除");
                list.add("取消");
                final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                optionCenterDialog.show(getContext(), list);
                optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //修改
                                Intent intent = new Intent(getContext(), ModifyActivity.class);
                                intent.putExtra("modify", maps.get(pos).get("id"));
                                startActivity(intent);
                                break;
                            case 1:
                                //删除
                                deleteById(pos);
                                break;
                            default:
                                break;
                        }
                        optionCenterDialog.dismiss();
                    }
                });
            }

        });
    }

    //增加关联删除
    private void deleteById(int pos) {
        Map<String,Object> deleteMap = new HashMap<>();
        String deleteUrl = "http://114.117.0.103:8080/recruit/jobinfo/deleteJobById";
        OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
        deleteMap.put("id",maps.get(pos).get("id"));
        deleteMap.put("isdeleted",true);
        okHttpUtil.getDataFromePostJson(deleteUrl, deleteMap, new OkHttpUtil.OnCallback() {
            @Override
            public void callback(String result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        queryFullAuthor();
                        ToastUtils.showTextToast(getActivity(),"删除成功");
                    }

                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


}