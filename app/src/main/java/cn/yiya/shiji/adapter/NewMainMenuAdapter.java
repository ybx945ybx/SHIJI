package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewGoodsListActivity;
import cn.yiya.shiji.activity.NewLocalWebActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BrandsItem;
import cn.yiya.shiji.entity.BrandsObject;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.LogstashReport.MainMenuHotCategoryLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuSpecialSaleLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuThemeLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuTopBannerLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuTopClotheLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainMenuTopInLogstashItem;
import cn.yiya.shiji.entity.MainBannerInfo;
import cn.yiya.shiji.entity.NewGoodsObject;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.FullyGridLayoutManager;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.PagerContainer;

/**
 * 商城分类
 * Created by Amy on 2016/8/17.
 */
public class NewMainMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String mId; //菜单编号
    private String mName;//菜单名称

    public final static int BANNER = 11; //banner
    public final static int TODAY_DEAL = 22; //今日特卖
    public final static int TOP_IN = 33; //至in单品
    public final static int PROMOTION = 44; //活动
    public final static int HOT_CATEGORY = 55; //热门品类
    public final static int TOP_BRAND = 66; //top品牌
    public final static int THEMATIC_TITLE = 77; //专题精选标题
    public final static int THEMATIC = 88; //专题精选

    private List<NewGoodsItem> mList;
    public final static int HEAD_SIZE = 6;

    private ConvenientBanner banner;

    private LoginListener mListener;

    public NewMainMenuAdapter(Context context, String menuId, String menuName, List<NewGoodsItem> mList, LoginListener mListener) {
        this.mContext = context;
        this.mId = menuId;
        this.mName = menuName;
        this.mList = mList;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:
                return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_banner, parent, false));
            case TODAY_DEAL:
                return new SpecialViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_today_deal, parent, false));
            case TOP_IN:
                return new TopInViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_top_in, parent, false));
            case PROMOTION:
                return new PromotionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_promotion, parent, false));
            case HOT_CATEGORY:
                return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_hot_category, parent, false));
            case TOP_BRAND:
                return new BrandViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_top_brand, parent, false));
            case THEMATIC_TITLE:
                return new ThematicTitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_thematic_title, parent, false));
            case THEMATIC:
                return new ThematicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_thematic_list, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ThematicViewHolder) {
            ThematicViewHolder viewHolder = (ThematicViewHolder) holder;
            final NewGoodsItem item = mList.get(position - HEAD_SIZE - 1);
//            Netroid.displayImage(Util.ClipImageBannerView(item.getCover()), viewHolder.ivThematicImage);
            viewHolder.ivThematicImage.setImageURI(Util.ClipImageBannerView(item.getCover()));
            viewHolder.ivThematicImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打点事件
                    MainMenuThemeLogstashItem mainMenuThemeLogstashItem = new MainMenuThemeLogstashItem();
                    mainMenuThemeLogstashItem.setType_name(mName);
                    mainMenuThemeLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainMenuThemeLogstashItem.setBanner_id(item.getId());
                    mainMenuThemeLogstashItem.setBanner_title(item.getTitle());
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMenuThemeEvent(mainMenuThemeLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    gotoActivityDetail(item.getId());
                }
            });
            viewHolder.rvGoodsList.setAdapter(new ThematicChildAdapter(item.getGoodses(), item.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return HEAD_SIZE + (mList == null ? 0 : (mList.size() + 1));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER;
        } else if (position == 1) {
            return TODAY_DEAL;
        } else if (position == 2) {
            return TOP_IN;
        } else if (position == 3) {
            return PROMOTION;
        } else if (position == 4) {
            return HOT_CATEGORY;
        } else if (position == 5) {
            return TOP_BRAND;
        } else if (position == 6) {
            return THEMATIC_TITLE;
        } else {
            return THEMATIC;
        }
    }


    /*====================================广告栏====================================================*/
    class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(final View itemView) {
            super(itemView);
            banner = (ConvenientBanner) itemView.findViewById(R.id.main_banner);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();

            new RetrofitRequest<MainBannerInfo>(ApiRequest.getApiShiji().getMainBanner(mId)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MainBannerInfo obj = (MainBannerInfo) msg.obj;
                        if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {
                            layoutParams.height = 0;
                            banner.setLayoutParams(layoutParams);
                            itemView.findViewById(R.id.line).setVisibility(View.VISIBLE);
                            return;
                        }
                        layoutParams.width = SimpleUtils.getScreenWidth(mContext);
                        layoutParams.height = layoutParams.width * 192 / 375;
                        banner.setLayoutParams(layoutParams);

                        banner.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
                        banner.startTurning(4000);

                        itemView.findViewById(R.id.viewline).setVisibility(View.VISIBLE);
                        banner.setPages(new CBViewHolderCreator<NetworkImageViewHolder>() {
                            @Override
                            public NetworkImageViewHolder createHolder() {
                                return new NetworkImageViewHolder();
                            }
                        }, obj.getList());
                    }
                }
            });
        }
    }

    class NetworkImageViewHolder implements Holder<MainBannerInfo.ListEntity> {
        private ImageView imageView;
        private SimpleDraweeView ivThemeIcon;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.new_main_banner, null);
            imageView = (ImageView) view.findViewById(R.id.iv_new_main_banner);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = layoutParams.width * 192 / 375;
            imageView.setLayoutParams(layoutParams);
            ivThemeIcon = (SimpleDraweeView) view.findViewById(R.id.iv_theme_icon);
            ivThemeIcon.setImageURI(Util.transferImage(BaseApplication.getInstance().getThemeIcon(), SimpleUtils.dp2px(mContext, 39)));
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, final MainBannerInfo.ListEntity data) {
            imageView.setImageResource(R.drawable.user_dafault);
            Netroid.displayImage(Util.ClipImageBannerView(data.getCover()), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  打点事件
                    MainMenuTopBannerLogstashItem mainMenuTopBannerLogstashItem = new MainMenuTopBannerLogstashItem();
                    mainMenuTopBannerLogstashItem.setType_name(mName);
                    mainMenuTopBannerLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainMenuTopBannerLogstashItem.setBanner_id(data.getId());
                    mainMenuTopBannerLogstashItem.setBanner_title(data.getTitle());
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMenuBannerEvent(mainMenuTopBannerLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    switch (data.getType()) {
                        case 1:           // 活动
                            Intent intent = new Intent(mContext, HomeIssueActivity.class);
                            intent.putExtra("activityId", data.getId());
                            intent.putExtra("menuId", mId);
                            mContext.startActivity(intent);
                            break;
                        case 2:           // 专题
                            Intent intent1 = new Intent(mContext, NewLocalWebActivity.class);
                            HtmlThematicInfo info = new HtmlThematicInfo();
                            info.setItemId(data.getId());
                            info.setTypeId("3");
                            String dataJson = new Gson().toJson(info);
                            intent1.putExtra("data", dataJson);
                            intent1.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");
                            intent1.putExtra("type", data.getType());
                            mContext.startActivity(intent1);
                            break;
                        case 3:           // h5分享
                            Intent intent2 = new Intent(mContext, NewLocalWebActivity.class);
                            intent2.putExtra("url", data.getUrl());
                            intent2.putExtra("type", data.getType());
                            intent2.putExtra("title", data.getTitle());
                            intent2.putExtra("data", new Gson().toJson(data));
                            mContext.startActivity(intent2);
                            break;
                    }
                }
            });
        }
    }

    /*====================================今日特卖====================================================*/
    class SpecialViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        public SpecialViewHolder(final View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();

            new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getMenuTodaySpecial(mId))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                NewGoodsObject obj = (NewGoodsObject) msg.obj;
                                if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {
                                    layoutParams.height = 0;
                                    recyclerView.setLayoutParams(layoutParams);
                                    itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                                    return;
                                }
                                recyclerView.setAdapter(new SpecialAdapter(obj.getList()));
                            } else {
                                layoutParams.height = 0;
                                recyclerView.setLayoutParams(layoutParams);
                                itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    class SpecialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<NewGoodsItem> itemList;

        public SpecialAdapter(List<NewGoodsItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SpecialItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_today_deal_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SpecialItemViewHolder viewHolder = (SpecialItemViewHolder) holder;
            final NewGoodsItem item = itemList.get(position);

//            Netroid.displayImage(Util.transfer(item.getCover()), viewHolder.ivGoodsImage);
            viewHolder.ivGoodsImage.setImageURI(Util.transfer(item.getCover()));
            viewHolder.tvBrand.setText(item.getBrand());
            viewHolder.tvGoods.setText(item.getTitle());
            viewHolder.tvPrice.setText("¥ " + item.getPrice());
            if (TextUtils.isEmpty(item.getList_price()) || item.getList_price().equals(item.getPrice())) {
                viewHolder.tvOldPrice.setVisibility(View.GONE);
                viewHolder.viewOldPrice.setVisibility(View.GONE);
            } else {
                viewHolder.tvOldPrice.setText("¥ " + item.getList_price());
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  打点事件
                    MainMenuSpecialSaleLogstashItem mainMenuSpecialSaleLogstashItem = new MainMenuSpecialSaleLogstashItem();
                    mainMenuSpecialSaleLogstashItem.setType_name(mName);
                    mainMenuSpecialSaleLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainMenuSpecialSaleLogstashItem.setGoods_id(item.getId());
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMenuSpecialSaleEvent(mainMenuSpecialSaleLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    gotoGoodsDetail(item.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        class SpecialItemViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout rlSpecial;
            SimpleDraweeView ivGoodsImage;
            TextView tvBrand;
            TextView tvGoods;
            TextView tvPrice;
            TextView tvOldPrice;
            View viewOldPrice;

            public SpecialItemViewHolder(View itemView) {
                super(itemView);
                rlSpecial = (RelativeLayout) itemView.findViewById(R.id.rl_special);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlSpecial.getLayoutParams();
                layoutParams.width = SimpleUtils.getScreenWidth(mContext);
                rlSpecial.setLayoutParams(layoutParams);

                ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
                tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
                tvGoods = (TextView) itemView.findViewById(R.id.tv_goods);
                tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
                tvOldPrice = (TextView) itemView.findViewById(R.id.tv_old_price);
                viewOldPrice = itemView.findViewById(R.id.view_old_price);
            }
        }
    }

    /*====================================至in单品====================================================*/
    class TopInViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout llTopIn;
        RecyclerView recyclerView;

        public TopInViewHolder(final View itemView) {
            super(itemView);
            llTopIn = (RelativeLayout) itemView.findViewById(R.id.ll_top_in);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llTopIn.getLayoutParams();

            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

            new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getMenuTopToday(mId))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                NewGoodsObject obj = (NewGoodsObject) msg.obj;
                                if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {
                                    layoutParams.height = 0;
                                    llTopIn.setLayoutParams(layoutParams);
                                    itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                                    return;
                                }
                                recyclerView.setAdapter(new TopInAdapter(obj.getList()));

                            } else {
                                layoutParams.height = 0;
                                llTopIn.setLayoutParams(layoutParams);
                                itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                            }
                        }
                    });
        }

    }

    class TopInAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<NewGoodsItem> itemList;

        public TopInAdapter(List<NewGoodsItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TopInItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_top_in_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TopInItemViewHolder viewHolder = (TopInItemViewHolder) holder;
            final NewGoodsItem item = itemList.get(position);

//            Netroid.displayImage(Util.transfer(item.getCover()), viewHolder.ivGoodsImage);
            viewHolder.ivGoodsImage.setImageURI(Util.transferCropImage(item.getCover(), SimpleUtils.dp2px(mContext, 80)));
            viewHolder.tvTop.setText("TOP " + (position + 1));
            viewHolder.tvPrice.setText("¥ " + item.getPrice());
            viewHolder.tvDesc.setText(item.getTitle());
            viewHolder.tvBrand.setText(item.getBrand());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  打点事件
                    MainMenuTopInLogstashItem mainMenuTopInLogstashItem = new MainMenuTopInLogstashItem();
                    mainMenuTopInLogstashItem.setType_name(mName);
                    mainMenuTopInLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainMenuTopInLogstashItem.setGoods_id(item.getId());
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMenuTopInEvent(mainMenuTopInLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    gotoGoodsDetail(item.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        class TopInItemViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivGoodsImage;
            TextView tvTop;
            TextView tvPrice;
            TextView tvDesc;
            TextView tvBrand;

            public TopInItemViewHolder(View itemView) {
                super(itemView);
                ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
                tvTop = (TextView) itemView.findViewById(R.id.tv_top);
                tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
                tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
                tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
            }
        }
    }

    /*====================================促销专题====================================================*/
    class PromotionViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llPromotion;
        RecyclerView recyclerView;

        public PromotionViewHolder(View itemView) {
            super(itemView);
            llPromotion = (LinearLayout) itemView.findViewById(R.id.ll_promotion);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llPromotion.getLayoutParams();
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new FullyGridLayoutManager(mContext, 2));
            recyclerView.setNestedScrollingEnabled(false);

            new RetrofitRequest<MainBannerInfo>(ApiRequest.getApiShiji().getMenuPromotion(mId))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                MainBannerInfo obj = (MainBannerInfo) msg.obj;
                                if (obj == null || obj.getList() == null || obj.getList().isEmpty()) {
                                    layoutParams.height = 0;
                                    llPromotion.setLayoutParams(layoutParams);
                                    return;

                                } else {
                                    List<MainBannerInfo.ListEntity> itemList = obj.getList();
                                    recyclerView.setAdapter(new PromotionAdapter(itemList));
                                }
                            } else {
                                layoutParams.height = 0;
                                llPromotion.setLayoutParams(layoutParams);
                            }
                        }
                    });
        }
    }

    class PromotionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<MainBannerInfo.ListEntity> itemList;

        public PromotionAdapter(List<MainBannerInfo.ListEntity> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PromotionItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_promotion_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PromotionItemViewHolder viewHolder = (PromotionItemViewHolder) holder;
            final MainBannerInfo.ListEntity data = itemList.get(position);

//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.ivPromotionImage.getLayoutParams();
//            layoutParams.width = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 26)) / 2;
//            layoutParams.height = layoutParams.width * 89 / 175;
//            viewHolder.ivPromotionImage.setLayoutParams(layoutParams);
//            Netroid.displayImage(Util.clipImageViewByWH(Util.transfer(data.getCover()), 374, 190), viewHolder.ivPromotionImage);

//            Netroid.displayImage(Util.transfer(data.getCover()), viewHolder.ivPromotionImage);
            viewHolder.ivPromotionImage.setImageURI(Util.transfer(data.getCover()));

            viewHolder.ivPromotionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (data.getType()) {
                        case 1:           // 活动
                            Intent intent = new Intent(mContext, HomeIssueActivity.class);
                            intent.putExtra("activityId", data.getId());
                            intent.putExtra("menuId", mId);
                            mContext.startActivity(intent);
                            break;
                        case 2:           // 专题
                            Intent intent1 = new Intent(mContext, NewLocalWebActivity.class);
                            HtmlThematicInfo info = new HtmlThematicInfo();
                            info.setItemId(data.getId());
                            info.setTypeId("3");
                            String dataJson = new Gson().toJson(info);
                            intent1.putExtra("data", dataJson);
                            intent1.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");
                            intent1.putExtra("type", data.getType());
                            mContext.startActivity(intent1);
                            break;
                        case 3:           // h5分享
                            Intent intent2 = new Intent(mContext, NewLocalWebActivity.class);
                            intent2.putExtra("url", data.getUrl());
                            intent2.putExtra("type", data.getType());
                            intent2.putExtra("title", data.getTitle());
                            intent2.putExtra("data", new Gson().toJson(data));
                            mContext.startActivity(intent2);
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : (itemList.size() > 2 ? 2 : itemList.size());
        }

        class PromotionItemViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivPromotionImage;

            public PromotionItemViewHolder(View itemView) {
                super(itemView);
                ivPromotionImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_promotion_image);
            }
        }
    }

    /*====================================热门品类====================================================*/
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout llCategory;
        RecyclerView recyclerView;

        public CategoryViewHolder(final View itemView) {
            super(itemView);
            llCategory = (RelativeLayout) itemView.findViewById(R.id.ll_category);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llCategory.getLayoutParams();
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new FullyGridLayoutManager(mContext, 3));

            new RetrofitRequest<HotCategoryObject>(ApiRequest.getApiShiji().getMenuCategoryList(mId))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                HotCategoryObject obj = (HotCategoryObject) msg.obj;
                                if (obj != null && obj.getList() != null && !obj.getList().isEmpty()) {
                                    recyclerView.setAdapter(new CategoryAdapter(obj.getList()));
                                } else {
                                    layoutParams.height = 0;
                                    llCategory.setLayoutParams(layoutParams);
                                    itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        }
    }

    class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<HotCategoryObject.SecondItem> itemList;

        public CategoryAdapter(List<HotCategoryObject.SecondItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_item_hot_category_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CategoryItemViewHolder viewHolder = (CategoryItemViewHolder) holder;
            final HotCategoryObject.SecondItem item = itemList.get(position);
//            Netroid.displayImage(Util.transfer(item.getCover()), viewHolder.ivCategory);
            viewHolder.ivCategory.setImageURI(Util.transferCropImage(item.getCover(), SimpleUtils.dp2px(mContext, 56)));
            viewHolder.tvName.setText(item.getName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 打点事件
                    MainMenuHotCategoryLogstashItem mainMenuHotCategoryLogstashItem = new MainMenuHotCategoryLogstashItem();
                    mainMenuHotCategoryLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainMenuHotCategoryLogstashItem.setType_name(mName);
                    mainMenuHotCategoryLogstashItem.setCategory_name(item.getName());
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMenuHotCategoryEvent(mainMenuHotCategoryLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    //跳转到商品列表页
                    Intent intent = new Intent(mContext, NewGoodsListActivity.class);
                    intent.putExtra("source", 1001);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.size();
        }

        class CategoryItemViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivCategory;
            TextView tvName;

            public CategoryItemViewHolder(View itemView) {
                super(itemView);
                ivCategory = (SimpleDraweeView) itemView.findViewById(R.id.iv_category_image);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
            }
        }
    }

    /*====================================top品牌====================================================*/
    class BrandViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout llTopBrand;
        private TextView tvTitle;
        private PagerContainer container;

        public BrandViewHolder(final View itemView) {
            super(itemView);
            llTopBrand = (RelativeLayout) itemView.findViewById(R.id.ll_top_brand);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llTopBrand.getLayoutParams();
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            container = (PagerContainer) itemView.findViewById(R.id.main_container);
            container.setHasAlpha(false);
            container.setMinAlpha(0.95f);
            container.setMinScale(0.9f);
//            container.setMargin(6);
            final ViewPager pager = container.getViewPager();

            new RetrofitRequest<BrandsObject>(ApiRequest.getApiShiji().getMenuBrandRank(mId))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                BrandsObject obj = (BrandsObject) msg.obj;
                                if (obj == null || obj.getList() == null || obj.list.isEmpty()) {
                                    layoutParams.height = 0;
                                    llTopBrand.setLayoutParams(layoutParams);
                                    itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                                    return;
                                }

                                List<BrandsItem> itemList = obj.list;
                                tvTitle.setText(obj.getTitle());
                                pager.setAdapter(new CoverFlowAdapter(itemList));
                                pager.setClipChildren(false);
                                pager.setCurrentItem(itemList.size() * 100 / 2, false);
                                pager.setOffscreenPageLimit(3);
                            } else {
                                layoutParams.height = 0;
                                llTopBrand.setLayoutParams(layoutParams);
                                itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                            }
                        }
                    });

        }
    }

    class CoverFlowAdapter extends PagerAdapter {
        private List<BrandsItem> itemList;

        public CoverFlowAdapter(List<BrandsItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.main_item_top_brand_list, null);
            final int nPosition = position % itemList.size();
            final BrandsItem item = itemList.get(nPosition);

            SimpleDraweeView ivBrand = (SimpleDraweeView) view.findViewById(R.id.iv_brand);
            SimpleDraweeView ivBrandLogo = (SimpleDraweeView) view.findViewById(R.id.iv_brand_logo);

            final ImageView btnFollow = (ImageView) view.findViewById(R.id.btn_follow);

            ivBrand.setImageURI(Util.ClipImageFlashSaleView(item.getImage()));
            ivBrandLogo.setImageURI(Util.transfer(item.getLogo()));

            if (item.getFollow() == 1) {
                btnFollow.setImageResource(R.mipmap.ic_followed_trans);
            } else {
                btnFollow.setImageResource(R.mipmap.ic_tofollow_trans);
            }

            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getFollow() == 0) {
                        if (mListener != null) {
                            mListener.goLogin();
                        }
                        return;
                    }
                    if (item.getFollow() == 1) {
                        new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(String.valueOf(item.getTag_id()))).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    Util.toast(mContext, "已取消订阅", true);
                                    item.setFollow(2);
                                    btnFollow.setImageResource(R.mipmap.ic_tofollow_trans);
                                } else {
                                    Util.toast(mContext, "取消失败", true);
                                }
                            }
                        });
                    } else {
                        new RetrofitRequest<>(ApiRequest.getApiShiji().postFollowBrands(String.valueOf(item.getTag_id()))).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    Util.toast(mContext, "已订阅", true);
                                    item.setFollow(1);
                                    btnFollow.setImageResource(R.mipmap.ic_followed_trans);
                                } else {
                                    Util.toast(mContext, "订阅失败", true);
                                }
                            }
                        });
                    }
                }
            });

            ivBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  打点事件
                    MainMenuTopClotheLogstashItem mainMenuTopClotheLogstashItem = new MainMenuTopClotheLogstashItem();
                    mainMenuTopClotheLogstashItem.setType_name(mName);
                    mainMenuTopClotheLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainMenuTopClotheLogstashItem.setBrand_id(String.valueOf(item.getId()));
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMenuTopClotheEvent(mainMenuTopClotheLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                    intent.putExtra("brand_id", item.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return itemList.size() * 100;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    /*====================================专题精选标题====================================================*/
    class ThematicTitleViewHolder extends RecyclerView.ViewHolder {

        public ThematicTitleViewHolder(View itemView) {
            super(itemView);
        }
    }

    /*====================================专题精选====================================================*/
    class ThematicViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivThematicImage;
        RecyclerView rvGoodsList;
        SimpleDraweeView ivThemIcon;

        public ThematicViewHolder(View itemView) {
            super(itemView);
            ivThematicImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_thematic_image);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivThematicImage.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = layoutParams.width * 192 / 375;
            ivThematicImage.setLayoutParams(layoutParams);

            rvGoodsList = (RecyclerView) itemView.findViewById(R.id.rv_goods_list);
            rvGoodsList.setLayoutManager(new FullyLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

            ivThemIcon = (SimpleDraweeView) itemView.findViewById(R.id.iv_theme_icon);
            ivThemIcon.setImageURI(Util.transferImage(BaseApplication.getInstance().getThemeIcon(), SimpleUtils.dp2px(mContext, 39)));
        }
    }

    class ThematicChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<NewGoodsItem> itemList;

        public final static int GOODS = 111;
        public final static int MORE = 112;

        private String activityId;

        public ThematicChildAdapter(List<NewGoodsItem> itemList, String activityId) {
            this.itemList = itemList;
            this.activityId = activityId;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case GOODS:
                    return new ThematicChildViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.main_item_thematic_child_list, parent, false));
                case MORE: //查看更多
                    return new ThematicMoreViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.main_item_thematic_child_more, parent, false));
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ThematicChildViewHolder) {
                ThematicChildViewHolder viewHolder = (ThematicChildViewHolder) holder;
                final NewGoodsItem item = itemList.get(position);
//                Netroid.displayImage(Util.transferCropImage(item.getCover(),SimpleUtils.dp2px(mContext,110)), viewHolder.ivGoodsImage);
                viewHolder.ivGoodsImage.setImageURI(Util.transferCropImage(item.getCover(), SimpleUtils.dp2px(mContext, 110)));
                viewHolder.tvBrand.setText(item.getBrand());
                viewHolder.tvPrice.setText("¥" + item.getPrice());

                if(item.getDiscount() == 10){
                    viewHolder.llytDiscount.setVisibility(View.GONE);
                }else {
                    viewHolder.llytDiscount.setVisibility(View.VISIBLE);
                    viewHolder.tvDiscount.setText(String.valueOf((int) item.getDiscount()));
                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoGoodsDetail(item.getId());
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position < itemList.size())
                return GOODS;
            else return MORE;
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : (itemList.size() + 1);
        }

        class ThematicChildViewHolder extends RecyclerView.ViewHolder {
            SimpleDraweeView ivGoodsImage;
            TextView tvBrand;
            TextView tvPrice;
            LinearLayout llytDiscount;
            TextView tvDiscount;

            public ThematicChildViewHolder(View itemView) {
                super(itemView);
                ivGoodsImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_goods_image);
                tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
                tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
                llytDiscount = (LinearLayout) itemView.findViewById(R.id.llyt_discount);
                tvDiscount = (TextView) itemView.findViewById(R.id.tv_discount);
            }
        }

        class ThematicMoreViewHolder extends RecyclerView.ViewHolder {

            public ThematicMoreViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoActivityDetail(activityId);
                    }
                });
            }
        }
    }


    /**
     * 跳转到活动详情
     *
     * @param activityId
     */
    private void gotoActivityDetail(String activityId) {
        Intent intent = new Intent(mContext, HomeIssueActivity.class);
        intent.putExtra("activityId", activityId);
        intent.putExtra("menuId", mId);
        mContext.startActivity(intent);
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

    public interface LoginListener {
        void goLogin();
    }

}
