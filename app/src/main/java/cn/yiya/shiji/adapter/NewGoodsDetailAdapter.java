package cn.yiya.shiji.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Toast;

import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.views.MyWebView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import java.util.ArrayList;
import java.util.List;
import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.HtmlActicity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewMallGoodsActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ActivityListObject;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.GoodsTranlateDesc;
import cn.yiya.shiji.entity.HtmlImageListItem;
import cn.yiya.shiji.entity.HtmlImageListObject;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.TagProduceObject;
import cn.yiya.shiji.entity.VisitGoodsEntity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tom on 2016/11/3.
 */
public class NewGoodsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private GoodsDetailEntity goodsDetailEntity;
    private ActivityListObject brandActObj;
    private TagProduceObject matchObj;
    private TagProduceObject workObj;
    private VisitGoodsEntity visitObj;
    private MallGoodsDetailObject recommendObj;
    private List<GoodsDetailEntity.GoodsColorsEntity.ImagesEntity> colorImages;
    private OnActionClickListener onActionClickListener;
    private Dialog progressDialog;

    private final static int TYPE_BANNER = 1;
    private final static int TYPE_SITE_BRAND = 2;
    private final static int TYPE_BRAND_ACTIVITY = 10;
    private final static int TYPE_GOODS_DETAIL = 3;
    private final static int TYPE_SERVICE_HELP = 4;
    private final static int TYPE_MATCH = 5;
    private final static int TYPE_WORK_LIST = 6;
    private final static int TYPE_SOURCE = 7;
    private final static int TYPE_VIST_GOODS = 8;
    private final static int TYPE_RECOMMEND = 9;

    private int selectedPositon = 0;                    // 颜色被选中的位置
    private float priceMax;                             //  所有商品最高价
    private boolean isGroupBuy;                         //  是否是团购
    private boolean beTransed;                          //  是否已经翻译过


    public NewGoodsDetailAdapter(Context mContext, GoodsDetailEntity goodsDetailEntity, ActivityListObject brandActObj, TagProduceObject matchObj,
                                 TagProduceObject workObj, VisitGoodsEntity visitObj, MallGoodsDetailObject recommendObj, boolean isGroupBuy){
        this.mContext = mContext;
        this.goodsDetailEntity = goodsDetailEntity;
        this.brandActObj = brandActObj;
        this.matchObj = matchObj;
        this.workObj = workObj;
        this.visitObj = visitObj;
        this.recommendObj = recommendObj;
        this.isGroupBuy = isGroupBuy;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                return new GoodsDetailBannerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_banner, parent, false));
            case TYPE_SITE_BRAND:
                return new GoodsDetailSiteBrandHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_site_brand, parent, false));
            case TYPE_BRAND_ACTIVITY:
                return new GoodsDetailBrandActivityHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_brand_activity, parent, false));
            case TYPE_GOODS_DETAIL:
                return new GoodsDetailHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_info, parent, false));
            case TYPE_SERVICE_HELP:
                return new GoodsDetailServeHelpHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_serve_help, parent, false));
            case TYPE_MATCH:
                return new GoodsDetailMatchHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_horizontal_gallery, parent, false));
            case TYPE_WORK_LIST:
                return new GoodsDetailWorkListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_horizontal_gallery, parent, false));
            case TYPE_SOURCE:
                return new GoodsDetailSourceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_source, parent, false));
            case TYPE_VIST_GOODS:
                return new GoodsDetailVistGoodsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_horizontal_gallery, parent, false));
            case TYPE_RECOMMEND:
                return new GoodsDetailRecommendHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_recommend, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)){
                case TYPE_MATCH:
                    ((GoodsDetailMatchHolder)holder).bindMatchData();
                    break;
                case TYPE_WORK_LIST:
                    ((GoodsDetailWorkListHolder)holder).bindWorkListData();
                    break;
                case TYPE_VIST_GOODS:
                    ((GoodsDetailVistGoodsHolder)holder).bindVisitData();
                    break;

            }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_SITE_BRAND;
        } else if (position == 2) {
            return TYPE_BRAND_ACTIVITY;
        }else if (position == 3) {
            return TYPE_GOODS_DETAIL;
        } else if (position == 4) {
            return TYPE_SERVICE_HELP;
        } else if (position == 5) {
            return TYPE_MATCH;
        } else if (position == 6) {
            return TYPE_WORK_LIST;
        } else if (position == 7) {
            return TYPE_SOURCE;
        } else if (position == 8) {
            return TYPE_VIST_GOODS;
        } else if (position == 9) {
            return TYPE_RECOMMEND;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return goodsDetailEntity == null ? 0 : 10;
    }

    // banner商品图
    class GoodsDetailBannerHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView ivThemeIcon;
        private FrameLayout flytIdViewpager;
        private Indicator indicator;
        private IndicatorViewPager indicatorViewPager;
        private SimpleDraweeView ivGoodsColor;
        private LinearLayout llytGoodsColors;
        private TextView tvGoodsName;
        private TextView tvPrice;
        private TextView tvListPrice;
        private LinearLayout llytForSeller;
        private TextView tvForSeller;
        private TextView tvForSellerProportion;
        private TextView tvForward;
        private TextView tvRecommend;
        private LinearLayout llytOne;
        private LinearLayout llytTwo;
        private LinearLayout llytThree;
        private LinearLayout llytFour;
        private LinearLayout llytFive;

        public GoodsDetailBannerHolder(View itemView) {
            super(itemView);
            ivThemeIcon = (SimpleDraweeView) itemView.findViewById(R.id.iv_theme_icon);
            ivThemeIcon.setImageURI(Util.transferImage(BaseApplication.getInstance().getThemeIcon(), SimpleUtils.dp2px(mContext, 39)));

            flytIdViewpager = (FrameLayout) itemView.findViewById(R.id.flyt_goods_viewpager);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flytIdViewpager.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = layoutParams.width;
            flytIdViewpager.setLayoutParams(layoutParams);
            ViewPager viewPager = (ViewPager) itemView.findViewById(R.id.goods_viewPager);
            indicator = (Indicator) itemView.findViewById(R.id.goods_indicator);
            indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
            colorImages = goodsDetailEntity.getGoods_colors().get(0).getImages();
            indicatorViewPager.setAdapter(creatBannerAdapter());

            llytGoodsColors = (LinearLayout) itemView.findViewById(R.id.rycv_goods_colors);
            if(goodsDetailEntity.getGoods_colors().size() > 1){
                int size = goodsDetailEntity.getGoods_colors().size();
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                llytOne = (LinearLayout) itemView.findViewById(R.id.llyt_one);
                llytTwo = (LinearLayout) itemView.findViewById(R.id.llyt_two);
                llytThree = (LinearLayout) itemView.findViewById(R.id.llyt_three);
                llytFour = (LinearLayout) itemView.findViewById(R.id.llyt_four);
                llytFive = (LinearLayout) itemView.findViewById(R.id.llyt_five);
                addColorView(size, layoutInflater);
            }else {
                llytGoodsColors.setVisibility(View.GONE);
            }

            tvGoodsName = (TextView) itemView.findViewById(R.id.home_detail_name);
            tvGoodsName.setText(goodsDetailEntity.getBrand().getName() + goodsDetailEntity.getGoods().getTitle());

            tvPrice = (TextView) itemView.findViewById(R.id.home_detail_price_now);
            tvPrice.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
            if(isGroupBuy){
                tvPrice.setText("￥" + Util.FloatKeepZero(goodsDetailEntity.getSkus().get(0).getGroupbuy_price()));
            }else {
                if (goodsDetailEntity.getGoods_colors().get(0).getMin_price() == goodsDetailEntity.getGoods_colors().get(0).getMax_price()) {
                    tvPrice.setText("￥" + Util.FloatKeepZero(
                        goodsDetailEntity.getGoods_colors().get(0).getMin_price()));
                } else {
                    tvPrice.setText("￥" + Util.FloatKeepZero(
                        goodsDetailEntity.getGoods_colors().get(0).getMin_price()) + "~" +
                        Util.FloatKeepZero(goodsDetailEntity.getGoods_colors().get(0).getMax_price()));
                }
            }


            tvListPrice = (TextView) itemView.findViewById(R.id.home_detail_price_old);
            tvListPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvListPrice.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
            priceMax = goodsDetailEntity.getGoods().getList_price();
            //for (int i = 0; i < goodsDetailEntity.getSkus().size(); i++){
            //    if(priceMax < goodsDetailEntity.getSkus().get(i).getList_price()){
            //        priceMax = goodsDetailEntity.getSkus().get(i).getList_price();
            //    }
            //}
            tvListPrice.setText("￥" + Util.FloatKeepZero(priceMax));

            if(goodsDetailEntity.getGoods_colors().get(0).getMax_price() < priceMax) {
                tvListPrice.setVisibility(View.VISIBLE);
            }else {
                tvListPrice.setVisibility(View.GONE);
            }
            llytForSeller = (LinearLayout) itemView.findViewById(R.id.llyt_for_seller);
            if(!TextUtils.isEmpty(goodsDetailEntity.getGoods().getFor_seller())) {
                llytForSeller.setVisibility(View.VISIBLE);
                tvForSeller = (TextView) itemView.findViewById(R.id.tv_for_seller);
                tvForSeller.setText(goodsDetailEntity.getGoods().getFor_seller());
                tvForSellerProportion = (TextView) itemView.findViewById(R.id.tv_for_seller_proportion);
                tvForSellerProportion.setText(goodsDetailEntity.getGoods().getShare_profit());
                tvForward = (TextView) itemView.findViewById(R.id.tv_forward);
                tvForward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onActionClickListener != null){
                            onActionClickListener.OnForwardClick();
                        }
                    }
                });
                tvRecommend = (TextView) itemView.findViewById(R.id.tv_recommend);
                if(goodsDetailEntity.getGoods().is_recommend()){
                    tvRecommend.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvRecommend.setText("[ 已推荐 ]");
                    tvRecommend.setTextColor(Color.parseColor("#9b9b9b"));
                    tvRecommend.setEnabled(false);
                }
                tvRecommend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onActionClickListener != null){
                            onActionClickListener.OnRecommendClick(tvRecommend);
                        }
                    }
                });
            }else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lp.height = SimpleUtils.dp2px(mContext, 30);
                llytForSeller.setLayoutParams(lp);
            }

        }

        private void addColorView(int size, LayoutInflater layoutInflater) {
            if(size < 10){
                llytOne.setVisibility(View.VISIBLE);
                for (int i = 0; i < size; i++){
                    View view = creatView(i, layoutInflater);
                    llytOne.addView(view);
                }
            }else if(size < 19){
                llytOne.setVisibility(View.VISIBLE);
                llytTwo.setVisibility(View.VISIBLE);
                for (int i = 0; i < size; i++){
                    if(i < 9) {
                        View view = creatView(i, layoutInflater);
                        llytOne.addView(view);
                    }else {
                        View view = creatView(i, layoutInflater);
                        llytTwo.addView(view);
                    }
                }
            }else if(size < 28){
                llytOne.setVisibility(View.VISIBLE);
                llytTwo.setVisibility(View.VISIBLE);
                llytThree.setVisibility(View.VISIBLE);
                for (int i = 0; i < size; i++){
                    if(i < 9) {
                        View view = creatView(i, layoutInflater);
                        llytOne.addView(view);
                    }else if (i < 18){
                        View view = creatView(i, layoutInflater);
                        llytTwo.addView(view);
                    }else {
                        View view = creatView(i, layoutInflater);
                        llytThree.addView(view);
                    }
                }
            }else if(size < 37){
                llytOne.setVisibility(View.VISIBLE);
                llytTwo.setVisibility(View.VISIBLE);
                llytThree.setVisibility(View.VISIBLE);
                llytFour.setVisibility(View.VISIBLE);
                for (int i = 0; i < size; i++){
                    if(i < 9) {
                        View view = creatView(i, layoutInflater);
                        llytOne.addView(view);
                    }else if (i < 18){
                        View view = creatView(i, layoutInflater);
                        llytTwo.addView(view);
                    }else if(i < 27){
                        View view = creatView(i, layoutInflater);
                        llytThree.addView(view);
                    }else {
                        View view = creatView(i, layoutInflater);
                        llytFour.addView(view);
                    }
                }
            }else {
                llytOne.setVisibility(View.VISIBLE);
                llytTwo.setVisibility(View.VISIBLE);
                llytThree.setVisibility(View.VISIBLE);
                llytFour.setVisibility(View.VISIBLE);
                llytFive.setVisibility(View.VISIBLE);
                for (int i = 0; i < size; i++){
                    if(i < 9) {
                        View view = creatView(i, layoutInflater);
                        llytOne.addView(view);
                    }else if (i < 18){
                        View view = creatView(i, layoutInflater);
                        llytTwo.addView(view);
                    }else if(i < 27){
                        View view = creatView(i, layoutInflater);
                        llytThree.addView(view);
                    }else if (i < 36){
                        View view = creatView(i, layoutInflater);
                        llytFour.addView(view);
                    }else {
                        View view = creatView(i, layoutInflater);
                        llytFive.addView(view);
                    }
                }
            }
        }

        private View creatView(int i, LayoutInflater layoutInflater) {
            View view = layoutInflater.inflate(R.layout.goods_detail_colors_item, null);
            final SimpleDraweeView ivColor = (SimpleDraweeView) view.findViewById(R.id.iv_color);
            if(i == selectedPositon){
                ivColor.setSelected(true);
            }else {
                ivColor.setSelected(false);
            }
            ivColor.setTag(i);
            if(goodsDetailEntity.getGoods_colors().get(i).getImages()!= null && goodsDetailEntity.getGoods_colors().get(i).getImages().size() > 0){
                ivColor.setImageURI(Util.transferImage(goodsDetailEntity.getGoods_colors().get(i).getImages().get(0).getImage(), SimpleUtils.dp2px(mContext, 17)));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int oldPosition = selectedPositon;
                    selectedPositon = (int)ivColor.getTag();
                    if(oldPosition == selectedPositon){
                        return;
                    }
                    ivColor.setSelected(true);
                    if(oldPosition < 9){
                        llytOne.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout l = (LinearLayout) llytOne.getChildAt(oldPosition);
                                SimpleDraweeView sv = (SimpleDraweeView) l.getChildAt(0);
                                sv.setSelected(false);
                            }
                        }, 50);
                    }else if(oldPosition < 18){
                        llytTwo.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout l = (LinearLayout) llytTwo.getChildAt(oldPosition - 9);
                                SimpleDraweeView sv = (SimpleDraweeView) l.getChildAt(0);
                                sv.setSelected(false);
                            }
                        }, 50);
                    }else if(oldPosition < 27){
                        llytThree.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout l = (LinearLayout) llytThree.getChildAt(oldPosition - 18);
                                SimpleDraweeView sv = (SimpleDraweeView) l.getChildAt(0);
                                sv.setSelected(false);
                            }
                        }, 50);
                    }else if (oldPosition < 36){
                        llytFour.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout l = (LinearLayout) llytFour.getChildAt(oldPosition - 27);
                                SimpleDraweeView sv = (SimpleDraweeView) l.getChildAt(0);
                                sv.setSelected(false);
                            }
                        }, 50);
                    }else {
                        llytFive.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout l = (LinearLayout) llytFive.getChildAt(oldPosition - 36);
                                SimpleDraweeView sv = (SimpleDraweeView) l.getChildAt(0);
                                sv.setSelected(false);
                            }
                        }, 50);
                    }
                    colorImages = goodsDetailEntity.getGoods_colors().get(selectedPositon).getImages();
                    indicatorViewPager.setAdapter(creatBannerAdapter());
                    indicatorViewPager.notifyDataSetChanged();
                    if(!isGroupBuy){
                        if (goodsDetailEntity.getGoods_colors().get(selectedPositon).getMin_price()
                            == goodsDetailEntity.getGoods_colors().get(selectedPositon).getMax_price()) {
                            final String value = "￥" + Util.FloatKeepZero(
                                goodsDetailEntity.getGoods_colors().get(selectedPositon).getMin_price());
                            tvPrice.postDelayed(new Runnable() {
                                @Override public void run() {
                                    tvPrice.setText(value);
                                }
                            }, 100);
                        } else {
                            final String value = "￥" + Util.FloatKeepZero(
                                goodsDetailEntity.getGoods_colors().get(selectedPositon).getMin_price()) + "~" +
                                Util.FloatKeepZero(goodsDetailEntity.getGoods_colors()
                                    .get(selectedPositon)
                                    .getMax_price());
                            tvPrice.postDelayed(new Runnable() {
                                @Override public void run() {
                                    tvPrice.setText(value);
                                }
                            }, 100);
                        }
                    }


                    if(goodsDetailEntity.getGoods_colors().get(selectedPositon).getMax_price() < priceMax) {
                        tvListPrice.postDelayed(new Runnable() {
                            @Override public void run() {
                                tvListPrice.setVisibility(View.VISIBLE);
                            }
                        }, 100);

                    }else {
                        tvListPrice.postDelayed(new Runnable() {
                            @Override public void run() {
                                tvListPrice.setVisibility(View.GONE);
                            }
                        }, 100);
                    }

                }
            });
            return view;
        }

        private IndicatorViewPager.IndicatorPagerAdapter creatBannerAdapter() {
            return new IndicatorViewPager.IndicatorViewPagerAdapter() {
                @Override
                public int getCount() {
                    return colorImages.size();
                }

                @Override
                public View getViewForTab(int i, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        view = LayoutInflater.from(mContext).inflate(R.layout.tab_indicator_guide, viewGroup, false);
                    }
                    if (colorImages.size() == 1) {
                        view.setVisibility(View.INVISIBLE);
                    }else{
                        view.setVisibility(View.VISIBLE);
                    }
                    return view;
                }

                @Override
                public View getViewForPage(int i, View view, ViewGroup viewGroup) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.goods_detail_banner, null);
                    ivGoodsColor = (SimpleDraweeView) view.findViewById(R.id.iv_color);
                    ivGoodsColor.setImageURI(Util.transferImage(colorImages.get(i).getImage(), SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 100)));
                    ivGoodsColor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 放大图层
                            Intent intent = new Intent(mContext, HtmlActicity.class);
                            HtmlImageListObject htmlobj = new HtmlImageListObject();
                            ArrayList<HtmlImageListItem> list = new ArrayList<>();
                            for (int i = 0; i < colorImages.size(); i++) {
                                HtmlImageListItem item = new HtmlImageListItem();
                                item.setUrl(colorImages.get(i).getImage());
                                list.add(item);
                            }
                            htmlobj.setList(list);
                            intent.putExtra("imageurl", new Gson().toJson(htmlobj));
                            mContext.startActivity(intent);
                            ((NewGoodsDetailActivity) mContext).overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
                        }
                    });
                    return view;
                }
            };
        }
    }

    // 商品网站和品牌
    class GoodsDetailSiteBrandHolder extends RecyclerView.ViewHolder{

        private SimpleDraweeView ivFlag;
        private RelativeLayout rlytFlag;
        private RelativeLayout rlytBrand;
        private TextView tvSite;
        private TextView tvDeliver;
        private TextView tvDiscountFee;
        private SimpleDraweeView ivBrand;
        private TextView tvBrand;
        private ImageView fbFollowBrand;

        public GoodsDetailSiteBrandHolder(View itemView) {
            super(itemView);
            ivFlag = (SimpleDraweeView) itemView.findViewById(R.id.iv_flag);
            ivFlag.setImageURI(Util.transferImage(goodsDetailEntity.getCountry().getFlag(), SimpleUtils.dp2px(mContext, 40)), R.mipmap.work_default);
            rlytFlag = (RelativeLayout) itemView.findViewById(R.id.rlyt_flg);
            rlytFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewMallGoodsActivity.class);
                    intent.putExtra("id", goodsDetailEntity.getSite().getId());
                    mContext.startActivity(intent);
                }
            });
            rlytBrand = (RelativeLayout) itemView.findViewById(R.id.rlyt_brand);
            rlytBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                    intent.putExtra("brand_id", goodsDetailEntity.getBrand().getId());
                    mContext.startActivity(intent);
                }
            });
            tvSite = (TextView) itemView.findViewById(R.id.tv_site);
            tvSite.setText(goodsDetailEntity.getSite().getDes());
            tvDeliver = (TextView) itemView.findViewById(R.id.tv_delivery_des);
            tvDeliver.setText("[" + goodsDetailEntity.getSite().getDelivery_des() + "]");
            tvDiscountFee = (TextView) itemView.findViewById(R.id.tv_discount_fee);
            if(!TextUtils.isEmpty(goodsDetailEntity.getGoods().getDiscount_fee())){
                tvDiscountFee.setText("(" + goodsDetailEntity.getGoods().getDiscount_fee() + ")");
            }else {
                tvDiscountFee.setVisibility(View.GONE);
            }

            ivBrand = (SimpleDraweeView) itemView.findViewById(R.id.iv_brand);
            ivBrand.setImageURI(Util.transferImage(goodsDetailEntity.getBrand().getLogo(), SimpleUtils.dp2px(mContext, 56)), R.mipmap.work_default);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
            tvBrand.setText(goodsDetailEntity.getBrand().getName());
            fbFollowBrand = (ImageView) itemView.findViewById(R.id.fb_follow_brand);
            if (goodsDetailEntity.getBrand().getFollowed() == 1) {
                fbFollowBrand.setImageResource(R.mipmap.ic_followed);
            } else {
                fbFollowBrand.setImageResource(R.mipmap.ic_tofollow);
            }
            fbFollowBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onActionClickListener != null){
                        onActionClickListener.OnBrandFollowClick(fbFollowBrand);
                    }
                }
            });

    }

    }

    class cBannerImageHolderView implements Holder<ActivityListObject.ActivityListItem> {
        private SimpleDraweeView imageView;

        @Override
        public View createView(Context context) {
            imageView = new SimpleDraweeView(context);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final ActivityListObject.ActivityListItem data) {
            String strBg = Util.transferImage(data.getCover(), SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 32));
            imageView.setImageURI(strBg, R.mipmap.work_default);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳活动详情
                    Intent intent = new Intent(mContext, HomeIssueActivity.class);
                    intent.putExtra("activityId", String.valueOf(data.getId()));
                    intent.putExtra("menuId", 7);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    class GoodsDetailBrandActivityHolder extends RecyclerView.ViewHolder{
        private ConvenientBanner cbBrandAct;

        public GoodsDetailBrandActivityHolder(final View itemView) {
            super(itemView);
            cbBrandAct = (ConvenientBanner) itemView.findViewById(R.id.cbanner_brand_activity);
            if(brandActObj != null) {
                if (brandActObj.getList() != null && brandActObj.getList().size() > 0) {
                    cbBrandAct.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cbBrandAct.getLayoutParams();
                    layoutParams.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 32);
                    layoutParams.height = layoutParams.width * 177/349;
                    layoutParams.rightMargin = SimpleUtils.dp2px(mContext, 16);
                    layoutParams.leftMargin = SimpleUtils.dp2px(mContext, 16);
                    cbBrandAct.setLayoutParams(layoutParams);
                    cbBrandAct.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
                    cbBrandAct.startTurning(4000);
                    cbBrandAct.setPages(new CBViewHolderCreator<cBannerImageHolderView>() {
                        @Override
                        public cBannerImageHolderView createHolder() {
                            return new cBannerImageHolderView();
                        }
                    }, brandActObj.getList());
                } else {
                    setItemView(itemView);
                }
            }else {
                setItemView(itemView);
            }
        }
    }

    class GoodsDetailHolder extends RecyclerView.ViewHolder{
        private TextView tvGoodsDetailTitle;
        private MyWebView tvGoodsDetailDes;
        private View vLine;
        private TextView tvTranslat;
        private MyWebView tvGoodsDetailDesTran;


        public GoodsDetailHolder(View itemView) {
            super(itemView);
            tvGoodsDetailTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvGoodsDetailTitle.setText("商品详情");
            tvGoodsDetailDes = (MyWebView) itemView.findViewById(R.id.tv_desc);
            tvGoodsDetailDes.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    return true;
                }
            });
            tvGoodsDetailDes.loadDataWithBaseURL(null, transerDesc(goodsDetailEntity.getGoods().getDesc()), "text/html", "UTF-8", null);
            vLine = itemView.findViewById(R.id.view_line);
            tvTranslat = (TextView) itemView.findViewById(R.id.tv_trans);
            tvTranslat.setText(Html.fromHtml("<u>" + "自动翻译" + "</u>"));

            tvGoodsDetailDesTran = (MyWebView) itemView.findViewById(R.id.tv_desc_trans);
            tvGoodsDetailDesTran.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(View v) {
                    return true;
                }
            });

            tvTranslat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tvTranslat.getText().toString().equals("谷歌翻译")){
                        tvGoodsDetailDesTran.setVisibility(View.GONE);
                        tvTranslat.setText(Html.fromHtml("<u>" + "自动翻译" + "</u>"));
                    }else {
                        if(beTransed) {
                            tvGoodsDetailDesTran.setVisibility(View.VISIBLE);
                            tvTranslat.setText("谷歌翻译");
                        }else {
                            showPreDialog("");
                            new RetrofitRequest<GoodsTranlateDesc>(ApiRequest.getApiShiji()
                                .getGoodsTranslateDesc(goodsDetailEntity.getGoods().getId())).handRequest(
                                new MsgCallBack() {
                                    @Override public void onResult(HttpMessage msg) {
                                        hidePreDialog();
                                        if (msg.isSuccess()) {
                                            vLine.setVisibility(View.VISIBLE);
                                            tvTranslat.setText("谷歌翻译");
                                            //tvTranslat.setEnabled(false);
                                            beTransed = true;
                                            GoodsTranlateDesc desc = (GoodsTranlateDesc) msg.obj;
                                            tvGoodsDetailDesTran.loadDataWithBaseURL(null, transerDesc(desc.getTranslated_desc()), "text/html",
                                                "UTF-8", null);
                                            tvGoodsDetailDesTran.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                        }
                    }

                }
            });

        }

        private String transerDesc(String s) {
            //String s = "去除以下标签：\n"
            //    + "1 <img  …  >\n"
            //    + "2 <video  …  ></video>\n"
            //    + "3 <audio  …  ></audio>\n"
            //    + "4 <embed  … >\n"
            //    + "5 <object   … ></object>\n"
            //    + "6 <source   … >\n"
            //    + "7 <script    … ></script>\n"
            //    + "8 <style     … ></style>\n"
            //    + "9 <applet    … ></applet>\n"
            //    + "10 <param   … >";
            String regImg = "<img[^>]*?>";
            String regVideo = "<video[^>]*?>[\\s\\S]*?<\\/video>";
            String regAudio = "<audio[^>]*?>[\\s\\S]*?<\\/audio>";
            String regEmbed = "<embed[^>]*?>";
            String regObj = "<object[^>]*?>[\\s\\S]*?<\\/object>";
            String regSource = "<source[^>]*?>";
            String regScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";
            String regStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";
            String regApplet = "<applet[^>]*?>[\\s\\S]*?<\\/applet>";
            String regParam = "<param[^>]*?>";
            Pattern p_img=Pattern.compile(regImg, Pattern.CASE_INSENSITIVE);
            Matcher m_img=p_img.matcher(s);
            s=m_img.replaceAll("");

            Pattern p_video=Pattern.compile(regVideo, Pattern.CASE_INSENSITIVE);
            Matcher m_video=p_video.matcher(s);
            s=m_video.replaceAll("");

            Pattern p_audio=Pattern.compile(regAudio, Pattern.CASE_INSENSITIVE);
            Matcher m_audio=p_audio.matcher(s);
            s=m_audio.replaceAll("");

            Pattern p_embed=Pattern.compile(regEmbed, Pattern.CASE_INSENSITIVE);
            Matcher m_embed=p_embed.matcher(s);
            s=m_embed.replaceAll("");

            Pattern p_obj=Pattern.compile(regObj, Pattern.CASE_INSENSITIVE);
            Matcher m_obj=p_obj.matcher(s);
            s=m_obj.replaceAll("");

            Pattern p_source=Pattern.compile(regSource, Pattern.CASE_INSENSITIVE);
            Matcher m_source=p_source.matcher(s);
            s=m_source.replaceAll("");

            Pattern p_script=Pattern.compile(regScript, Pattern.CASE_INSENSITIVE);
            Matcher m_script=p_script.matcher(s);
            s=m_script.replaceAll("");

            Pattern p_style=Pattern.compile(regStyle, Pattern.CASE_INSENSITIVE);
            Matcher m_style=p_style.matcher(s);
            s=m_style.replaceAll("");

            Pattern p_applet=Pattern.compile(regApplet, Pattern.CASE_INSENSITIVE);
            Matcher m_applet=p_applet.matcher(s);
            s=m_applet.replaceAll("");

            Pattern p_param=Pattern.compile(regParam, Pattern.CASE_INSENSITIVE);
            Matcher m_param=p_param.matcher(s);
            s=m_param.replaceAll("");
            //Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            return Configration.HTML_STYLE_HEAD + s + Configration.HTML_STYLE_TAIL;
        }
    }

    class GoodsDetailServeHelpHolder extends RecyclerView.ViewHolder{
        private ImageView ivServe;
        private ImageView ivMoreLiucheng;
        private ImageView ivLiucheng;

        public GoodsDetailServeHelpHolder(View itemView) {
            super(itemView);
            ivServe = (ImageView) itemView.findViewById(R.id.iv_service);
            ivMoreLiucheng = (ImageView) itemView.findViewById(R.id.iv_more_liucheng);
            ivLiucheng = (ImageView) itemView.findViewById(R.id.iv_liucheng);

            ivServe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onActionClickListener != null){
                        onActionClickListener.OnServiceClick();
                    }
                }
            });

            ivMoreLiucheng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ivLiucheng.getVisibility() == View.GONE){
                        ivLiucheng.setVisibility(View.VISIBLE);
                        ivMoreLiucheng.setImageResource(R.mipmap.jianhao);
                    }else {
                        ivLiucheng.setVisibility(View.GONE);
                        ivMoreLiucheng.setImageResource(R.mipmap.jiahao);
                    }
                }
            });
        }
    }

    class GoodsDetailMatchHolder extends RecyclerView.ViewHolder{
        private TextView tvMatchTitle;
        private RecyclerView rycvMatch;
        private GoodsDetailMatchAdapter mMatchAdapter;

        public GoodsDetailMatchHolder(final View itemView) {
            super(itemView);
            tvMatchTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvMatchTitle.setText("搭配推荐");
            rycvMatch = (RecyclerView) itemView.findViewById(R.id.home_detail_horizon_rycv);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rycvMatch.setLayoutManager(linearLayoutManager);
            rycvMatch.setItemAnimator(new DefaultItemAnimator());

        }

        public void bindMatchData(){
            if(matchObj != null) {
                if (matchObj.list != null && matchObj.list.size() > 0) {
                    mMatchAdapter = new GoodsDetailMatchAdapter(mContext, matchObj.list);
                    rycvMatch.setAdapter(mMatchAdapter);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rycvMatch.getLayoutParams();
                    lp.width = SimpleUtils.getScreenWidth(mContext);
                    lp.height = SimpleUtils.dp2px(mContext, 160);
                    lp.leftMargin = SimpleUtils.dp2px(mContext, 11);
                    lp.rightMargin = SimpleUtils.dp2px(mContext, 11);
                    lp.bottomMargin = SimpleUtils.dp2px(mContext, 13);
                    rycvMatch.setLayoutParams(lp);
                } else {
                    setItemView(itemView);
                }
            }else {
                setItemView(itemView);
            }
        }
    }

    class GoodsDetailWorkListHolder extends RecyclerView.ViewHolder{
        private TextView tvWorkListTitle;
        private ImageView ivWorkTitleMore;
        private RecyclerView rycvWorkList;
        private GoodsDetailWorkListAdapter mWorkListAdapter;

        public GoodsDetailWorkListHolder(final View itemView) {
            super(itemView);
            tvWorkListTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvWorkListTitle.setText("该品牌晒单");
            ivWorkTitleMore = (ImageView) itemView.findViewById(R.id.iv_title_more);
            ivWorkTitleMore.setVisibility(View.VISIBLE);
            ivWorkTitleMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳gai品牌晒单列表页
                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                        intent.putExtra("brand_id", goodsDetailEntity.getBrand().getId());
                        intent.putExtra("type", 2);
                        mContext.startActivity(intent);
                }
            });
            rycvWorkList = (RecyclerView) itemView.findViewById(R.id.home_detail_horizon_rycv);

        }

        public void bindWorkListData(){
            if(workObj != null) {
                if (workObj.list != null && workObj.list.size() > 0) {
                    rycvWorkList.setItemAnimator(new DefaultItemAnimator());
                    LinearLayoutManager linearm = new LinearLayoutManager(mContext);
                    linearm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rycvWorkList.setLayoutManager(linearm);
                    mWorkListAdapter = new GoodsDetailWorkListAdapter(mContext, workObj.list);
                    rycvWorkList.setAdapter(mWorkListAdapter);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rycvWorkList.getLayoutParams();
                    lp.width = SimpleUtils.getScreenWidth(mContext);
                    lp.height = (lp.width - SimpleUtils.dp2px(mContext, 52)) * 180 / 322;
                    lp.leftMargin = SimpleUtils.dp2px(mContext, 11);
                    lp.rightMargin = SimpleUtils.dp2px(mContext, 11);
                    lp.bottomMargin = SimpleUtils.dp2px(mContext, 30);
                    rycvWorkList.setLayoutParams(lp);
                } else {
                    setItemView(itemView);
                }
            }else {
                setItemView(itemView);
            }
        }
    }

    class GoodsDetailSourceHolder extends RecyclerView.ViewHolder{
        private TextView tvGoodsDetailSourceTitle;
        private ImageView ivGoSite;
        private SimpleDraweeView ivSite;
        private TextView tvSiteDesc;

        public GoodsDetailSourceHolder(View itemView) {
            super(itemView);
            tvGoodsDetailSourceTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvGoodsDetailSourceTitle.setText("商品来源");
            ivGoSite = (ImageView) itemView.findViewById(R.id.iv_go_site);
            ivGoSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentGoSite = new Intent(mContext, NewMallGoodsActivity.class);
                    intentGoSite.putExtra("id", goodsDetailEntity.getSite().getId());
                    mContext.startActivity(intentGoSite);

                }
            });
            ivSite = (SimpleDraweeView) itemView.findViewById(R.id.iv_site);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivSite.getLayoutParams();
            lp.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 128);
            lp.height = lp.width * 72/120;
            lp.topMargin = SimpleUtils.dp2px(mContext, 9);
            ivSite.setLayoutParams(lp);
            ivSite.setImageURI(Util.transferImage(goodsDetailEntity.getSite().getImage(), SimpleUtils.dp2px(mContext, lp.width)));
            tvSiteDesc = (TextView) itemView.findViewById(R.id.tv_site_desc);
            tvSiteDesc.setText("\u3000\u3000" + goodsDetailEntity.getSite().getDescription());
        }
    }

    class GoodsDetailVistGoodsHolder extends RecyclerView.ViewHolder{
        private TextView tvVisitTitle;
        private RecyclerView rycvVisit;
        private GoodsDetailVisitAdapter mVisitAdapter;

        public GoodsDetailVistGoodsHolder(final View itemView) {
            super(itemView);
            tvVisitTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvVisitTitle.setText("浏览记录");
            rycvVisit = (RecyclerView) itemView.findViewById(R.id.home_detail_horizon_rycv);

        }

        public void bindVisitData(){
            if(visitObj != null) {
                if (visitObj.getList() != null && visitObj.getList().size() > 0) {
                    rycvVisit.setItemAnimator(new DefaultItemAnimator());
                    LinearLayoutManager lm = new LinearLayoutManager(mContext);
                    lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rycvVisit.setLayoutManager(lm);
                    mVisitAdapter = new GoodsDetailVisitAdapter(mContext, visitObj.getList());
                    rycvVisit.setAdapter(mVisitAdapter);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rycvVisit.getLayoutParams();
                    lp.height = SimpleUtils.dp2px(mContext, 160);
                    lp.bottomMargin = SimpleUtils.dp2px(mContext, 14);
                    rycvVisit.setLayoutParams(lp);
                } else {
                    setItemView(itemView);
                }
            }else {
                setItemView(itemView);
            }
        }
    }

    class GoodsDetailRecommendHolder extends RecyclerView.ViewHolder{
        private TextView tvRecommendTitle;
        private LinearLayout llytLeft;
        private LinearLayout llytRight;
        View viewOne;
        View viewTwo;
        View viewThree;
        View viewFour;
        View viewFive;
        View viewSix;

        public GoodsDetailRecommendHolder(View itemView) {
            super(itemView);
            tvRecommendTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvRecommendTitle.setText("推荐商品");
            llytLeft = (LinearLayout) itemView.findViewById(R.id.llyt_left);
            llytRight = (LinearLayout) itemView.findViewById(R.id.llyt_right);

            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mLayoutParams.topMargin = SimpleUtils.dp2px(mContext, 2);
            mLayoutParams.bottomMargin = SimpleUtils.dp2px(mContext, 7);
            viewOne = layoutInflater.inflate(R.layout.goods_detail_recommend_item, null);
            viewOne.setLayoutParams(mLayoutParams);
            viewTwo = layoutInflater.inflate(R.layout.goods_detail_recommend_item, null);
            viewTwo.setLayoutParams(mLayoutParams);
            viewThree = layoutInflater.inflate(R.layout.goods_detail_recommend_item, null);
            viewThree.setLayoutParams(mLayoutParams);
            viewFour = layoutInflater.inflate(R.layout.goods_detail_recommend_item, null);
            viewFour.setLayoutParams(mLayoutParams);
            viewFive = layoutInflater.inflate(R.layout.goods_detail_recommend_item, null);
            viewFive.setLayoutParams(mLayoutParams);
            viewSix = layoutInflater.inflate(R.layout.goods_detail_recommend_item, null);
            viewSix.setLayoutParams(mLayoutParams);
            llytLeft.addView(viewOne);
            llytLeft.addView(viewThree);
            llytLeft.addView(viewFive);
            llytRight.addView(viewTwo);
            llytRight.addView(viewFour);
            llytRight.addView(viewSix);

            if(recommendObj != null) {
                if (recommendObj != null && recommendObj.getList().size() > 0) {
                    setRecommendView(recommendObj.getList());
                } else {
                    setItemView(itemView);
                }
            }else {
                setItemView(itemView);
            }

            itemView.findViewById(R.id.view_bottom).setVisibility(View.GONE);
        }

        private void setRecommendView(ArrayList<NewGoodsItem> list) {
            for (int i = 0; i < list.size(); i++){
                if(i > 5){
                    return;
                }
                bindRecomdData(i, list.get(i));
            }
        }

        private void bindRecomdData(int i, final NewGoodsItem item) {
            switch (i){
                case 0:
                    bindData(viewOne, item);
                    break;
                case 1:
                    bindData(viewTwo, item);
                    break;
                case 2:
                    bindData(viewThree, item);
                    break;
                case 3:
                    bindData(viewFour, item);
                    break;
                case 4:
                    bindData(viewFive, item);
                    break;
                case 5:
                    bindData(viewSix, item);
                    break;
            }
        }

        private void bindData(View viewOne, final NewGoodsItem item){
            TextView tvBrand = (TextView) viewOne.findViewById(R.id.item_brand);
            TextView tvTxt = (TextView) viewOne.findViewById(R.id.item_title);
            SimpleDraweeView ivImg = (SimpleDraweeView) viewOne.findViewById(R.id.item_img);
            TextView tvNow = (TextView) viewOne.findViewById(R.id.item_now_price);
            TextView tvOrignal = (TextView) viewOne.findViewById(R.id.item_orginal_price);
            LinearLayout llytDiscount = (LinearLayout) viewOne.findViewById(R.id.discount_layout);
            TextView tvFrom = (TextView) viewOne.findViewById(R.id.goods_from_text);
            ImageView ivSoldout = (ImageView) viewOne.findViewById(R.id.shop_image_sold_out);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivImg.getLayoutParams();

            if (item.getStatus() == 3) {
                ivSoldout.setVisibility(View.INVISIBLE);
            } else {
                ivSoldout.setVisibility(View.VISIBLE);
            }
            int nWidth = SimpleUtils.getScreenWidth(mContext) / 2 - SimpleUtils.dp2px(mContext, 14);
            if (!TextUtils.isEmpty(item.getCover())) {
                ivImg.setImageURI(Util.transferCropImage(item.getCover(), nWidth), R.mipmap.work_default);
            }
            params.width = nWidth;
            params.height = item.getCover_info().getHeight() * params.width / item.getCover_info().getWidth();
            ivImg.setLayoutParams(params);

            tvBrand.setText(item.getBrand());
            tvTxt.setText(item.getTitle());
            tvNow.setText("￥" + item.getPrice());
            tvNow.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));

            tvFrom.setText(item.getSite());

            llytDiscount.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getList_price())) {
                if (item.getPrice().equals(item.getList_price())) {
                    tvOrignal.setVisibility(View.GONE);
                } else {
                    tvOrignal.setVisibility(View.VISIBLE);
                    tvOrignal.setText("￥" + item.getList_price());
                    tvOrignal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tvOrignal.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/Helvetica Condensed Bold.ttf"));
                }
            }

            viewOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoGoodsDetail(item.getId());

                }
            });
            viewOne.setVisibility(View.VISIBLE);
        }

    }

    private void setItemView(View itemView){
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.height = 0;
        layoutParams.width = 0;
        itemView.setLayoutParams(layoutParams);
    }

    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        mContext.startActivity(intent);
    }

    public interface OnActionClickListener{
        void OnBrandFollowClick(ImageView floatingActionButton);
        void OnServiceClick();
        void OnForwardClick();
        void OnRecommendClick(TextView tvRecommend);
    }

    public void setOnActionClickListener(OnActionClickListener onActionClickListener){
        this.onActionClickListener = onActionClickListener;
    }

    public void showPreDialog(String str) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(mContext, str, false);
        progressDialog.show();
    }

    public void hidePreDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
