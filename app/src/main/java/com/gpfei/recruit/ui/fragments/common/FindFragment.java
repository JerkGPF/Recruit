package com.gpfei.recruit.ui.fragments.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.FindsAadapter;
import com.gpfei.recruit.ui.activities.common.FindsWebDetailsActivity;
import com.gpfei.recruit.utils.DividerItemDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

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


public class FindFragment extends Fragment implements OnBannerListener,View.OnClickListener {
    private Banner mBanner;
    private ArrayList<String> titles_list;
    private ArrayList<String> images_list;
    private RecyclerView mRecyclerview;
    private LinearLayout ll_load;
    private Button btn_load;
    private RelativeLayout rl_network_error;

    private List<Map<String,String>> mapsBanner;
    private List<Map<String,String>> mapsFind;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        initView(view);
        getData();
        return view;
    }
    private void getData() {
        String getUrl = "http://114.117.0.103:8080/recruit/finds/getAll";
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

    private void jx(String responsedata) {
        mapsBanner = new ArrayList<>();
        mapsFind = new ArrayList<>();
        try {
            Map<String,String> mapBanner;
            Map<String,String> mapFind;
            JSONObject jsonObject = new JSONObject(responsedata);
            int flag = jsonObject.getInt("code");//???????????????flag?????????
            if (flag == 100) {
                JSONObject data = jsonObject.optJSONObject("extend");  //???????????????
                JSONArray items = data.optJSONArray("data");
                //???????????????
                for (int i =0;i<items.length();i++){
                    mapBanner = new HashMap<>();
                    mapFind = new HashMap<>();
                    JSONObject object = items.getJSONObject(i);
                    if (object.getBoolean("isbanner")){
                        mapBanner.put("btitle",object.getString("btitle"));
                        mapBanner.put("bimage",object.getString("bimage"));
                        mapsBanner.add(mapBanner);
                    }else {
                        mapFind.put("title",object.getString("title"));
                        mapFind.put("content",object.getString("content"));
                        mapFind.put("url",object.getString("url"));
                        mapFind.put("image",object.getString("image"));
                        mapFind.put("count",object.getString("count"));
                        mapsFind.add(mapFind);
                    }
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
        //?????????????????????????????????
        titles_list=new ArrayList<>();
        images_list=new ArrayList<>();
        for (int i =0;i<mapsBanner.size();i++){
            titles_list.add(mapsBanner.get(i).get("btitle"));
            images_list.add(mapsBanner.get(i).get("bimage"));
            setBanner(titles_list,images_list);
        }
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerview.setLayoutManager(llm);
        //???????????????
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        //?????????
        FindsAadapter adapter = new FindsAadapter(getContext(), mapsFind);
        mRecyclerview.setAdapter(adapter);
        //????????????view
        ll_load.setVisibility(View.GONE);
        rl_network_error.setVisibility(View.GONE);
        //????????????
        adapter.setmItemClickListener(new FindsAadapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), FindsWebDetailsActivity.class);
                intent.putExtra("url", mapsFind.get(position).get("url"));
                startActivityForResult(intent, 100);
            }
        });
    }


    private void initView(View view) {
        mBanner = view.findViewById(R.id.mBanner);
        mRecyclerview = view.findViewById(R.id.mRecyclerview);
        ll_load = view.findViewById(R.id.ll_load);
        btn_load = view.findViewById(R.id.btn_load);
        btn_load.setOnClickListener(this);
        rl_network_error = view.findViewById(R.id.rl_network_error);
    }

    //???????????????
    private void setBanner(ArrayList<String> titles_list, ArrayList<String> images_list) {
        //??????banner??????
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //????????????????????????????????????????????????
        mBanner.setImageLoader(new MyLoader());
        //????????????????????????
        mBanner.setImages(this.images_list);
        //?????????????????????
        mBanner.setBannerTitles(this.titles_list);
        //????????????????????????
        mBanner.setDelayTime(3000);
        //????????????????????????
        mBanner.setBannerAnimation(Transformer.Default);
        //????????????
        mBanner.isAutoPlay(true);
        //?????????????????????
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //????????????
        mBanner.setOnBannerListener(this);
        //???????????????
        mBanner.start();
    }

    @Override
    public void OnBannerClick(int position) {

    }
    //????????????
    private class MyLoader implements ImageLoaderInterface {
        @Override
        public void displayImage(Context context, Object path, View imageView) {
            Glide.with(context).load(path).into((ImageView) imageView);
        }
        @Override
        public View createImageView(Context context) {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                getData();
                btn_load.setText("?????????...");
                break;
        }
    }
}