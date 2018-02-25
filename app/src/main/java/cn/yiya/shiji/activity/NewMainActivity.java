package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.AppScreenImageEntity;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.CommonWebViewEntity;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.entity.VersionData;
import cn.yiya.shiji.fragment.EssayFragment;
import cn.yiya.shiji.fragment.NewClassifyFragment;
import cn.yiya.shiji.fragment.NewDiscoverFragment;
import cn.yiya.shiji.fragment.NewMainFragment;
import cn.yiya.shiji.fragment.NewMainHomeFragment;
import cn.yiya.shiji.fragment.PersonalCenterFragment;
import cn.yiya.shiji.fragment.TravleNavigationFragment;
import cn.yiya.shiji.utils.MyPreference;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.ThemeManager;

/**
 * Created by chenjian on 2016/5/26.
 */
public class NewMainActivity extends BaseAppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener
        , NewMainHomeFragment.CustomActionListener {

    public static final String MALL = "MALL";
    public static final String CLASSIFY = "CLASSIFY";
    public static final String NAVIGATION = "NAVIGATION";
    public static final String FIND = "FIND";
    public static final String MINE = "MINE";

    private FragmentManager fm;
    private String FragmentTag = MALL;
    private Fragment mFragment;
    private Handler mHandler;


    private String mainActivityDest = "";

    public static boolean isActive = false;
    Toolbar toolbar;

    private ImageView ivMiddle;        // 中间图标
    private TextView tvMiddle;          // 中间文字
    private LinearLayout llytFind;      // 发现布局
    private RelativeLayout rlytSearch;  // 分类搜索
    public TextView tvCarcount;         // 购物车数量
    private ImageView ivLeft;           // 左边图标
    private RelativeLayout rlytCar;     // 右边购物车布局

    private LinearLayout llytTips;      // 引导提示
    private LinearLayout llytTipsWork;

    private int nLeftType;
    private static final int SEARCH = 111;
    private static final int CAMERA = 222;
    private static final int SETTING = 333;

    private final static int CAR_REQUEST_CODE = 1000;
    private static final int REQUEST_SETTINGS = 1001;
    public static boolean bLogin;

    public TextView tvFastTop;
    public ImageView ivMessage;         // 消息小红点
    private MsgReceiver msgReceiver;

    private boolean setSkin;
    private ImageView ivShare;
    private String bottomName;
    private String bottomUrl;
    private String bottomTitle;
    private RadioButton rbMidle;
    private ImageView ivBottomMidle;
    private boolean openShop;
    public boolean needShare;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        //动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.yiya.shiji.pushReciver");
        registerReceiver(msgReceiver, intentFilter);

        ThemeManager.init(this, 2, 0, null);

        initIntent();

        initViews();
        initFragment(savedInstanceState);
        initEvents();
        init();
    }


    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivLeft = (ImageView) findViewById(R.id.toolbar_left);

        ivMiddle = (ImageView) findViewById(R.id.toolbar_middle_img);
        tvMiddle = (TextView) findViewById(R.id.toolbar_middle_txt);
        rlytSearch = (RelativeLayout) findViewById(R.id.toolbar_middle_search_layout);
        llytFind = (LinearLayout) findViewById(R.id.toolbar_middle_layout);

        tvCarcount = (TextView) findViewById(R.id.toolbar_right_count);
        rlytCar = (RelativeLayout) findViewById(R.id.toolbar_right_layout);

        ivShare = (ImageView) findViewById(R.id.toolbar_right_share);
        rlytCar.setVisibility(View.VISIBLE);

        llytTips = (LinearLayout) findViewById(R.id.llyt_tips_travel);
        llytTipsWork = (LinearLayout) findViewById(R.id.llyt_tips_work);

        tvFastTop = (TextView) findViewById(R.id.tv_fast_top_guide_tips);
        ivMessage = (ImageView) findViewById(R.id.iv_message_dot);

        rbMidle = ((RadioButton) findViewById(R.id.rd_main_navigation));
        ivBottomMidle = (ImageView) findViewById(R.id.iv_bottom_midle);
