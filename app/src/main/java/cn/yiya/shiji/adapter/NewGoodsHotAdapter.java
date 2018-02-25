package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.FontStyleUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * 品牌详情 最热门
 * Created by Amy on 2016/9/28.
 */

public class NewGoodsHotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewGoodsItem> mLists;
    private boolean bShowPrice;
    private boolean isRecommend;

    public NewGoodsHotAdapter(Context context, boolean bShowPrice, boolean isRecommend) {
        this.mContext = context;
        this.bShowPrice = bShowPrice;
        this.isRecommend = isRecommend;
    }

    public void setmLists(List<NewGoodsItem> mLists) {
        this.mLists = mLists;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_brand_goods_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GoodsItemViewHolder viewHolder = (GoodsItemViewHolder) holder;
        final NewGoodsItem info = mLists.get(position);

        viewHolder.ivImg.setImageURI(Util.ScaleImageGoodes(info.getCover(), SimpleUtils.dp2px(mContext, 130) - 20));
        viewHolder.tvName.setText(info.getTitle());
        viewHolder.vSpace.setVisibility(position == mLists.size() - 1 ? View.VISIBLE : View.GONE);
        if (!bShowPrice || TextUtils.isEmpty(info.getList_price()) || info.getPrice().equals(info.getList_price())) {
            viewHolder.tvListPrice.setVisibility(View.GONE);
        } else {
            viewHolder.tvListPrice.setVisibility(View.VISIBLE);
            viewHolder.tvListPrice.setText("￥" + info.getList_price());
            viewHolder.tvListPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            FontStyleUtil.setHelveCondenBoldStyle(mContext, viewHolder.tvListPrice);
        }

        viewHolder.tvPrice.setText("￥" + info.getPrice());
        FontStyleUtil.setHelveCondenBoldStyle(mContext, viewHolder.tvPrice);

        viewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGoodsDetail(info.getId());
            }
        });

        if(isRecommend) {
            viewHolder.rlytRecommend.setVisibility(View.VISIBLE);
            viewHolder.tvForSeller.setText("佣金：￥" + info.getFor_seller());
            viewHolder.getTvForSellerPro.setText("佣金比例：" + info.getShare_profit());
            viewHolder.ivRecommend.post(new Runnable() {
                @Override
                public void run() {
                    viewHolder.ivRecommend.setSelected(info.is_recommend());
                }
            });
            viewHolder.ivRecommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(info.is_recommend()){
                        return;
                    }
                    addRecommend(info, viewHolder);
                }
            });
        }else {
            viewHolder.rlytRecommend.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    class GoodsItemViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView ivImg;
        TextView tvName;
        TextView tvPrice;
        TextView tvListPrice;
        View vSpace;
        RelativeLayout rlytRecommend;
        TextView tvForSeller;
        TextView getTvForSellerPro;
        ImageView ivRecommend;

        public GoodsItemViewHolder(View itemView) {
            super(itemView);
            ivImg = (SimpleDraweeView) itemView.findViewById(R.id.brands_item_image);
            tvName = (TextView) itemView.findViewById(R.id.brands_item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.brands_item_price);
            tvListPrice = (TextView) itemView.findViewById(R.id.brands_item_list_price);
            vSpace = itemView.findViewById(R.id.brands_item_space);
            rlytRecommend = (RelativeLayout) itemView.findViewById(R.id.rlyt_recommend);
            tvForSeller = (TextView) itemView.findViewById(R.id.tv_for_seller);
            getTvForSellerPro = (TextView) itemView.findViewById(R.id.tv_for_seller_per);
            ivRecommend = (ImageView) itemView.findViewById(R.id.iv_recommend);
        }
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

    /**
     * 添加推荐
     */
    private void addRecommend(final NewGoodsItem info, final GoodsItemViewHolder holder) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().getRecommendGoodsEdit(MapRequest.setRecommendGoodsAddMap(info.getId(), 1)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            info.setIs_recommend(true);
                            holder.ivRecommend.setSelected(true);
                            Util.showRecommendToast(mContext);
                        } else if (msg.isLossLogin()) {
                            if (onItemClickListener != null) onItemClickListener.gotoLogin();
                        } else if (msg.isNotHasShop()) {
                            Util.toast(mContext, "您还未开店，请先开店！", true);
                        } else {
                            Util.toast(mContext, "添加推荐失败", true);
                        }
                    }
                });
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void gotoLogin();
    }
}
