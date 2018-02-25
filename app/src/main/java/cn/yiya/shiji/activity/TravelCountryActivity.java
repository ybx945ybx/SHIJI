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
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CountryHotMessageAdapter;
import cn.yiya.shiji.adapter.HotCityAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.navigation.CityInfo;
import cn.yiya.shiji.entity.navigation.CityListObject;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.MapInfo;
import cn.yiya.shiji.entity.navigation.NewsInfo;
import cn.yiya.shiji.entity.navigation.NewsObject;
import cn.yiya.shiji.entity.navigation.RecommendInfo;
import cn.yiya.shiji.entity.navigation.RecommendList;
import cn.yiya.shiji.entity.navigation.TaxAndInfos;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.KenBurnsSupportView;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.joinable.JoinableAdapter;
import cn.yiya.shiji.views.joinable.JoinableLayout;
import cn.yiya.shiji.views.joinable.ParallaxRecyclerView;
import cn.yiya.shiji.views.joinable.ParallaxViewHolder;
import cn.yiya.shiji.views.joinable.RvJoiner;

/**
 * Created by Tom on 2016/3/25.
 */
public class TravelCountryActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener{
    private Handler mHandler;
    public Dialog progressDialog;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;

    private AppBarLayout appBarLayout;                              // 头部可伸缩布局
    private ImageView ivBack;

    private CollapsingToolbarLayout collapsingToolbarLayout;        // 可伸缩标题栏布局
    private Toolbar toolbar;                                        // 标题栏
    private TextView tvTitle;                                       // 标题栏标题
    private ImageView ivMap;                                        // 标题栏右侧地图定位图标
    private KenBurnsSupportView ivBackground;                       // 背景图片

    private LinearLayout llytCountryName;                           // 显示国家名称的布局
    private TextView tvCountryName;                                 // 国家英文名
    private TextView tvCountryCnName;                               // 国家中文名

    private LinearLayout llytCountryBrief;                          // 国家简介的布局
    private TextView tvBreifTry;
    private TextView tvCountryBriefOne;                             // 国家简介第一行
    private TextView tvCountryBriefEll;                             // 国家简介第二行

    private ParallaxRecyclerView rycvHotCity;                       // 国家下热门城市列表
    private HotCityAdapter hotCityAdapter;                          // 热门城市列表适配器

    private TextView tvHotCity;                                     // 热门城市标题

    private CountryHotMessageAdapter hotMessageAdapter;             // 热门资讯适配器

    private LinearLayout llytMoreCity;                              // 查看更多热门城市
    private ImageView ivMoreCity;
    private ProgressBar pbMoreCity;

    private LinearLayout llytMoreHotMessage;                        // 查看更多热门资讯
    private ImageView ivMoreHotMessage;
    private ProgressBar pbMoreHotMessageo;
    private LinearLayout llytTax;                                   // 退税指引板块
    private LinearLayout llytTaxGuide;                              // 退税指引的布局
    private TextView tvTaxTry;
    private TextView tvTaxGuideOne;                                 // 退税指引
    private TextView tvTaxGuideEll;                                 // 退税指引省略

    private String id;                                              // 国家id
    private MapInfo mapInfo;                                        // 国家坐标信息

    private boolean bNet;

    private RvJoiner rvJoiner = new RvJoiner();
    private JoinableLayout cityTextJoin;
    private JoinableLayout cityMoreJoin;
    private JoinableLayout newsMoreJoin;
    private JoinableLayout taxJoin;

    ArrayList<CityInfo> mCityList = new ArrayList<>();
    ArrayList<NewsInfo> mNewsList = new ArrayList<>();
    ArrayList<RecommendInfo> mRecommendBuyList = new ArrayList<>();
    TaxAndInfos taxAndInfos = new TaxAndInfos();
    boolean bLoadCountryTax;
    boolean bLoadCity;
    boolean bLoadRecommend;
    boolean bLoadNews;
    String name;
    private String[] strArrCountry = new String[2];
    private String[] strArrTax = new String[2];


