package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.SellerRecommendedAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.views.SwipeRecyclerView;

/**
 * 已推荐列表页
 * Created by Tom on 2016/12/5.
 */

public class SellerRecommendedActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private SwipeRecyclerView rycvSellerRecommended;
    private SellerRecommendedAdapter mAdapter;
	private int nOffset = 0;
    private int lastVisibleItemPosition;
    private boolean isBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_recommended);
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
            case R.id.tv_reload:
                getGoodsList();
                break;
        }

    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("已推荐");
        findViewById(R.id.title_right).setVisibility(View.GONE);
        rycvSellerRecommended = (SwipeRecyclerView) findViewById(R.id.rycv_seller_secommended);
        rycvSellerRecommended.setItemAnimator(new DefaultItemAnimator());
        rycvSellerRecommended.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SellerRecommendedAdapter(this);
        rycvSellerRecommended.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwubiaoqian, "暂无推荐", this);

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);

		rycvSellerRecommended.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {
                    loadMoreData(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = ((LinearLayoutManager) (recyclerView.getLayoutManager())).findLastVisibleItemPosition();
            }
        });

        mAdapter.setOnItemClickListener(new SellerRecommendedAdapter.OnItemClickListener() {
            @Override
            public void deleteAll() {

            }
        });
    }

    @Override
    protected void init() {
        getGoodsList();
    }

    /**
     * 获取已推荐商品列表
     */
    private void getGoodsList() {
		nOffset = 0;
        isBottom = false;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getRecommendGoods(MapRequest.setMapTwenty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.setmList(object.getList());
                        mAdapter.notifyDataSetChanged();
                        setSuccessView(rycvSellerRecommended);
                    }else {
						isBottom = true;
                        setNullView(rycvSellerRecommended);
                    }
                }else {
                    if(!NetUtil.IsInNetwork(SellerRecommendedActivity.this)){
                        setOffNetView(rycvSellerRecommended);
                    }
                }
            }
        });
    }
	/**
     * 加载更多
     */
    private void loadMoreData(final boolean isFromDelete) {
        nOffset += 20;
        new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getRecommendGoods(MapRequest.setMapTwenty(nOffset))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                    if (object.getList() != null && object.getList().size() > 0) {
                        mAdapter.getmList().addAll(object.getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                        if(isFromDelete){
                            setNullView(rycvSellerRecommended);
                        }
                    }
                }
            }
        });
    }
}
