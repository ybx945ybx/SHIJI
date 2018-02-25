package cn.yiya.shiji.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.CountryCouponList;
import cn.yiya.shiji.entity.MyCouponCountryList;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by jerry on 2016/3/29.
 */
public class CouponDetailActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TextView tvTitle, tvButton, tvRange, tvDeadline, tvScene, tvMothed, tvTitleBar, tvDes;
    private ImageView ivRight, ivBack;
    private Handler handler;
    private RelativeLayout relativeLayout;
    private RelativeLayout withoutNetRelativeLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CouponDetailInfo couponDetailInfo;
    private ImageView ivCoupon;
    private String couponId;
    private Intent intent;
    private final int COLLECTED = 1;

    private boolean isLongin;
    private boolean netState;           //  有网为true，没网为false

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    private SharedPreferences sharedPreferences;

    private ArrayList<CouponDetailInfo> couponList;                               // coupon的收藏列表
    private ArrayList<CouponDetailInfo> couponMoreInfoList;
    private boolean isNew = true;

    private boolean bNet;
    private String countryId;
    private String cityId;
    boolean source;
    private boolean isSaved = false;
    private static final int LOGIN_RESULT = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_detail_layout);
        isLongin = Util.isLogin();
        netState = NetUtil.NetAvailable(this);
        initIntent();
        initViews();
        init();
        initEvents();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null) {
            couponId = intent.getStringExtra("couponId");
            countryId = intent.getStringExtra("countryId");
            cityId = intent.getStringExtra("cityId");
        }
