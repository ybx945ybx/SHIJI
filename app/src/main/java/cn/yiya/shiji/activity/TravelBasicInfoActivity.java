package cn.yiya.shiji.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.navigation.CityInfo;
import cn.yiya.shiji.entity.navigation.CountryListInfo;
import cn.yiya.shiji.entity.navigation.TaxAndInfos;
import cn.yiya.shiji.fragment.TravelBasicInfoFragment;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by Tom on 2016/4/9.
 */
public class TravelBasicInfoActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private ImageView ivBack;
    private TextView tvTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TravelBasicInfoFragmentPagerAdapter mPagerAdapter;
    private String id;                                              // 国家或城市id
    private ArrayList<TaxAndInfos> infos = new ArrayList<>();
    private TaxAndInfos tax;
    private int type;                                               // 1是国家 2是城市
    private int position;
    private boolean bNet;
    private int from;                                               // 1是退税指引 其他是简介
    private String countryId;
    private RelativeLayout rlytNoData;
    private LinearLayout llytContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_basic_info_activity);

        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        position = getIntent().getIntExtra("position", 0);
        from = getIntent().getIntExtra("from", 0);
        countryId = getIntent().getStringExtra("countryId");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setTextColor(Color.parseColor("#3c3c3c"));
        tvTitle.setTextSize(17f);
        findViewById(R.id.title_right).setVisibility(View.INVISIBLE);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        rlytNoData = (RelativeLayout) findViewById(R.id.rl_without_net);
        llytContent = (LinearLayout) findViewById(R.id.basic_content_layout);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlytNoData.setOnClickListener(this);
    }

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        showPreDialog("加载中");
        bNet = NetUtil.IsInNetwork(this);

        if(type == 1){
            if (bNet) {
                new RetrofitRequest<CountryListInfo>(ApiRequest.getApiShiji().getCountryDetailInfo(String.valueOf(id))).
                        handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleCountryMsg(msg);
                            }
                        });
            } else {
                GetLocalRequest.getInstance().getLocalCountry(id, new Handler(getMainLooper()), new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        handleCountryMsg(msg);
                    }
                });
            }

        } else {
            if (bNet) {
                new RetrofitRequest<CityInfo>(ApiRequest.getApiShiji().getCityDetailInfo(String.valueOf(id))).handRequest(
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleCityMsg(msg);
                            }
                        }
                );
            } else {
                GetLocalRequest.getInstance().getCityBriefInfo("detail", id, countryId, new Handler(getMainLooper()),
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleCityMsg(msg);
                            }
                        });
            }
        }
    }

    private void handleCountryMsg(HttpMessage msg){
        if(msg.isSuccess()){
            hidePreDialog();
            CountryListInfo countryListInfo = (CountryListInfo)msg.obj;

            // 当前对象空，直接显示无数据界面
            if (countryListInfo == null) {
                setNoDataView(true);
                return;
            }
            infos = countryListInfo.getInfos();

            // 当前获取对象数据大小为0，直接显示无数据界面
            if (infos.size() == 0) {
                setNoDataView(true);
                return;
            }

            setNoDataView(false);
            tax = countryListInfo.getTax();
            if(tax != null) {
                TaxAndInfos point = new TaxAndInfos();
                point.setTitle("退税指引");
                point.setDes(tax.getContent());
                infos.add(point);
                if(from == 1){
                    tvTitle.setText("退税指引");
                }else {
                    tvTitle.setText(infos.get(0).getTitle());
                }
            }else {
                tvTitle.setText(infos.get(0).getTitle());
            }

            mPagerAdapter = new TravelBasicInfoFragmentPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mPagerAdapter);
            tabLayout.setTabsFromPagerAdapter(mPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tvTitle.setText(tab.getText());
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            Handler handler = new Handler(getMainLooper());
            if(from == 1 && infos != null){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(infos.size() - 1);
                    }
                }, 100);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }, 500);
        } else {
            hidePreDialog();
            setNoDataView(true);
        }
    }

    private void handleCityMsg(HttpMessage msg) {
        if (msg.isSuccess()) {
            hidePreDialog();
            CityInfo countryListInfo = (CityInfo) msg.obj;

            // 当前对象空，直接显示无数据界面
            if (countryListInfo == null) {
                setNoDataView(true);
                return;
            }

            infos = countryListInfo.getInfos();

            // 当前获取对象数据大小为0，直接显示无数据界面
            if (infos.size() == 0) {
                setNoDataView(true);
                return;
            }

            setNoDataView(false);
            tvTitle.setText(infos.get(0).getTitle());

            mPagerAdapter = new TravelBasicInfoFragmentPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mPagerAdapter);
            tabLayout.setTabsFromPagerAdapter(mPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tvTitle.setText(tab.getText());
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            hidePreDialog();
            setNoDataView(true);
        }
    }

    public class TravelBasicInfoFragmentPagerAdapter extends FragmentPagerAdapter {


        public TravelBasicInfoFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TravelBasicInfoFragment.instancePage(infos.get(position).getDes());
        }

        @Override
        public int getCount() {
            if(infos == null){
                return 0;
            }else {
                return infos.size();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return infos.get(position).getTitle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.rl_without_net:
                initData();
                break;
        }
    }

    // 没有数据时的界面切换
    private void setNoDataView(boolean bShow) {
        if (bShow) {
            rlytNoData.setVisibility(View.VISIBLE);
            llytContent.setVisibility(View.GONE);
        } else {
            rlytNoData.setVisibility(View.GONE);
            llytContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
