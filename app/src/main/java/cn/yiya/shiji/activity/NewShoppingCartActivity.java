package cn.yiya.shiji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.yiya.shiji.R;
import cn.yiya.shiji.adapter.NewShoppingCartAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.entity.CarCountInfo;
import cn.yiya.shiji.entity.ShoppingCarSourceObject;
import cn.yiya.shiji.entity.ShoppingCartGoods;
import cn.yiya.shiji.entity.ShoppingCartList;
import cn.yiya.shiji.entity.ShoppingCartObject;
import cn.yiya.shiji.utils.NetUtil;
import cn.yiya.shiji.utils.Util;
import cn.yiya.shiji.views.SwipeRecyclerView;

/**
 * 购物车
 * Created by Amy on 2016/11/3.
 */

public class NewShoppingCartActivity extends BaseAppCompatActivity implements View.OnClickListener, NewShoppingCartAdapter.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;

    private LinearLayout llContent;
    private LinearLayout llCheckAll;
    private CheckBox cbAll;
    private TextView tvAmount;
    private TextView tvAccount;

    private SwipeRecyclerView recyclerView;
    private NewShoppingCartAdapter mAdapter;
    private ArrayList<ShoppingCartList> mList;

    private int totalNormalCount;//购物车可结算的商品数量
    private int checkNormalCount;
    private float totalPrice;//总计金额 减掉售罄 减掉满减

    public final static int REQUEST_LOGIN = 1000;
    private boolean isInit = true;

    public ArrayList<CartsInfo> cartsList;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping_cart);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        ivBack = (ImageView) findViewById(R.id.title_back);
        tvTitle = (TextView) findViewById(R.id.title_txt);
        tvTitle.setText("购物车");
        findViewById(R.id.title_right).setVisibility(View.GONE);

        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llCheckAll = (LinearLayout) findViewById(R.id.ll_check_all);
        cbAll = (CheckBox) findViewById(R.id.cb_all);
        tvAmount = (TextView) findViewById(R.id.tv_amount);
        tvAccount = (TextView) findViewById(R.id.tv_account);

        recyclerView = (SwipeRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NewShoppingCartAdapter(this, this);
        recyclerView.setAdapter(mAdapter);

        isLogin();

        addDefaultNullView();
        initDefaultNullView(R.mipmap.zanwugouwuche, "购物车暂无商品", this);
    }

    @Override
    protected void initEvents() {
        ivBack.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
        llCheckAll.setOnClickListener(this);
        tvShopping.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    public void initData(boolean isShow) {
        if (!NetUtil.IsInNetwork(this)) {
            setOffNetView(llContent);
            return;
        }
        getCarCount();
        getShoppingCartData(isShow);
    }

    private void isLogin() {
        new RetrofitRequest<>(ApiRequest.getApiShiji().isLogin()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    init();
                } else if (msg.isLossLogin()) {
                    Intent intent = new Intent(NewShoppingCartActivity.this, LoginActivity.class);
                    startActivityForResult(intent, REQUEST_LOGIN);
                    overridePendingTransition(R.anim.slide_in_top_buttom, R.anim.slide_out_top_buttom);
                } else {
                    if (!NetUtil.IsInNetwork(NewShoppingCartActivity.this)) {

                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tv_account:
                // 跳转到结算页
                String cartIds = getCartIds();
                if (TextUtils.isEmpty(cartIds)) {
                    showTips("没有选择商品");
                    return;
                }
                Intent intentSubmit = new Intent(this, SubmitOrderActivity.class);
                intentSubmit.putExtra("cartids", getCartIds());
                startActivity(intentSubmit);
                break;
            case R.id.ll_check_all:
                //全选
                setCheckAll();
                break;
            case R.id.tv_shopping:
                //去逛逛
                Intent intentHome = new Intent(this, NewMainActivity.class);
                intentHome.putExtra("fragid", NewMainActivity.MALL);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;
            case R.id.tv_reload:
                //重新加载
                initData(true);
                break;
            default:
                break;
        }
    }


    /**
     * 获取购物车详细数据
     */
    private void getShoppingCartData(boolean isShow) {
        if (isShow) {
            showPreDialog("正在加载");
        }
        ShoppingCarSourceObject source = new ShoppingCarSourceObject();
        source.setSource("1");
        new RetrofitRequest<ShoppingCartObject>(ApiRequest.getApiShiji().getShoppingCartList(source))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            ShoppingCartObject info = (ShoppingCartObject) msg.obj;
                            if (info != null && info.getList() != null && !info.getList().isEmpty()) {
                                setSuccessView(llContent);
                                mList = info.getList();
                                initShoppingCartInfo();
                                mAdapter.setmList(mList);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                setNullView(llContent);
                                tvShopping.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (!NetUtil.IsInNetwork(NewShoppingCartActivity.this)) {
                                setOffNetView(llContent);
                                return;
                            }
                        }
                        hidePreDialog();
                    }
                });
    }


    /**
     * 默认全选的情况下初始化数据
     */
    private void initShoppingCartInfo() {
        cartsList = new ArrayList<>();

        totalNormalCount = 0;
        totalPrice = 0;
        cbAll.setChecked(true);
        int soldOutCount = 0;
        for (int i = 0; i < mList.size(); i++) {
            ArrayList<ShoppingCartGoods> goodsList = mList.get(i).getGoodses();
            float singleTotalPrice = 0; //官网商品总计
            int soldOut = 0;
            for (int j = 0; j < goodsList.size(); j++) {
                CartsInfo cartsInfo = new CartsInfo();
                cartsInfo.setCartId(goodsList.get(j).getCartId());
                cartsInfo.setSkuId(goodsList.get(j).getSkuId());
                cartsInfo.setNum(goodsList.get(j).getNum());
                cartsList.add(cartsInfo);

                if (goodsList.get(j).getStatus() == 1) {
                    goodsList.get(j).setChecked(true);
                    totalNormalCount += goodsList.get(j).getNum();
                    singleTotalPrice += (Float.parseFloat(goodsList.get(j).getPrice()) * goodsList.get(j).getNum());
                } else {
                    //已售罄
                    goodsList.get(j).setChecked(false);
                    soldOut += 1;
                }
            }
            if (soldOut == goodsList.size()) {
                mList.get(i).setSoldOut(true);
                mList.get(i).setChecked(false);
                soldOutCount += 1;
            } else {
                mList.get(i).setSoldOut(false);
                mList.get(i).setChecked(true);
            }
            if (mList.get(i).getDiscount_fee() != null && singleTotalPrice >= Float.parseFloat(mList.get(i).getDiscount_fee().getMoney())) {
                singleTotalPrice -= Float.parseFloat(mList.get(i).getDiscount_fee().getDiscount());
            }
            totalPrice += singleTotalPrice;
        }
        tvAmount.setText(Util.subZeroAndDot(String.valueOf(totalPrice)));
        tvAccount.setText("结算(" + totalNormalCount + ")");

        cbAll.setChecked(!(soldOutCount == mList.size()));
        llCheckAll.setEnabled(!(soldOutCount == mList.size()));
    }


    /**
     * 获取购物车数量
     */
    private void getCarCount() {
        new RetrofitRequest<CarCountInfo>(ApiRequest.getApiShiji().getCarCount()).handRequest(new MsgCallBack() {
            @Override
            public void onResult(HttpMessage msg) {
                if (msg.isSuccess()) {
                    CarCountInfo info = (CarCountInfo) msg.obj;
                    tvTitle.setText("购物车 (" + info.getCount() + ")");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(isInit);
        isInit = false;
    }


    /**
     * 全选
     */
    private void setCheckAll() {
        totalNormalCount = 0;
        totalPrice = 0;
        if (cbAll.isChecked()) {
            //取消全选
            cbAll.setChecked(false);
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setChecked(false);
                ArrayList<ShoppingCartGoods> goodsList = mList.get(i).getGoodses();
                for (int j = 0; j < goodsList.size(); j++) {
                    goodsList.get(j).setChecked(false);
                }
            }
        } else {
            //全选
            cbAll.setChecked(true);
            for (int i = 0; i < mList.size(); i++) {
                if (!mList.get(i).isSoldOut()) {
                    mList.get(i).setChecked(true);
                }
                ArrayList<ShoppingCartGoods> goodsList = mList.get(i).getGoodses();
                float singleTotalPrice = 0;
                for (int j = 0; j < goodsList.size(); j++) {
                    if (goodsList.get(j).getStatus() == 1) {
                        goodsList.get(j).setChecked(true);
                        totalNormalCount += goodsList.get(j).getNum();
                        singleTotalPrice += (Float.parseFloat(goodsList.get(j).getPrice()) * goodsList.get(j).getNum());
                    }
                }

                if (mList.get(i).getDiscount_fee() != null && singleTotalPrice >= Float.parseFloat(mList.get(i).getDiscount_fee().getMoney())) {
                    singleTotalPrice -= Float.parseFloat(mList.get(i).getDiscount_fee().getDiscount());
                }
                totalPrice += singleTotalPrice;
            }
        }
        mAdapter.notifyDataSetChanged();
        tvAmount.setText(Util.subZeroAndDot(String.valueOf(totalPrice)));
        tvAccount.setText("结算(" + totalNormalCount + ")");
    }

    /**
     * 获取cartIds
     *
     * @return
     */
    private String getCartIds() {
        String cartIds = "";
        for (int i = 0; i < mList.size(); i++) {
            ArrayList<ShoppingCartGoods> goodsList = mList.get(i).getGoodses();
            for (int j = 0; j < goodsList.size(); j++) {
                if (goodsList.get(j).isChecked()) {
                    cartIds += goodsList.get(j).getCartId() + ",";
                }
            }
        }
        if (TextUtils.isEmpty(cartIds)) return "";
        return (cartIds.substring(0, cartIds.length() - 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                initData(true);
                Util.getNewUserPullLayer(this, data);
            }
        } else {
            onBackPressed();
        }
    }

    @Override
    public void gotoCustomService() {
        showPreDialog("正在加载客服系统");
        YiChuangYun.openKF5(YiChuangYun.KF5Chat, this, 0, "", new YiChuangYun.onFinishInitListener() {
            @Override
            public void onFinishInit() {
                hidePreDialog();
            }
        });
    }

    @Override
    public void gotoReturnRules() {
        Intent intentAccept = new Intent(this, AcceptActivity.class);
        intentAccept.putExtra("type", 1);
        startActivity(intentAccept);
    }

    @Override
    public void gotoSiteMall(String siteId) {
        Intent intent = new Intent(this, NewMallGoodsActivity.class);
        intent.putExtra("id", Integer.valueOf(siteId));
        startActivity(intent);
    }

    @Override
    public void checkAll() {
        totalNormalCount = 0;
        checkNormalCount = 0;
        totalPrice = 0;
        getCheckShoppingCart();
    }

    private void getCheckShoppingCart() {
        if (mAdapter.getItemCount() == 0) {
            setNullView(llContent);
            tvShopping.setVisibility(View.VISIBLE);
            tvTitle.setText("购物车 (0)");
            return;
        }
        int totalNum = 0;
        int soldOut = 0;
        for (int i = 0; i < mList.size(); i++) {
            ArrayList<ShoppingCartGoods> goodsList = mList.get(i).getGoodses();
            float singleTotalPrice = 0; //官网商品总计
            if (mList.get(i).isSoldOut()) {
                soldOut += 1;
            }
            for (int j = 0; j < goodsList.size(); j++) {
                totalNum += goodsList.get(j).getNum();
                if (goodsList.get(j).getStatus() == 1) {
                    totalNormalCount += goodsList.get(j).getNum();
                    if (goodsList.get(j).isChecked()) {
                        checkNormalCount += goodsList.get(j).getNum();
                        singleTotalPrice += (Float.parseFloat(goodsList.get(j).getPrice()) * goodsList.get(j).getNum());
                    }
                }
            }
            if (mList.get(i).getDiscount_fee() != null && singleTotalPrice >= Float.parseFloat(mList.get(i).getDiscount_fee().getMoney())) {
                singleTotalPrice -= Float.parseFloat(mList.get(i).getDiscount_fee().getDiscount());
            }
            totalPrice += singleTotalPrice;
        }

        tvAmount.setText(Util.subZeroAndDot(String.valueOf(totalPrice)));
        tvAccount.setText("结算(" + checkNormalCount + ")");
        tvTitle.setText("购物车 (" + totalNum + ")");
        if (checkNormalCount < totalNormalCount) {
            cbAll.setChecked(false);
        } else {
            cbAll.setChecked(true);
        }
        if (soldOut == mList.size()) {
            cbAll.setChecked(false);
            llCheckAll.setEnabled(false);
        }
    }


    /**
     * 记录购物车商品的skuid cartid num
     * 在修改购物车商品时用到
     */
    public class CartsInfo {
        String cartId;
        String skuId;
        int num;

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

}
