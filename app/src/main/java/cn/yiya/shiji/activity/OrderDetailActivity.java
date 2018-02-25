package cn.yiya.shiji.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.android.PaymentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.LogisticsAdapter;
import cn.yiya.shiji.adapter.OrderGoodesAdapter;
import cn.yiya.shiji.adapter.PackageAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.entity.CMBPayInfo;
import cn.yiya.shiji.entity.OrderDetailInfo;
import cn.yiya.shiji.entity.OrderDetailObject;
import cn.yiya.shiji.entity.OrderSubInfo;
import cn.yiya.shiji.entity.PayModeInfo;
import cn.yiya.shiji.entity.PayModeObject;
import cn.yiya.shiji.entity.ShareWxInfo;
import cn.yiya.shiji.netroid.Netroid;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;
import cn.yiya.shiji.views.ProgressDialog;
import cn.yiya.shiji.views.ShareDialog;

/**
 * Created by chenjian on 2016/3/3.
 */
public class OrderDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ScrollView slContent;                           // 整个布局界面
    private ImageView ivBack;                               // 返回按钮
    private TextView tvTitle;                               // 标题
    private ImageView ivRight;                              // 联系客服
    private LinearLayout llytSubmitInfo;                    // 订单状态变色布局
    private TextView tvOrderStatus;                         // 订单状态文字显示
    private TextView tvOrderNo;                             // 订单号文字显示
    private TextView tvOrderSubmitTime;                     // 下单时间显示
    private TextView tvOrderTip;                            // 订单提示信息显示
    private ImageView ivLine;                               // 分割线

    private RelativeLayout rlReceiverAddress;
    private TextView tvAddressName;                         // 收件人姓名显示
    private TextView tvAddressPhone;                        // 收件人手机号码显示
    private TextView tvAddressId;                           // 收件人身份证号码显示
    private TextView tvAddressCity;                         // 收件人城市显示
    private TextView tvAddressDetail;                       // 收件人详细地址

    private TextView tvOrderGoodes;                         // 订单商品总价格
    private TextView tvOrderFee;                            // 订单平台服务费
    private TextView tvOrderTariff;                         // 订单关税
    private TextView tvOrderDiscount;                       // 订单优惠价格
    private TextView tvOrderTotal;                          // 订单总价格
    private TextView tvOrderRefund;                         // 订单中退款显示

    private RecyclerView mRecyclerView;                     // 包裹个数显示的横向RecycleView布局
    private PackageAdapter mPackageAdapter;                 // 包裹个数适配器
    private ImageView ivCountry;                            // 某个包裹网站所在国家的国旗图片
    private TextView tvPackageSite;                         // 某个包裹发货网站
    private TextView tvPackageDelivery;                     // 某个包裹发货方式
    private AllListView mPackageListView;                   // 包裹中商品展示ListView
    private OrderGoodesAdapter mGoodesAdapter;              // 包裹中商品数据适配器
    private TextView tvPackageGooodes;                      // 包裹中商品总价格
    private TextView tvPackagefee;                          // 包裹中商品官网运费
    private TextView tvPackTariff;                          // 包裹中商品关税
    private TextView tvPackageSubsidy;                      // 包裹中商品是否全额补贴
    private TextView tvPackageTotal;                        // 包裹中总价格
    private TextView tvPackageRefund;                       // 包裹中是否存在退款商品

    private LinearLayout llytPay;                           // 未付款中支付界面
    private RelativeLayout rlytWX;                          // 微信支付布局
    private RelativeLayout rlytZFB;                         // 支付宝支付布局
    private CheckBox cbZFB;                                 // 支付宝选中按钮
    private CheckBox cbWX;                                  // 微信选中按钮
    private RelativeLayout rlytGoToPay;                     // 点击去付款整个界面
    private RelativeLayout rlytTimePay;                     // 去付款按钮界面（包括倒计时）
    private TextView tvTimePay;                             // 去付款倒计时
    private RelativeLayout rlytPackageLogistics;            // 包裹中最新物流信息展示布局
    private TextView tvLatestLogistics;                     // 包裹中最新物流信息
    private TextView tvLatestTime;                          // 包裹中最新物流信息时间
    private ImageView ivLogisticsInfo;                      // 物流信息图片
    private AllListView lvLogistics;                        // 物流详细信息ListView
    private ImageView ivSendRed;                            // 发红包按钮
    private ImageView ivShowOrder;                          // 晒单按钮
    private RelativeLayout rlytPayMode;                     // 支付方式布局
    private LinearLayout llytLogistics;                     // 包裹物流详细信息布局
    private TextView tvPayMode;                             // 支付方式显示
    private TextView tvBackToCart;                          // 加回购物车按钮
    private View vBottom;                                   // 底部View用来控制留白

    private String strOrderNo;                              // 订单号
    private String strSubOrderNo;                           // 子订单号

    /**
     * 子订单状态
     */
    private int nPayStatus;                                 // 保存支付状态
    public static final int ORDER_TO_BE_PAID = 0;           // 支付状态：待支付
    public static final int ORDER_EXCEPTION_PAY = 1;       // 支付状态：支付异常
    public static final int ORDER_SUCCESS_PAY = 2;          // 支付状态：支付成功
    public static final int ORDER_HAD_ORDER = 3;            // 支付状态：已下单
    public static final int ORDER_TIME_OUT = 13;            // 支付状态：超时取消
    public static final int ORDER_CUSTOMER_CANCEL = 14;     // 支付状态：客服取消

    /**
     * 包裹状态
     */
    private int nPackageStatus;                             // 保存包裹状态
    private int nPackagePosition;                           // 当前包裹位置
    public static final int PACKAGE_TO_BE_PAID = 1;         // 包裹状态：待支付
    public static final int PACKAGE_PAY_TAXEX = 2;          // 包裹状态：待缴税
    public static final int PACKAGE_TRANSPORT = 3;          // 包裹状态：运输中
    public static final int PACKAGE_HAD_PAY = 4;            // 包裹状态：已完成
    public static final int PACKAGE_INVALID = 5;            // 包裹状态：无效

    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 招行银行卡支付渠道
     */
    private static final String CHANNEL_CMB = "cmb";

    private RelativeLayout rlytYhk;
    private TextView tvYhk;
    private CheckBox cbYhk;

    private static final int REQUEST_CODE_PAYMENT = 100;
    private static final int REQUEST_CODE_CMB = 101;

    private OrderDetailInfo mData;                          // 订单详情数据

    private Context mContext;
    private long[] countTime;                               // 倒计时
    private boolean bRun = false;                           // 是否正在倒计时
    private Handler mHandler;
    private timeRunnable runnable;
    private String strComment;                              // 订单文字提示状态
    private OrderSubInfo.LogisticInfo mLogisticInfo;        // 运输状态下物流信息
    private boolean bLogisticsHandler;                      // 物流信息是否倒序过
    private boolean bChange;                                // 订单是否产生了变化


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        mContext = OrderDetailActivity.this;

        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri == null) {
                strOrderNo = getIntent().getStringExtra("orderno");
                strSubOrderNo = getIntent().getStringExtra("orderId");
            } else {
                strOrderNo = uri.getQueryParameter("orderNumber");
            }
        }

        initViews();
        initEvents();
        init();
    }


    @Override
    protected void initViews() {
        slContent = (ScrollView) findViewById(R.id.order_content_layout);
        ivBack = (ImageView) findViewById(R.id.title_back);
        ivRight = (ImageView) findViewById(R.id.title_right);
        ivRight.setImageResource(R.mipmap.zaixiankefu);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("订单详情");

        llytSubmitInfo = (LinearLayout) findViewById(R.id.order_submit_layout);
        tvOrderStatus = (TextView) findViewById(R.id.order_info_status);
        tvOrderNo = (TextView) findViewById(R.id.order_info_num);
        tvOrderSubmitTime = (TextView) findViewById(R.id.order_info_time);
        tvOrderTip = (TextView) findViewById(R.id.order_info_tip);
        ivLine = (ImageView) findViewById(R.id.order_line_color);

        rlReceiverAddress = (RelativeLayout) findViewById(R.id.rl_receiver_address);
        tvAddressName = (TextView) findViewById(R.id.order_address_name);
        tvAddressPhone = (TextView) findViewById(R.id.order_address_phone);
        tvAddressId = (TextView) findViewById(R.id.order_address_id);
        tvAddressCity = (TextView) findViewById(R.id.order_address_city);
        tvAddressDetail = (TextView) findViewById(R.id.order_address_detail);

        tvOrderGoodes = (TextView) findViewById(R.id.order_goodes_total_price);
        tvOrderFee = (TextView) findViewById(R.id.order_goodes_fee);
        tvOrderTariff = (TextView) findViewById(R.id.order_goodes_tariff);
        tvOrderDiscount = (TextView) findViewById(R.id.order_goodes_discount);
        tvOrderTotal = (TextView) findViewById(R.id.order_total_price);
        tvOrderRefund = (TextView) findViewById(R.id.order_refund);
        mRecyclerView = (RecyclerView) findViewById(R.id.order_packages_recyclerview);
        ivCountry = (ImageView) findViewById(R.id.order_packages_country_img);
        tvPackageSite = (TextView) findViewById(R.id.order_packages_country_name);
        tvPackageDelivery = (TextView) findViewById(R.id.order_packages_delivery);
        mPackageListView = (AllListView) findViewById(R.id.order_goodes_listview);
        tvPackageGooodes = (TextView) findViewById(R.id.order_packages_goodes_price);
        tvPackagefee = (TextView) findViewById(R.id.order_packages_fee);
        tvPackTariff = (TextView) findViewById(R.id.order_packages_tariff);
        tvPackageSubsidy = (TextView) findViewById(R.id.order_packages_tariff_txt);
        tvPackageTotal = (TextView) findViewById(R.id.order_packages_total_price);
        tvPackageRefund = (TextView) findViewById(R.id.order_packages_refund);

        llytPay = (LinearLayout) findViewById(R.id.order_pay_layout);
        rlytWX = (RelativeLayout) findViewById(R.id.wx_layout);
        cbWX = (CheckBox) findViewById(R.id.cb_wx);
        rlytZFB = (RelativeLayout) findViewById(R.id.zfb_layout);
        cbZFB = (CheckBox) findViewById(R.id.cb_zfb);

        rlytYhk = (RelativeLayout) findViewById(R.id.cmb_layout);
        cbYhk = (CheckBox) findViewById(R.id.cb_cmb);
        tvYhk = (TextView) findViewById(R.id.cmb_txt);

        rlytGoToPay = (RelativeLayout) findViewById(R.id.order_go_pay_layout);
        rlytTimePay = (RelativeLayout) findViewById(R.id.order_goto_pay);
        tvTimePay = (TextView) findViewById(R.id.order_go_pay_time);

        rlytPackageLogistics = (RelativeLayout) findViewById(R.id.order_packages_logistics_info);
        tvLatestLogistics = (TextView) findViewById(R.id.order_packages_logistics_txt);
        tvLatestTime = (TextView) findViewById(R.id.order_packages_logistics_time);
        ivLogisticsInfo = (ImageView) findViewById(R.id.order_logistics_img);
        lvLogistics = (AllListView) findViewById(R.id.order_logistics_listview);
        ivSendRed = (ImageView) findViewById(R.id.order_send_red);
        ivShowOrder = (ImageView) findViewById(R.id.order_show);
        llytLogistics = (LinearLayout) findViewById(R.id.order_logistics_layout);
        rlytPayMode = (RelativeLayout) findViewById(R.id.order_pay_mode_layout);
        tvPayMode = (TextView) findViewById(R.id.order_pay_type);
        tvBackToCart = (TextView) findViewById(R.id.order_back_to_cart);
        vBottom = findViewById(R.id.order_bottom_view);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        rlytWX.setOnClickListener(this);
        rlytZFB.setOnClickListener(this);
        rlytYhk.setOnClickListener(this);
        rlytTimePay.setOnClickListener(this);
        rlytPackageLogistics.setOnClickListener(this);
        ivSendRed.setOnClickListener(this);
        ivShowOrder.setOnClickListener(this);
        tvBackToCart.setOnClickListener(this);
    }

    @Override
    protected void init() {
        mHandler = new Handler(getMainLooper());
        getOrderData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                showPreDialog("正在加载客服系统");
                YiChuangYun.openKF5(YiChuangYun.KF5Chat, this, YiChuangYun.ORDER_DETAIL, strOrderNo, new YiChuangYun.onFinishInitListener() {
                    @Override
                    public void onFinishInit() {
                        hidePreDialog();
                    }
                });
                break;
            case R.id.zfb_layout:
                cbWX.setChecked(false);
                cbYhk.setChecked(false);
                cbZFB.toggle();
                break;
            case R.id.wx_layout:
                cbZFB.setChecked(false);
                cbYhk.setChecked(false);
                cbWX.toggle();
                break;
            case R.id.cmb_layout:
                cbZFB.setChecked(false);
                cbWX.setChecked(false);
                cbYhk.toggle();
                break;
            case R.id.order_goto_pay:
                goToPayOrder();
                break;
            case R.id.order_packages_logistics_info:
                scrollToBottom();
                break;
            case R.id.order_send_red:
                sendRedPackage();
                break;
            case R.id.order_show:
                showOrder();
                break;
            case R.id.order_back_to_cart:
                backToCart();
                break;
        }
    }

    private void getOrderData() {
        showPreDialog("加载订单数据");
        bLogisticsHandler = false;
        new RetrofitRequest<OrderDetailObject>(ApiRequest.getApiShiji().getOrderDetail(strOrderNo)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OrderDetailObject mInfos = (OrderDetailObject) msg.obj;

                            mData = mInfos.getOrder();
                            nPackagePosition = curPackagePosition();

                            updatePackagesData(mData.getSub_orders().size());
                            updateViews(mData.getSub_orders());
                            updateGoodesViews(mData.getSub_orders(), nPackagePosition);
                            if (nPackagePosition > 0) {
                                updatePackageViews(nPackagePosition);
                            }

                            hidePreDialog();
                        } else {
                            hidePreDialog();
                        }
                    }
                }
        );
    }

    private int curPackagePosition() {
        if (TextUtils.isEmpty(strSubOrderNo)) {
            return 0;
        }
        int position = 0;
        int nSize = mData.getSub_orders().size();
        for (int i = 0; i < nSize; i++) {
            if (strSubOrderNo.equals(mData.getSub_orders().get(i).getSub_order_number())) {
                position = i;
            }
        }
        return position;
    }

    // 更新界面包裹支付信息
    private void updateViews(ArrayList<OrderSubInfo> lists) {
        int nSize = lists.size();
        float nGoodes = 0;
        float nService = 0;
        float nOfficial = 0;
        float nInternation = 0;
        float nTraiff = 0;
        float nDiscount = 0;
        float nTotal = 0;
        float nRefund = 0;

        for (int i = 0; i < nSize; i++) {
            ArrayList<OrderSubInfo.OrderGoodesInfo> mGoodesInfo = lists.get(i).getGoodses();
            int nGoodesSize = mGoodesInfo.size();
            for (int j = 0; j < nGoodesSize; j++) {
                nGoodes += mGoodesInfo.get(j).getNum() * mGoodesInfo.get(j).getPrice();
            }

            nService += lists.get(i).getService_fee();
            nOfficial += lists.get(i).getForeign_fee();
            nInternation += lists.get(i).getChannel_fee();
            nTraiff += lists.get(i).getTariff_fee();
            nRefund += lists.get(i).getRefund_fee();
        }
        nDiscount = mData.getDiscount();
        nTotal = nGoodes + nService + nOfficial + nInternation + nTraiff - nDiscount;

        tvOrderGoodes.setText(Util.FloatKeepTwo(nGoodes));
        tvOrderFee.setText(Util.FloatKeepTwo(nService + nOfficial + nInternation));
        tvOrderTariff.setText(Util.FloatKeepTwo(nTraiff));
        tvOrderDiscount.setText(Util.FloatKeepTwo(nDiscount));

        // 使用红包导致最后总价小于零，就显示为零
        if (nTotal < 0)
            tvOrderTotal.setText("0.00");
        else
            tvOrderTotal.setText(Util.FloatKeepTwo(nTotal));

        if (nRefund > 0) {
            tvOrderRefund.setVisibility(View.VISIBLE);
            tvOrderRefund.setText("(已退款" + Util.FloatKeepTwo(nRefund) + "元)");
        } else {
            tvOrderRefund.setVisibility(View.GONE);
        }

        if (mData.isDisplay_address()) {
            rlReceiverAddress.setVisibility(View.VISIBLE);
            // 包裹收件人信息
            tvAddressName.setText(mData.getAddress().getName());
            String phone = mData.getAddress().getMobile();
            phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
            tvAddressPhone.setText(phone);
            tvAddressId.setText(setPersonNo(mData.getAddress().getIdentityNumber()));
            tvAddressCity.setText(mData.getAddress().getProvince() + " " + mData.getAddress().getCity()
                    + " " + mData.getAddress().getDistrict());
            tvAddressDetail.setText(mData.getAddress().getAddress());
        } else {
            rlReceiverAddress.setVisibility(View.GONE);
        }
    }

    // 更新界面商品数据信息, 默认更新第一个包裹
    private void updateGoodesViews(ArrayList<OrderSubInfo> lists, int position) {
        if (lists == null || lists.size() == 0)
            return;

        OrderSubInfo mInfo = lists.get(position);
        ArrayList<OrderSubInfo.OrderGoodesInfo> mGoodsInfo = mInfo.getGoodses();
        int nSize = mGoodsInfo.size();
        float nGoodesPrice = 0;
        float nTotalPrice = 0;

        mGoodesAdapter = new OrderGoodesAdapter(mContext, mInfo.getGoodses());
        mPackageListView.setAdapter(mGoodesAdapter);
        for (int i = 0; i < nSize; i++) {
            nGoodesPrice += mGoodsInfo.get(i).getNum() * mGoodsInfo.get(i).getPrice();
        }

        // 总商品价格减去退款价格，即商品总价
        tvPackageGooodes.setText(Util.FloatKeepTwo(nGoodesPrice));
        tvPackagefee.setText(Util.FloatKeepTwo(mInfo.getForeign_fee() + mInfo.getChannel_fee()));

        nTotalPrice = nGoodesPrice + mInfo.getForeign_fee() + mInfo.getChannel_fee();

        // 关税显示
        if (mInfo.getTariff_fee() == 0) {
            if (mInfo.getFee_info().getTax_fee() > 0) {
                tvPackageSubsidy.setVisibility(View.VISIBLE);
                tvPackageSubsidy.setText(mInfo.getFee_info().getFee_des());
                tvPackTariff.setText("0");
            } else {
                tvPackageSubsidy.setVisibility(View.GONE);
                tvPackTariff.getPaint().setFlags(0);
                tvPackTariff.setText("0.00");
            }
        } else {
            tvPackageSubsidy.setVisibility(View.GONE);
            tvPackTariff.getPaint().setFlags(0);
            tvPackTariff.setText(Util.FloatKeepTwo(mInfo.getTariff_fee()));
            nTotalPrice += mInfo.getTariff_fee();
        }

        // 是否含有退款
        if (mInfo.getRefund_fee() > 0) {
            tvPackageRefund.setVisibility(View.VISIBLE);
            tvPackageRefund.setText("(已退款" + mInfo.getRefund_fee() + "元)");
        } else {
            tvPackageRefund.setVisibility(View.GONE);
        }

        // 合计显示
        tvPackageTotal.setText(Util.FloatKeepTwo(nTotalPrice));

        // 包裹订单状态
        int nStatus = mInfo.getGroup_info().getGroup();
        tvOrderStatus.setText("订单状态：" + mInfo.getGroup_info().getGroupDes());
        tvOrderNo.setText("订单编号：" + mData.getOrder_number());
        tvOrderSubmitTime.setText("下单时间：" + mData.getTime());
        switch (nStatus) {
            case PACKAGE_TO_BE_PAID:    // 待支付
                getPayModeList(strOrderNo);
                setGoPayView(true);
                llytSubmitInfo.setBackgroundColor(Color.parseColor("#ff783e"));
                ivLine.setBackgroundColor(Color.parseColor("#d65017"));
                setOrderColor(true);

                if (runnable == null) {
                    countTime = Util.counterTime(mData.getCurrent_time(), mData.getClose_time());
                    strComment = mInfo.getGroup_info().getComment();
                    runnable = new timeRunnable();
                    bRun = true;
                    mHandler.post(runnable);
                }

                break;
            case PACKAGE_PAY_TAXEX:     // 待缴税
                break;
            case PACKAGE_TRANSPORT:     // 运输中
                llytSubmitInfo.setBackgroundColor(Color.parseColor("#ffffff"));
                ivLine.setBackgroundColor(Color.parseColor("#0c0c0c"));
                setOrderColor(false);
                tvOrderTip.setText(mInfo.getGroup_info().getComment());
                setTransportSuccess(true, position);
                break;
            case PACKAGE_HAD_PAY:       // 已支付
                llytSubmitInfo.setBackgroundColor(Color.parseColor("#ffffff"));
                ivLine.setBackgroundColor(Color.parseColor("#0c0c0c"));
                setOrderColor(false);
                tvOrderTip.setText(mInfo.getGroup_info().getComment());
                setTransportSuccess(true, position);
                break;
            case PACKAGE_INVALID:       // 失效
                llytSubmitInfo.setBackgroundColor(Color.parseColor("#a1a1a1"));
                ivLine.setBackgroundColor(Color.parseColor("#80000000"));
                setOrderColor(true);
                tvOrderTip.setText(mInfo.getGroup_info().getComment());
                setInvalid(true);
                break;
        }

        // 包裹网站信息
        Netroid.displayImage(mInfo.getCountry().getFlag(), ivCountry);
        tvPackageSite.setText(mInfo.getSite().getDes());
        if (mInfo.getDelivery() == 1) {
            tvPackageDelivery.setText("[ 转运 ]");
        } else if (mInfo.getDelivery() == 2) {
            tvPackageDelivery.setText("[ 直邮 ]");
        }

    }

    private void getPayModeList(String orderNo) {
        new RetrofitRequest<PayModeObject>(ApiRequest.getApiShiji().getPayModeList(orderNo)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            PayModeObject object = (PayModeObject) msg.obj;
                            if (object != null) {
                                ArrayList<PayModeInfo> infos = object.getList();
                                for (int i = 0; i < infos.size(); i++) {
                                    PayModeInfo info = infos.get(i);
                                    if (info.bPayCMB() && info.bShow()) {
                                        rlytYhk.setVisibility(View.VISIBLE);
                                        tvYhk.setText(info.getWords());
                                        break;
                                    } else {
                                        rlytYhk.setVisibility(View.GONE);
                                    }
                                }

                                if (rlytYhk.getVisibility() == View.GONE && !cbWX.isChecked() && !cbZFB.isChecked()) {
                                    cbWX.setChecked(true);
                                }
                            }
                        }
                    }
                }
        );
    }

    // 加载包裹数据
    private void updatePackagesData(int nCount) {
        if (mData == null || nCount == 0) {
            return;
        }
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 0; i < nCount; i++) {
            strList.add(switchCase(i + 1));
        }

        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        manager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mPackageAdapter = new PackageAdapter(mContext, strList, nPackagePosition, new PackageAdapter.OnItemClickListenter() {
            @Override
            public void onItemClick(int position) {
                if (nPackagePosition == position) {
                    return;
                }
                updatePackageViews(position);
                nPackagePosition = position;
            }
        });
        mRecyclerView.setAdapter(mPackageAdapter);
    }

    private void setOrderColor(boolean bWhite) {
        int color = bWhite ? Color.parseColor("#ffffff") : Color.parseColor("#0c0c0c");
        tvOrderStatus.setTextColor(color);
        tvOrderNo.setTextColor(color);
        tvOrderSubmitTime.setTextColor(color);
        tvOrderTip.setTextColor(color);
    }

    // 点击包裹，更改界面
    private void updatePackageViews(int position) {
        updateGoodesViews(mData.getSub_orders(), position);
    }

    // 去支付界面
    private void setGoPayView(boolean bShow) {
        if (bShow) {
            setTransportSuccess(false);
            setInvalid(false);
            vBottom.setVisibility(View.VISIBLE);
            llytPay.setVisibility(View.VISIBLE);
            rlytGoToPay.setVisibility(View.VISIBLE);
            rlytTimePay.setVisibility(View.VISIBLE);
            tvBackToCart.setVisibility(View.GONE);
        } else {
            llytPay.setVisibility(View.GONE);
            rlytGoToPay.setVisibility(View.GONE);
        }
    }

    // 运输中界面和已完成界面
    private void setTransportSuccess(boolean bShow) {
        setTransportSuccess(bShow, 0);
    }

    // 运输中界面和已完成界面
    private void setTransportSuccess(boolean bShow, int position) {
        if (bShow) {
            setGoPayView(false);
            setInvalid(false);
            vBottom.setVisibility(View.GONE);
            llytLogistics.setVisibility(View.VISIBLE);
            ivSendRed.setVisibility(View.VISIBLE);
            ivShowOrder.setVisibility(View.VISIBLE);
            rlytPayMode.setVisibility(View.VISIBLE);
            mLogisticInfo = mData.getSub_orders().get(position).getLogistic();
            rlytPackageLogistics.setVisibility(View.VISIBLE);
            // 包裹下面显示最近的物流信息
            List<OrderSubInfo.LogisticInfo.LogisticPointInfo> mInfo = mLogisticInfo.getList();

            if (!bLogisticsHandler) {
                for (int i = 0; i < mData.getSub_orders().size(); i++) {
                    Collections.reverse(mData.getSub_orders().get(i).getLogistic().getList());
                }
                bLogisticsHandler = true;
            }

            if (mInfo != null && mInfo.size() > 0) {
                tvLatestLogistics.setText(mLogisticInfo.getList().get(0).getDesc());
                tvLatestTime.setText(mLogisticInfo.getList().get(0).getTime());
            }

            showLogisticsImg(mData.getSub_orders().get(position).getDelivery(), mInfo);
            lvLogistics.setAdapter(new LogisticsAdapter(mContext, mInfo));
            if (!TextUtils.isEmpty(mData.getPay_channel())) {
                if (mData.getPay_channel().equals(CHANNEL_WECHAT)) {
                    tvPayMode.setText("微信支付");
                } else if (mData.getPay_channel().equals(CHANNEL_ALIPAY)) {
                    tvPayMode.setText("支付宝钱包");
                } else if (mData.getPay_channel().equals(CHANNEL_CMB)) {
                    tvPayMode.setText("银行卡支付");
                }
            }

        } else {
            ivSendRed.setVisibility(View.GONE);
            ivShowOrder.setVisibility(View.GONE);
            llytLogistics.setVisibility(View.GONE);
            rlytPayMode.setVisibility(View.GONE);
            rlytPackageLogistics.setVisibility(View.GONE);
        }
    }

    // 已失效界面
    private void setInvalid(boolean bShow) {
        if (bShow) {
            setTransportSuccess(false);
            setGoPayView(false);

            rlytTimePay.setVisibility(View.GONE);
            if (mData.getStatus() == ORDER_TIME_OUT) {
                rlytGoToPay.setVisibility(View.VISIBLE);
                vBottom.setVisibility(View.VISIBLE);
                rlytPayMode.setVisibility(View.VISIBLE);
            } else {
                vBottom.setVisibility(View.VISIBLE);
                rlytGoToPay.setVisibility(View.GONE);
                rlytPayMode.setVisibility(View.VISIBLE);
            }
            tvBackToCart.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mData.getPay_channel())) {
                if (mData.getPay_channel().equals(CHANNEL_WECHAT)) {
                    tvPayMode.setText("微信支付");
                } else if (mData.getPay_channel().equals(CHANNEL_ALIPAY)) {
                    tvPayMode.setText("支付宝钱包");
                } else if (mData.getPay_channel().equals(CHANNEL_CMB)) {
                    tvPayMode.setText("银行卡支付");
                }
            }

        } else {
            rlytPayMode.setVisibility(View.GONE);
            rlytGoToPay.setVisibility(View.GONE);
        }
    }

    /**
     * 显示雪碧图
     *
     * @param type
     * @param lists
     */
    private void showLogisticsImg(int type, List<OrderSubInfo.LogisticInfo.LogisticPointInfo> lists) {
        int nSize = lists.size();
        int nStatus = 0;
        int resLogistics = R.mipmap.logistics_tax_1;
        for (int i = 0; i < nSize; i++) {
            if (lists.get(i).getStage() > 0) {
                nStatus = lists.get(i).getStage();
                break;
            }
        }

        // 转运
        if (type == 1) {
            switch (nStatus) {
                case 1:
                    resLogistics = R.mipmap.logistics_tax_1;
                    break;
                case 2:
                    resLogistics = R.mipmap.logistics_tax_2;
                    break;
                case 3:
                    resLogistics = R.mipmap.logistics_tax_3;
                    break;
                case 4:
                    resLogistics = R.mipmap.logistics_tax_4;
                    break;
                case 5:
                    resLogistics = R.mipmap.logistics_tax_5;
                    break;
                case 6:
                    resLogistics = R.mipmap.logistics_tax_6;
                    break;
//                case 7:
//                    resLogistics = R.drawable.logistics_tax_7;
//                    break;
                case 8:
                    resLogistics = R.mipmap.logistics_tax_8;
                    break;
                case 9:
                    resLogistics = R.mipmap.logistics_tax_9;
                    break;
                default:
                    resLogistics = R.mipmap.logistics_tax_1;
                    break;
            }
            // 直邮
        } else if (type == 2) {
            switch (nStatus) {
                case 1:
                    resLogistics = R.mipmap.logistics_1;
                    break;
                case 2:
                    resLogistics = R.mipmap.logistics_2;
                    break;
                case 3:
                    resLogistics = R.mipmap.logistics_3;
                    break;
//                case 6:
//                    resLogistics = R.drawable.logistics_4;
//                    break;
                case 9:
                    resLogistics = R.mipmap.logistics_5;
                    break;
                default:
                    resLogistics = R.mipmap.logistics_1;
                    break;
            }
        }

        ivLogisticsInfo.setImageResource(resLogistics);
    }


    private void goToPayOrder() {
        if (!(cbZFB.isChecked() || cbWX.isChecked() || cbYhk.isChecked())) {
            showTips("请选择支付方式");
            return;
        }

        if (cbYhk.isChecked()) {
            goPayCMB();
            return;
        }

        String channel_id = "";
        if (cbZFB.isChecked()) {
            channel_id = CHANNEL_ALIPAY;
        } else if (cbWX.isChecked()) {
            channel_id = CHANNEL_WECHAT;
        }

        showPreDialog("正在提交订单");

        Map<String, String> maps = new HashMap<>();
        maps.put("order_no", strOrderNo);
        maps.put("channel", channel_id);
        new RetrofitRequest<>(ApiRequest.getApiShiji().PayShoppingCartListToPing(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        hidePreDialog();
                        if (msg.isSuccess()) {
                            LinkedTreeMap<String, String> result = (LinkedTreeMap<String, String>) msg.obj;
                            String data = new Gson().toJson(result);
                            Intent intent = new Intent();
                            String packageName = getPackageName();
                            ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
                            intent.setComponent(componentName);
                            intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PAYMENT:
                    String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    if (result.equals("cancel")) {
                        showMsg("你已取消了支付");
                    } else if (result.equals("fail")) {
                        showMsg("支付失败");
                    } else if (result.equals("success")) {
                        bChange = true;
                        showMsg("支付成功");
                        if(!mData.isDisplay_address()) {
                            goEditShopInfo();
                        }
                    } else if (result.equals("invalid")) {
                        showMsg("亲!您还没有安装微信哦");
                    }
                    break;
                case REQUEST_CODE_CMB:

                    if(!mData.isDisplay_address()) {
                        goEditShopInfo();
                    }else {
                        getOrderData();
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            onBackPressed();
        }
    }

    // 店铺订单支付完成后跳转编辑店铺资料界面
    private void goEditShopInfo(){
        Intent intent = new Intent(this, EditUserInfoActivity.class);
        intent.putExtra("typeId", "typeId");
        startActivity(intent);
        finish();
    }

    private void goPayCMB() {
        new RetrofitRequest<CMBPayInfo>(ApiRequest.getApiShiji().getCMBPay(strOrderNo)).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    CMBPayInfo object = (CMBPayInfo) msg.obj;
                    if (object != null) {
                        Intent intent = new Intent(OrderDetailActivity.this, CMBWebViewActivity.class);
                        intent.putExtra("url", object.getUrl());
                        startActivityForResult(intent, REQUEST_CODE_CMB);
                    }
                }
            }
        });
    }

    // 将子包裹的个数转为大写的汉字
    private String switchCase(int i) {
        switch (i) {
            case 1:
                return "包裹一";
            case 2:
                return "包裹二";
            case 3:
                return "包裹三";
            case 4:
                return "包裹四";
            case 5:
                return "包裹五";
            case 6:
                return "包裹六";
            case 7:
                return "包裹七";
            case 8:
                return "包裹八";
            case 9:
                return "包裹九";
            case 10:
                return "包裹十";
        }

        return "";
    }

    public void showMsg(String msg) {
        ProgressDialog.showCustomSingleDialog(OrderDetailActivity.this, msg, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                getOrderData();
            }

            @Override
            public void CancelClick() {

            }
        });
    }


    private void scrollToBottom() {
        mHandler.post(new Runnable() {
            public void run() {
                if (slContent == null)
                    return;

                if (llytLogistics.getVisibility() == View.VISIBLE) {
                    int y = llytLogistics.getTop();
                    slContent.scrollTo(0, y);
                    slContent.smoothScrollTo(0, y);
                }
            }
        });
    }

    // 加回购物车
    private void backToCart() {

        new RetrofitRequest<>(ApiRequest.getApiShiji().gobackShoppingCar(strOrderNo)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ProgressDialog.showCommonScrollDialogCancel(OrderDetailActivity.this, "提示", "您已成功将订单加回购物车", "留在本页", "查看购物车",
                                    new ProgressDialog.DialogClick() {
                                        @Override
                                        public void OkClick() {
                                            Intent intent = new Intent();
                                            intent.setClass(mContext, NewShoppingCartActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void CancelClick() {

                                        }
                                    });
                        } else {
                            showTips("加回失败");
                        }
                    }
                }
        );
    }

    // 晒单
    private void showOrder() {
        Intent intent = new Intent(mContext, ChooseGoodsActivity.class);
        intent.putExtra("orderId", mData.getSub_orders().get(nPackagePosition).getSub_order_number());
        startActivity(intent);
    }

    // 发红包
    private void sendRedPackage() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("uid", strOrderNo);
        maps.put("source", String.valueOf(1));
        new RetrofitRequest<ShareWxInfo>(ApiRequest.getApiShiji().getRedPackageInfo(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShareWxInfo mInfo = (ShareWxInfo) msg.obj;
                            new ShareDialog(mContext, mInfo.getShareTimelineTitle(), mInfo.getShareTimelineImage(),
                                    mInfo.getUrl(), mInfo.getShareAppMessageDesc()).build().show();
                        }
                    }
                }
        );
    }

    class timeRunnable implements Runnable {

        @Override
        public void run() {
            if (bRun) {
                computeTime();
                String minute = countTime[0] < 10 ? "0" + countTime[0] : countTime[0] + "";
                String second = countTime[1] < 10 ? "0" + countTime[1] : countTime[1] + "";
                Pattern p = Pattern.compile("%M|%S");
                Matcher m = p.matcher(strComment);
                StringBuffer sb = new StringBuffer();
                while (m.find()) {
                    if (m.group().equals("%M"))
                        m.appendReplacement(sb, minute);
                    else if (m.group().equals("%S"))
                        m.appendReplacement(sb, second);
                }
                m.appendTail(sb);
                tvOrderTip.setText(sb.toString());
                tvTimePay.setText(minute + ":" + second);
                mHandler.postDelayed(runnable, 1000);
            } else {
                mHandler.removeCallbacks(runnable);
            }
        }
    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        countTime[1]--;
        if (countTime[1] < 0) {
            countTime[0]--;
            countTime[1] = 59;
            if (countTime[0] < 0) {
                bRun = false;
                countTime[0] = 0;
                countTime[1] = 0;
                rlytTimePay.setBackgroundColor(Color.parseColor("#d8d8d8"));
                bChange = true;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null)
            mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        if (bChange) {
            setResult(RESULT_OK);
        }
        finish();
    }

    private String setPersonNo(String no) {
        if (no.length() < 4) {
            return "";
        }

        int sub = no.length() / 4;
        StringBuilder sb = new StringBuilder(no);
        for (int i = sub; i < no.length() - sub; i++) {
            sb.setCharAt(i, '*');
        }

        return sb.toString();
    }
}
