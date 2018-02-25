package cn.yiya.shiji.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NearbyShopAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.StatusCode;
import cn.yiya.shiji.entity.navigation.CouponDetailObject;
import cn.yiya.shiji.entity.navigation.MallDetailInfo;
import cn.yiya.shiji.entity.navigation.MapInfo;
import cn.yiya.shiji.entity.navigation.RecommendInfo;
import cn.yiya.shiji.entity.navigation.RecommendList;
import cn.yiya.shiji.entity.navigation.StoreCategoryInfo;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.entity.navigation.StoreShortInfo;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.KenBurnsSupportView;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.ShareDialog;
import cn.yiya.shiji.views.joinable.JoinableAdapter;
import cn.yiya.shiji.views.joinable.JoinableLayout;
import cn.yiya.shiji.views.joinable.RvJoiner;

/**
 * Created by Tom on 2016/4/13.
 */
public class TravelStoreActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private Handler mHander;
    public Dialog progressDialog;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;

    private AppBarLayout appBarLayout;                              // 头部可伸缩布局

    private CollapsingToolbarLayout collapsingToolbarLayout;        // 可伸缩标题栏布局
    private Toolbar toolbar;                                        // 标题栏
    private TextView tvTitle;                                       // 标题栏标题
    private ImageView ivShare;                                      // 标题栏右侧分享图标
    private KenBurnsSupportView ivBackground;

    private RatingBar ratingBar;                                    // 商场星级

    private LinearLayout llytStoreName;                              // 显示商场名字的布局
    private TextView tvStoreName;                                    // 店铺英文名

    private RelativeLayout rlytAskWay;                              // 问路卡

    private RecyclerView rycvNearbyShop;                            // 附近商户列表
    private LinearLayout llytShopMore;                              // 查看更多
    private ImageView ivMoreShop;
    private ProgressBar pbMoreShop;

    private MapInfo mapInfo;
    private String id;                                              // 商场id
    String name;
    String country;
    String address;

    private boolean bNet;
    private String countryId;
    private String cityId;

    private ArrayList<BannerItem> mBannerLists = new ArrayList<>();
    private MallDetailInfo mInfos;
    private ArrayList<RecommendInfo> mRecommendLists = new ArrayList<>();
    private ArrayList<StoreShortInfo> mStoreLists = new ArrayList<>();

    private boolean bLoadInfo;
    private boolean bLoadBanner;
    private boolean bLoadRecommend;
    private boolean bLoadStore;

    private RvJoiner rvJoiner = new RvJoiner();

    private NearbyShopAdapter mAdapter;
    private JoinableLayout moreShop;

    private RelativeLayout relativeLayout;
    private ImageView ivBack;

    private int totalHttpCount;

    private ImageView ivTitleBack;
    private RelativeLayout rlTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_mall);
        mHander = new Handler(getMainLooper());
        id = getIntent().getStringExtra("id");

        countryId = getIntent().getStringExtra("countryId");
        cityId = getIntent().getStringExtra("cityId");

        initView();
        initEvent();
        initData();

    }

    private void initView() {

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        tvTitle = (TextView) findViewById(R.id.collpase_title);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivBackground = (KenBurnsSupportView)findViewById(R.id.backdrop);
        ivBack = (ImageView) findViewById(R.id.toolbar_back);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.brand_detail_backimage);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力
                int defaultColor = getResources().getColor(R.color.blue);
                int defaultTitleColor = getResources().getColor(R.color.white);
                int bgColor = palette.getDarkVibrantColor(defaultColor);
                int titleColor = palette.getLightVibrantColor(defaultTitleColor);

                int color1 = vibrant.getBodyTextColor();//内容颜色
                int color2 = vibrant.getTitleTextColor();//标题颜色
                int color3 = vibrant.getRgb();//rgb颜色
                collapsingToolbarLayout.setContentScrimColor(color1);
