package cn.yiya.shiji.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ExRcvAdapterWrapper;
import cn.yiya.shiji.adapter.MallGoodsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.BrandsItem;
import cn.yiya.shiji.entity.FlashActivityDetailInfo;
import cn.yiya.shiji.entity.FlashActivityInfo;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.IssueActivityDetailInfo;
import cn.yiya.shiji.entity.IssueActivityInfo;
import cn.yiya.shiji.entity.MallGoodsDetailObject;
import cn.yiya.shiji.entity.NewGoodsObject;
import cn.yiya.shiji.entity.ShareEntity;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by Tom on 2016/2/24.
 */
public class HomeIssueActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private ImageView ivCancle;
    private TextView tvTitle;
    private ImageView ivRight;
    private LinearLayout llytIssueHeader;
    private SimpleDraweeView ivHeader;
    private TextView tvDescHeader;
    private LinearLayout llytIssueFooter;
    private SimpleDraweeView ivFooterOne;
    private SimpleDraweeView ivFooterTwo;
    private SimpleDraweeView ivFooterThree;
    private RecyclerView mRecyclerView;
    private MallGoodsAdapter mAdapter;
    private ExRcvAdapterWrapper mExrcvAdapter;
    private ArrayList<NewGoodsItem> mList = new ArrayList<>();
    private ArrayList<NewGoodsItem> mFooterList = new ArrayList<>();
    private String data;
    private HtmlInfo dataInfo;
    private FlashActivityInfo flashActivityInfo;
    private IssueActivityInfo issueActivityInfo;
    private String headerImageUrl;
    private String flashActivityId;                  // 闪购活动编号
    private int activityId;                          // 活动编号
    private int thematic_id;                         // 专题编号
    private int type;                                // 11 是闪购详情，没有底部推荐
    private int menuId;
    private boolean isBottom;
    private int lastVisibleItemPosition;
    private int nOffset;

    private final static int FLASH_SALE = 11;        // 表示闪购，闪购不显示底部推荐
    private final static int REQUEST_LOGIN = 111;    // 登录
    private boolean isLogin;
    private ShareEntity share;                       //分享

    private RelativeLayout rlBrand;
    private ImageView ivBrandLogo;
    private TextView tvBrand;
    private TextView tvDesc;
    private boolean isRecommend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_issue);
        initItent();
        initViews();
        initEvents();
        init();
    }

    private void initItent() {
        Intent intent = getIntent();
        if (intent != null) {
            isRecommend = intent.getBooleanExtra("isRecommend", false);
            Uri uri = intent.getData();
            if (uri == null) {
                data = intent.getStringExtra("data");
                // 当前从专题列表H5跳转进来
                if (data != null) {
                    dataInfo = new Gson().fromJson(data, HtmlInfo.class);
                    issueActivityInfo = new Gson().fromJson(new Gson().toJson(dataInfo.getData()), IssueActivityInfo.class);
                    activityId = issueActivityInfo.getActivityId();
                    menuId = issueActivityInfo.getMenuId();
                } else {
                    // 11表示从闪购活动进来，其他情况分首页活动，和当前底部推荐两种进来
                    type = getIntent().getIntExtra("type", 0);
                    if (type == FLASH_SALE) {
                        flashActivityId = getIntent().getStringExtra("flashId");
                    } else {
                        activityId = Integer.parseInt(getIntent().getStringExtra("activityId"));
                        menuId = getIntent().getIntExtra("menuId", 0);
                    }
                }
            } else {
                //从网页跳转
                activityId = Integer.valueOf(uri.getQueryParameter("issueId"));
                type = Integer.valueOf(uri.getQueryParameter("activityType"));
            }
        }
    }

    private void getActivityDetail(final int type) {
        if (type == FLASH_SALE) {
            ivRight.setVisibility(View.VISIBLE);
            new RetrofitRequest<FlashActivityDetailInfo>(ApiRequest.getApiShiji().getFlashDetail(String.valueOf(flashActivityId)))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                FlashActivityDetailInfo flashActivityDetailInfo = (FlashActivityDetailInfo) msg.obj;
                                setImageLayoutParams(ivHeader, flashActivityDetailInfo.getCover());
                                tvDescHeader.setText(flashActivityDetailInfo.getDescription());
                                tvTitle.setText(flashActivityDetailInfo.getName());
                                share = flashActivityDetailInfo.getShare();
                            }
                        }
                    });
        } else {
            ivRight.setVisibility(View.VISIBLE);
            new RetrofitRequest<IssueActivityDetailInfo>(ApiRequest.getApiShiji().getActDetail(String.valueOf(activityId)))
                    .handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                IssueActivityDetailInfo activityDetailInfo = (IssueActivityDetailInfo) msg.obj;
                                thematic_id = activityDetailInfo.getThematic_id();
                                headerImageUrl = activityDetailInfo.getCover();
                                setImageLayoutParams(ivHeader, headerImageUrl);
                                tvDescHeader.setText(activityDetailInfo.getDescription());
                                tvTitle.setText(activityDetailInfo.getTitle());
                                share = activityDetailInfo.getShare();
                                if (menuId != 0) {
                                    new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getMenuList(String.valueOf(menuId)))
                                            .handRequest(new MsgCallBack() {
                                                @Override
                                                public void onResult(HttpMessage msg) {
                                                    if (msg.isSuccess()) {
                                                        NewGoodsObject object = (NewGoodsObject) msg.obj;
                                                        initFooterView(object);
                                                    }
                                                }
                                            });
                                } else {
                                    new RetrofitRequest<NewGoodsObject>(ApiRequest.getApiShiji().getThematicAct(String.valueOf(thematic_id)))
                                            .handRequest(new MsgCallBack() {
                                                @Override
                                                public void onResult(HttpMessage msg) {
                                                    if (msg.isSuccess()) {
                                                        NewGoodsObject object = (NewGoodsObject) msg.obj;
                                                        initFooterView(object);
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

    private void initFooterView(NewGoodsObject object) {
        if (object.getList() != null && object.getList().size() > 0) {
            int size = object.getList().size();
            switch (size) {
                case 1:
                    llytIssueFooter.setVisibility(View.GONE);
                    break;
                case 2:
                    for (int i = 0; i < size; i++) {
                        if (!object.getList().get(i).getCover().equals(headerImageUrl)) {
                            mFooterList.add(object.getList().get(i));
                        }
                    }
                    setOneRecommendView(mFooterList.get(0).getCover());
                    break;
                case 3:
                    for (int i = 0; i < size; i++) {
                        if (!object.getList().get(i).getCover().equals(headerImageUrl)) {
                            mFooterList.add(object.getList().get(i));
                        }
                    }
                    setTwoRecommendView(mFooterList.get(0).getCover(), mFooterList.get(1).getCover());
                    break;
                default:
                    for (int i = 0; i < 4; i++) {
                        if (!object.getList().get(i).getCover().equals(headerImageUrl)) {
                            mFooterList.add(object.getList().get(i));
                        }
                    }
                    setThreeRecommendView(mFooterList.get(0).getCover(), mFooterList.get(1).getCover(), mFooterList.get(2).getCover());
                    break;
            }
        } else {
            ViewGroup.LayoutParams layoutParams = llytIssueFooter.getLayoutParams();
            layoutParams.height = 0;
            llytIssueFooter.setLayoutParams(layoutParams);
            llytIssueFooter.setVisibility(View.GONE);
        }

    }

    private void setOneRecommendView(String url) {
        llytIssueFooter.setVisibility(View.VISIBLE);
        setFooterImageLayoutParams(ivFooterOne, url);
        ivFooterTwo.setVisibility(View.GONE);
        ivFooterThree.setVisibility(View.GONE);
    }

    private void setTwoRecommendView(String url1, String url2) {
        llytIssueFooter.setVisibility(View.VISIBLE);
        setFooterImageLayoutParams(ivFooterOne, url1);
        setFooterImageLayoutParams(ivFooterTwo, url2);
        ivFooterThree.setVisibility(View.GONE);
    }

    private void setThreeRecommendView(String url, String url2, String url3) {
        llytIssueFooter.setVisibility(View.VISIBLE);
        setFooterImageLayoutParams(ivFooterOne, url);
        setFooterImageLayoutParams(ivFooterTwo, url2);
        setFooterImageLayoutParams(ivFooterThree, url3);
    }

    private void getissueList(int type) {
        nOffset = 0;
        isBottom = false;
        if (type == 11) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("id", flashActivityId);
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(40));
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getFlashSaleList(maps)).
                    handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                                if (object.getList() != null && object.getList().size() > 0) {
                                    isBottom = false;
                                    mAdapter.setmLists(object.getList());
                                    mExrcvAdapter.notifyDataSetChanged();
                                } else {
                                    isBottom = true;
                                }
                            }
                        }
                    });
        } else {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("id", String.valueOf(activityId));
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(40));
            if(isRecommend){
                maps.put("isShopkeeper", String.valueOf(1));
            }
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getActList(maps)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                        if (object.getGoods_list() != null && object.getGoods_list().size() > 0) {
                            isBottom = false;
                            mAdapter.setmLists(object.getGoods_list());
                            mExrcvAdapter.notifyDataSetChanged();
                        } else {
                            isBottom = true;
                        }
                    }
                }
            });
        }
    }

    private void loardMore(int type) {
        nOffset += 40;
        isBottom = false;
        if (type == 11) {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("id", flashActivityId);
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(40));
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getFlashSaleList(maps)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                        if (object.getList() != null && object.getList().size() > 0) {
                            isBottom = false;
                            mAdapter.getmLists().addAll(object.getList());
                            mExrcvAdapter.notifyDataSetChanged();
                        } else {
                            isBottom = true;
                        }
                    }
                }
            });
        } else {
            HashMap<String, String> maps = new HashMap<>();
            maps.put("id", String.valueOf(activityId));
            maps.put("offset", String.valueOf(nOffset));
            maps.put("limit", String.valueOf(40));
            if(isRecommend){
                maps.put("isShopkeeper", String.valueOf(1));
            }
            new RetrofitRequest<MallGoodsDetailObject>(ApiRequest.getApiShiji().getActList(maps)).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MallGoodsDetailObject object = (MallGoodsDetailObject) msg.obj;
                        if (object.getGoods_list() != null && object.getGoods_list().size() > 0) {
                            isBottom = false;
                            mAdapter.getmLists().addAll(object.getGoods_list());
                            mExrcvAdapter.notifyDataSetChanged();
                        } else {
                            isBottom = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.issue_cover_footer_one:
                Intent intent01 = new Intent(this, HomeIssueActivity.class);
                intent01.putExtra("activityId", mFooterList.get(0).getId());
                intent01.putExtra("menuId", menuId);
                if(isRecommend){
                    intent01.putExtra("isRecommend", true);
                }
                startActivity(intent01);
                break;
            case R.id.issue_cover_footer_two:
                Intent intent02 = new Intent(this, HomeIssueActivity.class);
                intent02.putExtra("activityId", mFooterList.get(1).getId());
                intent02.putExtra("menuId", menuId);
                if(isRecommend){
                    intent02.putExtra("isRecommend", true);
                }
                startActivity(intent02);
                break;
            case R.id.issue_cover_footer_three:
                Intent intent03 = new Intent(this, HomeIssueActivity.class);
                intent03.putExtra("activityId", mFooterList.get(2).getId());
                intent03.putExtra("menuId", menuId);
                if(isRecommend){
                    intent03.putExtra("isRecommend", true);
                }
                startActivity(intent03);
                break;
            case R.id.title_right:
                goToShare();
                break;
            default:
                break;
        }
    }

    /**
     * 分享
     */
    private void goToShare() {
        if (share == null)
            return;
        new ShareDialog(this, share.getTitle(), share.getCover(), share.getUrl(), share.getDescription()).build().show();
    }

    public void setImageLayoutParams(SimpleDraweeView imageView, String imagesUrl) {
        imageView.setImageURI(imagesUrl);
    }

    public void setFooterImageLayoutParams(SimpleDraweeView imageView, String imagesUrl) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width = SimpleUtils.getScreenWidth(this) - SimpleUtils.dp2px(this, 12);
        layoutParams.height = (int) (layoutParams.width * 0.4f);
        layoutParams.leftMargin = SimpleUtils.dp2px(this, 6);
        layoutParams.rightMargin = SimpleUtils.dp2px(this, 6);
        layoutParams.bottomMargin = SimpleUtils.dp2px(this, 6);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageURI(Util.clipImageViewByWH(imagesUrl, layoutParams.width, layoutParams.height), R.mipmap.work_default);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        llytIssueHeader = (LinearLayout) layoutInflater.inflate(R.layout.issue_list_header, null);
        SimpleDraweeView ivThemIcon = (SimpleDraweeView) llytIssueHeader.findViewById(R.id.iv_theme_icon);
        ivThemIcon.setImageURI(Util.transferImage(BaseApplication.getInstance().getThemeIcon(), SimpleUtils.dp2px(this, 39)));
        ivHeader = (SimpleDraweeView) llytIssueHeader.findViewById(R.id.issue_cover_header);
        tvDescHeader = (TextView) llytIssueHeader.findViewById(R.id.issue_dec_header);
        ivCancle = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        ivRight = (ImageView) findViewById(R.id.title_right);

        rlBrand = (RelativeLayout) llytIssueHeader.findViewById(R.id.rl_brand);
        ivBrandLogo = (ImageView) llytIssueHeader.findViewById(R.id.iv_brand_logo);
        tvBrand = (TextView) llytIssueHeader.findViewById(R.id.tv_brand);
