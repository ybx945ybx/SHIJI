package cn.yiya.shiji.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.AcceptActivity;
import cn.yiya.shiji.activity.AddressManageActivity;
import cn.yiya.shiji.activity.CashAccountActivity;
import cn.yiya.shiji.activity.CommunityHomePageActivity;
import cn.yiya.shiji.activity.EditSubscibeActivity;
import cn.yiya.shiji.activity.EditUserInfoActivity;
import cn.yiya.shiji.activity.ForwardedGoodsActivity;
import cn.yiya.shiji.activity.InviteShopActivity;
import cn.yiya.shiji.activity.LoginActivity;
import cn.yiya.shiji.activity.MallHomeSkipWebView;
import cn.yiya.shiji.activity.MessageCenterActivity;
import cn.yiya.shiji.activity.MyWalletActivity;
import cn.yiya.shiji.activity.MyWishActivity;
import cn.yiya.shiji.activity.OpenMyShopActivity;
import cn.yiya.shiji.activity.OrderListActivity;
import cn.yiya.shiji.activity.RedPeopleApplyActivity;
import cn.yiya.shiji.activity.RedPeopleTaskActivity;
import cn.yiya.shiji.activity.ShopOrderListActivity;
import cn.yiya.shiji.activity.ShopRecommendActivity;
import cn.yiya.shiji.activity.XiaoShiJiWebViewActivity;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.Configration;
import cn.yiya.shiji.entity.HtmlInfo;
import cn.yiya.shiji.entity.NotifyCount;
import cn.yiya.shiji.entity.ShareSecondEntity;
import cn.yiya.shiji.entity.User;
import cn.yiya.shiji.entity.UserShopInfoEntity;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.SharedPreUtil;
import cn.yiya.shiji.utils.Util;

