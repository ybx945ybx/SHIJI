package cn.yiya.shiji.entity;

import java.util.ArrayList;

import cn.yiya.shiji.entity.navigation.CouponDetailInfo;

/**
 * Created by jerry on 2016/4/21.
 */
public class CountryCouponList {
    private String country_name;
    private ArrayList<CouponDetailInfo> coupon_list;

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public ArrayList<CouponDetailInfo> getCoupon_list() {
        return coupon_list;
    }

    public void setCoupon_list(ArrayList<CouponDetailInfo> coupon_list) {
        this.coupon_list = coupon_list;
    }
}
