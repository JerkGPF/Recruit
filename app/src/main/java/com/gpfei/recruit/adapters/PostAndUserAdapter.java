package com.gpfei.recruit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gpfei.recruit.R;

import java.util.List;
import java.util.Map;

public class PostAndUserAdapter extends RecyclerView.Adapter<com.gpfei.recruit.adapters.PostAndUserAdapter.MyViewHolder> {
    private Context context;
    private String string;
    private List<Map<String,String>> mapList;
    private OnItemClickLitener mOnItemClickLitener;


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public PostAndUserAdapter(Context context, List<Map<String,String>> mapList, String string) {
        this.context = context;
        this.mapList = mapList;
        this.string = string;
    }
    public PostAndUserAdapter(Context context,String string) {
        this.context = context;
        this.string = string;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hr_post_user_info, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void setData(List<Map<String,String>> mapList) {
        this.mapList = mapList;
        notifyDataSetChanged();//通知更新
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        System.out.println("holder" + string + "," + mapList);
        holder.tv_title_day.setText(mapList.get(position).get("title"));
        holder.tv_address_day.setText(mapList.get(position).get("place"));
        holder.tv_money_day.setText(mapList.get(position).get("salary"));
        holder.tv_company_day.setText(mapList.get(position).get("companyname"));
        holder.tv_name.setText(mapList.get(position).get("name"));
        holder.tv_phone.setText(mapList.get(position).get("phone"));

        if (mapList.get(position).get("sex").equals("M")){
            holder.tv_sex.setText("男");
        }else {
            holder.tv_sex.setText("女");
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title_day;
        private TextView tv_money_day;
        private TextView tv_name;
        private TextView tv_address_day;
        private TextView tv_company_day;
        private TextView tv_phone;
        private TextView tv_sex;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title_day = itemView.findViewById(R.id.tv_title_day);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_money_day = itemView.findViewById(R.id.tv_money_day);
            tv_address_day = itemView.findViewById(R.id.tv_address_day);
            tv_company_day = itemView.findViewById(R.id.tv_company_day);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_sex = itemView.findViewById(R.id.tv_sex);
        }
    }
}
