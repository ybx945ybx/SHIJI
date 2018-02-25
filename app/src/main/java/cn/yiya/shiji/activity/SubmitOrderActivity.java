package cn.yiya.shiji.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.PingppLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.ShoppingCartAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.config.BaseApplication;
import cn.yiya.shiji.config.CustomEvent;
import cn.yiya.shiji.entity.AddressListItem;
import cn.yiya.shiji.entity.CMBPayInfo;
import cn.yiya.shiji.entity.ChannelInfo;
import cn.yiya.shiji.entity.OrderNo;
import cn.yiya.shiji.entity.PayModeInfo;
import cn.yiya.shiji.entity.PayModeObject;
import cn.yiya.shiji.entity.PayOrderNoPost;
import cn.yiya.shiji.entity.RedPackageCountEntity;
import cn.yiya.shiji.entity.RedPackageObject;
import cn.yiya.shiji.entity.RedPackagrItem;
import cn.yiya.shiji.entity.ShoppingCartGoods;
import cn.yiya.shiji.entity.ShoppingCartGroup;
import cn.yiya.shiji.entity.ShoppingCartList;
import cn.yiya.shiji.entity.ShoppingCartObject;
import cn.yiya.shiji.entity.ShoppingCartPost;
import cn.yiya.shiji.entity.ShoppingCartPostObject;
import cn.yiya.shiji.entity.SkusObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.ProgressDialog;

/**
 * 确认提交订单
 * Created by yiya on 2015/9/7.
 */
public class SubmitOrderActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ExpandableListView mListView;
    private ShoppingCartAdapter mAdapter;
    private ArrayList<ShoppingCartGroup> mGroup;
    private ArrayList<ArrayList<ShoppingCartGoods>> mChilds;
