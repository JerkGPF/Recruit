package com.gpfei.recruit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.gpfei.recruit.R;

import java.util.List;
import java.util.Map;


public class SelectionAdapter extends RecyclerView.Adapter<com.gpfei.recruit.adapters.SelectionAdapter.MyViewHolder> {
    private Context context;
    private List<Map<String,String>> mapList;


    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public SelectionAdapter(Context context, List<Map<String,String>> mapList) {
        this.context=context;
        this.mapList=mapList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_selection,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_title_selection.setText(mapList.get(position).get("title"));
        holder.tv_address_selection.setText(mapList.get(position).get("place"));
        holder.tv_money_selection.setText(mapList.get(position).get("salary") + "");
        holder.tv_company_selection.setText(mapList.get(position).get("companyname"));
        holder.tv_time_selection.setText(mapList.get(position).get("createtime"));
        holder.tv_count_s.setText((mapList.get(position).get("count") + "人看过"));

        boolean isFlage = Boolean.parseBoolean(mapList.get(position).get("isdelete"));

        if (isFlage){
            holder.tv_delivery_day.setText("已删除");
        }else {
            holder.tv_delivery_day.setText("已收藏");
        }


        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_title_selection;
        private TextView tv_money_selection;
        private TextView tv_time_selection;
        private TextView tv_address_selection;
        private TextView tv_company_selection;
        private TextView tv_count_s;
        private TextView tv_delivery_day;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title_selection=itemView.findViewById(R.id.tv_title_selection);
            tv_time_selection=itemView.findViewById(R.id.tv_time_selection);
            tv_money_selection=itemView.findViewById(R.id.tv_money_selection);
            tv_address_selection=itemView.findViewById(R.id.tv_address_selection);
            tv_company_selection=itemView.findViewById(R.id.tv_company_selection);
            tv_count_s=itemView.findViewById(R.id.tv_count_s);
            tv_delivery_day = itemView.findViewById(R.id.tv_delivery_day);

        }
    }


}
