package com.gpfei.recruit.ui.fragments.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.SelectionAdapter;
import com.gpfei.recruit.ui.activities.common.JobWebDetailsActivity;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class FullTimeCollectFragment extends Fragment {
    private RecyclerView rRecyclerview;
    private LinearLayout ll_myCollect;
    private PullToRefreshLayout refresh_job;
    private List<Map<String, String>> maps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_time_collect, container, false);
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
                        equal();
                        //????????????
                        refresh_job.finishRefresh();
                        SmileToast smileToast = new SmileToast();
                        smileToast.smile("????????????");
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast(getContext(), "????????????????????????~");
                        //??????????????????
                        refresh_job.finishLoadMore();
                    }
                }, 2000);
            }
        });
        equal();

    }

    private void equal() {
        String url = "http://114.117.0.103:8080/recruit/resume/getbyusernameCollect";
        String userid = (String) getActivity().getIntent().getExtras().get("userId");
        System.out.println("userId" + userid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url + "?userid=" + userid).build();
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
            int flag = jsonObject.getInt("code");//???????????????flag?????????
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //???????????????
                JSONArray items = data.optJSONArray("resume");
                JSONArray job = data.optJSONArray("jobinfo");
                JSONArray companyInfo = data.optJSONArray("companyinfo");
                //???????????????
                for (int i = 0; i < items.length(); i++) {
                    map = new HashMap<>();
                    JSONObject resume = items.getJSONObject(i);
                    JSONObject jobObject = job.getJSONObject(i);
                    JSONObject companyObject = companyInfo.getJSONObject(i);
                    String kindFull = jobObject.getString("kind");
                    if (kindFull.equals("??????")) {
                        map.put("title", jobObject.getString("title"));
                        map.put("salary", jobObject.getString("salary"));
                        map.put("place", jobObject.getString("place"));
                        map.put("companyname", jobObject.getString("companyname"));
                        map.put("count", jobObject.getString("count"));
                        map.put("isdelete", resume.getString("isdelete"));
                        map.put("resumeId", resume.getString("id"));
                        map.put("detail", jobObject.getString("detail"));
                        map.put("username", companyObject.getString("username"));
                        map.put("kind", jobObject.getString("kind"));
                        maps.add(map);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ll_myCollect.setVisibility(View.GONE);
                            }
                        });
                    }
                }
                Log.d("??????", maps.toString());
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
                SelectionAdapter adapter = new SelectionAdapter(getApplicationContext(), maps);
                rRecyclerview.setItemAnimator(new DefaultItemAnimator());
                //???????????????
                rRecyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
                rRecyclerview.setAdapter(adapter);
                //?????????
                adapter.setOnItemClickLitener(new SelectionAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Boolean isFlage = Boolean.valueOf(maps.get(position).get("isdelete"));
                        if (isFlage) {
                            System.out.println("???????????????");
                            ToastUtils.showTextToast(getActivity(), "?????????????????????????????????");
                        } else {
                            //????????????
                            Intent intent = new Intent(getContext(), JobWebDetailsActivity.class);
                            intent.putExtra("url", maps.get(position).get("detail"));
                            intent.putExtra("title", maps.get(position).get("title"));
                            intent.putExtra("objectId", maps.get(position).get("username"));
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                        //???????????????????????????
                        String yes = "<font color='#2EC667'>" + "???" + "</font>";
                        String no = "<font color='#2EC667'>" + "???" + "</font>";
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setMessage("????????????????????????")//????????????????????????
                                //????????????????????????
                                .setNegativeButton(Html.fromHtml(no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(Html.fromHtml(yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //???????????????????????????
                                        String resumeId = maps.get(position).get("resumeId");
                                        handle(resumeId);
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                });
            }
        });
    }


    private void handle(String id) {
        String url = "http://114.117.0.103:8080/recruit/resume/updateResume";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("resId", id);
        formBodyBuilder.add("collect", String.valueOf(false));
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
                equal();
            }
        });
    }


}