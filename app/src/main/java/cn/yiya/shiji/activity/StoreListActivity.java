package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.StoreListAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.navigation.StoreObject;

/**
 * Created by jerry on 2016/3/29.
 * 商户列表
 */
public class StoreListActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TextView tvTitle, tvRight;
    private RecyclerView rvStoreList;
    private StoreListAdapter storeListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivBack;
    private int lastVisiableItem;
    private boolean isLoadmore = false;
    private Handler handler;
    private int loadTimes = 0;
    private final int OFFSET_TIMES = 10;
    private Intent intent;
    private String id;
    private TextView tvNoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_list_layout);
        handler = new Handler();
        intent = getIntent();
        id = intent.getStringExtra("id");

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvRight = (TextView) findViewById(R.id.title_right);
        rvStoreList = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        tvTitle.setText("商户列表");
        ivBack = (ImageView) findViewById(R.id.title_back);
        ivBack.setOnClickListener(this);
        tvRight.setVisibility(View.GONE);
        rvStoreList.setLayoutManager(new LinearLayoutManager(StoreListActivity.this));
        storeListAdapter = new StoreListAdapter(StoreListActivity.this);
        rvStoreList.setAdapter(storeListAdapter);
        tvNoData = (TextView) findViewById(R.id.reload);
    }

    @Override
    protected void initEvents() {
        tvNoData.setOnClickListener(this);
        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTimes = 0;
                getStoreListOfSame(loadTimes, id);// 商场id  570876a06bcbc0c017471ce1
            }
        });
        // 自动加载
        rvStoreList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && storeListAdapter.getItemCount() == lastVisiableItem + 1) {
                    if (isLoadmore) {
                        ++loadTimes;
                        getStoreListOfSame(loadTimes * OFFSET_TIMES, id);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = ((LinearLayoutManager) rvStoreList.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        storeListAdapter.setOnItemClickListener(new StoreListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                Intent intent = new Intent(StoreListActivity.this, TravelStoreActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.rl_without_net:
                initData();
                break;
        }
    }

    private void initData() {
        showPreDialog("正在加载...");
        getStoreListOfSame(loadTimes, id);// 商场id  570876a06bcbc0c017471ce1
    }

    public void getStoreListOfSame(final int offset, String mallId) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(10));
        maps.put("id", mallId);

        new RetrofitRequest<StoreObject>(ApiRequest.getApiShiji().getStoreListOfSame(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            setNoDataView(false);
                            StoreObject countryListObject = (StoreObject) msg.obj;
                            if (offset > 0) {
                                storeListAdapter.getList().addAll(countryListObject.list);
                            } else {
                                storeListAdapter.setList(countryListObject.list);
                            }
                            loadMore(countryListObject.list.size());             // 判断是否可以加载更多
                        } else {
                            showTips(msg.obj.toString());
                            setNoDataView(true);
                        }
                        hidePreDialog();
                        storeListAdapter.notifyDataSetChanged();
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );
    }

    private void loadMore(int size) {
        if (size >= OFFSET_TIMES) {
            isLoadmore = true;
        } else {
            isLoadmore = false;
        }
    }

    private void setNoDataView(boolean bShow) {
        if (bShow) {
            tvNoData.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }
}
