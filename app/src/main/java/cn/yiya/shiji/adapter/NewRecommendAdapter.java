package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.ActTagActivity;
import cn.yiya.shiji.activity.CommonTagActivity;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.NewCommentsActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewLikersActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.activity.NewWorkDetailsActivity;
import cn.yiya.shiji.activity.WebViewActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.entity.BannerItem;
import cn.yiya.shiji.entity.BannerObject;
import cn.yiya.shiji.entity.TagTypeInfo;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.entity.WorkTag;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.BitmapTool;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ShareDialog;
import cn.yiya.shiji.views.TagAbleImageView;
import cn.yiya.shiji.views.TagView;

/**
 * Created by Amy on 2016/6/15.
 */
public class NewRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Handler mHandler;
    public static final int BANNER = 11;
    public static final int RECOMMENDLIST = 22;

    private int selectType = 0;  //0推荐 1关注

    private ConvenientBanner banner;

    private ArrayList<WorkItem> mList;
    private LoginListener mListener;
    private boolean isTag = false;                      // 标签照片界面
    private String tagImage;
    private String tagName;
    private int tag_id;

    // 调接口跳转的按钮连续点击2秒内被认为是一次
    public static final int MIN_CLICK_DELAY_TIME = 2000;
    public long lastClickTime = 0;

    public NewRecommendAdapter(Context mContext, ArrayList<WorkItem> mList, LoginListener mListener, int selectType) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = mListener;
        mHandler = new Handler(mContext.getMainLooper());
        this.selectType = selectType;
    }

    public NewRecommendAdapter(Context mContext, LoginListener mListener, boolean isTag, String tagImage, String tagName, int tag_id) {
        this.mContext = mContext;
        this.mListener = mListener;
        this.isTag = isTag;
        this.tagImage = tagImage;
        this.tagName = tagName;
        this.tag_id = tag_id;
        mHandler = new Handler(mContext.getMainLooper());
    }

    public ArrayList<WorkItem> getList() {
        return mList;
    }

    public void setList(ArrayList<WorkItem> mList) {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:
                if (isTag) {
                    return new TagHeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag_arrow, parent, false));
                } else {
                    return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_new_recommend, parent, false));
                }
            case RECOMMENDLIST:
                return new RecommendListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new_recommend, parent, false));
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecommendListViewHolder) {
            RecommendListViewHolder mHolder = (RecommendListViewHolder) holder;
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
            else if (position > 0) return RECOMMENDLIST;
            else return 0;
        } else if (selectType == 1) { //关注
            return RECOMMENDLIST;
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

    // 按钮连续两次点击在2秒内认为是一次点击，多次的点击被视为无效
    public boolean isEffectClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if ((currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return true;
        }
        return false;
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
            new RetrofitRequest<BannerObject>(ApiRequest.getApiShiji().getBannerList()).handRequest(new MsgCallBack() {
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


    /*===============================推荐列表=============================================*/

    class RecommendListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llUser;                               //显示用户头像和名称
        SimpleDraweeView ivUserAvatar;                     //显示用户头像
        ImageView ivRedPeople;                             //红人标志
        TextView tvUserName;                               //显示用户名称
        ImageView btnFollow;                               //“关注”按钮


        TagAbleImageView ivTagImage;                       //布局中最大的图片
        LinearLayout llTag;                                //TagAbleImageView上显示的Tag布局
        TextView tvTagBrand;                               //Tag中显示的品牌

        TextView tvContent;                                //显示用户对图片商品的介绍
        TagFlowLayout tflLinkTag;                          //关键词链接

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

        public RecommendListViewHolder(View itemView) {
            super(itemView);
            llUser = (LinearLayout) itemView.findViewById(R.id.ll_user);
            ivUserAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_user_avatar);
            ivRedPeople = (ImageView) itemView.findViewById(R.id.iv_red_people);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            btnFollow = (ImageView) itemView.findViewById(R.id.btn_follow);
            ivTagImage = (TagAbleImageView) itemView.findViewById(R.id.iv_tag_image);
            llTag = (LinearLayout) itemView.findViewById(R.id.ll_tag);
            tvTagBrand = (TextView) itemView.findViewById(R.id.tv_tag_brand);

            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tflLinkTag = (TagFlowLayout) itemView.findViewById(R.id.tfl_link_tag);
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

    // 标签照片界面的头布局                   tom 2016-6-30
    class TagHeadViewHolder extends RecyclerView.ViewHolder {
        ImageView mTagImage;
        TextView mTagName;

        public TagHeadViewHolder(View itemView) {
            super(itemView);
            mTagImage = (ImageView) itemView.findViewById(R.id.layout_tag_arrow_image);
            mTagName = (TextView) itemView.findViewById(R.id.layout_tag_arrow_name);
            mTagName.setText(tagName);
            Netroid.displayImage(tagImage, mTagImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEffectClick())
                        new RetrofitRequest<TagTypeInfo>(ApiRequest.getApiShiji().getTagBrand(String.valueOf(tag_id))).
                                handRequest(new MsgCallBack() {
                                    @Override
                                    public void onResult(HttpMessage msg) {
                                        if (msg.isSuccess()) {
                                            if (msg.data != null) {
                                                TagTypeInfo tagTypeInfo = new Gson().fromJson(msg.data, TagTypeInfo.class);
                                                int tagType = tagTypeInfo.getType();
                                                int brandId = tagTypeInfo.getId();
                                                if (brandId > 0) {
                                                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("brand_id", brandId);
                                                    mContext.startActivity(intent);
                                                } else {
                                                    if (tagType == 4) {
                                                        Intent intent = new Intent(mContext, ActTagActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("tag_id", tag_id);
                                                        mContext.startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(mContext, CommonTagActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("tag_id", tag_id);
                                                        intent.putExtra("tag_content", tagName);
                                                        mContext.startActivity(intent);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                }
            });
        }
    }

    /**
     * 更新页面信息
     *
     * @param holder
     * @param item
     */
    private void updateViews(final RecommendListViewHolder holder, final WorkItem item) {
        if (item == null) return;
        /*设置用户头像 名称*/
        User user = item.getUser();
        if (user != null) {
            holder.ivUserAvatar.setImageURI(Util.transfer(user.getHead()));
            holder.tvUserName.setText(user.getName());
            if (user.getRed() == 1) {
                holder.ivRedPeople.setVisibility(View.VISIBLE);
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

        /*设置商品大图*/
        Netroid.displayImage(item.getImages().get(0).getUrl(), holder.ivTagImage.getImageView(), R.mipmap.work_default);
        ArrayList<TagAbleImageView.TagInfo> tagInfos = item.getImages().get(0).getTagInfos();
        holder.ivTagImage.initTags(null, 0);
        holder.ivTagImage.addTag(tagInfos, 2);

        if (item.getImages().get(0).getGoods_id() == null) {
            holder.llTag.setVisibility(View.GONE);
        } else {
            holder.llTag.setVisibility(View.VISIBLE);
            if (tagInfos == null || tagInfos.size() == 0) {
                holder.llTag.setVisibility(View.GONE);
            }
            for (TagAbleImageView.TagInfo tag : tagInfos) {
                if (tag.getType() == 1) {
                    String tagContent = tag.getContent() + " " + tag.getDetails();
                    if (TextUtils.isEmpty(tagContent.trim())) {
                        holder.llTag.setVisibility(View.GONE);
                    } else holder.tvTagBrand.setText(tagContent.trim());
                }
            }
        }

        if (!TextUtils.isEmpty(item.getContent())) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(item.getContent());
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }

        final ArrayList<WorkTag> tags = item.getImages().get(0).getTags();
        if (tags == null || tags.size() == 0) {
            holder.tflLinkTag.setVisibility(View.GONE);
        } else {
            holder.tflLinkTag.setVisibility(View.VISIBLE);
            TagAdapter tagAdapter = new TagAdapter(tags) {
                @Override
                public View getView(FlowLayout parent, int position, Object o) {
                    TextView tvHotWords = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_link_tag_recommend, parent, false);
                    tvHotWords.setText("#" + tags.get(position).getContent());
                    if (tags.get(position).getType() == 2) tvHotWords.setVisibility(View.GONE);
                    return tvHotWords;
                }
            };
            holder.tflLinkTag.setAdapter(tagAdapter);
        }

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
//        holder.rlLikeImage.setEnabled(true);
//        holder.ivLike.setEnabled(true);
//        holder.ivLike.setSelected(false);
//        if (!TextUtils.isEmpty(BaseApplication.getInstance().readUserId())) {
//            if (item.getLike_users() != null) {
//                for (int i = 0; i < item.getLike_users().length; i++) {
//                    if (Integer.parseInt(BaseApplication.getInstance().readUserId()) == item.getLike_users()[i]) {
//                        holder.rlLikeImage.setEnabled(false);
//                        holder.ivLike.setEnabled(false);
//                        holder.ivLike.setSelected(true);
//                    }
//                }
//            }
//        }
    }

    /**
     * 监听事件
     *
     * @param holder
     * @param item
     * @param position
     */
    private void initEvents(final RecommendListViewHolder holder, final WorkItem item, final int position) {
        //点击tag跳转到品牌官网
        holder.ivTagImage.setCustomEventListener(new TagAbleImageView.CustomEventListener() {
            @Override
            public void onAddTag() {

            }

            @Override
            public void onTagTextClick(TagView tagView) {
                //点击tag跳转到品牌官网
                ArrayList<WorkTag> taglist = item.getImages().get(0).getTags();
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
                ArrayList<WorkTag> taglist = item.getImages().get(0).getTags();
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

        //点击跳转到笔记详情页
        holder.ivTagImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWorkDetail(position, item);
            }
        });
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

        //点击#关键词跳转到关键词页面
        final ArrayList<WorkTag> tags = item.getImages().get(0).getTags();
        holder.tflLinkTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                int tagId = tags.get(position).getTag_id();
                int tagType = tags.get(position).getType();
                String tagContent = tags.get(position).getContent();
                getBrandId(tagId, tagType, tagContent);
                return false;
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

        //点击跳转到商品详情页面
        holder.llTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGoodsDetail(item);
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
     * 跳转到笔记详情
     *
     * @param position
     * @param item
     */
    private void goToWorkDetail(final int position, final WorkItem item) {
        Intent intent = new Intent(mContext, NewWorkDetailsActivity.class);
        intent.putExtra("work_id", item.getWork_id());
        intent.putExtra("goodsId", item.getImages().get(0).getGoods_id());
        intent.putExtra("position", position);
        intent.putExtra("data", new Gson().toJson(item));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (mListener != null) mListener.goToWorkDetail(intent);
    }

    /**
     * 点击关注按钮，关注或取消关注
     */
    private void clickFollow(final RecommendListViewHolder holder, WorkItem item) {
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
    private void followUser(final RecommendListViewHolder holder, final WorkItem item) {
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
    private void unFollowUser(final RecommendListViewHolder holder, final WorkItem item) {
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
    private void setWorkLike(final RecommendListViewHolder holder, final WorkItem item) {
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
            String title = item.getUser().getName() + "的购物笔记";
            String imageurl = Util.transfer(item.getImages().get(0).getUrl());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            String date = sDateFormat.format(new java.util.Date());
            String url = "http://h5.qnmami.com/html/note/index.html?id=" + item.getWork_id() + "&r=" + date;
            String content = item.getContent();
            new ShareDialog(mContext, title, imageurl, url, content).build().show();
        }
    }

    /**
     * 跳转到商品详情页
     */
    private void goToGoodsDetail(WorkItem item) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", item.getImages().get(0).getGoods_id());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 根据tag获取品牌跳转到品牌页，未获取到则跳转到标签页
     *
     * @param tagId
     */
    private void getBrandId(final int tagId, final int tagType, final String tagContent) {
        if (isEffectClick())
            new RetrofitRequest<TagTypeInfo>(ApiRequest.getApiShiji().getTagBrand(String.valueOf(tag_id))).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    int brandId = 0;
                    if (msg.data != null) {
                        TagTypeInfo tag = new Gson().fromJson(msg.data, TagTypeInfo.class);
                        brandId = tag.getId();
                    }
                    if (brandId > 0) {
                        Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("brand_id", brandId);
                        mContext.startActivity(intent);
                    } else {
                        if (tagType == 4) {
                            Intent intent = new Intent(mContext, ActTagActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("tag_id", tagId);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, CommonTagActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("tag_id", tagId);
                            intent.putExtra("tag_content", tagContent);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
    }

    public interface LoginListener {
        void goLogin();

        void setFollow(WorkItem item);

        void goToWorkDetail(Intent intent);

        void goToCommnets(Intent intent);
    }
}
