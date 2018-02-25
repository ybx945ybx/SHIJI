package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by chenjian on 2015/9/29.
 */
public class PayOrderNoPost {
    ArrayList<ChannelInfo> channels;
    String format;
    int addressId;                  // 用户没有选择地址的时候不传该字段
    int redPackageId;               // 红包编号
    int score;                      // 积分
    String recommendUser;
    String data_source;             // 1,表示服务器的购物车 0，本地的购物车
    String shopping_cart_ids;       // 商品的cartid
    String source;                  // 来源
    String data;
    ArrayList<ShoppingCartPost> carts;
    private int balance;  //0:不使用余额，1:使用余额

    public ArrayList<ShoppingCartPost> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<ShoppingCartPost> carts) {
        this.carts = carts;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData_source() {
        return data_source;
    }

    public void setData_source(String data_source) {
        this.data_source = data_source;
    }

    public String getShopping_cart_ids() {
        return shopping_cart_ids;
    }

    public void setShopping_cart_ids(String shopping_cart_ids) {
        this.shopping_cart_ids = shopping_cart_ids;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getRedPackageId() {
        return redPackageId;
    }

    public void setRedPackageId(int redPackageId) {
        this.redPackageId = redPackageId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRecommendUser() {
        return recommendUser;
    }

    public void setRecommendUser(String recommendUser) {
        this.recommendUser = recommendUser;
    }

    public ArrayList<ChannelInfo> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<ChannelInfo> channels) {
        this.channels = channels;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