//        couponId = "570884666bcbc0f045471cdc";
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        break;
                    default:
                        break;
                }
            }
        };

        bNet = NetUtil.NetAvailable(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                shareCoupon();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.tv_button:
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    collectCoupon();
                }
                break;
            case R.id.rl_without_net:
                init();
                break;
            default:
                break;
        }

    }

    // 分享coupon
    private void shareCoupon() {
        if (!NetUtil.NetAvailable(this) || !bNet) {
            showTips(Configration.OFF_LINE_TIPS);
            return;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String date = sDateFormat.format(new java.util.Date());
        String url = couponDetailInfo.getShare_info().getUrl();
        new ShareDialog(this, couponDetailInfo.getShare_info().getWx().getTitle(), couponDetailInfo.getShare_info().getWx().getImage(), url
                , couponDetailInfo.getShare_info().getDes()).build().show();
//        ShareTools.getInstance().showShare(this, couponDetailInfo.getShare_info().getWx().getTitle(), couponDetailInfo.getShare_info().getWx().getImage(), url
//                , couponDetailInfo.getShare_info().getDes(), false);
    }

    // 获取单个coupon的详细信息
    public void getCouponDetailInfo(String couponId) {
        bNet = NetUtil.IsInNetwork(CouponDetailActivity.this);
        if (source) {
            if (bNet) {
                new RetrofitRequest<CouponDetailInfo>(ApiRequest.getApiShiji().getCouponDetail(couponId)).handRequest(
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleMessage(msg);
                            }
                        }
                );
            } else {
                GetLocalRequest.getInstance().getLocalCouponDetail(couponId, handler, countryId, cityId, new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleMessage(msg);
                    }
                });
            }
        } else {
            couponDetailInfo = BaseApplication.getCouponDetailInfo();

            fillData();
        }
    }

    private void handleMessage(HttpMessage msg) {
        if (msg.isSuccess()) {
            couponDetailInfo = (CouponDetailInfo) msg.obj;

            fillData();
            if(TextUtils.isEmpty(couponDetailInfo.getBrief()))
            {
                beNullData();
            }

        } else {
            beNullData();
            showTips(msg.message);
        }
        hidePreDialog();
    }

    private void beNullData()
    {
        swipeRefreshLayout.setVisibility(View.GONE);
//        relativeLayout.setVisibility(View.GONE);
        withoutNetRelativeLayout.setVisibility(View.VISIBLE);
        hidePreDialog();
        return;
    }

    // 填充数据
    private void fillData() {
       String couponId = couponDetailInfo.getId();
        if(TextUtils.isEmpty(couponId))
        {
            beNullData();
            return;
        }

        if (bNet) {
            Netroid.displayImage(Util.transferImage(couponDetailInfo.getCover(), SimpleUtils.getScreenWidth(CouponDetailActivity.this)), ivCoupon);

        } else {
            BitmapTool.showFullImageView(CouponDetailActivity.this, couponDetailInfo.getCover(), ivCoupon);
//            ivCoupon.setImageBitmap(BitmapFactory.decodeFile(couponDetailInfo.getCover()));
        }

        tvTitle.setText(couponDetailInfo.getDes());
        tvRange.setText(getResources().getString(R.string.coupon_range) + " " + couponDetailInfo.getRange());
        tvDeadline.setText(getResources().getString(R.string.coupon_deadline) + " " + couponDetailInfo.getStart_time() + " ~ " + couponDetailInfo.getEnd_time());
        tvScene.setText(couponDetailInfo.getScene());
        tvMothed.setText(couponDetailInfo.getUsage());
//        tvDes.setText(couponDetailInfo.getDes());
        if (couponDetailInfo.getCollected() == COLLECTED) {
            tvButton.setEnabled(false);
            tvButton.setText(R.string.coupon_tip);
        }
        if (!bNet && TextUtils.isEmpty(countryId) && TextUtils.isEmpty(cityId)) {
            tvButton.setEnabled(false);
            tvButton.setText(R.string.coupon_tips);
//            tvButton.setVisibility(View.GONE);
        }
        if (!bNet && !TextUtils.isEmpty(countryId) && !TextUtils.isEmpty(cityId)) {
            tvButton.setEnabled(false);
            tvButton.setText(R.string.coupon_tips);
        }
        hidePreDialog();
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    // 收藏coupon
    public void getCouponCollect(String couponId) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().storeCoupon(couponId)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    showTips("领取成功，可在\"我的优惠券\"查看");
                    tvButton.setEnabled(false);
                    tvButton.setText(R.string.coupon_tip);
                    saveCoupon(couponDetailInfo);
                } else {
                    showTips(msg.message);
                }
            }
        });
    }

    private CountryCouponList createNewCountryCouponList(CouponDetailInfo couponDetailInfo) {
        CountryCouponList countryCouponList = new CountryCouponList();
        countryCouponList.setCountry_name(couponDetailInfo.getCountry_name());
        ArrayList<CouponDetailInfo> couponDetailInfoList = new ArrayList<>();
        wrapCouponMoreInfo(couponDetailInfoList);
        countryCouponList.setCoupon_list(couponDetailInfoList);

        return countryCouponList;
    }

    //本地保存收藏的coupon信息
    private void saveCoupon(CouponDetailInfo couponDetailInfo) {
        if(isSaved){
            return;                 //防止网络差的情况下多次点击按钮本地进行了多次保存
        }
        sharedPreferences = getSharedPreferences(Configration.TRAVELCOUPON, MODE_PRIVATE);
        String couponObject = sharedPreferences.getString(BaseApplication.FileId + "couponObject", "");

        MyCouponCountryList couponDetailObject;
        if (TextUtils.isEmpty(couponObject)) {
            couponDetailObject = new MyCouponCountryList();
            ArrayList<CountryCouponList> couponList = new ArrayList<>();

            couponList.add(createNewCountryCouponList(couponDetailInfo));
            couponDetailObject.setList(couponList);
        } else {
            couponDetailObject = new Gson().fromJson(couponObject, MyCouponCountryList.class);

            ArrayList<CountryCouponList> couponList = couponDetailObject.getList();
            for (int i = 0; i < couponList.size(); i++) {
                if (couponList.get(i).getCountry_name().equals(couponDetailInfo.getCountry_name())) {
                    isNew = false;
                    CouponDetailInfo couponMoreInfo = new CouponDetailInfo();
                    couponMoreInfo.setId(couponDetailInfo.getId());
                    couponMoreInfo.setPeriod(couponDetailInfo.getPeriod());
                    couponMoreInfo.setStore_name(couponDetailInfo.getStore_name());
                    couponMoreInfo.setRange(couponDetailInfo.getRange());
                    couponMoreInfo.setStart_time(couponDetailInfo.getStart_time());
                    couponMoreInfo.setEnd_time(couponDetailInfo.getEnd_time());
                    couponMoreInfo.setDes(couponDetailInfo.getDes());
                    couponMoreInfo.setBrief(couponDetailInfo.getBrief());
                    couponDetailInfo.setCollected(1);
                    couponMoreInfo.setScene(couponDetailInfo.getScene());
                    couponMoreInfo.setUsage(couponDetailInfo.getUsage());
                    couponList.get(i).getCoupon_list().add(couponMoreInfo);
                }
            }

            if (isNew) {
                couponList.add(createNewCountryCouponList(couponDetailInfo));
            }
            couponDetailObject.setList(couponList);
        }

        couponObject = new Gson().toJson(couponDetailObject);
        sharedPreferences.edit().putString(BaseApplication.FileId + "couponObject", couponObject).commit();

        saveCouponCover();

        isSaved = true;
    }

    private void wrapCouponMoreInfo(ArrayList<CouponDetailInfo> couponMoreInfoList) {
        CouponDetailInfo couponMoreInfo = new CouponDetailInfo();
        couponMoreInfo.setId(couponDetailInfo.getId());
        couponMoreInfo.setPeriod(couponDetailInfo.getPeriod());
        couponMoreInfo.setStore_name(couponDetailInfo.getStore_name());
        couponMoreInfo.setRange(couponDetailInfo.getRange());
        couponMoreInfo.setStart_time(couponDetailInfo.getStart_time());
        couponMoreInfo.setEnd_time(couponDetailInfo.getEnd_time());
        couponMoreInfo.setDes(couponDetailInfo.getDes());
        couponMoreInfo.setBrief(couponDetailInfo.getBrief());
        couponDetailInfo.setCollected(1);
        couponMoreInfo.setScene(couponDetailInfo.getScene());
        couponMoreInfo.setUsage(couponDetailInfo.getUsage());
        couponMoreInfoList.add(couponMoreInfo);

    }

    private void saveCouponCover() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL downUrl;
                    String url = Util.transferImage(couponDetailInfo.getCover(), SimpleUtils.getScreenWidth(CouponDetailActivity.this));
                    downUrl = new URL(url);
                    InputStream inputStream = downUrl.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    saveFile(Configration.COUPON_PATH, bitmap, "/" + couponDetailInfo.getId() + ".png");
                } catch (Exception e) {
                    String a = e.getMessage();
                    Log.e("abc", a);
                }
            }
        }).start();
    }

    private void saveFile(String path, Bitmap bm, String fileName) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        File mCaptureFile = new File(path + fileName);
        if (!mCaptureFile.exists()) {
            mCaptureFile.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mCaptureFile));

        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();

    }

    // 判断登录态和网络状态并领取coupon，
    private void collectCoupon() {
        bNet = NetUtil.NetAvailable(this);
        if (isLongin && bNet) {
            getCouponCollect(couponId);
        } else if (bNet && !isLongin) {
            Intent intent = new Intent(CouponDetailActivity.this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_RESULT);
        } else if (!bNet) {
            showTips(Configration.OFF_LINE_TIPS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            init();
            Util.getNewUserPullLayer(this, data);
        }
    }

    // 判断走接口还是走全局变量
    private boolean bDataSource() {
        if (bNet || !TextUtils.isEmpty(countryId) && !TextUtils.isEmpty(cityId)) {
            source = true;
        } else {
            source = false;
        }
        return source;
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvButton = (TextView) findViewById(R.id.tv_button);
        if (!netState) {
            tvButton.setClickable(false);
        }
        tvRange = (TextView) findViewById(R.id.tv_range);
        tvDeadline = (TextView) findViewById(R.id.tv_deadline);
        tvScene = (TextView) findViewById(R.id.tv_scene);
        tvMothed = (TextView) findViewById(R.id.tv_mothed);
        tvDes = (TextView) findViewById(R.id.tv_des);
        ivCoupon = (ImageView) findViewById(R.id.iv_coupon);

        relativeLayout = (RelativeLayout) findViewById(R.id.ll_title);
        tvTitleBar = (TextView) findViewById(R.id.title_txt);
        ivRight = (ImageView) findViewById(R.id.title_right);
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitleBar.setText("优惠券详情");
        relativeLayout.setBackgroundColor(0xFFFFFFFF);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        withoutNetRelativeLayout = (RelativeLayout)findViewById(R.id.rl_without_net);

        bDataSource();
    }

    @Override
    protected void initEvents() {
        ivRight.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCouponDetailInfo(couponId);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        tvButton.setOnClickListener(this);
        withoutNetRelativeLayout.setOnClickListener(this);
    }

    @Override
    protected void init() {
        isLongin = Util.isLogin();
        showPreDialog("正在加载...");
        getCouponDetailInfo(couponId);                    // coupon 详情
    }
}
