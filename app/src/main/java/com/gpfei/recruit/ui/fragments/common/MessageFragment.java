package com.gpfei.recruit.ui.fragments.common;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.adapters.FindsAadapter;
import com.gpfei.recruit.adapters.MessageAdapter;
import com.gpfei.recruit.ui.activities.MessageActivity;
import com.gpfei.recruit.ui.activities.common.HelpActivity;
import com.gpfei.recruit.ui.activities.common.MyIntegralActivity;
import com.gpfei.recruit.beans.Message;
import com.gpfei.recruit.utils.ToastUtils;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends EaseConversationListFragment{

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        initData();


    }

    private void initData() {
        // run in a second
        final long timeInterval = 1000;
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    // ------- code for task to run
                    conversationListView.refresh();
                    // ------- ends here
                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


}
