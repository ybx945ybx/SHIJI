package cn.yiya.shiji.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.HomeIssueActivity;
import cn.yiya.shiji.activity.NewCategoryActivity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewLocalWebActivity;
import cn.yiya.shiji.activity.NewMatchDetailActivity;
import cn.yiya.shiji.activity.NewSingleBrandActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MapRequest;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.BrandsRecInfo;
import cn.yiya.shiji.entity.HtmlThematicInfo;
import cn.yiya.shiji.entity.LogstashReport.CategoryLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainBannerLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MainRecBrandGoodsLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MatchGoodsLogstashItem;
import cn.yiya.shiji.entity.LogstashReport.MatchLogstashItem;
import cn.yiya.shiji.entity.MainActImgInfo;
import cn.yiya.shiji.entity.MainBannerInfo;
import cn.yiya.shiji.entity.MainFlashSaleInfo;
import cn.yiya.shiji.entity.ThematicInfo;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.WorkImage;
import cn.yiya.shiji.entity.WorkItem;
import cn.yiya.shiji.entity.WorkObject;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.FontStyleUtil;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.CountDownTimerView;
import cn.yiya.shiji.views.ExpandCollapseAnimator;
import cn.yiya.shiji.views.FloatingActionButton;
import cn.yiya.shiji.views.FullyLinearLayoutManager;
import cn.yiya.shiji.views.PagerContainer;
import cn.yiya.shiji.views.VerticalClipLayout;

/**
 * Created by chenjian on 2016/5/31.
 */
public class NewMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final static int BANNER = 11;
    public final static int CATEGORY = 22;
    public final static int FLASHSALE = 33;
    public final static int THEMATIC = 44;
    public final static int PAIR_WITH = 55;
    public final static int LIST_TYPE = 66;

    private int lastExpandRequest = 0;
    private static final int HEAD_POS = 5;

    private ConvenientBanner banner;
    private Handler mHandler;
    private Context mContext;
    private LoginListener mListener;
    private List<BrandsRecInfo.ListEntity> mLists;
    private SparseArray<HashSet<Integer>> mArray;
    private ArrayList<WorkItem> pairList = new ArrayList<>();
    private PairAdapter pairAdapter;

    public NewMainAdapter(Context context, SparseArray<HashSet<Integer>> array, List<BrandsRecInfo.ListEntity> list, LoginListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mLists = list;
        this.mArray = array;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:
                return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_banner, parent, false));
            case CATEGORY:
                return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_category, parent, false));
            case FLASHSALE:
                return new FlashSaleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_flash_sale, parent, false));
            case THEMATIC:
                return new ThematicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_discount, parent, false));
