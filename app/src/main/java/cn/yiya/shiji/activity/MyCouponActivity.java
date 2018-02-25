package cn.yiya.shiji.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.CountryCouponList;
import cn.yiya.shiji.entity.MyCouponCountryList;
import cn.yiya.shiji.entity.navigation.CouponDetailInfo;
import cn.yiya.shiji.fragment.MyCouponFragment;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by Tom on 2016/4/5.
 */
public class MyCouponActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;

    private TabLayout couponTab;
    private ViewPager couponVp;
    private CouponFragmentPagerAdapter couponAdapter;

    private Handler handler;
    private String country;                                                          // coupon收藏列表预留参数
    private ArrayList<String> countryNameList;                                       // 用来存放国家名字列表
    public static boolean netState;           //  有网为true，没网为false
    private ArrayList<CountryCouponList> couponList;                               // 获取coupon的列表
    private ArrayList<ArrayList<CouponDetailInfo>> couponMoreInfoList;

    private ImageView iv_no_coupon;
    private LinearLayout llCoupon;
    private RelativeLayout rlCoupon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        netState = NetUtil.NetAvailable(this);
        handler = new Handler();
        countryNameList = new ArrayList<>();
        couponMoreInfoList = new ArrayList<>();
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("我的折扣券");
        findViewById(R.id.title_right).setVisibility(View.INVISIBLE);
        couponTab = (TabLayout) findViewById(R.id.coupon_tab);
        couponVp = (ViewPager) findViewById(R.id.coupon_viewpager);

//        iv_no_coupon = (ImageView)findViewById(R.id.iv_no_coupon);
        llCoupon = (LinearLayout)findViewById(R.id.ll_coupon);
//        rlCoupon = (RelativeLayout)findViewById(R.id.rl_coupon);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwuzhekouquan, "您还没有任何折扣券~", this);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {
        showPreDialog("正在加载...");
        getCouponListOfUser();
    }

    public class CouponFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public CouponFragmentPagerAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            MyCouponFragment myCouponFragment = MyCouponFragment.instanceFragment(position);
            myCouponFragment.setCountryCouponList(couponMoreInfoList.get(position));
            return myCouponFragment;
        }

        @Override
        public int getCount() {
            if (countryNameList == null) {
                return 0;
            }
            return countryNameList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return countryNameList.get(position);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                getCouponListOfUser();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void getCouponListOfUser() {
        if(netState) {
            new RetrofitRequest<MyCouponCountryList>(ApiRequest.getApiShiji().getCounponList()).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                MyCouponCountryList couponDetailObject = (MyCouponCountryList) msg.obj;
                                if(couponDetailObject.getList() != null && couponDetailObject.getList().size() == 0){
                                    setNullView(llCoupon);
                                    hidePreDialog();
                                    return;
                                }
                                couponList = couponDetailObject.list;
                                filterCountryName(couponList);
                                fillData();
                                setSuccessView(llCoupon);

                            } else {
                                if(msg.isLossLogin()){
                                    getLocalCouponListOfUser();
                                }
                            }
                            hidePreDialog();
                        }
                    }
            );
        }else {
            getLocalCouponListOfUser();
        }
    }

    // 离线或未登录时获取本地coupon列表数据
    public void getLocalCouponListOfUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Configration.TRAVELCOUPON, MODE_PRIVATE);
//        if(BaseApplication.userInfo == null){
//            hidePreDialog();
//            return;
//        }
        String couponObject = sharedPreferences.getString(BaseApplication.FileId + "couponObject", "");
        if(!TextUtils.isEmpty(couponObject)){
            MyCouponCountryList couponDetailObject = new Gson().fromJson(couponObject, MyCouponCountryList.class);
            ArrayList<CountryCouponList> list = couponDetailObject.getList();
            filterCountryName(list);
            fillData();
            setSuccessView(llCoupon);

        }else {
            setNullView(llCoupon);
        }
        hidePreDialog();
    }


    // 遍历coupon列表取出国家名
    public ArrayList<String> filterCountryName(ArrayList<CountryCouponList> couponList) {
        if (couponList != null) {
            for (int i = 0; i < couponList.size(); i++) {
                countryNameList.add(couponList.get(i).getCountry_name());
                couponMoreInfoList.add(couponList.get(i).getCoupon_list());
            }
        }
        return countryNameList;
    }

    // 填充数据
    public void fillData(){
        couponAdapter = new CouponFragmentPagerAdapter(getSupportFragmentManager(), this);
        couponVp.setAdapter(couponAdapter);
        couponTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        couponTab.setTabsFromPagerAdapter(couponAdapter);
        couponTab.setupWithViewPager(couponVp);
    }
}
