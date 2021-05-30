package com.gpfei.recruit.ui.fragments.common;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessageFragment extends EaseConversationListFragment{


    int number = 0;

    private MyListener myListener;//②作为属性定义
    //①定义回调接口
    public interface MyListener{
        public void sendContent(int info);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myListener = (MyListener) getActivity();

    }

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        initData();
    }

    private void initData() {
        // run in a second
        final long timeInterval = 500;
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

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                initfor(conversationList);
                //显示数量至下标
                myListener.sendContent(number);//将内容进行回传
                Log.d("number", "run: unreadnumber :" + number);
                System.out.println("unreadnumber"+number);
                number = 0;
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,0,500);

    }

    private void initfor(List<EMConversation> conversationList) {
        for (int i = 0; i < conversationList.size();i++){
            EMConversation conversation = getItem(conversationList,i);
            number = number + conversation.getUnreadMsgCount();
        }
    }

    public EMConversation getItem(List<EMConversation> conversationLists,int arg0) {
        if (arg0 < conversationLists.size()) {
            return conversationLists.get(arg0);
        }
        return null;
    }



}