//            case RECOMMEND:
//                return new RecViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_recommend, parent, false));
            case PAIR_WITH:
                return new PairViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_child_pair_with, parent, false));
            case LIST_TYPE:
                return new BrandsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_brands_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BrandsViewHolder) {
            final BrandsRecInfo.ListEntity info = mLists.get(position - HEAD_POS);
            final BrandsViewHolder viewHolder = (BrandsViewHolder) holder;
            viewHolder.recyclerView.setAdapter(new BrandsAdapter(mLists.get(position - HEAD_POS).getGoods_list()));
            viewHolder.recyclerView.setNestedScrollingEnabled(false);

            viewHolder.tvName.setText(info.getBrand_name());
//            Netroid.displayImage(info.getBrand_icon(), viewHolder.ivLogo);
            viewHolder.ivLogo.setImageURI(Util.transferCropImage(info.getBrand_icon(), SimpleUtils.dp2px(mContext, 56)));
            viewHolder.tvDesc.setText(info.getBottom_left());
            viewHolder.tvTime.setText(info.getBottom_right());
            viewHolder.tvReason.setVisibility(TextUtils.isEmpty(info.getReason()) ? View.GONE : View.VISIBLE);
            viewHolder.tvReason.setText(info.getReason());

            if (mArray.size() == 0) {
                HashSet<Integer> set = new HashSet<>();
                set.add(position);
                mArray.put(info.getBrand_id(), set);
            } else {
                boolean exit = mArray.get(info.getBrand_id()) == null;
                HashSet<Integer> set;
                if (exit) {
                    set = new HashSet<>();
                } else {
                    set = mArray.get(info.getBrand_id());
                }
                set.add(position);
                mArray.put(info.getBrand_id(), set);
            }

            if (info.getFollow() == 1) {
                viewHolder.fabLike.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.fabLike.setLineMorphingState(1, true);
                    }
                }, 200);
            } else {
                viewHolder.fabLike.setLineMorphingState(0, true);
            }

            viewHolder.fabLike.setTag(position);

            viewHolder.fabLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (info.getFollow() == 1) {
                        viewHolder.fabLike.setLineMorphingState(0, true);
                        new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(String.valueOf(info.getTag_id()))).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    Util.toast(mContext, "已取消订阅", true);
                                    info.setFollow(2);
                                    if (mListener != null) {
                                        mListener.notifyData(info.getBrand_id(), position);
                                    }
                                } else if (msg.isLossLogin()) {
                                    viewHolder.fabLike.setLineMorphingState(0, true);
                                    if (mListener != null) {
                                        mListener.goLogin();
                                    }
                                } else {
                                    Util.toast(mContext, "取消失败", true);
                                    viewHolder.fabLike.setLineMorphingState(1, true);
                                }
                            }
                        });
                    } else {
                        viewHolder.fabLike.setLineMorphingState(1, true);
                        new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(String.valueOf(info.getTag_id()))).handRequest(new MsgCallBack() {
                            @Override
                            public void onResult(HttpMessage msg) {
                                if (msg.isSuccess()) {
                                    Util.toast(mContext, "已订阅", true);
                                    info.setFollow(1);
                                    if (mListener != null) {
                                        mListener.notifyData(info.getBrand_id(), position);
                                    }
                                } else if (msg.isLossLogin()) {
                                    viewHolder.fabLike.setLineMorphingState(0, true);
                                    if (mListener != null) {
                                        mListener.goLogin();
                                    }
                                } else {
                                    Util.toast(mContext, "订阅失败", true);
                                    viewHolder.fabLike.setLineMorphingState(0, true);
                                }
                            }
                        });
                    }
                }
            });

            viewHolder.ivLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                    intent.putExtra("brand_id", info.getBrand_id());
                    mContext.startActivity(intent);
                }
            });

            viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
                    intent.putExtra("brand_id", info.getBrand_id());
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof PairViewHolder) {
            final PairViewHolder viewHolder = (PairViewHolder) holder;
            viewHolder.llFooterMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    viewHolder.ivMore.setVisibility(View.GONE);
//                    viewHolder.progressBar.setVisibility(View.VISIBLE);
//                    getMatchRecommendList(pairList.size(), viewHolder);
                    if (mListener != null) mListener.goToDiscoverFragment(pairList.size());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mLists.size() + HEAD_POS;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return BANNER;
        } else if (position == 1) {
            return CATEGORY;
        } else if (position == 2) {
            return FLASHSALE;
        } else if (position == 3) {
            return THEMATIC;
        } else if (position == 4) {
            return PAIR_WITH;
        } else {
            return LIST_TYPE;
        }
    }

    // 广告栏
    class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(final View itemView) {
            super(itemView);
            banner = (ConvenientBanner) itemView.findViewById(R.id.main_banner);
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) banner.getLayoutParams();

            new RetrofitRequest<MainBannerInfo>(ApiRequest.getApiShiji().getMainBanner("7")).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        MainBannerInfo object = (MainBannerInfo) msg.obj;
                        if (object == null || object.getList() == null) {
                            layoutParams.height = 0;
                            banner.setLayoutParams(layoutParams);
                            itemView.findViewById(R.id.line).setVisibility(View.VISIBLE);
                            return;
                        }
                        layoutParams.width = SimpleUtils.getScreenWidth(mContext);
                        layoutParams.height = layoutParams.width * 192 / 375;
                        banner.setLayoutParams(layoutParams);

                        banner.setPageIndicator(new int[]{R.mipmap.main_indictator_normal, R.mipmap.main_indictator_focused});
                        banner.startTurning(4000);

                        List<MainBannerInfo.ListEntity> info = object.getList();
                        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                            @Override
                            public NetworkImageHolderView createHolder() {
                                return new NetworkImageHolderView();
                            }
                        }, info);
                    }
                }
            });
        }
    }

    class NetworkImageHolderView implements Holder<MainBannerInfo.ListEntity> {
        private ImageView imageView;
        private SimpleDraweeView ivThemIcon;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.new_main_banner, null);
            imageView = (ImageView) view.findViewById(R.id.iv_new_main_banner);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext);
            layoutParams.height = layoutParams.width * 192 / 375;
            imageView.setLayoutParams(layoutParams);
            ivThemIcon = (SimpleDraweeView) view.findViewById(R.id.iv_theme_icon);
            ivThemIcon.setImageURI(Util.transferImage(BaseApplication.getInstance().getThemeIcon(), SimpleUtils.dp2px(mContext, 39)));
            return view;
        }

        @Override
        public void UpdateUI(final Context context, final int position, final MainBannerInfo.ListEntity data) {
            imageView.setImageResource(R.drawable.user_dafault);
            String strBg = Util.ClipImageBannerView(data.getCover());
            Netroid.displayImage(strBg, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 打点事件
                    MainBannerLogstashItem mainBannerLogstashItem = new MainBannerLogstashItem();
                    mainBannerLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                    mainBannerLogstashItem.setBanner_id(data.getId());
                    mainBannerLogstashItem.setBanner_title(data.getTitle());
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainTopBannerEvent(mainBannerLogstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {

                        }
                    });

                    switch (data.getType()) {
                        case 1:           // 活动
                            Intent intent = new Intent(mContext, HomeIssueActivity.class);
                            intent.putExtra("activityId", data.getId());
                            intent.putExtra("menuId", 7);
                            mContext.startActivity(intent);
                            break;
                        case 2:           // 专题
                            Intent intent1 = new Intent(mContext, NewLocalWebActivity.class);
                            HtmlThematicInfo info = new HtmlThematicInfo();
                            info.setItemId(data.getId());
                            info.setTypeId("3");
                            String dataJson = new Gson().toJson(info);
                            intent1.putExtra("data", dataJson);
                            intent1.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");
                            intent1.putExtra("type", data.getType());
                            mContext.startActivity(intent1);
                            break;
                        case 3:           // h5分享
                            Intent intent2 = new Intent(mContext, NewLocalWebActivity.class);
                            intent2.putExtra("url", data.getUrl());
                            intent2.putExtra("type", data.getType());
                            intent2.putExtra("title", data.getTitle());
                            intent2.putExtra("data", new Gson().toJson(data));
                            mContext.startActivity(intent2);
                            break;
                    }
                }
            });
        }
    }

    // 专题分类
    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private View vPic1, vPic2, vPic3, vPic4;    // 首页中women，men, kids, beauty,点击

        public CategoryViewHolder(View itemView) {
            super(itemView);
            vPic1 = itemView.findViewById(R.id.main_pic_one);
            vPic2 = itemView.findViewById(R.id.main_pic_two);
            vPic3 = itemView.findViewById(R.id.main_pic_three);
            vPic4 = itemView.findViewById(R.id.main_pic_four);

            vPic1.setOnClickListener(new ClickEvents());
            vPic2.setOnClickListener(new ClickEvents());
            vPic3.setOnClickListener(new ClickEvents());
            vPic4.setOnClickListener(new ClickEvents());
        }
    }

    // 闪购
    class FlashSaleViewHolder extends RecyclerView.ViewHolder {

        private View viewline;
        private ImageView ivAct;
        private PagerContainer container;
        private LinearLayout llContainer;

        public FlashSaleViewHolder(View itemView) {
            super(itemView);
            viewline = itemView.findViewById(R.id.viewline);
            ivAct = (ImageView) itemView.findViewById(R.id.main_activity_img);
            container = (PagerContainer) itemView.findViewById(R.id.main_container);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            final ViewPager pager = container.getViewPager();

            new RetrofitRequest<MainActImgInfo>(ApiRequest.getApiShiji().getMainActImage()).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        final MainActImgInfo info = (MainActImgInfo) msg.obj;
                        if (info != null && info.getImage() != null) {
                            viewline.setVisibility(View.VISIBLE);
                            ivAct.setVisibility(View.VISIBLE);
                            Netroid.displayImage(Util.transferImage(info.getImage(), SimpleUtils.getScreenWidth(mContext)), ivAct);
                            ivAct.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // 打点事件
                                    MainBannerLogstashItem mainBannerLogstashItem = new MainBannerLogstashItem();
                                    mainBannerLogstashItem.setBanner_title(info.getTitle());
                                    mainBannerLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
                                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainActBannerEvent(mainBannerLogstashItem)).handRequest(new MsgCallBack() {
                                        @Override
                                        public void onResult(HttpMessage msg) {

                                        }
                                    });

                                    Intent intent = new Intent(mContext, NewLocalWebActivity.class);
                                    intent.putExtra("url", info.getUrl());
                                    intent.putExtra("type", 4);
                                    intent.putExtra("title", info.getTitle());
                                    intent.putExtra("data", new Gson().toJson(info.getShare()));
                                    mContext.startActivity(intent);
                                }
                            });
                        } else {
                            ivAct.setVisibility(View.GONE);
                        }
                    } else {
                        ivAct.setVisibility(View.GONE);
                    }
                }
            });

            new RetrofitRequest<MainFlashSaleInfo>(ApiRequest.getApiShiji().getMainFlashSale()).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        final MainFlashSaleInfo info = (MainFlashSaleInfo) msg.obj;
                        if (info == null || info.getList() == null || info.getList().isEmpty()) {
                            llContainer.setVisibility(View.GONE);
                            return;
                        }
                        viewline.setVisibility(View.VISIBLE);
                        llContainer.setVisibility(View.VISIBLE);
                        pager.setAdapter(new CoverFlowAdapter(info));
                        pager.setClipChildren(false);
                        pager.setCurrentItem(info.getList().size() * 100 / 2, false);
                        pager.setOffscreenPageLimit(3);
                    }
                }
            });
        }
    }

    class TextEntity {
        TextView textView;
        int text_pos;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public int getText_pos() {
            return text_pos;
        }

        public void setText_pos(int text_pos) {
            this.text_pos = text_pos;
        }
    }

    // 画廊效果
    private class CoverFlowAdapter extends PagerAdapter {
        private MainFlashSaleInfo mInfo;
        private ArrayList<TextEntity> viewList = new ArrayList<>();

        public CoverFlowAdapter(MainFlashSaleInfo info) {
            this.mInfo = info;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            final View view = LayoutInflater.from(mContext).inflate(R.layout.main_child_cover, null);
            final int nPosition = position % mInfo.getList().size();
            SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.image_cover);
            ImageView imageMask = (ImageView) view.findViewById(R.id.image_mask);
            final CountDownTimerView tvTime = (CountDownTimerView) view.findViewById(R.id.main_countdown);
            LinearLayout rlytNotice = (LinearLayout) view.findViewById(R.id.main_notice_layout);
            final TextView tvNotice = (TextView) view.findViewById(R.id.main_notice_txt);
            TextView tvName = (TextView) view.findViewById(R.id.flash_sale_name);

            int nNotice = mInfo.getList().get(nPosition).getNotice();
            if (nNotice == 1) {
                rlytNotice.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.GONE);
                imageMask.setVisibility(View.VISIBLE);
                tvNotice.setText("已提醒");
                tvNotice.setEnabled(false);
                imageView.setEnabled(false);
            } else {
                int type = Util.compareTime(mInfo.getNow(), mInfo.getList().get(nPosition).getBegin_time()
                        , mInfo.getList().get(nPosition).getEnd_time());
                switch (type) {
                    case -1:   // 开抢提醒
                        rlytNotice.setVisibility(View.VISIBLE);
                        tvTime.setVisibility(View.GONE);
                        imageMask.setVisibility(View.VISIBLE);
                        tvNotice.setEnabled(true);
                        imageView.setEnabled(false);
                        String timeTxt = Util.getHourMinuteTime(mInfo.getList().get(nPosition).getBegin_time()) + " 开抢提醒";
                        tvNotice.setText(timeTxt);
                        break;
                    case 0:    // 倒计提醒
                        rlytNotice.setVisibility(View.GONE);
                        tvTime.setVisibility(View.VISIBLE);
                        imageView.setEnabled(true);
                        float[] time = Util.counterTimeLong(mInfo.getNow(), mInfo.getList().get(nPosition).getEnd_time());
                        tvTime.setTime((int) time[2], (int) time[1], (int) time[0]);
                        tvTime.start();
                        break;
                    case 1:    // 已关闭
                        rlytNotice.setVisibility(View.VISIBLE);
                        tvTime.setVisibility(View.GONE);
                        tvNotice.setText("已关闭");
                        tvNotice.setEnabled(false);
                        imageView.setEnabled(false);
                        break;
                }

            }

            TextEntity textEntity = new TextEntity();
            textEntity.setTextView(tvNotice);
            textEntity.setText_pos(nPosition);
            viewList.add(textEntity);

            tvNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RetrofitRequest<>(ApiRequest.getApiShiji().setFlashSaleNotice(MapRequest.setFlashNotice(
                            mInfo.getList().get(nPosition).getId(), 1))).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                            if (msg.isSuccess()) {
                                Util.toast(mContext, "已设置提醒成功", true);
                                tvNotice.setText("已提醒");
                                tvNotice.setEnabled(false);
                                mInfo.getList().get(nPosition).setNotice(1);
                                for (int i = 0; i < viewList.size(); i++) {
                                    TextView textView = viewList.get(i).getTextView();
                                    int pos = viewList.get(i).getText_pos();
                                    if (pos == nPosition) {
                                        mInfo.getList().get(pos).setNotice(1);
                                        textView.setText("已提醒");
                                        textView.setEnabled(false);
                                    }
                                }
                            } else if (msg.isLossLogin()) {
                                if (mListener != null) {
                                    mListener.goLogin();
                                }
                            }
                        }
                    });
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvTime.findViewById(R.id.time_layout).getVisibility() == View.GONE) {
                        Util.toast(mContext, "该活动已关闭", true);
                        return;
                    }
                    Intent intent = new Intent(mContext, HomeIssueActivity.class);
                    intent.putExtra("flashId", mInfo.getList().get(nPosition).getId());
                    intent.putExtra("type", 11);
                    mContext.startActivity(intent);
                }
            });


