package cn.yiya.shiji.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewLikersImageAdapter;
import cn.yiya.shiji.adapter.NewMatchGoodsAdapter;
import cn.yiya.shiji.adapter.NewWorkCommentsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.CommentObject;
import cn.yiya.shiji.entity.LikerObject;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.ShareDialog;

/**
 * 搭配详情
 * Created by Amy on 2016/7/12.
 */
public class NewMatchDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int workId;               //作品Id
    private WorkItem matchItem;       //作品实体
    private int position = -1;
    private String data;
    private WorkItem listitem;
    private boolean bChanged = false;

    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivShoppingCart;
    private RelativeLayout rlRightLayout;
    private TextView tvCarCount;

    private NestedScrollView svScroll;
    private LinearLayout llUser;
    private SimpleDraweeView ivUserAvatar;
    private TextView ivRedPeople;
    private TextView tvUserName;
    private FloatingActionButton btnFollow;
    private ImageView ivDelete;               //主态删除按钮

    private ImageView ivTagImage;
    private RelativeLayout.LayoutParams layoutParams;

    private LinearLayout llLikeUserList;
    private TextView tvLikeSum;
    private RecyclerView rvLikeUser;
    private RelativeLayout rlLikersArrow;
    private NewLikersImageAdapter likeUserListAdapter;

    private TextView tvContent;
    private TextView tvContentDate;
    private RecyclerView rvGoods;
    private NewMatchGoodsAdapter goodsAdapter;
    private TagFlowLayout tflLinkTag;
    private AllListView lvCommentsList;
    private NewWorkCommentsAdapter commentAdapter;

    private RelativeLayout rlFooter;
    private RelativeLayout rlLike;
    private TextView tvLikeCount;
    private RelativeLayout rlComments;
    private TextView tvCommentsCount;
    private RelativeLayout rlShareImage;
    private RelativeLayout rlLikeImage;
    private ImageView ivLike;
    private RelativeLayout rlCommentsImage;

    private SwipeRefreshLayout srlRefresh;
    private SwipeRefreshLayout.OnRefreshListener listener;

    private static final int LOGIN_RESULT = 100;
    private static final int COMMENTS_REQUSET = 111;

    private boolean bDelete = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match_detail);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    /**
     * 获取Intent
     */
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri == null) {
                workId = intent.getIntExtra("work_id", 0);
                position = intent.getIntExtra("position", -1);
                data = intent.getStringExtra("data");
                if (!TextUtils.isEmpty(data)) {
                    listitem = new Gson().fromJson(data, WorkItem.class);
                }
            } else {
                workId = Integer.valueOf(uri.getQueryParameter("id"));
            }

        }
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.toolbar_middle_txt);
        tvTitle.setText("搭配详情");
        ivBack = (ImageView) findViewById(R.id.toolbar_left);
        ivShoppingCart = (ImageView) findViewById(R.id.toolbar_right_img);
        rlRightLayout = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCarCount = (TextView) findViewById(R.id.toolbar_right_count);
        rlRightLayout.setVisibility(View.VISIBLE);

        /*中间滚动部分*/
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        svScroll = (NestedScrollView) findViewById(R.id.sv_scroll);

        llUser = (LinearLayout) findViewById(R.id.ll_user);
        ivUserAvatar = (SimpleDraweeView) findViewById(R.id.iv_user_avatar);
        ivRedPeople = (TextView) findViewById(R.id.iv_red_people);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        btnFollow = (FloatingActionButton) findViewById(R.id.btn_follow);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);

        ivTagImage = (ImageView) findViewById(R.id.iv_tag_image);
        layoutParams = (RelativeLayout.LayoutParams) ivTagImage.getLayoutParams();
        layoutParams.width = SimpleUtils.getScreenWidth(this);
        layoutParams.height = layoutParams.width * 484 / 375;
        ivTagImage.setLayoutParams(layoutParams);

        llLikeUserList = (LinearLayout) findViewById(R.id.ll_like_user_list);
        tvLikeSum = (TextView) findViewById(R.id.tv_like_sum);
        rvLikeUser = (RecyclerView) findViewById(R.id.rv_like_user);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvLikeUser.setLayoutManager(layoutManager);
        rvLikeUser.setHasFixedSize(true);
        likeUserListAdapter = new NewLikersImageAdapter(this);
        rvLikeUser.setAdapter(likeUserListAdapter);
        rlLikersArrow = (RelativeLayout) findViewById(R.id.rl_likers_arrow);

        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContentDate = (TextView) findViewById(R.id.tv_content_date);

        rvGoods = (RecyclerView) findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new FullyLinearLayoutManager(this));

        tflLinkTag = (TagFlowLayout) findViewById(R.id.tfl_link_tag);

        lvCommentsList = (AllListView) findViewById(R.id.lv_comments_list);

        rlFooter = (RelativeLayout) findViewById(R.id.rl_footer);
        rlLike = (RelativeLayout) findViewById(R.id.rl_like);
        tvLikeCount = (TextView) findViewById(R.id.tv_like_count);
        rlComments = (RelativeLayout) findViewById(R.id.rl_comments);
        tvCommentsCount = (TextView) findViewById(R.id.tv_comments_count);
        rlShareImage = (RelativeLayout) findViewById(R.id.rl_share_image);
        rlLikeImage = (RelativeLayout) findViewById(R.id.rl_like_image);
        rlCommentsImage = (RelativeLayout) findViewById(R.id.rl_comments_image);
        ivLike = (ImageView) findViewById(R.id.iv_like);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwudapei, "暂无搭配", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlRightLayout.setOnClickListener(this);
        llUser.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        rlLikersArrow.setOnClickListener(this);
        rlLike.setOnClickListener(this);
        rlComments.setOnClickListener(this);
        rlShareImage.setOnClickListener(this);
        rlLikeImage.setOnClickListener(this);
        rlCommentsImage.setOnClickListener(this);
    }

    @Override
    protected void init() {
        if (!NetUtil.IsInNetwork(this)) {
            setOffNetView(srlRefresh);
            return;
        }
        setSuccessView(srlRefresh);
        setSwipeRefresh();
    }

    /**
     * 设置下拉刷新，刷新后更新页面数据
     */
    public void setSwipeRefresh() {
        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetUtil.IsInNetwork(NewMatchDetailActivity.this)) {
                    srlRefresh.setRefreshing(false);
                    return;
                }
                loadData();
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

    /**
     * 加载数据
     */
    private void loadData() {
        getMatchDetails(workId);
        getCommentList(workId, 0);
    }

    /**
     * 获取购物车数量
     */
    private void initCarCount() {
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    tvCarCount.setVisibility(View.VISIBLE);
                    CarCountInfo info = (CarCountInfo) msg.obj;
                    tvCarCount.setText("" + info.getCount());
                    if (info.getCount() == 0) {
                        tvCarCount.setVisibility(View.GONE);
                    }
                } else {
                    tvCarCount.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            //跳转到购物车
            case R.id.toolbar_right_layout:
                goToCarView();
                break;
            //点击跳转到用户信息页面
            case R.id.ll_user:
                Intent intent1 = new Intent(this, CommunityHomePageActivity.class);
                intent1.putExtra("user_id", matchItem.getUser_id());
                if (BaseApplication.getInstance().readUserId().equals(String.valueOf(matchItem.getUser_id()))) {
                    intent1.putExtra("isCurUser", true);
                }
                startActivity(intent1);
                break;
            //点击关注或取消关注
            case R.id.btn_follow:
                bChanged = true;
                clickFollow();
                break;
            //主态 点击删除
            case R.id.iv_delete:
                deleteWork();
                break;
            //点击跳转到点赞详情页
            case R.id.rl_like:
            case R.id.rl_likers_arrow:
                Intent intent2 = new Intent(this, NewLikersActivity.class);
                intent2.putExtra("work_id", workId);
                startActivity(intent2);
                break;
            //点击点赞
            case R.id.rl_like_image:
                bChanged = true;
                setWorkLike(workId);
                break;
            //点击评论跳转到评论详情页
            case R.id.rl_comments:
            case R.id.rl_comments_image:
                Intent intent3 = new Intent(this, NewCommentsActivity.class);
                intent3.putExtra("work_id", workId);
                startActivityForResult(intent3, COMMENTS_REQUSET);
                break;
            //点击分享
            case R.id.rl_share_image:
                shareWorkDetail();
                break;
            //重新加载
            case R.id.tv_reload:
                init();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到购物车页面
     */
    private void goToCarView() {
        Intent intent = new Intent(NewMatchDetailActivity.this, NewShoppingCartActivity.class);
        startActivity(intent);
    }

    /**
     * 获取搭配详情  接口/work/detail
     *
     * @param workId
     */
    private void getMatchDetails(int workId) {
        final HashMap<String, Object> maps = new HashMap<>();
        maps.put("work_id", workId);
        new RetrofitRequest<WorkItem>(ApiRequest.getApiShiji().getMatchDetails(String.valueOf(workId))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            matchItem = (WorkItem) msg.obj;
                            if (matchItem == null) return;
                            if (listitem != null) {
                                matchItem.getUser().setFollowed(listitem.getUser().getFollowed());
                            }
                            updateViews(matchItem);
                        } else {
                            showTips(msg.message);
                        }
                        svScroll.scrollTo(0, 0);
                        srlRefresh.setRefreshing(false);
                        rlFooter.setVisibility(View.VISIBLE);
                        findViewById(R.id.ll_layout).setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    /**
     * 获取搭配详情数据后更新页面信息
     *
     * @param matchItem
     */
    private void updateViews(final WorkItem matchItem) {
        if (matchItem == null) return;
        /*设置用户头像 名称*/
        User user = matchItem.getUser();
        if (user != null) {
            ivUserAvatar.setImageURI(Util.transfer(user.getHead()));
            tvUserName.setText(user.getName());
            if (user.getRed() == 1) {
                ivRedPeople.setVisibility(View.VISIBLE);
                ivRedPeople.setText(Util.transferLevel(user.getLevel()));
            } else {
                ivRedPeople.setVisibility(View.GONE);
            }
        } else {
            ivRedPeople.setVisibility(View.GONE);
        }

        if (BaseApplication.getInstance().readUserId().equals(String.valueOf(matchItem.getUser_id()))) {
            ivDelete.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.GONE);
        } else {
            ivDelete.setVisibility(View.GONE);
            btnFollow.setVisibility(View.VISIBLE);
            //判断是否已关注
            if (user.getFollowed() == 1) {
                btnFollow.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnFollow.setLineMorphingState(1, false);
                    }
                }, 200);
            } else {
                btnFollow.setLineMorphingState(0, false);
            }
        }

        /*设置大图*/
        Netroid.displayImage(Util.transferCropImage(matchItem.getImages().get(0).getUrl(), layoutParams.width), ivTagImage);

        /*点赞人数 若无人则不显示此区域，有人则获取点赞人数列表 显示前7人*/
        if (matchItem.getLike_count() > 0) {
            llLikeUserList.setVisibility(View.VISIBLE);
            tvLikeSum.setText(matchItem.getLike_count() + "");
            getLikeUserList(workId, 0);
        } else {
            llLikeUserList.setVisibility(View.GONE);
            tvLikeSum.setText("0");
        }
        if (!TextUtils.isEmpty(matchItem.getContent())) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(matchItem.getContent());
        } else {
            tvContent.setVisibility(View.GONE);
        }
        tvContentDate.setText(matchItem.getCreate_time());

        goodsAdapter = new NewMatchGoodsAdapter(this, matchItem.getImages().get(0).getGoods_ids());
        rvGoods.setAdapter(goodsAdapter);

        final List<WorkItem.BrandsEntity> brands = matchItem.getBrands();
        if (brands == null || brands.size() == 0) {
            tflLinkTag.setVisibility(View.GONE);
        } else {
            tflLinkTag.setVisibility(View.VISIBLE);
            TagAdapter tagAdapter = new TagAdapter(brands) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    TextView tvHotWords = (TextView) LayoutInflater.from(NewMatchDetailActivity.this).inflate(R.layout.item_link_tag, parent, false);
                    tvHotWords.setText(brands.get(position).getName());
                    return tvHotWords;
                }
            };
            tflLinkTag.setAdapter(tagAdapter);
        }
        tflLinkTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(NewMatchDetailActivity.this, NewSingleBrandActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("brand_id", brands.get(position).getId());
                startActivity(intent);
                return false;
            }
        });

        tvLikeCount.setText(matchItem.getLike_count() + "");
        tvCommentsCount.setText(matchItem.getComment_count() + "");
        //判断是否已点赞
        ivLike.setEnabled(true);
        rlLikeImage.setEnabled(true);
        ivLike.setSelected(false);
        if (matchItem.getIsAgree() == 1) {
            ivLike.setEnabled(false);
            rlLikeImage.setEnabled(false);
            ivLike.setSelected(true);
        }
        svScroll.scrollTo(0, 0);
    }

    /**
     * 获取点赞人列表 接口/work/like-user-list
     *
     * @param workId
     */
    private void getLikeUserList(int workId, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("work_id", String.valueOf(workId));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(7));
        new RetrofitRequest<LikerObject>(ApiRequest.getApiShiji().likedUser(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            LikerObject obj = (LikerObject) msg.obj;
                            if (obj != null && obj.list != null && !obj.list.isEmpty()) {
                                llLikeUserList.setVisibility(View.VISIBLE);
                                likeUserListAdapter.setList(obj.list);
                                likeUserListAdapter.notifyDataSetChanged();
                            } else {
                                llLikeUserList.setVisibility(View.GONE);
                            }
                        } else showTips(msg.message);

                        svScroll.scrollTo(0, 0);
                    }
                }
        );
    }

    /**
     * 获取评论列表
     *
     * @param workId
     */
    private void getCommentList(int workId, int offset) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("work_id", String.valueOf(workId));
        maps.put("offset", String.valueOf(offset));
        maps.put("limit", String.valueOf(5));
        new RetrofitRequest<CommentObject>(ApiRequest.getApiShiji().getWorkCommentList(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            CommentObject obj = (CommentObject) msg.obj;
                            if (obj.list != null && obj.list.size() > 0) {
                                lvCommentsList.setVisibility(View.VISIBLE);
                                findViewById(R.id.viewline).setVisibility(View.VISIBLE);
                                commentAdapter = new NewWorkCommentsAdapter(NewMatchDetailActivity.this, obj.list);
                                lvCommentsList.setAdapter(commentAdapter);
                            } else {
                                lvCommentsList.setVisibility(View.GONE);
                                findViewById(R.id.viewline).setVisibility(View.GONE);
                            }
                        }
                        svScroll.scrollTo(0, 0);
                    }
                }
        );
    }

    /**
     * 点击关注按钮，关注或取消关注
     */
    private void clickFollow() {
        if (matchItem == null || matchItem.getUser() == null)
            return;
        btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
        if (matchItem.getUser().getFollowed() == 1)
            unFollowUser(matchItem.getUser_id());
        else
            followUser(matchItem.getUser_id());
    }

    /**
     * 关注用户 接口 /user/follow-user
     *
     * @param userid
     */
    private void followUser(int userid) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(userid))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    matchItem.getUser().setFollowed(1);
                    showTips("关注");
                } else if (msg.isLossLogin()) {
                    btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                    Intent intent = new Intent(NewMatchDetailActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, LOGIN_RESULT);
                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                } else {
                    showTips("关注失败");
                    btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                }
            }
        });
    }

    /**
     * 取消关注用户 接口/user/unfollow-user
     *
     * @param userid
     */
    private void unFollowUser(int userid) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(userid))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            matchItem.getUser().setFollowed(2);
                            showTips("取消关注");
                        } else if (msg.isLossLogin()) {
                            btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                            Intent intent = new Intent(NewMatchDetailActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, LOGIN_RESULT);
                            overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                        } else {
                            showTips("取消关注失败");
                            btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                        }
                    }
                }
        );
    }

    /**
     * 点赞
     *
     * @param workId
     */
    private void setWorkLike(final int workId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("work_id", workId);
        new RetrofitRequest<>(ApiRequest.getApiShiji().setWorkLike(String.valueOf(workId))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            matchItem.setIsAgree(1);
                            ivLike.setSelected(true);
                            ivLike.setEnabled(false);
                            rlLikeImage.setEnabled(false);
                            matchItem.setLike_count(matchItem.getLike_count() + 1);
                            tvLikeSum.setText(matchItem.getLike_count() + "");
                            tvLikeCount.setText(matchItem.getLike_count() + "");
                            getLikeUserList(workId, 0);
                        } else if (msg.isLossLogin()) {
                            btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                            Intent intent = new Intent(NewMatchDetailActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, LOGIN_RESULT);
                            overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                        } else showTips("点赞失败");
                    }
                }
        );
    }

    /**
     * 分享
     */
    private void shareWorkDetail() {
        if (matchItem != null) {
            String title = matchItem.getUser().getName() + "的搭配";
            String imageurl = Util.transfer(matchItem.getImages().get(0).getUrl());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            String date = sDateFormat.format(new java.util.Date());
            String url = "http://h5.qnmami.com/collocation/html/index.html?id=" + matchItem.getWork_id() + "&r=" + date;
            String content = matchItem.getContent();
            new ShareDialog(this, title, imageurl, url, content).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LOGIN_RESULT) {
                setSwipeRefresh();
                Util.getNewUserPullLayer(this, data);
            } else if (requestCode == COMMENTS_REQUSET) {
                if (data.getBooleanExtra("submit", false)) {
                    bChanged = true;
                    setSwipeRefresh();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (bDelete) {
            intent.putExtra("delete", true);
            intent.putExtra("position", position);
            setResult(Activity.RESULT_OK, intent);

        } else if (bChanged && position != -1 && !TextUtils.isEmpty(data)) {

            intent.putExtra("position", position);
            intent.putExtra("data", new Gson().toJson(matchItem));
            setResult(Activity.RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCarCount();
    }

    /**
     * 删除作品
     */
    private void deleteWork() {

        showCustomMutiDialog("确定删除此购物笔记？", new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                new RetrofitRequest<>(ApiRequest.getApiShiji().deleteWork(workId)).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            bDelete = true;
                            showTips("删除成功");
                            onBackPressed();
                        } else if (msg.isLossLogin()) {
                            Intent intent = new Intent(NewMatchDetailActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivityForResult(intent, LOGIN_RESULT);
                            overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                        } else {
                            showTips("删除失败");
                        }
                    }
                });
            }

            @Override
            public void CancelClick() {

            }
        });
    }
}

