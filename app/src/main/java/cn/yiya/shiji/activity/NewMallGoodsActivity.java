package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.GoodsListOrderAdapter;
import cn.yiya.shiji.adapter.MallGoodsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.MallLimitOptionObject;
import cn.yiya.shiji.entity.ParaObject;
import cn.yiya.shiji.entity.SortInfo;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * 商城
 * Created by Amy on 2016/10/13.
 */

public class NewMallGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int siteId; //商城Id
    private boolean bShow;                                  // 是否显示打折

    private ImageView toolbarLeft;
    private RelativeLayout rlSearch;
    private RelativeLayout rlRight;
    private TextView tvCarcount;

    private TextView tvSiteName;

    private LinearLayout llScrollFilter;
    private TextView tvScrollOrder;
    private TextView tvScrollFilter;

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private MallGoodsAdapter mAdapter;
    private int lastVisibleItemPosition;

    // 左侧排序popupwindow
    private PopupWindow orderPopupWindow;
    private LinearLayout llytOrder;
    private RecyclerView rycvOrder;
    private GoodsListOrderAdapter goodsListOrderAdapter;

    /*商品*/
    private ParaObject post;
    private MallLimitOptionInfo mInfos;
    private static final String ALL = "全部";

    private int nOffset;
    private boolean isBottom;
    private final int LIMIT_FORTY = 40;
    private static final int REQUEST_FILTER = 1001;

    //选择的排序筛选条件  isCheck
    private String brand_ids = "";
    private String category_ids = "";
    private String sort_ids = "";
    private String price_ranges_id = "";
    private String genders = "";
    private String color = "";
    private String size = "";

    private static final int REQUEST_LOGIN = 100;
    private boolean isRecommend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mall_goods);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            siteId = intent.getIntExtra("id", 0);
            bShow = getIntent().getBooleanExtra("show", false);
            isRecommend = intent.getBooleanExtra("isRecommend", false);
        }
    }

    @Override
    protected void initViews() {
        toolbarLeft = (ImageView) findViewById(R.id.toolbar_left);
        rlSearch = (RelativeLayout) findViewById(R.id.toolbar_middle_search_layout);
        rlRight = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCarcount = (TextView) findViewById(R.id.toolbar_right_count);

        tvSiteName = (TextView) findViewById(R.id.tv_brand);

        llScrollFilter = (LinearLayout) findViewById(R.id.ll_order_filter);
        tvScrollOrder = (TextView) findViewById(R.id.tv_order);
        tvScrollFilter = (TextView) findViewById(R.id.tv_filter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new MallGoodsAdapter(this, new ArrayList<NewGoodsItem>(), bShow, isRecommend);
        recyclerView.setAdapter(mAdapter);

        rlytDefaultNullView = (RelativeLayout) findViewById(R.id.rlyt_default_null_view);
        tvDefaultNull = (TextView) findViewById(R.id.tv_default_null);
        llytDefaultOffNet = (LinearLayout) findViewById(R.id.llyt_off_net);
        tvReload = (TextView) findViewById(R.id.tv_reload);
        initDefaultNullView(R.mipmap.zanwusousuojieguo, "暂无结果", this);
    }

    @Override
    protected void initEvents() {
        toolbarLeft.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
        rlRight.setOnClickListener(this);

        tvScrollOrder.setOnClickListener(this);
        tvScrollFilter.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new MallGoodsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final NewGoodsItem info) {
                gotoGoodsDetail(info.getId());
            }

            @Override
            public void gotoLogin() {
                Intent intent = new Intent(NewMallGoodsActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if ((visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 10) && !isBottom) {
                    loadMoreData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
        loadMallGoodsData(null, true);
        getOptionData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.toolbar_middle_search_layout:
                startActivity(new Intent(this, NewSearchActivity.class));
                break;
            case R.id.toolbar_right_layout:
                Intent intent = new Intent(this, NewShoppingCartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            //排序 筛选
            case R.id.tv_order:
                showOrderPopwindow();
                break;
            case R.id.tv_filter:
                goToFilter();
                break;
            case R.id.tv_reload:
                loadMallGoodsData(mInfos, false);
                getOptionData();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(this, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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

    /**
     * 获取某一网站商品过滤选项
     */
    private void getOptionData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("id", String.valueOf(siteId));
        maps.put("brand", String.valueOf(0));
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getSiteOption(maps)).handRequest(
                new MsgCallBack() {
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
                            if (object.getList().getSize() != null) {
                                mInfos.setSize(object.getList().getSize());
                            }
                            if (object.getList().getGenders() != null) {
                                mInfos.getGenders().addAll(object.getList().getGenders());
                            }
                            if (object.getList().getPrice_ranges() != null) {
                                mInfos.getPrice_ranges().addAll(object.getList().getPrice_ranges());
                            }
                            if (object.getList().getColor() != null) {
                                mInfos.getColor().addAll(object.getList().getColor());
                            }
                            if (object.getList().getSorts() != null) {
                                mInfos.getSorts().addAll(object.getList().getSorts());
                            }
                        }
                    }
                }
        );
    }

    /**
     * 根据选择的商品筛选条件获取商品数据
     *
     * @param data
     */
    private void loadMallGoodsData(MallLimitOptionInfo data, boolean isInit) {
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

        post.setId(siteId);
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
        getMallGoodsData(maps);
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

        if (mInfos.getBrands() != null) {
            for (int i = 0; i < mInfos.getBrands().size(); i++) {
                SortInfo bInfo = mInfos.getBrands().get(i);
                if (bInfo.isCheck()) {
                    if (brand_ids.equals("")) {
                        brand_ids = bInfo.getId() + "";
                    } else {
                        brand_ids += "," + bInfo.getId();
                    }
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
        return maps;
    }

    private void getMallGoodsData(HashMap<String, String> maps) {
        showPreDialog("正在加载中");
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getHotMallList(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            llScrollFilter.setVisibility(View.VISIBLE);
                            MallGoodsDetailObject item = (MallGoodsDetailObject) msg.obj;
                            if (item != null && !TextUtils.isEmpty(item.getSiteTag())) {
                                tvSiteName.setText(item.getSiteTag());
                            }
                            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                                setSuccessView(recyclerView);
                            } else {
                                setNullView(recyclerView);
                                isBottom = true;
                            }
                            if(item.getTopic_list() != null && item.getTopic_list().size() > 0){  // 有专题活动插入
                                ArrayList<NewGoodsItem> list = new ArrayList<NewGoodsItem>();
                                list = item.getList();
                                list.addAll(1, item.getTopic_list());
                                mAdapter.setmLists(list);
                                mAdapter.notifyDataSetChanged();
                            }else {
                                mAdapter.setmLists(item == null ? null : item.getList());
                                mAdapter.notifyDataSetChanged();
                            }
                            staggeredGridLayoutManager.scrollToPositionWithOffset(0, 0);
                        } else {
                            if (!NetUtil.IsInNetwork(NewMallGoodsActivity.this)) {
                                setOffNetView(recyclerView);
                                llScrollFilter.setVisibility(View.GONE);
                            }
                        }
                        hidePreDialog();
                    }
                });
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
        nOffset += LIMIT_FORTY;
        post.setOffset(nOffset);
        post.setCount(0);

        HashMap<String, String> maps = getCheckedOptionMap(post);

        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getHotMallList(maps))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            MallGoodsDetailObject item = (MallGoodsDetailObject) msg.obj;
                            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                                if(item.getTopic_list() != null && item.getTopic_list().size() > 0){
                                    ArrayList<NewGoodsItem> list = new ArrayList<NewGoodsItem>();
                                    list = item.getList();
                                    list.addAll(1, item.getTopic_list());
                                    mAdapter.getmLists().addAll(list);
                                    mAdapter.notifyDataSetChanged();
                                }else {
                                    mAdapter.getmLists().addAll(item.getList());
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
     * 弹出左侧排序
     */
    private void showOrderPopwindow() {
        tvScrollOrder.setSelected(true);
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

        orderPopupWindow.showAsDropDown(llScrollFilter, 0, 2);

        goodsListOrderAdapter.setOnItemClickListener(new GoodsListOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MallLimitOptionInfo mInfos) {
                loadMallGoodsData(mInfos, false);
                orderPopupWindow.dismiss();
            }
        });

        orderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvScrollOrder.setSelected(false);
            }
        });

    }

    /**
     * 跳转至筛选页面
     */
    private void goToFilter() {
        Intent intentGoFilter = new Intent(this, GoodsListFilterActivity.class);
        SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "filter_option", new Gson().toJson(mInfos));
        intentGoFilter.putExtra("source", 1003);
        intentGoFilter.putExtra("siteId", siteId);
        startActivityForResult(intentGoFilter, REQUEST_FILTER);
        this.overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_in_out_fixed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER) {
                String infoData = SharedPreUtil.getString(this, Configration.SHAREDPREFERENCE, "filter_option_result", "");
                loadMallGoodsData(new Gson().fromJson(infoData, MallLimitOptionInfo.class), false);
            } else if (requestCode == REQUEST_LOGIN) {
                loadMallGoodsData(mInfos,false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "filter_option", "");
        SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "filter_option_result", "");
    }
}
