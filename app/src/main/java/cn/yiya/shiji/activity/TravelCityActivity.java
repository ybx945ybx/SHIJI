package cn.yiya.shiji.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CityHotMsgAdapter;
import cn.yiya.shiji.adapter.HotMallAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.StatusCode;
import cn.yiya.shiji.entity.navigation.CityInfo;
import cn.yiya.shiji.entity.navigation.CouponDetailObject;
import cn.yiya.shiji.entity.navigation.MallDetailInfo;
import cn.yiya.shiji.entity.navigation.MallDetailObject;
import cn.yiya.shiji.entity.navigation.MapInfo;
import cn.yiya.shiji.entity.navigation.NewsInfo;
import cn.yiya.shiji.entity.navigation.NewsObject;
import cn.yiya.shiji.entity.navigation.StoreCategoryInfo;
import cn.yiya.shiji.entity.navigation.StoreCategoryObject;
import cn.yiya.shiji.receiver.LocationService;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.KenBurnsSupportView;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.joinable.JoinableAdapter;
import cn.yiya.shiji.views.joinable.JoinableLayout;
import cn.yiya.shiji.views.joinable.RvJoiner;

/**
 * Created by Tom on 2016/3/30.
 */
public class TravelCityActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private Handler mHandler;
    public Dialog progressDialog;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;

    private AppBarLayout appBarLayout;                              // 头部可伸缩布局

    private CollapsingToolbarLayout collapsingToolbarLayout;        // 可伸缩标题栏布局
    private Toolbar toolbar;                                        // 标题栏
    private TextView tvTitle;                                       // 标题栏标题
    private ImageView ivMap;                                        // 标题栏右侧地图定位图标
    private KenBurnsSupportView ivBackground;

    private LinearLayout llytCityName;                              // 显示城市名称的布局
    private TextView tvCityName;                                    // 城市英文名
    private TextView tvCityCnName;                                  // 城市中文名

    private LinearLayout llytCityBrief;                             // 城市简介的布局
    private TextView tvBreifTry;
    private TextView tvCityBriefOne;                                // 城市简介第一行
    private TextView tvCityBriefEll;                                // 城市简介带省略的第二行

    private RecyclerView rycvHotMall;                               // 城市下热门商场列表
    private HotMallAdapter hotMallAdapter;                          // 热门商场列表适配器
    private CityHotMsgAdapter msgAdapter;
    private LinearLayout llytMallMore;                              // 查看更多热门商场
    private ImageView ivMoreMall;
    private ProgressBar pbMoreMall;
    private LinearLayout llytMessageMore;                           // 查看更多资讯
    private ImageView ivMoreHotMessage;
    private ProgressBar pbMoreHotMessage;

    private String id;
    private MapInfo mapInfo;


    private boolean bNet;
    private String countryId;
    private String cityId;

    private boolean bLoadCoupon;
    private boolean bLoadMall;
    private boolean bLoadShop;
    private boolean bLoadNews;

    private RvJoiner rvJoiner = new RvJoiner();

    private ArrayList<BannerItem> mBannerLists;
    private ArrayList<MallDetailInfo> mMallLists;
    private ArrayList<StoreCategoryInfo> mShopTitles;
    private ArrayList<NewsInfo> mNewsLists;

    private JoinableLayout mallMoreJoin;
    private JoinableLayout newsMoreJoin;

    private RelativeLayout relativeLayout;
    private ImageView ivBack;
    private LocationService locationService;
    private String longitude;
    private String latitude;

    private TextView tvreload;
    private boolean bCityInfo;

    private int totalHttpCount = 0;

    private ImageView ivTitleBack;
    private RelativeLayout rlTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_city);
        mHandler = new Handler(Looper.getMainLooper());
        id = getIntent().getStringExtra("id");
        countryId = getIntent().getStringExtra("countryId");

        cityId = id;
        initView();
        initEvent();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((BaseApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    private void initView() {

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ivBack = (ImageView) findViewById(R.id.toolbar_back);

        tvTitle = (TextView) findViewById(R.id.collpase_title);
        ivMap = (ImageView) findViewById(R.id.iv_map);
        ivBackground = (KenBurnsSupportView) findViewById(R.id.backdrop);

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

        llytCityName = (LinearLayout) findViewById(R.id.llyt_city_name);
        tvCityCnName = (TextView) findViewById(R.id.city_cn_name);
        tvCityName = (TextView) findViewById(R.id.city_name);

        llytCityBrief = (LinearLayout) findViewById(R.id.llyt_city_brief);
        tvBreifTry = (TextView) findViewById(R.id.tv_try);
        tvCityBriefOne = (TextView) findViewById(R.id.city_brief_one);
        tvCityBriefEll = (TextView) findViewById(R.id.city_brief_ell);

        rycvHotMall = (RecyclerView) findViewById(R.id.mall_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rycvHotMall.setLayoutManager(linearLayoutManager);
        rycvHotMall.setItemAnimator(new DefaultItemAnimator());

        relativeLayout = (RelativeLayout) findViewById(R.id.rl_without_net);
        relativeLayout.setOnClickListener(this);
        ivTitleBack = (ImageView) findViewById(R.id.title_back);
        ivTitleBack.setOnClickListener(this);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        ivMap.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        llytCityBrief.setOnClickListener(this);
    }

    private void initData() {
        totalHttpCount = 0;
        bNet = NetUtil.IsInNetwork(this);
        showPreDialog("正在加载");
        getCityInfo(id);
        getCoupon(0, id);
        getMallList(0, id, countryId);
        getHotShop(id);
        getHotMessage(0, id);

    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());
                if (longitude.equals("0.0")) {
                    longitude = "miss";
                } else {
                    ((BaseApplication) getApplication()).setLongitude(longitude);
                }

                if (latitude.equals("0.0")) {
                    latitude = "miss";
                } else {
                    ((BaseApplication) getApplication()).setLatitude(latitude);

                }

                locationService.stop();
            }
        }

    };

    private void getCityInfo(String id) {
        if (bNet) {
            new RetrofitRequest<CityInfo>(ApiRequest.getApiShiji().getCityBriefInfo(String.valueOf(id))).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleMessage(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getCityBriefInfo("", id, countryId, mHandler, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMessage(msg);
                }
            });
        }
    }

    private void handleMessage(HttpMessage msg) {
        if (msg.isSuccess()) {
            CityInfo info = (CityInfo) msg.obj;
            if (info.getCn_name() != null) {
                tvTitle.setText(info.getCn_name());
            } else {
                tvTitle.setText(info.getName());
            }
            tvCityName.setText(info.getName());
            tvCityCnName.setText(info.getCn_name());
            BitmapTool.showImageView(info.getCover(), ivBackground, R.mipmap.title_travel_defaule);
            if (!TextUtils.isEmpty(info.getCover())) {
                ivBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            ivBackground.setStart();
            mapInfo = info.getCoordinate();
            if (mapInfo != null) {
                mapInfo.setDes(info.getCn_name());
            }
            tvBreifTry.setText(info.getBrief());
            setBrief(tvBreifTry, tvCityBriefOne, tvCityBriefEll, 1);
        } else {
            bCityInfo = false;
            Toast.makeText(TravelCityActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setBrief(TextView tvTry, TextView tvBriefOne, TextView tvBriefEll, int linecount) {
        Layout layout = tvTry.getLayout();
        String text = tvTry.getLayout().getText().toString();
        int line = layout.getLineCount();
        String breifOne = "";
        if (line > linecount) {
            for (int i = 0; i < linecount; i++) {
                int start = layout.getLineStart(i);
                int end = layout.getLineEnd(i);
                breifOne = breifOne + text.substring(start, end);
            }
            tvBriefOne.setText(breifOne);
            int start = layout.getLineStart(linecount);
            tvBriefEll.setText(text.substring(start));
        } else {
            tvBriefEll.setText(text);
        }
        tvTry.setText("");
    }

    private void getCoupon(int offset, String id) {
        bLoadCoupon = false;

        if (bNet) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("offset", String.valueOf(offset));
            maps.put("limit", String.valueOf(10));
            maps.put("id", id);
            new RetrofitRequest<CouponDetailObject>(ApiRequest.getApiShiji().getCouponListOfCity(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleCouponMsg(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getLocalCouponList("city", id, id, mHandler, countryId, new MsgCallBack() {
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
            CouponDetailObject object = (CouponDetailObject) msg.obj;
            bLoadCoupon = true;
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
            if (!NetUtil.IsInNetwork(TravelCityActivity.this)) {
                bLoadCoupon = true;
                if (mBannerLists != null)
                    mBannerLists.clear();
            }
            if (msg.code == StatusCode.NoLocationData) {
                bLoadCoupon = false;
            }
            setTotalAdapter();
        }
    }

    private void getMallList(final int offset, String id, String countryId) {
        bLoadMall = false;
        if(offset > 0){
            showLoad(llytMallMore, ivMoreMall, pbMoreMall);
        }

        if (NetUtil.IsInNetwork(this)) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("offset", String.valueOf(offset));
            maps.put("limit", String.valueOf(3));
            maps.put("id", id);
            new RetrofitRequest<MallDetailObject>(ApiRequest.getApiShiji().getMallListOfCity(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleMallMsg(offset, msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getLocalMallList(id, mHandler, countryId, offset, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleMallMsg(offset, msg);
                }
            });
        }
    }

    private void handleMallMsg(int offset, HttpMessage msg) {
        totalHttpCount++;
        if (msg.isSuccess()) {
            bLoadMall = true;
            MallDetailObject object = (MallDetailObject) msg.obj;
            if (offset > 0) {
                if (object != null) {
                    mMallLists.addAll(object.list);
                    hotMallAdapter.notifyDataSetChanged();
                    hideLoad(llytMallMore, ivMoreMall, pbMoreMall);
                    setMallFoot(object.list.size());
                }
            } else {
                mMallLists = object.list;
                setTotalAdapter();
                if (object.list != null)
                    setMallFoot(object.list.size());
                else
                    bLoadMall = false;
                setMallFoot(0);
            }
        } else {
            if (!NetUtil.IsInNetwork(TravelCityActivity.this)) {
                bLoadMall = true;
                if (offset > 0) {
                    hideLoad(llytMallMore, ivMoreMall, pbMoreMall);
                    showTips(Configration.ON_DOWONLOAD_SOURCE);
                    return;
                } else {
                    if (mMallLists != null)
                        mMallLists.clear();
                    setTotalAdapter();
                }
            }
            if (msg.code == StatusCode.NoLocationData) {
                bLoadMall = false;
            }
        }
    }


    private void getHotShop(String id) {
        bLoadShop = false;

        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", id);

        new RetrofitRequest<StoreCategoryObject>(ApiRequest.getApiShiji().getStoreBrandsCategoryOfMall(maps))
                .handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                totalHttpCount++;
                if (msg.isSuccess()) {
                    StoreCategoryObject info = (StoreCategoryObject) msg.obj;
                    bLoadShop = true;
                    if (info != null && info.category_list.size() > 0) {
                        StoreCategoryInfo storeCategoryInfo = new StoreCategoryInfo();
                        storeCategoryInfo.setId("");
                        storeCategoryInfo.setName("全部");
                        mShopTitles = info.category_list;
                        mShopTitles.add(0, storeCategoryInfo);
                    }
                    setTotalAdapter();
                } else {
                    if (!NetUtil.IsInNetwork(TravelCityActivity.this)) {
                        bLoadShop = true;
                        if (mShopTitles != null)
                            mShopTitles.clear();
                        mShopTitles = new ArrayList<StoreCategoryInfo>();

                        StoreCategoryInfo storeCategoryInfo = new StoreCategoryInfo();
                        storeCategoryInfo.setId("");
                        storeCategoryInfo.setName("全部");
                        mShopTitles.add(storeCategoryInfo);
                        setTotalAdapter();
                    }
                }

            }
        });

    }

    private void getHotMessage(final int offset, String id) {
        bLoadNews = false;
        if(offset > 0){
            showLoad(llytMessageMore, ivMoreHotMessage, pbMoreHotMessage);
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(6));
        maps.put("id", id);
        new RetrofitRequest<NewsObject>(ApiRequest.getApiShiji().getNewsListOfCity(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        totalHttpCount++;
                        if (msg.isSuccess()) {
                            NewsObject object = (NewsObject) msg.obj;
                            bLoadNews = true;
                            if (offset > 0) {
                                mNewsLists.addAll(object.list);
                                msgAdapter.notifyDataSetChanged();
                                hideLoad(llytMessageMore, ivMoreHotMessage, pbMoreHotMessage);
                                setMessageFoot(object.list.size());
                            } else {
                                mNewsLists = object.list;
                                setTotalAdapter();
                                setMessageFoot(object.list.size());
                            }
                        } else {
                            if (!NetUtil.IsInNetwork(TravelCityActivity.this)) {
                                if (offset > 0) {
                                    hideLoad(llytMessageMore, ivMoreHotMessage, pbMoreHotMessage);
                                    showTips(Configration.ON_DOWONLOAD_SOURCE);
                                    return;
                                } else {
                                    bLoadNews = true;
                                    if (mNewsLists != null) {
                                        mNewsLists.clear();
                                    }
                                    setTotalAdapter();
                                }
                            }
                        }
                    }
                }
        );
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
        switch (v.getId()) {
            case R.id.toolbar_back:
                onBackPressed();
                break;
            case R.id.iv_map:
                if (!NetUtil.IsInNetwork(this)) {
                    showTips(Configration.OFF_LINE_TIPS);
                    return;
                }
                Intent intent = new Intent(this, MapWebViewActivity.class);
                intent.putExtra("data", new Gson().toJson(mapInfo));
                startActivity(intent);
                break;
            case R.id.llyt_city_brief:
                Intent intentBrief = new Intent(this, TravelBasicInfoActivity.class);
                intentBrief.putExtra("id", id);
                intentBrief.putExtra("type", 2);
                intentBrief.putExtra("countryId", countryId);
                startActivity(intentBrief);
                break;
            case R.id.county_footer_more:
                bNet = NetUtil.IsInNetwork(this);
                if(Util.BeNotQuicklyClick()){
                    getMallList(mMallLists.size(), id, countryId);
                }
                break;
            case R.id.more_shop_layout:
                Intent intentMoreShop = new Intent(TravelCityActivity.this, ShopSortActivity.class);
                intentMoreShop.putExtra("id", id);
                intentMoreShop.putExtra("type", 1);
                intentMoreShop.putExtra("cityId", cityId);
                intentMoreShop.putExtra("countryId", countryId);
                startActivity(intentMoreShop);
                break;
            case R.id.news_footer_more:
                getHotMessage(mNewsLists.size(), id);
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
        llytCityBrief.setAlpha((1 - percentage));
        llytCityName.setAlpha((1 - percentage));

        handleToolbarTitleVisibility(percentage);
    }

    private void setMallFoot(int size) {
        if (size < 3) {
            if (mallMoreJoin != null) {
                rvJoiner.remove(mallMoreJoin);
            }
        }
    }

    private void setMessageFoot(int size) {
        if (size < 6) {
            if (newsMoreJoin != null) {
                rvJoiner.remove(newsMoreJoin);
            }
        }
    }

    public void showPreDialog(String str) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.creatRequestDialog(this, str, false);
        progressDialog.show();
    }

    public void hidePreDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showTips(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private synchronized void setTotalAdapter() {
        if (rvJoiner != null) {
            rvJoiner = null;
            rvJoiner = new RvJoiner();
            rycvHotMall.setAdapter(null);
        }

        if (bLoadCoupon && bLoadMall && bLoadShop && bLoadNews) {
            hotMallAdapter = new HotMallAdapter(this, mMallLists, mBannerLists, countryId, cityId);
            rvJoiner.add(new JoinableAdapter(hotMallAdapter, HotMallAdapter.BANNER_TYPE, HotMallAdapter.TXT_TYPE
                    , HotMallAdapter.LIST_TYPE));
            if (mMallLists != null && mMallLists.size() > 2) {
                mallMoreJoin = new JoinableLayout(R.layout.mall_more_data_layout, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        llytMallMore = (LinearLayout) view.findViewById(R.id.county_footer_more);
                        ivMoreMall = (ImageView) view.findViewById(R.id.footer_img);
                        pbMoreMall = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                        llytMallMore.setBackgroundColor(Color.parseColor("#ffffff"));
                        llytMallMore.setOnClickListener(TravelCityActivity.this);
                    }
                });
                rvJoiner.add(mallMoreJoin);
            }

            msgAdapter = new CityHotMsgAdapter(this, id, cityId, countryId, bNet, mNewsLists, mShopTitles);
            rvJoiner.add(new JoinableAdapter(msgAdapter, CityHotMsgAdapter.SHOP_TYPE, CityHotMsgAdapter.TXT_TYPE
                    , CityHotMsgAdapter.LIST_TYPE));

            if (mNewsLists != null && mNewsLists.size() > 5) {
                newsMoreJoin = new JoinableLayout(R.layout.bottom_more_data_layout, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        llytMessageMore = (LinearLayout) view.findViewById(R.id.news_footer_more);
                        ivMoreHotMessage = (ImageView) view.findViewById(R.id.footer_img);
                        pbMoreHotMessage = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                        llytMessageMore.setOnClickListener(TravelCityActivity.this);
                    }
                });
                rvJoiner.add(newsMoreJoin);
            }

            rycvHotMall.setAdapter(rvJoiner.getAdapter());
            hidePreDialog();
        }
        beNullData();
    }

    private void beNullData() {
        if (!bLoadCoupon && !bLoadMall && !bCityInfo) {
            appBarLayout.setVisibility(View.GONE);
            rycvHotMall.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);
            hidePreDialog();
            return;
        } else if (bLoadCoupon || bLoadMall || bLoadNews || bLoadShop) {
            appBarLayout.setVisibility(View.VISIBLE);
            rycvHotMall.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);
        }

        boolean bBanner = mBannerLists == null || mBannerLists.size() == 0 ? true : false;
        boolean bMall = mMallLists == null || mMallLists.size() == 0 ? true : false;
        boolean bShop;
        if (bNet) {
            bShop = mShopTitles == null || mShopTitles.size() == 0 ? true : false;
        } else {
            bShop = mShopTitles == null || mShopTitles.size() <= 1 ? true : false;
        }
        boolean bNew = mNewsLists == null || mNewsLists.size() == 0 ? true : false;

        if(totalHttpCount < 4)
        {
            return;
        }

        if (bBanner && bMall && bShop && bNew && TextUtils.isEmpty(tvCityName.getText())) {
            appBarLayout.setVisibility(View.GONE);
            rycvHotMall.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);
            hidePreDialog();
        }
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
