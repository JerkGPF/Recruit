package com.gpfei.recruit.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.Window;

import com.gpfei.recruit.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseTitleBar;

public class MessageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message);
        initView();
    }

    private void initView() {
        EaseChatFragment easeChatFragment = new EaseChatFragment();  //环信聊天界面
        easeChatFragment.setArguments(getIntent().getExtras()); //需要的参数
        getSupportFragmentManager().beginTransaction().add(R.id.layout_chat,easeChatFragment).commit();  //Fragment切换

    }


}