package cn.yiya.shiji.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ExRcvAdapterWrapper;
import cn.yiya.shiji.adapter.ShopSortAdapter;
import cn.yiya.shiji.adapter.SortPopAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.GetLocalRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.navigation.StoreCategoryInfo;
import cn.yiya.shiji.entity.navigation.StoreCategoryObject;
import cn.yiya.shiji.entity.navigation.StoreObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.views.GuideDividerItemDecoration;

/**
 * Created by Tom on 2016/4/7.
 * 店铺分类
 */
public class ShopSortActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private ImageView ivBack;                           // 标题栏返回按钮
    private TextView tvTitle;                           // 标题
    private ImageView ivSort;                           // 分类箭头
    private LinearLayout llytSort;

    private PopupWindow sortPopupWindow;                // 分类PopupWindow
    private View view;                                  // PopupWindow布局
    private RecyclerView rycvSort;                      // PopupWindow分类列表
    private SortPopAdapter sortPopAdapter;              // PopupWindow分类列表适配器

    private RecyclerView rycvShop;                      // 店铺列表
    private ShopSortAdapter shopSortAdapter;            // 店铺列表适配器
    private ExRcvAdapterWrapper shopSortAdapterWrap;

    private RelativeLayout rlytFoot;
    private boolean isBottom;
    private int lastVisibleItemPosition;

    private Handler mHander;
    private String id;
    private String categroy_id;
    private int type;                                // 1是城市 2是mall
    private ArrayList<StoreCategoryInfo> sortInfoList = new ArrayList<>();

    private String countryId;
    private String cityId;

    private String longitude;
    private String latitude;

    private RelativeLayout rlWithoutNet;
    private int nCurCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_sort);
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        mHander = new Handler(getMainLooper());
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);

        cityId = getIntent().getStringExtra("cityId");
        countryId = getIntent().getStringExtra("countryId");

        longitude = ((BaseApplication)getApplication()).getLongitude();
        latitude =((BaseApplication)getApplication()).getLatitude();

        rlWithoutNet = (RelativeLayout)findViewById(R.id.rl_without_net);

        LayoutInflater inflater = LayoutInflater.from(this);

        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("全部店铺");
        ivSort = (ImageView) findViewById(R.id.title_sort);
        llytSort = (LinearLayout) findViewById(R.id.llyt_sort);

        rycvShop = (RecyclerView) findViewById(R.id.shop_sort_list);
        rycvShop.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setSmoothScrollbarEnabled(false);
        rycvShop.setLayoutManager(llm);
        shopSortAdapter = new ShopSortAdapter(this);

        rlytFoot = (RelativeLayout) inflater.inflate(R.layout.stroe_sort_foot, null);

        shopSortAdapterWrap = new ExRcvAdapterWrapper(shopSortAdapter, rycvShop.getLayoutManager());
        shopSortAdapterWrap.setFooterView(rlytFoot);
        rycvShop.setAdapter(shopSortAdapterWrap);
    }

    @Override
    protected void initEvents() {
        rlWithoutNet.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llytSort.setOnClickListener(this);
        rycvShop.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    getShopList(shopSortAdapter.getMlist().size(), id, categroy_id, false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });
    }

    @Override
    protected void init() {
        if(!NetUtil.IsInNetwork(ShopSortActivity.this)){
            rycvShop.setVisibility(View.GONE);
            rlWithoutNet.setVisibility(View.VISIBLE);
            return;
        } else {
            rycvShop.setVisibility(View.VISIBLE);
            rlWithoutNet.setVisibility(View.GONE);
        }
        getSortInfo(id, type);
    }


    private void getSortInfo(final String id, int type) {
        showPreDialog("正在加载");
        if(type == 1) {
            new RetrofitRequest<StoreCategoryObject>(ApiRequest.getApiShiji().getStoreBrandsCategoryOfCity(String.valueOf(id)))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                StoreCategoryObject info = (StoreCategoryObject) msg.obj;
                                addAll(info);
                                getShopList(0, id, sortInfoList.get(0).getId(), true);
                                categroy_id = "";
                            } else {
                                showTips(msg.obj.toString());
                            }
                            hidePreDialog();
                        }
                    });
        }else {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("id", String.valueOf(id));
            maps.put("type", "mall");
            new RetrofitRequest<StoreCategoryObject>(ApiRequest.getApiShiji().getStoreBrandsCategoryOfMall(maps))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                StoreCategoryObject info = (StoreCategoryObject) msg.obj;
                                addAll(info);
                                getShopList(0, id, sortInfoList.get(0).getId(), true);
                                categroy_id = "";
                            } else {
                                showTips(msg.obj.toString());
                            }
                            hidePreDialog();
                        }
                    });
        }
    }


    private void getShopList(final int offset, String city_mall_id, String categroy_id, boolean isShow) {
        if(isShow) {
            showPreDialog("正在加载");
        }
        if (offset == 0) {
            nCurCount = 0;
        }

        if (offset > 0 && offset == nCurCount) {
            return;
        }

        nCurCount = offset;
        isBottom = false;
        if(type == 1) {

            if(NetUtil.IsInNetwork(ShopSortActivity.this)){
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfCity(MapRequest.setStoreListOfCity(
                        offset, city_mall_id, categroy_id, longitude, latitude, countryId, 0))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            StoreObject object = (StoreObject) msg.obj;
                            setList(object, offset);
                        }
                        hidePreDialog();
                    }
                });
            }else{
                GetLocalRequest.getInstance().getLocalStoreList("city", city_mall_id, new Handler(Looper.getMainLooper()),
                        countryId, city_mall_id, 0, new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    StoreObject object = (StoreObject) msg.obj;
                                    setList(object, offset);
                                }
                                hidePreDialog();
                            }
                        });
            }

        } else {

            if(NetUtil.IsInNetwork(ShopSortActivity.this)){
                new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfMall(MapRequest.setStoreListOfMall(
                        0, city_mall_id, countryId, cityId, categroy_id, 0))).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            StoreObject object = (StoreObject) msg.obj;
                            setList(object, offset);
                        }
                        hidePreDialog();
                    }
                });
            }else {
                GetLocalRequest.getInstance().getLocalStoreList("mall", city_mall_id, new Handler(Looper.getMainLooper())
                        , countryId, cityId, 0, new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    StoreObject object = (StoreObject) msg.obj;
                                    setList(object, offset);
                                }
                                hidePreDialog();
                            }
                        });
            }
        }
    }

    private void addAll(StoreCategoryObject info) {
        StoreCategoryInfo storeCategoryInfo = new StoreCategoryInfo();
        storeCategoryInfo.setId("");
        storeCategoryInfo.setName("全部店铺");
        sortInfoList = info.category_list;
        sortInfoList.add(0, storeCategoryInfo);
    }

    private void setList(StoreObject object, int offset) {
        if (object.list != null && object.list.size() > 0) {
            isBottom = false;
            if (offset > 0) {
                shopSortAdapter.getMlist().addAll(object.list);
            } else {
                if (shopSortAdapter.getMlist() != null) {
                    shopSortAdapter.getMlist().clear();
                }
                shopSortAdapter.setMlist(object.list);
            }
            shopSortAdapterWrap.notifyDataSetChanged();
            rlytFoot.setVisibility(View.GONE);
        } else {
            isBottom = true;
            if (offset > 0) {
                rlytFoot.setVisibility(View.VISIBLE);
            } else {
                if (shopSortAdapter.getMlist() != null) {
                    shopSortAdapter.getMlist().clear();
                }
                shopSortAdapterWrap.notifyDataSetChanged();
                rlytFoot.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.llyt_sort:
                showPopUpWindow(findViewById(R.id.ll_title));
                break;
            case R.id.rl_without_net:
                init();
                break;
        }
    }

    private void showPopUpWindow(View v) {
        view = LayoutInflater.from(this).inflate(R.layout.pop_goods_type, null);
        rycvSort = (RecyclerView) view.findViewById(R.id.recycler_mall_type);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rycvSort.setItemAnimator(new DefaultItemAnimator());
        rycvSort.setLayoutManager(gridLayoutManager);
        rycvSort.addItemDecoration(new GuideDividerItemDecoration(this, R.drawable.guide_divider_decoration));
        sortPopAdapter = new SortPopAdapter(this, sortInfoList);
        rycvSort.setAdapter(sortPopAdapter);

        sortPopAdapter.setOnItemActionListener(new SortPopAdapter.OnItemActionListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                getShopList(0, id, sortInfoList.get(pos).getId(), true);
                sortPopupWindow.dismiss();
                tvTitle.setText(sortInfoList.get(pos).getName());
                categroy_id = sortInfoList.get(pos).getId();
                isBottom = false;
            }
        });

        sortPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, SimpleUtils.dp2px(this, 42)*5);
        sortPopupWindow.setFocusable(true);
        sortPopupWindow.setOutsideTouchable(true);
        sortPopupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        sortPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        sortPopupWindow.showAsDropDown(v);
        ivSort.setImageResource(R.mipmap.sort_up);
        ViewPropertyAnimator.animate(rycvShop).translationY(SimpleUtils.dp2px(this, 42)*5).setDuration(1000).start();

        sortPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivSort.setImageResource(R.mipmap.sort_down);
                ViewPropertyAnimator.animate(rycvShop).translationY(0).setDuration(500).start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