    private RelativeLayout relativeLayout;
    private TextView tvreload;
    private ImageView ivTitleBack;
    private RelativeLayout rlTitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_country);
        mHandler = new Handler(getMainLooper());
        id = getIntent().getStringExtra("id");          // 国家id

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
        ivMap = (ImageView) findViewById(R.id.iv_map);
        ivBack = (ImageView) findViewById(R.id.toolbar_back);
        ivBackground = (KenBurnsSupportView) findViewById(R.id.backdrop);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.brand_detail_backimage);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力
                int color1 = vibrant.getBodyTextColor();//内容颜色
                collapsingToolbarLayout.setContentScrimColor(color1);
            }
        });

        llytCountryName = (LinearLayout) findViewById(R.id.llyt_country_name);
        tvCountryName = (TextView) findViewById(R.id.country_name);
        tvCountryCnName = (TextView) findViewById(R.id.country_cn_name);

        llytCountryBrief = (LinearLayout) findViewById(R.id.llyt_country_brief);
        tvBreifTry = (TextView) findViewById(R.id.tv_try);
        tvCountryBriefOne = (TextView) findViewById(R.id.country_brief_one);
        tvCountryBriefEll = (TextView) findViewById(R.id.country_brief_ell);


        rycvHotCity = (ParallaxRecyclerView) findViewById(R.id.parallax_list);
        rycvHotCity.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rycvHotCity.setLayoutManager(layoutManager);

        relativeLayout = (RelativeLayout)findViewById(R.id.rl_without_net);
        relativeLayout.setOnClickListener(this);
        ivTitleBack = (ImageView) findViewById(R.id.title_back);
        ivTitleBack.setOnClickListener(this);

        rlTitle = (RelativeLayout)findViewById(R.id.rl_title);
    }

    private void initEvent() {
        ivMap.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        llytCountryBrief.setOnClickListener(this);
    }

    private void initData() {
        bNet = NetUtil.IsInNetwork(this);
        showPreDialog("正在加载");
        getCountryInfo(id);
        getCountryTaxInfo(id);
        getCityList(0, id);
        getRecommend(id);
        getHotMessage(0, id);
    }

    private void getCountryInfo(String id) {
        if (NetUtil.IsInNetwork(TravelCountryActivity.this)) {
            new RetrofitRequest<CountryListInfo>(ApiRequest.getApiShiji().getCountryBriefInfo(id)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleBriefMsg(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getLocalCountry(id, mHandler, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleBriefMsg(msg);
                }
            });
        }
    }

    private void handleBriefMsg(HttpMessage msg) {
        if (msg.isSuccess()) {
            CountryListInfo info = (CountryListInfo) msg.obj;
            if (info != null) {
                if (info.getCn_name() != null) {
                    tvTitle.setText(info.getCn_name());
                } else {
                    tvTitle.setText(info.getName());
                }
                tvCountryCnName.setText(info.getCn_name());
                tvCountryName.setText(info.getName());
                mapInfo = info.getCoordinate();
                if (mapInfo != null) {
                    mapInfo.setDes(info.getCn_name());
                }
                BitmapTool.showImageView(info.getCover(), ivBackground, R.mipmap.title_travel_defaule);
                if (!TextUtils.isEmpty(info.getCover())) {
                    ivBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                ivBackground.setStart();
                tvBreifTry.setText(info.getBrief());
                setBrief(tvBreifTry, 1);
                if (!TextUtils.isEmpty(strArrCountry[0])) {
                    tvCountryBriefOne.setText(strArrCountry[0]);
                    tvCountryBriefEll.setText(strArrCountry[1]);
                } else {
                    tvCountryBriefEll.setText(strArrCountry[1]);
                }

            }
        } else {
            Toast.makeText(TravelCountryActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCountryTaxInfo(String id) {
        bLoadCountryTax = false;
        if (NetUtil.IsInNetwork(TravelCountryActivity.this)) {
            new RetrofitRequest<TaxAndInfos>(ApiRequest.getApiShiji().getTaxRebateOfCountry(id)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleTaxMsg(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getTaxInfo(id, mHandler, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleTaxMsg(msg);
                }
            });
        }
    }

    private void handleTaxMsg(HttpMessage msg) {
        if (msg.isSuccess()) {
            bLoadCountryTax = true;
            TaxAndInfos info = (TaxAndInfos) msg.obj;
            tvBreifTry.setText(info.getBrief());
            setBrief(tvBreifTry, 2);
            taxAndInfos = info;
            setToTalAdapter();
        } else {
            if (!NetUtil.IsInNetwork(TravelCountryActivity.this)) {
                bLoadCountryTax = true;
                taxAndInfos = null;
            }
            if (msg.code == 5001) {
                bLoadCountryTax = false;
            }
            setToTalAdapter();
        }
    }

    private void getCityList(final int offset,String id) {
        if (offset > 0) {
            showLoad(llytMoreCity, ivMoreCity, pbMoreCity);
        }
        bLoadCity = false;

        if (NetUtil.IsInNetwork(TravelCountryActivity.this)) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("offset", String.valueOf(offset));
            maps.put("limit", String.valueOf(10));
            maps.put("id", id);
            new RetrofitRequest<CityListObject>(ApiRequest.getApiShiji().getCityList(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleCityMsg(offset, msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getCityList(offset, id, mHandler, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleCityMsg(offset, msg);
                }
            });
        }
    }

    private void handleCityMsg(int offset, HttpMessage msg) {
        if (msg.isSuccess()) {
            bLoadCity = true;
            CityListObject object = (CityListObject) msg.obj;
            if (offset > 0) {
                if (object != null) {
                    mCityList.addAll(object.list);
                    hotCityAdapter.notifyDataSetChanged();
                    hideLoad(llytMoreCity, ivMoreCity, pbMoreCity);
                    setCityFoot(object.list.size());
                }
            } else {
                mCityList = object.list;
                setToTalAdapter();
            }

        } else {
            bLoadCity = false;
        }
    }

    private synchronized void setToTalAdapter() {
        if(rvJoiner != null){
            rvJoiner = null;
            rvJoiner = new RvJoiner();
            rycvHotCity.setAdapter(null);
        }

        if(bLoadCity && bLoadRecommend && bLoadNews && bLoadCountryTax) {
            hotCityAdapter = new HotCityAdapter(this, mCityList, id);
            cityTextJoin = new JoinableLayout(R.layout.travel_txt_info, new JoinableLayout.Callback() {
                @Override
                public void onInflateComplete(View view, ViewGroup parent) {
                    tvHotCity = (TextView) view.findViewById(R.id.travle_txt);
                    tvHotCity.setText("热门城市");
                }
            });
            if (mCityList != null && mCityList.size() > 0) {

                rvJoiner.add(cityTextJoin);
            }
            rvJoiner.add(new JoinableAdapter(hotCityAdapter));
            if (mCityList != null && mCityList.size() > 9) {
                cityMoreJoin = new JoinableLayout(R.layout.country_more_data_layout, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        llytMoreCity = (LinearLayout) view.findViewById(R.id.county_footer_more);
                        ivMoreCity = (ImageView) view.findViewById(R.id.footer_img);
                        pbMoreCity = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                        llytMoreCity.setOnClickListener(TravelCountryActivity.this);
                    }
                });
                rvJoiner.add(cityMoreJoin);
            }

            hotMessageAdapter = new CountryHotMessageAdapter(this, mNewsList, mRecommendBuyList);
            rvJoiner.add(new JoinableAdapter(hotMessageAdapter,
                    CountryHotMessageAdapter.RECOMMEND_TYPE, CountryHotMessageAdapter.TXT_TYPE,
                    CountryHotMessageAdapter.LIST_TYPE));
            if (mNewsList != null && mNewsList.size() > 5) {
                newsMoreJoin = new JoinableLayout(R.layout.news_more_data_layout, new JoinableLayout.Callback() {
                    @Override
                    public void onInflateComplete(View view, ViewGroup parent) {
                        llytMoreHotMessage = (LinearLayout) view.findViewById(R.id.news_footer_more);
                        ivMoreHotMessage = (ImageView) view.findViewById(R.id.footer_img);
                        pbMoreHotMessageo = (ProgressBar) view.findViewById(R.id.footer_progressbar);
                        llytMoreHotMessage.setOnClickListener(TravelCountryActivity.this);
                    }
                });
                rvJoiner.add(newsMoreJoin);
            }
            if (taxAndInfos != null) {
                if (!TextUtils.isEmpty(taxAndInfos.getBrief())) {
                    taxJoin = new JoinableLayout(R.layout.country_footer_hotmessage_footer, new JoinableLayout.Callback() {
                        @Override
                        public void onInflateComplete(View view, ViewGroup parent) {
                            llytTax = (LinearLayout) view.findViewById(R.id.llyt_tax_guide);
                            llytTaxGuide = (LinearLayout) llytTax.findViewById(R.id.llyt_tax);
                            tvTaxTry = (TextView) llytTax.findViewById(R.id.tax_try);
                            tvTaxTry.setText(strArrTax[0] + strArrTax[1]);
                            tvTaxGuideOne = (TextView) llytTax.findViewById(R.id.tax_one);
                            tvTaxGuideEll = (TextView) llytTax.findViewById(R.id.tax_ell);

                            if (!TextUtils.isEmpty(strArrTax[0])) {
                                tvTaxGuideOne.setText(strArrTax[0]);
                                tvTaxGuideEll.setText(strArrTax[1]);
                            } else {
                                tvTaxGuideEll.setText(strArrTax[1]);
                            }

                            llytTaxGuide.setOnClickListener(TravelCountryActivity.this);
                        }
                    });
                    rvJoiner.add(taxJoin);
                }
            }

            rycvHotCity.setAdapter(rvJoiner.getAdapter());

            rycvHotCity.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    for (int i = 0; i < rycvHotCity.getChildCount(); i++) {
                        if (rycvHotCity.getChildViewHolder(rycvHotCity.getChildAt(i)) instanceof ParallaxViewHolder) {
                            ((ParallaxViewHolder) rycvHotCity.getChildViewHolder(rycvHotCity.getChildAt(i))).animateImage();
                        }
                    }
                }
            });
            hidePreDialog();

        }
        beNullData();
    }

    private void getRecommend(String id) {
        bLoadRecommend = false;

        if (NetUtil.IsInNetwork(TravelCountryActivity.this)) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("limit", String.valueOf(10));
            maps.put("id", id);

            new RetrofitRequest<RecommendList>(ApiRequest.getApiShiji().getCountryRecommend(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleRecMsg(msg);
                        }
                    }
            );
        } else {
            GetLocalRequest.getInstance().getRecommendInfo(id, mHandler, new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    handleRecMsg(msg);
                }
            });
        }
    }

    private void handleRecMsg(HttpMessage msg){
        if (msg.isSuccess()) {
            bLoadRecommend = true;
            RecommendList object = (RecommendList) msg.obj;
            mRecommendBuyList = object.list;
            setToTalAdapter();
        } else {
            if (!NetUtil.IsInNetwork(TravelCountryActivity.this)) {
                bLoadRecommend = true;
                mRecommendBuyList.clear();
                setToTalAdapter();
            }
            if (msg.code == 5001) {
                bLoadRecommend = false;

            }
        }
    }


    private void getHotMessage(final int offset, String id) {
        if(offset > 0){
            showLoad(llytMoreHotMessage, ivMoreHotMessage, pbMoreHotMessageo);
        }
        bLoadNews = false;

        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(6));
        maps.put("id", id);

        new RetrofitRequest<NewsObject>(ApiRequest.getApiShiji().getNewsOfCountry(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            bLoadNews = true;
                            NewsObject object = (NewsObject) msg.obj;
                            if (offset > 0) {
                                mNewsList.addAll(object.list);
                                hotMessageAdapter.notifyDataSetChanged();
                                hideLoad(llytMoreHotMessage, ivMoreHotMessage, pbMoreHotMessageo);
                                setMessageFoot(object.list.size());
                            } else {
                                mNewsList = object.list;
                                setToTalAdapter();
                            }

                        } else {
                            if (!NetUtil.IsInNetwork(TravelCountryActivity.this)) {
                                if (offset > 0) {
                                    hideLoad(llytMoreHotMessage, ivMoreHotMessage, pbMoreHotMessageo);
                                    showTips(Configration.ON_DOWONLOAD_SOURCE);
                                    return;
                                } else {
                                    bLoadNews = true;
                                    mNewsList.clear();
                                    setToTalAdapter();
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
        switch (v.getId()){
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
            case R.id.llyt_country_brief:
                Intent intentBrief = new Intent(this, TravelBasicInfoActivity.class );
                intentBrief.putExtra("id", id);
                intentBrief.putExtra("type", 1);
                startActivity(intentBrief);
                break;
            case R.id.county_footer_more:
                getCityList(mCityList.size(), id);
                break;
            case R.id.news_footer_more:
                getHotMessage(mNewsList.size(), id);
                break;
            case R.id.llyt_tax:
                Intent intentTax = new Intent(this, TravelBasicInfoActivity.class );
                intentTax.putExtra("id", id);
                intentTax.putExtra("type", 1);
                intentTax.putExtra("from", 1);
                startActivity(intentTax);
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
        appBarLayout.findFocus();
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;
        llytCountryBrief.setAlpha((1 - percentage));
        llytCountryName.setAlpha((1 - percentage));

        handleToolbarTitleVisibility(percentage);
    }

    private void setCityFoot(int size){
        if(size < 10){
            if (cityMoreJoin != null)
                rvJoiner.remove(cityMoreJoin);
        }
    }

    private void setMessageFoot(int size){
        if(size < 6){
            if (newsMoreJoin != null)
                rvJoiner.remove(newsMoreJoin);
        }
    }

    private void setBrief(TextView tvTry, int nType) {

        Layout layout = tvTry.getLayout();
        if(tvTry.getLayout() == null || !(tvTry.getLayout()instanceof StaticLayout)){
            return;
        }

        String[] temp = new String[2];
        String text = tvTry.getLayout().getText().toString();
        int line = layout.getLineCount();
        String breifOne = "";
        if(line > nType){
            for(int i = 0; i < nType; i++){
                int start = layout.getLineStart(i);
                int end = layout.getLineEnd(i);
                breifOne = breifOne + text.substring(start, end);
            }
            temp[0] = breifOne;
            int start = layout.getLineStart(nType);
            temp[1] = text.substring(start);
        }else {
            temp[0] = "";
            temp[1] = text;
        }
        tvTry.setText("");
        if (nType == 1) {
            strArrCountry = temp;
        } else {
            strArrTax = temp;
        }
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

    private void showTips(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private  void beNullData(){
        if(!bLoadCity && !bLoadRecommend  && !bLoadCountryTax){
            appBarLayout.setVisibility(View.GONE);
            rycvHotCity.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            rlTitle.setVisibility(View.VISIBLE);
            hidePreDialog();
            return;
        } else if (bLoadCity || bLoadRecommend || bLoadNews || bLoadCountryTax){
            appBarLayout.setVisibility(View.VISIBLE);
            rycvHotCity.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            rlTitle.setVisibility(View.GONE);

        }

        boolean bRec = mRecommendBuyList == null || mRecommendBuyList.size() == 0 ? true : false;
        boolean bCity = mCityList == null || mCityList.size() == 0 ? true : false;

        // jerry 因为taxAndInfos返回的是对象，不可能为空,所以判定数据
        boolean bCountry;
        if (taxAndInfos == null) {
            bCountry = true;
        } else {
            bCountry = TextUtils.isEmpty(taxAndInfos.getBrief()) && TextUtils.isEmpty(taxAndInfos.getContent()) && TextUtils.isEmpty(taxAndInfos.getCountry_id())
                    && TextUtils.isEmpty(taxAndInfos.getDes()) && TextUtils.isEmpty(taxAndInfos.getTitle())  ? true : false;
        }
//        boolean bCountry = taxAndInfos == null ? true : false;
        boolean bNew = mNewsList == null || mNewsList.size() == 0 ? true : false;
        if (bRec && bCity && bCountry && bNew) {
            appBarLayout.setVisibility(View.GONE);
            rycvHotCity.setVisibility(View.GONE);
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
