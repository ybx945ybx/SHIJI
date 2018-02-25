package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by yiya on 2015/9/2.
 */
public class ShoppingCartGroup {
    boolean isChecked;
    String country;
    String country_flag;
    String site;
    String site_id;
    ShoppingFee fees;
    int delivery;
    String delivery_des;
    String fee;
    String siteDes;
    String free_fee;
    String total_fee;
    ArrayList<FreightInfo> taxs;
    float weight;
    DiscountEntity discount_fee;

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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean isCheck) {
        this.isChecked = isCheck;
    }

    public ShoppingFee getFees() {
        return fees;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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

    public DiscountEntity getDiscount_fee() {
        return discount_fee;
    }

    public void setDiscount_fee(DiscountEntity discount_fee) {
        this.discount_fee = discount_fee;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public static class DiscountEntity {
        String money;
        String discount;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}
