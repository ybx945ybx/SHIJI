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

import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewRecommendAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.entity.WorkObject;

/**
 * Created by dell on 2015-07-28.
 */
public class UserTagWorkActivity extends BaseAppCompatActivity implements View.OnClickListener{

    private Handler mHandler;

    private SwipeRefreshLayout srlytTagWork;
    private RecyclerView rycvTagWork;
    private NewRecommendAdapter tagWorkAdapter;

    private TextView mTitleView;
    private ImageView mBackBtn;
    private int tag_id;
    private int user_id;
    private String user_name;
    private String tag_name;
    private String tag_image;
    private int lastVisibleItemPosition;
    private boolean isBottom;
    private int nOffset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mHandler = new Handler();
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        tag_id=this.getIntent().getIntExtra("tag_id",0);
        tag_name=this.getIntent().getStringExtra("tag_name");
        tag_image=this.getIntent().getStringExtra("tag_image");
        user_id=this.getIntent().getIntExtra("user_id", 0);
        user_name=this.getIntent().getStringExtra("user_name");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
            finish();
            break;
        }
    }

    /**
     * 获取列表
     */
    private void getTagWork() {
        showPreDialog("");
        nOffset = 0;

        HashMap<String, String> maps = new HashMap<>();
        maps.put("user_id", String.valueOf(user_id));
        maps.put("tag_id", String.valueOf(tag_id));
        maps.put("offset", String.valueOf(nOffset));
        maps.put("limit", String.valueOf(10));
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().userTagWorks(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        hidePreDialog();
                        srlytTagWork.setRefreshing(false);
                        if (msg.isSuccess()) {
                            WorkObject obj = (WorkObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                tagWorkAdapter.setList(obj.list);
                                tagWorkAdapter.notifyDataSetChanged();
                            }else {
                                isBottom = false;
                            }
                        } else {
                            showTips(msg.message);
                        }
                    }
                }
        );
    }

    /**
     * 加载更多
     */
    private void loadMoreData(){
        nOffset += 10;
        HashMap<String, String> maps = new HashMap<>();
        maps.put("user_id", String.valueOf(user_id));
        maps.put("tag_id", String.valueOf(tag_id));
        maps.put("offset", String.valueOf(nOffset));
        maps.put("limit", String.valueOf(10));
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().userTagWorks(maps)).handRequest(
                new MsgCallBack() {

            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    WorkObject obj = (WorkObject) msg.obj;
                    if (obj.list != null && obj.list.size() > 0) {

                        tagWorkAdapter.getList().addAll(obj.list);
                        tagWorkAdapter.notifyDataSetChanged();
                    }else {
                        isBottom = false;
                    }
                } else {
                    showTips(msg.message);
                }
            }
        });

    }

    @Override
    protected void initViews() {
        mTitleView= (TextView) this.findViewById(R.id.title_txt);
        mTitleView.setText(user_name+"的标签照片");
        mBackBtn= (ImageView) this.findViewById(R.id.title_back);
        findViewById(R.id.title_right).setVisibility(View.GONE);

        srlytTagWork = (SwipeRefreshLayout) findViewById(R.id.pulldown);
        rycvTagWork = (RecyclerView) findViewById(R.id.listView);
        rycvTagWork.setItemAnimator(new DefaultItemAnimator());
        rycvTagWork.setLayoutManager(new LinearLayoutManager(this));
        tagWorkAdapter = new NewRecommendAdapter(this, new NewRecommendAdapter.LoginListener() {
            @Override
            public void goLogin() {

            }

            @Override
            public void setFollow(WorkItem item) {

            }

            @Override
            public void goToWorkDetail(Intent intent) {
               startActivity(intent);
            }

            @Override
            public void goToCommnets(Intent intent) {
                startActivity(intent);
            }
        }, true, tag_image, tag_name, tag_id);
        rycvTagWork.setAdapter(tagWorkAdapter);

    }

    @Override
    protected void initEvents() {
        mBackBtn.setOnClickListener(this);
        srlytTagWork.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTagWork();
            }
        });
        rycvTagWork.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        getTagWork();
    }
}
