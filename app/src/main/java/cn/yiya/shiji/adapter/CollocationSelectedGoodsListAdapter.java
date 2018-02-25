package cn.yiya.shiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/7/21.
 */
public class CollocationSelectedGoodsListAdapter extends RecyclerView.Adapter<CollocationSelectedGoodsListAdapter.
        CollocationSelectedGoodsListViewHolder>{

    private Context mContext;
    private ArrayList<NewGoodsItem> mList;
    private OnItemClickListener onItemClickListener;
    private boolean isSimilar;

    public CollocationSelectedGoodsListAdapter(Context mContext,boolean isSimilar){
        this.mContext = mContext;
        this.isSimilar=isSimilar;
    }

    public ArrayList<NewGoodsItem> getmList() {
        return mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {
        this.mList = mList;
    }

    @Override
    public CollocationSelectedGoodsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollocationSelectedGoodsListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.collocation_selected_goods_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CollocationSelectedGoodsListViewHolder holder, int position) {
        final NewGoodsItem info = mList.get(position);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.framelayout.getLayoutParams();
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 28))/3;
        layoutParams.height = layoutParams.width;
        holder.framelayout.setLayoutParams(layoutParams);
        // TODO: 2016/7/21
        Netroid.displayImage(Util.transferCropImage(info.getCover(), layoutParams.width), holder.ivCover);

        holder.tvPrice.setText("¥" +info.getPrice());
        if(isSimilar){
            holder.tvPrice.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.OnItemClick(info);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mList == null){
            return 0;
        }else {
            return mList.size();
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(NewGoodsItem info);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class CollocationSelectedGoodsListViewHolder extends RecyclerView.ViewHolder{
        FrameLayout framelayout;
        NetworkImageView ivCover;
        TextView tvPrice;

        public CollocationSelectedGoodsListViewHolder(View itemView) {
            super(itemView);
            framelayout= (FrameLayout) itemView.findViewById(R.id.framelayout);
            ivCover = (NetworkImageView) itemView.findViewById(R.id.iv_cover);
            tvPrice= (TextView) itemView.findViewById(R.id.tv_price);
        }
    }
}

