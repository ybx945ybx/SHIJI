package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.LogisticsAdapter;
import cn.yiya.shiji.adapter.OrderGoodesAdapter;
import cn.yiya.shiji.adapter.PackageAdapter;
import cn.yiya.shiji.adapter.ShopOrderCashadapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.entity.OrderCashInfo;
import cn.yiya.shiji.entity.OrderDetailInfo;
import cn.yiya.shiji.entity.OrderDetailObject;
import cn.yiya.shiji.entity.OrderSubInfo;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.AllListView;
import cn.yiya.shiji.views.FullyLinearLayoutManager;

/**
 * 店铺订单详情
 * Created by Amy on 2016/10/19.
 */

public class ShopOrderDetailActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private String orderNumber = "";
    private ImageView ivBack;
    private TextView tvTitle;

    /*订单号部分*/
    private TextView tvOrderNum;
    private TextView tvOrderTime;
    /*佣金部分*/
    private TextView tvOrderCash;
    private RecyclerView rvOrderCash;
    /*收货人信息部分*/
    private RelativeLayout rlReceiverAddress;
    private TextView tvOrderName, tvOrderPhone, tvOrderIdentity, tvOrderCity, tvOrderAddress;
    /*商品总价 关税 运费 优惠*/
    private TextView tvOrderPrice, tvOrderFee, tvOrderTariff, tvOrderDiscount;
    /*订单总计 退款*/
    private TextView tvOrderTotalMoney, tvOrderRefund;
    /*包裹 横向*/
    private RecyclerView rvOrderPackages;
    /*包裹商品官网*/
    private SimpleDraweeView ivPackageSite;
    private TextView tvPackageSiteName, tvPackageDelivery;
    /*包裹下商品列表*/
    private AllListView lvPackageGoods;
    /*包裹下的商品总价 运费 关税 合计*/
    private TextView tvPackagePrice, tvPackageFee, tvPackageTariff, tvPackageTariffDesc, tvPackageTotalMoney, tvPackageRefund;
    /*查看物流*/
    private TextView tvShowLogistics;
    /*物流信息部分*/
    private LinearLayout llPackageLogistics;
    private ImageView ivPackageLogistics;
    private AllListView lvPackageLogistics;

    private OrderDetailInfo mData;
    private ArrayList<OrderSubInfo> orderSubList = new ArrayList<>();//包裹列表
    private int nPackagePosition = 0; //当前选择的包裹的position
    private boolean isShow = true;

    public static final int PACKAGE_TO_BE_PAID = 1;         // 包裹状态：待支付
    public static final int PACKAGE_PAY_TAXEX = 2;          // 包裹状态：待缴税
    public static final int PACKAGE_TRANSPORT = 3;          // 包裹状态：运输中
    public static final int PACKAGE_HAD_PAY = 4;            // 包裹状态：已完成
    public static final int PACKAGE_INVALID = 5;            // 包裹状态：无效

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order_detail);
        initIntent();
        initViews();
        initEvents();
        init();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderNumber = intent.getStringExtra("order_number");
        }
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("订单详情");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        tvOrderNum = (TextView) findViewById(R.id.tv_order_num);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);

        tvOrderCash = (TextView) findViewById(R.id.tv_order_cash);
        rvOrderCash = (RecyclerView) findViewById(R.id.rv_order_cash);
        rvOrderCash.setLayoutManager(new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rlReceiverAddress= (RelativeLayout) findViewById(R.id.rl_receiver_address);
        tvOrderName = (TextView) findViewById(R.id.tv_order_name);
        tvOrderPhone = (TextView) findViewById(R.id.tv_order_phone);
        tvOrderIdentity = (TextView) findViewById(R.id.tv_order_identity);
        tvOrderCity = (TextView) findViewById(R.id.tv_order_city);
        tvOrderAddress = (TextView) findViewById(R.id.tv_order_address);

        tvOrderPrice = (TextView) findViewById(R.id.tv_order_price);
        tvOrderFee = (TextView) findViewById(R.id.tv_order_fee);
        tvOrderTariff = (TextView) findViewById(R.id.tv_order_tariff);
        tvOrderDiscount = (TextView) findViewById(R.id.tv_order_discount);

        tvOrderTotalMoney = (TextView) findViewById(R.id.tv_order_total_money);
        tvOrderRefund = (TextView) findViewById(R.id.tv_order_refund);

        rvOrderPackages = (RecyclerView) findViewById(R.id.rv_order_packages);

        ivPackageSite = (SimpleDraweeView) findViewById(R.id.iv_package_site);
        tvPackageSiteName = (TextView) findViewById(R.id.tv_package_site_name);
        tvPackageDelivery = (TextView) findViewById(R.id.tv_package_delivery);

        lvPackageGoods = (AllListView) findViewById(R.id.lv_package_goods);

        tvPackagePrice = (TextView) findViewById(R.id.tv_package_price);
        tvPackageFee = (TextView) findViewById(R.id.tv_package_fee);
        tvPackageTariff = (TextView) findViewById(R.id.tv_package_tariff);
        tvPackageTariffDesc = (TextView) findViewById(R.id.tv_package_tariff_desc);
        tvPackageTotalMoney = (TextView) findViewById(R.id.tv_package_total_money);
        tvPackageRefund = (TextView) findViewById(R.id.tv_package_refund);

        tvShowLogistics = (TextView) findViewById(R.id.tv_show_logistics);

        llPackageLogistics = (LinearLayout) findViewById(R.id.ll_package_logistics);
        ivPackageLogistics = (ImageView) findViewById(R.id.iv_package_logistics);
        lvPackageLogistics = (AllListView) findViewById(R.id.lv_package_logistics);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvShowLogistics.setOnClickListener(this);
    }

    @Override
    protected void init() {
        getOrderDetail();
        getStoreOrderCashInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_show_logistics:
                if (isShow) {
                    llPackageLogistics.setVisibility(View.VISIBLE);
                    isShow = false;
                    tvShowLogistics.setText("收起物流");
                } else {
                    llPackageLogistics.setVisibility(View.GONE);
                    isShow = true;
                    tvShowLogistics.setText("查看物流");
                }
                break;
            default:
                break;
        }
    }

    private void getOrderDetail() {
        showPreDialog("加载订单数据");
        new RetrofitRequest<OrderDetailObject>(ApiRequest.getApiShiji().getOrderDetail(orderNumber))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OrderDetailObject obj = (OrderDetailObject) msg.obj;
                            if (obj != null) {
                                mData = obj.getOrder();
                                orderSubList = mData.getSub_orders();
                                updateViews();
                                reverseLogistic();
                                updatePackagesData(orderSubList.size());
                            }
                        }
                        hidePreDialog();
                    }
                });
    }

    /**
     * 更新订单编号  包裹收件人信息 商品总价 运费 关税 优惠 订单合计
     */
    private void updateViews() {
        float nGoods = 0; //商品总价
        float nService = 0;
        float nOfficial = 0;
        float nInternation = 0;
        float nTraiff = 0;
        float nDiscount = 0; //优惠
        float nTotal = 0; //合计
        float nRefund = 0;

        //订单编号
        tvOrderNum.setText("订单编号：" + mData.getOrder_number());
        tvOrderTime.setText("下单时间：" + mData.getTime());

        if(mData.isDisplay_address()) {
            rlReceiverAddress.setVisibility(View.VISIBLE);
            // 包裹收件人信息
            tvOrderName.setText(mData.getAddress().getName());
            tvOrderPhone.setText(mData.getAddress().getMobile());
            tvOrderIdentity.setText(setPersonNo(mData.getAddress().getIdentityNumber()));
            tvOrderCity.setText(mData.getAddress().getProvince() + " " + mData.getAddress().getCity()
                    + " " + mData.getAddress().getDistrict());
            tvOrderAddress.setText(mData.getAddress().getAddress());
        }else {
            rlReceiverAddress.setVisibility(View.GONE);
        }

        //商品总价 运费 关税 优惠 订单合计
        for (int i = 0; i < orderSubList.size(); i++) {
            ArrayList<OrderSubInfo.OrderGoodesInfo> mGoodesInfo = orderSubList.get(i).getGoodses(); //包裹下商品信息列表
            for (int j = 0; j < mGoodesInfo.size(); j++) {
                nGoods += mGoodesInfo.get(j).getNum() * mGoodesInfo.get(j).getPrice();
            }

            nService += orderSubList.get(i).getService_fee();
            nOfficial += orderSubList.get(i).getForeign_fee();
            nInternation += orderSubList.get(i).getChannel_fee();
            nTraiff += orderSubList.get(i).getTariff_fee();
            nRefund += orderSubList.get(i).getRefund_fee();
        }
        nDiscount = mData.getDiscount();
        nTotal = nGoods + nService + nOfficial + nInternation + nTraiff - nDiscount;

        tvOrderPrice.setText(Util.FloatKeepTwo(nGoods));
        tvOrderFee.setText(Util.FloatKeepTwo(nService + nOfficial + nInternation));
        tvOrderTariff.setText(Util.FloatKeepTwo(nTraiff));
        tvOrderDiscount.setText(Util.FloatKeepTwo(nDiscount));

        if (nTotal < 0) {
            tvOrderTotalMoney.setText("0.00");
        } else {
            tvOrderTotalMoney.setText(Util.FloatKeepTwo(nTotal));
        }

        if (nRefund > 0) {
            tvOrderRefund.setVisibility(View.VISIBLE);
            tvOrderRefund.setText("(已退款" + Util.FloatKeepTwo(nRefund) + "元)");
        } else {
            tvOrderRefund.setVisibility(View.GONE);
        }
    }

    /**
     * 加载包裹数据
     *
     * @param nCount
     */
    private void updatePackagesData(int nCount) {
        if (mData == null || nCount == 0) {
            return;
        }
        ArrayList<String> strList = new ArrayList<>();
        for (int i = 0; i < nCount; i++) {
            strList.add(switchCase(i + 1));
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        manager.setSmoothScrollbarEnabled(true);
        rvOrderPackages.setLayoutManager(manager);

        PackageAdapter mPackageAdapter = new PackageAdapter(this, strList, nPackagePosition, new PackageAdapter.OnItemClickListenter() {
            @Override
            public void onItemClick(int position) {
                if (nPackagePosition == position) {
                    return;
                }
                updateGoodsViews(position);
                nPackagePosition = position;
            }
        });
        rvOrderPackages.setAdapter(mPackageAdapter);

        //初始 先加载下包裹下的商品列表和物流信息
        updateGoodsViews(nPackagePosition);
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


    /**
     * 更新包裹下商品列表以及物流信息
     *
     * @param position
     */
    private void updateGoodsViews(int position) {
        if (orderSubList == null || orderSubList.size() == 0)
            return;

        OrderSubInfo mInfo = orderSubList.get(position);
        ArrayList<OrderSubInfo.OrderGoodesInfo> mGoodsInfo = mInfo.getGoodses();  //包裹下的商品列表
        float nGoodesPrice = 0;
        float nTotalPrice = 0;

        OrderGoodesAdapter mGoodesAdapter = new OrderGoodesAdapter(this, mInfo.getGoodses());
        lvPackageGoods.setAdapter(mGoodesAdapter);

        for (int i = 0; i < mGoodsInfo.size(); i++) {
            nGoodesPrice += mGoodsInfo.get(i).getNum() * mGoodsInfo.get(i).getPrice();
        }

        // 总商品价格减去退款价格，即商品总价
        tvPackagePrice.setText(Util.FloatKeepTwo(nGoodesPrice));
        tvPackageFee.setText(Util.FloatKeepTwo(mInfo.getForeign_fee() + mInfo.getChannel_fee()));

        nTotalPrice = nGoodesPrice + mInfo.getForeign_fee() + mInfo.getChannel_fee();

        // 关税显示
        if (mInfo.getTariff_fee() == 0) {
            if (mInfo.getFee_info().getTax_fee() > 0) {
                tvPackageTariffDesc.setVisibility(View.VISIBLE);
                tvPackageTariffDesc.setText(mInfo.getFee_info().getFee_des());
                tvPackageTariff.setText("0");
            } else {
                tvPackageTariffDesc.setVisibility(View.GONE);
                tvPackageTariff.setText("0.00");
            }
        } else {
            tvPackageTariffDesc.setVisibility(View.GONE);
            tvPackageTariff.setText(Util.FloatKeepTwo(mInfo.getTariff_fee()));
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
        tvPackageTotalMoney.setText(Util.FloatKeepTwo(nTotalPrice));

        // 包裹网站信息
        ivPackageSite.setImageURI(mInfo.getCountry().getFlag());
        tvPackageSiteName.setText(mInfo.getSite().getDes());
        if (mInfo.getDelivery() == 1) {
            tvPackageDelivery.setText("[ 转运 ]");
        } else if (mInfo.getDelivery() == 2) {
            tvPackageDelivery.setText("[ 直邮 ]");
        }

        int nStatus = mInfo.getGroup_info().getGroup();
        switch (nStatus) {
            case PACKAGE_TO_BE_PAID:    // 待支付
            case PACKAGE_INVALID:    // 失效
                tvShowLogistics.setVisibility(View.GONE);
                llPackageLogistics.setVisibility(View.GONE);
                break;
            case PACKAGE_TRANSPORT:     // 运输中
            case PACKAGE_HAD_PAY:     //已完成
                setLogistics(position);
                break;
            default:
                break;
        }
    }

    /**
     * 更新物流列表
     *
     * @param position
     */
    private void setLogistics(int position) {
        tvShowLogistics.setText("查看物流");
        isShow = true;
        llPackageLogistics.setVisibility(View.GONE);
        List<OrderSubInfo.LogisticInfo.LogisticPointInfo> logList = orderSubList.get(position).getLogistic().getList();
        if (logList == null || logList.isEmpty()) {
            tvShowLogistics.setVisibility(View.GONE);
        } else {
            tvShowLogistics.setVisibility(View.VISIBLE);
        }
        showLogisticsImg(orderSubList.get(position).getDelivery(), logList);
        lvPackageLogistics.setAdapter(new LogisticsAdapter(this, logList));
    }

    /**
     * 倒置物流信息
     */
    private void reverseLogistic() {
        for (int i = 0; i < orderSubList.size(); i++) {
            Collections.reverse(orderSubList.get(i).getLogistic().getList());
        }
    }

    /**
     * 将子包裹的个数转为大写的汉字
     *
     * @param i
     * @return
     */
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

    /**
     * 显示雪碧图
     *
     * @param type  1转运 2 直邮
     * @param lists
     */
    private void showLogisticsImg(int type, List<OrderSubInfo.LogisticInfo.LogisticPointInfo> lists) {
        int nSize = lists.size();
        int nStatus = 0; //0:非关键状态,1:付款成功，2:已经下单，3:电商发货，4:到达转运仓，5:转运仓发货，6:清关，7:交税，8:到达国内，9:完成
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

        ivPackageLogistics.setImageResource(resLogistics);
    }

    /**
     * 获取店铺单个订单佣金明细
     */
    private void getStoreOrderCashInfo() {
        new RetrofitRequest<OrderCashInfo>(ApiRequest.getApiShiji().getStoreOrderCashInfo(orderNumber))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            OrderCashInfo obj = (OrderCashInfo) msg.obj;
                            if (obj != null) {
                                tvOrderCash.setText("¥ " + Util.FloatKeepTwo(obj.getCash_amount()));
                                ShopOrderCashadapter cashadapter = new ShopOrderCashadapter(ShopOrderDetailActivity.this, obj.getGoods_list());
                                rvOrderCash.setAdapter(cashadapter);
                            } else {
                                tvOrderCash.setText("¥ 0.00");
                                rvOrderCash.setVisibility(View.GONE);
                            }
                        } else {
                            tvOrderCash.setText("¥ 0.00");
                            rvOrderCash.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
