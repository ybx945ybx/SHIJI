package cn.yiya.shiji.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CollocationSelectedGoodsActivity;
import cn.yiya.shiji.entity.HotCategoryFirstSecondObject;
import cn.yiya.shiji.fragment.CollocationSelectedGoodsThridFragment;

/**
 * Created by Tom on 2016/7/25.
 */
public class CollocationSelectedSecondAdapter extends RecyclerView.Adapter<CollocationSelectedSecondAdapter.CollocationSelectedSecondViewHolder> {
    private List<HotCategoryFirstSecondObject.ListEntity.SecondListEntity> mList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private String typeName;

    public CollocationSelectedSecondAdapter (Context mContext, List<HotCategoryFirstSecondObject.ListEntity.SecondListEntity> mList,String typeName){
        this.mList = mList;
        this.mContext = mContext;
        this.typeName=typeName;
    }
    @Override
    public CollocationSelectedSecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollocationSelectedSecondViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collocation_selected_second_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CollocationSelectedSecondViewHolder holder, int position) {
        final HotCategoryFirstSecondObject.ListEntity.SecondListEntity secondListEntity = mList.get(position);

        holder.tvSecond.setText(secondListEntity.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((CollocationSelectedGoodsActivity)mContext).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                CollocationSelectedGoodsThridFragment collocationSelectedGoodsThridFragment = new CollocationSelectedGoodsThridFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", secondListEntity.getId());
                bundle.putString("typeName",typeName);
                collocationSelectedGoodsThridFragment.setArguments(bundle);
                Fragment fragment = fm.findFragmentByTag("first_second");
                if(fragment != null){
                    ft.hide(fragment);
                }
                ft.add(R.id.fragment_container, collocationSelectedGoodsThridFragment, "thrid");
                ft.commit();
//                if(onItemClickListener != null){
//                    onItemClickListener.OnItemClick(secondListEntity.getId());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnItemClickListener{
        void OnItemClick(int id);
    }

    class CollocationSelectedSecondViewHolder extends RecyclerView.ViewHolder{

        TextView tvSecond;

        public CollocationSelectedSecondViewHolder(View itemView) {
            super(itemView);
            tvSecond = (TextView) itemView.findViewById(R.id.tv_second);
        }
    }
}
