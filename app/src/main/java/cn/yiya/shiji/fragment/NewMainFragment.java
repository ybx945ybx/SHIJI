package cn.yiya.shiji.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.HotCategoryObject;
import cn.yiya.shiji.entity.LogstashReport.MainMenuLogstashItem;
import cn.yiya.shiji.utils.NetUtil;


/**
 * 商城主页
 * Created by Amy on 2016/8/17.
 */
public class NewMainFragment extends BaseFragment implements View.OnClickListener {
    private View mView;

    private SwipeRefreshLayout srlRefresh;
    private LinearLayout llMain;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String[] mTitles;
    private List<HotCategoryObject.SecondItem> mList;
    private ArrayList<Fragment> mFragments;
    private NewFragmentPagerAdapter pagerAdapter;

    private boolean bSelected = true;

    private Fragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_main, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        srlRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.srl_refresh);
        llMain = (LinearLayout) mView.findViewById(R.id.ll_main);
        tabLayout = (TabLayout) mView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);

        setMainView(mView);
        initDefaultNullView(R.mipmap.zanwugouwubiji, "", this, 0, 230);
    }

    @Override
    protected void initEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 打点事件
                MainMenuLogstashItem mainMenuLogstashItem = new MainMenuLogstashItem();
                mainMenuLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                mainMenuLogstashItem.setType_name(mTitles[tab.getPosition()]);
                new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainTopMenuEvent(mainMenuLogstashItem)).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {

                    }
                });

                viewPager.setCurrentItem(tab.getPosition());
                mFragment = mFragments.get(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void init() {
        setSwipeRefresh();
    }

    /**
     * 设置下拉刷新，刷新后更新页面数据
     */
    public void setSwipeRefresh() {
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMenuList();
            }
        };
        srlRefresh.setOnRefreshListener(listener);
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(true);
            }
        });
        listener.onRefresh();
    }

    /**
     * 自定义tab
     *
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        if (position == 0 && bSelected) {
            v.setSelected(true);
            bSelected = false;
        }
        return v;
    }


    /**
     * 获取菜单列表
     */
    private void getMenuList() {
        new RetrofitRequest<HotCategoryObject>(ApiRequest.getApiShiji().getMenuList()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                srlRefresh.setRefreshing(false);
                if (msg.isSuccess()) {
                    srlRefresh.setEnabled(false);
                    llMain.setVisibility(View.VISIBLE);
                    setSuccessView(null);

                    HotCategoryObject obj = (HotCategoryObject) msg.obj;
                    mList = obj.getList();
                    mTitles = new String[mList.size()];
                    mFragments = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        String title = mList.get(i).getName().trim();
                        mTitles[i] = title;
                        if (title.equals("首页")) {
                            mFragments.add(new NewMainHomeFragment());//首页
                        } else {
                            mFragments.add(new NewMainMenuFragment().getInstance(String.valueOf(mList.get(i).getId()), mList.get(i).getName()));//菜单
                        }
                    }
                    pagerAdapter = new NewFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
                    viewPager.setAdapter(pagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        tab.setCustomView(getTabView(i));
                    }
                    viewPager.setCurrentItem(0);
                    mFragment = mFragments.get(0);
                } else {
                    if (!NetUtil.IsInNetwork(getActivity())) {
                        srlRefresh.setEnabled(false);
                        srlRefresh.setVisibility(View.GONE);
                        llMain.setVisibility(View.GONE);
                        setOffNetView(null);
                    }
                }
                srlRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : mFragments) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode & 0xffff, resultCode, data);
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reload:
                srlRefresh.setVisibility(View.VISIBLE);
                srlRefresh.setEnabled(true);
                init();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (mFragment instanceof NewMainHomeFragment) {
                NewMainHomeFragment.bEnable = false;
                NewMainMenuFragment.bEnable = true;
            } else if (mFragment instanceof NewMainMenuFragment) {
                NewMainHomeFragment.bEnable = true;
                NewMainMenuFragment.bEnable = false;
            }
        }
    }
}
