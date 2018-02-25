package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ForwardedGoodsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.MallGoodsDetailObject;

/**
 * Created by Tom on 2016/12/1.
 */

public class ForwardedGoodsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivSearch;

    private RecyclerView rycvForwardedGoods;
    private ForwardedGoodsAdapter mAdapter;

    private int lastVisibleItemPosition;
    private boolean isBottom;
    private int nOffset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forwarded_goods);
        initViews();
        initEvents();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                Intent intent = new Intent(ForwardedGoodsActivity.this, SearchForwardedGoodsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_reload:
                getGoodsList();
                break;
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("已转发商品");
        ivSearch = (ImageView) findViewById(R.id.title_right);
        ivSearch.setImageResource(R.mipmap.search_forward);

        rycvForwardedGoods = (RecyclerView) findViewById(R.id.rycv_forwarded);
        rycvForwardedGoods.setItemAnimator(new DefaultItemAnimator());
        rycvForwardedGoods.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ForwardedGoodsAdapter(this);
        rycvForwardedGoods.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwuzhuanfa, "暂无转发", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        rycvForwardedGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 3 && !isBottom) {
                    loadMoreData();
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
        getGoodsList();
    }

    private void getGoodsList() {
        nOffset = 0;
        isBottom = false;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getForwardGoods(MapRequest.setMapTwenty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        isBottom = false;
                        setSuccessView(rycvForwardedGoods);
                    }else {
                        isBottom = true;
                        setNullView(rycvForwardedGoods);
                    }
                }else {
                    setOffNetView(rycvForwardedGoods);
                }
            }
        });
    }

    private void loadMoreData() {
        nOffset += 20;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getForwardGoods(MapRequest.setMapTwenty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.getmList().addAll(object.getList());
                        mAdapter.notifyDataSetChanged();
                        isBottom = false;
                    }else {
                        isBottom = true;
                    }
                }else {
                    setOffNetView(rycvForwardedGoods);
                }
            }
        });
    }
}
