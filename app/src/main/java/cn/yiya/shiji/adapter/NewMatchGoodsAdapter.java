package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CollocationSelectedGoodsListActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.WorkImage;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;

/**
 * 搭配详情 商品列表
 * Created by Amy on 2016/7/13.
 */
public class NewMatchGoodsAdapter extends RecyclerView.Adapter<NewMatchGoodsAdapter.GoodsViewHolder> {

    private Context mContext;
    private List<WorkImage.GoodsEntity> mList;

    public NewMatchGoodsAdapter(Context mContext, List<WorkImage.GoodsEntity> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_match_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {
        final WorkImage.GoodsEntity item = mList.get(position);
        Netroid.displayImage(item.getCover(), holder.ivGoodsImage);
        holder.tvGoodsName.setText(item.getName());
        holder.tvGoodsBrand.setText(item.getBrand());
        holder.tvGoodsPrice.setText("¥" + item.getPrice());
        holder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetUtil.IsInNetwork(mContext)) {
                    Util.toast(mContext, Configration.OFF_LINE_TIPS, true);
                    return;
                }
                gotoGoodsDetail(item.getId());
            }
        });
        holder.btnFindSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找相似
                if (!NetUtil.IsInNetwork(mContext)) {
                    Util.toast(mContext, Configration.OFF_LINE_TIPS, true);
                    return;
                }
                Intent intent = new Intent(mContext, CollocationSelectedGoodsListActivity.class);
                intent.putExtra("goodsId", item.getId());
                intent.putExtra("isSimilar", true);
                mContext.startActivity(intent);
            }
        });
        holder.btnShoppping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去购买
                if (!NetUtil.IsInNetwork(mContext)) {
                    Util.toast(mContext, Configration.OFF_LINE_TIPS, true);
                    return;
                }
                gotoGoodsDetail(item.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView ivGoodsImage;
        TextView tvGoodsName, tvGoodsBrand, tvGoodsPrice;
        Button btnFindSimilar, btnShoppping;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            ivGoodsImage = (NetworkImageView) itemView.findViewById(R.id.iv_goods_image);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsBrand = (TextView) itemView.findViewById(R.id.tv_goods_brand);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            btnFindSimilar = (Button) itemView.findViewById(R.id.btn_find_similar);
            btnShoppping = (Button) itemView.findViewById(R.id.btn_shopping);
        }
    }
}
