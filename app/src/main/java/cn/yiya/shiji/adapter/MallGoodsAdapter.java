package cn.yiya.shiji.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.PublishedGoodsInfo;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * 商品列表Adapter
 * Created by chenjian on 2015/10/29.
 */
public class MallGoodsAdapter extends RecyclerView.Adapter<MallGoodsAdapter.GoodsViewHolder> {

    private Context mContext;
    private ArrayList<NewGoodsItem> mLists;
    private boolean bShow;
    private boolean isRecommend;
    private OnItemClickListener onItemClickListener;

    public MallGoodsAdapter(Context mContext, ArrayList<NewGoodsItem> mLists, boolean show) {
        this.mContext = mContext;
        this.mLists = mLists;
        this.bShow = show;
    }

    public MallGoodsAdapter(Context mContext, ArrayList<NewGoodsItem> mLists, boolean show, boolean isRecommend) {
        this.mContext = mContext;
        this.mLists = mLists;
        this.bShow = show;
        this.isRecommend = isRecommend;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(mContext).inflate(R.layout.goods_list_item, parent, false);

        return new GoodsViewHolder(mView);
    }

    public void setmLists(ArrayList<NewGoodsItem> mLists) {
        this.mLists = mLists;
    }

    public ArrayList<NewGoodsItem> getmLists() {
        return mLists;
    }

