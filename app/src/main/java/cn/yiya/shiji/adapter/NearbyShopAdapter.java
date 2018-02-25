package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CouponDetailActivity;
import cn.yiya.shiji.activity.MapWebViewActivity;
import cn.yiya.shiji.activity.ShopSortActivity;
import cn.yiya.shiji.activity.StoreListActivity;
import cn.yiya.shiji.activity.TravelMallActivity;
import cn.yiya.shiji.activity.TravelStoreActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.navigation.MallDetailInfo;
import cn.yiya.shiji.entity.navigation.RecommendInfo;
import cn.yiya.shiji.entity.navigation.ShopHoursInfo;
import cn.yiya.shiji.entity.navigation.StoreCategoryInfo;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.entity.navigation.StoreShortInfo;
import cn.yiya.shiji.fragment.HotShopFragment;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/4/6.
 */
public class NearbyShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isClick;

    public static final int BANNER_TYPE = 11;
    public static final int MALL_TYPE = 22;
    public static final int SHOP_TYPE = 33;
    public static final int OTHER_TYPE = 44;
    public static final int RECOMMEND_TYPE = 55;
    public static final int TXT_TYPE = 66;
    public static final int LIST_TYPE = 77;

    private Context mContext;
    private MallDetailInfo mInfos;
    private ArrayList<BannerItem> mBannerLists;
    private ArrayList<StoreCategoryInfo> mTitleLists;
    private ArrayList<StoreShortInfo> mLists;
    private ArrayList<RecommendInfo> mRecLists;
    private String countryId;
    private String cityId;
    private String id;
    private boolean bNet;
    private int recommendType;

    public NearbyShopAdapter(Context mContext, MallDetailInfo mInfos, ArrayList<BannerItem> mBannerLists,
                             ArrayList<StoreCategoryInfo> mTitleLists, ArrayList<StoreShortInfo> mLists,
                             ArrayList<RecommendInfo> mRecLists, String countryId, String cityId, String id
            , boolean net, int recommendType) {
        this.mContext = mContext;
        this.mInfos = mInfos;
        this.mBannerLists = mBannerLists;
        this.mTitleLists = mTitleLists;
        this.mLists = mLists;
        this.mRecLists = mRecLists;
        this.countryId = countryId;
        this.cityId = cityId;
        this.id = id;
        this.bNet = net;
        this.recommendType = recommendType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER_TYPE:
                return new BannerViewHolder(parent);
            case MALL_TYPE:
                return new MallInfoViewHolder(parent);
            case SHOP_TYPE:
                return new ShopViewHolder(parent);
            case OTHER_TYPE:
                return new OtherShopViewHolder(parent);
            case RECOMMEND_TYPE:
                return new RecommendViewHolder(parent);
            case TXT_TYPE:
                return new ShopNewByTxtViewHolder(parent);
            case LIST_TYPE:
                return new NearbyShopViewHolder(parent);
            default:
                throw new RuntimeException("Unknown issue type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case BANNER_TYPE:
                ((BannerViewHolder) holder).bindData(mBannerLists);
                break;
            case MALL_TYPE:
                ((MallInfoViewHolder) holder).bindData(mInfos);
                break;
            case OTHER_TYPE:
                ((OtherShopViewHolder) holder).bindData(mInfos.getBranch_count());
                break;
            case RECOMMEND_TYPE:
                ((RecommendViewHolder) holder).bindData(mRecLists);
                break;
            case LIST_TYPE:
                final StoreShortInfo info = mLists.get(position - 6);
                NearbyShopViewHolder pHolder = (NearbyShopViewHolder) holder;

                pHolder.tvShopName.setText(info.getName());
                pHolder.tvShopCategroy.setText(info.getCategory());
                pHolder.tvShopDistance.setText(info.getDistance());
                Netroid.displayImage(info.getCover(), pHolder.ivShopCover);
                if (info.getRate().equals("")) {
                    pHolder.ratingBar.setRating(0.0f);
                } else {
                    pHolder.ratingBar.setRating(Float.parseFloat(info.getRate()));
                }
                pHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (Integer.parseInt(info.getMall()) == 1) {
                            intent = new Intent(mContext, TravelMallActivity.class);
                        } else {
                            intent = new Intent(mContext, TravelStoreActivity.class);
                        }
                        intent.putExtra("id", info.getId());
                        mContext.startActivity(intent);
                    }
                });
                break;

        }
    }

    @Override
    public int getItemCount() {
        if (mInfos == null) {
            return 1;
        } else {
            return mLists.size() + 6;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER_TYPE;
        } else if (position == 1) {
            return MALL_TYPE;
        } else if (position == 2) {
            return SHOP_TYPE;
        } else if (position == 3) {
            return OTHER_TYPE;
        } else if (position == 4) {
            return RECOMMEND_TYPE;
        } else if (position == 5) {
            return TXT_TYPE;
        } else {
            return LIST_TYPE;
        }
    }

    protected class RecommendViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView rycvRecommend;
        public TextView tvTxt;

        public RecommendViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_footer_hotmessage_head, parent, false));
            rycvRecommend = (RecyclerView) itemView.findViewById(R.id.recommend_recycler);
            tvTxt = (TextView) itemView.findViewById(R.id.recommend_txt);
            tvTxt.setText("柿集推荐");

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rycvRecommend.setLayoutManager(linearLayoutManager);
            rycvRecommend.setItemAnimator(new DefaultItemAnimator());
            rycvRecommend.setNestedScrollingEnabled(false);

            LinearLayout.LayoutParams rycvLayoutParams = (LinearLayout.LayoutParams) rycvRecommend.getLayoutParams();
            rycvLayoutParams.height = (SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 48)) * 2 / 5;
            rycvLayoutParams.width = SimpleUtils.getScreenWidth(mContext);
            rycvRecommend.setLayoutParams(rycvLayoutParams);
            if (mRecLists.size() == 0) {
                itemView.setVisibility(View.GONE);
                rycvRecommend.setVisibility(View.GONE);
                return;
            }
        }

        public void bindData(ArrayList<RecommendInfo> mlist) {
            if (mRecLists.size() == 0) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
                return;
            }
            RecommendBuyAdapter recommendBuyAdapter = new RecommendBuyAdapter(mContext, recommendType, mlist);
            rycvRecommend.setAdapter(recommendBuyAdapter);
        }
    }

    protected class OtherShopViewHolder extends RecyclerView.ViewHolder {

        TextView tvBranchShopNum;
        RelativeLayout rlytBranch;

        public OtherShopViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_shop, parent, false));
            tvBranchShopNum = (TextView) itemView.findViewById(R.id.tv_branch_shop_num);
            rlytBranch = (RelativeLayout) itemView.findViewById(R.id.rlyt_branch);

        }

        public void bindData(String num) {

            if (Integer.parseInt(num) > 0) {
                tvBranchShopNum.setText("其他分店（" + num + "）");
                itemView.setVisibility(View.VISIBLE);
                rlytBranch.setVisibility(View.VISIBLE);
                rlytBranch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMoreBranch = new Intent(mContext, StoreListActivity.class);
                        intentMoreBranch.putExtra("id", id);
                        mContext.startActivity(intentMoreBranch);
                    }
                });
            } else {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
            }

            if (!bNet) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
            }
        }
    }

    protected class ShopViewHolder extends RecyclerView.ViewHolder {

        private TabLayout tabHotShop;                                   // 热门店铺TabLayout
        private ViewPager vpHotShop;
        RelativeLayout rlyt;
        TextView tvTxt;

        public ShopViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_footer_hotmessage_head, parent, false));
            itemView.setVisibility(View.GONE);
            tabHotShop = (TabLayout) itemView.findViewById(R.id.tab_hot_shop);
            tabHotShop.setTabMode(TabLayout.MODE_SCROLLABLE);
            vpHotShop = (ViewPager) itemView.findViewById(R.id.viewpager_hot_shop);

            tvTxt = (TextView) itemView.findViewById(R.id.hot_more_txt);

            HotShopFragmentPageAdapter adapter;
            if (mContext instanceof TravelMallActivity) {
                adapter = new HotShopFragmentPageAdapter(((TravelMallActivity) mContext).getSupportFragmentManager());
            } else {
                adapter = new HotShopFragmentPageAdapter(((TravelStoreActivity) mContext).getSupportFragmentManager());
            }

            vpHotShop.setAdapter(adapter);
            tabHotShop.setTabsFromPagerAdapter(adapter);
            tabHotShop.setupWithViewPager(vpHotShop);

            if (bNet) {
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfMall(
                        MapRequest.setStoreListOfMall(0, id, countryId, cityId, "", 1))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg);
                    }
                });
            } else {
                GetLocalRequest.getInstance().getLocalStoreList("mall", id, new Handler(Looper.getMainLooper()), countryId, cityId, 0, new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg);
                    }
                });
            }

            rlyt = (RelativeLayout) itemView.findViewById(R.id.more_shop_layout);
            if (!NetUtil.IsInNetwork(mContext)) {
                rlyt.setClickable(false);
                tvTxt.setVisibility(View.GONE);
                tabHotShop.setVisibility(View.GONE);
            }

            rlyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMoreShop = new Intent(mContext, ShopSortActivity.class);
                    intentMoreShop.putExtra("id", id);
                    intentMoreShop.putExtra("type", 2);
                    intentMoreShop.putExtra("cityId", cityId);
                    intentMoreShop.putExtra("countryId", countryId);
                    mContext.startActivity(intentMoreShop);
                }
            });
        }

        private void handleMessage(HttpMessage msg) {
            {
                if (msg.isSuccess()) {
                    StoreObject object = (StoreObject) msg.obj;
                    if (object.getCount() != null) {
                        int count = Integer.parseInt(object.getCount());
                        if (count > 3) {
                            LinearLayout.LayoutParams rycvLayoutParams = (LinearLayout.LayoutParams) vpHotShop.getLayoutParams();
                            rycvLayoutParams.width = SimpleUtils.getScreenWidth(mContext);
                            rycvLayoutParams.height = SimpleUtils.dp2px(mContext, 32) + (((SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 64)) / 3) + SimpleUtils.dp2px(mContext, 64)) * 2;
                            vpHotShop.setLayoutParams(rycvLayoutParams);
                            itemView.setVisibility(View.VISIBLE);
                        } else if (count > 0) {
                            LinearLayout.LayoutParams rycvLayoutParams = (LinearLayout.LayoutParams) vpHotShop.getLayoutParams();
                            rycvLayoutParams.width = SimpleUtils.getScreenWidth(mContext);
                            rycvLayoutParams.height = SimpleUtils.dp2px(mContext, 32) + (((SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 64)) / 3) + SimpleUtils.dp2px(mContext, 64));
                            vpHotShop.setLayoutParams(rycvLayoutParams);
                            itemView.setVisibility(View.VISIBLE);
                        } else {
                            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                            lp.height = 0;
                            lp.width = 0;
                            itemView.setLayoutParams(lp);
                            return;
                        }
                    } else {
                        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                        lp.height = 0;
                        lp.width = 0;
                        itemView.setLayoutParams(lp);
                        return;
                    }
                } else {

                }
            }
        }
    }

    public class HotShopFragmentPageAdapter extends FragmentPagerAdapter {

        public HotShopFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return HotShopFragment.instanceFragment(bNet, countryId, cityId, id, mTitleLists.get(position).getId(), 2);
        }

        @Override
        public int getCount() {
            return mTitleLists.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleLists.get(position).getName();
        }
    }

    protected class MallInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMallBriefEll;                                // 商场简介
        private TextView tvMallBreif;                                   // 展开后的商场简介
        private TextView tvMallBreifTry;
        private ImageView ivMoreBrief;                                  // 更多简介按钮

        private TextView tvMallAddress;                                 // 商场地址
        private ImageView ivMallAddress;                                // 商场地址呼出谷歌地图按钮

        private TextView tvMallPhone;                                   // 商场电话

        private LinearLayout llytGround;                                // 商场平面图板块
        private RecyclerView rycvMallGround;                            // 商场平面图
        private MallGroundAdapter mallGroundAdapter;

        private LinearLayout llytMallTime;                              // 商场营业时间板块
        private LinearLayout llytMon;
        private TextView tvMon;
        private TextView tvMonMorning;
        private TextView tvMonAfternoon;
        private LinearLayout llytTues;
        private TextView tvTues;
        private TextView tvTuesMorning;
        private TextView tvTuesAfternoon;
        private LinearLayout llytWed;
        private TextView tvWed;
        private TextView tvWedMorning;
        private TextView tvWedAfternoon;
        private LinearLayout llytThur;
        private TextView tvThur;
        private TextView tvThurMorning;
        private TextView tvThurAfternoon;
        private LinearLayout llytFri;
        private TextView tvFri;
        private TextView tvFriMorning;
        private TextView tvFriAfternoon;
        private LinearLayout llytSat;
        private TextView tvSat;
        private TextView tvSatMorning;
        private TextView tvSatAfternoon;
        private LinearLayout llytSun;
        private TextView tvSun;
        private TextView tvSunMorning;
        private TextView tvSunAfternoon;
        private ImageView ivMoreTime;                                   // 显示更多营业时间按钮
        ArrayList<ShopHoursInfo> shopHoursList;

        private RelativeLayout relativeLayout_phone;

        public MallInfoViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travel_mall_header, parent, false));

            tvMallBriefEll = (TextView) itemView.findViewById(R.id.tv_mall_brief_ell);
            tvMallBriefEll.setVisibility(View.VISIBLE);
            tvMallBreif = (TextView) itemView.findViewById(R.id.tv_mall_brief);
            tvMallBreif.setVisibility(View.GONE);
            tvMallBreifTry = (TextView) itemView.findViewById(R.id.tv_mall_brief_try);
            ivMoreBrief = (ImageView) itemView.findViewById(R.id.more_brief);

            tvMallAddress = (TextView) itemView.findViewById(R.id.tv_mall_address);
            ivMallAddress = (ImageView) itemView.findViewById(R.id.iv_anchor);

            tvMallPhone = (TextView) itemView.findViewById(R.id.tv_mall_phone);


            llytGround = (LinearLayout) itemView.findViewById(R.id.llyt_ground);
            rycvMallGround = (RecyclerView) itemView.findViewById(R.id.rycv_mall_ground);
            rycvMallGround.setItemAnimator(new DefaultItemAnimator());
            LinearLayoutManager layoutManagerHor = new LinearLayoutManager(mContext);
            layoutManagerHor.setOrientation(LinearLayoutManager.HORIZONTAL);
            rycvMallGround.setLayoutManager(layoutManagerHor);
            rycvMallGround.setNestedScrollingEnabled(false);


            llytMallTime = (LinearLayout) itemView.findViewById(R.id.llyt_time);
            llytMon = (LinearLayout) itemView.findViewById(R.id.llyt_mon);
            tvMon = (TextView) itemView.findViewById(R.id.tv_mon);
            tvMonMorning = (TextView) itemView.findViewById(R.id.tv_mon_moring);
            tvMonAfternoon = (TextView) itemView.findViewById(R.id.tv_mon_afternoon);
            llytTues = (LinearLayout) itemView.findViewById(R.id.llyt_tues);
            tvTues = (TextView) itemView.findViewById(R.id.tv_tues);
            tvTuesMorning = (TextView) itemView.findViewById(R.id.tv_tues_moring);
            tvTuesAfternoon = (TextView) itemView.findViewById(R.id.tv_tues_afternoon);
            llytWed = (LinearLayout) itemView.findViewById(R.id.llyt_wed);
            tvWed = (TextView) itemView.findViewById(R.id.tv_wed);
            tvWedMorning = (TextView) itemView.findViewById(R.id.tv_wed_moring);
            tvWedAfternoon = (TextView) itemView.findViewById(R.id.tv_wed_afternoon);
            llytThur = (LinearLayout) itemView.findViewById(R.id.llyt_thur);
            tvThur = (TextView) itemView.findViewById(R.id.tv_thur);
            tvThurMorning = (TextView) itemView.findViewById(R.id.tv_thur_moring);
            tvThurAfternoon = (TextView) itemView.findViewById(R.id.tv_thur_afternoon);
            llytFri = (LinearLayout) itemView.findViewById(R.id.llyt_fri);
            tvFri = (TextView) itemView.findViewById(R.id.tv_fri);
            tvFriMorning = (TextView) itemView.findViewById(R.id.tv_fri_moring);
            tvFriAfternoon = (TextView) itemView.findViewById(R.id.tv_fri_afternoon);
            llytSat = (LinearLayout) itemView.findViewById(R.id.llyt_sat);
            tvSat = (TextView) itemView.findViewById(R.id.tv_sat);
            tvSatMorning = (TextView) itemView.findViewById(R.id.tv_sat_moring);
            tvSatAfternoon = (TextView) itemView.findViewById(R.id.tv_sat_afternoon);
            llytSun = (LinearLayout) itemView.findViewById(R.id.llyt_sun);
            tvSun = (TextView) itemView.findViewById(R.id.tv_sun);
            tvSunMorning = (TextView) itemView.findViewById(R.id.tv_sun_moring);
            tvSunAfternoon = (TextView) itemView.findViewById(R.id.tv_sun_afternoon);
            ivMoreTime = (ImageView) itemView.findViewById(R.id.iv_more_time);

            relativeLayout_phone = (RelativeLayout) itemView.findViewById(R.id.etTelephone_relativeLayout);
        }

        public void bindData(final MallDetailInfo info) {

            mallGroundAdapter = new MallGroundAdapter(mContext);
            rycvMallGround.setAdapter(mallGroundAdapter);
            tvMallBreif.setText(info.getIntroduction());
            tvMallBriefEll.setText(info.getIntroduction());
            tvMallBreifTry.setText(info.getIntroduction());
            tvMallAddress.setText(info.getAddress() + "," + info.getCountry());
            if (TextUtils.isEmpty(info.getTelephone())) {
                relativeLayout_phone.setVisibility(View.GONE);
            } else {
                relativeLayout_phone.setVisibility(View.VISIBLE);
                tvMallPhone.setText(info.getTelephone());
            }


            if (info.getCoordinate() != null) {
                info.getCoordinate().setDes(info.getName());
            }
            if (info.getOpen_time() != null && info.getOpen_time().size() > 0) {
                shopHoursList = info.getOpen_time();
                setTodayOpenTime(shopHoursList, getToday());
                llytMallTime.setVisibility(View.VISIBLE);
            } else {
                llytMallTime.setVisibility(View.GONE);
            }
            if (info.getPlanes() != null && info.getPlanes().size() > 0) {
                mallGroundAdapter.setMlist(info.getPlanes());
                mallGroundAdapter.notifyDataSetChanged();
                llytGround.setVisibility(View.VISIBLE);
            } else {
                llytGround.setVisibility(View.GONE);
            }

            ivMallAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getCoordinate() == null || !NetUtil.IsInNetwork(mContext)) {
                        Toast.makeText(mContext, Configration.OFF_LINE_TIPS, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intentMap = new Intent(mContext, MapWebViewActivity.class);
                    intentMap.putExtra("data", new Gson().toJson(info.getCoordinate()));
                    mContext.startActivity(intentMap);
                }
            });
            ivMoreTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAllOpenTime(shopHoursList, getToday());
                    ivMoreTime.setVisibility(View.INVISIBLE);
                }
            });
            ivMoreBrief.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvMallBriefEll.setVisibility(View.GONE);
                    tvMallBreif.setVisibility(View.VISIBLE);
                    ivMoreBrief.setVisibility(View.INVISIBLE);
                    isClick = true;
                }
            });
            tvMallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.dialPhoneNumber(mContext, info.getTelephone());
                }
            });

            ViewTreeObserver vto = tvMallBreifTry.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (isClick) {
                        return;
                    }
                    Layout layout = tvMallBreifTry.getLayout();
                    if (tvMallBreifTry.getLayout() == null || !(tvMallBreifTry.getLayout() instanceof StaticLayout)) {
//                return;
                    } else {
                        int line = layout.getLineCount();
                        if (line > 1) {
                            ivMoreBrief.setVisibility(View.VISIBLE);
                        } else {
                            ivMoreBrief.setVisibility(View.INVISIBLE);
                        }
                        tvMallBreifTry.setText("");
                    }
                }
            });
        }


        private int getToday() {
            Calendar c = Calendar.getInstance();
            int w = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 1) {
                w = 7;
            }
            return w;
        }

        public void setWeekofDay(LinearLayout llytTime, TextView tvMorning, TextView tvAfternoon, ShopHoursInfo info) {
            llytTime.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(tvMorning.getText())) {
                tvMorning.setText(info.getStart_time() + " - " + info.getEnd_time());
                tvMorning.setVisibility(View.VISIBLE);
            } else {
                tvAfternoon.setText(info.getStart_time() + " - " + info.getEnd_time());
                tvAfternoon.setVisibility(View.VISIBLE);
            }
        }

        public void setToday(LinearLayout llytTime, TextView tvToday, TextView tvMorning, TextView tvAfternoon, ShopHoursInfo info) {
            llytTime.setVisibility(View.VISIBLE);
            tvToday.setTextColor(Color.parseColor("#ed5137"));
            if (TextUtils.isEmpty(tvMorning.getText())) {
                tvMorning.setText(info.getStart_time() + " - " + info.getEnd_time());
                tvMorning.setVisibility(View.VISIBLE);
                tvMorning.setTextColor(Color.parseColor("#ed5137"));
            } else {
                tvAfternoon.setText(info.getStart_time() + " - " + info.getEnd_time());
                tvAfternoon.setVisibility(View.VISIBLE);
                tvAfternoon.setTextColor(Color.parseColor("#ed5137"));
            }
        }

        public void setTodayOpenTime(ArrayList<ShopHoursInfo> list, int today) {
            clearToday(today);
            for (int i = 0; i < list.size(); i++) {
                if (today == list.get(i).getDay_ofweek()) {
                    switch (list.get(i).getDay_ofweek()) {
                        case 1:
                            setToday(llytMon, tvMon, tvMonMorning, tvMonAfternoon, list.get(i));
                            break;
                        case 2:
                            setToday(llytTues, tvTues, tvTuesMorning, tvTuesAfternoon, list.get(i));
                            break;
                        case 3:
                            setToday(llytWed, tvWed, tvWedMorning, tvWedAfternoon, list.get(i));
                            break;
                        case 4:
                            setToday(llytThur, tvThur, tvThurMorning, tvThurAfternoon, list.get(i));
                            break;
                        case 5:
                            setToday(llytFri, tvFri, tvFriMorning, tvFriAfternoon, list.get(i));
                            break;
                        case 6:
                            setToday(llytSat, tvSat, tvSatMorning, tvSatAfternoon, list.get(i));
                            break;
                        case 7:
                            setToday(llytSun, tvSun, tvSunMorning, tvSunAfternoon, list.get(i));
                            break;
                    }
                }
            }
        }

        public void setAllOpenTime(ArrayList<ShopHoursInfo> list, int today) {
            for (int i = 0; i < list.size(); i++) {
                if (today != list.get(i).getDay_ofweek()) {

                    switch (list.get(i).getDay_ofweek()) {
                        case 1:
                            setWeekofDay(llytMon, tvMonMorning, tvMonAfternoon, list.get(i));
                            break;
                        case 2:
                            setWeekofDay(llytTues, tvTuesMorning, tvTuesAfternoon, list.get(i));
                            break;
                        case 3:
                            setWeekofDay(llytWed, tvWedMorning, tvWedAfternoon, list.get(i));
                            break;
                        case 4:
                            setWeekofDay(llytThur, tvThurMorning, tvThurAfternoon, list.get(i));
                            break;
                        case 5:
                            setWeekofDay(llytFri, tvFriMorning, tvFriAfternoon, list.get(i));
                            break;
                        case 6:
                            setWeekofDay(llytSat, tvSatMorning, tvSatAfternoon, list.get(i));
                            break;
                        case 7:
                            setWeekofDay(llytSun, tvSunMorning, tvSunAfternoon, list.get(i));
                            break;
                    }
                }
            }
        }

        private void clearToday(int today) {
            switch (today) {
                case 1:
                    tvMonMorning.setText("");
                    tvMonAfternoon.setText("");
                    break;
                case 2:
                    tvTuesMorning.setText("");
                    tvTuesAfternoon.setText("");
                    break;
                case 3:
                    tvWedMorning.setText("");
                    tvWedAfternoon.setText("");
                    break;
                case 4:
                    tvThurMorning.setText("");
                    tvThurAfternoon.setText("");
                    break;
                case 5:
                    tvFriMorning.setText("");
                    tvFriAfternoon.setText("");
                    break;
                case 6:
                    tvSatMorning.setText("");
                    tvSatAfternoon.setText("");
                    break;
                case 7:
                    tvSunMorning.setText("");
                    tvSunAfternoon.setText("");
                    break;
            }
        }
    }

    protected class BannerViewHolder extends RecyclerView.ViewHolder {

        public ConvenientBanner mGalleryTravle;

        public BannerViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travle_head_banner, parent, false));
            if (mBannerLists.size() == 0) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
                return;
            }
            mGalleryTravle = (ConvenientBanner) itemView.findViewById(R.id.travle_banner);
            mGalleryTravle.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mGalleryTravle.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = (int)(layoutParams.width / 3.2f);
            mGalleryTravle.setLayoutParams(layoutParams);
            mGalleryTravle.startTurning(4000);
            itemView.findViewById(R.id.view).setVisibility(View.VISIBLE);
            mGalleryTravle.setVisibility(View.VISIBLE);
        }

        public void bindData(ArrayList<BannerItem> mlist) {
            if (mBannerLists.size() == 0) {
                return;
            }
            mGalleryTravle.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, mlist);
        }
    }

    class NetworkImageHolderView implements Holder<BannerItem> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final BannerItem item) {
            int width = SimpleUtils.getScreenWidth(mContext);
            int height = (int) (width / 3.2f);
            BitmapTool.clipShowImageView(item.getImage(), imageView, R.drawable.user_dafault, width, height);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentBanner = new Intent(mContext, CouponDetailActivity.class);
                    intentBanner.putExtra("couponId", item.getCouponId());
                    intentBanner.putExtra("countryId", countryId);
                    intentBanner.putExtra("cityId", cityId);
                    mContext.startActivity(intentBanner);
                }
            });
        }
    }

    public class ShopNewByTxtViewHolder extends RecyclerView.ViewHolder {
        TextView tvHotMessage;

        public ShopNewByTxtViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.travel_txt_info, parent, false));
            tvHotMessage = (TextView) itemView.findViewById(R.id.travle_txt);
            tvHotMessage.setText("附近商户");
            if (!NetUtil.IsInNetwork(mContext) || mLists == null) {
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.height = 0;
                lp.width = 0;
                itemView.setLayoutParams(lp);
            }
        }

    }

    public class NearbyShopViewHolder extends RecyclerView.ViewHolder {
        TextView tvShopName;
        ImageView ivShopCover;
        TextView tvShopCategroy;
        RatingBar ratingBar;
        TextView tvShopDistance;

        public NearbyShopViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.shop_item, parent, false));
            tvShopName = (TextView) itemView.findViewById(R.id.shop_name);
            ivShopCover = (ImageView) itemView.findViewById(R.id.shop_cover);
            tvShopCategroy = (TextView) itemView.findViewById(R.id.shop_category);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_rating);
            tvShopDistance = (TextView) itemView.findViewById(R.id.shop_distance);
        }
    }
}