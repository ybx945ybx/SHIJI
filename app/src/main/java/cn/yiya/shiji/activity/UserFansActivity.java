package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.UserFansAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.FansObject;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.utils.NetUtil;


/**
 * Created by dell on 2015-07-28.
 */
public class UserFansActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mListView;
    private UserFansAdapter mAdapter;

    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivFindFrends;

    private boolean isFans = true;
    private int user_id;
    private String user_name;
    private boolean isBottom;
    private int lastVisibleItemPosition;
    private int nOffset;
    private boolean isCustom;

    private static final int REQUEST_HOME_PAGE = 110;
    private static final int REQUEST_DISCOVER_FRIEND = 111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_userfans);
        user_id = this.getIntent().getIntExtra("user_id", 0);
        user_name = this.getIntent().getStringExtra("user_name");
        isFans = this.getIntent().getBooleanExtra("isFans", true);
        isCustom = this.getIntent().getBooleanExtra("isCustom", false);
        initViews();
        initEvents();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.title_right:
                Intent intent = new Intent(this, DiscoverFriendActivity.class);
                startActivityForResult(intent, REQUEST_DISCOVER_FRIEND);
                break;
            case R.id.tv_reload:
                getData();
                break;
        }
    }

    /**
     * 拉取列表
     */
    private void getData() {
        isBottom = false;
        nOffset = 0;
        if (isFans) {

            HashMap<String, String> maps = new HashMap<>();
            maps.put("user_id", String.valueOf(user_id));
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(10));

            new RetrofitRequest<FansObject>(ApiRequest.getApiShiji().userFans(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            hidePreDialog();
                            if (msg.isSuccess()) {
                                FansObject obj = (FansObject) msg.obj;
                                if (obj.list != null && obj.list.size() > 0) {
                                    mAdapter.setList(obj.list);
                                    setSuccessView(mSwipeRefreshLayout);
                                    mAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } else {
                                    setNullView(mSwipeRefreshLayout);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            } else {
                                mSwipeRefreshLayout.setRefreshing(false);
                                isBottom = true;
                                if (!NetUtil.IsInNetwork(UserFansActivity.this)) {
                                    setOffNetView(mSwipeRefreshLayout);
                                }
                            }
                        }
                    }
            );
        } else {

            HashMap<String, String> maps = new HashMap<>();
            maps.put("user_id", String.valueOf(user_id));
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(10));

            new RetrofitRequest<FansObject>(ApiRequest.getApiShiji().userFollowing(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            hidePreDialog();
                            if (msg.isSuccess()) {
                                FansObject obj = (FansObject) msg.obj;
                                if (obj.list != null && obj.list.size() > 0) {
                                    //未返回状态is_follow  自行设置
                                    for (int i = 0; i < obj.list.size(); i++) {
                                        obj.list.get(i).setIs_follow(true);
                                    }
                                    mAdapter.setList(obj.list);
                                    setSuccessView(mSwipeRefreshLayout);
                                    mAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } else {
                                    setNullView(mSwipeRefreshLayout);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    isBottom = true;
                                }
                            } else {
                                mSwipeRefreshLayout.setRefreshing(false);
                                if (!NetUtil.IsInNetwork(UserFansActivity.this)) {
                                    setOffNetView(mSwipeRefreshLayout);
                                }
                            }
                        }
                    }
            );
        }

    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
        nOffset += 10;
        if (isFans) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("user_id", String.valueOf(user_id));
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(10));

            new RetrofitRequest<FansObject>(ApiRequest.getApiShiji().userFans(maps)).handRequest(
                    new MsgCallBack() {

                        @Override
                        public void onResult(HttpMessage msg) {
                            hidePreDialog();
                            if (msg.isSuccess()) {
                                FansObject obj = (FansObject) msg.obj;
                                if (obj.list != null && obj.list.size() > 0) {
                                    mAdapter.getList().addAll(obj.list);
                                    mAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } else {
                                    isBottom = true;
                                }
                            } else {
                                if (!NetUtil.IsInNetwork(UserFansActivity.this)) {
                                    setOffNetView(mSwipeRefreshLayout);
                                }
                            }
                        }
                    });
        } else {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("user_id", String.valueOf(user_id));
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(10));

            new RetrofitRequest<FansObject>(ApiRequest.getApiShiji().userFollowing(maps)).handRequest(
                    new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            hidePreDialog();
                            if (msg.isSuccess()) {
                                FansObject obj = (FansObject) msg.obj;
                                if (obj.list != null && obj.list.size() > 0) {
                                    //未返回状态is_follow  自行设置
                                    for (int i = 0; i < obj.list.size(); i++) {
                                        obj.list.get(i).setIs_follow(true);
                                    }
                                    mAdapter.getList().addAll(obj.list);
                                    mAdapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } else {
                                    isBottom = true;
                                }
                            } else {
                                if (!NetUtil.IsInNetwork(UserFansActivity.this)) {
                                    setOffNetView(mSwipeRefreshLayout);
                                }
                            }
                        }
                    });
        }

    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) this.findViewById(R.id.title_txt);
        ivBack = (ImageView) this.findViewById(R.id.title_back);
        ivFindFrends = (ImageView) findViewById(R.id.title_right);
        ivFindFrends.setImageResource(R.mipmap.faxianhaoyou);
        if (isFans) {
            tvTitle.setText(user_name + "的粉丝");
            ivFindFrends.setVisibility(View.GONE);
        } else {
            tvTitle.setText(user_name + "的关注");
            if (isCustom) {
                ivFindFrends.setVisibility(View.VISIBLE);
                ivFindFrends.setOnClickListener(this);
            } else {
                ivFindFrends.setVisibility(View.GONE);
            }

        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.pulldown);

        mListView = (RecyclerView) this.findViewById(R.id.listViewfans);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new UserFansAdapter(this, isCustom);
        mListView.setAdapter(mAdapter);

        addDefaultNullView();
        if (isFans) {
            initDefaultNullView(R.mipmap.zanwufensi, "您还没有粉丝~", this);
        } else {
            initDefaultNullView(R.mipmap.zanwuguanzhu, "您还没有关注任何人~快去右上角添加吧", this);
        }

    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        mAdapter.setOnActionClickListener(new UserFansAdapter.OnActionClickListener() {
            @Override
            public void onActionClick() {
                setResult(RESULT_OK);
            }

            @Override
            public void onUserClick(int position) {
                User user = mAdapter.getList().get(position);
                Intent intent = new Intent(UserFansActivity.this, CommunityHomePageActivity.class);
                intent.putExtra("user_id", user.getUser_id());
                intent.putExtra("is_follow", user.is_follow());
                intent.putExtra("isCurUser", false);
                startActivityForResult(intent, REQUEST_HOME_PAGE);
            }
        });

    }

    @Override
    protected void init() {
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_DISCOVER_FRIEND) {
                if (data != null) {
                    if (data.getBooleanExtra("followChanged", false)) {
                        getData();
                    }
                }
            } else if (requestCode == REQUEST_HOME_PAGE) {
                if (data != null) {
                    if (data.getBooleanExtra("userInfoChanged", false)) {
                        getData();
                    }
                }
            }
        }
    }
}
