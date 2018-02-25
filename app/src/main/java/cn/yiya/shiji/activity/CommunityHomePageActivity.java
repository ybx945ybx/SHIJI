package cn.yiya.shiji.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.fragment.WorkListFragment;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.DisableViewPage;
import cn.yiya.shiji.views.RoundedNormalIV;

/**
 * 社区个人主页
 * Created by Tom on 2016/6/16.
 */
public class CommunityHomePageActivity extends BaseAppCompatActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;
    private ImageView ivBack;
    private TextView tvTitle;

    //User
    private LinearLayout llUser;
    private RoundedNormalIV civHead;
    private ImageView ivFollowEdit;
    private TextView tvName;
    private TextView ivRedPeople;
    private TextView tvTags;
    private TextView tvFollows;
    private TextView tvFans;
    private TextView tvLikes;
    private ArrayList<User> cbList = new ArrayList<User>();
    //红人资料
    private LinearLayout llRedDesc;
    private TextView tvRedDesc;

    //banner
    private FrameLayout flytIdViewpager;
    private IndicatorViewPager indicatorViewPager;
    private View viewBanner;
    private TabLayout tabLayout;
    private DisableViewPage mViewPage;
    private NewFragmentPagerAdapter communityFragmentAdapter;
    private List<Fragment> mFragments;
    private static final String[] mTitles = new String[]{"搭配", "购物笔记"};

    private boolean isCustom;                       // 主客态  true是主态false是客态
    private int user_id;
    private String user_name;
    private boolean is_follow;
    private static final int REQUEST_EDIT = 1000;
    private static final int REQUEST_FOLLOW = 1001;
    private static final int REQUEST_FANS = 1002;
    private static final int REQUEST_PUBLISH = 1003;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;

    private boolean userInfoChanged;
    private Indicator indicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_home_page);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            isCustom = intent.getBooleanExtra("isCurUser", false);
            user_id = intent.getIntExtra("user_id", 0);
            is_follow = intent.getBooleanExtra("is_follow", false);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.toolbar_back);
        tvTitle = (TextView) findViewById(R.id.collpase_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        startAlphaAnimation(tvTitle, 0, View.INVISIBLE);
        //banner
        flytIdViewpager = (FrameLayout) findViewById(R.id.flyt_id_viewpager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        indicator = (Indicator) findViewById(R.id.guide_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);

        appBarLayout = (AppBarLayout) findViewById(R.id.ablyt_home_page);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        CoordinatorLayout coordinator = (CoordinatorLayout) findViewById(R.id.main_content);
        if (behavior != null) {
            behavior.setTopAndBottomOffset(100);
            behavior.onNestedPreScroll(coordinator, appBarLayout, null, 0, 1, new int[2]);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.new_black_color));
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        mViewPage = (DisableViewPage) findViewById(R.id.viewpager);
        mFragments = new ArrayList<>();
        mFragments.add(new WorkListFragment().getInstance(user_id, isCustom, 2, false));//搭配
        mFragments.add(new WorkListFragment().getInstance(user_id, isCustom, 1, false));//购物笔记
        communityFragmentAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        tabLayout.setTabsFromPagerAdapter(communityFragmentAdapter);
        mViewPage.setAdapter(communityFragmentAdapter);
        tabLayout.setupWithViewPager(mViewPage);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
        mViewPage.setCurrentItem(0);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void init() {
        getUserInfo();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("userInfoChanged", userInfoChanged);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                onBackPressed();
                break;
            case R.id.iv_follow_edit:
                if (isCustom) {
                    Intent intent = new Intent(this, EditUserInfoActivity.class);
                    startActivityForResult(intent, REQUEST_EDIT);
                } else {
                    setFollow(is_follow);
                    userInfoChanged = true;
                }
                break;
            case R.id.tv_tags:
                Intent intent3 = new Intent(this, UserTagActivity.class);
                intent3.putExtra("user_name", user_name);
                intent3.putExtra("user_id", user_id);
                startActivity(intent3);
                break;
            case R.id.tv_follows:
                Intent intent1 = new Intent(this, UserFansActivity.class);
                intent1.putExtra("isFans", false);
                intent1.putExtra("user_name", user_name);
                intent1.putExtra("user_id", user_id);
                intent1.putExtra("isCustom", isCustom);
                if (isCustom) {
                    startActivityForResult(intent1, REQUEST_FOLLOW);
                } else {
                    startActivity(intent1);
                }
                break;
            case R.id.tv_fans:
                Intent intent4 = new Intent(this, UserFansActivity.class);
                intent4.putExtra("isFans", true);
                intent4.putExtra("user_id", user_id);
                intent4.putExtra("user_name", user_name);
                intent4.putExtra("isCustom", isCustom);    //主态跳转为true，可对页面进行操作， 关注的对象页面跳转为，不可对页面进行操作
                if (isCustom) {
                    startActivityForResult(intent4, REQUEST_FANS);
                } else {
                    startActivity(intent4);
                }
                break;
            case R.id.tv_likes:
                Intent intent5 = new Intent(this, NewUserLikedActivity.class);
                intent5.putExtra("user_id", user_id);
                startActivity(intent5);
                break;
            default:
                break;
        }

    }

    // 获取个人信息
    public void getUserInfo() {
        showPreDialog("");
        if (isCustom) {
            new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        hidePreDialog();
                        User mUser = (User) msg.obj;
                        user_name = mUser.getName();
                        updateViews(mUser);
                    } else {
                        hidePreDialog();
                    }
                }
            });
        } else {
            new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserSNSDetail(String.valueOf(user_id))).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                hidePreDialog();
                                User mUser = (User) msg.obj;
                                user_name = mUser.getName();
                                updateViews(mUser);
                            } else {
                                hidePreDialog();
                            }
                        }
                    }
            );
        }
    }

    private void updateUserNumberView(final int type) {
        new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    User user = (User) msg.obj;
                    switch (type) {
                        case REQUEST_FOLLOW:
                            tvFollows.setText("关注  " + user.getFollowing_count());
                            break;
                        case REQUEST_PUBLISH:
                            tvTags.setText("标签  " + user.getTags_count());
                            break;
                        case REQUEST_FANS:
                            tvFans.setText("粉丝  " + user.getFans_count());
                            break;
                        case REQUEST_EDIT:
                            Netroid.displayImage(Util.transfer(user.getHead()), civHead, R.mipmap.user_icon_default);
                            tvName.setText(user.getName());
                            break;
                    }
                }
            }
        });
    }

    private void updateViews(User user) {
        if (user != null) {
            cbList.add(user);
            if (!TextUtils.isEmpty(user.getRed_desc())) {
                cbList.add(user);
            } else {
                mViewPage.setCurrentItem(1);
            }
            indicatorViewPager.setAdapter(adapter);
            tvTitle.setText(user.getName());
        }
    }


    private void setFollow(boolean is_follow) {
        if (is_follow) {
            unFollowUser(user_id);
        } else {
            followUser(user_id);
        }
    }

    private void unFollowUser(final int user_id) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(user_id))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            is_follow = false;
                            cbList.get(0).setIs_follow(false);
                            adapter.notifyDataSetChanged();
                            showTips("取消关注");
                        } else {
                            showTips(msg.message);
                        }
                    }
                }
        );
    }

    private void followUser(final int user_id) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(user_id))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    is_follow = true;
                    cbList.get(0).setIs_follow(true);
                    adapter.notifyDataSetChanged();
                    showTips("关注");
                } else {
                    showTips(msg.message);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_EDIT:
                    if (data != null) {
                        Bitmap photo = data.getParcelableExtra("photo");
                        String name = data.getStringExtra("name");
                        if (!TextUtils.isEmpty(name)) {
                            cbList.get(0).setBitmap(photo);
                            cbList.get(0).setName(name);
                        }
                        String redDesc = data.getStringExtra("user_desc");
                        if (!TextUtils.isEmpty(redDesc)) {
                            if (cbList.size() == 1) {
                                cbList.add(cbList.get(0));
                            }
                            cbList.get(1).setRed_desc(redDesc);
                        } else {
                            if (cbList.size() == 2) {
                                cbList.remove(1);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    userInfoChanged = true;
                    break;
                case REQUEST_FOLLOW:
                    updateUserNumberView(REQUEST_FOLLOW);
                    userInfoChanged = true;
                    break;
                case REQUEST_FANS:
                    updateUserNumberView(REQUEST_FANS);
                    userInfoChanged = true;
                    break;
                case REQUEST_PUBLISH:
                    updateUserNumberView(REQUEST_PUBLISH);
                    userInfoChanged = true;
                    break;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;

        handleToolbarTitleVisibility(percentage);
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


    /*===============================红人轮播=============================================*/

    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public int getCount() {
            return cbList.size();
        }

        @Override
        public View getViewForTab(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(CommunityHomePageActivity.this).inflate(R.layout.tab_indicator_guide, viewGroup, false);
            }
            if (cbList.size() == 1) {
                view.setVisibility(View.INVISIBLE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public View getViewForPage(int i, View view, ViewGroup viewGroup) {
            viewBanner = LayoutInflater.from(CommunityHomePageActivity.this).inflate(R.layout.community_homepage_banner, null);
            llUser = (LinearLayout) viewBanner.findViewById(R.id.ll_user);
            civHead = (RoundedNormalIV) viewBanner.findViewById(R.id.civ_head);
            ivFollowEdit = (ImageView) viewBanner.findViewById(R.id.iv_follow_edit);
            tvName = (TextView) viewBanner.findViewById(R.id.tv_name);
            ivRedPeople = (TextView) viewBanner.findViewById(R.id.iv_red_people);
            tvTags = (TextView) viewBanner.findViewById(R.id.tv_tags);
            tvFollows = (TextView) viewBanner.findViewById(R.id.tv_follows);
            tvFans = (TextView) viewBanner.findViewById(R.id.tv_fans);
            tvLikes = (TextView) viewBanner.findViewById(R.id.tv_likes);

            ScrollView scrollRedDesc = (ScrollView) viewBanner.findViewById(R.id.scroll_red_desc);
            llRedDesc = (LinearLayout) viewBanner.findViewById(R.id.ll_red_desc);
            tvRedDesc = (TextView) viewBanner.findViewById(R.id.tv_red_desc);

            if (cbList.size() == 2 && i == 1) {
                User data = cbList.get(i);
                llUser.setVisibility(View.GONE);
                scrollRedDesc .setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(data.getRed_desc())) {
                    tvRedDesc.setText(data.getRed_desc());
                }
            } else {
                User data = cbList.get(i);
                llUser.setVisibility(View.VISIBLE);
                scrollRedDesc.setVisibility(View.GONE);

                if (data.getBitmap() != null) {
                    civHead.setImageBitmap(data.getBitmap());
                } else {
                    if (data.getHead() != null) {
                        Netroid.displayImage(Util.transfer(data.getHead()), civHead, R.mipmap.user_icon_default);
                    }
                }

                if (isCustom) {
                    ivFollowEdit.setImageResource(R.mipmap.white_edite_user);
                } else {
                    if (data.is_follow()) {
                        is_follow = data.is_follow();
                        ivFollowEdit.setImageResource(R.mipmap.white_followed);
                    } else {
                        ivFollowEdit.setImageResource(R.mipmap.white_tofollow);
                    }
                }

                tvName.setText(data.getName());
                if (data.getRed()==1) {
                    ivRedPeople.setVisibility(View.VISIBLE);
                    ivRedPeople.setText(Util.transferLevel(data.getLevel()));
                } else {
                    ivRedPeople.setVisibility(View.GONE);
                }
                tvTags.setText("标签  " + data.getTags_count());
                tvFollows.setText("关注  " + data.getFollowing_count());
                tvFans.setText("粉丝  " + data.getFans_count());
                tvLikes.setText("已赞  " + data.getLike_count());

                ivFollowEdit.setOnClickListener(CommunityHomePageActivity.this);
                tvTags.setOnClickListener(CommunityHomePageActivity.this);
                tvFollows.setOnClickListener(CommunityHomePageActivity.this);
                tvFans.setOnClickListener(CommunityHomePageActivity.this);
                tvLikes.setOnClickListener(CommunityHomePageActivity.this);
            }
            return viewBanner;
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_NONE;
        }
    };

    private boolean bSelected = true;

    /**
     * 自定义tab
     *
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        if (position == 0 && bSelected) {
            v.setSelected(true);
            bSelected = false;
        }
        return v;
    }

}