/**
 * 个人中心界面
 * tomyang 21016/06/13
 */
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {
    private View mView;
    private SwipeRefreshLayout srlytRefresh;
    /*
     * 用户基本信息布局（已登录样式和未登录样式）
     */
    private RelativeLayout rlytLogin;
    private SimpleDraweeView mivAvatar;
    private TextView tvRedLevel;
    private TextView tvName;
    private TextView tvFollows;
    private TextView tvFans;

    private RelativeLayout rlytNoLogin;
    /*
    订单板块
     */
    private RelativeLayout rlytPendingPay;
    private TextView tvPendingPayNum;
    private RelativeLayout rlytTransporting;
    private TextView tvTransportNum;
    private RelativeLayout rlytComplete;
    private RelativeLayout rlytAllOrders;
    /*
    开店板块
     */
    private LinearLayout llytMyShop;
    private RelativeLayout rlytMyShop;
    private LinearLayout ivGoActivity;
    private TextView tvShopName;
    private TextView tvActivityName;
    private LinearLayout llytCashAccount;
    private TextView tvCashSum;
    private LinearLayout llytInviteShop;
    private TextView tvInviteNum;
    private LinearLayout llytMyShopOrder;
    private TextView tvMyOrderNum;
    private TextView tvSellerRecommend;
    private TextView tvSellerForwarded;
    private TextView tvSellerGuide;
    //private RelativeLayout rlytShopGuide;
    private RelativeLayout rlytXiaoshiji;
    private TextView tvXiaoshiji;

    private ImageView llytJoinShop;

    /*
    剩余板块
     */
    private RelativeLayout rlytMyWallet;
    private RelativeLayout rlytMyWish;
    private RelativeLayout rlytMySubscribe;
    private RelativeLayout rlytMyMessage;
    private ImageView ivMessageDot;
    private ImageView ivGoMessage;
    private RelativeLayout rlytMyRegister;
    //    private RelativeLayout rlytMyCoupon;
    private RelativeLayout rlytRedPeopleApply;
    private RelativeLayout rlytRedPeopleTask;
    private RelativeLayout rlytMyDownload;
    private RelativeLayout rlytMyHelpCenter;
    private RelativeLayout rlytMyCustomService;
    private RelativeLayout rlytMyAddress;

    private static final int NOTIFY = 10;
    private static final int PENDING_ORDER = 7;
    private static final int PENDING_RECEIVE_ORDER = 8;
    private final static int REQUEST_EDIT_MINE = 667;
    private final static int REQUEST_LOGIN = 668;
    private final static int REQUEST_MESSAGE = 669;
    private final static int REQUEST_FOLLOW = 672;
    private static final int REQUEST_FANS = 673;
    private static final int REQUEST_ORDER = 674;
    private User mUser;
    private Handler mHandler;

    private boolean bUnLogin;
    private int user_id;
    private int shopId;

    private SwipeRefreshLayout.OnRefreshListener listener;
    private MsgReceiver msgReceiver;
    public boolean openedShop;
    private String shopUrl;
    private ShareSecondEntity share = new ShareSecondEntity();
    private String shopName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_personal_center, null);
        mHandler = new Handler(getActivity().getMainLooper());

        //动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.yiya.shiji.pushReciver");
        getActivity().registerReceiver(msgReceiver, intentFilter);

        initViews();
        initEvents();
        init();
        return mView;
    }

    @Override
    protected void initViews() {
        srlytRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.srlyt_refresh);

        rlytLogin = (RelativeLayout) mView.findViewById(R.id.rlyt_login);
        mivAvatar = (SimpleDraweeView) mView.findViewById(R.id.mine_avatar);
        tvRedLevel = (TextView) mView.findViewById(R.id.tv_red_level);
        tvName = (TextView) mView.findViewById(R.id.tv_name);
        tvFollows = (TextView) mView.findViewById(R.id.tv_follows);
        tvFans = (TextView) mView.findViewById(R.id.tv_fans);
        rlytNoLogin = (RelativeLayout) mView.findViewById(R.id.rlyt_no_login);

        rlytPendingPay = (RelativeLayout) mView.findViewById(R.id.rlyt_pending_pay);
        tvPendingPayNum = (TextView) mView.findViewById(R.id.tv_pending_pay_num);
        rlytTransporting = (RelativeLayout) mView.findViewById(R.id.rlyt_transporting);
        tvTransportNum = (TextView) mView.findViewById(R.id.tv_transporting_num);
        rlytComplete = (RelativeLayout) mView.findViewById(R.id.rlyt_complete);
        rlytAllOrders = (RelativeLayout) mView.findViewById(R.id.rlyt_all_orders);

        llytMyShop = (LinearLayout) mView.findViewById(R.id.llyt_my_shop);
//        rlytMyShop = (RelativeLayout) mView.findViewById(R.id.rlyt_my_shop);
        ivGoActivity = (LinearLayout) mView.findViewById(R.id.iv_go_activity);
        tvShopName = (TextView) mView.findViewById(R.id.shop_name);
        tvActivityName = (TextView) mView.findViewById(R.id.tv_activity_name);
        llytCashAccount = (LinearLayout) mView.findViewById(R.id.llyt_cash_account);
        tvCashSum = (TextView) mView.findViewById(R.id.tv_cash_sum);
        llytInviteShop = (LinearLayout) mView.findViewById(R.id.llyt_invite_shop);
        tvInviteNum = (TextView) mView.findViewById(R.id.tv_invite_sum);
        llytMyShopOrder = (LinearLayout) mView.findViewById(R.id.llyt_shop_order);
        tvMyOrderNum = (TextView) mView.findViewById(R.id.tv_shop_order_sum);
        tvSellerRecommend = (TextView) mView.findViewById(R.id.tv_seller_recommend);
        tvSellerForwarded = (TextView) mView.findViewById(R.id.tv_seller_forwarded);
        tvSellerGuide = (TextView) mView.findViewById(R.id.tv_seller_guide);
        rlytXiaoshiji = (RelativeLayout) mView.findViewById(R.id.rlyt_my_xiaoshiji);
        tvXiaoshiji = (TextView) mView.findViewById(R.id.tv_xiaoshiji);

        llytJoinShop = (ImageView) mView.findViewById(R.id.llyt_jion_shop);

        rlytMyWallet = (RelativeLayout) mView.findViewById(R.id.rlyt_my_wallet);
        rlytMyWish = (RelativeLayout) mView.findViewById(R.id.rlyt_my_wish);
        rlytMySubscribe = (RelativeLayout) mView.findViewById(R.id.rlyt_my_subscribe);
        rlytMyMessage = (RelativeLayout) mView.findViewById(R.id.rlyt_my_message);
        ivMessageDot = (ImageView) mView.findViewById(R.id.iv_message_dot);
        ivGoMessage = (ImageView) mView.findViewById(R.id.iv_go_message);

        rlytMyRegister = (RelativeLayout) mView.findViewById(R.id.rlyt_my_register);
