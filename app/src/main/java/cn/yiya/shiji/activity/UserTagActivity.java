package cn.yiya.shiji.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.TagAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.TagObject;
import cn.yiya.shiji.utils.NetUtil;

/**
 * Created by dell on 2015-07-28.
 */
public class UserTagActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TextView tvTitle;
    private ImageView ivBack;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mGridView;
    private TagAdapter mAdapter;

    private Handler mHandler;

    private int user_id;
    private String user_name;
    private int lastVisibleItemPosition;
    private boolean isBottom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_grid);
        mHandler = new Handler();
        user_id=this.getIntent().getIntExtra("user_id", 0);
        user_name=this.getIntent().getStringExtra("user_name");
        initViews();
        initEvents();
        init();
    }

    private void loadMoreData(final int offset) {
        isBottom = false;
        HashMap<String, String> maps = new HashMap<>();
        maps.put("user_id", String.valueOf(user_id));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(10));
        new RetrofitRequest<TagObject>(ApiRequest.getApiShiji().userTagList(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            TagObject obj = (TagObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                if (offset > 0) {
                                    mAdapter.getList().addAll(obj.list);
                                } else {
                                    mAdapter.setList(obj.list);
                                    setSuccessView(mSwipeRefreshLayout);
                                }

                                mAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }else {
                                if(offset == 0){
                                    setNullView(mSwipeRefreshLayout);
                                }
                            }
                        } else {
                            isBottom = true;
                            showTips(msg.message);
                            if( !NetUtil.IsInNetwork(UserTagActivity.this)){
                                setOffNetView(mSwipeRefreshLayout);
                            }else {
                                if(offset == 0){
                                    setNullView(mSwipeRefreshLayout);
                                }

                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.tv_reload:
                loadMoreData(0);
                break;
        }
    }

    @Override
    protected void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.pulldown);

        tvTitle = (TextView) this.findViewById(R.id.title_txt);
        tvTitle.setText(user_name + "的标签");
        ivBack = (ImageView) this.findViewById(R.id.title_back);
        findViewById(R.id.title_right).setVisibility(View.GONE);

        mGridView = (RecyclerView) this.findViewById(R.id.gridView);
        mGridView.setLayoutManager(new GridLayoutManager(this, 2));
        mGridView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TagAdapter(this,user_id,user_name);
        mGridView.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwubiaoqian, "您还没有任何标签~", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        mGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    loadMoreData(mAdapter.getList().size());
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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreData(0);
            }
        });
    }

    @Override
    protected void init() {
        loadMoreData(0);
    }
}
