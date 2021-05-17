package com.gpfei.recruit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gpfei.recruit.R;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.List;
import java.util.Map;


public class FindsAadapter extends RecyclerView.Adapter<com.gpfei.recruit.adapters.FindsAadapter.MyViewHolder> implements View.OnClickListener {
    private Context content;
    private List<Map<String,String>> mapList;
    /**
     * 点击事件
     * @param context
     * @param datalist
     */
    private OnItemClickListener mItemClickListener=null;

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public FindsAadapter(Context context, List<Map<String,String>> mapList) {
        this.content=context;
        this.mapList=mapList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_find,parent,false);
        //为每个item添加监听事件
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_title.setText(mapList.get(position).get("title"));
        holder.tv_content.setText(mapList.get(position).get("content"));
        holder.tv_time.setText(mapList.get(position).get("createdtime"));
        holder.tv_count_finds.setText(mapList.get(position).get("count")+"人看过");
        Glide.with(content).load(mapList.get(position).get("image")).error(R.drawable.load_bg).into(holder.iv_image);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_count_finds;
        private RoundedImageView iv_image;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content=(TextView)itemView.findViewById(R.id.tv_content_find);
            tv_title=(TextView)itemView.findViewById(R.id.tv_title_find);
            tv_time=(TextView)itemView.findViewById(R.id.tv_time_find);
            tv_count_finds=(TextView)itemView.findViewById(R.id.tv_count_finds);
            iv_image=(RoundedImageView) itemView.findViewById(R.id.iv_image_find);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
