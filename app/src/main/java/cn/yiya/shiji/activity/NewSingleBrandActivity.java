package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.GoodsListOrderAdapter;
import cn.yiya.shiji.adapter.NewFragmentPagerAdapter;
import cn.yiya.shiji.adapter.NewSingleBrandAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ActivityListObject;
import cn.yiya.shiji.entity.BackgroundItem;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.MallLimitOptionObject;
import cn.yiya.shiji.entity.OfficialTagsItem;
import cn.yiya.shiji.entity.ParaObject;
import cn.yiya.shiji.entity.ShopGoodsBrandGoodsObject;
import cn.yiya.shiji.entity.SortInfo;
import cn.yiya.shiji.entity.TagProduceObject;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.fragment.NewSingleBrandFragment;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Amy on 2016/10/10.
 */

public class NewSingleBrandActivity extends BaseAppCompatActivity implements View.OnClickListener, NewSingleBrandAdapter.AdapterAddViewListener {

    private int brand_id;
    private int type = 0;               // 商品详情晒单列表跳转过来为2  则显示成晒单界面
    private BackgroundItem backgroundItem;
    private String tagWork = "";
    private int tagId;


    private ImageView toolbarLeft;
    private RelativeLayout rlSearch;
    private RelativeLayout rlRight;
    private TextView tvCarcount;

    //品牌关注
    private TextView tvBrand;
    private Button btnFollow;
    //品牌介绍
    private LinearLayout rlDesc;
    private RelativeLayout rlDescTitle;
    private ImageView triangleArrow;

    private LinearLayout llDescContent;
    private TextView tvBrandDesc;

    //活动图部分
    private ConvenientBanner banner;
    private View viewBanner;

    //最热门 新上架 最折扣
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //商品 搭配 晒单 tab
    private RelativeLayout rlScrollTablayout;
    private TabLayout tabLayoutList;
    private LinearLayout llScrollFilter;
    private TextView tvScrollOrder;
    private TextView tvScrollFilter;

    //商品 搭配 晒单列表
    private RecyclerView recyclerView;
    private NewSingleBrandAdapter mAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int lastVisibleItemPosition;
    private int firstVisibleItemPosition;

    //位于顶部固定的tablayout
    private RelativeLayout rlTopTablayout;
    private TabLayout tabLayoutTop;
    private LinearLayout llTopFilter;
    private TextView tvTopOrder;
    private TextView tvTopFilter;

    //最热门 新上架 最折扣
    private String[] mTitles = {"最热门", "新上架", "最折扣"};
    private NewFragmentPagerAdapter pagerAdapter;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    //商品 搭配 晒单
    private String[] mTitlesList = {"商品", "搭配", "晒单"};

    private boolean bSelected = true;
    private boolean bSelectedList = true;
    private boolean bSelectedTop = true;

    // 左侧排序popupwindow
    private PopupWindow orderPopupWindow;
    private LinearLayout llytOrder;
    private RecyclerView rycvOrder;
    private GoodsListOrderAdapter goodsListOrderAdapter;

    private int angle = 0;
    private int tabPosition = 0;
    private boolean isScrollTab = false;
    private boolean isTopTab = false;
    private boolean isTop = false;

    private int nOffset;
    private boolean isBottom;
    private final int LIMIT_FORTY = 40;
    private final int LIMIT_TEN = 10;
    private final int LIMIT_TWENTY = 20;
    private static final int LOGIN_REQUSET = 1000;
    private static final int REQUEST_FILTER = 1001;
    private static final int REQUEST_MATCH = 1002;
    private static final int REQUEST_WORK = 1003;

    /*商品*/
    private ParaObject post;
    private MallLimitOptionInfo mInfos;
    private static final String ALL = "全部";

    //选择的排序筛选条件  isCheck
    private String category_ids = "";
    private String price_ranges_id = "";
    private String sort_ids = "";
    private String genders = "";
    private String color = "";
    private String size = "";
    private String brand_ids = "";

    //暂无结果
    private LinearLayout llNull;
    private TextView tvNull;
    private LinearLayout.LayoutParams tvNullLayoutParams;

