package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CommonTagAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.TagProduceObject;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by Tom on 2016/6/23.
 */
public class CommonTagActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private SwipeRefreshLayout srlytCommonTag;

    private ImageView ivBack;
    private TextView tvTitle;

    private RecyclerView rycvCommonTag;
    private CommonTagAdapter commonTagAdapter;

    private int tag_id;
    private String tag_content;
    private int nOffset;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    public Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tag);
        mHandler = new Handler(getMainLooper());
        initItent();
        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        srlytCommonTag = (SwipeRefreshLayout) findViewById(R.id.srlyt_common_tag);
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("#"+tag_content+"#");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        rycvCommonTag = (RecyclerView) findViewById(R.id.rycv_common_tag);
        rycvCommonTag.setLayoutManager(new GridLayoutManager(this, 2));
        rycvCommonTag.setItemAnimator(new DefaultItemAnimator());
        commonTagAdapter = new CommonTagAdapter(this);
        rycvCommonTag.setAdapter(commonTagAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwubiaoqian, "您还没有任何标签~", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        srlytCommonTag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWorkList();
            }
        });
        rycvCommonTag.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    loadMore();
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
    }

    @Override
    protected void init() {
        getWorkList();
    }

    private void initItent() {
        Intent intent = getIntent();
        if(intent != null){
            tag_id=this.getIntent().getIntExtra("tag_id",0);
            tag_content=intent.getStringExtra("tag_content");
        }
    }

    private void getWorkList() {
        showPreDialog("");
        nOffset = 0;
        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(MapRequest.setTagId(tag_id, nOffset)))
                .handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    hidePreDialog();
                    srlytCommonTag.setRefreshing(false);
                    TagProduceObject obj = (TagProduceObject) msg.obj;
                    if (obj.list != null && obj.list.size() > 0) {
                        commonTagAdapter.setmList(obj.list);
                        commonTagAdapter.notifyDataSetChanged();
                        setSuccessView(srlytCommonTag);
                    } else {
                        isBottom = true;
                        setNullView(srlytCommonTag);
                    }
                } else {
                    hidePreDialog();
                    srlytCommonTag.setRefreshing(false);
                    showTips(msg.message);
                    if(!NetUtil.IsInNetwork(CommonTagActivity.this)){
                        setOffNetView(srlytCommonTag);
                    }
                }
            }
        });
    }

    private void loadMore(){
        nOffset += 10;
        new RetrofitRequest<TagProduceObject>(ApiRequest.getApiShiji().tagStar(MapRequest.setTagId(tag_id, nOffset)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            hidePreDialog();
                            TagProduceObject obj = (TagProduceObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                commonTagAdapter.getmList().addAll(obj.list);
                                commonTagAdapter.notifyDataSetChanged();
                            } else {
                                isBottom = true;
                            }
                        } else {
                            if(!NetUtil.IsInNetwork(CommonTagActivity.this)){
                                setOffNetView(srlytCommonTag);
                            }
                            showTips(msg.message);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_reload:
                getWorkList();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
