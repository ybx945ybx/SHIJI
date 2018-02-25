package cn.yiya.shiji.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewCommentsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.CommentItem;
import cn.yiya.shiji.entity.CommentObject;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;

/**
 * 评论列表页
 * Created by Amy on 2016/6/1.
 */
public class NewCommentsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private Handler mHandler;
    private int workId;
    private int position = -1;
    private String data;
    private WorkItem workItem;
    private boolean bChanged = false;

    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView ivBack;

    private SwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener; //SwiperefreshLayout刷新监听
    private RecyclerView rvCommentsList;
    private LinearLayoutManager layoutManager;
    private NewCommentsAdapter mAdapter;
    private ArrayList<CommentItem> mList;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private int nOffect = 0;
    private static final int Limit = 10;

    private EditText etComment;
    private Button btnSendComment;

    private static final int LOGIN_RESULT = 200;
    private static boolean bLogin;

    private int commentType = 0;            // 0是评论 1是回复
    private int parentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comments);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    /**
     * 获取Intent
     */
    private void initIntent() {
        mHandler = new Handler();
        Intent intent = getIntent();
        if (intent != null) {
            workId = intent.getIntExtra("work_id", 0);
            position = intent.getIntExtra("position", -1);
            data = intent.getStringExtra("data");
            if (!TextUtils.isEmpty(data)) {
                workItem = new Gson().fromJson(data, WorkItem.class);
            }
        }
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.toolbar_middle_txt);
        tvTitle.setText("评论");
        ivBack = (ImageView) findViewById(R.id.toolbar_left);

        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        rvCommentsList = (RecyclerView) findViewById(R.id.rv_comments_list);
        layoutManager = new LinearLayoutManager(this);
        rvCommentsList.setLayoutManager(layoutManager);

        etComment = (EditText) findViewById(R.id.et_comment);
        btnSendComment = (Button) findViewById(R.id.btn_send_comment);
        showSoftInput();
        addDefaultNullView();
        initDefaultNullView(R.mipmap.without_comments, "暂无评论~", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        btnSendComment.setOnClickListener(this);
        rvCommentsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取适配器的Item个数以及最后可见的Item
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                // 如果最后可见的Item比总数小1，表示最后一个，预加载的意思
                if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) == totalItemCount - 1) && !isBottom) {
                    getCommentsList(nOffect);
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
        if (!TextUtils.isEmpty(BaseApplication.getInstance().readUserId())) {
            bLogin = true;
        } else {
            bLogin = false;
        }
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
                getCommentsList(0);
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
            case R.id.btn_send_comment:
                //发送评论
                String comment = etComment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {
                    if (bLogin) {
                        bChanged = true;
                        if (commentType == 0) {

                            addComment(workId, "", comment);
                        } else if (commentType == 1) {
                            addComment(workId, parentId + "", comment);
                        }
                    } else {
                        Intent intent = new Intent(NewCommentsActivity.this, LoginActivity.class);
                        startActivityForResult(intent, LOGIN_RESULT);
                        overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                    }
                } else showTips("评论不能为空！");
                break;
            case R.id.tv_reload:
                setSwipeRefresh();
                break;
            default:
                break;
        }
    }

    /**
     * 加载评论列表数据
     *
     * @param offset
     */
    private void getCommentsList(final int offset) {
        nOffect += 10;
        HashMap<String, String> maps = new HashMap<>();
        maps.put("work_id", String.valueOf(workId));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(Limit));
        new RetrofitRequest<CommentObject>(ApiRequest.getApiShiji().getWorkCommentList(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            CommentObject obj = (CommentObject) msg.obj;
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
                                    mAdapter = new NewCommentsAdapter(NewCommentsActivity.this, mList);
                                    rvCommentsList.setAdapter(mAdapter);
                                    mAdapter.setOnReplyClickListener(new NewCommentsAdapter.OnReplyClickListener() {
                                        @Override
                                        public void OnReplyClick(CommentItem commentItem) {
                                            showSoftInput();
                                            etComment.setHint("回复" + commentItem.getUser_name());
                                            commentType = 1;
                                            parentId = commentItem.getId();
                                        }
                                    });
                                }
                            }
                        } else {
                            if (!NetUtil.IsInNetwork(NewCommentsActivity.this)) {
                                setOffNetView(srlRefresh);
                            }
                        }
                        srlRefresh.setRefreshing(false);
                    }
                }
        );
    }

    /**
     * 发送评论
     *
     * @param workId
     * @param parentId
     * @param comment
     */
    private void addComment(final int workId, String parentId, String comment) {
        showPreDialog("");
        HashMap<String, String> maps = new HashMap<>();
        maps.put("work_id", String.valueOf(workId));
        maps.put("parent_id", String.valueOf(parentId));
        maps.put("comment", comment);
        new RetrofitRequest<>(ApiRequest.getApiShiji().addComment(maps)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                hidePreDialog();
                if (msg.isSuccess()) {
                    hideSoftInput();
                    setSwipeRefresh();
                    etComment.setText(null);
                    if (workItem != null) {
                        workItem.setComment_count(workItem.getComment_count() + 1);
                    }
                    commentType = 0;
                } else {
                    showTips(msg.message);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LOGIN_RESULT) {
                bLogin = true;
                Util.getNewUserPullLayer(this, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (bChanged) {
            intent.putExtra("submit", true);
            if (position != -1 && !TextUtils.isEmpty(data)) {
                intent.putExtra("position", position);
                intent.putExtra("data", new Gson().toJson(workItem));
            }
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    // 收起软键盘
    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(NewCommentsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        commentType = 0;
        etComment.setHint("写评论...");
    }


    // 弹出软键盘
    private void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etComment, 0);

    }
}
