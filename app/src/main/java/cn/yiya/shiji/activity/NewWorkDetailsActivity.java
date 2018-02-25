package cn.yiya.shiji.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewLikersImageAdapter;
import cn.yiya.shiji.adapter.NewWorkCommentsAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.CommentObject;
import cn.yiya.shiji.entity.LikerObject;
import cn.yiya.shiji.entity.TagTypeInfo;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.WorkGoodsObject;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.entity.WorkTag;
import cn.yiya.shiji.entity.goodes.NewGoodsItem;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.DebugUtil;
import cn.yiya.shiji.utils.MyPreference;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.ShareDialog;
import cn.yiya.shiji.views.TagAbleImageView;
import cn.yiya.shiji.views.TagView;

import static cn.yiya.shiji.R.id.item;

/**
 * 新改版的笔记详情
 * Created by Amy on 2016/5/26.
 */
public class NewWorkDetailsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private int workId;
    private String goodsId;
    private WorkItem mWorkItem;
    private int position = -1;
    private String data;
    private WorkItem listitem;
    private boolean bChanged = false;

    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView ivBack;
    private RelativeLayout rlRightLayout;
    private TextView tvCarCount;

    private NestedScrollView svScroll;
    private LinearLayout llUser;
    private ImageView ivRedPeople;
    private SimpleDraweeView ivUserAvatar;
    private TextView tvUserName;
    private FloatingActionButton btnFollow;
    private ImageView ivDelete;               //主态删除按钮

    private TagAbleImageView ivTagImage;
    private LinearLayout llTag;
    private TextView tvTagBrand;

    private LinearLayout llLikeUserList;
    private TextView tvLikeSum;
    private RecyclerView rvLikeUser;
    private RelativeLayout rlLikersArrow;
    private NewLikersImageAdapter likeUserListAdapter;

    private TextView tvContent;
    private TagFlowLayout tflLinkTag;
    private TextView tvContentDate;
    private RelativeLayout rlGoods;
    private ImageView ivGoodsImage;
    private TextView tvGoodsBrand;
    private TextView tvGoodsSite;
    private TextView tvGoodsPrice;

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

    private LinearLayout llytTips;
    private static final int LOGIN_RESULT = 100;
    private static final int COMMENTS_REQUSET = 111;

    private boolean bDelete = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_work_details);
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
                goodsId = intent.getStringExtra("goodsId");
                position = intent.getIntExtra("position", -1);
                data = intent.getStringExtra("data");
                if (!TextUtils.isEmpty(data)) {
                    listitem = new Gson().fromJson(data, WorkItem.class);
                }
            } else {
                DebugUtil.e("scheme:" + uri.getScheme());
                DebugUtil.e("host:" + uri.getHost());
                DebugUtil.e("path:" + uri.getPath());
                DebugUtil.e("query:" + uri.getQuery());
                workId = Integer.valueOf(uri.getQueryParameter("id"));
            }

        }
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTitle = (TextView) findViewById(R.id.toolbar_middle_txt);
        tvTitle.setText("笔记详情");
        ivBack = (ImageView) findViewById(R.id.toolbar_left);
        rlRightLayout = (RelativeLayout) findViewById(R.id.toolbar_right_layout);
        tvCarCount = (TextView) findViewById(R.id.toolbar_right_count);
        rlRightLayout.setVisibility(View.VISIBLE);

        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        svScroll = (NestedScrollView) findViewById(R.id.sv_scroll);

        llUser = (LinearLayout) findViewById(R.id.ll_user);
        ivUserAvatar = (SimpleDraweeView) findViewById(R.id.iv_user_avatar);
        ivRedPeople = (ImageView) findViewById(R.id.iv_red_people);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        btnFollow = (FloatingActionButton) findViewById(R.id.btn_follow);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);

        ivTagImage = (TagAbleImageView) findViewById(R.id.iv_tag_image);
        llTag = (LinearLayout) findViewById(R.id.ll_tag);
        tvTagBrand = (TextView) findViewById(R.id.tv_tag_brand);

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
        tflLinkTag = (TagFlowLayout) findViewById(R.id.tfl_link_tag);
        tvContentDate = (TextView) findViewById(R.id.tv_content_date);

        rlGoods = (RelativeLayout) findViewById(R.id.rl_goods);
        ivGoodsImage = (ImageView) findViewById(R.id.iv_goods_image);
        tvGoodsBrand = (TextView) findViewById(R.id.tv_goods_brand);
        tvGoodsSite = (TextView) findViewById(R.id.tv_goods_site);
        tvGoodsPrice = (TextView) findViewById(R.id.tv_goods_price);

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

        llytTips = (LinearLayout) findViewById(R.id.llyt_tips);

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwugouwubiji, "暂无购物笔记", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        rlRightLayout.setOnClickListener(this);
        llUser.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        llTag.setOnClickListener(this);
        rlGoods.setOnClickListener(this);
        rlLike.setOnClickListener(this);
        rlShareImage.setOnClickListener(this);
        rlLikeImage.setOnClickListener(this);
        rlComments.setOnClickListener(this);
        rlCommentsImage.setOnClickListener(this);
        rlLikersArrow.setOnClickListener(this);
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
        getWorkDetails(workId);
        getCommentList(workId, 0);
        if (!TextUtils.isEmpty(goodsId)) {
            rlGoods.setVisibility(View.VISIBLE);
            getGoodsProfile(goodsId);
        } else {
            rlGoods.setVisibility(View.GONE);
        }
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
                intent1.putExtra("user_id", mWorkItem.getUser_id());
                if (BaseApplication.getInstance().readUserId().equals(String.valueOf(mWorkItem.getUser_id()))) {
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
            //点击跳转到商品详情页面
            case R.id.ll_tag:
            case R.id.rl_goods:
                goToGoodsDetail();
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
        Intent intent = new Intent(NewWorkDetailsActivity.this, NewShoppingCartActivity.class);
        startActivity(intent);
    }

    /**
     * 获取笔记详情  接口/work/detail
     *
     * @param workId
     */
    private void getWorkDetails(int workId) {
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("work_id", workId);
        new RetrofitRequest<WorkItem>(ApiRequest.getApiShiji().getMatchDetails(String.valueOf(workId))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            mWorkItem = (WorkItem) msg.obj;
                            if (mWorkItem == null) return;
                            if (listitem != null) {
                                mWorkItem.getUser().setFollowed(listitem.getUser().getFollowed());
                            }
                            updateViews(mWorkItem);
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
     * 获取笔记详情数据后更新页面信息
     *
     * @param workitem
     */
    private void updateViews(final WorkItem workitem) {
        if (workitem == null) return;
        /*设置用户头像 名称*/
        User user = workitem.getUser();
        if (user != null) {
            ivUserAvatar.setImageURI(Util.transfer(user.getHead()));
            tvUserName.setText(user.getName());
            if (user.getRed() == 1) {
                ivRedPeople.setVisibility(View.VISIBLE);
            } else {
                ivRedPeople.setVisibility(View.GONE);
            }
        } else {
            ivRedPeople.setVisibility(View.GONE);
        }
        if (BaseApplication.getInstance().readUserId().equals(String.valueOf(workitem.getUser_id()))) {
            ivDelete.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.GONE);
        } else {
            ivDelete.setVisibility(View.GONE);
            btnFollow.setVisibility(View.VISIBLE);
            //判断是否已关注
            if (user.is_follow()) {
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

        /*设置商品大图*/
        Netroid.displayImage(workitem.getImages().get(0).getUrl(), ivTagImage.getImageView());
        ArrayList<TagAbleImageView.TagInfo> tagInfos = workitem.getImages().get(0).getTagInfos();
        ivTagImage.initTags(null, 0);
        ivTagImage.addTag(tagInfos, 2);
        ivTagImage.setCustomEventListener(new TagAbleImageView.CustomEventListener() {
            @Override
            public void onAddTag() {

            }

            @Override
            public void onTagTextClick(TagView tagView) {
                //点击tag跳转到品牌官网
                ArrayList<WorkTag> taglist = workitem.getImages().get(0).getTags();
                int tagId = 0;
                int tagType = 0;
                String tagContent = "";
                for (int i = 0; i < taglist.size(); i++) {
                    WorkTag item = taglist.get(i);
                    if (item.getType() == 1) {
                        tagId = item.getTag_id();
                        tagType = item.getType();
                        tagContent = item.getContent();
                    }
                }
                getBrandId(tagId, tagType, tagContent);
            }

            @Override
            public void onTagLinearClick(TagView tagView) {
                ArrayList<WorkTag> taglist = workitem.getImages().get(0).getTags();
                int tagId = 0;
                int tagType = 0;
                String tagContent = "";
                for (int i = 0; i < taglist.size(); i++) {
                    WorkTag item = taglist.get(i);
                    if (item.getType() == 3) {
                        tagId = item.getTag_id();
                        tagType = item.getType();
                        tagContent = item.getContent();
                    }
                }
                getBrandId(tagId, tagType, tagContent);
            }

            @Override
            public void onTagLongClick(TagView tagView) {

            }
        });
        if (workitem.getImages().get(0).getGoods_id() == null) {
            llTag.setVisibility(View.GONE);
        } else {
            llTag.setVisibility(View.VISIBLE);
            if (tagInfos == null || tagInfos.size() == 0) {
                llTag.setVisibility(View.GONE);
            }
            for (TagAbleImageView.TagInfo tag : tagInfos) {
                if (tag.getType() == 1) {
                    String tagContent = tag.getContent() + " " + tag.getDetails();
                    if (TextUtils.isEmpty(tagContent.trim())) {
                        llTag.setVisibility(View.GONE);
                    } else {
                        tvTagBrand.setText(tagContent.trim());
                        addGuide(llytTips, MyPreference.WORK_DETAIL_GOODS_GUIDE);
                    }
                }
            }
        }

        /*点赞人数 若无人则不显示此区域，有人则获取点赞人数列表 显示前7人*/
        if (workitem.getLike_count() > 0) {
            llLikeUserList.setVisibility(View.VISIBLE);
            tvLikeSum.setText(workitem.getLike_count() + "");
            getLikeUserList(workId, 0);
        } else {
            llLikeUserList.setVisibility(View.GONE);
            tvLikeSum.setText("0");
        }

        if (!TextUtils.isEmpty(workitem.getContent())) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(workitem.getContent());
        } else {
            tvContent.setVisibility(View.GONE);
        }
        tvContentDate.setText(workitem.getCreate_time());

        final ArrayList<WorkTag> tags = workitem.getImages().get(0).getTags();
        if (tags == null || tags.size() == 0) {
            tflLinkTag.setVisibility(View.GONE);
        } else {
            tflLinkTag.setVisibility(View.VISIBLE);
            TagAdapter tagAdapter = new TagAdapter(tags) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    TextView tvHotWords = (TextView) LayoutInflater.from(NewWorkDetailsActivity.this).inflate(R.layout.item_link_tag, parent, false);
                    tvHotWords.setText("#" + tags.get(position).getContent());
                    if (tags.get(position).getType() == 2) tvHotWords.setVisibility(View.GONE);
                    return tvHotWords;
                }
            };
            tflLinkTag.setAdapter(tagAdapter);
        }
        tflLinkTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //点击#关键词跳转到关键词页面
                int tagId = tags.get(position).getTag_id();
                int tagType = tags.get(position).getType();
                String tagContent = tags.get(position).getContent();
                getBrandId(tagId, tagType, tagContent);
                return false;
            }
        });

        tvLikeCount.setText(workitem.getLike_count() + "");
        tvCommentsCount.setText(workitem.getComment_count() + "");
        //判断是否已点赞
        ivLike.setEnabled(true);
        rlLikeImage.setEnabled(true);
        ivLike.setSelected(false);
        if (workitem.getIsAgree() == 1) {
            ivLike.setEnabled(false);
            rlLikeImage.setEnabled(false);
            ivLike.setSelected(true);
        }
        svScroll.scrollTo(0, 0);
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
                                commentAdapter = new NewWorkCommentsAdapter(NewWorkDetailsActivity.this, obj.list);
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
        if (mWorkItem == null || mWorkItem.getUser() == null)
            return;
        btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
        if (!mWorkItem.getUser().is_follow())
            followUser(mWorkItem.getUser_id());
        else
            unFollowUser(mWorkItem.getUser_id());
    }

    /**
     * 关注用户 接口 /user/follow-user
     *
     * @param userid
     */
    private void followUser(int userid) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(userid))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            mWorkItem.getUser().setFollowed(1);
                            showTips("关注");
                        } else if (msg.isLossLogin()) {
                            btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                            Intent intent = new Intent(NewWorkDetailsActivity.this, LoginActivity.class);
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
                            mWorkItem.getUser().setFollowed(2);
                            showTips("取消关注");
                        } else if (msg.isLossLogin()) {
                            btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                            Intent intent = new Intent(NewWorkDetailsActivity.this, LoginActivity.class);
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
                    }
                }
        );
    }

    /**
     * 获取某一商品概要信息  四期新增接口/shop/goods/goods-profile
     *
     * @param goodsId
     */
    private void getGoodsProfile(String goodsId) {
        new RetrofitRequest<WorkGoodsObject>(ApiRequest.getApiShiji().getGoodsProfileList(goodsId)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            WorkGoodsObject obj = (WorkGoodsObject) msg.obj;
                            if (obj != null && obj.getGoods() != null) {
                                NewGoodsItem item = obj.getGoods();
                                Netroid.displayImage(Util.transfer(item.getCover()), ivGoodsImage);
                                tvGoodsBrand.setText(item.getBrand() + item.getTitle());
                                tvGoodsSite.setText(item.getSite());
                                tvGoodsPrice.setText("¥" + item.getPrice());
                            }
                        } else showTips(msg.message);
                        svScroll.scrollTo(0, 0);
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
                            mWorkItem.setIsAgree(1);
                            ivLike.setSelected(true);
                            ivLike.setEnabled(false);
                            rlLikeImage.setEnabled(false);
                            mWorkItem.setLike_count(mWorkItem.getLike_count() + 1);
                            tvLikeSum.setText(mWorkItem.getLike_count() + "");
                            tvLikeCount.setText(mWorkItem.getLike_count() + "");
                            getLikeUserList(workId, 0);
                        } else if (msg.isLossLogin()) {
                            btnFollow.setLineMorphingState((btnFollow.getLineMorphingState() + 1) % 2, true);
                            Intent intent = new Intent(NewWorkDetailsActivity.this, LoginActivity.class);
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
        if (mWorkItem != null) {
            String title = mWorkItem.getUser().getName() + "的购物笔记";
            String imageurl = Util.transfer(mWorkItem.getImages().get(0).getUrl());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            String date = sDateFormat.format(new java.util.Date());
            String url = "http://h5.qnmami.com/html/note/index.html?id=" + mWorkItem.getWork_id() + "&r=" + date;
            String content = mWorkItem.getContent();
            new ShareDialog(this, title, imageurl, url, content).build().show();
        }
    }

    /**
     * 根据tag获取品牌跳转到品牌页，未获取到则跳转到标签页
     *
     * @param tagId
     */
    private void getBrandId(final int tagId, final int tagType, final String tagContent) {
        new RetrofitRequest<TagTypeInfo>(ApiRequest.getApiShiji().getTagBrand(String.valueOf(tagId))).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        int brandId = 0;
                        if (msg.data != null) {
                            TagTypeInfo tag = new Gson().fromJson(msg.data, TagTypeInfo.class);
                            brandId = tag.getId();
                        }
                        if (brandId > 0) {
                            Intent intent = new Intent(NewWorkDetailsActivity.this, NewSingleBrandActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("brand_id", brandId);
                            startActivity(intent);
                        } else {
                            if (tagType == 4) {
                                Intent intent = new Intent(NewWorkDetailsActivity.this, ActTagActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("tag_id", tagId);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(NewWorkDetailsActivity.this, CommonTagActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("tag_id", tagId);
                                intent.putExtra("tag_content", tagContent);
                                startActivity(intent);
                            }
                        }
                    }
                }
        );
    }

    /**
     * 跳转到商品详情页
     */
    private void goToGoodsDetail() {
//        GoodsIdInfo idInfo = new GoodsIdInfo();
//        idInfo.setGoodsId(mWorkItem.getImages().get(0).getGoods_id());
//        idInfo.setRecommend(mWorkItem.getImages().get(0).getRecommend());
//        Intent intent = new Intent(this, GoodsDetailActivity.class);
//        intent.putExtra("data", new Gson().toJson(idInfo));
//        startActivity(intent);
        Intent intent = new Intent(this, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", mWorkItem.getImages().get(0).getGoods_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
            setResult(RESULT_OK, intent);

        } else if (bChanged && position != -1 && !TextUtils.isEmpty(data)) {

            intent.putExtra("position", position);
            intent.putExtra("data", new Gson().toJson(mWorkItem));
            setResult(RESULT_OK, intent);
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
                            Intent intent = new Intent(NewWorkDetailsActivity.this, LoginActivity.class);
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
