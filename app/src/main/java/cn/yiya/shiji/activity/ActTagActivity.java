package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.CommonTagAdapter;
import cn.yiya.shiji.adapter.ExRcvAdapterWrapper;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.CampaignDetailInfo;
import cn.yiya.shiji.entity.TagProduceObject;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;

/**
 * 活动标签界面
 * Created by Tom on 2016/6/23.
 */
public class ActTagActivity extends BaseAppCompatActivity implements View.OnClickListener {
    /*
    标题栏
     */
    private ImageView ivBack;
    private TextView tvTitle;
    private LinearLayout llytHead;
    /*
    活动标签头图
     */
    private ImageView ivTagDetail;
    /*
    标签详情
     */
    private RelativeLayout rlytTagDetails;
    private ImageView ivTagDetailArrow;
    private TextView tvTagDetail;
    /*
    标签列表
     */
    private RecyclerView rycvActTag;
    private CommonTagAdapter tagAdapter;
    private ExRcvAdapterWrapper tagExrAdapter;

    private int tag_id;
    private int nOffset;
    private Handler mHandler;
    private int lastVisibleItemPosition;
    private boolean isBottom;

    public static final int Limit = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_tag);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        mHandler = new Handler();
        Intent intent = getIntent();
        if(intent != null){
            tag_id = this.getIntent().getIntExtra("tag_id", 0);
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        findViewById(R.id.title_right).setVisibility(View.GONE);

        llytHead = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.act_tag_head, null);
        ivTagDetail = (ImageView) llytHead.findViewById(R.id.iv_tag);

        rlytTagDetails = (RelativeLayout) llytHead.findViewById(R.id.rlyt_tag_details);
        ivTagDetailArrow = (ImageView) llytHead.findViewById(R.id.iv_tag_detail_arrow);
        tvTagDetail = (TextView) llytHead.findViewById(R.id.tv_tag_details);

        rycvActTag = (RecyclerView) findViewById(R.id.rycv_work_list);
        rycvActTag.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rycvActTag.setLayoutManager(gridLayoutManager);
        tagAdapter = new CommonTagAdapter(this);
        tagExrAdapter = new ExRcvAdapterWrapper(tagAdapter, gridLayoutManager);
        tagExrAdapter.setHeaderView(llytHead);
        rycvActTag.setAdapter(tagExrAdapter);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlytTagDetails.setOnClickListener(this);
        rycvActTag.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        getTagDetail();
        getWorkList();
    }

    private void getTagDetail() {
        new RetrofitRequest<CampaignDetailInfo>(ApiRequest.getApiShiji().getActTagDetail(String.valueOf(tag_id))).
                handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    CampaignDetailInfo info = (CampaignDetailInfo) msg.obj;
                    if(info != null){
                        tvTitle.setText(info.getTag());
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)ivTagDetail.getLayoutParams();
                        params.width = SimpleUtils.getScreenWidth(ActTagActivity.this);
                        params.height = params.width * 159/375;
                        ivTagDetail.setLayoutParams(params);
                        BitmapTool.clipShowImageView(info.getBanner(), ivTagDetail, R.drawable.user_dafault, params.width, params.height);
                        tvTagDetail.setText(Html.fromHtml(info.getDesc()));
                    }
                }
            }
        });
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
                            TagProduceObject obj = (TagProduceObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                tagAdapter.setmList(obj.list);
                                tagExrAdapter.notifyDataSetChanged();
                            } else {
                                isBottom = true;
                            }
                        } else {
                            hidePreDialog();
                            showTips(msg.message);
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
                        tagAdapter.getmList().addAll(obj.list);
                        tagExrAdapter.notifyDataSetChanged();
                    } else {
                        isBottom = true;
                    }
                } else {
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
            case R.id.rlyt_tag_details:
                if(tvTagDetail.getVisibility() == View.VISIBLE){
                    tvTagDetail.setVisibility(View.GONE);
                    ivTagDetailArrow.setImageResource(R.mipmap.xia_fanhui);
                }else {
                    tvTagDetail.setVisibility(View.VISIBLE);
                    ivTagDetailArrow.setImageResource(R.mipmap.shang_fanhui);
                }
        }
    }
}
