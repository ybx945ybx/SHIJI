package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.GoodsListOrderAdapter;
import cn.yiya.shiji.adapter.MallGoodsAdapter;
import cn.yiya.shiji.adapter.NewSearchAdapter;
import cn.yiya.shiji.adapter.RecordAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.MallLimitOptionObject;
import cn.yiya.shiji.entity.ParaObject;
import cn.yiya.shiji.entity.SearchHotWords;
import cn.yiya.shiji.entity.SearchRecordObject;
import cn.yiya.shiji.entity.SortInfo;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.entity.search.NewSearchEntity;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;

/**
 * Created by Amy on 2016/10/14.
 */

public class NewGoodsListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int siteId;             //商城Id
    private int source;        //1000 搜索  1001 首页  1002 二级分类
    private String categoryId = "";
    private boolean bShow;                                  // 是否显示打折
    private String title;
    private String typeName;

    private ImageView toolbarLeft;
    private TextView tvTitle;
    private RelativeLayout rlSearch;
    private RelativeLayout rlRight;
    private TextView tvCarcount;

    private LinearLayout llGoods;
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
    private ParaObject post = new ParaObject();
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

    //搜索
    private RelativeLayout toolbarSearch;
    private EditText mAutoCompleteTextView;
    private ImageView ivSearchCancel;
    private String word;

    private LinearLayout llSearch;
    private ScrollView mScrollView;
    private TagFlowLayout tflSearch;
    private LinearLayout llWords;
    private AllListView alvSearchRecord;
    private TextView tvNullRecord;
    private Button btnCleanRecord;
    private RecyclerView rvSearch;
    private NewSearchAdapter mSearchAdapter;

    private SearchRecordObject object = new SearchRecordObject();
    private List<String> listrecord = new ArrayList<>();
    private SearchHotWords hotWords;
    private String[] words;
    private TagAdapter tagAdapter;
    private RecordAdapter mRecordAdapter;
    private boolean isRecommend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goods_list);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            source = intent.getIntExtra("source", 0);
            siteId = intent.getIntExtra("id", 0);  //1001 1002
            bShow = getIntent().getBooleanExtra("show", false);
            categoryId = intent.getStringExtra("categoryid");   //1002
            word = intent.getStringExtra("word"); //1000
            title = intent.getStringExtra("title"); //1001 1002
            typeName = intent.getStringExtra("typeName"); //1002
            isRecommend = intent.getBooleanExtra("isRecommend", false);
        }
    }

    @Override
    protected void initViews() {
        toolbarLeft = (ImageView) findViewById(R.id.toolbar_left);
        tvTitle = (TextView) findViewById(R.id.toolbar_middle_txt);
        rlSearch = (RelativeLayout) findViewById(R.id.toolbar_middle_search_layout);
        rlRight = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCarcount = (TextView) findViewById(R.id.toolbar_right_count);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        llGoods = (LinearLayout) findViewById(R.id.ll_goods);
        llScrollFilter = (LinearLayout) findViewById(R.id.ll_order_filter);
        tvScrollOrder = (TextView) findViewById(R.id.tv_order);
        tvScrollFilter = (TextView) findViewById(R.id.tv_filter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new MallGoodsAdapter(this, new ArrayList<NewGoodsItem>(), bShow, isRecommend);
        recyclerView.setAdapter(mAdapter);

        //搜索
        toolbarSearch = (RelativeLayout) findViewById(R.id.toolbar_middle_search_layout);
        mAutoCompleteTextView = (EditText) findViewById(R.id.toolbar_middle_search_edittext);
        ivSearchCancel = (ImageView) findViewById(R.id.toolbar_middle_search_img);

        if (!TextUtils.isEmpty(word)) {
            mAutoCompleteTextView.setText(word);
        }

        llSearch = (LinearLayout) findViewById(R.id.ll_search);
        mScrollView = (ScrollView) findViewById(R.id.sv_scroll);
        tflSearch = (TagFlowLayout) findViewById(R.id.tfl_search_tips);
        llWords = (LinearLayout) findViewById(R.id.ll_words);
        alvSearchRecord = (AllListView) findViewById(R.id.lv_record);
        tvNullRecord = (TextView) findViewById(R.id.tv_record);
        btnCleanRecord = (Button) findViewById(R.id.btn_clean_record);
        rvSearch = (RecyclerView) findViewById(R.id.rv_search);
        rvSearch.setItemAnimator(new DefaultItemAnimator());
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        mSearchAdapter = new NewSearchAdapter(this);
        rvSearch.setAdapter(mSearchAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwusousuojieguo, "暂无结果", this);
    }

    @Override
    protected void initEvents() {
        toolbarLeft.setOnClickListener(this);
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

            }

//            @Override
//            public void OnRecommendClick(NewGoodsItem info) {
//
//            }
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


        //搜索
        mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSuccessView(null);
                llGoods.setVisibility(View.GONE);
                llSearch.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(s.toString())) {
                    ivSearchCancel.setVisibility(View.GONE);
                    llWords.setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                } else {
                    ivSearchCancel.setVisibility(View.VISIBLE);
                    llWords.setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                }

                getSearchData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {                                                     // 获得焦点动作
                    llGoods.setVisibility(View.GONE);
                    llSearch.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(mAutoCompleteTextView.getText().toString())) {
                        llWords.setVisibility(View.VISIBLE);
                        mScrollView.setVisibility(View.GONE);                                           // 搜索历史页面隐藏
                        mAutoCompleteTextView.setSelection(mAutoCompleteTextView.getText().length());
                        getSearchData(mAutoCompleteTextView.getText().toString());
                    } else {
                        llWords.setVisibility(View.GONE);
                        mScrollView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {                                       // 点击回车键发送信息
//                    ArrayList<String> mList = new ArrayList<>();
//                    if ((new Gson().fromJson(sharedPreferences.getString("record", ""), SearchRecordObject.class)) != null) {
//                        mList = (new Gson().fromJson(sharedPreferences.getString("record", ""), SearchRecordObject.class)).getList();
//                        for (int i = 0; i < mList.size(); i++) {
//                            if (mAutoCompleteTextView.getText().toString().equals(mList.get(i).toString())) {
//                                mList.remove(i);
//                            }
//                        }
//                        if (!mAutoCompleteTextView.getText().toString().equals("")) {
//                            mList.add(mAutoCompleteTextView.getText().toString());
//                            object = new SearchRecordObject();
//                            object.setList(mList);
//                            String json = new Gson().toJson(object);
//                            Util.saveLocalSearchRecord(mHandle, sharedPreferences, json);
//                        }
//                    } else {
//                        if (!mAutoCompleteTextView.getText().toString().equals("")) {
//                            mList.add(mAutoCompleteTextView.getText().toString());
//                            object = new SearchRecordObject();
//                            object.setList(mList);
//                            String json = new Gson().toJson(object);
//                            Util.saveLocalSearchRecord(mHandle, sharedPreferences, json);
//                        }
//                    }

                    if (!TextUtils.isEmpty(mAutoCompleteTextView.getText().toString())) {
                        word = mAutoCompleteTextView.getText().toString();
                        addRecord(word);
                        updateList();

                    } else {
                        showTips("搜索不能为空");
                    }
                    return true;
                }
                return false;
            }
        });

        mSearchAdapter.setOnItemClickListen(new NewSearchAdapter.OnItemClickListener() {
            @Override
            public void OnActItemClick(NewSearchEntity.ShopActListEntity shopActListEntity) {
                switch (shopActListEntity.getType()) {
                    case 1:           // 活动
                        Intent intent = new Intent(NewGoodsListActivity.this, HomeIssueActivity.class);
                        intent.putExtra("activityId", shopActListEntity.getId() + "");
                        intent.putExtra("menuId", 7);
                        if(isRecommend){
                            intent.putExtra("isRecommend", true);
                        }
                        startActivity(intent);
                        break;
                    case 2:           // 专题
                        Intent intent1 = new Intent(NewGoodsListActivity.this, NewLocalWebActivity.class);
                        HtmlThematicInfo info = new HtmlThematicInfo();
                        info.setItemId(shopActListEntity.getId() + "");
                        info.setTypeId("3");
                        String dataJson = new Gson().toJson(info);
                        intent1.putExtra("data", dataJson);
                        intent1.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");
                        intent1.putExtra("type", shopActListEntity.getType());
                        startActivity(intent1);
                        break;
                    case 3:           // h5分享
                        Intent intent2 = new Intent(NewGoodsListActivity.this, NewLocalWebActivity.class);
                        intent2.putExtra("url", shopActListEntity.getUrl());
                        intent2.putExtra("type", shopActListEntity.getType());
                        intent2.putExtra("title", shopActListEntity.getTitle());
                        intent2.putExtra("data", new Gson().toJson(shopActListEntity));
                        startActivity(intent2);
                        break;
                }

            }

            @Override
            public void OnBrandItemClick(NewSearchEntity.BrandListEntity brandListEntity) {
                addRecord(brandListEntity.getName());
                Intent intent = new Intent(NewGoodsListActivity.this, NewSingleBrandActivity.class);
                intent.putExtra("brand_id", brandListEntity.getId());
                if(isRecommend){
                    intent.putExtra("isRecommend", true);
                }
                startActivity(intent);
            }

            @Override
            public void OnSiteItemClick(NewSearchEntity.SiteListEntity siteListEntity) {
                addRecord(siteListEntity.getName());
                Intent intent = new Intent(NewGoodsListActivity.this, NewMallGoodsActivity.class);
                intent.putExtra("id", siteListEntity.getId());
                if(isRecommend){
                    intent.putExtra("isRecommend", true);
                }
                startActivity(intent);

            }

            @Override
            public void OnCategroyItemClick(NewSearchEntity.GoodsTypeListEntity goodsTypeListEntity) {
                addRecord(goodsTypeListEntity.getName());
                Intent intent = new Intent(NewGoodsListActivity.this, NewGoodsListActivity.class);
                intent.putExtra("id", 0);
                intent.putExtra("categoryid", String.valueOf(goodsTypeListEntity.getId()));
                intent.putExtra("title", goodsTypeListEntity.getName());
                intent.putExtra("source", 1002);
                if(isRecommend){
                    intent.putExtra("isRecommend", true);
                }
                startActivity(intent);

            }
        });

        initSearchEvent();
    }

    @Override
    protected void init() {
        initPostData();
        if (source == 1000) {
            toolbarSearch.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
            post.setWord(word);
        }
        loadGoodsListData(null, true);
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
                loadGoodsListData(mInfos, false);
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
        if (source == 1000) {
            maps.put("word", word);
        } else {
            maps.put("id", String.valueOf(siteId));
            if (source == 1002) {
                maps.put("category_ids", categoryId);
            }
        }
        maps.put("brand", String.valueOf(0));
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        switch (source) {
            case 1000:
                new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getSearchOption(maps)).handRequest(
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleOptionData(msg);
                            }
                        }
                );
                break;
            case 1001:
                new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getMenuCategoryOption(maps)).handRequest(
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleOptionData(msg);
                            }
                        }
                );
                break;
            case 1002:
                new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getCategoryOption(maps)).handRequest(
                        new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                handleOptionData(msg);
                            }
                        }
                );
                break;
        }
    }

    /**
     * 处理返回的筛选条件数据
     *
     * @param msg
     */
    private void handleOptionData(HttpMessage msg) {
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
                //当从三级分类进入时将全部的categoryid设为所选类别的categoryid
                if (source == 1002 && !TextUtils.isEmpty(categoryId)) {
                    mInfos.getCategories().clear();
                    ArrayList<SortInfo> categoryList = new ArrayList<>();
                    SortInfo cInfo = Util.getDeepClone(productCategories.get(0));
                    cInfo.setCheck(true);
                    cInfo.setName(ALL);
                    categoryList.add(cInfo);
                    mInfos.setCategories(categoryList);
                }
                mInfos.getCategories().addAll(productCategories);

                if(source==1002 && !TextUtils.isEmpty(title)){
                    boolean isMatch=false;
                    for(int i=0;i<mInfos.getCategories().size();i++){
                        if(title.equals(mInfos.getCategories().get(i).getName())){
                            isMatch=true;
                            mInfos.getCategories().get(i).setCheck(true);
                            break;
                        }
                    }
                    if(isMatch){
                        mInfos.getCategories().get(0).setCheck(false);
                    }
                }
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

                if(source==1002 && !TextUtils.isEmpty(typeName)){
                    boolean isMatch=false;
                    for(int i=0;i<mInfos.getGenders().size();i++){
                        if(typeName.equals(mInfos.getGenders().get(i).getName())){
                            isMatch=true;
                            mInfos.getGenders().get(i).setCheck(true);
                            break;
                        }
                    }
                    if(isMatch){
                        mInfos.getGenders().get(0).setCheck(false);
                    }
                }
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

    /**
     * 根据选择的商品筛选条件获取商品数据
     *
     * @param data
     */
    private void loadGoodsListData(MallLimitOptionInfo data, boolean isInit) {
        nOffset = 0;
        isBottom = false;

        brand_ids = "";
        genders = "";
        category_ids = (categoryId == null ? "" : categoryId);
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
        if (!TextUtils.isEmpty(post.getWord())) {
            maps.put("word", post.getWord());
        }
        if(isRecommend){
            maps.put("isShopkeeper", String.valueOf(1));
        }
        return maps;
    }

    private void getMallGoodsData(HashMap<String, String> maps) {
        showPreDialog("正在加载中");
        if (source == 1001) {
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getMallHomeList(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleGoodsdata(msg);
                            hidePreDialog();
                        }
                    });
        } else if (source == 1002) {
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getCategoryList(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleGoodsdata(msg);
                            hidePreDialog();
                        }
                    });
        } else if (source == 1000) {
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getSearchGoods(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleGoodsdata(msg);
                            hidePreDialog();
                        }
                    });
        }
    }

    /**
     * 处理返回得到的商品列表数据
     *
     * @param msg
     */
    private void handleGoodsdata(HttpMessage msg) {
        if (msg.isSuccess()) {
            llScrollFilter.setVisibility(View.VISIBLE);
            MallGoodsDetailObject item = (MallGoodsDetailObject) msg.obj;
            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                setSuccessView(recyclerView);
            } else {
                setNullView(recyclerView);
                isBottom = true;
                if (source == 1000) {
                    llScrollFilter.setVisibility(View.GONE);
                }
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
            if (!NetUtil.IsInNetwork(NewGoodsListActivity.this)) {
                setOffNetView(recyclerView);
                llScrollFilter.setVisibility(View.GONE);
            }
        }
    }

    private void handleMoreGoodsData(HttpMessage msg) {
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

    /**
     * 加载更多
     */
    private void loadMoreData() {
        nOffset += LIMIT_FORTY;
        post.setOffset(nOffset);
        post.setCount(0);

        HashMap<String, String> maps = getCheckedOptionMap(post);

        if (source == 1001) {
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getMallHomeList(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleMoreGoodsData(msg);
                        }
                    });
        } else if (source == 1002) {
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getCategoryList(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleMoreGoodsData(msg);
                        }
                    });
        } else if (source == 1000) {
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getSearchGoods(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            handleMoreGoodsData(msg);
                        }
                    });
        }
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
                loadGoodsListData(mInfos, false);
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
        intentGoFilter.putExtra("siteId", siteId);
        intentGoFilter.putExtra("source", source);
        intentGoFilter.putExtra("word", word);
        intentGoFilter.putExtra("categoryid", categoryId);
        startActivityForResult(intentGoFilter, REQUEST_FILTER);
        overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_in_out_fixed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FILTER) {
                String infoData = SharedPreUtil.getString(this, Configration.SHAREDPREFERENCE, "filter_option_result", "");
                loadGoodsListData(new Gson().fromJson(infoData, MallLimitOptionInfo.class), false);
            }
        }
    }


    /**
     * 获取最新的搜索数据
     *
     * @param s
     */
    private void getSearchData(String s) {
        new RetrofitRequest<NewSearchEntity>(ApiRequest.getApiShiji().getSearchAuxiliary(s)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    NewSearchEntity entity = (NewSearchEntity) msg.obj;
                    if (entity != null) {
                        mSearchAdapter.setSearchEntity(entity);
                        mSearchAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 获取热门搜索列表
     */
    private void getSearchHotWords() {
        new RetrofitRequest<SearchHotWords>(ApiRequest.getApiShiji().getHotSearch()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hotWords = (SearchHotWords) msg.obj;
                    words = hotWords.getWords();
                }
                if (words != null) {
                    tagAdapter = new TagAdapter(words) {                                                // 热门搜索数据
                        @Override
                        public View getView(FlowLayout parent, int position, Object o) {
                            TextView tvPrices = (TextView) LayoutInflater.from(NewGoodsListActivity.this).inflate(R.layout.search_words_item, tflSearch, false);
                            tvPrices.setText(words[position]);
                            return tvPrices;
                        }
                    };
                    tflSearch.setAdapter(tagAdapter);
                }

            }
        });
    }

    /**
     * 获取搜索记录
     */
    private void getRecord() {
        String record = SharedPreUtil.getString(this, Configration.SHAREDPREFERENCE, "record", "");
        object = new Gson().fromJson(record, SearchRecordObject.class);
        if (object != null) {
            listrecord = object.getList();
            Collections.reverse(listrecord);
            if (listrecord.size() > 5) {
                listrecord = listrecord.subList(0, 5);
            }
            mRecordAdapter = new RecordAdapter(listrecord, this, 1);
            mRecordAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
                @Override
                public void onClickListener(String record) {
                    addRecord(record);
                    mAutoCompleteTextView.setText(record);
                    word = record;
                    updateList();
                }
            });
            alvSearchRecord.setAdapter(mRecordAdapter);                                     // 搜索记录
            tvNullRecord.setVisibility(View.GONE);                                          // 取消无记录提示
            btnCleanRecord.setVisibility(View.VISIBLE);                                      // 清除记录提示
        } else {
            tvNullRecord.setVisibility(View.VISIBLE);                                       // 显示无记录提示
            btnCleanRecord.setVisibility(View.GONE);                                         // 取消清除记录提示
        }
    }

    private void initSearchEvent() {
        tflSearch.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                addRecord(words[position]);
                mAutoCompleteTextView.setText(words[position]);
                word = words[position];
                updateList();
                Util.hintIMETool(NewGoodsListActivity.this, view);
                return false;
            }
        });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {                                             // 滑动界面时隐藏输入法
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        Util.hintIMETool(NewGoodsListActivity.this, v);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        btnCleanRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listrecord.clear();
                mRecordAdapter.notifyDataSetChanged();
                SharedPreUtil.putString(NewGoodsListActivity.this, Configration.SHAREDPREFERENCE, "record", "");
                btnCleanRecord.setVisibility(View.GONE);
                tvNullRecord.setVisibility(View.VISIBLE);
            }
        });

        ivSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutoCompleteTextView.setText("");
            }
        });
    }

    private void addRecord(String word) {
        if (TextUtils.isEmpty(word)) {
            showTips("搜索不能为空");
            return;
        }

        ArrayList<String> mList = new ArrayList<>();
        String record = SharedPreUtil.getString(this, Configration.SHAREDPREFERENCE, "record", "");
        SearchRecordObject searchRecordObject = (new Gson().fromJson(record, SearchRecordObject.class));
        if (searchRecordObject != null) {
            mList = searchRecordObject.getList();
            for (int i = 0; i < mList.size(); i++) {
                if (word.equals(mList.get(i).toString())) {
                    mList.remove(i);
                }
            }
        }
        mList.add(word);
        object = new SearchRecordObject();
        object.setList(mList);
        String json = new Gson().toJson(object);
        SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "record", json);
        mAutoCompleteTextView.clearFocus();
    }

    private void updateList() {
        post.setWord(word);
        initPostData();
        loadGoodsListData(null, true);
        getOptionData();
        llGoods.setVisibility(View.VISIBLE);
        llSearch.setVisibility(View.GONE);
        llWords.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
        getSearchHotWords();
        getRecord();
        mAutoCompleteTextView.clearFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "filter_option", "");
        SharedPreUtil.putString(this, Configration.SHAREDPREFERENCE, "filter_option_result", "");
    }
}
