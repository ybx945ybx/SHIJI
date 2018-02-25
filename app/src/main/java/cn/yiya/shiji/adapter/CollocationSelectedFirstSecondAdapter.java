package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.HotCategoryFirstSecondObject;
import cn.yiya.shiji.views.FullyLinearLayoutManager;


/**
 * Created by Tom on 2016/7/25.
 */
public class CollocationSelectedFirstSecondAdapter extends RecyclerView.Adapter<CollocationSelectedFirstSecondAdapter.
        CollocationSelectedFirstSecondViewHolder> {

    private Context mContext;
    private List<HotCategoryFirstSecondObject.ListEntity> mList = new ArrayList<>();
    private ExpandCollapseListener expandCollapseListener;
    private int clickPosition = -1;
    private int lastClickPosition=-1;
    private boolean[] bVisable;

    public CollocationSelectedFirstSecondAdapter(Context mContext){
        this.mContext = mContext;
    }

    public List<HotCategoryFirstSecondObject.ListEntity> getmList() {
        return mList;
    }

    public void setmList(List<HotCategoryFirstSecondObject.ListEntity> mList) {
        this.mList = mList;
        bVisable = new boolean[mList.size()];
    }

    @Override
    public CollocationSelectedFirstSecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollocationSelectedFirstSecondViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collocation_selected_first_second_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CollocationSelectedFirstSecondViewHolder holder, final int position) {
        HotCategoryFirstSecondObject.ListEntity listEntity = mList.get(position);

        holder.tvFirstGroup.setText(listEntity.getName());
        holder.rycvSecond.setItemAnimator(new DefaultItemAnimator());
        holder.rycvSecond.setLayoutManager(new FullyLinearLayoutManager(mContext));
        CollocationSelectedSecondAdapter secondAdapter = new CollocationSelectedSecondAdapter(mContext, listEntity.getList(),listEntity.getName());
        holder.rycvSecond.setAdapter(secondAdapter);

        if(position == clickPosition){
            if(bVisable[position]){
                holder.rycvSecond.setVisibility(View.GONE);
                bVisable[position] = false;
                holder.ivFirstGroup.setImageResource(R.mipmap.jia);
            }else {
                holder.rycvSecond.setVisibility(View.VISIBLE);
                bVisable[position] = true;
                holder.ivFirstGroup.setImageResource(R.mipmap.jian);
            }
        }else {
            holder.rycvSecond.setVisibility(View.GONE);
            bVisable[position] = false;
            holder.ivFirstGroup.setImageResource(R.mipmap.jia);
        }

        holder.rlytFirstGroup.setTag(position);
        holder.rlytFirstGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastClickPosition = clickPosition;
                clickPosition = (int) v.getTag();
                if(bVisable[clickPosition]){
                    if(expandCollapseListener!=null) expandCollapseListener.Collapse(position);
                }else{
                    if(expandCollapseListener!=null) expandCollapseListener.Expand(position);
                }
                holder.rlytFirstGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        if(clickPosition==lastClickPosition){
                            notifyItemChanged(clickPosition);
                        }else{
                            if(lastClickPosition!=-1){
                               notifyItemChanged(lastClickPosition);
                            }
                            holder.rlytFirstGroup.post(new Runnable(){
                                @Override
                                public void run() {
                                    notifyItemChanged(clickPosition);
                                }

                            });

                        }
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface ExpandCollapseListener{
        void Expand(int position);
        void Collapse(int position);
    }

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener){
        this.expandCollapseListener = expandCollapseListener;
    }

    class CollocationSelectedFirstSecondViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rlytFirstGroup;
        TextView tvFirstGroup;
        ImageView ivFirstGroup;
        RecyclerView rycvSecond;

        public CollocationSelectedFirstSecondViewHolder(View itemView) {
            super(itemView);
            rlytFirstGroup = (RelativeLayout) itemView.findViewById(R.id.rlyt_first_group);
            tvFirstGroup = (TextView) itemView.findViewById(R.id.tv_group);
            ivFirstGroup = (ImageView) itemView.findViewById(R.id.iv_group);
            rycvSecond = (RecyclerView) itemView.findViewById(R.id.rycv_second);
        }
    }
}

