package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.TagProduceItem;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * 品牌详情Adapter
 * Created by Amy on 2016/10/10.
 */

public class NewSingleBrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_DESC = 1001;
    private final int TYPE_BANNER = 1002;
    private final int TYPE_POPULAR = 1003;
    private final int TYPE_LIST_TAB = 1004;
    private final int TYPE_NULL = 1005;
    private final int TYPE_LIST = 1006;

    private int HEAD_LENGTH = 5;
    private int listCount = 0;

    private RecyclerView.LayoutManager mLayoutManager;

    private Context mContext;
    private int type = 1;  //1商品 2搭配 3晒单
    private AdapterAddViewListener addViewListener;

    private ArrayList<NewGoodsItem> mGoodsLists = new ArrayList<>(); //商品
    private ArrayList<TagProduceItem> mMatchLists = new ArrayList<>(); //搭配
    private ArrayList<TagProduceItem> mTagLists = new ArrayList<>();//晒单

    private boolean isRecommend;

    public NewSingleBrandAdapter(Context context, RecyclerView.LayoutManager layoutManager, AdapterAddViewListener addViewListener) {
        this.mContext = context;
        this.mLayoutManager = layoutManager;
        this.addViewListener = addViewListener;
    }

    public NewSingleBrandAdapter(Context context, RecyclerView.LayoutManager layoutManager, AdapterAddViewListener addViewListener, boolean isRecommend) {
        this.mContext = context;
        this.mLayoutManager = layoutManager;
        this.addViewListener = addViewListener;
        this.isRecommend = isRecommend;
    }

    public void setmGoodsLists(ArrayList<NewGoodsItem> mGoodsLists) {
        this.type = 1;
        if (mGoodsLists == null) {
            this.mGoodsLists = new ArrayList<>();
        } else {
            this.mGoodsLists = mGoodsLists;
        }

    }

    public void setmMatchLists(ArrayList<TagProduceItem> mMatchLists) {
        this.type = 2;
        if (mMatchLists == null) {
            this.mMatchLists = new ArrayList<>();
        } else {
            this.mMatchLists = mMatchLists;
        }
    }

    public void setmTagLists(ArrayList<TagProduceItem> mTagLists) {
        this.type = 3;
        if (mTagLists == null) {
            this.mTagLists = new ArrayList<>();
        } else {
            this.mTagLists = mTagLists;
        }
    }


    public ArrayList<NewGoodsItem> getmGoodsLists() {
        return mGoodsLists;
    }

    public ArrayList<TagProduceItem> getmMatchLists() {
        return mMatchLists;
    }

    public ArrayList<TagProduceItem> getmTagLists() {
        return mTagLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DESC:
                return new DescViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_brand_title_desc, parent, false));
            case TYPE_BANNER:
                return new BannerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_brand_banner, parent, false));
            case TYPE_POPULAR:
                return new PopularViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_brand_popular, parent, false));
            case TYPE_LIST_TAB:
                return new TabViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_brand_tab, parent, false));
            case TYPE_NULL:
                return new NullViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_brand_no_data, parent, false));
            case TYPE_LIST:
                return new SingleBrandViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_brand_list, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof SingleBrandViewHolder) {
            final SingleBrandViewHolder holder = (SingleBrandViewHolder) viewHolder;
            final int listPos = position - HEAD_LENGTH;
            if (type == 1) {
                holder.layoutGoods.setVisibility(View.VISIBLE);
                holder.layoutMatch.setVisibility(View.GONE);
                holder.layoutTag.setVisibility(View.GONE);

                final NewGoodsItem goodsItem = mGoodsLists.get(listPos);

                // 专题活动
                if(goodsItem.getTopic_id() > 0) {
                    holder.rlytTopic.setVisibility(View.VISIBLE);
                    holder.rlytGoods.setVisibility(View.GONE);

                    ViewGroup.LayoutParams params = holder.ivTopic.getLayoutParams();
                    params.width = SimpleUtils.getScreenWidth(mContext) / 2 - SimpleUtils.dp2px(mContext, 14);
                    params.height = goodsItem.getCover_info().getHeight() * params.width / goodsItem.getCover_info().getWidth();
                    holder.ivTopic.setLayoutParams(params);
                    holder.ivJanbian.setLayoutParams(params);
//                    int nWidth = SimpleUtils.getScreenWidth(mContext) / 2 - SimpleUtils.dp2px(mContext, 14);
                    holder.ivTopic.setImageURI(Util.transferImage(goodsItem.getCover(), params.width));
                    holder.ivTopic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, HomeIssueActivity.class);
                            intent.putExtra("activityId", String.valueOf(goodsItem.getTopic_id()));
                            intent.putExtra("menuId", 7);
                            mContext.startActivity(intent);
                        }
                    });
                    holder.tvTopicName.setText(goodsItem.getTitle());
                }else {
            /*商品*/
                    holder.rlytTopic.setVisibility(View.GONE);
                    holder.rlytGoods.setVisibility(View.VISIBLE);

                    if (goodsItem.getStatus() == 3) {
                        holder.ivSoldOut.setVisibility(View.INVISIBLE);
                    } else {
                        holder.ivSoldOut.setVisibility(View.VISIBLE);
                    }

                    ViewGroup.LayoutParams params = holder.ivGoods.getLayoutParams();
                    params.width = SimpleUtils.getScreenWidth(mContext) / 2 - SimpleUtils.dp2px(mContext, 14);
                    params.height = goodsItem.getCover_info().getHeight() * params.width / goodsItem.getCover_info().getWidth();
                    holder.ivGoods.setLayoutParams(params);
                    if (!TextUtils.isEmpty(goodsItem.getCover())) {
                        holder.ivGoods.setImageURI(Util.transferCropImage(goodsItem.getCover(), params.width));
                    }

                    holder.tvGoodsBrand.setText(goodsItem.getBrand());
                    holder.tvGoodsTitle.setText(goodsItem.getTitle());
                    holder.tvGoodsSite.setText(goodsItem.getSite());

                    holder.tvNowPrice.setText("￥" + goodsItem.getPrice());
                    holder.tvNowPrice.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
                    if (!TextUtils.isEmpty(goodsItem.getList_price())) {
                        if (goodsItem.getPrice().equals(goodsItem.getList_price())) {
                            holder.tvPastPrice.setVisibility(View.GONE);
                        } else {
                            holder.tvPastPrice.setVisibility(View.VISIBLE);
                            holder.tvPastPrice.setText("￥" + goodsItem.getList_price());
                            holder.tvPastPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvPastPrice.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
                        }
                    }

                    if (goodsItem.getDiscount() != 10) {
                        holder.llDiscount.setVisibility(View.VISIBLE);
                        holder.tvDiscount.setText(Util.FloatKeepOne(goodsItem.getDiscount()));
                    } else {
                        holder.llDiscount.setVisibility(View.GONE);
                    }
                    holder.ivGoods.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((BaseAppCompatActivity) mContext).isEffectClick()) {
                                gotoGoodsDetail(goodsItem.getId());
                            }
                        }
                    });

                    if (isRecommend) {
                        holder.rlytRecommend.setVisibility(View.VISIBLE);
                        holder.tvForSeller.setText("佣金：￥" + goodsItem.getFor_seller());
                        holder.tvForSellerPer.setText("佣金比例：" + goodsItem.getShare_profit());
                        holder.ivRecommend.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.ivRecommend.setSelected(goodsItem.is_recommend());
                            }
                        });
                        holder.ivRecommend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(goodsItem.is_recommend()){
                                    return;
                                }
                                addRecommend(goodsItem, holder);
                            }
                        });
                    } else {
                        holder.rlytRecommend.setVisibility(View.GONE);
                    }
                }

            } else if (type == 2) {
            /*搭配*/
                holder.layoutGoods.setVisibility(View.GONE);
                holder.layoutMatch.setVisibility(View.VISIBLE);
                holder.layoutTag.setVisibility(View.GONE);

                final TagProduceItem matchItem = mMatchLists.get(listPos);
                ViewGroup.LayoutParams params = holder.ivMatch.getLayoutParams();
                params.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30)) / 2;
                params.height = params.width * 263 / 173;
                holder.ivMatch.setLayoutParams(params);
                if (!TextUtils.isEmpty(matchItem.getImage())) {
                    holder.ivMatch.setImageURI(Util.transfer(matchItem.getImage()));
                }

                holder.ivMatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addViewListener != null)
                            addViewListener.goToMatchDetail(matchItem.getWork_id(), listPos);
                    }
                });

            } else if (type == 3) {
            /*晒单*/
                holder.layoutGoods.setVisibility(View.GONE);
                holder.layoutMatch.setVisibility(View.GONE);
                holder.layoutTag.setVisibility(View.VISIBLE);

                final TagProduceItem tagItem = mTagLists.get(listPos);
                holder.tvTagContent.setText(tagItem.getContent());
                holder.tvTagLike.setText("" + tagItem.getLike_count());
                holder.tvTagComment.setText("" + tagItem.getComment_count());

                ViewGroup.LayoutParams params = holder.ivTag.getLayoutParams();
                params.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 30)) / 2;
                params.height = params.width;
                holder.ivTag.setLayoutParams(params);

                if (!TextUtils.isEmpty(tagItem.getImage())) {
                    holder.ivTag.setImageURI(Util.transferCropImage(tagItem.getImage(), params.width));
                }
                holder.ivTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addViewListener != null)
                            addViewListener.goToWorkDetail(tagItem.getWork_id(), listPos);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            listCount = mGoodsLists == null ? 0 : mGoodsLists.size();
        } else if (type == 2) {
            listCount = mMatchLists == null ? 0 : mMatchLists.size();
        } else if (type == 3) {
            listCount = mTagLists == null ? 0 : mTagLists.size();
        }
        return HEAD_LENGTH + listCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_DESC;
        } else if (position == 1) {
            return TYPE_BANNER;
        } else if (position == 2) {
            return TYPE_POPULAR;
        } else if (position == 3) {
            return TYPE_LIST_TAB;
        } else if (position == 4) {
            return TYPE_NULL;
        } else {
            return TYPE_LIST;
        }
    }

     /*==============================商品 搭配 晒单 =============================*/

    class SingleBrandViewHolder extends RecyclerView.ViewHolder {
        /*商品*/
        RelativeLayout layoutGoods;

        RelativeLayout rlytGoods;
        LinearLayout llGoods;
        SimpleDraweeView ivGoods;
        TextView tvGoodsBrand;
        TextView tvGoodsTitle;

        TextView tvNowPrice;
        TextView tvPastPrice;
        TextView tvGoodsSite;

        RelativeLayout rlytRecommend;
        TextView tvForSeller, tvForSellerPer;
        ImageView ivRecommend;

        RelativeLayout rlytTopic;
        SimpleDraweeView ivTopic;
        ImageView ivJanbian;
        TextView tvTopicName;

        //折扣
        LinearLayout llDiscount;
        TextView tvDiscount;
        //已售罄
        ImageView ivSoldOut;

        /*搭配*/
        LinearLayout layoutMatch;
        SimpleDraweeView ivMatch;
        /*晒单*/
        LinearLayout layoutTag;
        SimpleDraweeView ivTag;
        TextView tvTagContent;
        TextView tvTagComment;
        TextView tvTagLike;

        public SingleBrandViewHolder(View itemView) {
            super(itemView);
            /*商品*/
            layoutGoods = (RelativeLayout) itemView.findViewById(R.id.layout_goods);

            rlytGoods = (RelativeLayout) itemView.findViewById(R.id.rlyt_goods);
            llGoods = (LinearLayout) itemView.findViewById(R.id.ll_goods);
            ivGoods = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods);
            tvGoodsBrand = (TextView) itemView.findViewById(R.id.tv_goods_brand);
            tvGoodsTitle = (TextView) itemView.findViewById(R.id.tv_goods_title);

            tvNowPrice = (TextView) itemView.findViewById(R.id.tv_now_price);
            tvPastPrice = (TextView) itemView.findViewById(R.id.tv_past_price);
            tvGoodsSite = (TextView) itemView.findViewById(R.id.tv_goods_site);

            rlytRecommend = (RelativeLayout) itemView.findViewById(R.id.rlyt_recommend);
            tvForSeller = (TextView) itemView.findViewById(R.id.tv_for_seller);
            tvForSellerPer = (TextView) itemView.findViewById(R.id.tv_for_seller_per);
            ivRecommend = (ImageView) itemView.findViewById(R.id.iv_recommend);

            rlytTopic = (RelativeLayout) itemView.findViewById(R.id.rlyt_topic);
            ivTopic = (SimpleDraweeView) itemView.findViewById(R.id.iv_topic);
            ivJanbian = (ImageView) itemView.findViewById(R.id.iv_jianbian);
            tvTopicName = (TextView) itemView.findViewById(R.id.tv_topic_name);

            //折扣
            llDiscount = (LinearLayout) itemView.findViewById(R.id.ll_discount);
            tvDiscount = (TextView) itemView.findViewById(R.id.tv_discount);
            //已售罄
            ivSoldOut = (ImageView) itemView.findViewById(R.id.iv_sold_out);

            /*搭配*/
            layoutMatch = (LinearLayout) itemView.findViewById(R.id.layout_match);
            ivMatch = (SimpleDraweeView) itemView.findViewById(R.id.iv_match);
             /*晒单*/
            layoutTag = (LinearLayout) itemView.findViewById(R.id.layout_tag);
            ivTag = (SimpleDraweeView) itemView.findViewById(R.id.iv_tag);
            tvTagContent = (TextView) itemView.findViewById(R.id.tv_tag_content);
            tvTagComment = (TextView) itemView.findViewById(R.id.tv_tag_comment);
            tvTagLike = (TextView) itemView.findViewById(R.id.tv_tag_like);
        }
    }

    /*==============================品牌描述=============================*/
    class DescViewHolder extends RecyclerView.ViewHolder {

        public DescViewHolder(View itemView) {
            super(itemView);
            setFulSpan(itemView);
            if (addViewListener != null) addViewListener.addDescView(itemView);
        }
    }

    /*==============================活动图=============================*/
    class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(View itemView) {
            super(itemView);
            setFulSpan(itemView);
            if (addViewListener != null) addViewListener.addBannerView(itemView);
        }
    }


    /*==============================最热门 新上架 最折扣=============================*/
    class PopularViewHolder extends RecyclerView.ViewHolder {


        public PopularViewHolder(View itemView) {
            super(itemView);
            setFulSpan(itemView);
            if (addViewListener != null) addViewListener.addPopularView(itemView);
        }
    }


    /*==============================商品 搭配 晒单 Tab=============================*/
    class TabViewHolder extends RecyclerView.ViewHolder {


        public TabViewHolder(View itemView) {
            super(itemView);
            setFulSpan(itemView);
            if (addViewListener != null) addViewListener.addTabView(itemView);
        }
    }

    /*==============================暂无结果=============================*/

    class NullViewHolder extends RecyclerView.ViewHolder {

        public NullViewHolder(View itemView) {
            super(itemView);
            setFulSpan(itemView);
            if (addViewListener != null) addViewListener.addNullView(itemView);
        }
    }

    private void setFulSpan(View view) {
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            view.setLayoutParams(layoutParams);
        }
    }


    public interface AdapterAddViewListener {
        void addDescView(View itemView);

        void addBannerView(View itemView);

        void addPopularView(View itemView);

        void addTabView(View itemView);

        void addNullView(View itemView);

        void goToMatchDetail(int workId, int position);

        void goToWorkDetail(int workId, int position);

        void goToLogin();
    }

    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        DebugUtil.e("==========goodsdetail:" + goodsId);
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        mContext.startActivity(intent);
    }

    /**
     * 添加推荐
     */
    private void addRecommend(final NewGoodsItem info, final SingleBrandViewHolder holder) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().getRecommendGoodsEdit(MapRequest.setRecommendGoodsAddMap(info.getId(), 1)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            info.setIs_recommend(true);
                            holder.ivRecommend.setSelected(true);
                            Util.showRecommendToast(mContext);
                        } else if (msg.isLossLogin()) {
                            if (addViewListener != null) addViewListener.goToLogin();
                        } else if (msg.isNotHasShop()) {
                            Util.toast(mContext, "您还未开店，请先开店！", true);
                        } else {
                            Util.toast(mContext, "添加推荐失败", true);
                        }
                    }
                });
    }
}