//            String strBg = Util.ClipImageFlashSaleView(mInfo.getList().get(nPosition).getCover_origin());
//            Netroid.displayImage(strBg, imageView);
            imageView.setImageURI(Util.ClipImageFlashSaleView(mInfo.getList().get(nPosition).getCover_origin()));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            tvName.setText(mInfo.getList().get(nPosition).getName());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mInfo.getList().size() * 100;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    class ThematicViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;
        private ExpandCollapseAnimator animator;

        public ThematicViewHolder(View itemView) {
            super(itemView);

            Point displaySize = new Point();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);

            float expandedHeightAspect = 0.50f;
            float collapsedHeightAspect = 0.15f;
            final int expandedHeight = (int) (displaySize.x * expandedHeightAspect);
            final int collapsedHeight = (int) (displaySize.x * collapsedHeightAspect);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_discount);

            new RetrofitRequest<ThematicInfo>(ApiRequest.getApiShiji().getMainThematicList("2")).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    if (msg.isSuccess()) {
                        final ThematicInfo info = (ThematicInfo) msg.obj;
                        if (info == null || info.getList() == null || info.getList().isEmpty()) {
                            return;
                        }

                        animator = new ExpandCollapseAnimator(0.0001f * mContext.getResources().getDisplayMetrics().density);
                        recyclerView.setPadding(recyclerView.getPaddingLeft(),
                                expandedHeight - collapsedHeight, // first element need space to be expanded somewhere
                                recyclerView.getPaddingRight(),
                                0); // all this stuff works together thanks to clipToPadding=false
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new FullyLinearLayoutManager(mContext,
                                180);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setNestedScrollingEnabled(false);
                        RecyclerView.Adapter adapter = new ThemticAdapter(info.getList(), expandedHeight, collapsedHeight,
                                new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View view, MotionEvent event) {
                                        if (MotionEvent.ACTION_DOWN == event.getAction()) {
                                            return true;
                                        }
                                        if (MotionEvent.ACTION_UP != event.getAction()) {
                                            return false;
                                        }

                                        int clickedPosition = recyclerView.getChildAdapterPosition(view);
                                        if (clickedPosition != lastExpandRequest) {
                                            animator.setExpanding(clickedPosition);
                                            info.getList().get(clickedPosition).setbExpand(true);
                                            info.getList().get(lastExpandRequest).setbExpand(false);
                                            lastExpandRequest = clickedPosition;
                                        } else {
                                            Intent intent = new Intent(mContext, NewLocalWebActivity.class);
                                            HtmlThematicInfo mInfo = new HtmlThematicInfo();
                                            mInfo.setItemId(info.getList().get(clickedPosition).getId());
                                            mInfo.setTypeId("2");
                                            String dataJson = new Gson().toJson(mInfo);
                                            intent.putExtra("data", dataJson);
                                            intent.putExtra("type", 2);
//                                            intent.putExtra("url", "http://t1.h5.qnmami.com/app/homeActivities/html/index.html");//测试
                                            intent.putExtra("url", "http://h5.qnmami.com/app/homeActivities/html/index.html");//正式
                                            mContext.startActivity(intent);
                                        }

                                        return true;
                                    }
                                });

                        recyclerView.setAdapter(adapter);

                        recyclerView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                            @Override
                            public void onChildViewAdded(View parent, final View child) {
                                child.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {
                                        child.getViewTreeObserver().removeOnPreDrawListener(this);

                                        int position = recyclerView.getChildAdapterPosition(child);
                                        animator.add(position, (VerticalClipLayout) child);

                                        if (lastExpandRequest == position) {
                                            animator.setExpanding(position); // here we call setExpanding and not just call onStartExpanding() and onExpanded() because animator actually handles animation and decides when to notify listeners
                                            ViewGroup v = (ViewGroup) child;
                                            info.getList().get(position).setbExpand(true);
                                            initView(info.getList(), v, position);

                                        } else {
                                            ViewGroup v = (ViewGroup) child;
                                            initView(info.getList(), v, position);
                                        }
                                        return false;
                                    }
                                });
                            }

                            @Override
                            public void onChildViewRemoved(View parent, View child) {
                                int position = recyclerView.getChildAdapterPosition(child);
                                animator.remove(position);
                            }
                        });

                        animator.setOnViewExpandCollapseListener(new ExpandCollapseAnimator.OnViewExpandCollapseListener() {

                            @Override
                            public void onViewStartExpanding(int position, VerticalClipLayout v) {
                                ViewGroup group = (ViewGroup) v.getChildAt(0);
                                for (int i = 0; i < group.getChildCount(); i++) {
                                    View view = group.getChildAt(i);
                                    if (view.getId() == R.id.thematic_image_txt) {
                                        view.setVisibility(View.VISIBLE);
                                        ViewPropertyAnimator.animate(view).cancel();
                                        ViewPropertyAnimator.animate(view).alpha(1.0f).setInterpolator(new DecelerateInterpolator()).
                                                setDuration(300).start();
                                    }

                                    if (position > 0 && view.getId() == R.id.thematic_image_gradient) {
                                        view.setVisibility(View.VISIBLE);
                                        ViewPropertyAnimator.animate(view).cancel();
                                        ViewPropertyAnimator.animate(view).alpha(1.0f).setInterpolator(new DecelerateInterpolator()).
                                                setDuration(300).start();
                                    }

                                    if (view.getId() == R.id.thematic_image_arrow) {
                                        view.setVisibility(View.VISIBLE);
                                        ViewPropertyAnimator.animate(view).cancel();
                                        ViewPropertyAnimator.animate(view).translationX(0).setInterpolator(new DecelerateInterpolator()).
                                                setDuration(300).setStartDelay(100).start();
                                    }
                                }

                            }

                            @Override
                            public void onViewExpanded(int position, VerticalClipLayout v) {
                            }

                            @Override
                            public void onViewStartCollapsing(int position, VerticalClipLayout v) {
                                ViewGroup group = (ViewGroup) v.getChildAt(0);
                                for (int i = 0; i < group.getChildCount(); i++) {
                                    View view = group.getChildAt(i);
                                    if (view.getId() == R.id.thematic_image_txt) {
                                        ViewPropertyAnimator.animate(view).cancel();
                                        ViewPropertyAnimator.animate(view).alpha(0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).
                                                start();
                                    }

                                    if (view.getId() == R.id.thematic_image_gradient) {
                                        ViewPropertyAnimator.animate(view).cancel();
                                        ViewPropertyAnimator.animate(view).alpha(0f).setDuration(300).setInterpolator(new DecelerateInterpolator()).
                                                start();
                                    }

                                    if (view.getId() == R.id.thematic_image_arrow) {
                                        ViewPropertyAnimator.animate(view).cancel();
                                        ViewPropertyAnimator.animate(view).translationX(-400).setDuration(300).setInterpolator(new DecelerateInterpolator()).
                                                start();
                                    }
                                }
                            }

                            @Override
                            public void onViewsChanging() {
                            }
                        });

                        animator.start();
                    }
                }
            });

        }
    }

    private void initView(List<ThematicInfo.ListEntity> mLists, ViewGroup v, int position) {
        ViewGroup group = (ViewGroup) v.getChildAt(0);
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if (view.getId() == R.id.thematic_image_arrow) {
                ViewPropertyAnimator.animate(view).translationX(-400).setDuration(0).start();
                if (mLists.get(position).isbExpand()) {
                    view.setVisibility(View.VISIBLE);
                    ViewPropertyAnimator.animate(view).cancel();
                    ViewPropertyAnimator.animate(view).translationX(0).
                            setDuration(300).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).start();
                }
            }

            if (view.getId() == R.id.thematic_image_txt) {
                view.setVisibility(View.GONE);
                if (mLists.get(position).isbExpand()) {
                    view.setVisibility(View.VISIBLE);
                    ViewPropertyAnimator.animate(view).cancel();
                    ViewPropertyAnimator.animate(view).alpha(1.0f)
                            .setInterpolator(new DecelerateInterpolator()).setDuration(20).start();
                }
            }

            if (view.getId() == R.id.thematic_image_gradient) {
                view.setVisibility(View.GONE);
                if (position > 0 && mLists.get(position).isbExpand()) {
                    view.setVisibility(View.VISIBLE);
                    ViewPropertyAnimator.animate(view).cancel();
                    ViewPropertyAnimator.animate(view).alpha(1.0f)
                            .setInterpolator(new DecelerateInterpolator()).setDuration(20).start();
                }
            }
        }
    }

    public class ThemticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int expandedHeight;
        private final int collapsedHeight;
        private final View.OnTouchListener onTouchListener;
        private List<ThematicInfo.ListEntity> mLists;

        public ThemticAdapter(List<ThematicInfo.ListEntity> mLists, int eHeight, int cHeight, View.OnTouchListener onTouchListener) {
            this.onTouchListener = onTouchListener;
            this.mLists = mLists;
            this.expandedHeight = eHeight;
            this.collapsedHeight = cHeight;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.main_thematic_card, parent, false);
            view.setOnTouchListener(onTouchListener);
            view.getLayoutParams().height = collapsedHeight;
            view.findViewById(R.id.main_content).getLayoutParams().height = expandedHeight;
            return new ThematicCardViewHolder(view);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return true;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position < 0 || position >= getItemCount()) {
                throw (new IllegalArgumentException(String.format("tiles doesn't have position %s", position)));
            }

            ThematicInfo.ListEntity info = mLists.get(position);

            final ThematicCardViewHolder cHolder = (ThematicCardViewHolder) holder;

            cHolder.tvArrow.setText(handleString(info.getName()));
            cHolder.tvName.setText(handleString(info.getName()));
            if (!TextUtils.isEmpty(info.getCover())) {
//                String strBgTxt = Util.ClipImageBannerView(info.getCover());
//                Netroid.displayImage(strBgTxt, cHolder.ivBgTxt);
                cHolder.ivBgTxt.setImageURI(Util.ClipImageBannerView(info.getCover()));
            }

            if (!TextUtils.isEmpty(info.getWordless_cover())) {
//                String strBg = Util.ClipImageBannerView(info.getWordless_cover());
//                Netroid.displayImage(strBg, cHolder.ivBg);
                cHolder.ivBg.setImageURI(Util.ClipImageBannerView(info.getWordless_cover()));
            }
        }

        private String handleString(String str) {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            if (str.length() == 2) {
                return str.substring(0, 1) + " " + str.substring(1, 2);
            }

            if (str.length() == 3) {
                return str.substring(0, 1) + " " + str.substring(1, 2) + " " + str.substring(2, 3);
            }

            return str;
        }

        @Override
        public int getItemCount() {
            return mLists.size();
        }

        class ThematicCardViewHolder extends RecyclerView.ViewHolder {

            private SimpleDraweeView ivBg;
            private TextView tvName;
            private SimpleDraweeView ivBgTxt;
            private TextView tvArrow;

            public ThematicCardViewHolder(View itemView) {
                super(itemView);
                ivBg = (SimpleDraweeView) itemView.findViewById(R.id.thematic_image);
                tvName = (TextView) itemView.findViewById(R.id.thematic_text);
                ivBgTxt = (SimpleDraweeView) itemView.findViewById(R.id.thematic_image_txt);
                tvArrow = (TextView) itemView.findViewById(R.id.thematic_image_arrow);
            }
        }
    }

