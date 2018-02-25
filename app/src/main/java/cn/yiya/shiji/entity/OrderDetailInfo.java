package cn.yiya.shiji.entity;

import java.util.ArrayList;

/**
 * Created by chenjian on 2016/3/3.
 */
public class OrderDetailInfo {

    /**
     * order_number : XXXXXXXXXXXXXXXXXXX
     * time : 2015-10-01 01:01:01
     * current_time : 2016-02-25 16:15:06
     * close_time : 2016-02-25 11:59:53
     * pay_channel : 微信
     * status : 0
     * amount : 1234.01
     * discount : 12
     * display_address:true //显示收件地址：true    不显示收件地址：false
     */

    private String order_number;
    private String time;
    private String current_time;
    private String close_time;
    private String pay_channel;
    private int status;
    private double amount;
    private int discount;
    private OrderAddressInfo address;
    private ArrayList<OrderSubInfo> sub_orders;
    private boolean display_address;

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getOrder_number() {
        return order_number;
    }

    public String getTime() {
        return time;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public int getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }

    public int getDiscount() {
        return discount;
    }

    public OrderAddressInfo getAddress() {
        return address;
    }

    public void setAddress(OrderAddressInfo address) {
        this.address = address;
    }

    public ArrayList<OrderSubInfo> getSub_orders() {
        return sub_orders;
    }

    public void setSub_orders(ArrayList<OrderSubInfo> sub_orders) {
        this.sub_orders = sub_orders;
    }

    public class OrderAddressInfo {
        /**
         * province : 山西省
         * city : 太原市
         * district : 高新区
         * adress : XXX街道
         * name : 王五
         * mobile : 18678890987
         * post_code : 邮编
         * identityNumber : 身份证号码
         */

        private String province;
        private String city;
        private String district;
        private String address;
        private String name;
        private String mobile;
        private String post_code;
        private String identityNumber;

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setAddress(String adress) {
            this.address = address;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setPost_code(String post_code) {
            this.post_code = post_code;
        }

        public void setIdentityNumber(String identityNumber) {
            this.identityNumber = identityNumber;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getPost_code() {
            return post_code;
        }

        public String getIdentityNumber() {
            return identityNumber;
        }
    }

    public boolean isDisplay_address() {
        return display_address;
    }

    public void setDisplay_address(boolean display_address) {
        this.display_address = display_address;
    }
}
