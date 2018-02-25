package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.MyRememberUserAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.RememberUserItem;
import cn.yiya.shiji.entity.RememberUserObject;

/**
 * Created by weixuewu on 15/8/2.
 */
public class DiscoverFriendActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TextView tvTitle;
    private ImageView ivBack;
    private Handler mHandler;
    private RecyclerView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyRememberUserAdapter mAdapter;
    private boolean isAge;
    public boolean followChanged;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tab);

        isAge=false;
        mHandler = new Handler(this.getMainLooper());

        initViews();
        initEvents();
        init();
    }

    @Override
    protected void initViews() {
        tvTitle= (TextView) this.findViewById(R.id.title_txt);
        tvTitle.setText("发现好友");
        ivBack= (ImageView) this.findViewById(R.id.title_back);
        findViewById(R.id.title_right).setVisibility(View.GONE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pulldown);

        mListView = (RecyclerView) this.findViewById(R.id.listView);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyRememberUserAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreData(0);
            }
        });
        mAdapter.setOnActionClickListener(new MyRememberUserAdapter.OnActionClickListener() {
            @Override
            public void onWorkClick(RememberUserItem.Work work) {
                Intent intent1 = new Intent(DiscoverFriendActivity.this, NewWorkDetailsActivity.class);
                intent1.putExtra("work_id", work.getWork_id());
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void init() {
        loadMoreData(0);
    }

    private void followUser(final RememberUserItem item) {
        showPreDialog("");
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    item.setIs_follow(true);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showTips(msg.message);
                }
            }
        });
    }

     /**
     * 加载更多
     */
    private void loadMoreData(final int offset){
        if(isAge){
            new RetrofitRequest<RememberUserObject>(ApiRequest.getApiShiji().getRecFriends(MapRequest.setUserIdMap(
                    BaseApplication.getInstance().readUserId(), offset))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        RememberUserObject obj = (RememberUserObject) msg.obj;
                        if (obj.list != null && obj.list.size() > 0) {
                            if (offset > 0) {
                                mAdapter.getList().addAll(obj.list);
                            } else {
                                mAdapter.setList(obj.list);
                            }
                            mAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    } else {
                        showTips(msg.message);
                    }
                }
            });
        }else{
            new RetrofitRequest<RememberUserObject>(ApiRequest.getApiShiji().getUserFriends(MapRequest.setMapTen(offset))).
                    handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if(msg.isSuccess()){
                                RememberUserObject obj = (RememberUserObject)msg.obj;
                                if(obj.list != null && obj.list.size() > 0){
                                    if(offset > 0){
                                        mAdapter.getList().addAll(obj.list);
                                    }else{
                                        mAdapter.setList(obj.list);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }else{
                                showTips(msg.message);
                            }
                        }
                    }
            );
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("followChanged", followChanged);
        setResult(RESULT_OK, intent);
        finish();
    }
}
