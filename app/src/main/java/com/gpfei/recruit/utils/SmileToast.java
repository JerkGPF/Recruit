package com.gpfei.recruit.utils;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gpfei.recruit.R;
import com.gpfei.recruit.utils.view.SmileToastView;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class SmileToast {
    static SmileToastView smileToastView;

    public void smile(String str){
        Toast smileToast=new Toast(getApplicationContext());
        //view布局
        View smileView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_smile,null,false);
        TextView text = smileView.findViewById(R.id.toastMessage);
        text.setText(str);
        //给customToastView增加动画效果
        smileToastView = smileView.findViewById(R.id.smileView);
        smileToastView.startAnim();
        text.setBackgroundResource(R.drawable.shape_toast_smile_text);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        smileToast.setView(smileView);
        smileToast.setDuration(Toast.LENGTH_SHORT);
        smileToast.show();
    }
}