//        rbMidle.setText(bottomName);
    }

    @Override
    protected void initEvents() {
        ((RadioGroup) findViewById(R.id.rdgroup_main)).setOnCheckedChangeListener(this);
        rlytCar.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivBottomMidle.setOnClickListener(this);
    }

    @Override
    protected void init() {
        mHandler = new Handler(getMainLooper());
//        checkNewSkin();
        initUpdateVersion();
        if(TextUtils.isEmpty(bottomTitle) || TextUtils.isEmpty(bottomUrl)){
            getCommonWebView();
        }else {

            checkNewSkin();
        }
    }

    private void initCarCount() {
        tvCarcount.setVisibility(View.GONE);
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    bLogin = true;
                    tvCarcount.setVisibility(View.VISIBLE);
                    CarCountInfo info = (CarCountInfo) msg.obj;
                    tvCarcount.setText("" + info.getCount());
                    if (info.getCount() == 0) {
                        tvCarcount.setVisibility(View.GONE);
                    }

                } else if (msg.isLossLogin()) {
                    tvCarcount.setVisibility(View.GONE);
                    bLogin = false;
                }
            }
        });
    }

    // 设置导航的toolbar
    private void setNavigationToolbar() {
        ivLeft.setVisibility(View.GONE);
        ivMiddle.setVisibility(View.GONE);
        rlytSearch.setVisibility(View.GONE);
        llytFind.setVisibility(View.GONE);
        rlytCar.setVisibility(View.GONE);

        if(needShare){
            ivShare.setVisibility(View.VISIBLE);
        }else {
            ivShare.setVisibility(View.GONE);
        }
        tvMiddle.setVisibility(View.VISIBLE);
        tvMiddle.setText(bottomTitle);
    }

    // 设置商城的toolbar
    private void setMallToolbar() {
        rlytSearch.setVisibility(View.GONE);
        llytFind.setVisibility(View.GONE);
        tvMiddle.setVisibility(View.GONE);

        rlytCar.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.mipmap.icon_new_search);
        ivMiddle.setVisibility(View.VISIBLE);
        nLeftType = SEARCH;
        ivShare.setVisibility(View.GONE);
    }

    // 设置分类的toolbar
    private void setClassifyToolbar() {
        ivLeft.setVisibility(View.GONE);
        ivMiddle.setVisibility(View.GONE);
        llytFind.setVisibility(View.GONE);
        tvMiddle.setVisibility(View.GONE);
        ivShare.setVisibility(View.GONE);

        rlytCar.setVisibility(View.VISIBLE);
        rlytSearch.setVisibility(View.VISIBLE);
    }

    // 设置发现的toolbar
    private void setFindToolbar() {
        tvMiddle.setVisibility(View.GONE);
        ivMiddle.setVisibility(View.GONE);
        rlytSearch.setVisibility(View.GONE);
        ivShare.setVisibility(View.GONE);

        rlytCar.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.mipmap.ic_camera);
        llytFind.setVisibility(View.VISIBLE);
        nLeftType = CAMERA;

        addGuide(llytTipsWork, MyPreference.MAIN_WORK_GUIDE);
    }

    // 设置个人中心的toolbar
    private void setPersonToolbar() {
        ivMiddle.setVisibility(View.GONE);
        rlytSearch.setVisibility(View.GONE);
        llytFind.setVisibility(View.GONE);
        ivShare.setVisibility(View.GONE);

        rlytCar.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setImageResource(R.mipmap.shezhi);
        tvMiddle.setVisibility(View.VISIBLE);
        tvMiddle.setText("我");
        nLeftType = SETTING;
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            bottomName = intent.getStringExtra("bottomName");
            bottomUrl = intent.getStringExtra("bottomUrl");
            bottomTitle = intent.getStringExtra("bottomTitle");
            mainActivityDest = intent.getStringExtra("main_activity_dest");
            if (!TextUtils.isEmpty(mainActivityDest)) {
                Log.e("main_activity_dest", mainActivityDest);

                if (mainActivityDest.equals("flash")) {
                    String flashId = intent.getStringExtra("flashId");
                    Intent intent0 = new Intent(this, HomeIssueActivity.class);
                    intent0.putExtra("type", Configration.FLASH_SALE);
                    intent0.putExtra("flashId", flashId);
                    startActivity(intent0);
                } else if (mainActivityDest.equals("service")) {
                    Intent intent1 = new Intent(this, MessageCenterActivity.class);
                    intent1.putExtra("type", intent.getStringExtra("type"));
                    startActivity(intent1);
                } else if (mainActivityDest.equals("community")) {
                    Intent intent13 = new Intent(this, MessageCenterActivity.class);
                    startActivity(intent13);
                } else if (mainActivityDest.equals("homeIssue")) {
                    Intent intent2 = new Intent(this, HomeIssueActivity.class);
                    intent2.putExtra("activityId", intent.getStringExtra("activityId"));
                    startActivity(intent2);
                } else if (mainActivityDest.equals("newLocalWebViewThem")) {
                    Intent intent9 = new Intent(this, NewLocalWebActivity.class);
                    intent9.putExtra("main_activity_dest", "newLocalWebViewThem");
                    intent9.putExtra("data", getIntent().getStringExtra("data"));
                    intent9.putExtra("type", 2);
                    intent9.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
                    startActivity(intent9);
                } else if (mainActivityDest.equals("newLocalWebView")) {
                    Intent intent3 = new Intent(this, NewLocalWebActivity.class);
                    intent3.putExtra("activityId", intent.getStringExtra("activityId"));
                    intent3.putExtra("title", intent.getStringExtra("name"));
                    intent3.putExtra("url", intent.getStringExtra("url"));
                    intent3.putExtra("hshare", intent.getStringExtra("hshare"));
                    intent3.putExtra("type", 203);
                    startActivity(intent3);

                } else if (mainActivityDest.equals("newWorkDetail")) {
                    Intent intent4 = new Intent(this, NewWorkDetailsActivity.class);
                    intent4.putExtra("work_id", intent.getStringExtra("workId"));
                    startActivity(intent4);

                } else if (mainActivityDest.equals("newMatchDetail")) {
                    Intent intent5 = new Intent(this, NewMatchDetailActivity.class);
                    intent5.putExtra("matchId", intent.getStringExtra("matchId"));
                    startActivity(intent5);

                } else if (mainActivityDest.equals("userDetail")) {
                    Intent intent6 = new Intent(this, CommunityHomePageActivity.class);
                    intent6.putExtra("user_id", intent.getStringExtra("userId"));
                    startActivity(intent6);
                } else if (mainActivityDest.equals("orderDetail")) {
                    Intent intent7 = new Intent(this, OrderDetailActivity.class);
                    intent7.putExtra("orderId", intent.getStringExtra("subOrderNum"));
                    intent7.putExtra("orderno", intent.getStringExtra("orderNum"));
                    startActivity(intent7);
                } else if (mainActivityDest.equals("shoppingCart")) {
                    Intent intent8 = new Intent(this, NewShoppingCartActivity.class);
                    startActivity(intent8);
                } else if (mainActivityDest.equals("AD")) {
                    int adType = intent.getIntExtra("adType", -1);
                    String adContent = intent.getStringExtra("adContent");
                    String adShare = intent.getStringExtra("adShare");
                    switch (adType) {
                        case 1:
                            Intent intent10 = new Intent(NewMainActivity.this, NewLocalWebActivity.class);
                            intent10.putExtra("url", adContent);
                            intent10.putExtra("type", 204);
                            if (!TextUtils.isEmpty(adShare)) {
                                ShareEntity shareEntity = new ShareEntity();
                                AppScreenImageEntity.Share share = new Gson().fromJson(adShare, AppScreenImageEntity.Share.class);
                                shareEntity.setDescription(share.getDesc());
                                shareEntity.setTitle(share.getTitle());
                                shareEntity.setCover(share.getCover());
                                shareEntity.setUrl(adContent);
                                intent10.putExtra("data", new Gson().toJson(shareEntity));
                                intent10.putExtra("title", share.getTitle());
                            }
                            startActivity(intent10);
                            break;
                        case 2:
                            Intent intent11 = new Intent(NewMainActivity.this, CommunityHomePageActivity.class);
                            intent11.putExtra("user_id", Integer.parseInt(adContent));
                            startActivity(intent11);
                            break;
                        case 3:
                            Intent intent12 = new Intent(NewMainActivity.this, HomeIssueActivity.class);
                            intent12.putExtra("activityId", adContent);
                            intent12.putExtra("menuId", 7);
                            startActivity(intent12);
                            break;
                    }
                }else if(mainActivityDest.equals("openShop")){
//                    final Handler handler = new Handler(){
//                        @Override
//                        public void handleMessage(Message msg) {
//                            super.handleMessage(msg);
//                            switch (msg.what) {
//                                case 0:
//                                    showSuccessOpenShop();
//                                    break;
//                            }
//                        }
//                    };
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        public void run() {
//                            handler.sendEmptyMessage(0);
//                        }
//                    }, 1500);
                    getCommonWebView();
                    ((RadioButton) findViewById(R.id.rd_main_mine)).setChecked(true);
                    openShop = true;
                    intent.putExtra("main_activity_dest", "");
                }
                mainActivityDest = "";
            }
        }
    }

    private void getCommonWebView() {
        new RetrofitRequest<CommonWebViewEntity>(ApiRequest.getApiShiji().getCommonWebView()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    CommonWebViewEntity obj = (CommonWebViewEntity) msg.obj;
                    if(obj.getList() != null && obj.getList().size() > 0){
                        bottomName = obj.getList().get(0).getName();
                        bottomUrl = obj.getList().get(0).getUrl();
                        bottomTitle = obj.getList().get(0).getTitle();
                        BaseApplication.getInstance().saveThemeIcon(obj.getCover());

                        checkNewSkin();
                    }
                }
            }
        });
    }

    /**
     * 解决fragment重叠问题
     *
     * @param bundle
     */
    private void initFragment(Bundle bundle) {
        fm = getSupportFragmentManager();
        if (bundle != null) {
            String tag = bundle.getString("TAG");
            FragmentTransaction ft = fm.beginTransaction();
            Fragment fragment;
            String[] strArr = new String[]{MALL, CLASSIFY, NAVIGATION, FIND, MINE};
            for (String str : strArr) {
                fragment = fm.findFragmentByTag(str);
                if (fragment != null) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
            if (TextUtils.isEmpty(tag)) {
                setMallToolbar();
                turnPage(MALL);
            } else {
                turnPage(tag);
            }
        } else {
            if(openShop){
                setPersonToolbar();
                turnPage(MINE);
            }else {
                setMallToolbar();
                turnPage(MALL);
            }
        }
    }

    //fragment 跳转
    public void turnPage(String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        FragmentTag = tag;
        //隐藏当前显示的fragment
        if (mFragment != null) {
            ft.hide(mFragment);
        }
        //获取要显示的fragment
        mFragment = fm.findFragmentByTag(tag);
        if (mFragment == null) {
            mFragment = creatFragmentByTag(tag);
            if (mFragment != null) {
                ft.add(R.id.container_main, mFragment, tag);
            }
        }
        //显示
        ft.show(mFragment).commit();
    }

    // 需要显示的Fragment
    public Fragment creatFragmentByTag(String tag) {
        if (tag == null) {
            return null;
        }

        Fragment fragment;
        switch (tag) {
            case MALL:          // 商城
                fragment = new NewMainFragment();
                break;
            case CLASSIFY:      // 分类
                fragment = new NewClassifyFragment();
                break;
            case FIND:          // 发现
                fragment = new NewDiscoverFragment();
                break;
            case NAVIGATION:    // 导航
//                fragment = new TravleNavigationFragment();
                fragment = new EssayFragment().getInstance(bottomUrl);
                break;
            case MINE:          // 我
                fragment = new PersonalCenterFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isCurUser", true);
                fragment.setArguments(bundle);
                break;
            default:
                fragment = new NewMainFragment();
                break;
        }
        return fragment;
    }

    /**
     * 检查服务器是否有新的皮肤更新
     */
    private void checkNewSkin() {
        rbMidle.setText(bottomName);
        SharedPreferences sharedPreferences = getSharedPreferences("skin", 0);
        String endTime = sharedPreferences.getString("endtime", "");
        String nowTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

        try {
            Date endDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(endTime);               // 活动结束时间
            Date nowDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(nowTime);
            if (nowDate.compareTo(endDate) <= 0) {
                setBitmap(R.id.rd_main_home, "7", "2");
                setBitmap(R.id.rd_main_classify, "8", "3");
                if(!TextUtils.isEmpty(bottomName)) {
                    setBitmap(R.id.rd_main_navigation, "9", "4");
                    rbMidle.setText(bottomName);
                }else {
                    setMidleBitMap();
                }
                setBitmap(R.id.rd_main_find, "10", "5");
                setBitmap(R.id.rd_main_mine, "11", "6");
            }

        } catch (Exception e) {
        }
    }


    /**
     * 将新的皮肤图片设置进去
     *
     * @param id
     */
    private void setBitmap(int id, String newPath, String oldPath) {
        File skinFile = new File(SplashActivity.SKIN_PATH);
        if (!skinFile.exists()) {
            return;
        }
        Drawable betrue = new BitmapDrawable(getResources(), BitmapFactory.decodeFile((SplashActivity.SKIN_PATH + newPath + ".png")));
        Drawable befalse = new BitmapDrawable(getResources(), BitmapFactory.decodeFile((SplashActivity.SKIN_PATH + oldPath + ".png")));

        if (betrue != null && befalse != null) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_checked}, betrue);
            stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, befalse);
            RadioButton rb = (RadioButton) findViewById(id);