//                collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
//                collapsingToolbar.setExpandedTitleColor(titleColor);
            }
        });

        ratingBar = (RatingBar) findViewById(R.id.rb_rating);
        llytStoreName = (LinearLayout) findViewById(R.id.llyt_mall_name);
        tvStoreName = (TextView) findViewById(R.id.tv_shop_name);

        rlytAskWay = (RelativeLayout) findViewById(R.id.rlyt_ask_way);

        rycvNearbyShop = (RecyclerView) findViewById(R.id.nearby_shop_list);
        rycvNearbyShop.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rycvNearbyShop.setLayoutManager(linearLayoutManager);

        relativeLayout = (RelativeLayout) findViewById(R.id.rl_without_net);
        relativeLayout.setOnClickListener(this);

        ivTitleBack = (ImageView) findViewById(R.id.title_back);
        ivTitleBack.setOnClickListener(this);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        rlytAskWay.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
    }

    private void initData() {
        totalHttpCount = 0;
        bNet = NetUtil.IsInNetwork(this);
        showPreDialog("正在加载");
        getStoreInfo(id);
        getCoupon(id);
        getRecommend(id);
        getNearbyShop(0, id);
    }

    private void getStoreInfo(String id) {
        bLoadInfo = false;

        if (bNet) {
            new RetrofitRequest<MallDetailInfo>(ApiRequest.getApiShiji().getStoreDetailInfo(id)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleDetailMsg(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getLocalStoreInfo(id, mHander, countryId, cityId, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleDetailMsg(msg);
                }
            });
        }
    }

    private void handleDetailMsg(HttpMessage msg) {
        totalHttpCount++;
        if (msg.isSuccess()) {
            bLoadInfo = true;
            MallDetailInfo info = (MallDetailInfo) msg.obj;
            mInfos = info;

            setTotalAdapter();
            if (info.getCn_name() != null && !info.getCn_name().equals("")) {
                tvTitle.setText(info.getCn_name());
            } else {
                tvTitle.setText(info.getName());
            }
            BitmapTool.showImageView(info.getCover(), ivBackground, R.mipmap.title_travel_defaule);
            if (!TextUtils.isEmpty(info.getCover())) {
                ivBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            ivBackground.setStart();
            if (!TextUtils.isEmpty(info.getRate())) {
                ratingBar.setRating(Float.parseFloat(info.getRate()));
                ratingBar.setVisibility(View.VISIBLE);
            } else {
                ratingBar.setRating(0f);
                ratingBar.setVisibility(View.VISIBLE);
            }
            name = info.getName();
            country = info.getCountry();
            address = info.getAddress();

            llytStoreName.setVisibility(View.INVISIBLE);
            tvStoreName.setText(info.getName());
            tvStoreName.setVisibility(View.VISIBLE);
            mapInfo = info.getCoordinate();

        } else {
            if (!NetUtil.IsInNetwork(TravelStoreActivity.this)) {
                bLoadInfo = true;
                mInfos = null;
            }
            if (msg.code == StatusCode.NoLocationData) {
                bLoadInfo = false;
            }
            setTotalAdapter();
        }
    }

    private void getCoupon(String id) {
        bLoadBanner = false;
        if (bNet) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("offset", String.valueOf(0));
            maps.put("limit", String.valueOf(10));
            maps.put("id", id);

            new RetrofitRequest<CouponDetailObject>(ApiRequest.getApiShiji().getCouponListOfStore(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleCouponMsg(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getLocalCouponList("store", id, cityId, mHander, countryId, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleCouponMsg(msg);
                }
            });
        }
    }

    private void handleCouponMsg(HttpMessage msg) {
        totalHttpCount++;
        if (msg.isSuccess()) {
            bLoadBanner = true;
            CouponDetailObject object = (CouponDetailObject) msg.obj;
            ArrayList<BannerItem> bannerList = new ArrayList<>();
            if (object != null && object.list != null) {
                for (int i = 0; i < object.list.size(); i++) {
                    BannerItem item = new BannerItem();
                    item.setCouponId(object.list.get(i).getId());
                    item.setImage(object.list.get(i).getCover());
                    bannerList.add(item);
                }
            }

            mBannerLists = bannerList;
            setTotalAdapter();
        } else {
            if (!NetUtil.IsInNetwork(TravelStoreActivity.this)) {
                bLoadBanner = true;
                if (mBannerLists != null) {
                    mBannerLists.clear();
                }

                setTotalAdapter();
            }
            if (msg.code == StatusCode.NoLocationData) {
                bLoadBanner = false;
            }
        }
    }

    private void getRecommend(String id) {
        bLoadRecommend = false;
        new RetrofitRequest<RecommendList>(ApiRequest.getApiShiji().getStoreRecommendInfo(id)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        totalHttpCount++;
                        if (msg.isSuccess()) {
                            bLoadRecommend = true;
                            RecommendList object = (RecommendList) msg.obj;
                            mRecommendLists = object.list;
                            setTotalAdapter();
                        } else {
                            if (!NetUtil.IsInNetwork(TravelStoreActivity.this)) {
                                bLoadRecommend = true;
                                if (mRecommendLists != null) {
                                    mRecommendLists.clear();
                                }
                                setTotalAdapter();
                            }
                        }
                    }
                }
        );
    }

    public void getNearbyShop(final int offset, String id) {
        bLoadStore = false;
        if(offset > 0){
            showLoad(llytShopMore, ivMoreShop, pbMoreShop);
        }
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(3));
        maps.put("id", id);

        new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfAround(maps)).handRequest(
                new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                totalHttpCount++;
                if (msg.isSuccess()) {
                    bLoadStore = true;
                    StoreObject object = (StoreObject) msg.obj;
                    if (offset > 0) {
                        mStoreLists.addAll(object.list);
                        mAdapter.notifyDataSetChanged();
                        hideLoad(llytShopMore, ivMoreShop, pbMoreShop);
                        setNearbyShopFoot(object.list.size());
                    } else {
                        mStoreLists = object.list;
                        setTotalAdapter();
                        setNearbyShopFoot(object.list.size());
                    }
                } else {
                    if (!NetUtil.IsInNetwork(TravelStoreActivity.this)) {
                        if (offset > 0) {
                            hideLoad(llytShopMore, ivMoreShop, pbMoreShop);
                            showTips(Configration.ON_DOWONLOAD_SOURCE);
                            return;
                        } else {
                            bLoadStore = true;
                            if (mStoreLists != null) {
                                mStoreLists.clear();
                            }
                            setTotalAdapter();
                        }
                    }
                }
            }
        });
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(tvTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(tvTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share:
                shareStore();
                break;
            case R.id.toolbar_back:
                onBackPressed();
                break;
            case R.id.rlyt_ask_way:
                Intent intent = new Intent(this, AskWayActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("country", country);
                intent.putExtra("address", address);
                startActivity(intent);
                break;
            case R.id.news_footer_more:
                if(Util.BeNotQuicklyClick()){
                    getNearbyShop(mStoreLists.size(), id);
                }
                break;
            case R.id.rl_without_net:
                initData();
                break;
            case R.id.title_back:
                finish();
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;
        ratingBar.setAlpha((1 - percentage));
        tvStoreName.setAlpha((1 - percentage));
        rlytAskWay.setAlpha(1 - percentage);

        handleToolbarTitleVisibility(percentage);

    }

    public void showPreDialog(String str){
        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(this, str, false);
        progressDialog.show();
    }

    public void hidePreDialog(){
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void setNearbyShopFoot(int size){
        if(size < 3) {
            if (moreShop != null) {
                rvJoiner.remove(moreShop);
            }
        }
    }
    private void showTips(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void shareStore() {
        if (!NetUtil.NetAvailable(this) || !bNet) {
            showTips(Configration.OFF_LINE_TIPS);
            return;
        }

        String url = mInfos.getShare_info().getUrl();
        String imageUrl;
        if(bNet)
        {
            imageUrl = mInfos.getShare_info().getWx().getImage();
        }else{
            imageUrl = GetLocalRequest.getLocalLogoPath(countryId, cityId, mInfos.getShare_info().getWx().getImage());
        }
        new ShareDialog(this, mInfos.getShare_info().getWx().getTitle(),
                imageUrl , url, mInfos.getShare_info().getDes()).build().show();
//        ShareTools.getInstance().showShare(this, mInfos.getShare_info().getWx().getTitle(),
//                imageUrl , url, mInfos.getShare_info().getDes());
    }

    private synchronized void setTotalAdapter() {
        if (rvJoiner != null) {
            rvJoiner = null;
            rvJoiner = new RvJoiner();
            rycvNearbyShop.setAdapter(null);
        }

        if (bLoadInfo && bLoadBanner && bLoadRecommend  && bLoadStore && mInfos != null && mInfos.getName() != null) {
            mAdapter = new NearbyShopAdapter(this, mInfos, mBannerLists, new ArrayList<StoreCategoryInfo>(),
                    mStoreLists, mRecommendLists, countryId, cityId, id, bNet, 3);
            rvJoiner.add(new JoinableAdapter(mAdapter, NearbyShopAdapter.BANNER_TYPE, NearbyShopAdapter.MALL_TYPE,
                    NearbyShopAdapter.SHOP_TYPE, NearbyShopAdapter.OTHER_TYPE, NearbyShopAdapter.RECOMMEND_TYPE,
                    NearbyShopAdapter.TXT_TYPE, NearbyShopAdapter.LIST_TYPE));
            if(mStoreLists.size() > 2){
                moreShop = new JoinableLayout(R.layout.bottom_more_data_layout, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        llytShopMore = (LinearLayout) view.findViewById(R.id.news_footer_more);
                        ivMoreShop = (ImageView) view.findViewById(R.id.footer_img);
                        pbMoreShop = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                        llytShopMore.setOnClickListener(TravelStoreActivity.this);
                    }
                });
                rvJoiner.add(moreShop);
            }

            rycvNearbyShop.setAdapter(rvJoiner.getAdapter());
            hidePreDialog();
        }
        beNullData();

    }
    private void beNullData() {
        if (!bLoadInfo && !bLoadBanner) {
            appBarLayout.setVisibility(View.GONE);
            rycvNearbyShop.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);
            hidePreDialog();
        } else if (bLoadInfo || bLoadBanner || bLoadRecommend  || bLoadStore) {
            appBarLayout.setVisibility(View.VISIBLE);
            rycvNearbyShop.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);
        }

        if(totalHttpCount < 4) {
            return;
        }

        boolean bBanner = mBannerLists == null || mBannerLists.size()==0 ? true : false;
        boolean bRecommend = mRecommendLists == null|| mRecommendLists.size() == 0  ? true : false;
        boolean bNearStore = mStoreLists == null || mStoreLists.size() == 0 ? true : false;
        boolean bStore;

        if(mInfos != null && !TextUtils.isEmpty(mInfos.getName()))
            bStore = false;
        else
            bStore = true;

        if(bBanner && bRecommend && bNearStore && bStore){
            appBarLayout.setVisibility(View.GONE);
            rycvNearbyShop.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);
            hidePreDialog();
        }

        hidePreDialog();
    }

    private void showLoad(View moreView, ImageView imageView, ProgressBar progressBar){
        moreView.setClickable(false);
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoad(View moreView, ImageView imageView, ProgressBar progressBar){
        moreView.setClickable(true);
        imageView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}

