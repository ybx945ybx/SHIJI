package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by yiya on 2015/9/17.
 */
public class ShoppingCartList {
    ArrayList<ShoppingCartGoods> goodses;
    String country_id;
    String country;
    String country_flag;
    String site_id;
    String site;
    String siteTag;
    String siteDes;
    int delivery;
    String delivery_des;
    String channel_id;
    String fee_num;
    String free_fee;
    String total_fee;
    String fee;
    ShoppingFee fees;
    ShoppingCartGroup.DiscountEntity discount_fee;
    int is_show_discount_fee; //1 不满足满减   2 满足满减
    ArrayList<FreightInfo> taxs;
    boolean isChecked;
    boolean isSoldOut;


    public String getSiteTag() {
        return siteTag;
    }

    public void setSiteTag(String siteTag) {
        this.siteTag = siteTag;
    }

    public ArrayList<ShoppingCartGoods> getGoodses() {
        return goodses;
    }

    public void setGoodses(ArrayList<ShoppingCartGoods> goodses) {
        this.goodses = goodses;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry_flag() {
        return country_flag;
    }

    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }

    public ShoppingFee getFees() {
        return fees;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setFees(ShoppingFee fees) {
        this.fees = fees;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getDelivery_des() {
        return delivery_des;
    }

    public void setDelivery_des(String delivery_des) {
        this.delivery_des = delivery_des;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFree_fee() {
        return free_fee;
    }

    public void setFree_fee(String free_fee) {
        this.free_fee = free_fee;
    }

    public ArrayList<FreightInfo> getTaxs() {
        return taxs;
    }

    public void setTaxs(ArrayList<FreightInfo> taxs) {
        this.taxs = taxs;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSiteDes() {
        return siteDes;
    }

    public void setSiteDes(String siteDes) {
        this.siteDes = siteDes;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public ShoppingCartGroup.DiscountEntity getDiscount_fee() {
        return discount_fee;
    }

    public void setDiscount_fee(ShoppingCartGroup.DiscountEntity discount_fee) {
        this.discount_fee = discount_fee;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getFee_num() {
        return fee_num;
    }

    public void setFee_num(String fee_num) {
        this.fee_num = fee_num;
    }

    public int getIs_show_discount_fee() {
        return is_show_discount_fee;
    }

    public void setIs_show_discount_fee(int is_show_discount_fee) {
        this.is_show_discount_fee = is_show_discount_fee;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}