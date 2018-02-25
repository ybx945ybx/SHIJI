package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ExchangeCouponRecordAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ExchangeCodeObject;
import cn.yiya.shiji.utils.NetUtil;

public class ExchangeCouponRecordActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivback;
    private TextView tvTitle;
    private RecyclerView mRecyclerView;
    private ExchangeCouponRecordAdapter mAdapter;
    private int lastVisibleItemPosition;
    private boolean isBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echange_coupon_record);
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
                getExchangeCodeRecord(0);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // 获取兑换记录
    private void getExchangeCodeRecord(final int offset){
        showPreDialog("");
        isBottom = false;
        new RetrofitRequest<ExchangeCodeObject>(ApiRequest.getApiShiji().exchangeList(MapRequest.setMapTwenty(offset)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hidePreDialog();
                            ExchangeCodeObject object = (ExchangeCodeObject) msg.obj;
                            if(object.list != null && object.list.size() > 0){
                                isBottom = false;
                                if(offset > 0){
                                    mAdapter.getmList().addAll(object.list);
                                }else {
                                    mAdapter.setmList(object.list);
                                    setSuccessView(mRecyclerView);
                                }
                                mAdapter.notifyDataSetChanged();
                            }else {
                                isBottom =true;
                                setNullView(mRecyclerView);
                            }
                        } else {
                            hidePreDialog();
                            showTips(msg.message);
                            if(!NetUtil.IsInNetwork(ExchangeCouponRecordActivity.this)){
                                setOffNetView(mRecyclerView);
                            }
                        }
                    }
                });
    }

    @Override
    protected void initViews() {
        ivback = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("兑换记录");
        findViewById(R.id.title_right).setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) findViewById(R.id.record_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ExchangeCouponRecordAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwuduihuanjilu, "暂无兑换记录", this);
    }

    @Override
    protected void initEvents() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if(visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 1 && !isBottom){
                    getExchangeCodeRecord(mAdapter.getmList().size());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

            }
        });

        ivback.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getExchangeCodeRecord(0);
    }
}