//        rlytMyCoupon = (RelativeLayout) mView.findViewById(R.id.rlyt_my_coupon);
//        rlytMyDownload = (RelativeLayout) mView.findViewById(R.id.rlyt_my_downloard);
        rlytMyHelpCenter = (RelativeLayout) mView.findViewById(R.id.rlyt_my_help_center);
        rlytRedPeopleApply = (RelativeLayout) mView.findViewById(R.id.rlyt_red_people_apply);
        rlytRedPeopleTask = (RelativeLayout) mView.findViewById(R.id.rlyt_red_people_task);
        rlytMyCustomService = (RelativeLayout) mView.findViewById(R.id.rlyt_my_custom_service);
        rlytMyAddress = (RelativeLayout) mView.findViewById(R.id.rlyt_my_address);
    }

    @Override
    public void initEvents() {
        rlytLogin.setOnClickListener(this);
        rlytNoLogin.setOnClickListener(this);
        rlytPendingPay.setOnClickListener(this);
        rlytTransporting.setOnClickListener(this);
        rlytComplete.setOnClickListener(this);
        rlytAllOrders.setOnClickListener(this);
        llytJoinShop.setOnClickListener(this);
//        rlytMyShop.setOnClickListener(this);
        ivGoActivity.setOnClickListener(this);
        tvShopName.setOnClickListener(this);
        llytCashAccount.setOnClickListener(this);
        llytInviteShop.setOnClickListener(this);
        llytMyShopOrder.setOnClickListener(this);
        tvSellerGuide.setOnClickListener(this);
        tvSellerForwarded.setOnClickListener(this);
        tvSellerRecommend.setOnClickListener(this);
        rlytXiaoshiji.setOnClickListener(this);
        rlytMyWallet.setOnClickListener(this);
        rlytMyWish.setOnClickListener(this);
        rlytMySubscribe.setOnClickListener(this);
        rlytMyMessage.setOnClickListener(this);
        rlytMyRegister.setOnClickListener(this);
//        rlytMyCoupon.setOnClickListener(this);
//        rlytMyDownload.setOnClickListener(this);
        rlytMyHelpCenter.setOnClickListener(this);
        rlytRedPeopleApply.setOnClickListener(this);
        rlytRedPeopleTask.setOnClickListener(this);
        rlytMyCustomService.setOnClickListener(this);
        rlytMyAddress.setOnClickListener(this);

        listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserInfo();
                loadNotifyData();
            }
        };
        srlytRefresh.setOnRefreshListener(listener);

        srlytRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlytRefresh.setRefreshing(true);
            }
        });

        listener.onRefresh();

    }

    @Override
    protected void init() {
    }

    @Override
    public void onClick(View view) {
        // 不需要登陆
        // 离线下载
        if (isEffectClick()) {
            if (view.getId() == R.id.rlyt_my_message) {
                Intent intent8 = new Intent(getActivity(), MessageCenterActivity.class);
                startActivityForResult(intent8, REQUEST_MESSAGE);
                return;
            }
//            if (TextUtils.isEmpty(BaseApplication.FileId)) {
//                if (view.getId() == R.id.rlyt_my_coupon) {
//                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
//                    startActivityForResult(intentLogin, REQUEST_LOGIN);
//                    getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
//                    return;
//                }
//            } else {
//                if (view.getId() == R.id.rlyt_my_coupon) {
//                    startActivity(new Intent(getActivity(), MyCouponActivity.class));
//                    return;
//                }
//            }
            // 未登陆
            if (bUnLogin) {
                if (NetUtil.IsInNetwork(getActivity())) {
                    //未登录状态跳转至登录界面
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intentLogin, REQUEST_LOGIN);
                    getActivity().overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                    return;
                } else {
                    showToast(Configration.OFF_LINE_TIPS);
                    return;
                }
            }

            switch (view.getId()) {
                case R.id.rlyt_login:        // 社区主页
                    Intent intent5 = new Intent(getActivity(), CommunityHomePageActivity.class);
                    intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent5.putExtra("isCurUser", true);
                    intent5.putExtra("user_id", user_id);
                    startActivityForResult(intent5, REQUEST_EDIT_MINE);
                    break;
                case R.id.rlyt_pending_pay:  // 待支付
                    goMineOrderView(1);
                    break;
                case R.id.rlyt_transporting: // 运输中
                    goMineOrderView(2);
                    break;
                case R.id.rlyt_all_orders:   // 所有订单
                    goMineOrderView(0);
                    break;
                case R.id.rlyt_complete:     // 已完成订单
                    goMineOrderView(3);
                    break;
                case R.id.llyt_jion_shop:   // 开店流程开始
                    Intent intentOpenShop = new Intent(getActivity(), OpenMyShopActivity.class);
                    intentOpenShop.putExtra("type", 1);
                    startActivity(intentOpenShop);
                    break;
                case R.id.shop_name:
                    Intent intentEditShop = new Intent(getActivity(), EditUserInfoActivity.class);
                    startActivity(intentEditShop);
                    break;
                case R.id.iv_go_activity:    // 我的店铺 卖家消息
                    Intent intentShop = new Intent(getActivity(), MessageCenterActivity.class);
                    intentShop.putExtra("type", 1);
                    startActivity(intentShop);
                    break;
                case R.id.llyt_cash_account:   // 现金账户
                    Intent intentCash = new Intent(getActivity(), CashAccountActivity.class);
                    startActivity(intentCash);
                    break;
                case R.id.llyt_invite_shop:   // 邀请开店
                    Intent intent22 = new Intent(getActivity(), InviteShopActivity.class);
                    intent22.putExtra("shop_id", shopId);
                    startActivity(intent22);
                    break;
                case R.id.llyt_shop_order:   // 我的店铺订单
                    Intent intent23 = new Intent(getActivity(), ShopOrderListActivity.class);
                    intent23.putExtra("shop_id", shopId);
                    startActivity(intent23);
                    break;
                case R.id.tv_seller_guide:   // 店主指南
                    Intent intentGuide = new Intent(getActivity(), AcceptActivity.class);
                    intentGuide.putExtra("type", 3);
                    startActivity(intentGuide);
                    break;
                case R.id.tv_seller_recommend:   // 店主推荐
                    Intent intentRecommend = new Intent(getActivity(), ShopRecommendActivity.class);
                    startActivity(intentRecommend);
                    break;
                case R.id.tv_seller_forwarded:   // 已转发商品
                    Intent intentForwarded = new Intent(getActivity(), ForwardedGoodsActivity.class);
                    startActivity(intentForwarded);
                    break;
                case R.id.rlyt_my_xiaoshiji:   // 我的小柿集
                    Intent intentXiaoshiji = new Intent(getActivity(), XiaoShiJiWebViewActivity.class);
                    intentXiaoshiji.putExtra("url", shopUrl);
                    intentXiaoshiji.putExtra("share", new Gson().toJson(share));
                    intentXiaoshiji.putExtra("name", shopName);
                    startActivity(intentXiaoshiji);
                    break;
                case R.id.rlyt_my_wallet:    // 我的钱包
                    Intent intent7 = new Intent(getActivity(), MyWalletActivity.class);
                    startActivity(intent7);
                    break;
                case R.id.rlyt_my_wish:      // 心愿单
                    Intent intent9 = new Intent(getActivity(), MyWishActivity.class);
                    startActivity(intent9);
                    break;
                case R.id.rlyt_my_subscribe: // 订阅
                    Intent intent12 = new Intent(getActivity(), EditSubscibeActivity.class);
                    startActivity(intent12);
                    break;
//                case R.id.rlyt_my_message:   // 个人消息
//                    Intent intent8 = new Intent(getActivity(), MessageCenterActivity.class);
//                    startActivityForResult(intent8, REQUEST_MESSAGE);
//                    break;
                case R.id.rlyt_my_register:  // 签到有礼
                    HtmlInfo htmlInfo = new HtmlInfo();
                    htmlInfo.setNeed(2);
                    htmlInfo.setTitle("签到有礼");
                    htmlInfo.setUrl("http://h5.qnmami.com/sign/index.html?rand=1465873068611758");
                    Intent intent = new Intent(getActivity(), MallHomeSkipWebView.class);
                    intent.putExtra("data", new Gson().toJson(htmlInfo));
                    startActivity(intent);
                    break;
                case R.id.rlyt_red_people_apply:     // 红人申请
                    Intent intentRedPeople = new Intent(getActivity(), RedPeopleApplyActivity.class);
//                    Intent intentRedPeople = new Intent(getActivity(), RedPeopleTaskActivity.class);
                    startActivity(intentRedPeople);
                    break;
                case R.id.rlyt_red_people_task:     // 红人任务
                    Intent intentRedPeopleTask = new Intent(getActivity(), RedPeopleTaskActivity.class);
                    startActivity(intentRedPeopleTask);
                    break;
                case R.id.rlyt_my_help_center: // 帮助中心
                    showPreDialog("正在加载客服系统");
                    YiChuangYun.openKF5(YiChuangYun.HelpCenter, getActivity(), 0, "", new YiChuangYun.onFinishInitListener() {
                        @Override
                        public void onFinishInit() {
                            hidePreDialog();
                        }
                    });
                    break;
                case R.id.rlyt_my_custom_service: // 在线客服
                    showPreDialog("正在加载客服系统");
                    YiChuangYun.openKF5(YiChuangYun.KF5Chat, getActivity(), 0, "", new YiChuangYun.onFinishInitListener() {
                        @Override
                        public void onFinishInit() {
                            hidePreDialog();
                        }
                    });
                    break;
                case R.id.rlyt_my_address:        // 地址管理
                    Intent intent6 = new Intent(getActivity(), AddressManageActivity.class);
                    intent6.putExtra("isSelect", false);
                    startActivity(intent6);
                    break;
            }
        }

    }

    // 跳转至订单列表界面
    private void goMineOrderView(int type) {
        Intent intent;
        intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, REQUEST_ORDER);
    }

    private void loadNotifyData() {
        new RetrofitRequest<NotifyCount>(ApiRequest.getApiShiji().getNotifyCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                srlytRefresh.setRefreshing(false);
                if (getActivity() == null) {
                    return;
                }
                if (msg.isSuccess()) {
                    NotifyCount obj = (NotifyCount) msg.obj;
                    if (obj != null) {
                        // 待付款
                        if (obj.getPending_pay_order() > 0) {
                            tvPendingPayNum.setText(obj.getPending_pay_order() + "");
                            tvPendingPayNum.setVisibility(View.VISIBLE);
                        } else {
                            tvPendingPayNum.setVisibility(View.INVISIBLE);
                        }
                        // 运输中
                        if (obj.getPending_receive_order() > 0) {
                            tvTransportNum.setText(obj.getPending_receive_order() + "");
                            tvTransportNum.setVisibility(View.VISIBLE);
                        } else {
                            tvTransportNum.setVisibility(View.INVISIBLE);
                        }
                    }

                } else if (msg.isLossLogin()) {       // 判断是否有登录态  tomyang  2016-5-27
                    bUnLogin = true;
                    setLogOut();
                }
            }
        });

        // 判断消息小红点是否显示
        checkMessageDot();
    }

    private void checkMessageDot() {
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    if(getActivity() != null) {
                        if (!TextUtils.isEmpty(SharedPreUtil.getString(getActivity(), SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_MSG, "")) ||
                                !TextUtils.isEmpty(SharedPreUtil.getString(getActivity(), SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_ID, "")) ||
                                !TextUtils.isEmpty(SharedPreUtil.getString(getActivity(), SharedPreUtil.SERVICE_SP, SharedPreUtil.SERVICE_COMMUNITY, ""))) {
                            ivMessageDot.setVisibility(View.VISIBLE);
                            ivGoMessage.setVisibility(View.GONE);
                        } else {
                            ivMessageDot.setVisibility(View.GONE);
                            ivGoMessage.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (msg.isLossLogin()) {       // 判断是否有登录态  tomyang  2016-5-27
                    ivMessageDot.setVisibility(View.GONE);
                    ivGoMessage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getUserInfo() {
        if (getActivity() != null)
            new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
                @Override
                public void onResult(HttpMessage msg) {
                    srlytRefresh.setRefreshing(false);
                    if (getActivity() != null)
                        if (msg.isSuccess()) {
                            mUser = (User) msg.obj;
                            user_id = mUser.getUser_id();
                            openedShop = mUser.isHave_shop();
                            shopId = mUser.getShop_id();
                            updateViews(mUser);
                            if(openedShop){
                                getUserShopInfo();
                            }
                        } else if (msg.isLossLogin()) {
                            bUnLogin = true;
                            setLogOut();
                        }
                }
            });
    }

    private void getUserShopInfo() {
        new RetrofitRequest<UserShopInfoEntity>(ApiRequest.getApiShiji().getUserShopInfo()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if(msg.isSuccess()){
                    UserShopInfoEntity info = (UserShopInfoEntity) msg.obj;
                    shopName = info.getShop_name();
                    tvXiaoshiji.setText(info.getShop_name_type());
                    share.setShareTimelineTitle(info.getShareTimelineTitle());
                    share.setShareTimelineImage(info.getShareTimelineImage());
                    share.setShareAppMessageTitle(info.getShareAppMessageTitle());
                    share.setShareAppMessageImage(info.getShareAppMessageImage());
                    share.setShareAppMessageDesc(info.getShareAppMessageDesc());
                    shopUrl = info.getShop_url();
                }
            }
        });
    }

    private void updateViews(User user) {
        if (user == null) {
            return;
        }
        rlytNoLogin.setVisibility(View.GONE);
        rlytLogin.setVisibility(View.VISIBLE);

        mivAvatar.setImageURI(Util.transfer(user.getHead()));

        tvRedLevel.setText(Util.transferLevel(user.getLevel()));

        tvName.setText("" + user.getName());
        tvFans.setText("粉丝 " + user.getFans_count());
        tvFollows.setText("关注 " + user.getFollowing_count());

        if (user.isHave_shop()) {
            llytMyShop.setVisibility(View.VISIBLE);
            llytJoinShop.setVisibility(View.GONE);
            tvShopName.setText(user.getShop_name());
            tvCashSum.setText(user.getCash_account() + "");
            tvInviteNum.setText(user.getInvite_shop_num() + "");
            tvMyOrderNum.setText(user.getShop_order_num() + "");
            tvActivityName.setText(user.getShop_msg());
        } else {
            llytMyShop.setVisibility(View.GONE);
            llytJoinShop.setVisibility(View.VISIBLE);
        }

        if(user.getRed() == 1){
            rlytRedPeopleTask.setVisibility(View.VISIBLE);
            mView.findViewById(R.id.view_red_task).setVisibility(View.VISIBLE);
            rlytRedPeopleApply.setVisibility(View.GONE);
        }else {
            rlytRedPeopleTask.setVisibility(View.GONE);
            mView.findViewById(R.id.view_red_task).setVisibility(View.GONE);
            rlytRedPeopleApply.setVisibility(View.VISIBLE);
        }
    }

    private void updateUserNumberView() {
        new RetrofitRequest<User>(ApiRequest.getApiShiji().getUserDetail()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    User user = (User) msg.obj;
                    updateViews(user);
                } else if (msg.isLossLogin()) {
                    bUnLogin = true;
                    setLogOut();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (BaseApplication.needRefresh == true) {
            BaseApplication.needRefresh = false;
            bUnLogin = false;
            listener.onRefresh();
        } else {
            loadNotifyData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (BaseApplication.needRefresh == true) {
                BaseApplication.needRefresh = false;
                bUnLogin = false;
                listener.onRefresh();
            } else {
                loadNotifyData();
            }
        }
    }

    private void clearView() {
        if (rlytNoLogin.getVisibility() == View.VISIBLE) {
            return;
        }
        rlytNoLogin.setVisibility(View.VISIBLE);
        rlytLogin.setVisibility(View.INVISIBLE);
        tvPendingPayNum.setVisibility(View.INVISIBLE);
        tvTransportNum.setVisibility(View.INVISIBLE);
        llytJoinShop.setVisibility(View.VISIBLE);
        llytMyShop.setVisibility(View.GONE);
        ivMessageDot.setVisibility(View.GONE);
        ivGoMessage.setVisibility(View.VISIBLE);
    }

    // 更新消息显示个数
    private void updateNotifyNumberView(final int type) {
        new RetrofitRequest<NotifyCount>(ApiRequest.getApiShiji().getNotifyCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (getActivity() == null) {
                    return;
                }
                if (msg.isSuccess()) {
                    NotifyCount obj = (NotifyCount) msg.obj;
                    if (obj != null) {
                        switch (type) {
                            case PENDING_ORDER:
                                if (obj.getPending_pay_order() > 0) {
                                    tvPendingPayNum.setText(obj.getPending_pay_order() + "");
                                    tvPendingPayNum.setVisibility(View.VISIBLE);
                                } else {
                                    tvPendingPayNum.setVisibility(View.INVISIBLE);
                                }

                                if (obj.getPending_receive_order() > 0) {
                                    tvTransportNum.setText(obj.getPending_receive_order() + "");
                                    tvTransportNum.setVisibility(View.VISIBLE);
                                } else {
                                    tvTransportNum.setVisibility(View.INVISIBLE);
                                }
                                break;
                        }
                    }
                } else if (msg.isLossLogin()) {
                    bUnLogin = true;
                    setLogOut();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_EDIT_MINE:
                    if (data != null) {
                        boolean userInfoChanged = data.getBooleanExtra("userInfoChanged", false);
                        if (userInfoChanged) {
                            updateUserNumberView();
                        }
                    }
                    break;
                case REQUEST_MESSAGE:
                    checkMessageDot();
                    break;
                case REQUEST_FOLLOW:
                    updateUserNumberView();
                    break;
                case REQUEST_FANS:
                    updateUserNumberView();
                    break;
                case REQUEST_LOGIN:
                    setLogIn();
                    break;
                case REQUEST_ORDER:
                    updateNotifyNumberView(PENDING_ORDER);
                    break;
            }
        }
    }

    // 设置登陆状态界面
    public void setLogIn() {
        rlytLogin.setVisibility(View.VISIBLE);
        rlytNoLogin.setVisibility(View.INVISIBLE);
        bUnLogin = false;
        listener.onRefresh();
    }


    // 设置未登录界面
    public void setLogOut() {
        bUnLogin = true;
        clearView();
    }

    // 广播接收器
    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ivMessageDot.setVisibility(View.VISIBLE);
            ivGoMessage.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(msgReceiver);
    }
}
