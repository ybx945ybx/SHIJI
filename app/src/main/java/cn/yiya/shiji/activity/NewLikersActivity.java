package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewLikersAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.LikerItem;
import cn.yiya.shiji.entity.LikerObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;

/**
 * 点赞列表页
 * Created by Amy on 2016/6/1.
 */
public class NewLikersActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private int workId;

    private Toolbar toolbar;                                    //Toolbar
    private TextView tvTitle;                                    //Toolbar标题  居中显示
    private ImageView ivBack;

    private SwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听
    private RecyclerView rvLikersList;
    private LinearLayoutManager layoutManager;
    private NewLikersAdapter mAdapter;
    private NewLikersAdapter.OnActionClickListener mListener;
    private ArrayList<LikerItem> mList;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private int nOffect = 0;
    private static final int Limit = 10;
    private static final int LOGIN_RESULT = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_likers);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        mHandler = new Handler();
        Intent intent = getIntent();
        if (intent != null) {
            workId = intent.getIntExtra("work_id", 0);
        }
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.toolbar_middle_txt);
        tvTitle.setText("赞");
        ivBack = (ImageView) findViewById(R.id.toolbar_left);

        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        rvLikersList = (RecyclerView) findViewById(R.id.rv_likers_list);
        layoutManager = new LinearLayoutManager(this);
        rvLikersList.setLayoutManager(layoutManager);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwudianzan, "暂无点赞~", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        mListener = new NewLikersAdapter.OnActionClickListener() {
            @Override
            public void OnUserClick(int position) {
                //点击跳转到用户详情页
                LikerItem item = mList.get(position);
                Intent intent = new Intent(NewLikersActivity.this, CommunityHomePageActivity.class);
                intent.putExtra("user_id", item.getUser_id());
                if (BaseApplication.getInstance().readUserId().equals(String.valueOf(item.getUser_id()))) {
                    intent.putExtra("isCurUser", true);
                }
                startActivity(intent);
            }

            @Override
            public void goLogin() {
                Intent intent = new Intent(NewLikersActivity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_RESULT);
                NewLikersActivity.this.overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
            }
        };
        rvLikersList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {
                    getLikersList(nOffect);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    protected void init() {
        setSwipeRefresh();
    }

    /**
     * 设置下拉刷新，刷新后更新页面数据
     */
    public void setSwipeRefresh() {
        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nOffect = 0;
                isBottom = false;
                mList = new ArrayList<>();
                getLikersList(0);
            }
        };
        srlRefresh.setOnRefreshListener(listener);
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(true);
            }
        });
        listener.onRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.tv_reload:
                setSwipeRefresh();
                break;
            default:
                break;
        }
    }

    /**
     * 加载点赞人列表
     *
     * @param offset
     */
    private void getLikersList(final int offset) {
        nOffect += 10;
        HashMap<String, String> maps = new HashMap<>();
        maps.put("work_id", String.valueOf(workId));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(Limit));
        new RetrofitRequest<LikerObject>(ApiRequest.getApiShiji().likedUser(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    LikerObject obj = (LikerObject) msg.obj;
                    if (obj == null || obj.list == null || obj.list.isEmpty()) {
                        isBottom = true;
                        if (offset == 0) {
                            setNullView(srlRefresh);
                        }
                    } else {
                        isBottom = false;
                        if (offset > 0) {
                            mList.addAll(obj.list);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mList = obj.list;
                            setSuccessView(srlRefresh);
                            mAdapter = new NewLikersAdapter(NewLikersActivity.this, mList, mListener);
                            rvLikersList.setAdapter(mAdapter);
                        }
                    }
                } else {
                    if (!NetUtil.IsInNetwork(NewLikersActivity.this)) {
                        setOffNetView(srlRefresh);
                    }
                }
                srlRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGIN_RESULT) {
                setSwipeRefresh();
                Util.getNewUserPullLayer(this, data);
            }
        }
    }
}