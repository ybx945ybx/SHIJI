package cn.yiya.shiji.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.yiya.shiji.R;
import cn.yiya.shiji.activity.BaseAppCompatActivity;
import cn.yiya.shiji.activity.HtmlActicity;
import cn.yiya.shiji.activity.NewGoodsDetailActivity;
import cn.yiya.shiji.activity.NewShoppingCartActivity;
import cn.yiya.shiji.activity.SubmitOrderActivity;
import cn.yiya.shiji.adapter.GoodsBuyAdapter;
import cn.yiya.shiji.business.ApiRequest;
import cn.yiya.shiji.business.HttpMessage;
import cn.yiya.shiji.business.MsgCallBack;
import cn.yiya.shiji.business.RetrofitRequest;
import cn.yiya.shiji.business.YiChuangYun;
import cn.yiya.shiji.entity.CartsEntity;
import cn.yiya.shiji.entity.GoodsDetailEntity;
import cn.yiya.shiji.entity.GoodsParamsInfo;
import cn.yiya.shiji.entity.GoodsSortInfo;
import cn.yiya.shiji.entity.HtmlImageListItem;
import cn.yiya.shiji.entity.HtmlImageListObject;
import cn.yiya.shiji.entity.ShoppingCarSourceObject;
import cn.yiya.shiji.entity.ShoppingCartGoods;
import cn.yiya.shiji.entity.ShoppingCartPost;
import cn.yiya.shiji.entity.ShoppingCartPostObject;
import cn.yiya.shiji.entity.ShoppingCartSku;
import cn.yiya.shiji.entity.SizeTableEntity;
import cn.yiya.shiji.utils.SimpleUtils;
import cn.yiya.shiji.utils.Util;

import static cn.yiya.shiji.utils.Util.subZeroAndDot;

/**
 * 商品详情和购物车的商品弹层
 * Created by Amy on 2016/11/10.
 */

public class GoodsBuyDialog extends Dialog implements View.OnClickListener {
    private View mView;
    private BaseAppCompatActivity mContext;

    private JsonObject goodsJson;
    private GoodsDetailEntity goodsDetailEntity;

    private JsonElement skusJson;
    private JSONArray skuArray = null;//skusJson->sku array

    private GoodsDetailEntity.SizeEntity sizeEntity;
    private List<GoodsDetailEntity.SizeEntity.TablesEntity> tables;

    private int type; //1 商品详情直接购买  2 商品详情加入购物车  3购物车修改商品
    private ShoppingCartGoods cartGoods;// 购物车修改 商品的信息
    private ArrayList<NewShoppingCartActivity.CartsInfo> cartsList;

    private SimpleDraweeView ivGoodsCover;
    private TextView tvPrice, tvNum, tvChooseDesc;

    //size color width inseam specialsizetype
    private AllListView rvCheckParams;
    private GoodsBuyAdapter paramsAdapter;

    private TextView tvMinus, tvAdd, tvCount;
    private TextView tvCustomService;
    private TextView tvSubmit;

    private int txtCount = 1;//商品数量
    int matchPos = -1;  //匹配尺码 的坐标
    int checkColorPos = 0;  //最后点击的颜色 坐标
    String skuId = "";
    String toast = "请选择";

    private boolean isGroupBuy = false;
    private int groupbuyPrice = 0;//团购价格

    private ArrayList<GoodsParamsInfo> paramsInfos = new ArrayList<>(); //商品size等参数信息集合
    private LinkedHashMap<String, CheckKeyEntity> keyposMap = new LinkedHashMap<>(); //size等参数在 paramsInfos中对应坐标集合
    private HashMap<String, String> initValueMap;//修改商品 size等参数初始的信息集合
    private ArrayList<SizeTableEntity> tableList;//size  每个size匹配的尺码表
    private LinkedHashMap<String, ArrayList<String>> skuValueMap = new LinkedHashMap<>();//sku所有属性值集合

    /**
     * 商品详情跳转过来
     *
     * @param context
     * @param goodsJson
     * @param skusJson
     * @param type
     */
    public GoodsBuyDialog(BaseAppCompatActivity context, JsonObject goodsJson, JsonElement skusJson, int type, boolean isGroupbuy) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;
        this.type = type;
        this.isGroupBuy = isGroupbuy;