//    private List<ShoppingCart> mChild;

    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView tvEdit;
    private TextView tvForeignFee;

    private RelativeLayout rlytAddress;     // 已选好地址布局
    private RelativeLayout rlytAddAddress;  // 添加新地址
    private TextView tvName;                // 收件人名字
    private TextView tvMobile;              // 收件人号码
    private TextView tvOrderNo;             // 收件人身份证号码
    private TextView tvCity;                // 收件人城市地址
    private TextView tvAddress;             // 收件人详细地址

    private String strAddressJson = "";     // 从地址列表选择后返回的结构体

    private RelativeLayout rlytAccount;
    private LinearLayout llytSubmit;
    private RelativeLayout rlytAdress;
    private RelativeLayout rlytInternational;
    private String json;
    private float foreignFee = 0;
    private float totalFee = 0;
    private float GoodsFee = 0;
    private float redFee;
    private float cashFee = 0;
    private boolean bUseRed;
    private TextView tvTotal;
    private TextView tvAccount;
    private RelativeLayout rlytZFB;
    private CheckBox cbZFB;
    private RelativeLayout rlytWX;
    private CheckBox cbWX;
    private CheckBox cbYhk;
    private RelativeLayout rlytYhk;
    private TextView tvYhk;
    private TextView tvWx;
    private TextView tvZfb;
    private String strOrderNo;
    private RelativeLayout rlytRedpacket;
    private TextView tvRedPacketNum;
    private int addressId = 0;
    private int redPackageID;
    private float weight;
    private TextView tvRed;
    private CheckBox cbAccept;
    private TextView tvAccept;
    private String unCart;
    private ShoppingCartAdapter.updatePriceListener mListener;
    private SwipeRefreshLayout sLayout;

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static final int REQUEST_CODE_LOGIN = 2001;
    private static final int REQUEST_CODE_CMB = 2002;
    private static final int REQUEST_ORDER = 2003;

    private ArrayList<SkusObject.Skus> skuList = new ArrayList<>();

    /**
     * 现金账户支付渠道
     */
    private static final String CHANNEL_CASH = "cash";
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

    public static final int ADDRESS_SELECT = 200;
    public static final int RED_PACKAGE = 201;
    private TextView tvRedFund;
    private LinearLayout llytPay;
    private float redDiff;
    private float goodsTotal;

    private boolean directBuy;          // 是否是商品详情界面直接购买跳转过来结算的

    private RelativeLayout cashLayout;
    private TextView tvCash;
    private CheckBox cbCash;
    private boolean haveShop;
    private int cashType; //1 >=总金额   0 < 总金额
    private float diffCash;
    private boolean hasAddDiff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shopcart);
        PingppLog.DEBUG = true;
        initViews();
        initEvents();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.add_address:
            case R.id.address_layout:
                Intent intent = new Intent(SubmitOrderActivity.this, AddressManageActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, ADDRESS_SELECT);
                break;
            case R.id.go_to_pay:
                goPaytoService();
                break;
            case R.id.zfb_layout:
                cbWX.setChecked(false);
                cbYhk.setChecked(false);
                cbZFB.toggle();
                if (cashType == 1) {
                    cbCash.setChecked(false);
                    setAddDiff();
                }
                break;
            case R.id.wx_layout:
                cbZFB.setChecked(false);
                cbYhk.setChecked(false);
                cbWX.toggle();
                if (cashType == 1) {
                    cbCash.setChecked(false);
                    setAddDiff();
                }
                break;
            case R.id.cmb_layout:
                cbZFB.setChecked(false);
                cbWX.setChecked(false);
                cbYhk.toggle();
                if (cashType == 1) {
                    cbCash.setChecked(false);
                    setAddDiff();
                }
                break;
            case R.id.cash_layout:
                if (cashFee == 0.0) return;
                cbCash.toggle();
                if (cashType == 1) {
                    cbZFB.setChecked(false);
                    cbYhk.setChecked(false);
                    cbWX.setChecked(false);
                }
                if (cbCash.isChecked()) {
                    totalFee -= diffCash;
                    hasAddDiff = false;
                } else {
                    totalFee += diffCash;
                    hasAddDiff = true;
                }
                if (totalFee < 0) {
                    tvTotal.setText("0.00");
                } else {
                    tvTotal.setText(Util.FloatKeepTwo(totalFee));
                }
                break;
            case R.id.red_packet_layout:
                Intent intentAct = new Intent(SubmitOrderActivity.this, RedPackageActivity.class);
                intentAct.putExtra("type", 2);
                intentAct.putExtra("fee", GoodsFee);
                if (redPackageID > 0) {
                    intentAct.putExtra("id", redPackageID);
                }
                SkusObject skusObject = new SkusObject();
                skusObject.setList(skuList);
                intentAct.putExtra("skus", new Gson().toJson(skusObject));
                startActivityForResult(intentAct, RED_PACKAGE);
                break;
            case R.id.shopcate_accept:
                Intent intentAccept = new Intent(SubmitOrderActivity.this, AcceptActivity.class);
                intentAccept.putExtra("type", 1);
                startActivity(intentAccept);
                break;
        }
    }

    private void setAddDiff() {
        if (!hasAddDiff) {
            totalFee += diffCash;
            hasAddDiff = true;
            if (totalFee < 0) {
                tvTotal.setText("0.00");
            } else {
                tvTotal.setText(Util.FloatKeepTwo(totalFee));
            }
        }
    }

    @Override
    protected void initViews() {
        View header = LayoutInflater.from(this).inflate(R.layout.activity_account, null);
        View footer = LayoutInflater.from(this).inflate(R.layout.pay_mode, null);

        ivBack = (ImageView) findViewById(R.id.title_back);
        ivBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("确认订单");
        tvEdit = (ImageView) findViewById(R.id.title_right);
        tvEdit.setVisibility(View.GONE);

        // 结算界面不可见，提交订单界面可见，添加地址界面可见，已选地址不可见，国际运费界面可见
        rlytAccount = (RelativeLayout) findViewById(R.id.account_layout);
        llytSubmit = (LinearLayout) findViewById(R.id.submit_layout);
        rlytAdress = (RelativeLayout) header.findViewById(R.id.address_layout);
        rlytAddAddress = (RelativeLayout) header.findViewById(R.id.add_address);
        tvForeignFee = (TextView) header.findViewById(R.id.international_fee);
        rlytInternational = (RelativeLayout) header.findViewById(R.id.international_layout);
        tvName = (TextView) header.findViewById(R.id.account_name);
        tvMobile = (TextView) header.findViewById(R.id.account_mobile);
        tvOrderNo = (TextView) header.findViewById(R.id.account_orderno);
        tvCity = (TextView) header.findViewById(R.id.account_city);
        tvAddress = (TextView) header.findViewById(R.id.account_adress);
        tvTotal = (TextView) llytSubmit.findViewById(R.id.account_total);

        llytSubmit.setVisibility(View.VISIBLE);
        rlytAccount.setVisibility(View.GONE);
        rlytInternational.setVisibility(View.GONE);
        if (TextUtils.isEmpty(strAddressJson)) {
            rlytAdress.setVisibility(View.GONE);
            rlytAddAddress.setVisibility(View.VISIBLE);
        } else {
            rlytAdress.setVisibility(View.VISIBLE);
            rlytAddAddress.setVisibility(View.GONE);
        }

        tvForeignFee.setText("￥0");

        rlytAdress.setOnClickListener(this);
        rlytAddAddress.setOnClickListener(this);

        mListView = (ExpandableListView) findViewById(R.id.shop_listview);
        mListView.addHeaderView(header);
        mListView.addFooterView(footer);

        tvAccount = (TextView) findViewById(R.id.go_to_pay);
        rlytZFB = (RelativeLayout) footer.findViewById(R.id.zfb_layout);
        rlytWX = (RelativeLayout) footer.findViewById(R.id.wx_layout);
        rlytYhk = (RelativeLayout) footer.findViewById(R.id.cmb_layout);
        cbZFB = (CheckBox) footer.findViewById(R.id.cb_zfb);
        cbWX = (CheckBox) footer.findViewById(R.id.cb_wx);
        cbYhk = (CheckBox) footer.findViewById(R.id.cb_cmb);
        tvYhk = (TextView) footer.findViewById(R.id.cmb_txt);
        tvWx = (TextView) footer.findViewById(R.id.tv_wx);
        tvZfb = (TextView) footer.findViewById(R.id.tv_zfb);
        tvRed = (TextView) footer.findViewById(R.id.red_text);
        tvRed.setText("");
        cbAccept = (CheckBox) findViewById(R.id.shopcart_accept_cb);
        tvAccept = (TextView) findViewById(R.id.shopcate_accept);
        rlytRedpacket = (RelativeLayout) footer.findViewById(R.id.red_packet_layout);
        tvRedPacketNum = (TextView) footer.findViewById(R.id.tv_red_packet_num);
        tvRedFund = (TextView) footer.findViewById(R.id.red_refund);
        llytPay = (LinearLayout) footer.findViewById(R.id.pay_mode_layout);
        sLayout = ((SwipeRefreshLayout) findViewById(R.id.shop_pulldown));

        mListView.setDivider(null);

        cashLayout = (RelativeLayout) footer.findViewById(R.id.cash_layout);
        tvCash = (TextView) footer.findViewById(R.id.tv_cash);
        cbCash = (CheckBox) footer.findViewById(R.id.cb_cash);
        if (BaseApplication.getInstance().readHaveShop()) {
            cashLayout.setVisibility(View.VISIBLE);
            haveShop = true;
        } else {
            cashLayout.setVisibility(View.GONE);
            haveShop = false;
        }
    }

    @Override
    protected void initEvents() {
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        tvAccount.setOnClickListener(this);
        rlytZFB.setOnClickListener(this);
        rlytWX.setOnClickListener(this);
        rlytYhk.setOnClickListener(this);
        cashLayout.setOnClickListener(this);
        rlytRedpacket.setOnClickListener(this);
        rlytInternational.setOnClickListener(this);
        tvAccept.setOnClickListener(this);

        mListener = new ShoppingCartAdapter.updatePriceListener() {
            @Override
            public void updatePrice(float price, int count) {
                totalFee = totalFee + price;
                tvTotal.setText(Util.FloatKeepTwo(totalFee));
            }
        };

        sLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bUseRed = false;
                getAcountData();

            }
        });
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        if (intent != null) {
            json = intent.getStringExtra("json");
            directBuy = intent.getBooleanExtra("directBuy", false);
            unCart = intent.getStringExtra("uncart");
            cart_ids = intent.getStringExtra("cartids");
            if (directBuy) {
                ShoppingCartPostObject object = new Gson().fromJson(json, ShoppingCartPostObject.class);
                ArrayList<ShoppingCartPost> carts = new ArrayList<>();

                String str64 = object.getData();
                //解码
                byte b[] = android.util.Base64.decode(str64, Base64.DEFAULT);
                String str = new String(b);

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray Jarray = parser.parse(str).getAsJsonArray();

                for (JsonElement obj : Jarray) {
                    ShoppingCartPost cse = gson.fromJson(obj, ShoppingCartPost.class);
                    carts.add(cse);
                }

                payOrderNoPost.setCarts(carts);
            } else {
                payOrderNoPost.setShopping_cart_ids(cart_ids);
            }
        }
        addsetAddressId();
        mGroup = new ArrayList<>();
        mChilds = new ArrayList<>();
        getAcountData();

        if (directBuy) {
            payOrderNoPost.setData_source("0");
        } else {
            payOrderNoPost.setData_source("1");
        }
    }

    private void getAcountData() {
        sLayout.setRefreshing(true);
        new RetrofitRequest<ShoppingCartObject>(ApiRequest.getApiShiji().setShoppingCartList(post)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        foreignFee = 0;
                        sLayout.setRefreshing(false);
                        if (msg.isSuccess()) {
                            if (mGroup != null) {
                                mGroup.clear();
                            }
                            if (mChilds != null) {
                                mChilds.clear();
                            }
                            totalFee = 0;                                                         // 初始化总价
                            int fee = 0;                                                          // 初始化商品价格
                            weight = 0;
                            ShoppingCartObject info = (ShoppingCartObject) msg.obj;
                            ArrayList<ShoppingCartList> mList = info.getList();
                            for (int i = 0; i < mList.size(); i++) {
                                ShoppingCartGroup group = new ShoppingCartGroup();                // 父ListView数据
                                ShoppingCartList newInfo = mList.get(i);
                                group.setCountry(newInfo.getCountry());
                                group.setCountry_flag(newInfo.getCountry_flag());
                                group.setSite(newInfo.getSite());
                                group.setFees(newInfo.getFees());
                                group.setDelivery(newInfo.getDelivery());
                                group.setDelivery_des(newInfo.getDelivery_des());
                                group.setFee(newInfo.getFee());
                                group.setFree_fee(newInfo.getFree_fee());
                                group.setTaxs(newInfo.getTaxs());
                                group.setSite_id(newInfo.getSite_id());
                                group.setSiteDes(mList.get(i).getSiteDes());
                                group.setTotal_fee(newInfo.getTotal_fee());
                                group.setDiscount_fee(newInfo.getDiscount_fee());
                                foreignFee += mList.get(i).getFees().getChannelFee();             // 国际运费根据父ListView循环


                                float gWeight = 0;
                                for (int j = 0; j < mList.get(i).getGoodses().size(); j++) {
                                    ShoppingCartGoods goods = mList.get(i).getGoodses().get(j);
                                    fee += Float.parseFloat(goods.getPrice()) * goods.getNum();                     // 子循环得出单个网站总价格，外面父循环得出所有价格
                                    weight += goods.getWeight() * goods.getNum();                 // 单个网站商品重量
                                    gWeight += goods.getWeight() * goods.getNum();
                                }

                                group.setWeight(gWeight);
                                mGroup.add(group);

                                totalFee += mList.get(i).getFees().getServiceFee();               // 总价加上平台手续费
                                totalFee += mList.get(i).getFees().getForeignFee();               // 总价加上官网运费

                                mChilds.add(mList.get(i).getGoodses());

                                // 添加sku
                                ArrayList<ShoppingCartGoods> goods = mList.get(i).getGoodses();
                                for (int j = 0; j < goods.size(); j++) {
                                    SkusObject.Skus skus = new SkusObject.Skus();
                                    skus.setSku_id(goods.get(j).getSkuId());
                                    skus.setNum(goods.get(j).getNum());
                                    skuList.add(skus);
                                }

                                SkusObject skusObject = new SkusObject();
                                skusObject.setList(skuList);
                                getSkuRedPacketCount(skusObject);
                            }
                            /**
                             * 总价 = 商品总价fee + 平台手续费 + 官网运费 + 国际运费
                             */
                            GoodsFee = fee;
                            totalFee += fee;
                            mAdapter = new ShoppingCartAdapter(SubmitOrderActivity.this, mGroup, mChilds, 1, null, mListener, directBuy);
                            mListView.setAdapter(mAdapter);
                            tvForeignFee.setText("￥" + Util.FloatKeepTwo(foreignFee));

                            totalFee += foreignFee;                                               // 加上国际运费

                            goodsTotal = totalFee;

                            if (redPackageID > 0 && !bUseRed) {
                                totalFee = useRed(redFee);
                                if (totalFee < 0) {
                                    tvTotal.setText("0.00");
                                } else {
                                    tvTotal.setText(Util.FloatKeepTwo(totalFee));
                                }
                            } else {
                                tvTotal.setText(Util.FloatKeepTwo(totalFee));
                            }

                            for (int i = 0; i < mGroup.size(); i++) {
                                mListView.expandGroup(i);
                            }

                            if (haveShop) {
                                cashFee = info.getBalance();
                                tvCash.setText(Util.FloatKeepTwo(cashFee));
                                cbCash.setChecked(true);
                                cbZFB.setChecked(false);
                                cbWX.setChecked(false);

                                if (cashFee >= totalFee) {
                                    cashType = 1;
                                    cbYhk.setChecked(false);
                                    diffCash = totalFee;
                                    totalFee -= diffCash;
                                } else {
                                    cashType = 0;
                                    cbYhk.setChecked(true);
                                    if (cashFee == 0.0) {
                                        cbCash.setChecked(false);
                                        cbCash.setButtonDrawable(R.mipmap.big_check_disable);
                                        cashLayout.setEnabled(false);
                                    }
                                    diffCash = cashFee;
                                    totalFee -= diffCash;
                                }
                                if (totalFee < 0) {
                                    tvTotal.setText("0.00");
                                } else {
                                    tvTotal.setText(Util.FloatKeepTwo(totalFee));
                                }
                            }

                        } else {
                            hidePreDialog();
                            if (msg.isLossLogin()) {
                                showTips("登陆已过期，请重新登陆");
                                Intent intent = new Intent(SubmitOrderActivity.this, LoginActivity.class);
                                intent.putExtra("expired", "expired");
                                startActivityForResult(intent, REQUEST_CODE_LOGIN);
                                overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                            }
                        }
                    }
                }
        );

        getPayModeList(strOrderNo);

    }

    private void getSkuRedPacketCount(SkusObject skusObject) {
        new RetrofitRequest<RedPackageCountEntity>(ApiRequest.getApiShiji().getRedPackageSkuCount(skusObject)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            RedPackageCountEntity entity = (RedPackageCountEntity) msg.obj;
                            if(entity.getCount() > 0){
                                tvRedPacketNum.setVisibility(View.VISIBLE);
                                tvRedPacketNum.setText("可用红包" + entity.getCount() + "个");
                            }else {
                                tvRedPacketNum.setVisibility(View.GONE);
                            }

                        }
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADDRESS_SELECT:
                    if (data != null) {
                        strAddressJson = data.getStringExtra("json");
                        AddressListItem info = new Gson().fromJson(strAddressJson, AddressListItem.class);
                        rlytAdress.setVisibility(View.VISIBLE);
                        rlytAddAddress.setVisibility(View.GONE);
                        tvName.setText(info.getRecipient());
                        tvMobile.setText(info.getMobile());
                        tvOrderNo.setText(info.getIdentity_number());
                        tvCity.setText(info.getProvince() + info.getCity() + info.getDistrict());
                        tvAddress.setText(info.getAddress());
                        addressId = info.getId();

                        bUseRed = false;
                        if (addressId > 0) {
                            addsetAddressId();
//                            getAcountData();

                        }
                        payOrderNoPost.setAddressId(addressId);
                    }

                    break;
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
//                        MobclickAgent.onEvent(this, UMengEvent.OrderCancel);
                        CustomEvent.onEvent(this, ((BaseApplication) getApplication()).getDefaultTracker(), "SubmitOrderActivity", CustomEvent.OrderCancel);
                        showMsg("你已取消了支付");
                    } else if (result.equals("fail")) {
                        showMsg("支付失败");
                    } else if (result.equals("success")) {
//                        MobclickAgent.onEvent(this, UMengEvent.OrderSucess);
                        CustomEvent.onEvent(this, ((BaseApplication) getApplication()).getDefaultTracker(), "SubmitOrderActivity", CustomEvent.OrderSucess);
                        showMsg("支付成功");
                    } else if (result.equals("invalid")) {
                        showMsg("亲!您还没有安装微信哦");
                    }
                    break;
                case REQUEST_CODE_LOGIN:
                    getAcountData();
                    Util.getNewUserPullLayer(this, data);
                    break;
                case REQUEST_CODE_CMB:
                    goMineOrderView();
                    break;
                case RED_PACKAGE:
                    if (data != null) {
                        String red = data.getStringExtra("json");
                        if (TextUtils.isEmpty(red)) {
                            redPackageID = 0;
                            payOrderNoPost.setRedPackageId(redPackageID);
                            totalFee += redFee;
                            redFee = 0;
                            tvRedFund.setVisibility(View.GONE);
                            llytPay.setVisibility(View.VISIBLE);
                            tvRed.setText("");
                            tvTotal.setText(Util.FloatKeepTwo(totalFee));
                            return;
                        }
                        RedPackagrItem info = new Gson().fromJson(red, RedPackagrItem.class);

                        redPackageID = info.getId();
                        payOrderNoPost.setRedPackageId(redPackageID);

                        totalFee = useRed(info.getHandsel() - redFee);
                        if (info.getHandsel() > 0) {
                            tvRed.setText("-￥" + Util.FloatKeepTwo(info.getHandsel()));
                        }
                        redFee = info.getHandsel();

                        if (totalFee < 0) {
                            tvTotal.setText("0.00");
                        } else {
                            tvTotal.setText(Util.FloatKeepTwo(totalFee));
                        }
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == REQUEST_ORDER) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private float useRed(float fee) {
        float difFee = 0;
        if (!haveShop) {
            difFee = totalFee - fee;
            if (difFee < 0) {
                tvRedFund.setVisibility(View.VISIBLE);
                tvRed.setText("-￥" + Util.FloatKeepTwo(goodsTotal));
                redDiff = Math.abs(difFee);
                tvRedFund.setText("红包剩余金额￥" + Util.FloatKeepTwo(redDiff) + "已返回账户");
                llytPay.setVisibility(View.GONE);
            } else {
                tvRedFund.setVisibility(View.GONE);
                llytPay.setVisibility(View.VISIBLE);
                if (difFee == 0) {
                    llytPay.setVisibility(View.GONE);
                }
            }
        } else {
            difFee = totalFee - fee;
            tvRedFund.setVisibility(View.GONE);
            tvRed.setText("-￥" + fee);
        }
        bUseRed = true;
        if (fee == 0) {
            bUseRed = false;
        }
        return difFee;
    }


    /**
     * 提交订单到给服务器得到订单号码,优先检查登陆
     */
    private void goPaytoService() {

        if (!cbAccept.isChecked()) {
            showTips("同意柿集退换货政策才能提交");
            return;
        }

        if (addressId == 0) {
            showTips("请添加收货地址");
            return;
        }
        if (!haveShop) {
            if (!(cbZFB.isChecked() || cbWX.isChecked() || cbYhk.isChecked())) {
                showTips("请选择支付方式");
                return;
            }
        } else {
            if (cashFee == 0.0) {
                if (!(cbZFB.isChecked() || cbWX.isChecked() || cbYhk.isChecked())) {
                    showTips("请选择支付方式");
                    return;
                }
            } else {
                if (!(cbCash.isChecked() || cbZFB.isChecked() || cbWX.isChecked() || cbYhk.isChecked())) {
                    showTips("请选择支付方式");
                    return;
                }

                if (cashType == 0) {
                    if (cbCash.isChecked() && !(cbZFB.isChecked() || cbWX.isChecked() || cbYhk.isChecked())) {
                        showTips("现金账户余额不足，\n请再选择一种支付方式");
                        return;
                    }
                }
            }
            if (cbCash.isChecked()) {
                payOrderNoPost.setBalance(1);
            } else {
                payOrderNoPost.setBalance(0);
            }
        }

//        MobclickAgent.onEvent(this, UMengEvent.SubmitOrder);
        CustomEvent.onEvent(this, ((BaseApplication) getApplication()).getDefaultTracker(), "SubmitOrderActivity", CustomEvent.SubmitOrder);
        showPreDialog("正在提交订单");

        ArrayList<ChannelInfo> channelList = new ArrayList<>();
        for (int i = 0; i < mGroup.size(); i++) {
            ChannelInfo info = new ChannelInfo();
            for (int j = 0; j < mGroup.get(i).getTaxs().size(); j++) {
                if (mGroup.get(i).getTaxs().get(j).isSelect()) {
                    info.setChannel_id(mGroup.get(i).getTaxs().get(j).getId());
                }
                info.setSite_id(mGroup.get(i).getSite_id());
            }
            channelList.add(info);
        }
        payOrderNoPost.setChannels(channelList);

        new RetrofitRequest<OrderNo>(ApiRequest.getApiShiji().PayShoppingCartList(payOrderNoPost)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        hidePreDialog();
                        if (msg.isSuccess()) {
                            OrderNo no = (OrderNo) msg.obj;
                            strOrderNo = no.getOrder_num();

                            if (llytPay.getVisibility() == View.GONE) {
                                if (no.getStatus() == 2) {
                                    Intent intent;
                                    intent = new Intent(SubmitOrderActivity.this, OrderDetailActivity.class);
                                    intent.putExtra("orderno", strOrderNo);
                                    intent.putExtra("orderId", strOrderNo);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    goPayforMoney(CHANNEL_WECHAT);
                                }
                            } else {
                                if (haveShop && totalFee <= 0) {
                                    if (no.getStatus() == 2) {
                                        Intent intent;
                                        intent = new Intent(SubmitOrderActivity.this, OrderDetailActivity.class);
                                        intent.putExtra("orderno", strOrderNo);
                                        intent.putExtra("orderId", strOrderNo);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        goPayforMoney(CHANNEL_WECHAT);
                                    }
                                } else {
                                    if (cbZFB.isChecked()) {
                                        goPayforMoney(CHANNEL_ALIPAY);
                                    } else if (cbWX.isChecked()) {
                                        goPayforMoney(CHANNEL_WECHAT);
                                    } else {
                                        goPayCMB();
                                    }
                                }

                            }
                        } else {
                            showTips("提交失败");
                        }
                    }
                }
        );
    }

    // 获取支付列表方式
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
//                                        break;
                                    } else if(info.bPayWX()){
                                        tvWx.setText(info.getWords());
                                    }else if(info.bPayZFB()){
                                        tvZfb.setText(info.getWords());
                                    }
                                }
                            }

                            if (rlytYhk.getVisibility() == View.GONE && !cbWX.isChecked() && !cbZFB.isChecked()) {
                                cbWX.setChecked(true);
                            }
                        }
                    }
                });
    }

    private void goPayCMB() {
        new RetrofitRequest<CMBPayInfo>(ApiRequest.getApiShiji().getCMBPay(strOrderNo)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            CMBPayInfo object = (CMBPayInfo) msg.obj;
                            if (object != null) {
                                Intent intent = new Intent(SubmitOrderActivity.this, CMBWebViewActivity.class);
                                intent.putExtra("url", object.getUrl());
                                startActivityForResult(intent, REQUEST_CODE_CMB);
                            }
                        }
                    }
                }
        );
    }

    private void goPayforMoney(String type) {

        Map<String, String> maps = new HashMap<>();
        maps.put("order_no", strOrderNo);
        maps.put("channel", type);

        new RetrofitRequest<>(ApiRequest.getApiShiji().PayShoppingCartListToPing(maps)).handRequest(
                new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
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
        );
    }

    public void showMsg(String msg) {
        ProgressDialog.showCustomSingleDialog(SubmitOrderActivity.this, msg, new ProgressDialog.DialogClick() {
            @Override
            public void OkClick() {
                goMineOrderView();
            }

            @Override
            public void CancelClick() {

            }
        });
    }

    // 支付后跳到支付订单详情页面
    private void goMineOrderView() {
        Intent intent = new Intent(SubmitOrderActivity.this, OrderDetailActivity.class);
        intent.putExtra("orderno", strOrderNo);
        intent.putExtra("orderId", strOrderNo);
        startActivityForResult(intent, REQUEST_ORDER);
    }

    public PayOrderNoPost post;

    private void addsetAddressId() {
        post = new PayOrderNoPost();

        if (directBuy) {                         // 商品详情界面
            ShoppingCartPostObject object = new Gson().fromJson(json, ShoppingCartPostObject.class);
            post.setData(object.getData());
            post.setFormat(object.getFormat());
            post.setAddressId(addressId);
        } else {
            post.setAddressId(addressId);
            post.setShopping_cart_ids(cart_ids);
            post.setSource("1");
        }
        if (redPackageID > 0) {
            post.setRedPackageId(redPackageID);
        }
    }

    private String cart_ids = "";
    private PayOrderNoPost payOrderNoPost = new PayOrderNoPost();

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