//        tvDesc = (TextView) llytIssueHeader.findViewById(R.id.tv_desc);

        mRecyclerView = (RecyclerView) findViewById(R.id.issue_list);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mAdapter = new MallGoodsAdapter(this, mList, false, isRecommend);
        mExrcvAdapter = new ExRcvAdapterWrapper(mAdapter, staggeredGridLayoutManager);
        if (type != FLASH_SALE) {
            llytIssueFooter = (LinearLayout) layoutInflater.inflate(R.layout.issue_list_footer, null);
            ivFooterOne = (SimpleDraweeView) llytIssueFooter.findViewById(R.id.issue_cover_footer_one);
            ivFooterTwo = (SimpleDraweeView) llytIssueFooter.findViewById(R.id.issue_cover_footer_two);
            ivFooterThree = (SimpleDraweeView) llytIssueFooter.findViewById(R.id.issue_cover_footer_three);
            mExrcvAdapter.setFooterView(llytIssueFooter);
        }
        mExrcvAdapter.setHeaderView(llytIssueHeader);
        mRecyclerView.setAdapter(mExrcvAdapter);

    }

    @Override
    protected void initEvents() {
        if (type != FLASH_SALE) {
            ivFooterOne.setOnClickListener(this);
            ivFooterTwo.setOnClickListener(this);
            ivFooterThree.setOnClickListener(this);
        }
        ivCancle.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取适配器的Item个数以及最后可见的Item
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visivleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                //如果最后可见的item比总数小1，则表示最后一个，这里小3，预加载的意思
                if ((visivleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 20) && !isBottom) {
                    loardMore(type);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastPositions = new int[2];
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = Math.max(lastPositions[0], lastPositions[1]);
            }
        });
        mAdapter.setOnItemClickListener(new MallGoodsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(final NewGoodsItem info) {
                if (isEffectClick()) {
                    Intent intent = new Intent(HomeIssueActivity.this, NewGoodsDetailActivity.class);
                    intent.putExtra("goodsId", info.getId());
                    startActivity(intent);

                }
            }

            @Override
            public void gotoLogin() {
                Intent intent = new Intent(HomeIssueActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
                overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
            }


        });
    }

