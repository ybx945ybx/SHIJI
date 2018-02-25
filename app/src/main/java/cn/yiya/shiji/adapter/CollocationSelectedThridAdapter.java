package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HotCategroyThridObject;

/**
 * Created by Tom on 2016/7/26.
 */
public class CollocationSelectedThridAdapter extends RecyclerView.Adapter<CollocationSelectedThridAdapter.
        CollocationSelectedThridViewHolder> {

    private Context mContext;
    private List<HotCategroyThridObject.ThridListEntity> mList = new ArrayList<>();
    private int id;
    private OnItemClickListener onItemClickListener;

    public CollocationSelectedThridAdapter (Context mContext, int id){
        this.mContext = mContext;
        this.id = id;
    }

    public List<HotCategroyThridObject.ThridListEntity> getmList() {
        return mList;
    }

    public void setmList(List<HotCategroyThridObject.ThridListEntity> mList) {
        this.mList = mList;
    }

    @Override
    public CollocationSelectedThridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollocationSelectedThridViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collocation_selected_second_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CollocationSelectedThridViewHolder holder, int position) {
        final HotCategroyThridObject.ThridListEntity thridListEntity = mList.get(position);

        holder.tvThrid.setText(thridListEntity.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(id, thridListEntity.getId(), thridListEntity.getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnItemClickListener{
        void OnItemClick(int id, int categroy_id, String name);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class CollocationSelectedThridViewHolder extends RecyclerView.ViewHolder{

        TextView tvThrid;

        public CollocationSelectedThridViewHolder(View itemView) {
            super(itemView);
            tvThrid = (TextView) itemView.findViewById(R.id.tv_second);
        }
    }
}