//    class RecViewHolder extends RecyclerView.ViewHolder {
//
//        RecyclerView recyclerView;
//
//        public RecViewHolder(View itemView) {
//            super(itemView);
//
//            recyclerView = (RecyclerView) itemView.findViewById(R.id.main_rec_recycler);
//            LinearLayoutManager manager = new LinearLayoutManager(mContext);
//            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            recyclerView.setLayoutManager(manager);
//            recyclerView.setNestedScrollingEnabled(false);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//            new RetrofitRequest<DaliyRecInfo>(ApiRequest.getApiShiji().getDailyRec()).handRequest(new MsgCallBack() {
//                @Override
//                public void onResult(HttpMessage msg) {
//                    if (msg.isSuccess()) {
//                        DaliyRecInfo object = (DaliyRecInfo) msg.obj;
//                        if (object == null || object.getList().isEmpty()) {
//                            return;
//                        }
//
//                        recyclerView.setAdapter(new RecItemAdapter(object.getList()));
//                    }
//                }
//            });
//        }
//    }

//    class RecItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private List<DaliyRecInfo.ListEntity> mLists;
//
//        public RecItemAdapter(List<DaliyRecInfo.ListEntity> mLists) {
//            this.mLists = mLists;
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new RecItmeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rec_item, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//            final RecItmeViewHolder viewHolder = (RecItmeViewHolder) holder;
//            final DaliyRecInfo.ListEntity listInfo = mLists.get(position);
//
//            //给每日推荐列表第一个添加引导
//            if (position == 0) {
//                if (!MyPreference.takeSharedPreferences(mContext, MyPreference.MAIN_RECOMMEND_FOLLOW_BRAND)) {
//                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//                    alphaAnimation.setFillAfter(true);
//                    alphaAnimation.setDuration(1000);
//                    alphaAnimation.setStartOffset(500);
//                    viewHolder.llytTips.setVisibility(View.VISIBLE);
//                    viewHolder.llytTips.setAnimation(alphaAnimation);
//                    alphaAnimation.startNow();
//                    MyPreference.saveSharedPreferences(mContext, MyPreference.MAIN_RECOMMEND_FOLLOW_BRAND, true);
//                    final Handler mHandler = new Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            super.handleMessage(msg);
//                            switch (msg.what) {
//                                case 10:
//                                    if (viewHolder.llytTips.getVisibility() == View.VISIBLE) {
//                                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
//                                        alphaAnimation.setFillAfter(true);
//                                        alphaAnimation.setDuration(1000);
//                                        viewHolder.llytTips.setVisibility(View.INVISIBLE);
//                                        viewHolder.llytTips.setAnimation(alphaAnimation);
//                                        alphaAnimation.startNow();
//                                    }
//                                    break;
//                            }
//                        }
//                    };
//
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        public void run() {
//                            mHandler.sendEmptyMessage(10);
//                        }
//                    }, 3000);
//                }
//            }
//
//            List<DaliyRecInfo.ListEntity.GoodsEntity> goodLists = listInfo.getGoods();
//            viewHolder.ivLogo.setImageURI(Util.transferCropImage(listInfo.getCover(), SimpleUtils.dp2px(mContext, 55)));
//            viewHolder.tvName.setText(listInfo.getBrand_name());
//            viewHolder.ivOne.setImageURI(Util.transferCropImage(goodLists.get(0).getCover(), SimpleUtils.dp2px(mContext, 177)));
//            viewHolder.ivTwo.setImageURI(Util.transferCropImage(goodLists.get(1).getCover(), SimpleUtils.dp2px(mContext, 84)));
//            viewHolder.ivThree.setImageURI(Util.transferCropImage(goodLists.get(2).getCover(), SimpleUtils.dp2px(mContext, 84)));
//            viewHolder.vSpace.setVisibility(position == mLists.size() - 1 ? View.VISIBLE : View.GONE);
//            if (listInfo.getFollow() == 1) {
//                viewHolder.fabLike.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        viewHolder.fabLike.setLineMorphingState(1, false);
//                    }
//                }, 200);
//            } else {
//                viewHolder.fabLike.setLineMorphingState(0, false);
//            }
//
//            viewHolder.fabLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listInfo.getFollow() == 0) {
//                        if (mListener != null) {
//                            mListener.goLogin();
//                        }
//                        return;
//                    }
//
//                    viewHolder.fabLike.setLineMorphingState((viewHolder.fabLike.getLineMorphingState() + 1) % 2, true);
//
//                    // 打点事件
//                    MainRecBrandLogstashItem mainRecBrandLogstashItem = new MainRecBrandLogstashItem();
//                    mainRecBrandLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
//                    mainRecBrandLogstashItem.setBrand_id(String.valueOf(listInfo.getBrand_id()));
//
//                    if (listInfo.getFollow() == 1) {
//                        // 打点上报
//                        new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainRecUnfollowEvent(mainRecBrandLogstashItem)).handRequest(new MsgCallBack() {
//                            @Override
//                            public void onResult(HttpMessage msg) {
//                            }
//                        });
//
//                        new RetrofitRequest<>(ApiRequest.getApiShiji().postUnFollowBrands(String.valueOf(listInfo.getTag_id()))).handRequest(new MsgCallBack() {
//                            @Override
//                            public void onResult(HttpMessage msg) {
//                                if (msg.isSuccess()) {
//                                    Util.toast(mContext, "已取消订阅", true);
//                                    listInfo.setFollow(2);
//                                } else {
//                                    Util.toast(mContext, "取消失败", true);
//                                    viewHolder.fabLike.setLineMorphingState((viewHolder.fabLike.getLineMorphingState() + 1) % 2, true);
//                                }
//                            }
//                        });
//                    } else {
//                        // 打点上报
//                        new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainRecFollowEvent(mainRecBrandLogstashItem)).handRequest(new MsgCallBack() {
//                            @Override
//                            public void onResult(HttpMessage msg) {
//                            }
//                        });
//
//                        new RetrofitRequest<>(ApiRequest.getApiShiji().postFollowBrands(String.valueOf(listInfo.getTag_id()))).handRequest(new MsgCallBack() {
//                            @Override
//                            public void onResult(HttpMessage msg) {
//                                if (msg.isSuccess()) {
//                                    Util.toast(mContext, "已订阅", true);
//                                    listInfo.setFollow(1);
//                                } else {
//                                    Util.toast(mContext, "订阅失败", true);
//                                    viewHolder.fabLike.setLineMorphingState((viewHolder.fabLike.getLineMorphingState() + 1) % 2, true);
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//
//            viewHolder.ivLogo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    // 打点上报
//                    MainRecBrandLogstashItem mainRecBrandLogstashItem = new MainRecBrandLogstashItem();
//                    mainRecBrandLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
//                    mainRecBrandLogstashItem.setBrand_id(String.valueOf(listInfo.getBrand_id()));
//                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainRecBrandEvent(mainRecBrandLogstashItem)).handRequest(new MsgCallBack() {
//                        @Override
//                        public void onResult(HttpMessage msg) {
//                        }
//                    });
//
//                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
//                    intent.putExtra("brand_id", listInfo.getBrand_id());
//                    mContext.startActivity(intent);
//                }
//            });
//
//            viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, NewSingleBrandActivity.class);
//                    intent.putExtra("brand_id", listInfo.getBrand_id());
//                    mContext.startActivity(intent);
//                }
//            });
//
//            viewHolder.ivOne.setOnClickListener(new ClickEvents(listInfo.getGoods().get(0).getGoods_id(), listInfo.getGoods().get(0).getRecommend(), String.valueOf(listInfo.getBrand_id())));
//            viewHolder.ivTwo.setOnClickListener(new ClickEvents(listInfo.getGoods().get(1).getGoods_id(), listInfo.getGoods().get(1).getRecommend(), String.valueOf(listInfo.getBrand_id())));
//            viewHolder.ivThree.setOnClickListener(new ClickEvents(listInfo.getGoods().get(2).getGoods_id(), listInfo.getGoods().get(2).getRecommend(), String.valueOf(listInfo.getBrand_id())));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mLists.size();
//        }
//
//        class RecItmeViewHolder extends RecyclerView.ViewHolder {
//
//            private SimpleDraweeView ivLogo;
//            private TextView tvName;
//            private FloatingActionButton fabLike;
//            private SimpleDraweeView ivOne;
//            private SimpleDraweeView ivTwo;
//            private SimpleDraweeView ivThree;
//            private View vSpace;
//
//            private LinearLayout llytTips;          // 引导提示   tom 2016/7/6
//
//            public RecItmeViewHolder(View itemView) {
//                super(itemView);
//                ivLogo = (SimpleDraweeView) itemView.findViewById(R.id.rec_item_logo);
//                tvName = (TextView) itemView.findViewById(R.id.rec_item_name);
//                fabLike = (FloatingActionButton) itemView.findViewById(R.id.rec_item_like);
//                ivOne = (SimpleDraweeView) itemView.findViewById(R.id.rec_item_one);
//                ivTwo = (SimpleDraweeView) itemView.findViewById(R.id.rec_item_two);
//                ivThree = (SimpleDraweeView) itemView.findViewById(R.id.rec_item_three);
//                vSpace = itemView.findViewById(R.id.rec_item_space);
//                llytTips = (LinearLayout) itemView.findViewById(R.id.llyt_tips);
//            }
//        }
//    }

    class BrandsViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView ivLogo;
        TextView tvName;
        FloatingActionButton fabLike;
        RecyclerView recyclerView;
        TextView tvDesc;
        TextView tvTime;
        TextView tvReason;

        public BrandsViewHolder(View itemView) {
            super(itemView);

            ivLogo = (SimpleDraweeView) itemView.findViewById(R.id.main_brands_logo);
            tvName = (TextView) itemView.findViewById(R.id.main_brands_name);
            fabLike = (FloatingActionButton) itemView.findViewById(R.id.main_brands_like);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.main_brands_recycler);
            tvDesc = (TextView) itemView.findViewById(R.id.main_brands_desc);
            tvTime = (TextView) itemView.findViewById(R.id.main_brands_time);
            tvReason = (TextView) itemView.findViewById(R.id.main_brands_reason);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }
    }

    class BrandsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<BrandsRecInfo.ListEntity.GoodsListEntity> mLists;

        public BrandsAdapter(List<BrandsRecInfo.ListEntity.GoodsListEntity> mLists) {
            this.mLists = mLists;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BrandsItmeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_brands_item_child, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BrandsItmeViewHolder viewHolder = (BrandsItmeViewHolder) holder;
            BrandsRecInfo.ListEntity.GoodsListEntity info = mLists.get(position);
//            Netroid.displayImage(Util.ScaleImageGoodes(info.getCover(), SimpleUtils.dp2px(mContext, 130) - 20), viewHolder.ivImg);
            viewHolder.ivImg.setImageURI(Util.ScaleImageGoodes(info.getCover(), SimpleUtils.dp2px(mContext, 130) - 20));
            viewHolder.tvName.setText(info.getTitle());
            viewHolder.vSpace.setVisibility(position == mLists.size() - 1 ? View.VISIBLE : View.GONE);

            if (!TextUtils.isEmpty(info.getList_price())) {
                if (info.getPrice().equals(info.getList_price())) {
                    viewHolder.tvListPrice.setVisibility(View.GONE);
                    viewHolder.llytDiscount.setVisibility(View.GONE);
                } else {
                    viewHolder.llytDiscount.setVisibility(View.VISIBLE);
                    viewHolder.tvDiscount.setText(Util.FloatKeepZero((float) info.getDiscount()));
                    viewHolder.tvListPrice.setVisibility(View.VISIBLE);
                    viewHolder.tvListPrice.setText("￥" + info.getList_price());
                    viewHolder.tvListPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    FontStyleUtil.setHelveCondenBoldStyle(mContext, viewHolder.tvListPrice);
                }
            }
            viewHolder.tvPrice.setText("￥" + info.getPrice());
            FontStyleUtil.setHelveCondenBoldStyle(mContext, viewHolder.tvPrice);
            viewHolder.ivImg.setOnClickListener(new ClickEvents(info.getId(), info.getRecommend()));
        }

        @Override
        public int getItemCount() {
            return mLists.size();
        }

        class BrandsItmeViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView ivImg;
            TextView tvName;
            TextView tvPrice;
            TextView tvListPrice;
            View vSpace;

            SimpleDraweeView ivThemeIcon;
            LinearLayout llytDiscount;
            TextView tvDiscount;

            public BrandsItmeViewHolder(View itemView) {
                super(itemView);
                ivImg = (SimpleDraweeView) itemView.findViewById(R.id.brands_item_image);
                tvName = (TextView) itemView.findViewById(R.id.brands_item_name);
                tvPrice = (TextView) itemView.findViewById(R.id.brands_item_price);
                tvListPrice = (TextView) itemView.findViewById(R.id.brands_item_list_price);
                vSpace = itemView.findViewById(R.id.brands_item_space);

                ivThemeIcon = (SimpleDraweeView) itemView.findViewById(R.id.iv_theme_icon);
                ivThemeIcon.setImageURI(Util.transferImage(BaseApplication.getInstance().getThemeIcon(), SimpleUtils.dp2px(mContext, 33)));
                llytDiscount = (LinearLayout) itemView.findViewById(R.id.llyt_discount);
                tvDiscount = (TextView) itemView.findViewById(R.id.tv_discount);
            }
        }
    }

    class PairViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        RelativeLayout llFooterMore;
        ImageView ivMore;
        ProgressBar progressBar;

        public PairViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.main_pair_recycler);
            recyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext));
            recyclerView.setNestedScrollingEnabled(false);

            llFooterMore = (RelativeLayout) itemView.findViewById(R.id.ll_footer_more);
            ivMore = (ImageView) itemView.findViewById(R.id.iv_more);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar_more);

            ivMore.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getMatchRecommendList(0, this);
        }
    }

    class PairAdapter extends RecyclerView.Adapter<PairAdapter.PairItemViewHolder> {
        private ArrayList<WorkItem> mList;

        public PairAdapter(ArrayList<WorkItem> mList) {
            this.mList = mList;
        }

        public ArrayList<WorkItem> getList() {
            return this.mList;
        }

        @Override
        public PairAdapter.PairItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PairItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_pair_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final PairAdapter.PairItemViewHolder holder, final int position) {
            final WorkItem item = mList.get(position);
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
                holder.btnFollow.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.btnFollow.setLineMorphingState(1, false);
                    }
                }, 200);
            } else {
                holder.btnFollow.setLineMorphingState(0, false);
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.llTag.getLayoutParams();
            layoutParams.width = SimpleUtils.getScreenWidth(mContext) - SimpleUtils.dp2px(mContext, 2) * 2;
            layoutParams.height = layoutParams.width;
            holder.llTag.setLayoutParams(layoutParams);
            //设置左侧大图
            holder.ivTagImage.setImageURI(Util.transferCropImage(item.getImages().get(0).getUrl(), layoutParams.width / 3));

            //设置右侧是三个图
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
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMatchGoodsEvent(logstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                        }
                    });

                    if (!NetUtil.IsInNetwork(mContext)) {
                        Util.toast(mContext, Configration.OFF_LINE_TIPS, true);
                    } else {
                        goToGoodesDetail(goodsId);
                    }
                }
            });

            holder.btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFollow(item, holder);
                }
            });

            //点击跳转到用户信息页面
            holder.llUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommunityHomePageActivity.class);
                    intent.putExtra("user_id", item.getUser_id());
                    int followed = item.getUser().getFollowed();
                    if (followed == 1) {
                        intent.putExtra("is_follow", true);
                    } else {
                        intent.putExtra("is_follow", false);
                    }
                    mContext.startActivity(intent);
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
                    new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainMatchEvent(logstashItem)).handRequest(new MsgCallBack() {
                        @Override
                        public void onResult(HttpMessage msg) {
                        }
                    });

                    Intent intent = new Intent(mContext, NewMatchDetailActivity.class);
                    intent.putExtra("work_id", item.getWork_id());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        class PairItemViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout llUser;
            SimpleDraweeView ivUserAvatar;
            ImageView ivRedPeople;
            TextView tvUserName;
            FloatingActionButton btnFollow;

            LinearLayout llTag;
            RelativeLayout rlTag;
            SimpleDraweeView ivTagImage;
            ListView lvGoods;

            View viewline;

            public PairItemViewHolder(View itemView) {
                super(itemView);
                llUser = (RelativeLayout) itemView.findViewById(R.id.ll_user);
                ivUserAvatar = (SimpleDraweeView) itemView.findViewById(R.id.iv_user_avatar);
                ivRedPeople = (ImageView) itemView.findViewById(R.id.iv_red_people);
                tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                btnFollow = (FloatingActionButton) itemView.findViewById(R.id.btn_follow);

                llTag = (LinearLayout) itemView.findViewById(R.id.ll_tag);

                rlTag = (RelativeLayout) itemView.findViewById(R.id.rl_tag);
                ivTagImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_tag_image);
                lvGoods = (ListView) itemView.findViewById(R.id.lv_goods);

                viewline = itemView.findViewById(R.id.viewline);
            }
        }
    }


    class ClickEvents implements View.OnClickListener {

        String id;
        String recommend;
        String brand_id;

        public ClickEvents() {
        }

        public ClickEvents(String goodId, String recommend) {
            this.id = goodId;
            this.recommend = recommend;
        }

        public ClickEvents(String goodId, String recommend, String brand_id) {
            this.id = goodId;
            this.recommend = recommend;
            this.brand_id = brand_id;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.main_pic_one:
                    reportcCategoryEvent("女士");
                    goToCategory(126);
                    break;
                case R.id.main_pic_two:
                    reportcCategoryEvent("男士");
                    goToCategory(124);
                    break;
                case R.id.main_pic_three:
                    reportcCategoryEvent("儿童");
                    goToCategory(127);
                    break;
                case R.id.main_pic_four:
                    reportcCategoryEvent("生活");
                    goToCategory(128);
                    break;
                case R.id.brands_item_image:
                    goToGoodesDetail(id);
                    break;
                case R.id.rec_item_one:
                    reportcRecGoodsDetailEvent(brand_id, id);
                    goToGoodesDetail(id);
                    break;
                case R.id.rec_item_two:
                    reportcRecGoodsDetailEvent(brand_id, id);
                    goToGoodesDetail(id);
                    break;
                case R.id.rec_item_three:
                    reportcRecGoodsDetailEvent(brand_id, id);
                    goToGoodesDetail(id);
                    break;
            }
        }
    }

    // 打点事件   分类栏
    private void reportcCategoryEvent(String name) {
        CategoryLogstashItem categoryLogstashItem = new CategoryLogstashItem();
        categoryLogstashItem.setName(name);
        categoryLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
        new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainCagetoryEvent(categoryLogstashItem)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {

            }
        });
    }

    // 打点事件   个性化推荐去商品详情
    private void reportcRecGoodsDetailEvent(String brand_id, String goodsId) {
        MainRecBrandGoodsLogstashItem mainRecBrandGoodsLogstashItem = new MainRecBrandGoodsLogstashItem();
        mainRecBrandGoodsLogstashItem.setBrand_id(brand_id);
        mainRecBrandGoodsLogstashItem.setUser_id(BaseApplication.getInstance().readUserId());
        mainRecBrandGoodsLogstashItem.setGoods_id(goodsId);
        new RetrofitRequest<>(ApiRequest.getApiShijiLogstash().reportMainRecGoodsEvent(mainRecBrandGoodsLogstashItem)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {

            }
        });
    }

    private void goToGoodesDetail(final String goodsId) {
        Intent intent = new Intent(mContext, NewGoodsDetailActivity.class);
        intent.putExtra("goodsId", goodsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    public interface LoginListener {
        void goLogin();

        void notifyData(int id, int position);

        void goToDiscoverFragment(int posoiton);
    }

    private void goToCategory(int typeId) {
        Intent intent = new Intent(mContext, NewCategoryActivity.class);
        intent.putExtra("typeId", typeId);//一级分类编号
        mContext.startActivity(intent);
    }

    /**
     * 获取推荐搭配列表
     *
     * @param offset
     */
    private void getMatchRecommendList(final int offset, final PairViewHolder holder) {
        new RetrofitRequest<WorkObject>(ApiRequest.getApiShiji().getRecommendList(
                MapRequest.setMapMatchRecommendList(offset, 3, "2"))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    WorkObject obj = (WorkObject) msg.obj;
                    if (obj != null && obj.list != null && !obj.list.isEmpty()) {
                        if (obj.list.size() < 3) holder.llFooterMore.setVisibility(View.GONE);
                        if (offset > 0) {
                            pairList.addAll(obj.list);
                            pairAdapter.notifyItemRangeInserted(pairAdapter.getList().size(), obj.list.size());
                        } else if (offset == 0) {
                            pairList = obj.list;
                            pairAdapter = new PairAdapter(pairList);
                            holder.recyclerView.setAdapter(pairAdapter);
                        }
                        holder.progressBar.setVisibility(View.GONE);
                        holder.ivMore.setVisibility(View.VISIBLE);
                    } else {
                        holder.llFooterMore.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 点击关注按钮，关注或取消关注
     */
    private void clickFollow(WorkItem item, PairAdapter.PairItemViewHolder holder) {
        if (item == null || item.getUser() == null)
            return;
        holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
        if (item.getUser().getFollowed() == 1)
            unFollowUser(item, holder);
        else
            followUser(item, holder);
    }

    /**
     * 关注用户 接口 /user/follow-user
     */
    private void followUser(final WorkItem item, final PairAdapter.PairItemViewHolder holder) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    item.getUser().setFollowed(1);
                    Util.toast(mContext, "关注", true);
                } else if (msg.isLossLogin()) {
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                    if (mListener != null) {
                        mListener.goLogin();
                    }
                } else {
                    Util.toast(mContext, "关注失败", true);
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                }
            }
        });
    }

    /**
     * 取消关注用户 接口/user/unfollow-user
     */
    private void unFollowUser(final WorkItem item, final PairAdapter.PairItemViewHolder holder) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().setUnFollow(String.valueOf(item.getUser_id()))).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    item.getUser().setFollowed(2);
                    Util.toast(mContext, "取消关注", true);
                } else if (msg.isLossLogin()) {
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                    if (mListener != null) {
                        mListener.goLogin();
                    }
                } else {
                    Util.toast(mContext, "取消关注失败", true);
                    holder.btnFollow.setLineMorphingState((holder.btnFollow.getLineMorphingState() + 1) % 2, true);
                }
            }
        });
    }
}