//    private void showRecommendGoods() {
//        View layoutTips = LayoutInflater.from(this).inflate(R.layout.collect_goods_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
//        TextView tvContent = (TextView) layoutTips.findViewById(R.id.toast_context);
//        tvContent.setText("已添加至个人中心－店主推荐");
//        final Toast toast = new Toast(this);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setView(layoutTips);
//        toast.show();
//    }

    @Override
    protected void init() {
        getissueList(type);
        getBrandInfo(type);
        getActivityDetail(type);
        getIsLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                isLogin = true;
            }
        }
    }

    private void getIsLogin() {
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    isLogin = true;
                }
            }
        });
    }

    private void getBrandInfo(int type) {
        if (type == FLASH_SALE) return;
        new RetrofitRequest<BrandsItem>(ApiRequest.getApiShiji().getBrandInfo(String.valueOf(activityId)))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            rlBrand.setVisibility(View.VISIBLE);
                            final BrandsItem item = (BrandsItem) msg.obj;
                            Netroid.displayImage(Util.transfer(item.getLogo()), ivBrandLogo);
                            tvBrand.setText(item.getName());
//                            tvDesc.setText(item.getDes().trim());
                            rlBrand.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(isEffectClick()) {
                                        goToBrandDetail(item);
                                    }
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 跳转到品牌详情
     *
     * @param item
     */
    private void goToBrandDetail(BrandsItem item) {
        Intent intent = new Intent(this, NewSingleBrandActivity.class);
        intent.putExtra("brand_id", item.getId());
        if(isRecommend){
            intent.putExtra("isRecommend", true);
        }
        startActivity(intent);
    }
}