        this.goodsJson = goodsJson;
        this.goodsDetailEntity = new Gson().fromJson(goodsJson, GoodsDetailEntity.class);
        this.skusJson = skusJson;

        if (!goodsJson.get("size").toString().equals("false")) {
            this.sizeEntity = new Gson().fromJson(goodsJson.get("size"), GoodsDetailEntity.SizeEntity.class);
            this.tables = sizeEntity.getTables();
        }

        this.cartGoods = null;
        this.cartsList = null;
        this.txtCount = cartGoods == null ? 1 : cartGoods.getNum();

        skuArray = getSkuArray();
        initCheckedSku();
        initMatchSizeTable();
        initSortInfo();
    }

    /**
     * 购物车跳转过来
     *
     * @param context
     * @param goodsJson
     * @param cartGoods
     * @param cartsList
     */
    public GoodsBuyDialog(BaseAppCompatActivity context, JsonObject goodsJson, ShoppingCartGoods cartGoods, ArrayList<NewShoppingCartActivity.CartsInfo> cartsList) {
        super(context, R.style.ActionSheetDialogStyle);
        this.mContext = context;

        this.goodsJson = goodsJson;
        this.goodsDetailEntity = new Gson().fromJson(goodsJson, GoodsDetailEntity.class);
        this.skusJson = null;

        if (!goodsJson.get("size").toString().equals("false")) {
            this.sizeEntity = new Gson().fromJson(goodsJson.get("size"), GoodsDetailEntity.SizeEntity.class);
            this.tables = sizeEntity.getTables();
        }

        this.type = 3;
        this.cartGoods = cartGoods;
        this.cartsList = cartsList;
        this.txtCount = cartGoods == null ? 1 : cartGoods.getNum();

        skuArray = getSkuArray();
        initCheckedSku();
        initMatchSizeTable();
        initSortInfo();
    }

    @SuppressLint({"RtlHardcoded", "InflateParams"})
    public Dialog build() {

        initView();
        initData();
        initEvent();

        setContentView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        return this;
    }

    /**
     * 初始化得到sku键值对列表，即skuArray
     */
    private JSONArray getSkuArray() {
        JSONArray jsonArray = null;
        if (skusJson == null) {
            skusJson = goodsJson.get("skus");
        }
        String strjson = skusJson.toString();
        try {
            jsonArray = new JSONArray(strjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    /**
     * 购物车修改商品 先获取商品当前在购物车中的sku参数的集合,即initValueMap
     */
    private void initCheckedSku() {
        initValueMap = new HashMap<>();
        if (cartGoods != null) {
            ArrayList<ShoppingCartSku> initSku = cartGoods.getSku();
            for (int i = 0; i < initSku.size(); i++) {
                initValueMap.put(initSku.get(i).getKey(), initSku.get(i).getValue());
            }
        }
    }

    /**
     * 根据size—>header初始化每个size匹配的尺码表
     */
    private void initMatchSizeTable() {
        if (sizeEntity == null) return;

        String keyDesc = sizeEntity.getKey_des();
        String ourSize = sizeEntity.getOur_size();
        if (TextUtils.isEmpty(keyDesc) || TextUtils.isEmpty(ourSize)) return;

        tableList = new ArrayList<>();
        List<String> header = tables.get(0).getHeader();

        for (int i = 0; i < header.size(); i++) {
            SizeTableEntity sizeTableEntity = new SizeTableEntity();
            sizeTableEntity.setName(header.get(i));

            //获取匹配尺码的在列表中的下标
            if (header.get(i).equals(ourSize)) {
                matchPos = i;
            }

            //设置mark 用来判断背景是否为灰色
            if (header.get(i).equals(keyDesc)) {
                sizeTableEntity.setMark(true);
            }
            tableList.add(sizeTableEntity);
        }
    }

    /**
     * 得到每个size匹配的尺码表
     *
     * @param sizevalue
     * @return
     */
    private ArrayList<SizeTableEntity> getMatchSizeTable(String sizevalue) {
        if (matchPos == -1) return null;

        ArrayList<SizeTableEntity> copyList = Util.getDeepClone(tableList);
        boolean isMatch = false;

        for (int i = 0; i < tables.get(0).getData().size(); i++) {
            if (sizevalue.equals(tables.get(0).getData().get(i).get(matchPos))) {
                isMatch = true;
                List<String> item = tables.get(0).getData().get(i);
                for (int j = 0; j < copyList.size(); j++) {
                    copyList.get(j).setValue(item.get(j));
                }
                break;
            }
        }
        if (!isMatch) return null;

        copyList.remove(matchPos);
        return copyList;
    }

    /**
     * ☆☆☆ 初始化商品参数数据
     */
    private void initSortInfo() {
        if (goodsDetailEntity == null) return;

        GoodsDetailEntity.GoodsEntity goods = goodsDetailEntity.getGoods();

        for (int i = 0; i < goods.getDimensions().size(); i++) {
            String key = goods.getDimensions().get(i);

            CheckKeyEntity keyEntity = new CheckKeyEntity();
            keyEntity.setParam_pos(i);
            keyEntity.setCheck_pos(-1);

            GoodsParamsInfo paramsInfo = new GoodsParamsInfo();
            paramsInfo.setKey(key);
            if (key.equals("size")) {
                paramsInfo.setSize(sizeEntity);
                paramsInfo.setSize_match_pos(matchPos);
            }

            //获取属性值
            ArrayList<String> skusParam = new ArrayList<>(); //sku中对应当前key的值的集合
            for (int j = 0; j < skuArray.length(); j++) {
                try {
                    JSONObject sku = skuArray.getJSONObject(j);
                    if (isGroupBuy && sku.has("groupbuy_price") && !TextUtils.isEmpty(sku.get("groupbuy_price").toString()) && groupbuyPrice == 0) {
                        groupbuyPrice = Integer.valueOf(sku.get("groupbuy_price").toString());//获取第一个团购价格 目前默认团购价格不变
                    }
                    if (!skusParam.contains(sku.getString(key))) {
                        skusParam.add(sku.getString(key));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            skuValueMap.put(key, skusParam);

            ArrayList<GoodsSortInfo> paramsList = new ArrayList<>();
            if (key.equals("color")) {
                //颜色 全部加进来
                List<GoodsDetailEntity.GoodsColorsEntity> goodsColors = goodsDetailEntity.getGoods_colors();

                if (goodsColors == null || goodsColors.size() == 0) {
                    GoodsSortInfo sInfo = new GoodsSortInfo();
                    String skuValue = "无在售" + key;
                    sInfo.setName(skuValue);
                    sInfo.setTitle(skuValue);
                    sInfo.setImages(goodsColors.get(0).getImages());
                    sInfo.setMin_price(goodsColors.get(0).getMin_price());
                    sInfo.setMax_price(goodsColors.get(0).getMax_price());
                    sInfo.setValid(false);
                    sInfo.setChecked(false);
                    paramsList.add(sInfo);

                } else {
                    for (int j = 0; j < goodsColors.size(); j++) {
                        GoodsSortInfo sInfo = new GoodsSortInfo();
                        sInfo.setChecked(false);
                        sInfo.setTitle(goodsColors.get(j).getO_name());
                        sInfo.setName(goodsColors.get(j).getName());
                        sInfo.setCover(goodsColors.get(j).getCover());
                        sInfo.setImages(goodsColors.get(j).getImages());
                        sInfo.setMin_price(isGroupBuy ? groupbuyPrice : goodsColors.get(j).getMin_price());
                        sInfo.setMax_price(isGroupBuy ? groupbuyPrice : goodsColors.get(j).getMax_price());
                        sInfo.setValid(skusParam.contains(goodsColors.get(j).getName())); //是否有效

                        String initValue = initValueMap.get(key);
                        if (!TextUtils.isEmpty(initValue) && initValue.equals(sInfo.getTitle()) && sInfo.isValid()) {
                            sInfo.setChecked(true);
                            keyEntity.setCheck_pos(paramsList.size());
                            keyEntity.setCheck_title(sInfo.getTitle());
                            keyEntity.setCheck_name(sInfo.getName());
                            checkColorPos = paramsList.size();
                        }
                        paramsList.add(sInfo);
                    }
                }

            } else {
                //size width等
                if (skusParam.size() > 0) {
                    for (int h = 0; h < skusParam.size(); h++) {
                        GoodsSortInfo sInfo = new GoodsSortInfo();
                        String skuValue = skusParam.get(h);

                        sInfo.setValid(true);
                        sInfo.setChecked(false);
                        sInfo.setName(skuValue);
                        sInfo.setTitle(skuValue);

                        if (key.equals("size")) {
                            sInfo.setSizemap(getMatchSizeTable(skuValue));
                        }

                        String initValue = initValueMap.get(key);
                        if (!TextUtils.isEmpty(initValue) && initValue.equals(sInfo.getTitle())) {
                            sInfo.setChecked(true);
                            keyEntity.setCheck_pos(paramsList.size());
                            keyEntity.setCheck_title(sInfo.getTitle());
                            keyEntity.setCheck_name(sInfo.getName());
                        }
                        paramsList.add(sInfo);
                    }
                } else {
                    GoodsSortInfo sInfo = new GoodsSortInfo();
                    String skuValue = "无在售" + key;
                    sInfo.setName(skuValue);
                    sInfo.setTitle(skuValue);
                    sInfo.setValid(false);
                    sInfo.setChecked(false);
                    paramsList.add(sInfo);
                }
            }
            paramsInfo.setValueList(paramsList);
            paramsInfos.add(paramsInfo);
            keyposMap.put(key, keyEntity);
        }

    }

    /**
     * 初始化布局
     */
    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_new_goods_buy, null);

        ivGoodsCover = (SimpleDraweeView) mView.findViewById(R.id.iv_goods_cover);
        tvPrice = (TextView) mView.findViewById(R.id.tv_price);
        tvNum = (TextView) mView.findViewById(R.id.tv_num);
        tvChooseDesc = (TextView) mView.findViewById(R.id.tv_choose_desc);

        rvCheckParams = (AllListView) mView.findViewById(R.id.rv_check_params);

        tvMinus = (TextView) mView.findViewById(R.id.tv_minus);
        tvAdd = (TextView) mView.findViewById(R.id.tv_add);
        tvCount = (TextView) mView.findViewById(R.id.tv_count);

        tvCustomService = (TextView) mView.findViewById(R.id.tv_custom_service);
        tvSubmit = (TextView) mView.findViewById(R.id.tv_submit);
    }

    /**
     * 初始化缩略图 价格 数量 等
     */
    private void initData() {
        setColorImagePrice();
        tvNum.setText(String.valueOf(txtCount));
        tvCount.setText(String.valueOf(txtCount));

        paramsAdapter = new GoodsBuyAdapter(mContext, paramsInfos);
        rvCheckParams.setAdapter(paramsAdapter);
        setCheckChangeState();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ivGoodsCover.setOnClickListener(this);
        tvMinus.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvCustomService.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        paramsAdapter.setOnCheckListener(new GoodsBuyAdapter.OnCheckListener() {
            @Override
            public void onCheck(int paramPosition, int checkPosition) {
                checkParams(paramPosition, checkPosition);
            }
        });
    }

    /**
     * 属性点击事件
     *
     * @param paramPosition paramsInfos 属性的position  判断是size color 或其他
     * @param checkPosition 具体属性点击的Position
     */
    private void checkParams(int paramPosition, int checkPosition) {
        // 获取点击的是什么参数属性
        ArrayList<GoodsSortInfo> paramList = paramsInfos.get(paramPosition).getValueList();
        String paramKey = paramsInfos.get(paramPosition).getKey();
        String paramValue = paramList.get(checkPosition).getName();

        //设置keyposMap相应的值
        if (paramList.get(checkPosition).isChecked()) {
            keyposMap.get(paramKey).setCheck_pos(checkPosition);
            keyposMap.get(paramKey).setCheck_title(paramList.get(checkPosition).getTitle());
            keyposMap.get(paramKey).setCheck_name(paramValue);
            if (paramKey.equals("color")) {
                checkColorPos = checkPosition;
                GoodsSortInfo sortInfo = paramsInfos.get(keyposMap.get("color").getParam_pos()).getValueList().get(checkColorPos);
                String imgUrl = sortInfo.getImages().get(0).getImage();
                ivGoodsCover.setImageURI(Util.ScaleImageGoodes(imgUrl, SimpleUtils.dp2px(mContext, 90)));
            }
        } else {
            keyposMap.get(paramKey).setCheck_pos(-1);
            keyposMap.get(paramKey).setCheck_title("");
            keyposMap.get(paramKey).setCheck_name("");
        }

        //复制skuValueMap
        HashMap<String, ArrayList<String>> valueMap = Util.getDeepClone(skuValueMap);

        //循环剔除valueMap中无效的属性值
        for (String key : keyposMap.keySet()) {
            //根据选中的属性进行剔除
            if (keyposMap.get(key).getCheck_pos() > -1) {

                HashMap<String, ArrayList<String>> validMap = new HashMap<>();
                for (int i = 0; i < paramsInfos.size(); i++) {
                    validMap.put(paramsInfos.get(i).getKey(), new ArrayList<String>());
                }

                //先根据选择的属性获取它的有效值集合validMap
                for (int j = 0; j < skuArray.length(); j++) {
                    try {
                        JSONObject sku = skuArray.getJSONObject(j);
                        if (keyposMap.get(key).getCheck_name().equals(sku.get(key))) {
                            for (String key1 : keyposMap.keySet()) {
                                if (!validMap.get(key1).contains(sku.getString(key1))) {
                                    validMap.get(key1).add(sku.getString(key1));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //根据有效值集合 判断是否是有效值  不是则剔除
                for (String key2 : valueMap.keySet()) {
                    if (key2.equals(key)) continue;

                    ArrayList<String> valueList = Util.getDeepClone(valueMap.get(key2));
                    for (int j = 0; j < valueList.size(); j++) {
                        if (!validMap.get(key2).contains(valueList.get(j))) {
                            valueMap.get(key2).remove(valueList.get(j));
                        }
                    }
                }
            }
        }

        //最后的valueMap就是所有有效的属性值的集合
        for (String key : valueMap.keySet()) {
            ArrayList<GoodsSortInfo> list = paramsInfos.get(keyposMap.get(key).getParam_pos()).getValueList();
            for (int j = 0; j < list.size(); j++) {
                list.get(j).setValid(true);
                if (!valueMap.get(key).contains(list.get(j).getName())) {
                    list.get(j).setValid(false);
                }
            }
        }

        //notify GoodsCheckParamsAdapter
        paramsAdapter.notifyDataSetChanged();
        setCheckChangeState();
    }

    /**
     * 设置选中的颜色的缩略图和价格
     */
    private void setColorImagePrice() {
        GoodsSortInfo sortInfo = paramsInfos.get(keyposMap.get("color").getParam_pos()).getValueList().get(checkColorPos);
        String imgUrl = sortInfo.getImages().get(0).getImage();
        ivGoodsCover.setImageURI(Util.ScaleImageGoodes(imgUrl, SimpleUtils.dp2px(mContext, 90)));

        String price = subZeroAndDot(String.valueOf(sortInfo.getMin_price()));
        if (sortInfo.getMin_price() != sortInfo.getMax_price()) {
            price += "~" + subZeroAndDot(String.valueOf(sortInfo.getMax_price()));
        }
        tvPrice.setText(price);
    }

    /**
     * 根据check后的数据修改显示在页面上的状态
     */
    private void setCheckChangeState() {
        skuId = "";
        toast = "请选择";
        String strDesc = "已选择";

        JSONArray copySkuArray = getSkuArray();
        for (Map.Entry<String, CheckKeyEntity> entry : keyposMap.entrySet()) {
            String key = entry.getKey();
            CheckKeyEntity keyEntity = entry.getValue();

            if (keyEntity.getCheck_pos() > -1) {
                strDesc += "“" + keyEntity.getCheck_title() + "”、";
                JSONArray array = new JSONArray();
                for (int i = 0; i < copySkuArray.length(); i++) {
                    try {
                        JSONObject sku = copySkuArray.getJSONObject(i);
                        if (sku.get(key).equals(keyEntity.getCheck_name())) {
                            array.put(sku);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                copySkuArray = array;
            } else {
                toast += "“" + key + "”、";
            }
        }

        toast = toast.replaceAll("、+?$", "");
        strDesc = strDesc.replaceAll("、+?$", "");

        if (strDesc.length() > 3) {
            tvChooseDesc.setText(strDesc);

            if (copySkuArray.length() > 0) {
                String currentPrice = "";
                try {
                    currentPrice = !isGroupBuy ? Util.subZeroAndDot(copySkuArray.getJSONObject(0).getString("current_price")) : String.valueOf(groupbuyPrice);
                    skuId = copySkuArray.getJSONObject(0).getString("id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvPrice.setText(currentPrice);
            }
        } else {
            tvChooseDesc.setText("请选择样式尺寸");
            setColorImagePrice();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goods_cover:
                //放大
                enlargeImage();
                break;
            case R.id.tv_minus:
                if (txtCount - 1 == 0) return;
                tvCount.setText(String.valueOf(txtCount -= 1));
                tvNum.setText(String.valueOf(txtCount));
                //数量减
                break;
            case R.id.tv_add:
                tvCount.setText(String.valueOf(txtCount += 1));
                tvNum.setText(String.valueOf(txtCount));
                //数量加
                break;
            case R.id.tv_custom_service:
                //客服
                gotoService();
                break;
            case R.id.tv_submit:
                //直接购买或加入购物车
                gotoPayOrCart();
                break;
            default:
                break;
        }
    }

    /**
     * 确定  直接购买或加入购物车
     */
    private void gotoPayOrCart() {
        if (toast.length() > 3) {
            Util.toast(mContext, toast, true);
            return;
        }

        if (type == 1) {
            //商品详情 直接购买
            if (isGroupBuy) {
                skuId = "g|" + skuId;
            }
            gotoPay(skuId);
        } else if (type == 2) {
            //商品详情 加入购物车
            addToCart(skuId);
        } else if (type == 3) {
            //购物车 修改商品
            checkUpdateCartGoods(skuId);
        }
    }

    /**
     * 直接购买
     *
     * @param skuId
     */
    private void gotoPay(String skuId) {
        Intent intentSubmit = new Intent(mContext, SubmitOrderActivity.class);

        ArrayList<ShoppingCartPost> listCart = new ArrayList<>();
        ShoppingCartPost info = new ShoppingCartPost();
        info.setNum(txtCount);
        info.setSkuId(skuId);
        info.setGoodsId(goodsDetailEntity.getGoods().getId());
        listCart.add(info);

        ShoppingCartPost[] arrCart = new ShoppingCartPost[listCart.size()];
        for (int i = 0; i < listCart.size(); i++) {
            arrCart[i] = listCart.get(i);
        }
        String str = new Gson().toJson(arrCart);
        String str64 = android.util.Base64.encodeToString(str.getBytes(), Base64.DEFAULT);

        ShoppingCartPostObject object = new ShoppingCartPostObject();
        object.setData(str64);
        object.setFormat("base64");
        String json = new Gson().toJson(object);

        intentSubmit.putExtra("json", json);
        intentSubmit.putExtra("directBuy", true);
        mContext.startActivity(intentSubmit);
    }

    /**
     * 加入购物车
     *
     * @param skuId
     */
    private void addToCart(String skuId) {
        ShoppingCarSourceObject cartObject = new ShoppingCarSourceObject();
        ArrayList<CartsEntity> cartsList = new ArrayList<>();
        CartsEntity carts = new CartsEntity();
        carts.setNum(txtCount);
        carts.setSkuId(skuId);
        cartsList.add(carts);
        cartObject.setCarts(cartsList);
        new RetrofitRequest<>(ApiRequest.getApiShiji().addShoppingCartList(cartObject))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            //得到商品图片的坐标（用于计算动画开始的坐标）
                            int startLoc[] = new int[2];
                            ivGoodsCover.getLocationOnScreen(startLoc);
                            if (type == 2) {
                                GoodsSortInfo sortInfo = paramsInfos.get(keyposMap.get("color").getParam_pos()).getValueList().get(checkColorPos);
                                String imgUrl = Util.ScaleImageGoodes(sortInfo.getImages().get(0).getImage(), SimpleUtils.dp2px(mContext, 90));
                                if (mContext.isEffectClick()) {
                                    ((NewGoodsDetailActivity) mContext).addToCartAnimator(startLoc, txtCount, imgUrl);
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 检查购物车中是否已含有与 当前skuid相同，cartid不同的商品
     *
     * @param skuId
     */
    private void checkUpdateCartGoods(String skuId) {
        String lastCartId = cartGoods.getCartId();
        int lastTxtCount = txtCount;
        boolean hasSame = false;
        for (int i = 0; i < cartsList.size(); i++) {
            if (skuId.equals(cartsList.get(i).getSkuId()) && !cartGoods.getCartId().equals(cartsList.get(i).getCartId())) {
                hasSame = true;
                lastCartId = cartsList.get(i).getCartId();
                lastTxtCount += cartsList.get(i).getNum();
                break;
            }
        }

        if (hasSame) {
            deleteGoods(cartGoods.getCartId(), skuId, lastCartId, lastTxtCount);
        } else {
            updateCartGoods(skuId, lastCartId, lastTxtCount);
        }

    }

    /**
     * 删除重复的CartId
     *
     * @param cartId
     */
    private void deleteGoods(String deleteCartId, final String skuId, final String cartId, final int num) {
        new RetrofitRequest<>(ApiRequest.getApiShiji().deleteShoppingCart(deleteCartId))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            updateCartGoods(skuId, cartId, num);
                        } else {
                            Util.toast(mContext, "操作失败", true);
                        }
                    }
                });
    }

    /**
     * 修改购物车商品
     *
     * @param skuId
     */
    private void updateCartGoods(String skuId, String cartId, int num) {
        ShoppingCarSourceObject cartObject = new ShoppingCarSourceObject();
        ArrayList<CartsEntity> cartsList = new ArrayList<>();
        CartsEntity carts = new CartsEntity();
        carts.setId(cartId);
        carts.setNum(num);
        carts.setSkuId(skuId);
        cartsList.add(carts);
        cartObject.setCarts(cartsList);
        new RetrofitRequest<>(ApiRequest.getApiShiji().updateShoppingCartGoods(cartObject))
                .handRequest(new MsgCallBack() {
                    @Override
                    public void onResult(HttpMessage msg) {
                        if (msg.isSuccess()) {
                            dismiss();
                            Util.toast(mContext, "操作成功", true);
                            if (type == 3) {
                                ((NewShoppingCartActivity) mContext).initData(true);
                            }
                        } else {
                            Util.toast(mContext, "操作失败", true);
                        }
                    }
                });
    }

    /**
     * 打开客服
     */
    private void gotoService() {
        mContext.showPreDialog("正在加载客服系统");
        YiChuangYun.openKF5(YiChuangYun.KF5Chat, mContext, YiChuangYun.GOODES_DETAIL, goodsDetailEntity.getGoods().getId(), new YiChuangYun.onFinishInitListener() {
            @Override
            public void onFinishInit() {
                mContext.hidePreDialog();
            }
        });
    }

    /**
     * 放大图层
     */
    private void enlargeImage() {
        Intent intent = new Intent(mContext, HtmlActicity.class);
        HtmlImageListObject htmlobj = new HtmlImageListObject();
        ArrayList<HtmlImageListItem> list = new ArrayList<>();

        GoodsSortInfo sortInfo = paramsInfos.get(keyposMap.get("color").getParam_pos()).getValueList().get(checkColorPos);
        List<GoodsDetailEntity.GoodsColorsEntity.ImagesEntity> colorImages = sortInfo.getImages();
        for (int i = 0; i < colorImages.size(); i++) {
            HtmlImageListItem item = new HtmlImageListItem();
            item.setUrl(colorImages.get(i).getImage());
            list.add(item);
        }
        htmlobj.setList(list);
        intent.putExtra("imageurl", new Gson().toJson(htmlobj));
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.silde_in_center, R.anim.silde_out_border);
    }

    /**
     * 记录参数选择信息
     */
    class CheckKeyEntity implements Serializable {
        int param_pos;
        int check_pos;
        String check_name;
        String check_title;

        public int getParam_pos() {
            return param_pos;
        }

        public void setParam_pos(int param_pos) {
            this.param_pos = param_pos;
        }

        public int getCheck_pos() {
            return check_pos;
        }

        public void setCheck_pos(int check_pos) {
            this.check_pos = check_pos;
        }

        public String getCheck_name() {
            return check_name;
        }

        public void setCheck_name(String check_name) {
            this.check_name = check_name;
        }

        public String getCheck_title() {
            return check_title;
        }

        public void setCheck_title(String check_title) {
            this.check_title = check_title;
        }
    }
}