    private boolean isRecommend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_single_brand);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            brand_id = intent.getIntExtra("brand_id", 0);
            type = intent.getIntExtra("type", 0);
            tabPosition = (type == 2 ? 2 : 0);
            isRecommend = intent.getBooleanExtra("isRecommend", false);
        }
    }

    @Override
    protected void initViews() {
        toolbarLeft = (ImageView) findViewById(R.id.toolbar_left);
        rlSearch = (RelativeLayout) findViewById(R.id.toolbar_middle_search_layout);
        rlRight = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCarcount = (TextView) findViewById(R.id.toolbar_right_count);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new NewSingleBrandAdapter(this, staggeredGridLayoutManager, this, isRecommend);
        recyclerView.setAdapter(mAdapter);

        //固定在顶部的布局
        rlTopTablayout = (RelativeLayout) findViewById(R.id.rl_top_tablayout);
        tabLayoutTop = (TabLayout) findViewById(R.id.tablayout_top);
        llTopFilter = (LinearLayout) findViewById(R.id.ll_order_filter_top);
        tvTopOrder = (TextView) findViewById(R.id.tv_order_top);
        tvTopFilter = (TextView) findViewById(R.id.tv_filter_top);
    }

    @Override
    protected void initEvents() {
        toolbarLeft.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        rlRight.setOnClickListener(this);

        tvTopOrder.setOnClickListener(this);
        tvTopFilter.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (tabPosition == 0) {
                    if ((visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                            (lastVisibleItemPosition) >= totalItemCount - 10) && !isBottom) {
                        loadMoreBrandGoodsData();
                    }
                } else if (tabPosition == 1) {
                    if ((visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                            (lastVisibleItemPosition) >= totalItemCount - 3) && !isBottom) {
                        loadMoreMatchData();
                    }

                } else if (tabPosition == 2) {
                    if ((visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                            (lastVisibleItemPosition) >= totalItemCount - 3) && !isBottom) {
                        loadMoreTagData();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] firstPositions = new int[2];
                if (firstPositions == null) {
                    firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                firstVisibleItemPosition = Math.min(firstPositions[0], firstPositions[1]);

                if (firstVisibleItemPosition >= 3) {
                    rlTopTablayout.setVisibility(View.VISIBLE);
                    if (llScrollFilter.getVisibility() == View.VISIBLE) {
                        llTopFilter.setVisibility(View.VISIBLE);
                    } else {
                        llTopFilter.setVisibility(View.INVISIBLE);
                    }
                } else {
                    rlTopTablayout.setVisibility(View.INVISIBLE);
                    llTopFilter.setVisibility(View.INVISIBLE);
                }

                int[] lastPositions = new int[2];
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = Math.max(lastPositions[0], lastPositions[1]);

            }
        });
    }

    @Override
    protected void init() {
        initPostData();
        if (type == 0) {
            loadBrandGoodsData(null, true, false);
        }
        getBrandOption();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.toolbar_middle_search_layout:
                Intent intent1 = new Intent(this, NewSearchActivity.class);
                if(isRecommend){
                    intent1.putExtra("isRecommend", true);
                }
                startActivity(intent1);
                break;
            case R.id.toolbar_right_layout:
                Intent intent = new Intent(this, NewShoppingCartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.rl_desc_title:
                showDesc();
                break;
            case R.id.btn_follow:
                clickFollow();
                break;
            //排序 筛选
            case R.id.tv_order:
                isTop = false;
                staggeredGridLayoutManager.scrollToPositionWithOffset(3, -2);
                showOrderPopwindow();
                break;
            case R.id.tv_order_top:
                isTop = true;
                showOrderPopwindow();
                break;
            case R.id.tv_filter:
                isTop = false;
                goToFilter();
                break;
            case R.id.tv_filter_top:
                isTop = true;
                goToFilter();
                break;
            default:
                break;
        }
    }

    /**
     * 获取购物车数量
     */
    private void initCarCount() {
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    tvCarcount.setVisibility(View.VISIBLE);
                    CarCountInfo info = (CarCountInfo) msg.obj;
                    tvCarcount.setText("" + info.getCount());
                    if (info.getCount() == 0) {
                        tvCarcount.setVisibility(View.GONE);
                    }
                } else {
                    tvCarcount.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 是否显示品牌描述
     */
    private void showDesc() {
        angle += 180;
        ViewPropertyAnimator.animate(triangleArrow).cancel();
        ViewPropertyAnimator.animate(triangleArrow).rotation(angle % 360).setDuration(300).start();
        if (angle % 360 == 0) {
            //隐藏
            llDescContent.post(new Runnable() {
                @Override
                public void run() {
                    llDescContent.setVisibility(View.GONE);
                }
            });
        } else if (angle % 360 == 180) {
            //显示
            llDescContent.post(new Runnable() {
                @Override
                public void run() {
                    llDescContent.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    /**
     * 关注
     */
    private void clickFollow() {
        if (backgroundItem == null) return;
        if (backgroundItem.is_follow()) {
            new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(String.valueOf(backgroundItem.getTag_id()))).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                showTips("已取消关注");
                                btnFollow.setText("关注");
                                backgroundItem.setIs_follow(false);
                            } else if (msg.isLossLogin()) {
                                login();
                            } else {
                                showTips("取消失败");
                            }
                        }
                    }
            );
        } else {
            new RetrofitRequest<>(ApiRequest.getApiShiji().postFollowBrands(String.valueOf(backgroundItem.getTag_id()))).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                showTips("已关注");
                                btnFollow.setText("已关注");
                                backgroundItem.setIs_follow(true);
                            } else if (msg.isLossLogin()) {
                                login();
                            } else {
                                showTips("关注失败");
                            }
                        }
                    }
            );
        }
    }


    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, LOGIN_REQUSET);
        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
    }

     /*==============================RecyclerView 列表数据之前的布局=============================*/

    @Override
    public void addDescView(View itemView) {
        //品牌关注
        tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
        btnFollow = (Button) itemView.findViewById(R.id.btn_follow);

        //品牌介绍
        rlDesc = (LinearLayout) itemView.findViewById(R.id.rl_desc);
        rlDescTitle = (RelativeLayout) itemView.findViewById(R.id.rl_desc_title);
        triangleArrow = (ImageView) itemView.findViewById(R.id.triangle_arrow);
        llDescContent = (LinearLayout) itemView.findViewById(R.id.ll_desc_content);
        tvBrandDesc = (TextView) itemView.findViewById(R.id.tv_brand_desc);

        getBrandDesc();

        rlDescTitle.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
    }

    @Override
    public void addBannerView(View itemView) {
        //活动图部分
        banner = (ConvenientBanner) itemView.findViewById(R.id.main_banner);
        viewBanner = itemView.findViewById(R.id.viewline_banner);
        getActBanner();
    }

    @Override
    public void addPopularView(View itemView) {
        //最热门 新上架 最折扣
        tabLayout = (TabLayout) itemView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) itemView.findViewById(R.id.viewpager);
        setGoodsFragment();
    }

    @Override
    public void addTabView(View itemView) {
        rlScrollTablayout = (RelativeLayout) itemView.findViewById(R.id.rl_scroll_tablayout);
        tabLayoutList = (TabLayout) itemView.findViewById(R.id.tablayout_list);
        llScrollFilter = (LinearLayout) itemView.findViewById(R.id.ll_order_filter);
        tvScrollOrder = (TextView) itemView.findViewById(R.id.tv_order);
        tvScrollFilter = (TextView) itemView.findViewById(R.id.tv_filter);
        if (tabPosition == 0) llScrollFilter.setVisibility(View.VISIBLE);
        else llScrollFilter.setVisibility(View.GONE);
        setListTab();

        tvScrollOrder.setOnClickListener(this);
        tvScrollFilter.setOnClickListener(this);
    }

    @Override
    public void addNullView(View itemView) {
        llNull = (LinearLayout) itemView.findViewById(R.id.ll_null);
        tvNull = (TextView) itemView.findViewById(R.id.tv_default_null);
        tvNullLayoutParams = (LinearLayout.LayoutParams) llNull.getLayoutParams();
        tvNullLayoutParams.height = 0;
        llNull.setLayoutParams(tvNullLayoutParams);
    }

    @Override
    public void goToMatchDetail(int workId, int position) {
        Intent intent = new Intent(this, NewMatchDetailActivity.class);
        intent.putExtra("work_id", workId);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_MATCH);
    }

    @Override
    public void goToWorkDetail(int workId, int position) {
        Intent intent = new Intent(this, NewWorkDetailsActivity.class);
        intent.putExtra("work_id", workId);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_WORK);
    }

    @Override
    public void goToLogin() {
        login();
    }

    /**
     * 获取品牌基本信息
     */
    private void getBrandDesc() {
        new RetrofitRequest<BackgroundItem>(ApiRequest.getApiShiji().getBrandsHeaderDetail(String.valueOf(brand_id)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            backgroundItem = (BackgroundItem) msg.obj;
                            if (!TextUtils.isEmpty(backgroundItem.getTag())) {
                                tagWork = backgroundItem.getTag();
                            } else {
                                tagWork = backgroundItem.getName();
                            }
                            tvBrand.setText(backgroundItem.getName());
                            if (backgroundItem.is_follow()) {
                                btnFollow.setText("已关注");
                            }
                            tvBrandDesc.setText(backgroundItem.getDes());
                            if (TextUtils.isEmpty(backgroundItem.getDes())) {
                                rlDesc.setVisibility(View.GONE);
                            }

                            getTagId();
                        }
                    }
                });
    }

    /**
     * 获取官方标签
     */
    private void getTagId() {
        new RetrofitRequest<OfficialTagsItem>(ApiRequest.getApiShiji().getOfficialTagsList(tagWork))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            if (msg.obj != null) {
                                OfficialTagsItem item = (OfficialTagsItem) msg.obj;
                                tagId = item.getTag_id();
                                if (type == 2) {
                                    initTagData();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 获取品牌活动图
     */
    private void getActBanner() {
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        layoutParams.width = SimpleUtils.getScreenWidth(this);

        new RetrofitRequest<ActivityListObject>(ApiRequest.getApiShiji().getActivityList(String.valueOf(brand_id))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    ActivityListObject obj = (ActivityListObject) msg.obj;
                    if (obj == null || obj.list == null || obj.getList().isEmpty()) {
                        layoutParams.height = 0;
                        banner.setLayoutParams(layoutParams);
                        banner.setVisibility(View.GONE);
                        viewBanner.setVisibility(View.GONE);
                        return;
                    }
                    layoutParams.height = layoutParams.width * 190 / 375;
                    banner.setLayoutParams(layoutParams);
                    banner.setVisibility(View.VISIBLE);
                    viewBanner.setVisibility(View.VISIBLE);

                    banner.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
                    banner.startTurning(4000);

                    if (obj.getList().size() == 1) {
                        banner.setCanLoop(false);
                        banner.setManualPageable(false);
                        banner.setPointViewVisible(false);
                    }

                    banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                        @Override
                        public NetworkImageHolderView createHolder() {
                            return new NetworkImageHolderView();
                        }
                    }, obj.getList());
                }
            }
        });
    }

    class NetworkImageHolderView implements Holder<ActivityListObject.ActivityListItem> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final ActivityListObject.ActivityListItem item) {
            imageView.setImageResource(R.drawable.user_dafault);
            Netroid.displayImage(Util.ClipImageBannerView(item.getCover()), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewSingleBrandActivity.this, HomeIssueActivity.class);
                    intent.putExtra("activityId", item.getId() + "");
                    intent.putExtra("menuId", 7);
                    if(isRecommend){
                        intent.putExtra("isRecommend", true);
                    }
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 获取最热门 新上架 最折扣数据
     */
    private void setGoodsFragment() {
        mFragments.add(new NewSingleBrandFragment().getInstance(brand_id, 1, false, isRecommend));
        mFragments.add(new NewSingleBrandFragment().getInstance(brand_id, 2, false, isRecommend));
        mFragments.add(new NewSingleBrandFragment().getInstance(brand_id, 3, true, isRecommend));

        pagerAdapter = new NewFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i, mTitles, 1));
        }
        viewPager.setCurrentItem(0);
    }

    /**
     * 自定义tab
     *
     * @param position
     * @return
     */
    public View getTabView(int position, String[] mTitles, int index) {
        View v = LayoutInflater.from(this).inflate(R.layout.item_tab_view, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(mTitles[position]);
        int typePos = 0;
        if (type == 2) typePos = 2;
        if (index == 1) {
            if (position == 0 && bSelected) {
                v.setSelected(true);
                bSelected = false;
            }
        } else if (index == 2) {
            if (position == typePos && bSelectedList) {
                v.setSelected(true);
                bSelectedList = false;
            }
        } else if (index == 3) {
            if (position == typePos && bSelectedTop) {
                v.setSelected(true);
                bSelectedTop = false;
            }
        }

        return v;
    }

    /**
     * 设置商品 搭配 晒单Tab
     */
    private void setListTab() {
        for (int i = 0; i < mTitlesList.length; i++) {
            TabLayout.Tab tab = tabLayoutList.newTab();
            tabLayoutList.addTab(tab);
            tab.setCustomView(getTabView(i, mTitlesList, 2));

            TabLayout.Tab tab2 = tabLayoutTop.newTab();
            tabLayoutTop.addTab(tab2);
            tab2.setCustomView(getTabView(i, mTitlesList, 3));

            if (type == 2 && i == 2) {
                tab.select();
                tab2.select();
            }
        }

        tabLayoutList.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isScrollTab = true;
                tabPosition = tab.getPosition();
                if (!isTopTab) {
                    loadList(false);
                }
                tabLayoutTop.getTabAt(tabPosition).select();
                isScrollTab = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutTop.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isTopTab = true;
                tabPosition = tab.getPosition();
                if (!isScrollTab) {
                    loadList(true);
                }
                tabLayoutList.getTabAt(tabPosition).select();
                isTopTab = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void loadList(boolean isTopTab) {
        if (tabPosition == 0) {
            loadBrandGoodsData(mInfos, false, isTopTab);

        } else if (tabPosition == 1) {
            loadMatchData(isTopTab);

        } else if (tabPosition == 2) {
            getTagData(isTopTab);
        }
    }

    /*==============================商品=============================*/

    /**
     * 初始化筛选排序条件集合
     */
    private void initPostData() {
        post = new ParaObject();
        mInfos = new MallLimitOptionInfo();
        //筛选
        ArrayList<SortInfo> genderList = new ArrayList<>();//人群
        ArrayList<SortInfo> categoryList = new ArrayList<>();//品类
        ArrayList<SortInfo> priceList = new ArrayList<>();//价格
        ArrayList<SortInfo> colorList = new ArrayList<>();//颜色

        //排序
        ArrayList<SortInfo> sortList = new ArrayList<>();//排序

        SortInfo gInfo = new SortInfo();
        gInfo.setCheck(true);
        gInfo.setName(ALL);
        gInfo.setKey("");
        genderList.add(gInfo);
        mInfos.setGenders(genderList);

        SortInfo cInfo = new SortInfo();
        cInfo.setCheck(true);
        cInfo.setName(ALL);
        categoryList.add(cInfo);
        mInfos.setCategories(categoryList);

        SortInfo pInfo = new SortInfo();
        pInfo.setCheck(true);
        pInfo.setName(ALL);
        priceList.add(pInfo);
        mInfos.setPrice_ranges(priceList);

        SortInfo soInfo = new SortInfo();
        soInfo.setCheck(true);
        soInfo.setName("默认排序");
        sortList.add(soInfo);
        mInfos.setSorts(sortList);

        SortInfo coInfo = new SortInfo();
        coInfo.setCheck(true);
        coInfo.setName(ALL);
        colorList.add(coInfo);
        mInfos.setColor(colorList);
    }

    private void loadBrandGoodsData(MallLimitOptionInfo data, boolean isInit, boolean bTopTab) {
        nOffset = 0;
        isBottom = false;

        brand_ids = "";
        genders = "";
        category_ids = "";
        sort_ids = "";
        price_ranges_id = "";
        color = "";
        size = "";

        if (!isInit) {
            if (data == null) return;
            mInfos = data;
            checkOption();
        }

        post.setId(brand_id);
        post.setOffset(nOffset);
        post.setLimit(LIMIT_FORTY);
        post.setBrand_ids(brand_ids);
        post.setCategory_ids(category_ids);
        post.setGenders(genders);
        post.setPrice_ranges_id(price_ranges_id);
        post.setSort_id(sort_ids);
        post.setSize(size);
        post.setColor(color);
        post.setCount(0);

        HashMap<String, String> maps = getCheckedOptionMap(post);
        if (isInit) {
            getGoodsData(maps);
        } else {
            getGoodsDataTab(maps, bTopTab);
        }
    }

    private void loadMoreBrandGoodsData() {
        nOffset += LIMIT_FORTY;
        post.setOffset(nOffset);
        post.setCount(0);

        HashMap<String, String> maps = getCheckedOptionMap(post);

        new RetrofitRequest<ShopGoodsBrandGoodsObject>(ApiRequest.getApiShiji().getShopGoodsBrandGoods(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShopGoodsBrandGoodsObject item = (ShopGoodsBrandGoodsObject) msg.obj;
                            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                                if(item.getTopic_list() != null && item.getTopic_list().size() > 0){
                                    ArrayList<NewGoodsItem> list = new ArrayList<NewGoodsItem>();
                                    list = item.getList();
                                    list.addAll(1, item.getTopic_list());
                                    mAdapter.getmGoodsLists().addAll(list);
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    mAdapter.getmGoodsLists().addAll(item.getList());
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                isBottom = true;
                            }
                        }
                    }
                });
    }

    /**
     * 检查选择的筛选项
     */
    private void checkOption() {
        if (mInfos.getGenders() != null) {
            for (int i = 0; i < mInfos.getGenders().size(); i++) {
                SortInfo gInfo = mInfos.getGenders().get(i);
                if (gInfo.isCheck()) {
                    genders = gInfo.getKey();
                }
            }
        }

        if (mInfos.getCategories() != null) {
            for (int i = 0; i < mInfos.getCategories().size(); i++) {
                SortInfo cInfo = mInfos.getCategories().get(i);
                if (cInfo.isCheck()) {
                    category_ids = cInfo.getId() + "";
                    if (mInfos.getSize() != null) {
                        for (int j = 0; j < mInfos.getSize().size(); j++) {
                            if (cInfo.getProduct_type_id() == mInfos.getSize().get(j).getProduct_type_id()) {
                                ArrayList<SortInfo> sizeList = mInfos.getSize().get(j).getSizes();
                                for (int n = 0; n < sizeList.size(); n++) {
                                    if (sizeList.get(n).isCheck()) {
                                        size = sizeList.get(n).getId() + "";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (mInfos.getPrice_ranges() != null) {
            for (int i = 0; i < mInfos.getPrice_ranges().size(); i++) {
                SortInfo pInfo = mInfos.getPrice_ranges().get(i);
                if (pInfo.isCheck()) {
                    price_ranges_id = "" + pInfo.getId();
                }
            }
        }

        if (mInfos.getSorts() != null) {
            for (int i = 0; i < mInfos.getSorts().size(); i++) {
                SortInfo sInfo = mInfos.getSorts().get(i);
                if (sInfo.isCheck()) {
                    sort_ids = "" + sInfo.getId();
                }
            }
        }

        if (mInfos.getColor() != null) {
            for (int i = 0; i < mInfos.getColor().size(); i++) {
                SortInfo coInfo = mInfos.getColor().get(i);
                if (coInfo.isCheck()) {
                    color = "" + coInfo.getName();
                }
            }
        }
    }

    /**
     * 获取选择的商品筛选条件的集合
     */
    private HashMap<String, String> getCheckedOptionMap(ParaObject post) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", String.valueOf(post.getId()));
        maps.put("limit", String.valueOf(post.getLimit()));
        maps.put("offset", String.valueOf(post.getOffset()));
        maps.put("brand_ids", post.getBrand_ids());
        maps.put("genders", post.getGenders());
        maps.put("category_ids", post.getCategory_ids());
        maps.put("sort_id", post.getSort_id());
        maps.put("count", String.valueOf((post.getCount())));
        maps.put("price_ranges_id", post.getPrice_ranges_id());
        maps.put("size", post.getSize());
        maps.put("color", post.getColor());
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        return maps;
    }

    private void getGoodsData(HashMap<String, String> maps) {
        showPreDialog("正在加载中");
        new RetrofitRequest<ShopGoodsBrandGoodsObject>(ApiRequest.getApiShiji().getShopGoodsBrandGoods(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShopGoodsBrandGoodsObject item = (ShopGoodsBrandGoodsObject) msg.obj;
                            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                                if(item.getTopic_list() != null && item.getTopic_list().size() > 0){
                                    ArrayList<NewGoodsItem> list = new ArrayList<NewGoodsItem>();
                                    list = item.getList();
                                    list.addAll(1, item.getTopic_list());
                                    mAdapter.setmGoodsLists(list);
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    mAdapter.setmGoodsLists(item.getList());
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                isBottom = true;
                                showNullView(true);
                            }
                        }
                        hidePreDialog();
                    }
                });
    }

    private void getGoodsDataTab(HashMap<String, String> maps, final boolean bTopTab) {
        showPreDialog("正在加载中");
        new RetrofitRequest<ShopGoodsBrandGoodsObject>(ApiRequest.getApiShiji().getShopGoodsBrandGoods(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShopGoodsBrandGoodsObject item = (ShopGoodsBrandGoodsObject) msg.obj;
                            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                                showNullView(false);
                                if(item.getTopic_list() != null && item.getTopic_list().size() > 0){
                                    ArrayList<NewGoodsItem> list = new ArrayList<NewGoodsItem>();
                                    list = item.getList();
                                    list.addAll(1, item.getTopic_list());
                                    mAdapter.setmGoodsLists(list);
                                }else {
                                    mAdapter.setmGoodsLists(item.getList());
                                }
                            } else {
                                showNullView(true);
                                mAdapter.setmGoodsLists(null);
                                isBottom = true;
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        if (bTopTab) {
                            staggeredGridLayoutManager.scrollToPositionWithOffset(3, -2);
                        }
                        llScrollFilter.setVisibility(View.VISIBLE);
                        hidePreDialog();
                    }
                });
    }

    /**
     * 获取某一品牌下过滤选项
     */
    private void getBrandOption() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", String.valueOf(brand_id));
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getShopGoodsBrandOption(maps)).
                handRequest(new MsgCallBack() {
                                @Override
                                public void onResult(HttpMessage msg) {
                                    if (msg.isSuccess()) {
                                        MallLimitOptionObject object = (MallLimitOptionObject) msg.obj;
                                        if (object == null || object.getList() == null) {
                                            return;
                                        }

                                        //将获取到的品类按一级分类分组
                                        ArrayList<SortInfo> caList = object.getList().getCategories();

                                        HashMap<String, ArrayList<SortInfo>> maps = new HashMap<>();

                                        for (int i = 0; i < caList.size(); i++) {
                                            String productTypeId = caList.get(i).getProduct_type_id() + "";
                                            if (maps.get(productTypeId) == null) {
                                                ArrayList<SortInfo> productTypeIds = new ArrayList<>();
                                                productTypeIds.add(caList.get(i));
                                                maps.put(productTypeId, productTypeIds);
                                            } else {
                                                ArrayList<SortInfo> productTypeIds = maps.get(productTypeId);
                                                productTypeIds.add(caList.get(i));
                                                maps.put(productTypeId, productTypeIds);
                                            }
                                        }


                                        Iterator iter = maps.entrySet().iterator();
                                        while (iter.hasNext()) {
                                            Map.Entry entry = (Map.Entry) iter.next();
                                            ArrayList<SortInfo> productCategories = (ArrayList<SortInfo>) entry.getValue();
                                            mInfos.getCategories().addAll(productCategories);
                                        }

                                        //获取尺码
                                        if (object.getList().getSize() != null) {
                                            for (int i = 0; i < object.getList().getSize().size(); i++) {
                                                SortInfo sortInfo = new SortInfo();
                                                sortInfo.setName(ALL);
                                                sortInfo.setCheck(true);
                                                object.getList().getSize().get(i).getSizes().add(0, sortInfo);
                                            }
                                        }
                                        mInfos.setSize(object.getList().getSize());

                                        mInfos.getGenders().addAll(object.getList().getGenders());
                                        mInfos.getPrice_ranges().addAll(object.getList().getPrice_ranges());
                                        mInfos.getColor().addAll(object.getList().getColor());
                                        mInfos.getSorts().addAll(object.getList().getSorts());
                                    }
                                }
                            }
                );

    }

    /**
     * 弹出左侧排序
     */
    private void showOrderPopwindow() {
        tvScrollOrder.setSelected(true);
        tvTopOrder.setSelected(true);
        llytOrder = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.fragment_goods_list_order, null);
        rycvOrder = (RecyclerView) llytOrder.findViewById(R.id.rycv_goods_list_order);
        rycvOrder.setItemAnimator(new DefaultItemAnimator());
        rycvOrder.setLayoutManager(new LinearLayoutManager(this));
        goodsListOrderAdapter = new GoodsListOrderAdapter(this, mInfos);
        rycvOrder.setAdapter(goodsListOrderAdapter);

        int width = SimpleUtils.getScreenWidth(this) / 2;
        int heigh = SimpleUtils.getScreenHeight(this) - SimpleUtils.dp2px(this, 100);
        orderPopupWindow = new PopupWindow(llytOrder, width, heigh);
        orderPopupWindow.setFocusable(true);
        orderPopupWindow.setOutsideTouchable(true);
        orderPopupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        orderPopupWindow.setAnimationStyle(R.style.order_popwindow_anim_style);

        orderPopupWindow.showAsDropDown(llTopFilter, 0, 2);

        goodsListOrderAdapter.setOnItemClickListener(new GoodsListOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MallLimitOptionInfo mInfos) {
                loadBrandGoodsData(mInfos, false, isTop);
                orderPopupWindow.dismiss();
            }
        });

        orderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvScrollOrder.setSelected(false);
                tvTopOrder.setSelected(false);
            }
        });

    }

    /**
     * 跳转至筛选页面
     */
    private void goToFilter() {
        Intent intentGoFilter = new Intent(this, GoodsListFilterActivity.class);
        intentGoFilter.putExtra("data", new Gson().toJson(mInfos, MallLimitOptionInfo.class));
        intentGoFilter.putExtra("source", 1004);
        intentGoFilter.putExtra("showBrand", false);
        startActivityForResult(intentGoFilter, REQUEST_FILTER);
        this.overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_in_out_fixed);
    }

    /*==============================搭配=============================*/

    /**
     * 获取品牌搭配列表
     */
    private void loadMatchData(final boolean bTopTab) {
        nOffset = 0;
        isBottom = false;

        showPreDialog("正在加载中");

        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(
                MapRequest.setTagId(tagId, nOffset, LIMIT_TEN, 2))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    TagProduceObject item = (TagProduceObject) msg.obj;
                    if (item != null && item.list != null && !item.list.isEmpty()) {
                        showNullView(false);
                        mAdapter.setmMatchLists(item.list);
                    } else {
                        showNullView(true);
                        mAdapter.setmMatchLists(null);
                        isBottom = true;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (bTopTab) {
                    staggeredGridLayoutManager.scrollToPositionWithOffset(3, -2);
                }
                llScrollFilter.setVisibility(View.GONE);
                llTopFilter.setVisibility(View.INVISIBLE);
                hidePreDialog();
            }
        });
    }

    /**
     * 加载更多搭配列表
     */
    private void loadMoreMatchData() {
        nOffset += LIMIT_TEN;

        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(
                MapRequest.setTagId(tagId, nOffset, LIMIT_TEN, 2))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    TagProduceObject item = (TagProduceObject) msg.obj;
                    if (item != null && item.list != null && !item.list.isEmpty()) {
                        mAdapter.getmMatchLists().addAll(item.list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }
            }
        });
    }

    /*==============================晒单=============================*/

    private void initTagData() {
        nOffset = 0;
        isBottom = false;

        showPreDialog("正在加载中");

        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(
                MapRequest.setTagId(tagId, nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    TagProduceObject item = (TagProduceObject) msg.obj;
                    if (item != null && item.list != null && !item.list.isEmpty()) {
                        mAdapter.setmTagLists(item.list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                        showNullView(true);
                    }
                }
                hidePreDialog();
            }
        });
    }

    /**
     * 获取晒单数据
     */
    private void getTagData(final boolean bTopTab) {
        nOffset = 0;
        isBottom = false;

        showPreDialog("正在加载中");

        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(
                MapRequest.setTagId(tagId, nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    TagProduceObject item = (TagProduceObject) msg.obj;
                    if (item != null && item.list != null && !item.list.isEmpty()) {
                        showNullView(false);
                        mAdapter.setmTagLists(item.list);
                    } else {
                        showNullView(true);
                        mAdapter.setmTagLists(null);
                        isBottom = true;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (bTopTab) {
                    staggeredGridLayoutManager.scrollToPositionWithOffset(3, -2);
                }
                llScrollFilter.setVisibility(View.GONE);
                llTopFilter.setVisibility(View.INVISIBLE);
                hidePreDialog();
            }
        });
    }

    /**
     * 加载更多晒单数据
     */
    private void loadMoreTagData() {
        nOffset += LIMIT_TEN;

        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(
                MapRequest.setTagId(tagId, nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    TagProduceObject item = (TagProduceObject) msg.obj;
                    if (item != null && item.list != null && !item.list.isEmpty()) {
                        mAdapter.getmTagLists().addAll(item.list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER) {
                String infoData = data.getStringExtra("infoData");
                loadBrandGoodsData(new Gson().fromJson(infoData, MallLimitOptionInfo.class), false, isTop);
            } else if (requestCode == LOGIN_REQUSET) {
                getBrandDesc();
                Util.getNewUserPullLayer(this, data);
            } else if (requestCode == REQUEST_MATCH || requestCode == REQUEST_WORK) {
                ChangeItem(data);
            }
        }
    }

    /**
     * 同步删除自己的搭配或晒单
     *
     * @param intent
     */
    private void ChangeItem(Intent intent) {
        if (intent != null) {
            boolean bDelete = intent.getBooleanExtra("delete", false);
            int position = intent.getIntExtra("position", -1);
            if (bDelete) {
                if (tabPosition == 1) {
                    mAdapter.getmMatchLists().remove(position);
                } else if (tabPosition == 2) {
                    mAdapter.getmTagLists().remove(position);
                }
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }
        }
    }

    private void showNullView(boolean isShow) {
        if (tvNullLayoutParams == null) return;
        if (isShow) {
            tvNullLayoutParams.height = SimpleUtils.getScreenHeight(this) - rlScrollTablayout.getHeight() - rlSearch.getHeight();
            if (tabPosition == 0) {
                tvNull.setText("暂无筛选结果");
                tvNull.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.mipmap.zanwusousuojieguo, null), null, null);
            } else if (tabPosition == 1) {
                tvNull.setText("暂无搭配");
                tvNull.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.mipmap.zanwudapei, null), null, null);
            } else if (tabPosition == 2) {
                tvNull.setText("暂无晒单");
                tvNull.setCompoundDrawablesWithIntrinsicBounds(null, ResourcesCompat.getDrawable(getResources(), R.mipmap.zanwugouwubiji, null), null, null);
            }
        } else {
            tvNullLayoutParams.height = 0;
        }
        llNull.setLayoutParams(tvNullLayoutParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
    }
}