//            rb.setTextAppearance(this, R.style.main_radio_group);
            rb.setCompoundDrawablesWithIntrinsicBounds(null, stateListDrawable, null, null);
        }
    }

    private void setMidleBitMap() {
        File skinFile = new File(SplashActivity.SKIN_PATH);
        if (!skinFile.exists()) {
            return;
        }
        Drawable betrue = new BitmapDrawable(getResources(), BitmapFactory.decodeFile((SplashActivity.SKIN_PATH + 13 + ".png")));
        Drawable befalse = new BitmapDrawable(getResources(), BitmapFactory.decodeFile((SplashActivity.SKIN_PATH + 14 + ".png")));

        if (betrue != null && befalse != null) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, betrue);
            stateListDrawable.addState(new int[]{-android.R.attr.state_selected}, befalse);
            ivBottomMidle.setImageDrawable(stateListDrawable);

            ivBottomMidle.setVisibility(View.VISIBLE);
            rbMidle.setVisibility(View.INVISIBLE);
//            RadioButton rb = (RadioButton) findViewById(id);
//            rb.setTextAppearance(this, R.style.main_radio_group);
//            rb.setCompoundDrawablesWithIntrinsicBounds(null, stateListDrawable, null, null);
        }
    }

    // 判断当前是否有更新
    private void initUpdateVersion() {
        if (!BaseApplication.bUpdate) {
            final int localVerson = BaseApplication.getInstance().getCurVersion();
            new RetrofitRequest<VersionData>(ApiRequest.getApiShiji().getVersion(String.valueOf(localVerson)))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess() && msg.obj != null) {
                                VersionData remoteVer = (VersionData) msg.obj;
                                int remoteVersion = Integer.parseInt(remoteVer.getAndroid().getCode());
                                if (remoteVersion > localVerson) {
                                    ProgressDialog.showUpdateVersionDialog(NewMainActivity.this, remoteVer, 1);
                                } else {
                                    BaseApplication.bUpdate = false;
                                }
                            }
                        }
                    });
        }

    }

    @Override
    protected void onResume() {
        isActive = true;
        super.onResume();

        // 跳到首页
        Intent intent = getIntent();
        if (intent != null) {
            String fragId = intent.getStringExtra("fragid");
            if (!TextUtils.isEmpty(fragId) && fragId.equals(MALL)) {
                ((RadioButton) findViewById(R.id.rd_main_home)).setChecked(true);
                intent.putExtra("fragid", "");
            }
        }

        initCarCount();

        // 添加目的地按钮引导
//        addGuide(llytTips, MyPreference.MAIN_TRAVEL_GUIDE);

//        if (!TextUtils.isEmpty(intent.getStringExtra("order"))) {
//            if (intent.getStringExtra("order").equals("order")) {
//                RadioButton rb = (RadioButton) findViewById(R.id.rd_main_home);
//                rb.setChecked(true);
//                intent.putExtra("order", "");
//            } else if(intent.getStringExtra("order").equals("shoppingCar")){
//                RadioButton rb = (RadioButton) findViewById(R.id.rd_main_shop);
//                rb.setChecked(true);
//                intent.putExtra("order", "");
//            }
//        }
//        isActive = true;
//        super.onResume();
//
//        if (BaseApplication.bRefuse) {
//            finish();
//            System.exit(0);
//            Process.killProcess(Process.myPid());
//        }
        // 判断消息小红点是否显示
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    if (!TextUtils.isEmpty(SharedPreUtil.getString(NewMainActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, "")) ||
                            !TextUtils.isEmpty(SharedPreUtil.getString(NewMainActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, "")) ||
                            !TextUtils.isEmpty(SharedPreUtil.getString(NewMainActivity.this, SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_COMMUNITY, ""))) {
                        ivMessage.setVisibility(View.VISIBLE);
                    } else {
                        ivMessage.setVisibility(View.GONE);
                    }
                } else if (msg.isLossLogin()) {       // 判断是否有登录态  tomyang  2016-5-27
                    ivMessage.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    protected void onPause() {
        isActive = false;
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存当前显示的fragment名
        outState.putString("TAG", FragmentTag);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rd_main_find:      // 发现
                CustomEvent.onEvent(NewMainActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(),
                        "MainActivity", CustomEvent.SwitchDiscover);
                setFindToolbar();
                turnPage(FIND);
                ivBottomMidle.setSelected(false);
                break;
            case R.id.rd_main_classify:  // 分类
                CustomEvent.onEvent(NewMainActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(),
                        "MainActivity", CustomEvent.SwitchCategory);
                setClassifyToolbar();
                turnPage(CLASSIFY);
                ivBottomMidle.setSelected(false);
                break;
            case R.id.rd_main_home:     // 商城
                CustomEvent.onEvent(NewMainActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(),
                        "MainActivity", CustomEvent.SwitchHomePage);
                setMallToolbar();
                turnPage(MALL);
                ivBottomMidle.setSelected(false);
                break;
            case R.id.rd_main_mine:     // 我
                CustomEvent.onEvent(NewMainActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(),
                        "MainActivity", CustomEvent.SwitchMine);
                setPersonToolbar();
                turnPage(MINE);
                ivBottomMidle.setSelected(false);
                break;
            case R.id.rd_main_navigation:         // 导航
                CustomEvent.onEvent(NewMainActivity.this, ((BaseApplication) getApplication()).getDefaultTracker(),
                        "MainActivity", CustomEvent.SwitchDiscover);
                setNavigationToolbar();
                turnPage(NAVIGATION);
                ivBottomMidle.setSelected(true);
                break;
        }
    }

    /**
     * 连续按返回键退出标志
     */
    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mFragment instanceof NewDiscoverFragment) {
                boolean bHide = ((NewDiscoverFragment) mFragment).hidePopupWindow();
                if (bHide) {
                    return true;
                }
            }
            if(mFragment instanceof EssayFragment){
                boolean bBack = ((EssayFragment)mFragment).goBackKey();
                if(bBack){
                    return true;
                }
            }
            RadioButton rd = (RadioButton) findViewById(R.id.rd_main_home);
            if (!rd.isChecked()) {
                rd.setChecked(true);
                return true;
            } else {
                if (isExit) {
                    finish();
                    BaseApplication.getInstance().fastTopGuide = false;
                    BaseApplication.isInit = false;
                    return super.onKeyDown(keyCode, event);
                } else {
                    isExit = true;
                    showTips("再按一次将退出应用！");
                    //3秒后重置
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isExit = false;
                        }
                    }, 3000);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 当前从登陆界面回来，并且停留在个人中心界面，刷新
            if (data != null) {
                if (data.getBooleanExtra("success", false)) {
                    if (mFragment instanceof PersonalCenterFragment) {
                        ((PersonalCenterFragment) mFragment).setLogIn();
                    }
                }
            }
            switch (requestCode) {
                case CAR_REQUEST_CODE:
                    break;
                case REQUEST_SETTINGS:
                    if (mFragment instanceof PersonalCenterFragment) {
                        ((PersonalCenterFragment) mFragment).setLogOut();
                    }
                    break;
                default:
                    break;
            }
            Util.getNewUserPullLayer(this, data);
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    public void showTips(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 添加快速回到顶部的提示
    public void addBackTopGuide() {
        if (!BaseApplication.getInstance().fastTopGuide) {
            BaseApplication.getInstance().fastTopGuide = true;
//            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            TranslateAnimation alphaAnimation = new TranslateAnimation(-1200, 0, 0, 0);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(500);
            tvFastTop.setVisibility(View.VISIBLE);
            tvFastTop.setAnimation(alphaAnimation);
            alphaAnimation.startNow();
            final Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 10:
                            if (tvFastTop.getVisibility() == View.VISIBLE) {
                                TranslateAnimation alphaAnimation = new TranslateAnimation(0, -1200, 0, 0);
                                alphaAnimation.setFillAfter(true);
                                alphaAnimation.setDuration(1000);
                                tvFastTop.setVisibility(View.INVISIBLE);
                                tvFastTop.setAnimation(alphaAnimation);
                                alphaAnimation.startNow();
                            }
                            break;
                    }
                }
            };

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    mHandler.sendEmptyMessage(10);
                }
            }, 4000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bottom_midle:
                rbMidle.setChecked(true);
                break;
            case R.id.toolbar_right_layout:
                if (isEffectClick()) {
                    goToCarView();
                }
                break;
            case R.id.toolbar_right_share:
                FragmentManager fragmentManager = getSupportFragmentManager();
                EssayFragment essayFragment = (EssayFragment)fragmentManager.findFragmentByTag(NAVIGATION);
                essayFragment.goToShare();
                break;
            case R.id.toolbar_left:
                switch (nLeftType) {
                    case SEARCH:
                        Intent intent = new Intent(this, NewSearchActivity.class);
                        startActivity(intent);
                        break;
                    case CAMERA:
                        if (mFragment instanceof NewDiscoverFragment) {
                            //到NewDiscoverFragment的方法中执行
                            if (bLogin) {
                                ((NewDiscoverFragment) mFragment).ClickCamera();
                            } else {
                                Intent intentLogin = new Intent(NewMainActivity.this, LoginActivity.class);
                                startActivityForResult(intentLogin, REQUEST_SETTINGS);
                                overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                            }
                        }
                        break;
                    case SETTING:
                        if (isEffectClick()) {
                            goToSetting();
                        }
                        break;
                }
                break;
        }
    }

    private void goToSetting() {
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    Intent intent0 = new Intent(NewMainActivity.this, SettingActivity.class);
                    startActivityForResult(intent0, REQUEST_SETTINGS);
                } else if (msg.isLossLogin()) {
                    Intent intent = new Intent(NewMainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_SETTINGS);
                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                } else {
                    if (!NetUtil.IsInNetwork(NewMainActivity.this)) {
                        showTips(Configration.OFF_LINE_TIPS);
                    }
                }
            }
        });
    }

    private void goToCarView() {
        Intent intentGoShopCart = new Intent(NewMainActivity.this, NewShoppingCartActivity.class);
        startActivityForResult(intentGoShopCart, CAR_REQUEST_CODE);
    }

    //搭配查看更多跳转到发现推荐搭配
    @Override
    public void goToDiscover(int position) {
        ((RadioButton) findViewById(R.id.rd_main_find)).setChecked(true);
        if (mFragment instanceof NewDiscoverFragment) {
            ((NewDiscoverFragment) mFragment).setDapeiMore(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
    }

    /**
     * 广播接收器
     *
     * @author len
     */
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ivMessage.setVisibility(View.VISIBLE);
        }

    }

    private void showSuccessOpenShop(){
        View layoutTips = LayoutInflater.from(this).inflate(R.layout.success_open_shop_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        final Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(layoutTips);
        toast.show();
    }

    // 文章页面是否有分享按钮
    public void canShare(boolean isCan){
        if(isCan){
            ivShare.setVisibility(View.VISIBLE);
        }else {
            ivShare.setVisibility(View.GONE);
        }
    }
}
