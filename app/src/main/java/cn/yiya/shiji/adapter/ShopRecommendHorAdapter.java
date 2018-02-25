package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.SellerRecommendedActivity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Amy on 2016/11/30.
 */

public class ShopRecommendHorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();

    private int type; // 1 large  2 small
    public final static int GOODS = 111;
    public final static int MORE = 112;

    private OnClickListener onClickListener;

    public ShopRecommendHorAdapter(Context mContext, int type) {
        this.mContext = mContext;
        this.type = type;
    }

    public ArrayList<NewGoodsItem> getmList() {
        return this.mList;
    }

    public void setmList(ArrayList<NewGoodsItem> mList) {

        this.mList = (mList == null ? new ArrayList<NewGoodsItem>() : mList);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GOODS:
                if (type == 1) {
                    return new LargeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_recommend_large_list, parent, false));
                } else if (type == 2) {
                    return new SmallViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_recommend_small_list, parent, false));
                }
                return null;
            case MORE:
                return new MoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.main_item_thematic_child_more, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder.getItemViewType() == GOODS) {
            final NewGoodsItem item = mList.get(position);
            if (type == 1) {
                LargeViewHolder viewHolder = (LargeViewHolder) holder;
                viewHolder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 120)));
                viewHolder.tvPrice.setText("¥ " + item.getPrice());
                viewHolder.tvCommission.setText("佣金：¥" + item.getFor_seller());
                viewHolder.tvCommissionRatio.setText("佣金比例：" + item.getShare_profit());
                viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) onClickListener.delete(position);
                    }
                });
                viewHolder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoGoodsDetail(item.getId());
                    }
                });

            } else if (type == 2) {
                SmallViewHolder viewHolder = (SmallViewHolder) holder;
                viewHolder.ivGoodsImage.setImageURI(Util.transferImage(item.getCover(), SimpleUtils.dp2px(mContext, 80)));
                viewHolder.ivGoodsImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoGoodsDetail(item.getId());
                    }
                });
//                viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onClickListener != null) onClickListener.delete(position);
//                    }
//                });
            }
        } else {
            if (type == 2) {
                MoreViewHolder viewHolder = (MoreViewHolder) holder;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SimpleUtils.dp2px(mContext, 80), ViewGroup.LayoutParams.MATCH_PARENT);
                viewHolder.llMore.setLayoutParams(params);
                viewHolder.tvMore.setTextSize(11);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mList.size()) return GOODS;
        else return MORE;
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

    class LargeViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivGoodsImage;
        ImageView ivDelete;
        TextView tvPrice, tvCommission, tvCommissionRatio;

        public LargeViewHolder(View itemView) {
            super(itemView);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvCommission = (TextView) itemView.findViewById(R.id.tv_commission);
            tvCommissionRatio = (TextView) itemView.findViewById(R.id.tv_commission_ratio);
        }
    }

    class SmallViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivGoodsImage;
//        ImageView ivDelete;

        public SmallViewHolder(View itemView) {
            super(itemView);
            ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
//            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    public interface OnClickListener {
        void delete(int position);
    }

    class MoreViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llMore;
        private TextView tvMore;

        public MoreViewHolder(View itemView) {
            super(itemView);
            llMore = (LinearLayout) itemView.findViewById(R.id.ll_more);
            tvMore = (TextView) itemView.findViewById(R.id.tv_more);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SellerRecommendedActivity.class);
                    mContext.startActivity(intent);

                }
            });
        }
    }

}
