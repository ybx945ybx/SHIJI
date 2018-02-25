package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by jerry on 2016/2/25.
 */
public class OrderListInfo {
    private ArrayList<Goods> goods;
    private String current_time;
    private String create_time;
    private String close_time;
    private String order_number;
    private String sub_order_number;
    private String status;
    private String status_des;
    private GroupInfo group_info;
    private String amount; //商品金额
    private long diff;

    //店铺订单新增
//    "shop_name":'小柿集'  //店铺名
//    "shop_image":'xxx'   //店铺头像
//    "shop_id": '1'  //店铺id
//    "consignee_name": "dfjfje",   //收货人
//    "cash_amount": 100,    //佣金
//    "goods_num": 1    //商品总数量
    private String shop_name;
    private String shop_image;
    private String shop_id;
    private String consignee_name;
    private String cash_amount;
    private int goods_num;


    public long getDiff() {
        return diff;
    }

    public void setDiff(long diff) {
        this.diff = diff;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getSub_order_number() {
        return sub_order_number;
    }

    public void setSub_order_number(String sub_order_number) {
        this.sub_order_number = sub_order_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_des() {
        return status_des;
    }

    public void setStatus_des(String status_des) {
        this.status_des = status_des;
    }

    public GroupInfo getGroup_info() {
        return group_info;
    }

    public void setGroup_info(GroupInfo group_info) {
        this.group_info = group_info;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(String cash_amount) {
        this.cash_amount = cash_amount;
    }

    public String getConsignee_name() {
        return consignee_name;
    }

    public void setConsignee_name(String consignee_name) {
        this.consignee_name = consignee_name;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }
}
