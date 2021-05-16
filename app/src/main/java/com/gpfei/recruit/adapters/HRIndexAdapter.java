package com.gpfei.recruit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gpfei.recruit.R;
import com.gpfei.recruit.beans.DayBean;
import com.gpfei.recruit.beans.MyUser;
import com.gpfei.recruit.ui.activities.common.MyInfoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HRIndexAdapter extends RecyclerView.Adapter<HRIndexAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickLitener mOnItemClickLitener;
    private List<Map<String,String>> mapList;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public HRIndexAdapter(Context context, List<Map<String,String>> mapList) {
        this.context = context;
        this.mapList = mapList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hr_index, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_hr_index_name.setText(mapList.get(position).get("name"));
        if (mapList.get(position).get("sex").equals("M")){
            holder.tv_hr_index_sex.setText("男");
        }else {
            holder.tv_hr_index_sex.setText("女");

        }
        holder.tv_hr_index_age.setText(mapList.get(position).get("birthday"));
        holder.tv_hr_index_phone.setText(mapList.get(position).get("phone"));
        holder.tv_hr_index_exp.setText(mapList.get(position).get("experience"));

        //头像
        Glide.with(context).load(mapList.get(position).get("head")).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.iv_head) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.iv_head.setImageDrawable(circularBitmapDrawable);
            }
        });

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_hr_index_name,
                tv_hr_index_sex, tv_hr_index_exp,
                tv_hr_index_phone,tv_hr_index_age;
        private ImageView iv_head;



        public MyViewHolder(View itemView) {
            super(itemView);
            tv_hr_index_name = itemView.findViewById(R.id.tv_hr_index_name);
            tv_hr_index_sex = itemView.findViewById(R.id.tv_hr_index_sex);
            tv_hr_index_exp = itemView.findViewById(R.id.tv_hr_index_exp);
            tv_hr_index_phone = itemView.findViewById(R.id.tv_hr_index_phone);
            tv_hr_index_age = itemView.findViewById(R.id.tv_hr_index_age);
            iv_head = itemView.findViewById(R.id.iv_hr_index_head);
        }
    }

}
