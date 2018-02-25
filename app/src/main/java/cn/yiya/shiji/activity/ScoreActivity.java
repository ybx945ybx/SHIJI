package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ScoreAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.ScoreObject;
import cn.yiya.shiji.utils.NetUtil;

public class ScoreActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private ImageView ivBack;
    private TextView tvTitle;

    private RecyclerView scoreList;
    private ScoreAdapter mAdapter;

    private int nOffset;
    private boolean isBottom;
    private int lastVisibleItemPosition;
    private static final int REQUEST_SCORE_LIST = 222;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initViews();
        initEvents();
        init();
    }

    private void getData(){
        nOffset = 0;
        new RetrofitRequest<ScoreObject>(ApiRequest.getApiShiji().getScore(String.valueOf(nOffset))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ScoreObject obj = (ScoreObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                mAdapter.setList(obj.list);
                                mAdapter.notifyDataSetChanged();
                                setSuccessView(scoreList);
                            } else {
                                isBottom = true;
                                setNullView(scoreList);
                            }
                        }else {
                            if(!NetUtil.IsInNetwork(ScoreActivity.this)){
                                setOffNetView(scoreList);
                            }
                        }
                    }
                }
        );
    }

    private void loadMore(){
        nOffset += 20;
        new RetrofitRequest<ScoreObject>(ApiRequest.getApiShiji().getScore(String.valueOf(nOffset))).handRequest(
                new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    ScoreObject obj = (ScoreObject) msg.obj;
                    if (obj.list != null && obj.list.size() > 0) {
                        mAdapter.getList().addAll(obj.list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                }else {
                    if(!NetUtil.IsInNetwork(ScoreActivity.this)){
                        setOffNetView(scoreList);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.tv_reload:
                getData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_CANCELED && requestCode == REQUEST_SCORE_LIST){
            getData();
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView)findViewById(R.id.title_back);
        tvTitle = (TextView)findViewById(R.id.title_txt);
        tvTitle.setText("积分明细");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        scoreList = (RecyclerView)findViewById(R.id.score_detail_list);
        scoreList.setItemAnimator(new DefaultItemAnimator());
        scoreList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ScoreAdapter(this);
        scoreList.setAdapter(mAdapter);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwujifen, "暂无积分~", this);
    }

    @Override
    protected void initEvents() {
        scoreList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                LinearLayoutManager gridLayoutManager = (LinearLayoutManager) layoutManager;
                lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getData();
    }
}
