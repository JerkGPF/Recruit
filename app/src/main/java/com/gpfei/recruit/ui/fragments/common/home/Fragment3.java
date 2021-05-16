package com.gpfei.recruit.ui.fragments.common.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.DayAdapter;
import com.gpfei.recruit.adapters.SelectionAdapter;
import com.gpfei.recruit.beans.PartAndResume;
import com.gpfei.recruit.beans.PracticeAndResume;
import com.gpfei.recruit.beans.SelectionBean;
import com.gpfei.recruit.beans.User;
import com.gpfei.recruit.beans.WeekendBean;
import com.gpfei.recruit.ui.activities.common.JobWebDetailsActivity;
import com.gpfei.recruit.utils.DividerItemDecoration;
import com.gpfei.recruit.utils.ToastUtils;
import com.longsh.optionframelibrary.OptionCenterDialog;
import com.mob.MobSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Fragment3 extends Fragment implements View.OnClickListener {
     private RecyclerView rRecyclerview;
    private RelativeLayout rl_load_view3;
    private Button btn_load3;
    private RelativeLayout rl_network_error3;

    private List<Map<String,String>> maps;
    private SharedPreferences sp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        initView(view);
        getJobInfo();
        return view;
    }

    private void initView(View view) {
        rRecyclerview =  view.findViewById(R.id.sRecyclerview);
        rl_load_view3 = view.findViewById(R.id.rl_load_view3);
        btn_load3 =  view.findViewById(R.id.btn_load3);
        sp = getActivity().getSharedPreferences("allInfo", MODE_PRIVATE);

        btn_load3.setOnClickListener(this);
        rl_network_error3 =  view.findViewById(R.id.rl_network_error3);
    }


    private void getJobInfo() {
        String getUrl = "http://114.117.0.103:8080/recruit/jobinfo/findAll";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(getUrl+"?kind=实习").build();
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
                    map.put("companyname",object.getString("companyname"));
                    map.put("createtime",object.getString("createtime"));
                    map.put("id",object.getString("id"));//jobId
                    map.put("username",object.getString("username"));
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
        rRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        DayAdapter adapter = new DayAdapter(getContext(), maps,"实习");
        rRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        rRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rRecyclerview.setAdapter(adapter);
        //隐藏加载view
        rl_load_view3.setVisibility(View.GONE);
        rl_network_error3.setVisibility(View.GONE);
        adapter.setOnItemClickLitener(new DayAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //点击事件
                Intent intent = new Intent(getContext(), JobWebDetailsActivity.class);
                intent.putExtra("url", maps.get(position).get("detail"));
                intent.putExtra("objectId", maps.get(position).get("username"));
                intent.putExtra("title", maps.get(position).get("title"));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int pos) {
                //长按弹出列表提示框
                final ArrayList<String> list = new ArrayList<>();
                list.add("分享");
                list.add("投递");
                list.add("收藏");
                list.add("取消");
                final OptionCenterDialog optionCenterDialog = new OptionCenterDialog();
                optionCenterDialog.show(getContext(), list);
                optionCenterDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String userid = sp.getString("userid",null);

                        switch (position) {
                            case 0:
                                String url = maps.get(pos).get("detail");
                                String title = maps.get(pos).get("title");
                                share(url,title);
                                break;
                            case 1:
                                setMessage(maps.get(pos).get("id"), userid,true, false,"投递");
                                break;
                            case 2:
                                setMessage(maps.get(pos).get("id"), userid,false, true,"收藏");
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
    private void share(String url, String title) {
        OnekeyShare oks = new OnekeyShare();
        // title标题，微信、QQ和QQ空间等平台使用
        //oks.setTitle(getString(R.string.share));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title+"招聘！");
        // setImageUrl是网络图片的url
        //oks.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
        // url在微信、Facebook等平台中使用
        oks.setUrl(url);
        // 启动分享GUI
        oks.show(MobSDK.getContext());
    }

    private void setMessage(String id, String userid, boolean b, boolean b1,String str) {
        String url = "http://114.117.0.103:8080/recruit/resume/insert";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("userId", userid);
        formBodyBuilder.add("jobId",id);
        formBodyBuilder.add("delievery", String.valueOf(b));
        formBodyBuilder.add("collect", String.valueOf(b1));
        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responsedata = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responsedata);
                    int flag = jsonObject.getInt("code");//获取返回值flag的内容
                    if (flag == 100) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showImageToast(getContext(), str + "成功");
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load3:
                btn_load3.setText("加载中...");
                break;
        }
    }
}