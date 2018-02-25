package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.GoodsDetailActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.activity.RecommendBuyWebView;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.GoodsIdInfo;
import cn.yiya.shiji.entity.navigation.RecommendInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * Created by Tom on 2016/3/28.
 */
public class RecommendBuyAdapter extends RecyclerView.Adapter<RecommendBuyAdapter.RecommendBuyViewHolder> {
    private Context mContext;
    private ArrayList<RecommendInfo> mList;

    public ArrayList<RecommendInfo> getmList() {
        return mList;
    }

    public void setmList(ArrayList<RecommendInfo> mList) {
        this.mList = mList;
    }

    private int type;                           // 1是国家下点击item无跳转2是mall下的点击跳至品牌界面3是store下跳购买

    public RecommendBuyAdapter(Context mContext, int  type){
        this.mContext = mContext;
        this.type = type;
    }

    public RecommendBuyAdapter(Context mContext, int  type, ArrayList<RecommendInfo> mList){
        this.mContext = mContext;
        this.type = type;
        this.mList = mList;
    }

    @Override
    public RecommendBuyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendBuyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recommend_buy_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecommendBuyViewHolder holder, int position) {
        final RecommendInfo info = mList.get(position);

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.rltyItem.getLayoutParams();
        layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 48))*2/5;
        layoutParams.height = layoutParams.width;
        layoutParams.leftMargin = SimpleUtils.dp2px(mContext, 16);
        holder.rltyItem.setLayoutParams(layoutParams);
        BitmapTool.showImageView(info.getLogo(), holder.ivLogo, R.mipmap.recommend_buy_bg);
        holder.tvName.setText(info.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 2) {
                    if(!NetUtil.IsInNetwork(mContext)){
                        Toast.makeText(mContext, Configration.OFF_LINE_TIPS, Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                        intent.putExtra("brand_id", Integer.parseInt(info.getId()));
                        mContext.startActivity(intent);
                    }
                }else if (type == 1) {
                    if(TextUtils.isEmpty(info.getUrl())){
                        return;
                    }else {
                        Intent intent = new Intent(mContext, RecommendBuyWebView.class);
                        intent.putExtra("url", info.getUrl());
                        intent.putExtra("name", info.getName());
                        mContext.startActivity(intent);
                    }
                }else if(type == 3){
//                    if(!NetUtil.IsInNetwork(mContext)){
//                        Toast.makeText(mContext, Configration.OFF_LINE_TIPS, Toast.LENGTH_SHORT).show();
//                        return;
//                    }else {
//                        GoodsIdInfo goodsIdInfo = new GoodsIdInfo();
//                        goodsIdInfo.setGoodsId(info.getId());
//                        goodsIdInfo.setRecommend(info.getRecommend());
                        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
                        intent.putExtra("goodsId", info.getId());
                        mContext.startActivity(intent);
//                    }
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

    class RecommendBuyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rltyItem;
        ImageView ivLogo;
        TextView tvName;

        public RecommendBuyViewHolder(View itemView) {
            super(itemView);
            rltyItem = (RelativeLayout) itemView.findViewById(R.id.gallery_item_tag_layout);
            ivLogo = (ImageView) itemView.findViewById(R.id.recommend_buy_item_logo);
            tvName = (TextView) itemView.findViewById(R.id.recommend_buy_item_name);
        }
    }
}

