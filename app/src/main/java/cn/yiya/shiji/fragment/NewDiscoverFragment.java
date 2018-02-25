package cn.yiya.shiji.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CollocationAddImgActivity;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.activity.PublishWorkActivity;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;

/**
 * 发现
 * Created by Amy on 2016/7/11.
 */
public class NewDiscoverFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private AppCompatActivity mActivity;

    //Toolbar
    private Toolbar toolbar;
    private LinearLayout llTitle;
    private TextView tvTitle;
    private ImageView ivTitle;

    private PopupWindow popupWindow;
    private int iSelectType = 0;        //0表示推荐 1表示关注
    private int LOGIN_REQUSET = 200;

    //TabLayout
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private NewFragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private String[] mTitles = new String[]{"搭配", "购物笔记"};
    private int currentItemPosition = 0;

    private NewMatchFragment matchFragment;
    private NewRecommendFragment recommendFragment;

    private Fragment mFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_discover, container, false);
        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        llTitle = (LinearLayout) getActivity().findViewById(R.id.toolbar_middle_layout);
        tvTitle = (TextView) getActivity().findViewById(R.id.toolbar_middle_find_txt);
        ivTitle = (ImageView) getActivity().findViewById(R.id.toolbar_middle_arrow);
        tvTitle.setText("推荐");
        ivTitle.setVisibility(View.VISIBLE);

        tabLayout = (TabLayout) mView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) mView.findViewById(R.id.viewpager);

        mFragments = new ArrayList<>();
        matchFragment = new NewMatchFragment().getInstance(iSelectType);
        mFragments.add(matchFragment);//搭配
        recommendFragment = new NewRecommendFragment().getInstance(iSelectType);
        mFragments.add(recommendFragment);//购物笔记
        mAdapter = new NewFragmentPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
        viewPager.setCurrentItem(0);
        mFragment = matchFragment;

        setDapei();
    }

    /**
     * 根据0推荐 1关注设置fragment
     *
     * @param iSelectType
     */
    private void setFragmentType(int iSelectType) {
        recommendFragment.setSwipeRefresh(iSelectType);
        matchFragment.setSwipeRefresh(iSelectType);
        viewPager.setCurrentItem(currentItemPosition);
    }

    @Override
    protected void initEvents() {
        llTitle.setOnClickListener(this);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentItemPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
                mFragment = mFragments.get(currentItemPosition);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //下拉选择推荐、关注
            case R.id.toolbar_middle_layout:
                popUp();
                break;
            default:
                break;
        }
    }

    /**
     * 弹出PopupWindow
     */
    private void popUp() {
        View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.view_new_recommend_follow, null);
        LinearLayout llFollowing = (LinearLayout) view.findViewById(R.id.ll_following);
        final TextView tvFollowing = (TextView) view.findViewById(R.id.tv_following);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        popupWindow.setAnimationStyle(R.style.PopUpWindowStyle);

        if (tvTitle.getText().toString().equals("推荐")) {
            tvFollowing.setText("关注");
        } else {
            tvFollowing.setText("推荐");
        }

        llFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (iSelectType == 0) {
                    new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                iSelectType = 1;
                                setFragmentType(iSelectType);
                                tvTitle.setText("关注");
                            } else if (msg.isLossLogin()) {
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivityForResult(intent, LOGIN_REQUSET);
                                getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                            } else {
                                if (!NetUtil.IsInNetwork(mContext)) {
                                    Util.toast(mContext, Configration.OFF_LINE_TIPS, true);
                                }
                            }
                        }
                    });
                } else {
                    iSelectType = 0;
                    setFragmentType(iSelectType);
                    tvTitle.setText("推荐");
                }

            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewPropertyAnimator.animate(ivTitle).cancel();
                ViewPropertyAnimator.animate(ivTitle).rotation(0).setDuration(300).start();
            }
        });

        ViewPropertyAnimator.animate(ivTitle).cancel();
        ViewPropertyAnimator.animate(ivTitle).rotation(180).setDuration(300).start();
        popupWindow.showAsDropDown(toolbar);
    }

    /**
     * 隐藏PopupWindow
     *
     * @return
     */
    public boolean hidePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            ivTitle.setImageResource(R.mipmap.ic_arrow_down);
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGIN_REQUSET) {
                iSelectType = 1;
                tvTitle.setText("关注");
                setFragmentType(iSelectType);
            } else {
                for (Fragment fragment : mFragments) {
                    if (fragment != null) {
                        fragment.onActivityResult(requestCode & 0xffff, resultCode, data);
                    }
                }
            }
        }
    }

    public void ClickCamera() {
        if (currentItemPosition == 0) {
            //搭配
            Intent intent = new Intent(mContext, CollocationAddImgActivity.class);
            startActivity(intent);
        } else {
            //购物笔记
            Intent intent1 = new Intent(mContext, PublishWorkActivity.class);
            intent1.putExtra("FLAG", 1);
            startActivity(intent1);
        }
    }

    private boolean bDapeiMore = false;
    private int dapeiLength = 0;

    public void setDapeiMore(int position) {
        this.bDapeiMore = true;
        this.dapeiLength = position;

        setDapei();
    }

    private void setDapei() {
        if (bDapeiMore == true && matchFragment != null) {
            if (iSelectType == 1) {
                bDapeiMore = false;
                iSelectType = 0;
                currentItemPosition = 0;
                tvTitle.setText("推荐");
                setFragmentType(iSelectType);
            } else {
                currentItemPosition = 0;
                viewPager.setCurrentItem(currentItemPosition);
            }
            matchFragment.scrollToPosition(bDapeiMore, dapeiLength);
        }
    }

    private boolean bSelected = true;

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

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (mFragment instanceof NewMatchFragment) {
                NewMatchFragment.bEnable = false;
                NewRecommendFragment.bEnable = true;
            } else if (mFragment instanceof NewRecommendFragment) {
                NewMatchFragment.bEnable = true;
                NewRecommendFragment.bEnable = false;
            }
        }
    }
}
