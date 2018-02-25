package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.NewCommentsActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewLikersActivity;
import cn.yiya.shiji.activity.NewMainActivity;
import cn.yiya.shiji.activity.NewMatchDetailActivity;
import cn.yiya.shiji.activity.WebViewActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.BannerObject;
import cn.yiya.shiji.entity.LogstashReport.MatchGoodsLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MatchLogstashItem;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.WorkImage;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;


/**
 * 搭配列表 Adapter
 * Created by Amy on 2016/7/11.
 */
public class NewMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<WorkItem> mList;
    private CustomActionListener mListener;
    private Handler mHandler;

    public static final int BANNER = 11;
    public static final int MATCHLIST = 22;
    private int selectType = 0;  //0推荐 1关注

    private ConvenientBanner banner;

    public NewMatchAdapter(Context mContext, ArrayList<WorkItem> mList, CustomActionListener mListener, int selectType) {
        this.mContext = mContext;
        mHandler = new Handler(mContext.getMainLooper());
        this.mList = mList;
        this.mListener = mListener;
        this.selectType = selectType;
    }

    public ArrayList<WorkItem> getList() {
        return this.mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:
                return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_new_recommend, parent, false));
            case MATCHLIST:
                return new MatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_match, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MatchViewHolder) {
            MatchViewHolder mHolder = (MatchViewHolder) holder;
            WorkItem item;
            if (selectType == 0) {
                item = mList.get(position - 1);
            } else {
                item = mList.get(position);
            }
            updateViews(mHolder, item);
            initEvents(mHolder, item, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (selectType == 0) { //推荐
            if (position == 0) return BANNER;
            else if (position > 0) return MATCHLIST;
            else return 0;
        } else if (selectType == 1) { //关注
            return MATCHLIST;
        } else
            return 0;
    }

    @Override
    public int getItemCount() {
        if (selectType == 0) {  //推荐
            return mList == null ? 1 : (mList.size() + 1);
        } else {
            return mList == null ? 0 : mList.size();
        }
    }

    /*===============================广告栏=============================================*/
    class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(final View itemView) {
            super(itemView);
            banner = (ConvenientBanner) itemView.findViewById(R.id.banner);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = layoutParams.width * 159 / 375;
            banner.setLayoutParams(layoutParams);
            banner.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
            banner.startTurning(4000);
            new RetrofitRequest<BannerObject>(ApiRequest.getApiShiji().getMatchBannerList()).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        BannerObject object = (BannerObject) msg.obj;
                        if (object.list != null && object.list.size() > 0) {
                            ArrayList<BannerItem> list = object.list;
                            if (list.size() == 1) {
                                banner.setCanLoop(false);
                                banner.setManualPageable(false);
                                banner.setPointViewVisible(false);
                            }
                            banner.setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.viewline).setVisibility(View.VISIBLE);
                            banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                                @Override
                                public NetworkImageHolderView createHolder() {
                                    return new NetworkImageHolderView();
                                }
                            }, list);
                        } else {
                            banner.setVisibility(View.GONE);
                            itemView.findViewById(R.id.viewline).setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
    }

    class NetworkImageHolderView implements Holder<BannerItem> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final BannerItem item) {
            int width = SimpleUtils.getScreenWidth(mContext);
            int height = width * 159 / 375;
            BitmapTool.clipShowImageView(item.getImage(), imageView, R.drawable.user_dafault, width, height);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getUrl().endsWith(".apk")) {
                        Util.downloadApp(mContext, item.getUrl(), item.getUrl().substring(item.getUrl().lastIndexOf("/"), item.getUrl().length() - 4));
                    } else {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("imageurl", item.getImage());
                        intent.putExtra("url", item.getUrl() + "&in_app=2");
                        intent.putExtra("title", item.getTitle());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }



     /*===============================推荐关注列表=============================================*/

    class MatchViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llUser;                               //显示用户头像和名称
        SimpleDraweeView ivUserAvatar;                     //显示用户头像
        TextView tvUserName;                               //显示用户名称
        TextView ivRedPeople;                             //红人标志
        ImageView btnFollow;                    //“关注”按钮

        LinearLayout llTag;
        RelativeLayout rlTag;
        SimpleDraweeView ivTagImage;                          //布局中最大的图片
        ListView lvGoods;

        RelativeLayout rlLike;                             //点赞人数布局
        TextView tvLikeCount;                              //显示点赞人数
        RelativeLayout rlComments;                         //评论人数布局
        TextView tvCommentsCount;                          //显示评论人数
        ImageView ivShare;                                 //“分享”按钮
        ImageView ivLike;                                  //“点赞”按钮
        ImageView ivComments;                              //“评论”按钮
        RelativeLayout rlShareImage;                       //右下分享
        RelativeLayout rlLikeImage;                        //右下点赞
        RelativeLayout rlCommentsImage;                    //右下评论

        public MatchViewHolder(View itemView) {
            super(itemView);
            llUser = (LinearLayout) itemView.findViewById(R.id.ll_user);
            ivUserAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_user_avatar);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            ivRedPeople = (TextView) itemView.findViewById(R.id.iv_red_people);
            btnFollow = (ImageView) itemView.findViewById(R.id.btn_follow);

            llTag = (LinearLayout) itemView.findViewById(R.id.ll_tag);

            rlTag = (RelativeLayout) itemView.findViewById(R.id.rl_tag);
            ivTagImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_tag_image);

            lvGoods = (ListView) itemView.findViewById(R.id.lv_goods);

            rlLike = (RelativeLayout) itemView.findViewById(R.id.rl_like);
            tvLikeCount = (TextView) itemView.findViewById(R.id.tv_like_count);
            rlComments = (RelativeLayout) itemView.findViewById(R.id.rl_comments);
            tvCommentsCount = (TextView) itemView.findViewById(R.id.tv_comments_count);
            ivShare = (ImageView) itemView.findViewById(R.id.iv_share);
            ivLike = (ImageView) itemView.findViewById(R.id.iv_like);
            ivComments = (ImageView) itemView.findViewById(R.id.iv_comments);
            rlShareImage = (RelativeLayout) itemView.findViewById(R.id.rl_share_image);
            rlLikeImage = (RelativeLayout) itemView.findViewById(R.id.rl_like_image);
            rlCommentsImage = (RelativeLayout) itemView.findViewById(R.id.rl_comments_image);
        }
    }

    /**
     * 更新页面信息
     *
     * @param holder
     * @param item
     */
    private void updateViews(final MatchViewHolder holder, final WorkItem item) {
        if (item == null) return;
        /*设置用户头像 名称*/
        final User user = item.getUser();
        if (user != null) {
            holder.ivUserAvatar.setImageURI(Util.transfer(user.getHead()));
            holder.tvUserName.setText(user.getName());
            if (user.getRed() == 1) {
                holder.ivRedPeople.setVisibility(View.VISIBLE);
                holder.ivRedPeople.setText(Util.transferLevel(user.getLevel()));
            } else {
                holder.ivRedPeople.setVisibility(View.GONE);
            }
        } else {
            holder.ivRedPeople.setVisibility(View.GONE);
        }
        //判断是否已关注
        if (user.getFollowed() == 1) {
            holder.btnFollow.setImageResource(R.mipmap.ic_followed);
        } else {
            holder.btnFollow.setImageResource(R.mipmap.ic_tofollow);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llTag.getLayoutParams();
        layoutParams.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 2) * 2;
        layoutParams.height = layoutParams.width;
        holder.llTag.setLayoutParams(layoutParams);

        holder.ivTagImage.setImageURI(Util.transferCropImage(item.getImages().get(0).getUrl(), layoutParams.width / 3));
       /*设置右侧是三个图*/
        List<WorkImage.GoodsEntity> goodsList = item.getImages().get(0).getGoods_ids();
        if (goodsList.size() > 3) {
            goodsList = goodsList.subList(0, 3);
        }
        NewMatchGoodsImageAdapter goodsAdapter = new NewMatchGoodsImageAdapter(mContext, goodsList);
        holder.lvGoods.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemClickListener(new NewMatchGoodsImageAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String goodsId, String recommend) {
                // 打点上报
                MatchGoodsLogstashItem logstashItem = new MatchGoodsLogstashItem();
                logstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                logstashItem.setGoods_id(goodsId);
                new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMatchGoodsEvent(logstashItem)).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                    }
                });

                if (!NetUtil.IsInNetwork(mContext)) {
                    Util.toast(mContext, Configration.OFF_LINE_TIPS, true);
                } else {
                    if (((NewMainActivity) mContext).bLogin) {
                        gotoGoodsDetail(goodsId);
                    } else {
                        if (mListener != null)
                            mListener.goLogin();
                    }
                }
            }
        });

        //点赞数和评论数
        holder.tvLikeCount.setText(item.getLike_count() + "");
        holder.tvCommentsCount.setText(item.getComment_count() + "");
        //判断是否已点赞
        holder.rlLikeImage.setEnabled(true);
        holder.ivLike.setEnabled(true);
        holder.ivLike.setSelected(false);
        if (item.getIsAgree() == 1) {
            holder.rlLikeImage.setEnabled(false);
            holder.ivLike.setEnabled(false);
            holder.ivLike.setSelected(true);
        }
    }

    /**
     * 监听事件
     *
     * @param holder
     * @param item
     * @param position
     */
    private void initEvents(final MatchViewHolder holder, final WorkItem item, final int position) {
        //点击赞跳转到点赞列表
        holder.rlLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewLikersActivity.class);
                intent.putExtra("work_id", item.getWork_id());
                mContext.startActivity(intent);
            }
        });

        //点击评论跳转到评论列表
        holder.rlComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到评论列表
                goToComments(position, item);
            }
        });


        //点击跳转到用户信息页面
        holder.llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommunityHomePageActivity.class);
                intent.putExtra("user_id", item.getUser_id());
                if (BaseApplication.getInstance().readUserId().equals(String.valueOf(item.getUser_id()))) {
                    intent.putExtra("isCurUser", true);
                }
                int followed = item.getUser().getFollowed();
                if (followed == 1) {
                    intent.putExtra("is_follow", true);
                } else {
                    intent.putExtra("is_follow", false);
                }
                mContext.startActivity(intent);
            }
        });

        //点击关注或取消关注
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFollow(holder, item);
            }
        });

        //点击大图跳转到详情页
        holder.rlTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打点上报
                MatchLogstashItem logstashItem = new MatchLogstashItem();
                logstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                logstashItem.setId(String.valueOf(item.getWork_id()));
                new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMatchEvent(logstashItem)).handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                    }
                });

                goToDetail(position, item);
            }
        });

        //点击点赞
        holder.rlLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWorkLike(holder, item);
            }
        });

        //点击评论跳转到评论列表
        holder.rlCommentsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到评论列表
                goToComments(position, item);
            }
        });

        //点击分享
        holder.rlShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWorkDetail(item);
            }
        });

    }

    /**
     * 跳转到商品详情
     *
     * @param goodsId
     */
    private void gotoGoodsDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到详情页
     *
     * @param position
     * @param item
     */
    private void goToDetail(final int position, final WorkItem item) {
        Intent intent = new Intent(mContext, NewMatchDetailActivity.class);
        intent.putExtra("work_id", item.getWork_id());
        intent.putExtra("position", position);
        intent.putExtra("data", new Gson().toJson(item));
        if (mListener != null) mListener.goToDetail(intent);
    }

    /**
     * 跳转到评论列表
     *
     * @param position
     * @param item
     */
    private void goToComments(final int position, final WorkItem item) {
        Intent intent = new Intent(mContext, NewCommentsActivity.class);
        intent.putExtra("work_id", item.getWork_id());
        intent.putExtra("position", position);
        intent.putExtra("data", new Gson().toJson(item));
        if (mListener != null) mListener.goToCommnets(intent);
    }

    /**
     * 点击关注按钮，关注或取消关注
     */
    private void clickFollow(final MatchViewHolder holder, WorkItem item) {
        if (item.getUser().getFollowed() == 1) {
            unFollowUser(holder, item);
        } else {
            followUser(holder, item);
        }
    }

    /**
     * 关注用户 接口 /user/follow-user
     *
     * @param holder
     * @param item
     */
    private void followUser(final MatchViewHolder holder, final WorkItem item) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    holder.btnFollow.setImageResource(R.mipmap.ic_followed);
                    item.getUser().setFollowed(1);
                    item.getUser().setIs_follow(true);
                    mListener.setFollow(item);
                    Util.toast(mContext, "关注", true);
                } else if (msg.isLossLogin()) {
                    holder.btnFollow.setImageResource(R.mipmap.ic_tofollow);
                    if (mListener != null)
                        mListener.goLogin();
                } else {
                    holder.btnFollow.setImageResource(R.mipmap.ic_tofollow);
                    Util.toast(mContext, "关注失败", true);
                }
            }
        });
    }

    /**
     * 取消关注用户 接口/user/unfollow-user
     *
     * @param item
     */
    private void unFollowUser(final MatchViewHolder holder, final WorkItem item) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    holder.btnFollow.setImageResource(R.mipmap.ic_tofollow);
                    item.getUser().setFollowed(2);
                    item.getUser().setIs_follow(false);
                    mListener.setFollow(item);
                    Util.toast(mContext, "取消关注", true);
                } else if (msg.isLossLogin()) {
                    holder.btnFollow.setImageResource(R.mipmap.ic_followed);
                    if (mListener != null)
                        mListener.goLogin();
                } else {
                    holder.btnFollow.setImageResource(R.mipmap.ic_followed);
                    Util.toast(mContext, "取消关注失败", true);
                }
            }
        });
    }

    /**
     * 点赞
     *
     * @param holder
     * @param item
     */
    private void setWorkLike(final MatchViewHolder holder, final WorkItem item) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setWorkLike(String.valueOf(item.getWork_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    item.setIsAgree(1);
                    holder.ivLike.setSelected(true);
                    holder.rlLikeImage.setEnabled(false);
                    holder.ivLike.setEnabled(false);
                    item.setLike_count(item.getLike_count() + 1);
                    holder.tvLikeCount.setText(item.getLike_count() + "");
                } else if (msg.isLossLogin()) {
                    if (mListener != null)
                        mListener.goLogin();
                } else {
                    Util.toast(mContext, "点赞失败", true);
                }
            }
        });
    }

    /**
     * 分享
     */
    private void shareWorkDetail(WorkItem item) {
        if (item != null) {
            String title = item.getUser().getName() + "的搭配";
            String imageurl = Util.transfer(item.getImages().get(0).getUrl());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            String date = sDateFormat.format(new java.util.Date());
            String url = "http://h5.qnmami.com/collocation/html/index.html?id=" + item.getWork_id() + "&r=" + date;
            String content = item.getContent();
            new ShareDialog(mContext, title, imageurl, url, content).build().show();
        }
    }

    public interface CustomActionListener {
        void goLogin();

        void setFollow(WorkItem item);

        void goToDetail(Intent intent);

        void goToCommnets(Intent intent);
    }
}
