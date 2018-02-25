package cn.yiya.shiji.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CollocationSelectedGoodsListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.MallLimitOptionInfo;
import cn.yiya.shiji.entity.MallLimitOptionObject;
import cn.yiya.shiji.entity.ParaObject;
import cn.yiya.shiji.entity.SortInfo;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.Util;

/**
 * Created by Tom on 2016/7/20.
 */
public class CollocationSelectedGoodsListActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivRight;

    private SwipeRefreshLayout srlytCollocationList;
    private RecyclerView rycvCollocationList;
    private CollocationSelectedGoodsListAdapter mAdapter;

    private int nOffset;
    private boolean isBottom;
    private int lastVisibleItemPosition;
    private int id;
    private String brand_ids = "";
    private String category_ids = "";
    private String sort_ids = "";
    private String price_ranges_id = "";
    private String genders = "";
    private String color = "";
    private String size = "";
    private String titleName;
    private String typeName;
    private ParaObject post = new ParaObject();
    private Handler mHandle;
    private Runnable myRunnable;
    private static final int REQUEST_FILTER = 111;
    private static final String ALL = "全部";
    private MallLimitOptionInfo mInfos = new MallLimitOptionInfo();
    private SharedPreferences sharedPreferences;
    private boolean isSimilar = false;//判断是否是找相似单品
    private String goodsId;          //商品编号


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collocation_selected_goods_list);
        mHandle = new Handler(getMainLooper());
        sharedPreferences = getSharedPreferences(Configration.SHAREDPREFERENCE, MODE_PRIVATE);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
            category_ids = intent.getStringExtra("category");
            post.setId(id);
            titleName = intent.getStringExtra("name");
            goodsId = intent.getStringExtra("goodsId");
            isSimilar = intent.getBooleanExtra("isSimilar", false);
            typeName= intent.getStringExtra("typeName");
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        ivRight = (ImageView) findViewById(R.id.title_right);
        ivRight.setImageResource(R.mipmap.shaixuan);
        if (isSimilar) {
            tvTitle.setText("相似单品");
            ivRight.setVisibility(View.GONE);
        } else {
            tvTitle.setText(titleName);
        }

        srlytCollocationList = (SwipeRefreshLayout) findViewById(R.id.srlyt_collocation_selected_goods_list);
        rycvCollocationList = (RecyclerView) findViewById(R.id.rycv_collocation_selected_goods_list);
        rycvCollocationList.setItemAnimator(new DefaultItemAnimator());
        rycvCollocationList.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new CollocationSelectedGoodsListAdapter(this,isSimilar);
        rycvCollocationList.setAdapter(mAdapter);

        rlytDefaultNullView = (RelativeLayout) findViewById(R.id.rlyt_default_null_view);
        tvDefaultNull = (TextView) findViewById(R.id.tv_default_null);
        llytDefaultOffNet = (LinearLayout) findViewById(R.id.llyt_off_net);
        tvReload = (TextView) findViewById(R.id.tv_reload);
        initDefaultNullView(R.mipmap.zanwusousuojieguo, "暂无结果", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);

        srlytCollocationList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isSimilar) {
                    getList();
                } else getSimilarGoods(goodsId, nOffset);
            }
        });

        rycvCollocationList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                //如果最后可见的item比总数小1，则表示最后一个，这里小3，预加载的意思
                if (visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 3 && !isBottom) {
                    if (!isSimilar) {
                        loadMore();
                    } else getSimilarGoods(goodsId, nOffset);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        mAdapter.setOnItemClickListener(new CollocationSelectedGoodsListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(NewGoodsItem info) {
                if(!isSimilar) {
                    Intent intent = new Intent();
                    intent.putExtra("info", new Gson().toJson(info));
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    gotoGoodsDetail(info.getId());
                }
            }
        });
    }

    @Override
    protected void init() {
        if (!isSimilar) {
            initDefaultData();
            getList();
            getLimitData();
        } else getSimilarGoods(goodsId, nOffset);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                Intent intentGoFilter = new Intent(CollocationSelectedGoodsListActivity.this, GoodsListFilterActivity.class);
                SharedPreUtil.putString(this,Configration.SHAREDPREFERENCE,"filter_option",new Gson().toJson(mInfos));
                intentGoFilter.putExtra("source", 1002);
                intentGoFilter.putExtra("siteId", id);
                intentGoFilter.putExtra("categoryid", category_ids);
                startActivityForResult(intentGoFilter, REQUEST_FILTER);
                overridePendingTransition(R.anim.slide_in_bottom_top, R.anim.slide_in_out_fixed);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILTER) {
                String infoData = SharedPreUtil.getString(this,Configration.SHAREDPREFERENCE,"filter_option_result","");
                if(!TextUtils.isEmpty(infoData)) {
                    getCommitData(new Gson().fromJson(infoData, MallLimitOptionInfo.class));
                }
            }
        }
    }

    private void getList() {
        nOffset = 0;
        post.setCategory_ids(category_ids);
        post.setGenders(genders);
        post.setBrand_ids(brand_ids);
        post.setCount(0);
        post.setSort_id(sort_ids);
        post.setPrice_ranges_id(price_ranges_id);
        post.setSize(size);
        post.setColor(color);
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getCategoryList(MapRequest.setMallListMap(
                nOffset, post))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                srlytCollocationList.setRefreshing(false);
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object != null && object.getList().size() > 0) {
                        if (mAdapter.getmList() != null) {
                            mAdapter.getmList().clear();
                        }
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        setSuccessView(srlytCollocationList);
                    } else {
                        isBottom = true;
                        if (mAdapter.getmList() != null) {
                            mAdapter.getmList().clear();
                        }
                        mAdapter.notifyDataSetChanged();
                        setNullView(srlytCollocationList);
                    }
                } else {
                    if (!NetUtil.IsInNetwork(CollocationSelectedGoodsListActivity.this)) {
                        setOffNetView(srlytCollocationList);
                    }
                }
            }
        });
    }

    // 加载更多
    private void loadMore() {
        nOffset += 40;
        post.setCount(0);
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getCategoryList(MapRequest.setMallListMap(
                nOffset, post))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object != null && object.getList().size() > 0) {
                        mAdapter.getmList().addAll(object.getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }
            }
        });
    }

    // 获取筛选条件
    private void getLimitData() {
        if (myRunnable != null) {
            mHandle.removeCallbacks(myRunnable);
        }
        myRunnable = new MyRunnable();
        mHandle.post(myRunnable);
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("id", String.valueOf(id));
            maps.put("brand", String.valueOf(0));
            maps.put("category_ids", category_ids);
            new RetrofitRequest<MallLimitOptionObject>(ApiRequest.getApiShiji().getCategoryOption(maps)).handRequest(
                    new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallLimitOptionObject object = (MallLimitOptionObject) msg.obj;

                        //对品类数组进行分类
                        if (object == null || object.getList() == null) {
                            return;
                        }
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
                            if (!TextUtils.isEmpty(category_ids)) {
                                mInfos.getCategories().clear();
                                ArrayList<SortInfo> categoryList = new ArrayList<SortInfo>();
                                SortInfo cInfo = Util.getDeepClone(productCategories.get(0));
//                                SortInfo cInfo = productCategories.get(0).clone();
                                cInfo.setCheck(true);
                                cInfo.setName(ALL);
                                categoryList.add(cInfo);
                                mInfos.setCategories(categoryList);
                            }
                            mInfos.getCategories().addAll(productCategories);

                            if(!isSimilar && !TextUtils.isEmpty(titleName)){
                                boolean isMatch=false;
                                for(int i=0;i<mInfos.getCategories().size();i++){
                                    if(titleName.equals(mInfos.getCategories().get(i).getName())){
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

                            if(!isSimilar && !TextUtils.isEmpty(typeName)){
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
            });
        }
    }

    //初始化筛选选项，手动添加全部和默认标签
    private void initDefaultData() {
        ArrayList<SortInfo> genderList = new ArrayList<>();
        ArrayList<SortInfo> categoryList = new ArrayList<>();
        ArrayList<SortInfo> priceList = new ArrayList<>();
        ArrayList<SortInfo> colorList = new ArrayList<>();
        ArrayList<SortInfo> sortList = new ArrayList<>();

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

    //  确定，提交筛选项
    public void getCommitData(MallLimitOptionInfo data) {
        if (data != null) {
            genders = "";
            sort_ids = "";
            price_ranges_id = "";
            brand_ids = "";
            color = "";
            mInfos = data;

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
            getList();
        }
    }

    /**
     * 获取相似单品
     *
     * @param goodsId
     */
    private void getSimilarGoods(String goodsId, final int offset) {
        nOffset += 40;
        HashMap<String, String> maps = new HashMap<>();
        maps.put("goodsId", goodsId);
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(40));
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getNearGoodesList(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                            if (object != null && object.getList().size() > 0) {
                                if (offset == 0) {
                                    mAdapter.setmList(object.getList());
                                    setSuccessView(srlytCollocationList);
                                } else {
                                    mAdapter.getmList().addAll(object.getList());
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                isBottom = true;
                                if (offset == 0) {
                                    setNullView(srlytCollocationList);
                                }
                            }
                        } else {
                            if (!NetUtil.IsInNetwork(CollocationSelectedGoodsListActivity.this)) {
                                setOffNetView(srlytCollocationList);
                            }
                        }
                        srlytCollocationList.setRefreshing(false);
                    }
                }
        );
    }

    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(this, NewGoodsDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("goodsId", goodsId);
        startActivity(intent);
    }

}