    @Override
    public void onBindViewHolder(final GoodsViewHolder holder, int position) {
        ViewGroup.LayoutParams params = holder.ivImg.getLayoutParams();

        final NewGoodsItem info = mLists.get(position);

        // 专题活动
        if(info.getTopic_id() > 0) {
            holder.rlytTopic.setVisibility(View.VISIBLE);
            holder.rlytGoods.setVisibility(View.GONE);

            ViewGroup.LayoutParams lparams = holder.ivTopic.getLayoutParams();
            lparams.width = SimpleUtils.getScreenWidth(mContext) / 2 - SimpleUtils.dp2px(mContext, 14);
            lparams.height = info.getCover_info().getHeight() * lparams.width / info.getCover_info().getWidth();
            holder.ivTopic.setLayoutParams(lparams);
            holder.ivJanbian.setLayoutParams(lparams);
            holder.ivTopic.setImageURI(Util.transferImage(info.getCover(), lparams.width));
            holder.ivTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, HomeIssueActivity.class);
                    intent.putExtra("activityId", String.valueOf(info.getTopic_id()));
                    intent.putExtra("menuId", 7);
                    if(isRecommend){
                        intent.putExtra("isRecommend", true);
                    }
                    mContext.startActivity(intent);
                }
            });
            holder.tvTopicName.setText(info.getTitle());
        }else {  // 商品
            holder.rlytTopic.setVisibility(View.GONE);
            holder.rlytGoods.setVisibility(View.VISIBLE);

            if (info.getStatus() == 3) {
                holder.ivSoldout.setVisibility(View.INVISIBLE);
            } else {
                holder.ivSoldout.setVisibility(View.VISIBLE);
            }
            int nWidth = SimpleUtils.getScreenWidth(mContext) / 2 - SimpleUtils.dp2px(mContext, 14);
            if (!TextUtils.isEmpty(info.getCover())) {
                Netroid.displayImage(Util.transferCropImage(info.getCover(), nWidth), holder.ivImg);
            }
            params.width = nWidth;
            params.height = info.getCover_info().getHeight() * params.width / info.getCover_info().getWidth();
            holder.ivImg.setLayoutParams(params);

            holder.tvBrand.setText(info.getBrand());
            holder.tvTxt.setText(info.getTitle());
            holder.tvNow.setText("￥" + info.getPrice());
            holder.tvNow.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));

            holder.tvFrom.setText(info.getSite());

            if (bShow) {
                holder.llytDiscount.setVisibility(View.GONE);
            } else {
                if (info.getDiscount() != 10) {
                    holder.llytDiscount.setVisibility(View.VISIBLE);
                    holder.tvDiscount.setText(Util.FloatKeepOne(info.getDiscount()));
                } else {
                    holder.llytDiscount.setVisibility(View.GONE);
                }
            }

            if (!TextUtils.isEmpty(info.getList_price())) {
                if (info.getPrice().equals(info.getList_price())) {
                    holder.tvOrignal.setVisibility(View.GONE);
                } else {
                    holder.tvOrignal.setVisibility(View.VISIBLE);
                    holder.tvOrignal.setText("￥" + info.getList_price());
                    holder.tvOrignal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tvOrignal.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
                }
            }

            if (isRecommend) {
                holder.rlytRecommend.setVisibility(View.VISIBLE);
                holder.tvForSeller.setText("佣金：￥" + info.getFor_seller());
                holder.tvForSellerPer.setText("佣金比例：" + info.getShare_profit());
                holder.ivRecommend.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.ivRecommend.setSelected(info.is_recommend());
                    }
                });

                holder.ivRecommend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (info.is_recommend()) {
                            return;
                        }
                        addRecommend(info, holder);
                    }
                });
            } else {
                holder.rlytRecommend.setVisibility(View.GONE);
            }

            holder.rlytItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CustomEvent.onEvent(mContext, ((BaseApplication) (mContext.getApplicationContext())).getDefaultTracker(), "MallGoodsAdapter", CustomEvent.MallToDetail);
                    if (bShow) {
                        PublishedGoodsInfo goodsInfo = new PublishedGoodsInfo();
                        goodsInfo.setGoods_id(info.getId());
                        goodsInfo.setCover(info.getCover());
                        goodsInfo.setBrand(info.getBrand());
                        goodsInfo.setTitle(info.getTitle());
                        goodsInfo.setSite(info.getSite());
                        goodsInfo.setPrice(info.getPrice());
                        Intent intent = new Intent();
                        intent.putExtra("goodes", new Gson().toJson(goodsInfo));
                        ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                        ((Activity) mContext).finish();
                        return;
                    }
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(info);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    public interface OnItemClickListener {
        void OnItemClick(NewGoodsItem info);

        void gotoLogin();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlytGoods;
        TextView tvBrand;
        TextView tvTxt;
        NetworkImageView ivImg;
        TextView tvNow;
        TextView tvOrignal;
        LinearLayout llytDiscount;
        TextView tvDiscount;
        TextView tvFrom;
        RelativeLayout rlytItem;
        ImageView ivSoldout;

        RelativeLayout rlytRecommend;
        TextView tvForSeller;
        TextView tvForSellerPer;
        ImageView ivRecommend;

        RelativeLayout rlytTopic;
        SimpleDraweeView ivTopic;
        TextView tvTopicName;
        ImageView ivJanbian;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            rlytGoods = (RelativeLayout) itemView.findViewById(R.id.rlyt_goods);
            tvBrand = (TextView) itemView.findViewById(R.id.item_brand);
            tvTxt = (TextView) itemView.findViewById(R.id.item_title);
            ivImg = (NetworkImageView) itemView.findViewById(R.id.item_img);
            tvNow = (TextView) itemView.findViewById(R.id.item_now_price);
            tvOrignal = (TextView) itemView.findViewById(R.id.item_orginal_price);
            llytDiscount = (LinearLayout) itemView.findViewById(R.id.discount_layout);
            tvDiscount = (TextView) itemView.findViewById(R.id.discount_tv);
            tvFrom = (TextView) itemView.findViewById(R.id.goods_from_text);
            rlytItem = (RelativeLayout) itemView.findViewById(R.id.cardview);
            ivSoldout = (ImageView) itemView.findViewById(R.id.shop_image_sold_out);

            rlytRecommend = (RelativeLayout) itemView.findViewById(R.id.rlyt_recommend);
            tvForSeller = (TextView) itemView.findViewById(R.id.tv_for_seller);
            tvForSellerPer = (TextView) itemView.findViewById(R.id.tv_for_seller_per);
            ivRecommend = (ImageView) itemView.findViewById(R.id.iv_recommend);

            rlytTopic = (RelativeLayout) itemView.findViewById(R.id.rlyt_topic);
            ivTopic = (SimpleDraweeView) itemView.findViewById(R.id.iv_topic);
            tvTopicName = (TextView) itemView.findViewById(R.id.tv_topic_name);
            ivJanbian = (ImageView) itemView.findViewById(R.id.iv_jianbian);
        }
    }

    /**
     * 添加推荐
     */
    private void addRecommend(final NewGoodsItem info, final GoodsViewHolder holder) {
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



}


