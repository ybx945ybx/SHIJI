package cn.yiya.shiji.entity;

import java.util.List;

/**
 * 邀请开店页面信息
 * Created by Amy on 2016/10/24.
 */

public class ShopCommissionInfo {

    /**
     * seller : [{"shop_name":"小野鸡炖蘑菇","mobile":"12345678900","commission":"2132"}]
     * seller_num : 3               //   邀请卖家数
     * wages : 1200                   //    累计工资
     * "shop_price" : 200      //开店费用
     * "share_profit"  88      //邀请收入
     */

    private int seller_num;
    private float wages;
    private float shop_price;
    private float share_profit;
    /**
     * shop_name : 小野鸡炖蘑菇
     * mobile : 12345678900
     * commission : 2132           // 提成
     */

    private List<SellerEntity> seller;  //卖家列表

    public int getSeller_num() {
        return seller_num;
    }

    public void setSeller_num(int seller_num) {
        this.seller_num = seller_num;
    }

    public float getWages() {
        return wages;
    }

    public void setWages(float wages) {
        this.wages = wages;
    }

    public float getShop_price() {
        return shop_price;
    }

    public void setShop_price(float shop_price) {
        this.shop_price = shop_price;
    }

    public float getShare_profit() {
        return share_profit;
    }

    public void setShare_profit(float share_profit) {
        this.share_profit = share_profit;
    }

    public List<SellerEntity> getSeller() {
        return seller;
    }

    public void setSeller(List<SellerEntity> seller) {
        this.seller = seller;
    }

    public static class SellerEntity {
        private String shop_name;
        private String mobile;
        private String commission;

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }
    }


}
