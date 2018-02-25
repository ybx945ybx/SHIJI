package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewMatchDetailActivity;
import cn.yiya.shiji.entity.TagProduceItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/11/9.
 */
public class GoodsDetailMatchAdapter extends RecyclerView.Adapter<GoodsDetailMatchAdapter.GoodsDetailMatchViewHolder>{

    private Context mContext;
    private ArrayList<TagProduceItem> mMatchlist;

    public GoodsDetailMatchAdapter (Context mContext, ArrayList<TagProduceItem> mMatchlist){
        this.mContext = mContext;
        this.mMatchlist = mMatchlist;
    }

    @Override
    public GoodsDetailMatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsDetailMatchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.goods_detail_match_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsDetailMatchViewHolder holder, int position) {
        final TagProduceItem item = mMatchlist.get(position);

        holder.ivBig.setImageURI(Util.transferImage(item.getImage(), SimpleUtils.dp2px(mContext, 100)));
        switch (item.getGoods_image().size()){
            case 1:
                holder.ivOne.setVisibility(View.INVISIBLE);
                holder.ivTwo.setImageURI(Util.transferImage(item.getGoods_image().get(0), SimpleUtils.dp2px(mContext, 50)));
                holder.ivThree.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.ivOne.setImageURI(Util.transferImage(item.getGoods_image().get(0), SimpleUtils.dp2px(mContext, 50)));
                holder.ivTwo.setImageURI(Util.transferImage(item.getGoods_image().get(1), SimpleUtils.dp2px(mContext, 50)));
                holder.ivThree.setVisibility(View.INVISIBLE);
                break;
            case 3:
                holder.ivOne.setImageURI(Util.transferImage(item.getGoods_image().get(0), SimpleUtils.dp2px(mContext, 50)));
                holder.ivTwo.setImageURI(Util.transferImage(item.getGoods_image().get(1), SimpleUtils.dp2px(mContext, 50)));
                holder.ivThree.setImageURI(Util.transferImage(item.getGoods_image().get(2), SimpleUtils.dp2px(mContext, 50)));
                break;
            default:
                holder.ivOne.setImageURI(Util.transferImage(item.getGoods_image().get(0), SimpleUtils.dp2px(mContext, 50)));
                holder.ivTwo.setImageURI(Util.transferImage(item.getGoods_image().get(1), SimpleUtils.dp2px(mContext, 50)));
                holder.ivThree.setImageURI(Util.transferImage(item.getGoods_image().get(2), SimpleUtils.dp2px(mContext, 50)));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewMatchDetailActivity.class);
                intent.putExtra("work_id", item.getWork_id());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMatchlist == null ? 0 : mMatchlist.size();
    }

    class GoodsDetailMatchViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView ivBig;
        private SimpleDraweeView ivOne;
        private SimpleDraweeView ivTwo;
        private SimpleDraweeView ivThree;
        public GoodsDetailMatchViewHolder(View itemView) {
            super(itemView);
            ivBig = (SimpleDraweeView) itemView.findViewById(R.id.iv_big_image);
            ivOne = (SimpleDraweeView) itemView.findViewById(R.id.iv_one);
            ivTwo = (SimpleDraweeView) itemView.findViewById(R.id.iv_two);
            ivThree = (SimpleDraweeView) itemView.findViewById(R.id.iv_three);
        }
    }
}
